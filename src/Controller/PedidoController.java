package Controller;

import Model.Carrito;
import Model.CarritoDetalle;
import Model.DetalleFactura;
import Model.DetallePedido;
import Model.EstadoPago;
import Model.Factura;
import Model.MetodoPago;
import Model.Pago;
import Model.Pedido;
import Model.TipoEntrega;
import Model.Usuario;

import Service.Implement.CarritoServiceImpl;
import Service.Implement.FacturaServiceImpl;
import Service.Implement.GeneradorFacturaPdf;
import Service.Implement.PagoServiceImpl;
import Service.Implement.PedidoServiceImpl;
import Service.Interfaz.ICarritoService;
import Service.Interfaz.IFacturaService;
import Service.Interfaz.IPagoService;
import Service.Interfaz.IPedidoService;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Orquesta el paso "Confirmar pedido" del wizard de compra
 * (Carrito -> Pago -> Confirmacion -> Factura).
 *
 * Mientras el cliente navega los pasos 1-3, el Carrito vive
 * solo en memoria: no se toca la base de datos. Todo se
 * persiste en un único punto: confirmarPedido().
 *
 * Orden de persistencia (igual al diagrama de secuencia):
 *   1) INSERT pedido + INSERT detalle_pedido (PedidoService)
 *   2) INSERT pago                            (PagoService)
 *   3) INSERT factura                         (FacturaService)
 *   4) Generar el PDF                         (GeneradorFacturaPdf)
 *   5) Vaciar el carrito                      (CarritoService)
 *
 * DEPENDENCIAS QUE NO CONTROLA ESTA CLASE (DAO):
 *  - PedidoDAOImpl.insertar() debe devolver el id generado
 *    (Statement.RETURN_GENERATED_KEYS) y setearlo en el Pedido.
 *    Hoy no lo hace -> pedido.getIdPedido() queda en 0 y el
 *    pago/factura de abajo fallarían al enlazar la FK id_pedido.
 *  - DetallePedidoDAO debe insertar cada línea del pedido
 *    dentro de la misma transacción que el INSERT pedido.
 *    Hoy esa clase está vacía.
 * Si algo de esto falla, el error es de la capa DAO, no de este
 * Controller: aquí solo se asume el contrato de IPedidoService.
 * ===============================================================
 */
public class PedidoController {

    private final ICarritoService carritoService;
    private final IPedidoService pedidoService;
    private final IPagoService pagoService;
    private final IFacturaService facturaService;
    private final GeneradorFacturaPdf generadorFacturaPdf;

    public PedidoController() {
        this.carritoService = new CarritoServiceImpl();
        this.pedidoService = new PedidoServiceImpl();
        this.pagoService = new PagoServiceImpl();
        this.facturaService = new FacturaServiceImpl();
        this.generadorFacturaPdf = new GeneradorFacturaPdf();
    }

    // Constructor alterno para pruebas (inyectar servicios falsos/mocks)
    public PedidoController(ICarritoService carritoService,
                             IPedidoService pedidoService,
                             IPagoService pagoService,
                             IFacturaService facturaService,
                             GeneradorFacturaPdf generadorFacturaPdf) {
        this.carritoService = carritoService;
        this.pedidoService = pedidoService;
        this.pagoService = pagoService;
        this.facturaService = facturaService;
        this.generadorFacturaPdf = generadorFacturaPdf;
    }

    /**
     * Punto de entrada único del Paso 3 -> Paso 4 del wizard.
     * Se llama cuando el cliente presiona "Confirmar pedido".
     *
     * @param carrito        carrito activo del cliente (Paso 1)
     * @param tipoEntrega    elegido en el Paso 2
     * @param metodoPago     elegido en el Paso 2
     * @param montoRecibido  solo se valida si metodoPago = EFECTIVO
     */
    public ResultadoConfirmacion confirmarPedido(Carrito carrito,
                                                  TipoEntrega tipoEntrega,
                                                  MetodoPago metodoPago,
                                                  BigDecimal montoRecibido) {

        // ---------- 1. Validaciones de entrada ----------
        if (carrito == null || carrito.estaVacio()) {
            return ResultadoConfirmacion.error("El carrito está vacío.");
        }

        if (carrito.getUsuario() == null) {
            return ResultadoConfirmacion.error("No hay un usuario en sesión.");
        }

        if (tipoEntrega == null || metodoPago == null) {
            return ResultadoConfirmacion.error("Debes elegir tipo de entrega y método de pago.");
        }

        Usuario cliente = carrito.getUsuario();

        // ---------- 2. Armar el Pedido a partir del Carrito (aún nada en BD) ----------
        Pedido pedido = new Pedido();
        pedido.setUsuario(cliente);
        pedido.setTipoEntrega(tipoEntrega);
        pedido.setDescuento(BigDecimal.ZERO); // TODO: aplicar Promocion aquí si corresponde

        for (CarritoDetalle detalleCarrito : carrito.getDetalles()) {
            pedido.agregarDetalle(
                    detalleCarrito.getProducto(),
                    detalleCarrito.getCantidad()
            );
        }

        if (!pedido.tieneProductos()) {
            return ResultadoConfirmacion.error("El pedido no tiene productos.");
        }

        // ---------- 3. Validar el monto si el pago es en efectivo ----------
        if (metodoPago == MetodoPago.EFECTIVO) {
            if (montoRecibido == null || montoRecibido.compareTo(pedido.getTotal()) < 0) {
                return ResultadoConfirmacion.error(
                        "El monto recibido (Q" + montoRecibido + ") es menor al total (Q" + pedido.getTotal() + ")."
                );
            }
        }

        // ---------- 4. Persistir el pedido: INSERT pedido + INSERT detalle_pedido ----------
        boolean pedidoGuardado = pedidoService.registrarPedido(pedido);

        if (!pedidoGuardado || pedido.getIdPedido() <= 0) {
            return ResultadoConfirmacion.error(
                    "No se pudo registrar el pedido. Revisa PedidoDAO/DetallePedidoDAO."
            );
        }

        // ---------- 5. Registrar el pago ----------
        Pago pago = new Pago();
        pago.setPedido(pedido);
        pago.setMetodoPago(metodoPago);
        pago.setMonto(pedido.getTotal());
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstado(EstadoPago.PAGADO); // kiosco de autoservicio: se cobra al confirmar

        boolean pagoGuardado = pagoService.guardar(pago);

        if (!pagoGuardado) {
            return ResultadoConfirmacion.error(
                    "El pedido #" + pedido.getNumeroOrden()
                            + " se creó, pero no se pudo registrar el pago. Avisa a un trabajador."
            );
        }

        // ---------- 6. Generar la factura ----------
        // El encabezado (Factura) sí se guarda en BD. El detalle
        // (DetalleFactura) NO tiene tabla propia: se arma en memoria
        // a partir de DetallePedido solo para dibujar el PDF.
        Factura factura = new Factura(pedido, cliente);
        factura.setMetodoPago(metodoPago);
        factura.setDireccion(
                tipoEntrega == TipoEntrega.PARA_LLEVAR ? "Para llevar" : "Comer en restaurante"
        );
        // Se usa el número de orden del pedido en vez de generarNumeroFactura(),
        // porque ese método arma el número con idFactura y todavía no existe
        // (la factura no se ha insertado). Así evitamos esa dependencia circular.
        factura.setNumeroFactura("FAC-" + pedido.getNumeroOrden());

        for (DetallePedido detallePedido : pedido.getDetalles()) {
            factura.agregarDetalle(new DetalleFactura(
                    detallePedido.getProducto(),
                    detallePedido.getCantidad(),
                    detallePedido.getPrecio()
            ));
        }

        factura.calcularSubtotal();
        factura.calcularTotal(); // calcula el IVA internamente

        boolean facturaGuardada = facturaService.guardar(factura);

        if (!facturaGuardada) {
            return ResultadoConfirmacion.error(
                    "El pedido y el pago se guardaron, pero no se pudo generar la factura."
            );
        }

        // ---------- 7. Vaciar el carrito ----------
        carritoService.vaciarCarrito(carrito.getIdCarrito());

        // ---------- 8. Generar el PDF ----------
        File pdfFactura;
        try {
            pdfFactura = generadorFacturaPdf.generarPdf(factura);
        } catch (Exception e) {
            e.printStackTrace();
            // El pedido/pago/factura ya quedaron guardados; solo falló el PDF.
            return ResultadoConfirmacion.exitoSinPdf(pedido, factura);
        }

        return ResultadoConfirmacion.ok(pedido, factura, pdfFactura);
    }

    /**
     * Resultado del Paso 3 -> Paso 4. El Paso 4 (PanelFacturaWizard)
     * solo necesita leer esto para mostrar el resumen y el botón
     * de descarga de PDF.
     */
    public static class ResultadoConfirmacion {

        private final boolean exito;
        private final String mensajeError;
        private final Pedido pedido;
        private final Factura factura;
        private final File pdfFactura;

        private ResultadoConfirmacion(boolean exito, String mensajeError,
                                       Pedido pedido, Factura factura, File pdfFactura) {
            this.exito = exito;
            this.mensajeError = mensajeError;
            this.pedido = pedido;
            this.factura = factura;
            this.pdfFactura = pdfFactura;
        }

        public static ResultadoConfirmacion ok(Pedido pedido, Factura factura, File pdfFactura) {
            return new ResultadoConfirmacion(true, null, pedido, factura, pdfFactura);
        }

        public static ResultadoConfirmacion exitoSinPdf(Pedido pedido, Factura factura) {
            return new ResultadoConfirmacion(true,
                    "El pedido se registró, pero el PDF no pudo generarse.",
                    pedido, factura, null);
        }

        public static ResultadoConfirmacion error(String mensaje) {
            return new ResultadoConfirmacion(false, mensaje, null, null, null);
        }

        public boolean isExito() {
            return exito;
        }

        public String getMensajeError() {
            return mensajeError;
        }

        public Pedido getPedido() {
            return pedido;
        }

        public Factura getFactura() {
            return factura;
        }

        public File getPdfFactura() {
            return pdfFactura;
        }
    }
}
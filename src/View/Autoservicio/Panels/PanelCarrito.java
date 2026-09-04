package View.Autoservicio.Panels;

import Base.DashboardBase;
import Controller.PedidoController;
import Model.Carrito;
import Model.CarritoDetalle;
import Model.MetodoPago;
import Model.TipoEntrega;
import Model.Usuario;
import Service.Implement.CarritoDetalleServiceImpl;
import Service.Implement.CarritoServiceImpl;
import Service.Interfaz.ICarritoDetalleService;
import Service.Interfaz.ICarritoService;
import Utils.Sesion;
import View.Autoservicio.Panels.Carrito.PasoConfirmacion;
import View.Autoservicio.Panels.Carrito.PasoEntregaPago;
import View.Autoservicio.Panels.Carrito.PasoFactura;
import View.Autoservicio.Panels.Carrito.PasoRevisarCarrito;
import View.Autoservicio.Panels.Carrito.StepperCarrito;
import View.Componentes.PanelFondo;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Window;
import java.math.BigDecimal;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * --------------------------------------------------------------- Contenedor
 * del wizard de compra del Cliente:
 *
 * (1) Carrito -> (2) Entrega y Pago -> (3) Confirmacion -> (4) Factura
 * ===============================================================
 */
public class PanelCarrito extends PanelFondo {

    /**
     * Costo fijo de envio a domicilio. Q0 para los demas tipos de entrega.
     */
    public static final BigDecimal COSTO_ENVIO_DOMICILIO = new BigDecimal("15.00");

    private final ICarritoService carritoService = new CarritoServiceImpl();
    private final ICarritoDetalleService carritoDetalleService = new CarritoDetalleServiceImpl();
    private final PedidoController pedidoController = new PedidoController();

    private final StepperCarrito stepper = new StepperCarrito();
    private final JPanel contenedorPasos = new JPanel(new CardLayout());

    private final PasoRevisarCarrito paso1;
    private final PasoEntregaPago paso2;
    private final PasoConfirmacion paso3;
    private final PasoFactura paso4;

    private Carrito carrito;

    private TipoEntrega tipoEntrega = TipoEntrega.COMER_EN_RESTAURANTE;
    private MetodoPago metodoPago = MetodoPago.EFECTIVO;
    private BigDecimal montoRecibido;

    private String direccionEntrega;
    private String referenciaEntrega;

    private String nombreCliente;
    private String correoCliente;
    private String nit;
    private boolean consumidorFinal = true;

    public PanelCarrito() {

        super();

        setOpaque(false);
        setLayout(new BorderLayout());

        contenedorPasos.setOpaque(false);

        paso1 = new PasoRevisarCarrito(this);
        paso2 = new PasoEntregaPago(this);
        paso3 = new PasoConfirmacion(this);
        paso4 = new PasoFactura(this);

        contenedorPasos.add(paso1, "PASO1");
        contenedorPasos.add(paso2, "PASO2");
        contenedorPasos.add(paso3, "PASO3");
        contenedorPasos.add(paso4, "PASO4");

        add(stepper, BorderLayout.NORTH);
        add(contenedorPasos, BorderLayout.CENTER);
    }

    @Override
    public void setVisible(boolean visible) {

        super.setVisible(visible);

        if (visible) {
            irAPaso1();
        }
    }

    // ==========================================================
    // NAVEGACION
    // ==========================================================
    public void irAPaso1() {

        cargarCarritoDesdeBD();
        paso1.refrescar();
        mostrar("PASO1", 1);
    }

    public void irAPaso2() {

        if (carrito == null || carrito.estaVacio()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Tu carrito esta vacio. Agrega productos antes de continuar.",
                    "Carrito vacio",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        precargarDatosFacturacion();
        paso2.refrescar();
        mostrar("PASO2", 2);
    }

    public void irAPaso3() {
        paso3.refrescar();
        mostrar("PASO3", 3);
    }

    private void irAPaso4(PedidoController.ResultadoConfirmacion resultado) {
        paso4.mostrarResultado(resultado);
        mostrar("PASO4", 4);
    }

    /**
     * Boton "Volver al inicio" del Paso 4.
     */
    public void volverAInicio() {

        Window ventana = SwingUtilities.getWindowAncestor(this);

        if (ventana instanceof DashboardBase dashboard) {
            dashboard.onOpcionSeleccionada("INICIO");
        }
    }

    private void mostrar(String idPaso, int numeroPaso) {

        ((CardLayout) contenedorPasos.getLayout()).show(contenedorPasos, idPaso);
        stepper.setPasoActual(numeroPaso);
    }

    // ==========================================================
    // CARRITO (Paso 1)
    // ==========================================================
    private void cargarCarritoDesdeBD() {

        Usuario usuario = Sesion.getInstancia().getUsuario();

        if (usuario == null) {
            carrito = null;
            return;
        }

        carrito = carritoService.obtenerOCrearCarritoActivo(usuario.getIdUsuario());

        if (carrito != null) {
            carrito.getDetalles().clear();
            carrito.getDetalles().addAll(carritoService.obtenerDetalles(carrito.getIdCarrito()));
        }
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void actualizarCantidad(CarritoDetalle detalle, int nuevaCantidad) {

        carritoDetalleService.actualizarCantidad(detalle.getIdCarritoDetalle(), nuevaCantidad);
        cargarCarritoDesdeBD();
        paso1.actualizarResumen();
    }

    public void eliminarProducto(CarritoDetalle detalle) {

        carritoDetalleService.eliminarProducto(detalle.getIdCarritoDetalle());
        cargarCarritoDesdeBD();
        paso1.refrescar();
    }

    public void vaciarCarrito() {

        if (carrito == null || carrito.estaVacio()) {
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que quieres vaciar todo el carrito?",
                "Vaciar carrito",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            carritoService.vaciarCarrito(carrito.getIdCarrito());
            cargarCarritoDesdeBD();
            paso1.refrescar();
        }
    }

    /**
     * Subtotal + IVA (12%), SIN envio.
     */
    public BigDecimal calcularSubtotalConIva() {

        if (carrito == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal subtotal = carrito.calcularTotal();
        BigDecimal iva = subtotal.multiply(new BigDecimal("0.12"));

        return subtotal.add(iva);
    }

    /**
     * Subtotal + IVA (12%) + envio (si aplica Domicilio). Total real a pagar.
     */
    public BigDecimal calcularTotalAPagar() {
        return calcularSubtotalConIva().add(getCostoEnvio());
    }

    // ==========================================================
    // ENTREGA Y PAGO (Paso 2)
    // ==========================================================
    private void precargarDatosFacturacion() {

        Usuario usuario = Sesion.getInstancia().getUsuario();

        if (usuario == null) {
            return;
        }

        if (nombreCliente == null || nombreCliente.isBlank()) {
            nombreCliente = usuario.getNombreCompleto();
        }

        if (correoCliente == null || correoCliente.isBlank()) {
            correoCliente = usuario.getCorreo();
        }
    }

    public TipoEntrega getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega(TipoEntrega tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public BigDecimal getMontoRecibido() {
        return montoRecibido;
    }

    public void setMontoRecibido(BigDecimal montoRecibido) {
        this.montoRecibido = montoRecibido;
    }

    public BigDecimal getCostoEnvio() {
        return BigDecimal.ZERO;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getReferenciaEntrega() {
        return referenciaEntrega;
    }

    public void setReferenciaEntrega(String referenciaEntrega) {
        this.referenciaEntrega = referenciaEntrega;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public boolean isConsumidorFinal() {
        return consumidorFinal;
    }

    public void setConsumidorFinal(boolean consumidorFinal) {
        this.consumidorFinal = consumidorFinal;
    }

    /**
     * Valida los datos minimos del Paso 2 antes de dejar avanzar a
     * Confirmacion. Devuelve null si todo esta bien, o el mensaje de error a
     * mostrar si falta algo.
     */
    public String validarPaso2() {

        if (nombreCliente == null || nombreCliente.isBlank()) {
            return "El nombre del cliente es obligatorio para la factura.";
        }

        if (correoCliente == null || correoCliente.isBlank()) {
            return "El correo electronico es obligatorio para la factura.";
        }

        if (!consumidorFinal && (nit == null || nit.isBlank())) {
            return "Indica el NIT o marca la casilla \"Consumidor final\".";
        }

        return null;
    }

    // ==========================================================
    // CONFIRMACION (Paso 3) -> registra el pedido en la BD
    // ==========================================================
    public void confirmarPedido() {

        // El mockup no le pide al cliente cuánto efectivo entrega
        // (el pago en efectivo se cobra al recibir el pedido, no en
        // el kiosco). PedidoController sí valida montoRecibido >=
        // total cuando metodoPago = EFECTIVO, así que aquí se
        // autocompleta con el total exacto para que esa validación
        // pase sin pedirle un dato que la interfaz no muestra.
        if (metodoPago == MetodoPago.EFECTIVO && montoRecibido == null) {
            montoRecibido = calcularTotalAPagar();
        }

        PedidoController.ResultadoConfirmacion resultado = pedidoController.confirmarPedido(
        carrito,
        tipoEntrega,
        metodoPago,
        montoRecibido,
        getCostoEnvio(),
        direccionEntrega,
        referenciaEntrega,
        consumidorFinal ? null : nit,
        nombreCliente,
        correoCliente
);

        if (!resultado.isExito()) {
            JOptionPane.showMessageDialog(
                    this,
                    resultado.getMensajeError(),
                    "No se pudo confirmar el pedido",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // El pedido ya se guardo y el carrito se vacio en la BD (ver
        // PedidoController); se limpia tambien el estado local del
        // wizard para que un pedido nuevo no arrastre datos del
        // anterior.
        carrito = null;
        montoRecibido = null;

        irAPaso4(resultado);
    }
}

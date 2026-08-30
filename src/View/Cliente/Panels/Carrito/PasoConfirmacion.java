package View.Cliente.Panels.Carrito;

import Model.Carrito;
import Model.CarritoDetalle;
import Model.MetodoPago;
import Model.TipoEntrega;
import View.Autoservicio.Panels.PanelCarrito;
import View.Componentes.PanelRedondeado;
import View.Utils.AdministradorTema;
import View.Utils.FabricaBotones;
import View.Utils.FabricaEtiquetas;
import View.Utils.FabricaScroll;
import View.Utils.FormateadorMoneda;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Paso 3 del wizard: "Revisa tu pedido" antes de confirmarlo.
 *
 * Muestra en solo lectura todo lo elegido en los pasos 1 y 2
 * (productos, entrega, pago, datos de facturación) y el resumen
 * final. El botón "Confirmar pedido" es el único punto donde se
 * escribe en la base de datos (ver PanelCarrito.confirmarPedido()).
 * ===============================================================
 */
public class PasoConfirmacion extends JPanel {

    private final PanelCarrito padre;

    private final JPanel panelDetalleProductos = new JPanel();
    private JLabel lblTipoEntrega;
    private JLabel lblDireccion;
    private JLabel lblMetodoPago;

    private final JPanel panelItemsResumen = new JPanel();
    private JLabel lblCantidadItems;
    private JLabel lblSubtotal;
    private JLabel lblIva;
    private JLabel lblEnvio;
    private JLabel lblTotal;

    private JButton btnConfirmar;

    public PasoConfirmacion(PanelCarrito padre) {

        this.padre = padre;

        setOpaque(false);
        setLayout(new BorderLayout(0, AdministradorTema.espacioMediano()));
        setBorder(BorderFactory.createEmptyBorder(
                AdministradorTema.espacioGrande(),
                AdministradorTema.espacioGrande(),
                AdministradorTema.espacioGrande(),
                AdministradorTema.espacioGrande()
        ));

        JPanel columnas = new JPanel(new GridLayout(1, 2, AdministradorTema.espacioGrande(), 0));
        columnas.setOpaque(false);
        columnas.add(construirColumnaDetalle());
        columnas.add(construirColumnaResumen());

        JScrollPane scroll = FabricaScroll.crearPanel(envolver(columnas));
        scroll.setBorder(BorderFactory.createEmptyBorder());

        add(scroll, BorderLayout.CENTER);
        add(construirPie(), BorderLayout.SOUTH);
    }

    private JPanel envolver(JPanel contenido) {
        JPanel envoltorio = new JPanel(new BorderLayout());
        envoltorio.setOpaque(false);
        envoltorio.add(contenido, BorderLayout.NORTH);
        return envoltorio;
    }

    // ==========================================================
    // COLUMNA IZQUIERDA: DETALLE DEL PEDIDO + ENTREGA + PAGO
    // ==========================================================
    private JPanel construirColumnaDetalle() {

        JPanel columna = new JPanel();
        columna.setOpaque(false);
        columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));

        PanelRedondeado tarjetaProductos = tarjeta();
        tarjetaProductos.add(subtitulo("Detalle del pedido"));
        tarjetaProductos.add(Box.createVerticalStrut(10));

        panelDetalleProductos.setOpaque(false);
        panelDetalleProductos.setLayout(new BoxLayout(panelDetalleProductos, BoxLayout.Y_AXIS));
        panelDetalleProductos.setAlignmentX(LEFT_ALIGNMENT);
        tarjetaProductos.add(panelDetalleProductos);

        PanelRedondeado tarjetaEntrega = tarjeta();
        tarjetaEntrega.add(subtitulo("Entrega"));
        tarjetaEntrega.add(Box.createVerticalStrut(8));
        lblTipoEntrega = filaEtiquetaValor(tarjetaEntrega, "Tipo:");
        lblDireccion = filaEtiquetaValor(tarjetaEntrega, "Dirección:");

        PanelRedondeado tarjetaPago = tarjeta();
        tarjetaPago.add(subtitulo("Pago"));
        tarjetaPago.add(Box.createVerticalStrut(8));
        lblMetodoPago = filaEtiquetaValor(tarjetaPago, "Método:");

        columna.add(tarjetaProductos);
        columna.add(Box.createVerticalStrut(AdministradorTema.espacioMediano()));
        columna.add(tarjetaEntrega);
        columna.add(Box.createVerticalStrut(AdministradorTema.espacioMediano()));
        columna.add(tarjetaPago);

        return columna;
    }

    // ==========================================================
    // COLUMNA DERECHA: TU PEDIDO + TIEMPO ESTIMADO
    // ==========================================================
    private JPanel construirColumnaResumen() {

        JPanel columna = new JPanel();
        columna.setOpaque(false);
        columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));

        PanelRedondeado tarjeta = tarjeta();

        tarjeta.add(subtitulo("Tu pedido"));
        lblCantidadItems = FabricaEtiquetas.crearPequeño("0 artículos");
        lblCantidadItems.setAlignmentX(LEFT_ALIGNMENT);
        lblCantidadItems.setForeground(Color.GRAY);
        tarjeta.add(lblCantidadItems);
        tarjeta.add(Box.createVerticalStrut(10));

        panelItemsResumen.setOpaque(false);
        panelItemsResumen.setLayout(new BoxLayout(panelItemsResumen, BoxLayout.Y_AXIS));
        panelItemsResumen.setAlignmentX(LEFT_ALIGNMENT);
        tarjeta.add(panelItemsResumen);

        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(separador());
        tarjeta.add(Box.createVerticalStrut(10));

        lblSubtotal = filaMonto(tarjeta, "Subtotal");
        lblIva = filaMonto(tarjeta, "IVA (12%)");
        lblEnvio = filaMonto(tarjeta, "Envío");

        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(separador());
        tarjeta.add(Box.createVerticalStrut(8));

        lblTotal = filaMonto(tarjeta, "Total a pagar");
        lblTotal.setFont(AdministradorTema.fuenteTitulo().deriveFont(Font.BOLD, 20f));
        lblTotal.setForeground(AdministradorTema.colorPrincipal());

        PanelRedondeado tarjetaTiempo = new PanelRedondeado(AdministradorTema.colorFondo());
        tarjetaTiempo.setLayout(new BoxLayout(tarjetaTiempo, BoxLayout.Y_AXIS));
        tarjetaTiempo.setBorder(AdministradorTema.bordeTarjeta());

        JLabel lblTiempo = FabricaEtiquetas.crearSubtitulo("⏱  Tiempo estimado: 15 - 20 minutos");
        lblTiempo.setAlignmentX(LEFT_ALIGNMENT);
        lblTiempo.setForeground(AdministradorTema.colorAcento());

        JLabel lblAviso = FabricaEtiquetas.crearPequeño("Te notificaremos cuando tu pedido esté listo.");
        lblAviso.setAlignmentX(LEFT_ALIGNMENT);

        tarjetaTiempo.add(lblTiempo);
        tarjetaTiempo.add(Box.createVerticalStrut(4));
        tarjetaTiempo.add(lblAviso);

        columna.add(tarjeta);
        columna.add(Box.createVerticalStrut(AdministradorTema.espacioMediano()));
        columna.add(tarjetaTiempo);

        return columna;
    }

    // ==========================================================
    // PIE: VOLVER / CONFIRMAR PEDIDO
    // ==========================================================
    private JPanel construirPie() {

        JPanel pie = new JPanel(new BorderLayout());
        pie.setOpaque(false);

        JButton btnVolver = FabricaBotones.crearSecundario("← Volver a Entrega y Pago");
        btnVolver.addActionListener(e -> padre.irAPaso2());

        btnConfirmar = FabricaBotones.crearPrimario("✓ Confirmar pedido");
        btnConfirmar.addActionListener(e -> confirmar());

        pie.add(btnVolver, BorderLayout.WEST);
        pie.add(btnConfirmar, BorderLayout.EAST);

        return pie;
    }

    private void confirmar() {

        // Evita doble clic mientras se registra el pedido en la BD
        // (INSERT pedido + detalle_pedido + pago + factura + PDF).
        btnConfirmar.setEnabled(false);
        btnConfirmar.setText("Confirmando...");

        SwingUtilities.invokeLater(() -> {
            try {
                padre.confirmarPedido();
            } finally {
                btnConfirmar.setEnabled(true);
                btnConfirmar.setText("✓ Confirmar pedido");
            }
        });
    }

    // ==========================================================
    // REFRESCO: se llama cada vez que PanelCarrito muestra este paso
    // ==========================================================
    public void refrescar() {

        Carrito carrito = padre.getCarrito();

        // ---- Detalle de productos ----
        panelDetalleProductos.removeAll();

        if (carrito != null) {
            for (CarritoDetalle detalle : carrito.getDetalles()) {

                JLabel item = FabricaEtiquetas.crearTexto(
                        detalle.getProducto().getNombre() + "  x" + detalle.getCantidad()
                );
                item.setAlignmentX(LEFT_ALIGNMENT);
                item.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
                panelDetalleProductos.add(item);
            }
        }

        // ---- Entrega ----
        TipoEntrega tipo = padre.getTipoEntrega();
        lblTipoEntrega.setText(textoTipoEntrega(tipo));

        if (tipo == TipoEntrega.DOMICILIO) {
            String direccion = padre.getDireccionEntrega() != null ? padre.getDireccionEntrega() : "";
            String referencia = padre.getReferenciaEntrega();
            lblDireccion.setText(direccion + (referencia != null && !referencia.isBlank()
                    ? " (" + referencia + ")" : ""));
            lblDireccion.getParent().setVisible(true);
        } else {
            lblDireccion.getParent().setVisible(false);
        }

        // ---- Pago ----
        lblMetodoPago.setText(textoMetodoPago(padre.getMetodoPago()));

        // ---- Resumen ----
        panelItemsResumen.removeAll();

        if (carrito != null) {
            for (CarritoDetalle detalle : carrito.getDetalles()) {

                JLabel item = FabricaEtiquetas.crearPequeño(
                        detalle.getCantidad() + "x  " + detalle.getProducto().getNombre()
                );
                JLabel precio = FabricaEtiquetas.crearPequeño(
                        FormateadorMoneda.formatear(detalle.getSubtotal())
                );

                JPanel fila = new JPanel(new BorderLayout());
                fila.setOpaque(false);
                fila.setAlignmentX(LEFT_ALIGNMENT);
                fila.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
                fila.add(item, BorderLayout.WEST);
                fila.add(precio, BorderLayout.EAST);

                panelItemsResumen.add(fila);
            }

            lblCantidadItems.setText(carrito.getCantidadProductos() + " artículos");
        }

        BigDecimal subtotal = carrito != null ? carrito.calcularTotal() : BigDecimal.ZERO;
        BigDecimal iva = subtotal.multiply(new BigDecimal("0.12"));
        BigDecimal envio = padre.getCostoEnvio();
        BigDecimal total = subtotal.add(iva).add(envio);

        lblSubtotal.setText(FormateadorMoneda.formatear(subtotal));
        lblIva.setText(FormateadorMoneda.formatear(iva));
        lblEnvio.setText(envio.signum() > 0 ? FormateadorMoneda.formatear(envio) : "Gratis");
        lblTotal.setText(FormateadorMoneda.formatear(total));

        panelDetalleProductos.revalidate();
        panelDetalleProductos.repaint();
        panelItemsResumen.revalidate();
        panelItemsResumen.repaint();

        revalidate();
        repaint();
    }

    private String textoTipoEntrega(TipoEntrega tipo) {

        if (tipo == null) {
            return "-";
        }

        return switch (tipo) {
            case COMER_EN_RESTAURANTE -> "Comer en restaurante";
            case PARA_LLEVAR -> "Para llevar";
            case DOMICILIO -> "Domicilio";
        };
    }

    private String textoMetodoPago(MetodoPago metodo) {

        if (metodo == null) {
            return "-";
        }

        return switch (metodo) {
            case EFECTIVO -> "Efectivo (pagar al recibir)";
            case TARJETA -> "Tarjeta (Visa, MasterCard)";
            case TRANSFERENCIA -> "Yappy";
        };
    }

    // ==========================================================
    // AYUDANTES DE CONSTRUCCION
    // ==========================================================
    private PanelRedondeado tarjeta() {
        PanelRedondeado tarjeta = new PanelRedondeado();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(AdministradorTema.bordeTarjeta());
        return tarjeta;
    }

    private JLabel subtitulo(String texto) {
        JLabel lbl = FabricaEtiquetas.crearSubtitulo(texto);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

    private JSeparator separador() {
        JSeparator sep = new JSeparator();
        sep.setAlignmentX(LEFT_ALIGNMENT);
        return sep;
    }

    private JLabel filaEtiquetaValor(JPanel contenedor, String etiqueta) {

        JPanel fila = new JPanel(new BorderLayout(6, 0));
        fila.setOpaque(false);
        fila.setAlignmentX(LEFT_ALIGNMENT);
        fila.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));

        JLabel lblEtiqueta = FabricaEtiquetas.crearPequeño(etiqueta);
        lblEtiqueta.setForeground(Color.GRAY);

        JLabel lblValor = FabricaEtiquetas.crearTexto("-");

        fila.add(lblEtiqueta, BorderLayout.WEST);
        fila.add(lblValor, BorderLayout.CENTER);

        contenedor.add(fila);

        return lblValor;
    }

    private JLabel filaMonto(JPanel contenedor, String etiqueta) {

        JPanel fila = new JPanel(new BorderLayout());
        fila.setOpaque(false);
        fila.setAlignmentX(LEFT_ALIGNMENT);
        fila.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));

        JLabel lblEtiqueta = FabricaEtiquetas.crearTexto(etiqueta);
        JLabel lblValor = FabricaEtiquetas.crearTexto("Q0.00");
        lblValor.setHorizontalAlignment(SwingConstants.RIGHT);

        fila.add(lblEtiqueta, BorderLayout.WEST);
        fila.add(lblValor, BorderLayout.EAST);

        contenedor.add(fila);

        return lblValor;
    }
}

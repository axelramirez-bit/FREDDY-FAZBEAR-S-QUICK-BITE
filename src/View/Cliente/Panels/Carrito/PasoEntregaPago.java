package View.Cliente.Panels.Carrito;

import Model.CarritoDetalle;
import Model.MetodoPago;
import Model.TipoEntrega;
import View.Autoservicio.Panels.PanelCarrito;
import View.Componentes.PanelRedondeado;
import View.Utils.AdministradorTema;
import View.Utils.FabricaBotones;
import View.Utils.FabricaCampos;
import View.Utils.FabricaEtiquetas;
import View.Utils.FabricaScroll;
import View.Utils.FormateadorMoneda;
import View.Utils.PaletaColores;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * --------------------------------------------------------------- Paso 2 del
 * wizard: "Entrega y Pago".
 *
 * Columna izquierda : Tipo de entrega + Direccion de entrega (solo si eligio
 * Domicilio). Columna central : Metodo de pago + Datos de facturacion. Columna
 * derecha : "Tu pedido" (resumen con envio incluido).
 * ===============================================================
 */
public class PasoEntregaPago extends JPanel {

    private final PanelCarrito padre;

    // ---- Tipo de entrega ----
    private final ButtonGroup grupoEntrega = new ButtonGroup();
    private JRadioButton rbRestaurante;
    private JRadioButton rbLlevar;

    // ---- Metodo de pago ----
    private final ButtonGroup grupoPago = new ButtonGroup();
    private JRadioButton rbEfectivo;
    private JRadioButton rbTarjeta;
    private JRadioButton rbYappy;

    // ---- Datos de facturacion ----
    private JTextField campoNombre;
    private JTextField campoCorreo;
    private JTextField campoNit;
    private JCheckBox chkConsumidorFinal;

    // ---- Resumen "Tu pedido" ----
    private final JPanel panelItemsResumen = new JPanel();
    private JLabel lblCantidadItems;
    private JLabel lblSubtotal;
    private JLabel lblIva;
    private JLabel lblEnvio;
    private JLabel lblTotal;

    public PasoEntregaPago(PanelCarrito padre) {

        this.padre = padre;

        setOpaque(false);
        setLayout(new BorderLayout(0, AdministradorTema.espacioMediano()));
        setBorder(BorderFactory.createEmptyBorder(
                AdministradorTema.espacioGrande(),
                AdministradorTema.espacioGrande(),
                AdministradorTema.espacioGrande(),
                AdministradorTema.espacioGrande()
        ));

        JPanel columnas = new JPanel(new GridLayout(1, 3, AdministradorTema.espacioGrande(), 0));
        columnas.setOpaque(false);

        columnas.add(construirColumnaEntrega());
        columnas.add(construirColumnaPago());
        columnas.add(construirColumnaResumen());

        JScrollPane scroll = FabricaScroll.crearPanel(envolver(columnas));
        scroll.setBorder(BorderFactory.createEmptyBorder());

        add(scroll, BorderLayout.CENTER);
        add(construirPie(), BorderLayout.SOUTH);
    }

    // Fuerza que el contenido no se estire verticalmente dentro del scroll.
    private JPanel envolver(JPanel contenido) {
        JPanel envoltorio = new JPanel(new BorderLayout());
        envoltorio.setOpaque(false);
        envoltorio.add(contenido, BorderLayout.NORTH);
        return envoltorio;
    }

    // ==========================================================
    // COLUMNA 1: TIPO DE ENTREGA + DIRECCION
    // ==========================================================
    private JPanel construirColumnaEntrega() {

        JPanel columna = new JPanel();
        columna.setOpaque(false);
        columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));

        PanelRedondeado tarjeta = tarjeta();
        tarjeta.add(subtitulo("1. Tipo de entrega"));
        tarjeta.add(Box.createVerticalStrut(10));

        rbRestaurante = opcionEntrega("Comer en restaurante", "Sin costo adicional");
        rbLlevar = opcionEntrega("Para llevar", "Sin costo adicional");

        grupoEntrega.add(rbRestaurante);
        grupoEntrega.add(rbLlevar);
        rbRestaurante.setSelected(true);

        tarjeta.add(rbRestaurante);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(rbLlevar);

        java.awt.event.ActionListener alCambiarEntrega = e -> actualizarResumen();
        rbRestaurante.addActionListener(alCambiarEntrega);
        rbLlevar.addActionListener(alCambiarEntrega);

        columna.add(tarjeta);

        return columna;
    }

    // ==========================================================
    // COLUMNA 2: METODO DE PAGO + DATOS DE FACTURACION
    // ==========================================================
    private JPanel construirColumnaPago() {

        JPanel columna = new JPanel();
        columna.setOpaque(false);
        columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));

        PanelRedondeado tarjetaPago = tarjeta();
        tarjetaPago.add(subtitulo("2. Método de pago"));
        tarjetaPago.add(Box.createVerticalStrut(10));

        rbEfectivo = opcionEntrega("Efectivo", "Pagar al recibir");
        rbTarjeta = opcionEntrega("Tarjeta", "Visa, MasterCard");
        // Yappy (pago por QR) se guarda internamente como TRANSFERENCIA:
        // Model.MetodoPago todavía no tiene un valor propio para pagos
        // por QR/billetera digital.
        rbYappy = opcionEntrega("Yappy", "Pago fácil y rápido");

        grupoPago.add(rbEfectivo);
        grupoPago.add(rbTarjeta);
        grupoPago.add(rbYappy);
        rbEfectivo.setSelected(true);

        tarjetaPago.add(rbEfectivo);
        tarjetaPago.add(Box.createVerticalStrut(6));
        tarjetaPago.add(rbTarjeta);
        tarjetaPago.add(Box.createVerticalStrut(6));
        tarjetaPago.add(rbYappy);

        PanelRedondeado tarjetaFacturacion = tarjeta();
        tarjetaFacturacion.add(subtitulo("3. Datos de facturación"));
        tarjetaFacturacion.add(Box.createVerticalStrut(10));

        campoNombre = campoConEtiqueta(tarjetaFacturacion, "Nombre del cliente *");
        campoCorreo = campoConEtiqueta(tarjetaFacturacion, "Correo electrónico *");
        campoNit = campoConEtiqueta(tarjetaFacturacion, "NIT (opcional)");

        chkConsumidorFinal = new JCheckBox("Consumidor final (sin NIT)");
        chkConsumidorFinal.setOpaque(false);
        chkConsumidorFinal.setSelected(true);
        chkConsumidorFinal.setAlignmentX(LEFT_ALIGNMENT);
        chkConsumidorFinal.addActionListener(e
                -> campoNit.setEnabled(!chkConsumidorFinal.isSelected())
        );
        campoNit.setEnabled(false);

        tarjetaFacturacion.add(chkConsumidorFinal);

        columna.add(tarjetaPago);
        columna.add(Box.createVerticalStrut(AdministradorTema.espacioMediano()));
        columna.add(tarjetaFacturacion);

        return columna;
    }

    // ==========================================================
    // COLUMNA 3: RESUMEN "TU PEDIDO"
    // ==========================================================
    private JPanel construirColumnaResumen() {

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
        JSeparator sep1 = new JSeparator();
        sep1.setAlignmentX(LEFT_ALIGNMENT);
        tarjeta.add(sep1);
        tarjeta.add(Box.createVerticalStrut(10));

        lblSubtotal = filaMonto(tarjeta, "Subtotal");
        lblIva = filaMonto(tarjeta, "IVA (12%)");
        lblEnvio = filaMonto(tarjeta, "Envío");

        tarjeta.add(Box.createVerticalStrut(8));
        JSeparator sep2 = new JSeparator();
        sep2.setAlignmentX(LEFT_ALIGNMENT);
        tarjeta.add(sep2);
        tarjeta.add(Box.createVerticalStrut(8));

        lblTotal = filaMonto(tarjeta, "Total");
        lblTotal.setFont(AdministradorTema.fuenteTitulo().deriveFont(Font.BOLD, 20f));
        lblTotal.setForeground(AdministradorTema.colorPrincipal());

        JPanel envoltorio = new JPanel(new BorderLayout());
        envoltorio.setOpaque(false);
        envoltorio.add(tarjeta, BorderLayout.NORTH);

        return envoltorio;
    }

    // ==========================================================
    // PIE: VOLVER / CONTINUAR
    // ==========================================================
    private JPanel construirPie() {

        JPanel pie = new JPanel(new BorderLayout());
        pie.setOpaque(false);

        JButton btnVolver = FabricaBotones.crearSecundario("← Volver al carrito");
        btnVolver.addActionListener(e -> padre.irAPaso1());

        JButton btnContinuar = FabricaBotones.crearPrimario("Continuar a Confirmación");
        btnContinuar.addActionListener(e -> continuar());

        pie.add(btnVolver, BorderLayout.WEST);
        pie.add(btnContinuar, BorderLayout.EAST);

        return pie;
    }

    private void continuar() {

        guardarSeleccionEnPadre();

        String error = padre.validarPaso2();

        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Faltan datos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        padre.irAPaso3();
    }

    private void guardarSeleccionEnPadre() {

        if (rbLlevar.isSelected()) {
            padre.setTipoEntrega(TipoEntrega.PARA_LLEVAR);
        } else {
            padre.setTipoEntrega(TipoEntrega.COMER_EN_RESTAURANTE);
        }

        if (rbTarjeta.isSelected()) {
            padre.setMetodoPago(MetodoPago.TARJETA);
        } else if (rbYappy.isSelected()) {
            padre.setMetodoPago(MetodoPago.TRANSFERENCIA);
        } else {
            padre.setMetodoPago(MetodoPago.EFECTIVO);
        }

        padre.setNombreCliente(textoDe(campoNombre));
        padre.setCorreoCliente(textoDe(campoCorreo));
        padre.setNit(textoDe(campoNit));
        padre.setConsumidorFinal(chkConsumidorFinal.isSelected());
    }

    private String textoDe(JTextField campo) {
        String texto = campo.getText();
        return texto == null ? null : texto.trim();
    }

    // ==========================================================
    // REFRESCO: se llama cada vez que PanelCarrito muestra este paso
    // ==========================================================
    public void refrescar() {

        campoNombre.setText(padre.getNombreCliente() != null ? padre.getNombreCliente() : "");
        campoCorreo.setText(padre.getCorreoCliente() != null ? padre.getCorreoCliente() : "");

        actualizarResumen();
    }

    private void actualizarResumen() {

        panelItemsResumen.removeAll();

        var carrito = padre.getCarrito();

        if (carrito != null) {

            for (CarritoDetalle detalle : carrito.getDetalles()) {

                JLabel item = FabricaEtiquetas.crearPequeño(
                        detalle.getCantidad() + "x  " + detalle.getProducto().getNombre()
                );
                item.setAlignmentX(LEFT_ALIGNMENT);

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
        BigDecimal envio = BigDecimal.ZERO;
        BigDecimal total = subtotal.add(iva).add(envio);

        lblSubtotal.setText(FormateadorMoneda.formatear(subtotal));
        lblIva.setText(FormateadorMoneda.formatear(iva));
        lblEnvio.setText(envio.signum() > 0 ? FormateadorMoneda.formatear(envio) : "Gratis");
        lblTotal.setText(FormateadorMoneda.formatear(total));

        panelItemsResumen.revalidate();
        panelItemsResumen.repaint();
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

    private JRadioButton opcionEntrega(String titulo, String detalle) {

        JRadioButton radio = new JRadioButton(
                "<html><b>" + titulo + "</b><br><span style='font-size:85%;color:#777777;'>"
                + detalle + "</span></html>"
        );
        radio.setOpaque(false);
        radio.setAlignmentX(LEFT_ALIGNMENT);
        radio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        radio.setFocusPainted(false);

        return radio;
    }

    private JTextField campoConEtiqueta(JPanel contenedor, String etiqueta) {

        JLabel lbl = FabricaEtiquetas.crearPequeño(etiqueta);
        lbl.setAlignmentX(LEFT_ALIGNMENT);

        JTextField campo = FabricaCampos.crearCampo();
        campo.setAlignmentX(LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, campo.getPreferredSize().height));

        contenedor.add(lbl);
        contenedor.add(Box.createVerticalStrut(4));
        contenedor.add(campo);
        contenedor.add(Box.createVerticalStrut(10));

        return campo;
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

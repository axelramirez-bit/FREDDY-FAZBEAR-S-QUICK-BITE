package View.Cliente.Panels.Carrito;

import Controller.PedidoController.ResultadoConfirmacion;
import Model.DetalleFactura;
import Model.Factura;
import View.Autoservicio.Panels.PanelCarrito;
import View.Componentes.PanelRedondeado;
import View.Utils.AdministradorTema;
import View.Utils.FabricaBotones;
import View.Utils.FabricaEtiquetas;
import View.Utils.FabricaScroll;
import View.Utils.FormateadorMoneda;
import View.Utils.PaletaColores;

import javax.swing.*;
import java.awt.*;
import java.awt.Desktop;
import java.io.File;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Paso 4 del wizard: "¡Pedido confirmado!".
 *
 * Es de solo lectura: recibe el ResultadoConfirmacion que devolvió
 * PedidoController.confirmarPedido() (Paso 3) y muestra el número
 * de orden, el tiempo estimado y el resumen final, con opción de
 * descargar el PDF generado.
 * ===============================================================
 */
public class PasoFactura extends JPanel {

    private final PanelCarrito padre;

    private final JLabel lblTituloExito = new JLabel();
    private final JLabel lblSubExito = new JLabel();

    private final JPanel panelItemsResumen = new JPanel();
    private JLabel lblCantidadItems;
    private JLabel lblSubtotal;
    private JLabel lblIva;
    private JLabel lblEnvio;
    private JLabel lblTotal;

    private JButton btnDescargarPdf;

    private ResultadoConfirmacion resultadoActual;

    public PasoFactura(PanelCarrito padre) {

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
        columnas.add(construirColumnaExito());
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
    // COLUMNA IZQUIERDA: MENSAJE DE ÉXITO + ACCIONES
    // ==========================================================
    private JPanel construirColumnaExito() {

        PanelRedondeado tarjeta = new PanelRedondeado();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(AdministradorTema.bordeTarjeta());

        JLabel icono = new JLabel("\u2714", SwingConstants.CENTER);
        icono.setFont(icono.getFont().deriveFont(Font.BOLD, 48f));
        icono.setForeground(AdministradorTema.colorAcento());
        icono.setAlignmentX(CENTER_ALIGNMENT);

        lblTituloExito.setFont(AdministradorTema.fuenteTitulo());
        lblTituloExito.setForeground(AdministradorTema.colorTexto());
        lblTituloExito.setAlignmentX(CENTER_ALIGNMENT);
        lblTituloExito.setText("¡Pedido confirmado!");

        lblSubExito.setFont(AdministradorTema.fuenteNormal());
        lblSubExito.setForeground(Color.GRAY);
        lblSubExito.setAlignmentX(CENTER_ALIGNMENT);
        lblSubExito.setHorizontalAlignment(SwingConstants.CENTER);

        PanelRedondeado tarjetaTiempo = new PanelRedondeado(AdministradorTema.colorFondo());
        tarjetaTiempo.setLayout(new BoxLayout(tarjetaTiempo, BoxLayout.Y_AXIS));
        tarjetaTiempo.setBorder(AdministradorTema.bordeTarjeta());
        tarjetaTiempo.setAlignmentX(CENTER_ALIGNMENT);
        tarjetaTiempo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel lblTiempo = FabricaEtiquetas.crearSubtitulo("⏱  Tiempo estimado de entrega: 15 - 20 minutos");
        lblTiempo.setForeground(AdministradorTema.colorAcento());
        lblTiempo.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblAviso = FabricaEtiquetas.crearPequeño("Te notificaremos cuando tu pedido esté listo.");
        lblAviso.setAlignmentX(LEFT_ALIGNMENT);

        tarjetaTiempo.add(lblTiempo);
        tarjetaTiempo.add(Box.createVerticalStrut(4));
        tarjetaTiempo.add(lblAviso);

        JPanel botones = new JPanel(new GridLayout(1, 2, 10, 0));
        botones.setOpaque(false);
        botones.setAlignmentX(CENTER_ALIGNMENT);
        botones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        btnDescargarPdf = FabricaBotones.crearSecundario("⬇ Descargar factura (PDF)");
        btnDescargarPdf.addActionListener(e -> descargarPdf());

        JButton btnAgradecimiento = FabricaBotones.crearSecundario("¡Gracias por tu compra! 🎉");
        btnAgradecimiento.setEnabled(false);

        botones.add(btnDescargarPdf);
        botones.add(btnAgradecimiento);

        tarjeta.add(Box.createVerticalGlue());
        tarjeta.add(icono);
        tarjeta.add(Box.createVerticalStrut(12));
        tarjeta.add(lblTituloExito);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(lblSubExito);
        tarjeta.add(Box.createVerticalStrut(AdministradorTema.espacioGrande()));
        tarjeta.add(tarjetaTiempo);
        tarjeta.add(Box.createVerticalStrut(AdministradorTema.espacioMediano()));
        tarjeta.add(botones);
        tarjeta.add(Box.createVerticalGlue());

        JPanel envoltorio = new JPanel(new BorderLayout());
        envoltorio.setOpaque(false);
        envoltorio.add(tarjeta, BorderLayout.CENTER);

        return envoltorio;
    }

    // ==========================================================
    // COLUMNA DERECHA: RESUMEN DEL PEDIDO PAGADO
    // ==========================================================
    private JPanel construirColumnaResumen() {

        PanelRedondeado tarjeta = new PanelRedondeado();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(AdministradorTema.bordeTarjeta());

        JLabel titulo = FabricaEtiquetas.crearSubtitulo("Resumen del pedido");
        titulo.setAlignmentX(LEFT_ALIGNMENT);
        tarjeta.add(titulo);

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

        lblTotal = filaMonto(tarjeta, "Total pagado");
        lblTotal.setFont(AdministradorTema.fuenteTitulo().deriveFont(Font.BOLD, 20f));
        lblTotal.setForeground(AdministradorTema.colorPrincipal());

        JPanel envoltorio = new JPanel(new BorderLayout());
        envoltorio.setOpaque(false);
        envoltorio.add(tarjeta, BorderLayout.NORTH);

        return envoltorio;
    }

    // ==========================================================
    // PIE: VOLVER AL INICIO
    // ==========================================================
    private JPanel construirPie() {

        JPanel pie = new JPanel(new BorderLayout());
        pie.setOpaque(false);

        JButton btnVolver = FabricaBotones.crearPrimario("Volver al inicio");
        btnVolver.addActionListener(e -> padre.volverAInicio());

        pie.add(btnVolver, BorderLayout.CENTER);

        return pie;
    }

    private void descargarPdf() {

        if (resultadoActual == null || resultadoActual.getPdfFactura() == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "La factura en PDF no pudo generarse para este pedido.",
                    "PDF no disponible",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        File pdf = resultadoActual.getPdfFactura();

        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Desktop.getDesktop().open(pdf);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Factura guardada en: " + pdf.getAbsolutePath(),
                        "Factura generada",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo abrir el PDF automáticamente. Se guardó en:\n" + pdf.getAbsolutePath(),
                    "No se pudo abrir",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // ==========================================================
    // MOSTRAR RESULTADO: llamado por PanelCarrito al llegar aquí
    // ==========================================================
    public void mostrarResultado(ResultadoConfirmacion resultado) {

        this.resultadoActual = resultado;

        if (resultado == null || !resultado.isExito()) {
            lblTituloExito.setText("No se pudo confirmar el pedido");
            lblSubExito.setText(resultado != null ? resultado.getMensajeError() : "");
            btnDescargarPdf.setEnabled(false);
            return;
        }

        Factura factura = resultado.getFactura();

        lblTituloExito.setText("¡Pedido confirmado!");
        lblSubExito.setText(
                "<html><div style='text-align:center;'>Tu pedido #"
                        + resultado.getPedido().getNumeroOrden()
                        + " ha sido recibido correctamente."
                        + (resultado.isCorreoEnviado()
                                ? "<br>Te enviamos la factura a tu correo."
                                : "")
                        + "</div></html>"
        );

        btnDescargarPdf.setEnabled(resultado.getPdfFactura() != null);

        // ---- Resumen final a partir de la Factura ya guardada ----
        panelItemsResumen.removeAll();

        int cantidadTotal = 0;

        for (DetalleFactura detalle : factura.getDetalles()) {

            cantidadTotal += detalle.getCantidad();

            JLabel item = FabricaEtiquetas.crearPequeño(
                    detalle.getCantidad() + "x  " + detalle.getNombreProducto()
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

        lblCantidadItems.setText(cantidadTotal + " artículos");

        lblSubtotal.setText(FormateadorMoneda.formatear(factura.getSubtotal()));
        lblIva.setText(FormateadorMoneda.formatear(factura.getIva()));

        BigDecimalSeguro envio = new BigDecimalSeguro(factura.getCostoEnvio());
        lblEnvio.setText(envio.esCero() ? "Gratis" : FormateadorMoneda.formatear(factura.getCostoEnvio()));

        lblTotal.setText(FormateadorMoneda.formatear(factura.getTotal()));

        panelItemsResumen.revalidate();
        panelItemsResumen.repaint();

        revalidate();
        repaint();
    }

    // Envoltorio minúsculo solo para no repetir el chequeo de null/cero
    // de BigDecimal en varios lugares.
    private record BigDecimalSeguro(java.math.BigDecimal valor) {
        boolean esCero() {
            return valor == null || valor.signum() == 0;
        }
    }

    // ==========================================================
    // AYUDANTES DE CONSTRUCCION
    // ==========================================================
    private JSeparator separador() {
        JSeparator sep = new JSeparator();
        sep.setAlignmentX(LEFT_ALIGNMENT);
        return sep;
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

package View.Autoservicio.Panels.Carrito;

import Model.Carrito;
import Model.CarritoDetalle;
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
import java.math.RoundingMode;
import java.util.List;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Paso 1 del wizard: "Revisa tus productos".
 *
 * Columna izquierda: lista de FilaProductoCarrito (una por cada
 * CarritoDetalle) + código de descuento + "Vaciar carrito" /
 * "Continuar a Entrega y Pago".
 *
 * Columna derecha: "Resumen del pedido" (Subtotal, IVA, Total).
 * ===============================================================
 */
public class PasoRevisarCarrito extends JPanel {

    private static final BigDecimal TASA_IVA = new BigDecimal("0.12");

    private final PanelCarrito padre;

    private final JPanel panelFilas = new JPanel();
    private final JLabel lblSinProductos = FabricaEtiquetas.crearCentrado(
            "Tu carrito está vacío. Agrega productos desde el menú."
    );

    private JLabel lblCantidadResumen;
    private JLabel lblSubtotal;
    private JLabel lblIva;
    private JLabel lblTotal;

    public PasoRevisarCarrito(PanelCarrito padre) {

        this.padre = padre;

        setOpaque(false);
        setLayout(new BorderLayout(AdministradorTema.espacioGrande(), 0));
        setBorder(BorderFactory.createEmptyBorder(
                AdministradorTema.espacioGrande(),
                AdministradorTema.espacioGrande(),
                AdministradorTema.espacioGrande(),
                AdministradorTema.espacioGrande()
        ));

        add(construirColumnaIzquierda(), BorderLayout.CENTER);
        add(construirColumnaDerecha(), BorderLayout.EAST);
    }

    // ==========================================================
    // COLUMNA IZQUIERDA
    // ==========================================================
    private JPanel construirColumnaIzquierda() {

        JPanel izquierda = new JPanel(new BorderLayout(0, AdministradorTema.espacioMediano()));
        izquierda.setOpaque(false);

        JLabel titulo = FabricaEtiquetas.crearSubtitulo("Productos en tu carrito");
        izquierda.add(titulo, BorderLayout.NORTH);

        panelFilas.setOpaque(false);
        panelFilas.setLayout(new BoxLayout(panelFilas, BoxLayout.Y_AXIS));

        JScrollPane scroll = FabricaScroll.crearPanel(panelFilas);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        izquierda.add(scroll, BorderLayout.CENTER);

        izquierda.add(construirPie(), BorderLayout.SOUTH);

        return izquierda;
    }

    private JPanel construirPie() {

        JPanel pie = new JPanel();
        pie.setOpaque(false);
        pie.setLayout(new BoxLayout(pie, BoxLayout.Y_AXIS));

        // ---- Código de descuento ----
        JPanel filaCodigo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        filaCodigo.setOpaque(false);

        JTextField campoCodigo = FabricaCampos.crearCampo();
        campoCodigo.setToolTipText("¿Tienes un código de descuento?");

        JButton btnAplicar = FabricaBotones.crearSecundario("Aplicar");
        btnAplicar.addActionListener(e -> {

            String codigo = campoCodigo.getText() == null ? "" : campoCodigo.getText().trim();

            if (codigo.isBlank()) {
                return;
            }

            // TODO: no existe todavía un IPromocionService.buscarPorCodigo().
            // Cuando exista, reemplazar este mensaje por la validación real
            // y aplicar el descuento devuelto sobre el resumen.
            JOptionPane.showMessageDialog(
                    this,
                    "Los códigos de descuento todavía no están conectados a una promoción real.",
                    "Código de descuento",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        filaCodigo.add(campoCodigo);
        filaCodigo.add(btnAplicar);

        // ---- Botones de navegación ----
        JPanel filaBotones = new JPanel(new BorderLayout());
        filaBotones.setOpaque(false);
        filaBotones.setBorder(BorderFactory.createEmptyBorder(
                AdministradorTema.espacioPequeño(), 0, 0, 0));

        JButton btnVaciar = FabricaBotones.crearSecundario("Vaciar carrito");
        btnVaciar.setForeground(PaletaColores.PRINCIPAL);
        btnVaciar.addActionListener(e -> padre.vaciarCarrito());

        JButton btnContinuar = FabricaBotones.crearPrimario("Continuar a Entrega y Pago");
        btnContinuar.addActionListener(e -> padre.irAPaso2());

        filaBotones.add(btnVaciar, BorderLayout.WEST);
        filaBotones.add(btnContinuar, BorderLayout.EAST);

        pie.add(filaCodigo);
        pie.add(filaBotones);

        return pie;
    }

    // ==========================================================
    // COLUMNA DERECHA: RESUMEN
    // ==========================================================
    private JPanel construirColumnaDerecha() {

        PanelRedondeado tarjeta = new PanelRedondeado();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(AdministradorTema.bordeTarjeta());
        tarjeta.setPreferredSize(new Dimension(280, 0));

        JLabel titulo = FabricaEtiquetas.crearSubtitulo("Resumen del pedido");
        titulo.setAlignmentX(LEFT_ALIGNMENT);

        lblCantidadResumen = filaResumen("Productos", "0");
        lblSubtotal = filaResumen("Subtotal", "Q0.00");
        lblIva = filaResumen("IVA (12%)", "Q0.00");

        JSeparator separador = new JSeparator();
        separador.setAlignmentX(LEFT_ALIGNMENT);

        lblTotal = filaResumen("Total", "Q0.00");
        lblTotal.setFont(AdministradorTema.fuenteTitulo().deriveFont(Font.BOLD, 20f));
        lblTotal.setForeground(AdministradorTema.colorPrincipal());

        tarjeta.add(titulo);
        tarjeta.add(Box.createVerticalStrut(AdministradorTema.espacioMediano()));
        tarjeta.add(lblCantidadResumen.getParent());
        tarjeta.add(lblSubtotal.getParent());
        tarjeta.add(lblIva.getParent());
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(separador);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(lblTotal.getParent());

        JPanel envoltorio = new JPanel(new BorderLayout());
        envoltorio.setOpaque(false);
        envoltorio.add(tarjeta, BorderLayout.NORTH);

        return envoltorio;
    }

    /**
     * Crea una fila "etiqueta ... valor" y devuelve el JLabel del
     * valor (el que hay que actualizar después). El JPanel fila
     * queda accesible vía valor.getParent().
     */
    private JLabel filaResumen(String etiqueta, String valorInicial) {

        JPanel fila = new JPanel(new BorderLayout());
        fila.setOpaque(false);
        fila.setAlignmentX(LEFT_ALIGNMENT);
        fila.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));

        JLabel lblEtiqueta = FabricaEtiquetas.crearTexto(etiqueta);
        JLabel lblValor = FabricaEtiquetas.crearTexto(valorInicial);
        lblValor.setHorizontalAlignment(SwingConstants.RIGHT);

        fila.add(lblEtiqueta, BorderLayout.WEST);
        fila.add(lblValor, BorderLayout.EAST);

        return lblValor;
    }

    // ==========================================================
    // REFRESCO DE DATOS
    // ==========================================================

    /** Reconstruye toda la lista de filas a partir del carrito actual. */
    public void refrescar() {

        panelFilas.removeAll();

        Carrito carrito = padre.getCarrito();
        List<CarritoDetalle> detalles = carrito != null ? carrito.getDetalles() : List.of();

        if (detalles.isEmpty()) {

            panelFilas.add(lblSinProductos);

        } else {

            for (CarritoDetalle detalle : detalles) {

                FilaProductoCarrito fila = new FilaProductoCarrito(detalle);

                fila.setListener(new FilaProductoCarrito.FilaCarritoListener() {
                    @Override
                    public void onCantidadCambiada(CarritoDetalle d, int nuevaCantidad) {
                        padre.actualizarCantidad(d, nuevaCantidad);
                    }

                    @Override
                    public void onEliminar(CarritoDetalle d) {
                        padre.eliminarProducto(d);
                    }
                });

                fila.setAlignmentX(LEFT_ALIGNMENT);
                fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, fila.getPreferredSize().height));

                panelFilas.add(fila);
                panelFilas.add(Box.createVerticalStrut(AdministradorTema.espacioPequeño()));
            }
        }

        panelFilas.revalidate();
        panelFilas.repaint();

        actualizarResumen();
    }

    /** Solo recalcula los montos del resumen (no reconstruye las filas). */
    public void actualizarResumen() {

        Carrito carrito = padre.getCarrito();

        if (carrito == null || carrito.estaVacio()) {

            lblCantidadResumen.setText("0");
            lblSubtotal.setText(FormateadorMoneda.formatear(BigDecimal.ZERO));
            lblIva.setText(FormateadorMoneda.formatear(BigDecimal.ZERO));
            lblTotal.setText(FormateadorMoneda.formatear(BigDecimal.ZERO));
            return;
        }

        BigDecimal subtotal = carrito.calcularTotal();
        BigDecimal iva = subtotal.multiply(TASA_IVA).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(iva);

        lblCantidadResumen.setText(carrito.getCantidadProductos() + " artículo(s)");
        lblSubtotal.setText(FormateadorMoneda.formatear(subtotal));
        lblIva.setText(FormateadorMoneda.formatear(iva));
        lblTotal.setText(FormateadorMoneda.formatear(total));
    }
}

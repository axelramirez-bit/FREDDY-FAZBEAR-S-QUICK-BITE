package View.Componentes;

import Model.Producto;
import View.Utils.AdministradorTema;
import View.Utils.FabricaBotones;
import View.Utils.FabricaEtiquetas;
import View.Utils.UIConstants;
import View.Utils.UtilImagenes;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.math.RoundingMode;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Tarjeta de producto reutilizable: imagen + nombre + descripción
 * + precio + selector de cantidad + botón "Agregar al carrito".
 *
 * Es UNA sola clase para las 8 categorías del Cliente
 * (Desayunos, Almuerzos, Postres, McCafé, Bebidas, Antojos,
 * Cajita Feliz, Combos) — cada PanelX solo hace un bucle sobre
 * su lista de Producto y crea una TarjetaProducto por cada uno.
 * No existe "TarjetaBebida" ni "TarjetaPostre".
 *
 * Toda la construcción visual usa lo que ya existe:
 * PanelRedondeado (marco de la imagen), SelectorCantidad
 * (cantidad), FabricaBotones/FabricaEtiquetas/UtilImagenes/
 * AdministradorTema (estilo y medidas). No se inventó ninguna
 * constante nueva — anchoTarjetaProducto(), altoTarjetaProducto(),
 * anchoImagenProducto() y altoImagenProducto() ya existían en
 * AdministradorTema, solo faltaba esta clase que las usara.
 *
 * Uso típico dentro de un PanelX de categoría:
 *
 *     for (Producto producto : productos) {
 *         TarjetaProducto tarjeta = new TarjetaProducto(producto);
 *         tarjeta.setAgregarCarritoListener((p, cantidad) ->
 *                 carritoService.agregar(p, cantidad));
 *         panelProductos.add(tarjeta);
 *     }
 * ===============================================================
 */
public class TarjetaProducto extends PanelRedondeado {

    private final Producto producto;

    private final SelectorCantidad selectorCantidad;

    private final JButton btnAgregar;

    private AgregarCarritoListener listener;

    public interface AgregarCarritoListener {
        void alAgregar(Producto producto, int cantidad);
    }

    public TarjetaProducto(Producto producto) {

        super(AdministradorTema.radioTarjeta());

        this.producto = producto;

        configurarTarjeta();

        add(crearPanelImagen(), BorderLayout.WEST);

        JPanel panelContenido = crearPanelContenido();

        this.selectorCantidad = crearSelectorCantidad();

        this.btnAgregar = FabricaBotones.crearSecundario("Agregar al carrito");
        btnAgregar.setAlignmentX(LEFT_ALIGNMENT);
        btnAgregar.addActionListener(e -> notificarAgregar());

        panelContenido.add(crearFilaCantidad());
        panelContenido.add(Box.createRigidArea(new Dimension(0, UIConstants.ESPACIO_SUBTITULO)));
        panelContenido.add(btnAgregar);

        add(panelContenido, BorderLayout.CENTER);
    }

    // ==========================================================
    // ESTRUCTURA
    // ==========================================================

    private void configurarTarjeta() {

        setLayout(new BorderLayout(UIConstants.ESPACIO_TITULO, 0));

        setBorder(BorderFactory.createEmptyBorder(
                UIConstants.ESPACIO_TITULO,
                UIConstants.ESPACIO_TITULO,
                UIConstants.ESPACIO_TITULO,
                UIConstants.ESPACIO_TITULO
        ));

        setPreferredSize(new Dimension(
                AdministradorTema.anchoTarjetaProducto(),
                AdministradorTema.altoTarjetaProducto()
        ));
    }

    private JPanel crearPanelImagen() {

        int ancho = AdministradorTema.anchoImagenProducto();
        int alto = AdministradorTema.altoImagenProducto();

        PanelRedondeado marco = new PanelRedondeado(
                AdministradorTema.radioTarjeta(),
                AdministradorTema.colorTarjeta()
        );

        marco.mostrarBorde(true);
        marco.setColorBorde(AdministradorTema.colorTexto());
        marco.grosorBorde(3);
        marco.setPreferredSize(new Dimension(ancho, alto));
        marco.setLayout(new BorderLayout());

        JLabel lblImagen = new JLabel(
                UtilImagenes.imagenProducto(
                        producto.getImagenPrincipal(),
                        ancho - UIConstants.ESPACIO_SUBTITULO,
                        alto - UIConstants.ESPACIO_SUBTITULO
                )
        );
        lblImagen.setHorizontalAlignment(JLabel.CENTER);

        marco.add(lblImagen, BorderLayout.CENTER);

        return marco;
    }

    private JPanel crearPanelContenido() {

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblNombre = FabricaEtiquetas.crearTitulo(producto.getNombre());
        lblNombre.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblDescripcion = FabricaEtiquetas.crearTexto(
                "<html><body style='width:220px'>"
                + producto.getDescripcion()
                + "</body></html>"
        );
        lblDescripcion.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblPrecio = FabricaEtiquetas.crearSubtitulo(formatearPrecio());
        lblPrecio.setForeground(AdministradorTema.colorPrincipal());
        lblPrecio.setAlignmentX(LEFT_ALIGNMENT);

        panel.add(lblNombre);
        panel.add(Box.createRigidArea(new Dimension(0, UIConstants.ESPACIO_SUBTITULO)));
        panel.add(lblDescripcion);
        panel.add(Box.createRigidArea(new Dimension(0, UIConstants.ESPACIO_SUBTITULO)));
        panel.add(lblPrecio);
        panel.add(Box.createRigidArea(new Dimension(0, UIConstants.ESPACIO_TITULO)));

        return panel;
    }

    private SelectorCantidad crearSelectorCantidad() {

        SelectorCantidad selector = new SelectorCantidad();
        selector.configurarLimites(1, Math.max(1, producto.getStock()));

        if (!producto.isDisponible()) {
            selector.setEditable(false);
        }

        return selector;
    }

    private JPanel crearFilaCantidad() {

        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, UIConstants.ESPACIO_SUBTITULO, 0));
        fila.setOpaque(false);
        fila.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblCantidad = FabricaEtiquetas.crearTexto("Cantidad");

        fila.add(lblCantidad);
        fila.add(selectorCantidad);

        return fila;
    }

    // ==========================================================
    // UTILIDADES
    // ==========================================================

    private String formatearPrecio() {
        return "Q" + producto.getPrecio().setScale(2, RoundingMode.HALF_UP);
    }

    private void notificarAgregar() {

        if (!producto.isDisponible()) {
            return;
        }

        if (listener != null) {
            listener.alAgregar(producto, selectorCantidad.getCantidad());
        }

        selectorCantidad.reiniciar();
    }

    // ==========================================================
    // GETTERS / LISTENER
    // ==========================================================

    public Producto getProducto() {
        return producto;
    }

    public int getCantidadSeleccionada() {
        return selectorCantidad.getCantidad();
    }

    public void setAgregarCarritoListener(AgregarCarritoListener listener) {
        this.listener = listener;
    }

}
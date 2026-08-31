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
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.font.TextAttribute;
import java.math.RoundingMode;
import java.util.Map;

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

        // BUG QUE ESTO CORRIGE: imagenProducto(ancho, alto) estiraba
        // la foto (getScaledInstance sin respetar proporción) y,
        // además, marco quedaba deformado porque BorderLayout.WEST
        // SIEMPRE estira su hijo a todo el alto del contenedor,
        // ignorando marco.setPreferredSize(). imagenProductoCuadrada()
        // resuelve la deformación de la foto; el envoltorio de abajo
        // resuelve la deformación del marco.
        JLabel lblImagen = new JLabel(
                UtilImagenes.imagenProductoCuadrada(
                        producto.getImagenPrincipal(),
                        ancho - UIConstants.ESPACIO_SUBTITULO
                )
        );
        lblImagen.setHorizontalAlignment(JLabel.CENTER);

        marco.add(lblImagen, BorderLayout.CENTER);

        // Envoltorio con FlowLayout: a diferencia de BorderLayout,
        // FlowLayout SÍ respeta el preferredSize de marco y lo centra
        // verticalmente dentro del alto real de la tarjeta, en vez de
        // estirarlo. Así el marco se ve cuadrado sin importar el alto
        // de la tarjeta.
        JPanel envoltorio = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        envoltorio.setOpaque(false);
        envoltorio.add(marco);

        return envoltorio;
    }

    private JPanel crearPanelContenido() {

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblNombre = FabricaEtiquetas.crearTitulo(producto.getNombre());
        lblNombre.setAlignmentX(LEFT_ALIGNMENT);

        // BUG QUE ESTO CORRIGE: FabricaEtiquetas.crearTitulo() aplica
        // AdministradorTema.fuenteTituloNegrita(), que es la fuente de
        // TÍTULO DE SECCIÓN (UIConstants.TAMANO_TITULO_SECCION = 40px,
        // pensada para encabezados de página como "Desayunos" o
        // "Combos"), no para el nombre dentro de una tarjeta. Con un
        // nombre de producto en 40px, la etiqueta por sí sola ya ocupa
        // buena parte de los ~230px de alto de la tarjeta, empujando
        // todo lo que va debajo (descripción, precio, el selector de
        // cantidad "-"/"+" y el botón "Agregar al carrito") fuera del
        // área visible de la tarjeta — esa era la causa real de que el
        // selector de cantidad "no apareciera". Ya existía en el
        // proyecto AdministradorTema.fuenteTituloProducto(), que usa
        // UIConstants.TAMANO_TITULO_PRODUCTO = 19px (el tamaño pensado
        // específicamente para esto, ver su comentario: "Granizado de
        // Arándano"); solo faltaba usarla aquí en vez de la de sección.
        lblNombre.setFont(AdministradorTema.fuenteTituloProducto());

        // ANTES: JLabel con "<html><body style='width:210px'>...".
        // BUG QUE ESTO CORRIGE: ese 210px estaba escrito a mano para
        // el ancho fijo que tenía la tarjeta en ese momento. Ahora la
        // tarjeta se estira para llenar la mitad del ancho de
        // cualquier monitor (ver GridLayout(0,2) en
        // Base.PanelProductos), así que un ancho de texto fijo en
        // píxeles queda corto en un monitor grande (texto amontonado
        // en una columna angosta dentro de una tarjeta ancha) y se
        // desborda en uno chico. JTextArea con salto de línea por
        // palabra SÍ recalcula su ajuste según el ancho real que le
        // toque en cada layout — se ve y se comporta como una
        // etiqueta normal (sin borde, sin fondo, no editable) pero el
        // texto se adapta solo.
        JTextArea lblDescripcion = new JTextArea(producto.getDescripcion());
        // BUG QUE ESTO CORRIGE: sin rows fijas, el alto que BoxLayout
        // le reservaba a la descripción variaba según el texto de
        // cada producto, haciendo impredecible cuánto espacio le
        // quedaba al selector de cantidad y al botón. Con 2 líneas
        // fijas, el alto de la tarjeta (ALTO_TARJETA_PRODUCTO) queda
        // garantizado para TODOS los productos, sin importar qué tan
        // larga sea la descripción.
        lblDescripcion.setRows(2);
        lblDescripcion.setFont(AdministradorTema.fuenteNormal());
        lblDescripcion.setForeground(AdministradorTema.colorTexto());
        lblDescripcion.setLineWrap(true);
        lblDescripcion.setWrapStyleWord(true);
        lblDescripcion.setEditable(false);
        lblDescripcion.setFocusable(false);
        lblDescripcion.setOpaque(false);
        lblDescripcion.setBorder(null);
        lblDescripcion.setAlignmentX(LEFT_ALIGNMENT);
        // Le dice a BoxLayout "tómate todo el ancho disponible de la
        // columna, no solo el que el texto necesitaría sin envolver".
        lblDescripcion.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                Integer.MAX_VALUE
        ));

        JPanel filaPrecio = crearFilaPrecio();
        filaPrecio.setAlignmentX(LEFT_ALIGNMENT);

        panel.add(lblNombre);
        panel.add(Box.createRigidArea(new Dimension(0, UIConstants.ESPACIO_SUBTITULO)));
        panel.add(lblDescripcion);
        panel.add(Box.createRigidArea(new Dimension(0, UIConstants.ESPACIO_SUBTITULO)));
        panel.add(filaPrecio);
        panel.add(Box.createRigidArea(new Dimension(0, UIConstants.ESPACIO_TITULO)));

        return panel;
    }

    // ==========================================================
    // PRECIO / PROMOCIÓN
    // ==========================================================

    /**
     * Fila de precio, en UNA sola línea de alto en ambos casos, para
     * no pedirle a la tarjeta espacio vertical extra:
     *
     * • Sin promoción vigente: solo el precio normal (como antes).
     * • Con promoción vigente: badge "-X%" (reutiliza
     * EtiquetaEstado.peligro(), ya existente en el proyecto) +
     * precio original tachado + precio final destacado.
     *
     * Usa Producto.tienePromocion() / getPrecioFinal(), que ya
     * existían en el Model y no estaban conectados a ninguna vista.
     */
    private JPanel crearFilaPrecio() {

        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, UIConstants.ESPACIO_SUBTITULO, 0));
        fila.setOpaque(false);

        if (producto.tienePromocion()) {

            int porcentaje = producto.getPromocion().getDescuento().intValue();
            fila.add(EtiquetaEstado.peligro("-" + porcentaje + "%"));

            JLabel lblAnterior = FabricaEtiquetas.crearTexto(
                    "Q" + producto.getPrecio().setScale(2, RoundingMode.HALF_UP));
            lblAnterior.setForeground(AdministradorTema.colorTexto());
            lblAnterior.setFont(lblAnterior.getFont().deriveFont(
                    Map.of(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON)));
            fila.add(lblAnterior);

            JLabel lblFinal = FabricaEtiquetas.crearSubtitulo(
                    "Q" + producto.getPrecioFinal().setScale(2, RoundingMode.HALF_UP));
            lblFinal.setForeground(AdministradorTema.colorPrincipal());
            fila.add(lblFinal);

        } else {

            JLabel lblPrecio = FabricaEtiquetas.crearSubtitulo(formatearPrecio());
            lblPrecio.setForeground(AdministradorTema.colorPrincipal());
            fila.add(lblPrecio);
        }

        return fila;
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
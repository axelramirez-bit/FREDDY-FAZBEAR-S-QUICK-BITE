// Paquete Base
package Base;

// Importa Carrito
import Model.Carrito;
// Importa CarritoDetalle
import Model.CarritoDetalle;
// Importa Categoria
import Model.Categoria;
// Importa Producto
import Model.Producto;
// Importa el servicio de detalles de carrito
import Service.Implement.CarritoDetalleServiceImpl;
// Importa el servicio de carritos
import Service.Implement.CarritoServiceImpl;
// Importa el servicio de productos
import Service.Implement.ProductoServiceImpl;
// Importa la interfaz de detalles de carrito
import Service.Interfaz.ICarritoDetalleService;
// Importa la interfaz de carritos
import Service.Interfaz.ICarritoService;
// Importa la interfaz de productos
import Service.Interfaz.IProductoService;
// Importa ServicioBusqueda
import Service.ServicioBusqueda;
// Importa Sesion
import Utils.Sesion;
// Importa PanelFondo
import View.Componentes.PanelFondo;
// Importa RejillaResponsiva (grid de tarjetas)
import View.Componentes.RejillaResponsiva;
// Importa TarjetaProducto
import View.Componentes.TarjetaProducto;
// Importa las constantes de UI
import View.Utils.UIConstants;

// Importa JOptionPane
import javax.swing.JOptionPane;
// Importa JPanel
import javax.swing.JPanel;
// Importa Scrollable
import javax.swing.Scrollable;
// Importa SwingUtilities
import javax.swing.SwingUtilities;
// Importa Dimension
import java.awt.Dimension;
// Importa Rectangle
import java.awt.Rectangle;
// Importa List
import java.util.List;
// Importa Predicate (filtro)
import java.util.function.Predicate;
// Importa Collectors
import java.util.stream.Collectors;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * --------------------------------------------------------------- Panel
 * genérico de catálogo: pinta un grid de TarjetaProducto a partir de una lista
 * de Producto filtrada.
 *
 * ES UNA SOLA CLASE para todas las categorías del Cliente (Desayunos,
 * Almuerzos, Postres, McCafé, Bebidas, Antojos, Cajita Feliz) y también para
 * Combos y Promociones. Las clases que hoy existen como placeholder
 * (PanelDesayunos, PanelBebidas, etc.) dejan de tener contenido propio: solo
 * instancian esta clase con el filtro correcto.
 *
 * Ejemplo de uso dentro de PanelDesayunos:
 *
 * public class PanelDesayunos extends PanelProductos { public PanelDesayunos()
 * { super(producto -> producto.getCategoria() != null &&
 * "Desayunos".equalsIgnoreCase( producto.getCategoria().getNombre())); } }
 *
 * Ejemplo para Combos (filtra por nombre de categoría "Combos"):
 *
 * public class PanelCombos extends PanelProductos { public PanelCombos() {
 * super(producto -> producto.getCategoria() != null &&
 * "Combos".equalsIgnoreCase( producto.getCategoria().getNombre())); } }
 *
 * Ejemplo para Promociones (no depende de categoría, sino de si el producto
 * tiene promoción activa):
 *
 * public class PanelPromociones extends PanelProductos { public
 * PanelPromociones() { super(Producto::tienePromocion); } }
 *
 * Constructor alterno recibiendo directamente una Categoria (para cuando el
 * equipo agregue categorías dinámicas desde Administrador y ya no haga falta
 * escribir el nombre a mano):
 *
 * new PanelProductos(categoriaSeleccionada);
 *
 * AVISO: IProductoService todavía no tiene listarPorCategoria(). Este panel
 * filtra en memoria sobre listarProductosDisponibles() mientras esa consulta no
 * exista en Service/DAO. Cuando se agregue, cambiar cargarProductos() para
 * usarla directamente y evitar traer todo el catálogo cada vez.
 * ===============================================================
 */
// Panel genérico de catálogo: muestra tarjetas de productos filtrados
public class PanelProductos extends PanelFondo {

    // Servicio de productos
    private final IProductoService productoService;

    // Servicio de carritos
    private final ICarritoService carritoService;

    // Servicio de detalles de carrito
    private final ICarritoDetalleService carritoDetalleService;

    // Filtro que decide qué productos se muestran
    private final Predicate<Producto> filtro;

    // Panel que contiene las tarjetas
    private JPanel panelGrid;

    // Última lista que pasó el filtro de categoría/promoción (antes de
    // aplicar texto de búsqueda). Se guarda para no volver a consultar
    // la base de datos cada vez que el usuario escribe en la barra de
    // búsqueda: ServicioBusqueda.buscarProductos() filtra en memoria
    // sobre esta lista.
    // Lista que pasó el filtro (sin búsqueda de texto)
    private List<Producto> productosFiltroCategoria = List.of();

    // Constructor: recibe el filtro de productos
    public PanelProductos(Predicate<Producto> filtro) {

        // Llama al constructor de PanelFondo
        super();

        // Crea el servicio de productos
        this.productoService = new ProductoServiceImpl();
        // Crea el servicio de carritos
        this.carritoService = new CarritoServiceImpl();
        // Crea el servicio de detalles de carrito
        this.carritoDetalleService = new CarritoDetalleServiceImpl();
        // Guarda el filtro
        this.filtro = filtro;

        // Configura el panel
        configurarPanel();

        // Carga los productos
        cargarProductos();
    }

    // Constructor alterno: filtra por una categoría
    public PanelProductos(Categoria categoria) {

        // Reutiliza el constructor con un filtro por categoría
        this(producto -> producto.getCategoria() != null
                // Producto con categoría y categoría no nula
                && categoria != null
                // y con el mismo id de categoría
                && producto.getCategoria().getIdCategoria() == categoria.getIdCategoria());
    }

    // ==========================================================
    // ESTRUCTURA
    // ==========================================================
    // Configura el panel
    private void configurarPanel() {

        // Fondo transparente
        setOpaque(false);

        // Usa BorderLayout
        setLayout(new java.awt.BorderLayout());

        // El grid real de tarjetas ya NO es "this": es un panel aparte
        // que vive DENTRO de un JScrollPane. Antes panelGrid = this,
        // por eso nunca hubo scroll (this era directamente lo que
        // PanelContenido agregaba con CardLayout, sin envoltura).
        //
        // BUG QUE ESTO CORRIGE (1): un JPanel con FlowLayout dentro de
        // un JScrollPane, POR DEFECTO, no ajusta su ancho al del
        // viewport — Swing le deja "ancho infinito" para calcular su
        // tamaño preferido, así que FlowLayout pone TODAS las
        // tarjetas en una sola fila horizontal en vez de saltar de
        // línea, y el scroll termina siendo horizontal (o las
        // tarjetas quedan cortadas fuera del área visible) en vez de
        // vertical. Se soluciona con PanelGridDesplazable, que
        // implementa Scrollable y devuelve true en
        // getScrollableTracksViewportWidth(): así el panel SIEMPRE
        // toma el ancho del viewport.
        //
        // RejillaResponsiva NUNCA cambia el tamaño de la tarjeta (usa
        // siempre AdministradorTema.anchoTarjetaProducto()/altoTarjetaProducto(),
        // el mismo tamaño que ya está en git — ver UIConstants.java,
        // ANCHO_TARJETA_PRODUCTO / ALTO_TARJETA_PRODUCTO). El "2" de abajo
        // es el MÁXIMO de columnas: en pantalla grande caben 2 tarjetas
        // por fila; si el ancho disponible del viewport no alcanza para
        // 2 (pantalla chica / ventana angosta), automáticamente baja a 1
        // sola columna. El resto se resuelve con scroll vertical.
        // Crea el grid con scroll vertical correcto
        panelGrid = new PanelGridDesplazable(new RejillaResponsiva(
                // Espacio entre tarjetas
                UIConstants.ESPACIO_ENTRE_TARJETAS,
                // Máximo de 2 columnas
                2
        ));
        // Grid transparente
        panelGrid.setOpaque(false);

        // FabricaScroll.crear() deja el viewport OPACO con un color
        // sólido (para tablas y listas normales, donde eso es
        // correcto). Aquí no sirve: PanelProductos hereda de
        // PanelFondo, que pinta una imagen en paintComponent(); un
        // viewport opaque queda ENCIMA de esa imagen y la tapa por
        // completo. Por eso, a diferencia de las tablas del CRUD de
        // Administrador, aquí SÍ hay que forzar transparencia después
        // de crear el scroll.
        // Crea el scroll que contiene el grid
        javax.swing.JScrollPane scroll = View.Utils.FabricaScroll.crear(panelGrid);
        // Scroll transparente
        scroll.setOpaque(false);
        // Viewport transparente
        scroll.getViewport().setOpaque(false);

        // Agrega el scroll al centro
        add(scroll, java.awt.BorderLayout.CENTER);
    }

    // ==========================================================
    // CARGA DE PRODUCTOS
    // ==========================================================
    /**
     * Vuelve a traer los productos disponibles y repinta el grid. Público para
     * que Administrador pueda llamarlo desde otro panel si necesita refrescar
     * el catálogo del Cliente tras un cambio (por ejemplo, al agregar un
     * producto nuevo).
     */
    // Trae los productos disponibles y repinta el grid
    public void cargarProductos() {

        // Guarda la lista filtrada
        productosFiltroCategoria = productoService
                // Trae los productos disponibles
                .listarProductosDisponibles()
                // Los convierte en stream
                .stream()
                // Aplica el filtro
                .filter(filtro)
                // Los junta en una lista
                .collect(Collectors.toList());

        // Pinta las tarjetas
        renderizar(productosFiltroCategoria);
    }

    /**
     * Filtra por texto (nombre de producto) sobre la lista que ya pasó el
     * filtro de categoría/promoción, reutilizando el servicio ServicioBusqueda
     * que ya existía pero no estaba conectado a ninguna vista. No vuelve a
     * consultar la base de datos.
     *
     * @param texto Texto escrito en la barra de búsqueda. Vacío o null muestra
     * de nuevo todos los productos del filtro.
     */
    // Filtra por texto sobre la lista ya filtrada
    public void aplicarBusqueda(String texto) {

        // Pinta el resultado de la búsqueda
        renderizar(ServicioBusqueda.buscarProductos(productosFiltroCategoria, texto));
    }

    // Pinta el grid de tarjetas a partir de la lista final ya filtrada
    // (categoría + búsqueda de texto, si aplica).
    // Pinta el grid con la lista final
    private void renderizar(List<Producto> productos) {

        // Vacía el grid
        panelGrid.removeAll();

        // Recorre los productos
        for (Producto producto : productos) {

            // Crea la tarjeta del producto
            TarjetaProducto tarjeta = new TarjetaProducto(producto);

            // Registra qué hacer al agregar al carrito
            tarjeta.setAgregarCarritoListener(this::alAgregarAlCarrito);

            // Agrega la tarjeta al grid
            panelGrid.add(tarjeta);
        }

        // Recalcula el layout
        panelGrid.revalidate();
        // Repinta el grid
        panelGrid.repaint();
    }

    // ==========================================================
    // CARRITO
    // ==========================================================
    // Agrega un producto al carrito del usuario
    private void alAgregarAlCarrito(Producto producto, int cantidad) {

        // Si no hay usuario en sesión
        if (Sesion.getInstancia().getUsuario() == null) {

            // Muestra un aviso
            JOptionPane.showMessageDialog(
                    // Ventana padre
                    this,
                    // Mensaje
                    "Debes iniciar sesión para agregar productos al carrito.",
                    // Título
                    "Sesión requerida",
                    // Tipo advertencia
                    JOptionPane.WARNING_MESSAGE
            );

            // Termina el método
            return;
        }

        // Obtiene el id del usuario en sesión
        int idUsuario = Sesion.getInstancia().getUsuario().getIdUsuario();

        // Obtiene o crea su carrito activo
        Carrito carrito = carritoService.obtenerOCrearCarritoActivo(idUsuario);

        // Si no se pudo obtener el carrito
        if (carrito == null) {

            // Muestra un error
            JOptionPane.showMessageDialog(
                    // Ventana padre
                    this,
                    // Mensaje
                    "No se pudo crear el carrito para tu usuario.",
                    // Título
                    "Carrito no disponible",
                    // Tipo error
                    JOptionPane.ERROR_MESSAGE
            );

            // Termina el método
            return;
        }

        // Crea el detalle del carrito
        CarritoDetalle detalle = new CarritoDetalle(
                // Id 0 (aún no existe)
                0,
                // Carrito
                carrito,
                // Producto
                producto,
                // Cantidad
                cantidad,
                // Sin observaciones
                null
        );

        // Guarda el producto en el carrito
        boolean agregado = carritoDetalleService.agregarProducto(detalle);

        // Si no se pudo agregar
        if (!agregado) {

            // Muestra un error
            JOptionPane.showMessageDialog(
                    // Ventana padre
                    this,
                    // Mensaje
                    "No se pudo agregar el producto al carrito.",
                    // Título
                    "Error",
                    // Tipo error
                    JOptionPane.ERROR_MESSAGE
            );

            // Termina el método
            return;
        }

        // Muestra la confirmación en el hilo de Swing
        SwingUtilities.invokeLater(()
                // Diálogo de información
                -> JOptionPane.showMessageDialog(
                        // Ventana padre
                        this,
                        // Mensaje con el nombre del producto
                        producto.getNombre() + " se agregó al carrito.",
                        // Título
                        "Producto agregado",
                        // Tipo información
                        JOptionPane.INFORMATION_MESSAGE
                )
        );
    }

    // ==========================================================
    // GRID CON SCROLL VERTICAL CORRECTO
    // ==========================================================
    /**
     * JPanel que SÍ se ajusta correctamente al ancho de un JScrollPane. Un
     * JPanel normal no implementa Scrollable, así que el JScrollPane no lo
     * obliga a ajustarse al ancho del viewport y su layout termina calculando
     * el tamaño como si tuviera ancho infinito (el bug que hacía que Inicio se
     * viera como una fila cortada de tarjetas sin texto, o todas amontonadas en
     * una sola fila). Con getScrollableTracksViewportWidth() == true, el panel
     * siempre toma el ancho disponible del viewport; el layout real (GridLayout
     * de 2 columnas) se encarga de partir ese ancho en dos y solo queda scroll
     * vertical.
     */
    // Panel interno que se ajusta al ancho del scroll
    private static class PanelGridDesplazable extends JPanel implements Scrollable {

        // Constructor: recibe el layout
        PanelGridDesplazable(java.awt.LayoutManager layout) {
            // Pasa el layout a JPanel
            super(layout);
        }

        // Implementa Scrollable
        @Override
        // Sigue el ancho del viewport
        public boolean getScrollableTracksViewportWidth() {
            // Devuelve true
            return true;
        }

        // Implementa Scrollable
        @Override
        // No sigue el alto del viewport
        public boolean getScrollableTracksViewportHeight() {
            // Devuelve false
            return false;
        }

        // Implementa Scrollable
        @Override
        // Tamaño preferido del viewport
        public Dimension getPreferredScrollableViewportSize() {
            // Devuelve el tamaño preferido
            return getPreferredSize();
        }

        // Implementa Scrollable
        @Override
        // Incremento al usar la rueda
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            // Cuatro veces el espacio entre tarjetas
            return UIConstants.ESPACIO_ENTRE_TARJETAS * 4;
        }

        // Implementa Scrollable
        @Override
        // Incremento al hacer clic en la barra
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            // Si el scroll es vertical
            return orientation == javax.swing.SwingConstants.VERTICAL
                    // avanza el alto visible
                    ? visibleRect.height
                    // si no, el ancho visible
                    : visibleRect.width;
        }

    }

}

package Base;


import Model.Carrito;
import Model.CarritoDetalle;
import Model.Categoria;
import Model.Producto;
import Service.Implement.CarritoDetalleServiceImpl;
import Service.Implement.CarritoServiceImpl;
import Service.Implement.ProductoServiceImpl;
import Service.Interfaz.ICarritoDetalleService;
import Service.Interfaz.ICarritoService;
import Service.Interfaz.IProductoService;
import Service.ServicioBusqueda;
import Utils.Sesion;
import View.Componentes.PanelFondo;
import View.Componentes.RejillaResponsiva;
import View.Componentes.TarjetaProducto;
import View.Utils.UIConstants;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Panel genérico de catálogo: pinta un grid de TarjetaProducto a
 * partir de una lista de Producto filtrada.
 *
 * ES UNA SOLA CLASE para todas las categorías del Cliente
 * (Desayunos, Almuerzos, Postres, McCafé, Bebidas, Antojos,
 * Cajita Feliz) y también para Combos y Promociones. Las clases
 * que hoy existen como placeholder (PanelDesayunos, PanelBebidas,
 * etc.) dejan de tener contenido propio: solo instancian esta
 * clase con el filtro correcto.
 *
 * Ejemplo de uso dentro de PanelDesayunos:
 *
 *     public class PanelDesayunos extends PanelProductos {
 *         public PanelDesayunos() {
 *             super(producto -> producto.getCategoria() != null
 *                     && "Desayunos".equalsIgnoreCase(
 *                             producto.getCategoria().getNombre()));
 *         }
 *     }
 *
 * Ejemplo para Combos (filtra por nombre de categoría "Combos"):
 *
 *     public class PanelCombos extends PanelProductos {
 *         public PanelCombos() {
 *             super(producto -> producto.getCategoria() != null
 *                     && "Combos".equalsIgnoreCase(
 *                             producto.getCategoria().getNombre()));
 *         }
 *     }
 *
 * Ejemplo para Promociones (no depende de categoría, sino de si
 * el producto tiene promoción activa):
 *
 *     public class PanelPromociones extends PanelProductos {
 *         public PanelPromociones() {
 *             super(Producto::tienePromocion);
 *         }
 *     }
 *
 * Constructor alterno recibiendo directamente una Categoria (para
 * cuando el equipo agregue categorías dinámicas desde
 * Administrador y ya no haga falta escribir el nombre a mano):
 *
 *     new PanelProductos(categoriaSeleccionada);
 *
 * AVISO: IProductoService todavía no tiene listarPorCategoria().
 * Este panel filtra en memoria sobre listarProductosDisponibles()
 * mientras esa consulta no exista en Service/DAO. Cuando se
 * agregue, cambiar cargarProductos() para usarla directamente y
 * evitar traer todo el catálogo cada vez.
 * ===============================================================
 */
public class PanelProductos extends PanelFondo {

    private final IProductoService productoService;

    private final ICarritoService carritoService;

    private final ICarritoDetalleService carritoDetalleService;

    private final Predicate<Producto> filtro;

    private JPanel panelGrid;

    // Última lista que pasó el filtro de categoría/promoción (antes de
    // aplicar texto de búsqueda). Se guarda para no volver a consultar
    // la base de datos cada vez que el usuario escribe en la barra de
    // búsqueda: ServicioBusqueda.buscarProductos() filtra en memoria
    // sobre esta lista.
    private List<Producto> productosFiltroCategoria = List.of();

    public PanelProductos(Predicate<Producto> filtro) {

        super();

        this.productoService = new ProductoServiceImpl();
        this.carritoService = new CarritoServiceImpl();
        this.carritoDetalleService = new CarritoDetalleServiceImpl();
        this.filtro = filtro;

        configurarPanel();

        cargarProductos();
    }

    public PanelProductos(Categoria categoria) {

        this(producto -> producto.getCategoria() != null
                && categoria != null
                && producto.getCategoria().getIdCategoria() == categoria.getIdCategoria());
    }

    // ==========================================================
    // ESTRUCTURA
    // ==========================================================

    private void configurarPanel() {

        setOpaque(false);

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
        // BUG QUE ESTO CORRIGE (2): antes se usaba GridLayout(0, 2, ...),
        // que fuerza SIEMPRE 2 columnas fijas y además reparte el ancho
        // (y el alto) real del viewport entre las celdas por partes
        // iguales, IGNORANDO el tamaño preferido de la tarjeta. Eso traía
        // dos problemas: (a) en pantalla completa también se mostraban
        // varias tarjetas por fila/pantalla, encimadas/apretadas; y (b) al
        // repartir también el alto entre todas las filas visibles, con
        // varios productos cada fila quedaba más baja de lo que la
        // tarjeta necesita para mostrar completo su contenido, cortando lo
        // último del panel (la fila de cantidad -selectorCantidad, con sus
        // botones "-"/"+"- y el botón "Agregar al carrito" quedaban fuera
        // del área visible de la tarjeta, o se veían apretados/incompletos).
        //
        // RejillaResponsiva soluciona ambas cosas: NUNCA cambia el tamaño
        // de la tarjeta (usa siempre el tamaño real definido en el tema:
        // AdministradorTema.anchoTarjetaProducto()/altoTarjetaProducto(),
        // el mismo tamaño con el que ya estaba en git), y agrega tantas
        // filas verticales como haga falta -cada una con la ALTURA REAL de
        // la tarjeta, nunca comprimida- dejando el scroll vertical del
        // JScrollPane hacer el resto. El "2" de abajo es el máximo de
        // columnas; se deja en 1 para que, sin importar el tamaño de la
        // pantalla (chica o pantalla completa), SIEMPRE se muestre una
        // sola tarjeta por fila, tal como se pidió.
        panelGrid = new PanelGridDesplazable(new RejillaResponsiva(
                UIConstants.ESPACIO_ENTRE_TARJETAS,
                1
        ));
        panelGrid.setOpaque(false);

        // FabricaScroll.crear() deja el viewport OPACO con un color
        // sólido (para tablas y listas normales, donde eso es
        // correcto). Aquí no sirve: PanelProductos hereda de
        // PanelFondo, que pinta una imagen en paintComponent(); un
        // viewport opaque queda ENCIMA de esa imagen y la tapa por
        // completo. Por eso, a diferencia de las tablas del CRUD de
        // Administrador, aquí SÍ hay que forzar transparencia después
        // de crear el scroll.
        javax.swing.JScrollPane scroll = View.Utils.FabricaScroll.crear(panelGrid);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        add(scroll, java.awt.BorderLayout.CENTER);
    }

    // ==========================================================
    // CARGA DE PRODUCTOS
    // ==========================================================

    /**
     * Vuelve a traer los productos disponibles y repinta el grid.
     * Público para que Administrador pueda llamarlo desde otro
     * panel si necesita refrescar el catálogo del Cliente tras
     * un cambio (por ejemplo, al agregar un producto nuevo).
     */
    public void cargarProductos() {

        productosFiltroCategoria = productoService
                .listarProductosDisponibles()
                .stream()
                .filter(filtro)
                .collect(Collectors.toList());

        renderizar(productosFiltroCategoria);
    }

    /**
     * Filtra por texto (nombre de producto) sobre la lista que ya pasó
     * el filtro de categoría/promoción, reutilizando el servicio
     * ServicioBusqueda que ya existía pero no estaba conectado a
     * ninguna vista. No vuelve a consultar la base de datos.
     *
     * @param texto Texto escrito en la barra de búsqueda. Vacío o null
     *              muestra de nuevo todos los productos del filtro.
     */
    public void aplicarBusqueda(String texto) {

        renderizar(ServicioBusqueda.buscarProductos(productosFiltroCategoria, texto));
    }

    // Pinta el grid de tarjetas a partir de la lista final ya filtrada
    // (categoría + búsqueda de texto, si aplica).
    private void renderizar(List<Producto> productos) {

        panelGrid.removeAll();

        for (Producto producto : productos) {

            TarjetaProducto tarjeta = new TarjetaProducto(producto);

            tarjeta.setAgregarCarritoListener(this::alAgregarAlCarrito);

            panelGrid.add(tarjeta);
        }

        panelGrid.revalidate();
        panelGrid.repaint();
    }

    // ==========================================================
    // CARRITO
    // ==========================================================

    private void alAgregarAlCarrito(Producto producto, int cantidad) {

        if (Sesion.getInstancia().getUsuario() == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debes iniciar sesión para agregar productos al carrito.",
                    "Sesión requerida",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int idUsuario = Sesion.getInstancia().getUsuario().getIdUsuario();

        Carrito carrito = carritoService.buscarPorUsuario(idUsuario);

        if (carrito == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró un carrito activo para tu usuario.",
                    "Carrito no disponible",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        CarritoDetalle detalle = new CarritoDetalle(
                0,
                carrito,
                producto,
                cantidad,
                null
        );

        boolean agregado = carritoDetalleService.agregarProducto(detalle);

        if (!agregado) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo agregar el producto al carrito.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(
                        this,
                        producto.getNombre() + " se agregó al carrito.",
                        "Producto agregado",
                        JOptionPane.INFORMATION_MESSAGE
                )
        );
    }

    // ==========================================================
    // GRID CON SCROLL VERTICAL CORRECTO
    // ==========================================================

    /**
     * JPanel que SÍ se ajusta correctamente al ancho de un
     * JScrollPane. Un JPanel normal no implementa Scrollable, así que
     * el JScrollPane no lo obliga a ajustarse al ancho del viewport
     * y su layout termina calculando el tamaño como si tuviera
     * ancho infinito (el bug que hacía que Inicio se viera como una
     * fila cortada de tarjetas sin texto, o todas amontonadas en una
     * sola fila). Con getScrollableTracksViewportWidth() == true, el
     * panel siempre toma el ancho disponible del viewport; el layout
     * real (GridLayout de 2 columnas) se encarga de partir ese ancho
     * en dos y solo queda scroll vertical.
     */
    private static class PanelGridDesplazable extends JPanel implements Scrollable {

        PanelGridDesplazable(java.awt.LayoutManager layout) {
            super(layout);
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return UIConstants.ESPACIO_ENTRE_TARJETAS * 4;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return orientation == javax.swing.SwingConstants.VERTICAL
                    ? visibleRect.height
                    : visibleRect.width;
        }

    }

}
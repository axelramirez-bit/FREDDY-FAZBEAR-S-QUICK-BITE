package Base;

import View.Componentes.BarraBusqueda;
import View.Componentes.PanelFondo;
import View.Componentes.TarjetaKPI;
import View.Utils.FabricaBotones;
import View.Utils.FabricaCampos;
import View.Utils.FabricaTablas;
import View.Utils.RenderizadorEstado;
import View.Utils.UIConstants;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Panel genérico de tabla + Agregar/Editar/Eliminar para las
 * pantallas CRUD de Administrador (Categorías, Productos,
 * Promociones, Usuarios, Trabajadores...).
 *
 * VERSIÓN EXTENDIDA — agrega, sobre la versión original, todo lo
 * que pedían los bocetos de las 10 pantallas de Administrador:
 *
 *   • Fila de tarjetas KPI arriba de la tabla (opcional).
 *   • Barra de búsqueda + un combo de filtro (opcional).
 *   • Columna de "Estado" pintada como badge de color (opcional).
 *   • Paginación (en memoria, sobre lo que ya trajo el Service).
 *
 * SIGUE SIENDO COMPATIBLE con cualquier subclase que ya exista
 * siguiendo el patrón original: los 6 métodos abstractos de
 * siempre (getColumnas, listarTodos, convertirFila, alAgregar,
 * alEditar, alEliminar) no cambiaron ni de nombre ni de firma.
 * Todo lo nuevo son métodos con implementación por defecto que
 * una subclase sobreescribe SOLO si quiere esa pieza — si no los
 * tocas, el panel se comporta exactamente como antes (una barra
 * de 3 botones + una tabla, sin KPIs, sin badges, sin paginación).
 *
 * EJEMPLO — Productos, replicando el boceto de la interfaz 4
 * (tarjetas KPI, búsqueda + filtro por categoría, badge de
 * "Disponible"/"Sin stock", paginación):
 *
 *     public class PanelProductosAdmin extends PanelCrudBase<Producto> {
 *
 *         private final IProductoService productoService = new ProductoServiceImpl();
 *
 *         public PanelProductosAdmin() {
 *             super();
 *         }
 *
 *         protected Object[] getColumnas() {
 *             return new Object[]{ "ID", "Producto", "Categoría", "Precio", "Stock", "Estado", "Acciones" };
 *         }
 *
 *         protected List<Producto> listarTodos() {
 *             return productoService.listar();
 *         }
 *
 *         protected Object[] convertirFila(Producto p) {
 *             return new Object[]{
 *                     p.getIdProducto(), p.getNombre(), p.getCategoria().getNombre(),
 *                     "Q" + p.getPrecio(), p.getStock(),
 *                     p.getStock() == 0 ? "Sin stock" : (p.isDisponible() ? "Disponible" : "Inactivo"),
 *                     "" // la columna de Acciones la resuelve tu propio renderer de botones, si ya tienes uno
 *             };
 *         }
 *
 *         // ---- lo nuevo, todo opcional ----
 *
 *         protected List<TarjetaKPI> crearTarjetasResumen() {
 *             List<TarjetaKPI> tarjetas = new ArrayList<>();
 *             tarjetas.add(new TarjetaKPI(FabricaIconos.crear("icon_comida", 24), "Total Productos", String.valueOf(productoService.listar().size()), ""));
 *             tarjetas.add(new TarjetaKPI(FabricaIconos.crear("icon_check", 24), "Disponibles", String.valueOf(productoService.contarDisponibles()), ""));
 *             tarjetas.add(new TarjetaKPI(FabricaIconos.crear("icon_alerta", 24), "Sin Stock", String.valueOf(productoService.contarSinStock()), ""));
 *             tarjetas.add(new TarjetaKPI(FabricaIconos.crear("icon_promociones", 24), "Con Promoción Activa", String.valueOf(productoService.contarConPromocion()), ""));
 *             return tarjetas;
 *         }
 *
 *         protected String textoBotonAgregar() { return "Nuevo Producto"; }
 *
 *         protected String placeholderBusqueda() { return "Buscar producto..."; }
 *
 *         protected String[] opcionesFiltro() {
 *             // "Todas" siempre debe ir primero — significa "sin filtro".
 *             return new String[]{ "Todas", "Hamburguesas", "Snacks", "Bebidas", "Platos", "Combos" };
 *         }
 *
 *         protected boolean coincideBusqueda(Producto p, String texto) {
 *             return p.getNombre().toLowerCase().contains(texto.toLowerCase());
 *         }
 *
 *         protected boolean coincideFiltro(Producto p, String filtro) {
 *             return p.getCategoria().getNombre().equalsIgnoreCase(filtro);
 *         }
 *
 *         protected int columnaEstado() { return 5; } // índice de "Estado" en getColumnas()
 *
 *         protected void alAgregar() { ... }
 *         protected void alEditar(Producto p) { ... }
 *         protected boolean alEliminar(Producto p) { return productoService.cambiarEstado(p.getIdProducto(), false); }
 *     }
 *
 * Si una pantalla no necesita algo (ej. Categorías no necesita
 * combo de filtro), simplemente no sobreescribe ese método y esa
 * pieza no aparece — no hay que "apagarla" a mano.
 * ===============================================================
 */
public abstract class PanelCrudBase<T> extends PanelFondo {

    private JTable tabla;

    private DefaultTableModel modeloTabla;

    /** Todo lo que trajo listarTodos(), sin filtrar. */
    private List<T> todasLasEntidades = new ArrayList<>();

    /** Lo que queda después de aplicar búsqueda + combo. */
    private List<T> entidadesFiltradas = new ArrayList<>();

    /** Solo la página visible ahora mismo — es lo que resuelve fila -> entidad. */
    private List<T> entidadesPagina = new ArrayList<>();

    private int paginaActual = 1;

    private BarraBusqueda barraBusqueda;

    private JComboBox<String> comboFiltro;

    private JLabel lblResumenPaginacion;

    private JPanel panelBotonesPagina;

    protected PanelCrudBase() {

        super();

        setOpaque(false);
        setLayout(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));

        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPiePaginacion(), BorderLayout.SOUTH);
    }

    // ==========================================================
    // MÉTODOS QUE CADA CRUD CONCRETO DEBE IMPLEMENTAR (sin cambios)
    // ==========================================================

    protected abstract Object[] getColumnas();

    protected abstract List<T> listarTodos();

    protected abstract Object[] convertirFila(T entidad);

    protected abstract void alAgregar();

    protected abstract void alEditar(T entidad);

    protected abstract boolean alEliminar(T entidad);

    // ==========================================================
    // GANCHOS NUEVOS — TODOS OPCIONALES (implementación por
    // defecto = "esta pantalla no usa esta pieza")
    // ==========================================================

    /**
     * Tarjetas KPI arriba de la tabla (ej. Total/Disponibles/Sin
     * Stock/Con Promoción). Regresa null o una lista vacía si la
     * pantalla no necesita resumen.
     */
    protected List<TarjetaKPI> crearTarjetasResumen() {
        return null;
    }

    /** Texto del botón principal, ej. "Nuevo Producto", "Nuevo Trabajador". */
    protected String textoBotonAgregar() {
        return "Agregar";
    }

    /** Texto de fondo de la barra de búsqueda. */
    protected String placeholderBusqueda() {
        return "Buscar...";
    }

    /**
     * Opciones del combo de filtro. La primera opción SIEMPRE se
     * trata como "sin filtro" (ej. "Todas", "Todos los roles").
     * Regresa null si la pantalla no necesita combo.
     */
    protected String[] opcionesFiltro() {
        return null;
    }

    /** Cómo decide esta pantalla si una entidad coincide con el texto buscado. */
    protected boolean coincideBusqueda(T entidad, String texto) {
        return true;
    }

    /** Cómo decide esta pantalla si una entidad coincide con la opción de filtro elegida. */
    protected boolean coincideFiltro(T entidad, String filtroSeleccionado) {
        return true;
    }

    /**
     * Índice (dentro de getColumnas()) de la columna que debe
     * pintarse como badge de color en vez de texto plano.
     * -1 = ninguna columna es badge.
     */
    protected int columnaEstado() {
        return -1;
    }

    /** Cuántas filas mostrar por página. */
    protected int filasPorPagina() {
        return 7;
    }

    // ==========================================================
    // ENCABEZADO: RESUMEN KPI + BARRA DE BÚSQUEDA/FILTRO/AGREGAR
    // ==========================================================

    private JPanel crearEncabezado() {

        JPanel encabezado = new JPanel(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));
        encabezado.setOpaque(false);

        JPanel filaResumen = crearFilaResumen();

        if (filaResumen != null) {
            encabezado.add(filaResumen, BorderLayout.NORTH);
        }

        encabezado.add(crearBarraAcciones(), BorderLayout.SOUTH);

        return encabezado;
    }

    private JPanel crearFilaResumen() {

        List<TarjetaKPI> tarjetas = crearTarjetasResumen();

        if (tarjetas == null || tarjetas.isEmpty()) {
            return null;
        }

        JPanel fila = new JPanel(new FlowLayout(
                FlowLayout.LEFT,
                UIConstants.ESPACIO_SUBTITULO,
                0
        ));

        fila.setOpaque(false);

        for (TarjetaKPI tarjeta : tarjetas) {
            fila.add(tarjeta);
        }

        return fila;
    }

    private JPanel crearBarraAcciones() {

        JPanel barra = new JPanel(new BorderLayout());
        barra.setOpaque(false);

        // ---- lado izquierdo: búsqueda + filtro ----
        JPanel ladoIzquierdo = new JPanel(new FlowLayout(
                FlowLayout.LEFT,
                UIConstants.ESPACIO_SUBTITULO,
                0
        ));
        ladoIzquierdo.setOpaque(false);

        barraBusqueda = new BarraBusqueda(placeholderBusqueda());

        barraBusqueda.getCampo().getDocument().addDocumentListener(
                new DocumentListener() {

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        aplicarFiltros();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        aplicarFiltros();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        aplicarFiltros();
                    }
                }
        );

        ladoIzquierdo.add(barraBusqueda);

        String[] opciones = opcionesFiltro();

        if (opciones != null && opciones.length > 0) {

            comboFiltro = FabricaCampos.crearCombo();

            for (String opcion : opciones) {
                comboFiltro.addItem(opcion);
            }

            comboFiltro.addActionListener(e -> aplicarFiltros());

            ladoIzquierdo.add(comboFiltro);
        }

        // ---- lado derecho: botones ----
        JPanel ladoDerecho = new JPanel(new FlowLayout(
                FlowLayout.RIGHT,
                UIConstants.ESPACIO_SUBTITULO,
                0
        ));
        ladoDerecho.setOpaque(false);

        JButton btnAgregar = FabricaBotones.crearPrimario(textoBotonAgregar());
        JButton btnEditar = FabricaBotones.crearSecundario("Editar");
        JButton btnEliminar = FabricaBotones.crearSecundario("Eliminar");

        btnAgregar.addActionListener(e -> {
            alAgregar();
            cargarDatos();
        });

        btnEditar.addActionListener(e -> editarSeleccionado());

        btnEliminar.addActionListener(e -> eliminarSeleccionado());

        ladoDerecho.add(btnEditar);
        ladoDerecho.add(btnEliminar);
        ladoDerecho.add(btnAgregar);

        barra.add(ladoIzquierdo, BorderLayout.WEST);
        barra.add(ladoDerecho, BorderLayout.EAST);

        return barra;
    }

    // ==========================================================
    // TABLA
    // ==========================================================

    private JPanel crearPanelTabla() {

        this.modeloTabla = FabricaTablas.crearModeloSoloLectura(getColumnas());

        this.tabla = FabricaTablas.crearTabla(modeloTabla);

        int columnaEstado = columnaEstado();

        if (columnaEstado >= 0 && columnaEstado < getColumnas().length) {

            tabla.getColumnModel()
                    .getColumn(columnaEstado)
                    .setCellRenderer(new RenderizadorEstado());
        }

        return FabricaTablas.crearPanelTabla(tabla);
    }

    // ==========================================================
    // PIE: PAGINACIÓN
    // ==========================================================

    private JPanel crearPiePaginacion() {

        JPanel pie = new JPanel(new BorderLayout());
        pie.setOpaque(false);

        lblResumenPaginacion = new JLabel();
        lblResumenPaginacion.setHorizontalAlignment(SwingConstants.LEFT);

        panelBotonesPagina = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        panelBotonesPagina.setOpaque(false);

        pie.add(lblResumenPaginacion, BorderLayout.WEST);
        pie.add(panelBotonesPagina, BorderLayout.EAST);

        return pie;
    }

    private void repintarPaginacion() {

        int totalFilas = entidadesFiltradas.size();

        int filasPorPagina = Math.max(1, filasPorPagina());

        int totalPaginas = Math.max(1, (int) Math.ceil(totalFilas / (double) filasPorPagina));

        if (paginaActual > totalPaginas) {
            paginaActual = totalPaginas;
        }

        if (paginaActual < 1) {
            paginaActual = 1;
        }

        int desde = totalFilas == 0 ? 0 : (paginaActual - 1) * filasPorPagina + 1;
        int hasta = Math.min(paginaActual * filasPorPagina, totalFilas);

        lblResumenPaginacion.setText(
                "Mostrando " + desde + " a " + hasta + " de " + totalFilas
        );

        panelBotonesPagina.removeAll();

        JButton btnAnterior = FabricaBotones.crearAccion("<");
        btnAnterior.setEnabled(paginaActual > 1);
        btnAnterior.addActionListener(e -> irAPagina(paginaActual - 1));
        panelBotonesPagina.add(btnAnterior);

        // Evita pintar 100 botones si hay muchas páginas: solo se
        // muestran unas cuantas alrededor de la página actual.
        int primeraVisible = Math.max(1, paginaActual - 2);
        int ultimaVisible = Math.min(totalPaginas, primeraVisible + 4);

        for (int numero = primeraVisible; numero <= ultimaVisible; numero++) {

            JButton btnPagina = FabricaBotones.crearAccion(String.valueOf(numero));

            if (numero == paginaActual) {
                btnPagina.setEnabled(false);
            }

            final int destino = numero;
            btnPagina.addActionListener(e -> irAPagina(destino));

            panelBotonesPagina.add(btnPagina);
        }

        JButton btnSiguiente = FabricaBotones.crearAccion(">");
        btnSiguiente.setEnabled(paginaActual < totalPaginas);
        btnSiguiente.addActionListener(e -> irAPagina(paginaActual + 1));
        panelBotonesPagina.add(btnSiguiente);

        panelBotonesPagina.revalidate();
        panelBotonesPagina.repaint();
    }

    private void irAPagina(int numeroPagina) {
        paginaActual = numeroPagina;
        repintarTablaConPaginaActual();
    }

    // ==========================================================
    // CARGA DE DATOS
    // ==========================================================

    /**
     * Vuelve a traer todas las entidades desde el Service,
     * reaplica búsqueda/filtro y repinta la tabla desde la
     * página 1. Público para que el formulario de alta/edición
     * pueda llamarlo al cerrar y refrescar la lista.
     */
    public void cargarDatos() {

        this.todasLasEntidades = listarTodos();

        paginaActual = 1;

        aplicarFiltros();
    }

    /**
     * Reaplica búsqueda + combo sobre lo que ya está en memoria
     * (todasLasEntidades) sin volver a consultar el Service — se
     * usa en cada tecla escrita o cada cambio de combo.
     */
    private void aplicarFiltros() {

        String texto = barraBusqueda == null ? "" : barraBusqueda.getTexto();

        String filtroSeleccionado =
                (comboFiltro == null || comboFiltro.getSelectedIndex() <= 0)
                        ? null
                        : (String) comboFiltro.getSelectedItem();

        entidadesFiltradas = new ArrayList<>();

        for (T entidad : todasLasEntidades) {

            boolean pasaBusqueda = texto.isEmpty() || coincideBusqueda(entidad, texto);

            boolean pasaFiltro = filtroSeleccionado == null || coincideFiltro(entidad, filtroSeleccionado);

            if (pasaBusqueda && pasaFiltro) {
                entidadesFiltradas.add(entidad);
            }
        }

        paginaActual = 1;

        repintarTablaConPaginaActual();
    }

    private void repintarTablaConPaginaActual() {

        int filasPorPagina = Math.max(1, filasPorPagina());

        int desde = (paginaActual - 1) * filasPorPagina;
        int hasta = Math.min(desde + filasPorPagina, entidadesFiltradas.size());

        entidadesPagina = desde >= hasta
                ? new ArrayList<>()
                : new ArrayList<>(entidadesFiltradas.subList(desde, hasta));

        modeloTabla.setRowCount(0);

        for (T entidad : entidadesPagina) {
            modeloTabla.addRow(convertirFila(entidad));
        }

        repintarPaginacion();
    }

    // ==========================================================
    // EDITAR / ELIMINAR SOBRE LA FILA SELECCIONADA
    // ==========================================================

    private void editarSeleccionado() {

        T entidad = obtenerSeleccionado();

        if (entidad == null) {
            return;
        }

        alEditar(entidad);

        cargarDatos();
    }

    private void eliminarSeleccionado() {

        T entidad = obtenerSeleccionado();

        if (entidad == null) {
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar el elemento seleccionado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        boolean eliminado = alEliminar(entidad);

        if (!eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar el elemento seleccionado.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        cargarDatos();
    }

    private T obtenerSeleccionado() {

        int fila = tabla.getSelectedRow();

        if (fila < 0 || fila >= entidadesPagina.size()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un elemento de la tabla.",
                    "Ningún elemento seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        return entidadesPagina.get(fila);
    }

}
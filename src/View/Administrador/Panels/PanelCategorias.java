package View.Administrador.Panels;

import Model.Categoria;
import Service.Implement.CategoriaServiceImpl;
import Service.Implement.ProductoServiceImpl;
import Service.Interfaz.ICategoriaService;
import Service.Interfaz.IProductoService;
import View.Componentes.BarraBusqueda;
import View.Componentes.PanelFondo;
import View.Componentes.TarjetaKPI;
import View.Utils.FabricaBotones;
import View.Utils.FabricaEtiquetas;
import View.Utils.FabricaIconos;
import View.Utils.FabricaTablas;
import View.Utils.RenderizadorEstado;
import View.Utils.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Gestión de Categorías del Administrador — reescrita a mano
 * (deja de extender PanelCrudBase) para que la pantalla calce con
 * el mockup real: 3 tarjetas KPI arriba, búsqueda, tabla con
 * badge de Estado y paginación estilo "Mostrando X a Y de Z".
 *
 * IMPORTANTE — "Prod. sin categoría":
 * ProductoDAOImpl.listar() usa INNER JOIN con categoria (ver
 * comentario en ese archivo), así que un producto con
 * id_categoria NULL simplemente NO aparece en ningún listado del
 * sistema — ni aquí ni en el panel de Productos. Por eso esa
 * tarjeta no puede calcularse con exactitud desde el código actual
 * y se muestra como "N/D" con una nota explicando el motivo, en
 * vez de inventar un número. Si el equipo quiere ese dato real,
 * ProductoDAOImpl.listar() necesita cambiar a LEFT JOIN (y revisar
 * a todo lo que llama producto.getCategoria().getNombre() sin
 * checar null primero).
 * ===============================================================
 */
public class PanelCategorias extends PanelFondo {

    private static final int COLUMNA_ESTADO = 3;

    // ANTES: 7 filas por página, así que un catálogo de 9 categorías
    // mostraba solo 7 y había que darle clic a "Siguiente" para ver
    // el resto — el usuario lo veía como "no muestra todas". Con el
    // scroll vertical del dashboard ya funcionando (ver FabricaScroll),
    // no hace falta paginar listas de este tamaño: se sube el límite
    // para que, en la práctica, todo el catálogo quepa en una sola
    // "página" y la tabla simplemente crezca con scroll.
    private static final int FILAS_POR_PAGINA = 500;

    private final ICategoriaService categoriaService = new CategoriaServiceImpl();
    private final IProductoService productoService = new ProductoServiceImpl();

    private List<Categoria> todasLasCategorias = new ArrayList<>();
    private List<Categoria> categoriasFiltradas = new ArrayList<>();
    private List<Categoria> categoriasPagina = new ArrayList<>();

    private TarjetaKPI tarjetaTotal;
    private TarjetaKPI tarjetaActivas;
    private TarjetaKPI tarjetaProdSinCategoria;

    private BarraBusqueda barraBusqueda;

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    private JLabel lblResumenPaginacion;
    private JPanel panelBotonesPagina;
    private int paginaActual = 1;

    public PanelCategorias() {

        super();

        setOpaque(false);
        setLayout(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));

        JPanel norte = new JPanel(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));
        norte.setOpaque(false);
        norte.add(FabricaEtiquetas.crearTitulo("CATEGORÍAS"), BorderLayout.NORTH);
        norte.add(crearFilaKPI(), BorderLayout.CENTER);
        norte.add(crearBarraAcciones(), BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPiePaginacion(), BorderLayout.SOUTH);

        cargarDatos();
    }

    // ==========================================================
    // KPI
    // ==========================================================

    private JPanel crearFilaKPI() {

        JPanel fila = new JPanel(new GridLayout(1, 3, UIConstants.ESPACIO_SUBTITULO, 0));
        fila.setOpaque(false);
        fila.setBorder(BorderFactory.createEmptyBorder(UIConstants.ESPACIO_SUBTITULO, 0, 0, 0));

        tarjetaTotal = new TarjetaKPI(FabricaIconos.productos(), "Total Categorías", "0", "");
        tarjetaActivas = new TarjetaKPI(FabricaIconos.productos(), "Categorías Activas", "0", "");
        tarjetaProdSinCategoria = new TarjetaKPI(FabricaIconos.productos(), "Prod. sin categoría", "N/D", "");

        fila.add(tarjetaTotal);
        fila.add(tarjetaActivas);
        fila.add(tarjetaProdSinCategoria);

        return fila;
    }

    // ==========================================================
    // BÚSQUEDA + BOTONES
    // ==========================================================

    private JPanel crearBarraAcciones() {

        JPanel barra = new JPanel(new BorderLayout());
        barra.setOpaque(false);

        JPanel ladoIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, UIConstants.ESPACIO_SUBTITULO, 0));
        ladoIzquierdo.setOpaque(false);

        barraBusqueda = new BarraBusqueda("Buscar categoría...");
        barraBusqueda.getCampo().getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {
                    @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
                    @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
                    @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
                });

        ladoIzquierdo.add(barraBusqueda);

        JPanel ladoDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, UIConstants.ESPACIO_SUBTITULO, 0));
        ladoDerecho.setOpaque(false);

        JButton btnEditar = FabricaBotones.crearSecundario("Editar");
        JButton btnEstado = FabricaBotones.crearSecundario("Desactivar");
        JButton btnNueva = FabricaBotones.crearPrimario("Nueva Categoría");

        btnEditar.addActionListener(e -> editarSeleccionada());
        btnEstado.addActionListener(e -> cambiarEstadoSeleccionada());
        btnNueva.addActionListener(e -> abrirFormulario(null));

        ladoDerecho.add(btnEditar);
        ladoDerecho.add(btnEstado);
        ladoDerecho.add(btnNueva);

        barra.add(ladoIzquierdo, BorderLayout.WEST);
        barra.add(ladoDerecho, BorderLayout.EAST);

        return barra;
    }

    // ==========================================================
    // TABLA
    // ==========================================================

    private JPanel crearPanelTabla() {

        Object[] columnas = {"ID", "Categoría", "Descripción", "Estado"};

        modeloTabla = FabricaTablas.crearModeloSoloLectura(columnas);
        tabla = FabricaTablas.crearTabla(modeloTabla);

        tabla.getColumnModel().getColumn(COLUMNA_ESTADO).setCellRenderer(new RenderizadorEstado());

        return FabricaTablas.crearPanelTabla(tabla);
    }

    // ==========================================================
    // PIE: PAGINACIÓN + NOTA
    // ==========================================================

    private JPanel crearPiePaginacion() {

        JPanel pie = new JPanel(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO / 2));
        pie.setOpaque(false);
        pie.setBorder(BorderFactory.createEmptyBorder(UIConstants.ESPACIO_SUBTITULO / 2, 0, 0, 0));

        JPanel filaPaginacion = new JPanel(new BorderLayout());
        filaPaginacion.setOpaque(false);

        lblResumenPaginacion = new JLabel();
        lblResumenPaginacion.setHorizontalAlignment(SwingConstants.LEFT);

        panelBotonesPagina = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        panelBotonesPagina.setOpaque(false);

        filaPaginacion.add(lblResumenPaginacion, BorderLayout.WEST);
        filaPaginacion.add(panelBotonesPagina, BorderLayout.EAST);

        pie.add(filaPaginacion, BorderLayout.NORTH);
        pie.add(FabricaEtiquetas.crearPequeño(
                "Nota: \"Prod. sin categoría\" queda como N/D porque la consulta actual de "
                        + "productos excluye los que no tienen categoría asignada (ver comentario en el código)."
        ), BorderLayout.SOUTH);

        return pie;
    }

    private void repintarPaginacion() {

        int totalFilas = categoriasFiltradas.size();
        int totalPaginas = Math.max(1, (int) Math.ceil(totalFilas / (double) FILAS_POR_PAGINA));

        if (paginaActual > totalPaginas) paginaActual = totalPaginas;
        if (paginaActual < 1) paginaActual = 1;

        int desde = totalFilas == 0 ? 0 : (paginaActual - 1) * FILAS_POR_PAGINA + 1;
        int hasta = Math.min(paginaActual * FILAS_POR_PAGINA, totalFilas);

        lblResumenPaginacion.setText("Mostrando " + desde + " a " + hasta + " de " + totalFilas);

        panelBotonesPagina.removeAll();

        JButton btnAnterior = FabricaBotones.crearAccion("<");
        btnAnterior.setEnabled(paginaActual > 1);
        btnAnterior.addActionListener(e -> irAPagina(paginaActual - 1));
        panelBotonesPagina.add(btnAnterior);

        int primeraVisible = Math.max(1, paginaActual - 2);
        int ultimaVisible = Math.min(totalPaginas, primeraVisible + 4);

        for (int numero = primeraVisible; numero <= ultimaVisible; numero++) {
            JButton btnPagina = FabricaBotones.crearAccion(String.valueOf(numero));
            if (numero == paginaActual) btnPagina.setEnabled(false);
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
    // DATOS
    // ==========================================================

    public void cargarDatos() {

        todasLasCategorias = categoriaService.listar();

        int activas = 0;
        for (Categoria categoria : todasLasCategorias) {
            if (categoria.isEstado()) activas++;
        }

        tarjetaTotal.actualizar(String.valueOf(todasLasCategorias.size()), "");
        tarjetaActivas.actualizar(String.valueOf(activas), "");
        // "Prod. sin categoría" se deja en N/D (ver nota de clase);
        // no se sobreescribe con un número inventado.

        paginaActual = 1;
        aplicarFiltros();
    }

    private void aplicarFiltros() {

        String texto = barraBusqueda == null ? "" : barraBusqueda.getTexto().toLowerCase();

        categoriasFiltradas = new ArrayList<>();

        for (Categoria categoria : todasLasCategorias) {
            if (texto.isEmpty() || categoria.getNombre().toLowerCase().contains(texto)) {
                categoriasFiltradas.add(categoria);
            }
        }

        paginaActual = 1;
        repintarTablaConPaginaActual();
    }

    private void repintarTablaConPaginaActual() {

        int desde = (paginaActual - 1) * FILAS_POR_PAGINA;
        int hasta = Math.min(desde + FILAS_POR_PAGINA, categoriasFiltradas.size());

        categoriasPagina = desde >= hasta
                ? new ArrayList<>()
                : new ArrayList<>(categoriasFiltradas.subList(desde, hasta));

        modeloTabla.setRowCount(0);

        for (Categoria categoria : categoriasPagina) {
            modeloTabla.addRow(new Object[]{
                    categoria.getIdCategoria(),
                    categoria.getNombre(),
                    categoria.getDescripcion() == null ? "-" : categoria.getDescripcion(),
                    categoria.isEstado() ? "Activa" : "Inactiva"
            });
        }

        repintarPaginacion();
    }

    // ==========================================================
    // ACCIONES SOBRE LA FILA SELECCIONADA
    // ==========================================================

    private Categoria obtenerSeleccionada() {

        int fila = tabla.getSelectedRow();

        if (fila < 0 || fila >= categoriasPagina.size()) {
            JOptionPane.showMessageDialog(this, "Selecciona una categoría de la tabla.",
                    "Ninguna categoría seleccionada", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return categoriasPagina.get(fila);
    }

    private void editarSeleccionada() {
        Categoria categoria = obtenerSeleccionada();
        if (categoria != null) {
            abrirFormulario(categoria);
        }
    }

    /**
     * No hay borrado físico: se activa/desactiva (mismo criterio de
     * siempre en este panel) para no romper los productos que ya
     * tienen asignada la categoría.
     */
    private void cambiarEstadoSeleccionada() {

        Categoria categoria = obtenerSeleccionada();

        if (categoria == null) {
            return;
        }

        boolean nuevoEstado = !categoria.isEstado();
        String accion = nuevoEstado ? "activar" : "desactivar";

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas " + accion + " \"" + categoria.getNombre() + "\"?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        if (!categoriaService.cambiarEstado(categoria.getIdCategoria(), nuevoEstado)) {
            JOptionPane.showMessageDialog(this, "No se pudo " + accion + " la categoría.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        cargarDatos();
    }

    private void abrirFormulario(Categoria categoriaExistente) {

        boolean esEdicion = categoriaExistente != null;

        JTextField txtNombre = new JTextField(esEdicion ? categoriaExistente.getNombre() : "");
        JTextField txtDescripcion = new JTextField(esEdicion ? categoriaExistente.getDescripcion() : "");
        JTextField txtOrden = new JTextField(esEdicion ? String.valueOf(categoriaExistente.getOrden()) : "0");

        Object[] campos = {
                "Nombre:", txtNombre,
                "Descripción:", txtDescripcion,
                "Orden:", txtOrden
        };

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                campos,
                esEdicion ? "Editar categoría" : "Nueva categoría",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (confirmacion != JOptionPane.OK_OPTION) {
            return;
        }

        if (txtNombre.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "El nombre de la categoría es obligatorio.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int orden = parsearOrden(txtOrden.getText());

        Categoria categoriaAValidar;
        boolean exito;

        if (esEdicion) {
            categoriaExistente.setNombre(txtNombre.getText().trim());
            categoriaExistente.setDescripcion(txtDescripcion.getText().trim());
            categoriaExistente.setOrden(orden);
            categoriaAValidar = categoriaExistente;
        } else {
            categoriaAValidar = new Categoria(0, txtNombre.getText().trim(),
                    txtDescripcion.getText().trim(), null, null, null, orden, true);
        }

        String motivoInvalido = categoriaService.validar(categoriaAValidar);
        if (motivoInvalido != null) {
            JOptionPane.showMessageDialog(this, motivoInvalido,
                    "No se pudo guardar la categoría", JOptionPane.WARNING_MESSAGE);
            return;
        }

        exito = esEdicion
                ? categoriaService.actualizar(categoriaAValidar)
                : categoriaService.guardar(categoriaAValidar);

        if (!exito) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar la categoría. Verifica tu conexión e inténtalo de nuevo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        cargarDatos();
    }

    private int parsearOrden(String texto) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
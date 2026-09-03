package View.Administrador.Panels;

import Model.Categoria;
import Model.Producto;
import Model.Promocion;
import Service.Implement.CategoriaServiceImpl;
import Service.Implement.ProductoServiceImpl;
import Service.Implement.PromocionServiceImpl;
import Service.Interfaz.ICategoriaService;
import Service.Interfaz.IProductoService;
import Service.Interfaz.IPromocionService;
import View.Componentes.BarraBusqueda;
import View.Componentes.PanelFondo;
import View.Componentes.TarjetaKPI;
import View.Utils.FabricaBotones;
import View.Utils.FabricaCampos;
import View.Utils.FabricaEtiquetas;
import View.Utils.FabricaIconos;
import View.Utils.FabricaTablas;
import View.Utils.RenderizadorEstado;
import View.Utils.UIConstants;
import View.Utils.Validaciones;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Gestión de Productos del Administrador — reescrita a mano (deja
 * de extender PanelCrudBase) para calzar con el mockup: 4 tarjetas
 * KPI, búsqueda + filtro por categoría, tabla con badge de Estado
 * y paginación "Mostrando X a Y de Z".
 *
 * El formulario de alta/edición conserva toda la lógica que ya
 * existía (categoría obligatoria, promoción opcional, validación
 * de precio/stock) — solo cambió el contenedor visual alrededor.
 * ===============================================================
 */
public class PanelProductos extends PanelFondo {

    private static final int COLUMNA_ESTADO = 5;

    // Mismo criterio que PanelCategorias: antes 7 filas por página
    // hacían que el filtro "Todas las categorías" pareciera no
    // mostrar el catálogo completo. Se sube el límite y se deja que
    // el scroll del dashboard (ya arreglado) maneje listas largas.
    private static final int FILAS_POR_PAGINA = 500;
    private static final String TODAS_LAS_CATEGORIAS = "Todas las categorías";

    private final IProductoService productoService = new ProductoServiceImpl();
    private final ICategoriaService categoriaService = new CategoriaServiceImpl();
    private final IPromocionService promocionService = new PromocionServiceImpl();

    private List<Producto> todosLosProductos = new ArrayList<>();
    private List<Producto> productosFiltrados = new ArrayList<>();
    private List<Producto> productosPagina = new ArrayList<>();

    private TarjetaKPI tarjetaTotal;
    private TarjetaKPI tarjetaDisponibles;
    private TarjetaKPI tarjetaSinStock;
    private TarjetaKPI tarjetaConPromocion;

    private BarraBusqueda barraBusqueda;
    private JComboBox<String> comboCategoriaFiltro;

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    private JLabel lblResumenPaginacion;
    private JPanel panelBotonesPagina;
    private int paginaActual = 1;

    public PanelProductos() {

        super();

        setOpaque(false);
        setLayout(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));

        JPanel norte = new JPanel(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));
        norte.setOpaque(false);
        norte.add(FabricaEtiquetas.crearTitulo("PRODUCTOS"), BorderLayout.NORTH);
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

        JPanel fila = new JPanel(new GridLayout(1, 4, UIConstants.ESPACIO_SUBTITULO, 0));
        fila.setOpaque(false);
        fila.setBorder(BorderFactory.createEmptyBorder(UIConstants.ESPACIO_SUBTITULO, 0, 0, 0));

        tarjetaTotal = new TarjetaKPI(FabricaIconos.productos(), "Total Productos", "0", "");
        tarjetaDisponibles = new TarjetaKPI(FabricaIconos.productos(), "Disponibles", "0", "");
        tarjetaSinStock = new TarjetaKPI(FabricaIconos.productos(), "Sin Stock", "0", "");
        tarjetaConPromocion = new TarjetaKPI(FabricaIconos.promociones(), "Con Promoción Activa", "0", "");

        fila.add(tarjetaTotal);
        fila.add(tarjetaDisponibles);
        fila.add(tarjetaSinStock);
        fila.add(tarjetaConPromocion);

        return fila;
    }

    // ==========================================================
    // BÚSQUEDA + FILTRO + BOTONES
    // ==========================================================

    private JPanel crearBarraAcciones() {

        JPanel barra = new JPanel(new BorderLayout());
        barra.setOpaque(false);

        JPanel ladoIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, UIConstants.ESPACIO_SUBTITULO, 0));
        ladoIzquierdo.setOpaque(false);

        barraBusqueda = new BarraBusqueda("Buscar producto...");
        barraBusqueda.getCampo().getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {
                    @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
                    @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
                    @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
                });

        comboCategoriaFiltro = FabricaCampos.crearCombo();
        comboCategoriaFiltro.addItem(TODAS_LAS_CATEGORIAS);
        for (Categoria categoria : categoriaService.listar()) {
            comboCategoriaFiltro.addItem(categoria.getNombre());
        }
        comboCategoriaFiltro.addActionListener(e -> aplicarFiltros());

        ladoIzquierdo.add(barraBusqueda);
        ladoIzquierdo.add(comboCategoriaFiltro);

        JPanel ladoDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, UIConstants.ESPACIO_SUBTITULO, 0));
        ladoDerecho.setOpaque(false);

        JButton btnEditar = FabricaBotones.crearSecundario("Editar");
        JButton btnEliminar = FabricaBotones.crearSecundario("Eliminar");
        JButton btnNuevo = FabricaBotones.crearPrimario("Nuevo Producto");

        btnEditar.addActionListener(e -> editarSeleccionado());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        btnNuevo.addActionListener(e -> abrirFormularioNuevo());

        ladoDerecho.add(btnEditar);
        ladoDerecho.add(btnEliminar);
        ladoDerecho.add(btnNuevo);

        barra.add(ladoIzquierdo, BorderLayout.WEST);
        barra.add(ladoDerecho, BorderLayout.EAST);

        return barra;
    }

    // ==========================================================
    // TABLA
    // ==========================================================

    private JPanel crearPanelTabla() {

        Object[] columnas = {"ID", "Producto", "Categoría", "Precio", "Stock", "Estado"};

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
                "Nota: solo se listan productos con categoría asignada "
                        + "(la consulta actual excluye productos sin categoría; ver PanelCategorias)."
        ), BorderLayout.SOUTH);

        return pie;
    }

    private void repintarPaginacion() {

        int totalFilas = productosFiltrados.size();
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

        todosLosProductos = productoService.listarProductos();

        int disponibles = 0;
        int sinStock = 0;
        int conPromocion = 0;

        for (Producto producto : todosLosProductos) {

            if (producto.isDisponible()) disponibles++;
            if (producto.getStock() <= 0) sinStock++;
            if (producto.getPromocion() != null && producto.getPromocion().isEstado()) conPromocion++;
        }

        tarjetaTotal.actualizar(String.valueOf(todosLosProductos.size()), "");
        tarjetaDisponibles.actualizar(String.valueOf(disponibles), "");
        tarjetaSinStock.actualizar(String.valueOf(sinStock), "");
        tarjetaConPromocion.actualizar(String.valueOf(conPromocion), "");

        paginaActual = 1;
        aplicarFiltros();
    }

    private void aplicarFiltros() {

        String texto = barraBusqueda == null ? "" : barraBusqueda.getTexto().toLowerCase();

        String categoriaSeleccionada =
                (comboCategoriaFiltro == null || comboCategoriaFiltro.getSelectedIndex() <= 0)
                        ? null
                        : (String) comboCategoriaFiltro.getSelectedItem();

        productosFiltrados = new ArrayList<>();

        for (Producto producto : todosLosProductos) {

            boolean pasaBusqueda = texto.isEmpty()
                    || producto.getNombre().toLowerCase().contains(texto);

            boolean pasaCategoria = categoriaSeleccionada == null
                    || (producto.getCategoria() != null
                        && categoriaSeleccionada.equals(producto.getCategoria().getNombre()));

            if (pasaBusqueda && pasaCategoria) {
                productosFiltrados.add(producto);
            }
        }

        paginaActual = 1;
        repintarTablaConPaginaActual();
    }

    private void repintarTablaConPaginaActual() {

        int desde = (paginaActual - 1) * FILAS_POR_PAGINA;
        int hasta = Math.min(desde + FILAS_POR_PAGINA, productosFiltrados.size());

        productosPagina = desde >= hasta
                ? new ArrayList<>()
                : new ArrayList<>(productosFiltrados.subList(desde, hasta));

        modeloTabla.setRowCount(0);

        for (Producto producto : productosPagina) {
            modeloTabla.addRow(new Object[]{
                    producto.getIdProducto(),
                    producto.getNombre(),
                    producto.getCategoria() != null ? producto.getCategoria().getNombre() : "-",
                    "Q" + producto.getPrecio(),
                    producto.getStock(),
                    producto.getStock() <= 0 ? "Sin stock" : (producto.isDisponible() ? "Disponible" : "Inactivo")
            });
        }

        repintarPaginacion();
    }

    // ==========================================================
    // ACCIONES SOBRE LA FILA SELECCIONADA
    // ==========================================================

    private Producto obtenerSeleccionado() {

        int fila = tabla.getSelectedRow();

        if (fila < 0 || fila >= productosPagina.size()) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto de la tabla.",
                    "Ningún producto seleccionado", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return productosPagina.get(fila);
    }

    private void editarSeleccionado() {
        Producto producto = obtenerSeleccionado();
        if (producto != null) {
            abrirFormularioEdicion(producto);
        }
    }

    /**
     * "Eliminar" intenta el borrado físico; si la BD lo rechaza
     * porque el producto ya fue vendido (FK detalle_pedido), se le
     * ofrece al administrador desactivarlo en su lugar, en vez de
     * solo mostrar un error sin salida.
     */
    private void eliminarSeleccionado() {

        Producto producto = obtenerSeleccionado();

        if (producto == null) {
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar \"" + producto.getNombre() + "\"?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            if (!productoService.eliminarProducto(producto.getIdProducto())) {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el producto.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

        } catch (IllegalStateException ex) {

            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    ex.getMessage() + "\n\n¿Deseas desactivarlo en su lugar?",
                    "No se puede eliminar",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (respuesta == JOptionPane.YES_OPTION) {
                productoService.desactivarProducto(producto.getIdProducto());
            } else {
                return;
            }
        }

        cargarDatos();
    }

    // ==========================================================
    // FORMULARIO DE ALTA / EDICIÓN
    // ==========================================================

    private void abrirFormularioNuevo() {

        List<Categoria> categoriasDisponibles = categoriaService.listarActivas();

        if (categoriasDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debes crear al menos una categoría antes de agregar un producto.",
                    "Sin categorías", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Promocion> promocionesDisponibles = promocionService.listarPromocionesActivas();

        JTextField txtNombre = new JTextField();
        JTextField txtDescripcion = new JTextField();
        JTextField txtPrecio = new JTextField("0.00");
        JTextField txtStock = new JTextField("0");
        JTextField txtImagen = new JTextField();
        JComboBox<String> comboCategoria = crearComboCategorias(categoriasDisponibles);
        JComboBox<String> comboPromocion = crearComboPromociones(promocionesDisponibles);
        JCheckBox chkDestacado = new JCheckBox("Producto destacado");

        Object[] campos = {
                "Nombre:", txtNombre,
                "Descripción:", txtDescripcion,
                "Categoría:", comboCategoria,
                "Precio:", txtPrecio,
                "Stock:", txtStock,
                "Imagen (nombre de archivo sin extensión):", txtImagen,
                "Promoción:", comboPromocion,
                chkDestacado
        };

        int confirmacion = JOptionPane.showConfirmDialog(this, campos, "Nuevo producto",
                JOptionPane.OK_CANCEL_OPTION);

        if (confirmacion != JOptionPane.OK_OPTION) {
            return;
        }

        if (Validaciones.estaVacio(txtNombre.getText())) {
            JOptionPane.showMessageDialog(this, "El nombre del producto es obligatorio.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal precio = parsearPrecio(txtPrecio.getText());
        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido mayor a 0.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer stock = parsearEntero(txtStock.getText());
        if (stock == null || stock < 0) {
            JOptionPane.showMessageDialog(this, "El stock debe ser un número entero válido mayor o igual a 0.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Categoria categoriaSeleccionada = categoriasDisponibles.get(comboCategoria.getSelectedIndex());
        Promocion promocionSeleccionada = obtenerPromocionSeleccionada(comboPromocion, promocionesDisponibles);

        Producto producto = new Producto(
                0, categoriaSeleccionada, txtNombre.getText().trim(), txtDescripcion.getText().trim(),
                precio, null, stock, stock > 0, 0, 0, chkDestacado.isSelected(),
                txtImagen.getText().trim().isEmpty() ? null : txtImagen.getText().trim(),
                null, true, null, promocionSeleccionada
        );

        String motivoInvalido = productoService.validar(producto);
        if (motivoInvalido != null) {
            JOptionPane.showMessageDialog(this, motivoInvalido,
                    "No se pudo guardar el producto", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!productoService.registrarProducto(producto)) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar el producto. Verifica tu conexión e inténtalo de nuevo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        cargarDatos();
    }

    private void abrirFormularioEdicion(Producto producto) {

        List<Categoria> categoriasDisponibles = categoriaService.listarActivas();

        if (producto.getCategoria() != null && !categoriasDisponibles.contains(producto.getCategoria())) {
            categoriasDisponibles.add(producto.getCategoria());
        }

        List<Promocion> promocionesDisponibles = promocionService.listarPromocionesActivas();

        if (producto.getPromocion() != null && !promocionesDisponibles.contains(producto.getPromocion())) {
            promocionesDisponibles.add(producto.getPromocion());
        }

        JTextField txtNombre = new JTextField(producto.getNombre());
        JTextField txtDescripcion = new JTextField(producto.getDescripcion());
        JTextField txtPrecio = new JTextField(producto.getPrecio() != null ? producto.getPrecio().toString() : "0.00");
        JTextField txtStock = new JTextField(String.valueOf(producto.getStock()));
        JTextField txtImagen = new JTextField(producto.getImagenPrincipal() != null ? producto.getImagenPrincipal() : "");
        JComboBox<String> comboCategoria = crearComboCategorias(categoriasDisponibles);
        JComboBox<String> comboPromocion = crearComboPromociones(promocionesDisponibles);
        JCheckBox chkDestacado = new JCheckBox("Producto destacado", producto.isDestacado());
        JCheckBox chkActivo = new JCheckBox("Producto activo", producto.isEstado());

        preseleccionarCategoria(comboCategoria, categoriasDisponibles, producto.getCategoria());
        preseleccionarPromocion(comboPromocion, promocionesDisponibles, producto.getPromocion());

        Object[] campos = {
                "Nombre:", txtNombre,
                "Descripción:", txtDescripcion,
                "Categoría:", comboCategoria,
                "Precio:", txtPrecio,
                "Stock:", txtStock,
                "Imagen (nombre de archivo sin extensión):", txtImagen,
                "Promoción:", comboPromocion,
                chkDestacado,
                chkActivo
        };

        int confirmacion = JOptionPane.showConfirmDialog(this, campos, "Editar producto",
                JOptionPane.OK_CANCEL_OPTION);

        if (confirmacion != JOptionPane.OK_OPTION) {
            return;
        }

        if (Validaciones.estaVacio(txtNombre.getText())) {
            JOptionPane.showMessageDialog(this, "El nombre del producto es obligatorio.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal precio = parsearPrecio(txtPrecio.getText());
        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido mayor a 0.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer stock = parsearEntero(txtStock.getText());
        if (stock == null || stock < 0) {
            JOptionPane.showMessageDialog(this, "El stock debe ser un número entero válido mayor o igual a 0.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        producto.setCategoria(categoriasDisponibles.get(comboCategoria.getSelectedIndex()));
        producto.setNombre(txtNombre.getText().trim());
        producto.setDescripcion(txtDescripcion.getText().trim());
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setImagenPrincipal(txtImagen.getText().trim().isEmpty() ? null : txtImagen.getText().trim());
        producto.setDestacado(chkDestacado.isSelected());
        producto.setEstado(chkActivo.isSelected());
        producto.setPromocion(obtenerPromocionSeleccionada(comboPromocion, promocionesDisponibles));

        String motivoInvalido = productoService.validar(producto);
        if (motivoInvalido != null) {
            JOptionPane.showMessageDialog(this, motivoInvalido,
                    "No se pudo actualizar el producto", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!productoService.actualizarProducto(producto)) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar el producto. Verifica tu conexión e inténtalo de nuevo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        cargarDatos();
    }

    // ==========================================================
    // UTILITARIOS DEL FORMULARIO
    // ==========================================================

    private JComboBox<String> crearComboCategorias(List<Categoria> categorias) {
        List<String> nombres = new ArrayList<>();
        for (Categoria categoria : categorias) nombres.add(categoria.getNombre());
        return new JComboBox<>(nombres.toArray(new String[0]));
    }

    private JComboBox<String> crearComboPromociones(List<Promocion> promociones) {
        List<String> nombres = new ArrayList<>();
        nombres.add("Ninguna");
        for (Promocion promocion : promociones) nombres.add(promocion.getNombre());
        return new JComboBox<>(nombres.toArray(new String[0]));
    }

    private void preseleccionarCategoria(JComboBox<String> combo, List<Categoria> categorias, Categoria actual) {
        if (actual == null) return;
        for (int i = 0; i < categorias.size(); i++) {
            if (categorias.get(i).getIdCategoria() == actual.getIdCategoria()) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void preseleccionarPromocion(JComboBox<String> combo, List<Promocion> promociones, Promocion actual) {
        if (actual == null) {
            combo.setSelectedIndex(0);
            return;
        }
        for (int i = 0; i < promociones.size(); i++) {
            if (promociones.get(i).getIdPromocion() == actual.getIdPromocion()) {
                combo.setSelectedIndex(i + 1);
                return;
            }
        }
    }

    private Promocion obtenerPromocionSeleccionada(JComboBox<String> combo, List<Promocion> promociones) {
        int indice = combo.getSelectedIndex();
        if (indice <= 0) return null;
        return promociones.get(indice - 1);
    }

    private BigDecimal parsearPrecio(String texto) {
        try {
            return new BigDecimal(texto.trim().replace(",", "."));
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }

    private Integer parsearEntero(String texto) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }

}
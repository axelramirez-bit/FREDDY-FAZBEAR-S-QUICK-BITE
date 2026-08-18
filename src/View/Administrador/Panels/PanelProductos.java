package View.Administrador.Panels;

import Base.PanelCrudBase;
import Model.Categoria;
import Model.Producto;
import Model.Promocion;
import Service.Implement.CategoriaServiceImpl;
import Service.Implement.ProductoServiceImpl;
import Service.Implement.PromocionServiceImpl;
import Service.Interfaz.ICategoriaService;
import Service.Interfaz.IProductoService;
import Service.Interfaz.IPromocionService;
import View.Utils.Validaciones;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD de Productos del Administrador, implementado sobre
 * PanelCrudBase (mismo patrón que PanelCategorias). Antes esta
 * clase era un PanelTemporal vacío ("PRODUCTOS" a secas), por eso
 * el panel de productos según categoría nunca cumplía su función:
 * no había forma de crear, editar o desactivar productos ni de
 * asignarles una categoría desde Administrador — solo existían los
 * datos que trae el script SQL inicial.
 *
 * El formulario de alta/edición usa JOptionPane con campos, igual
 * que PanelCategorias, para mantener el mismo estilo simple; el
 * equipo puede reemplazarlo por un diálogo propio sin tocar el
 * resto de la clase.
 */
public class PanelProductos extends PanelCrudBase<Producto> {

    private final IProductoService productoService = new ProductoServiceImpl();

    private final ICategoriaService categoriaService = new CategoriaServiceImpl();

    private final IPromocionService promocionService = new PromocionServiceImpl();

    public PanelProductos() {

        super();

        cargarDatos();
    }

    // ==========================================================
    // MÉTODOS REQUERIDOS POR PanelCrudBase
    // ==========================================================

    @Override
    protected Object[] getColumnas() {

        return new Object[]{ "ID", "Nombre", "Categoría", "Precio", "Stock", "Disponible", "Activo" };
    }

    @Override
    protected List<Producto> listarTodos() {

        return productoService.listarProductos();
    }

    @Override
    protected Object[] convertirFila(Producto producto) {

        return new Object[]{
                producto.getIdProducto(),
                producto.getNombre(),
                producto.getCategoria() != null ? producto.getCategoria().getNombre() : "-",
                "Q" + producto.getPrecio(),
                producto.getStock(),
                producto.isDisponible() ? "Sí" : "No",
                producto.isEstado() ? "Sí" : "No"
        };
    }

    @Override
    protected void alAgregar() {

        List<Categoria> categoriasDisponibles = categoriaService.listarActivas();

        if (categoriasDisponibles.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debes crear al menos una categoría antes de agregar un producto.",
                    "Sin categorías",
                    JOptionPane.WARNING_MESSAGE
            );

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

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                campos,
                "Nuevo producto",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (confirmacion != JOptionPane.OK_OPTION) {
            return;
        }

        if (Validaciones.estaVacio(txtNombre.getText())) {

            JOptionPane.showMessageDialog(
                    this,
                    "El nombre del producto es obligatorio.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        BigDecimal precio = parsearPrecio(txtPrecio.getText());

        if (precio == null || precio.compareTo(BigDecimal.ZERO) < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "El precio debe ser un número válido mayor o igual a 0.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Integer stock = parsearEntero(txtStock.getText());

        if (stock == null || stock < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "El stock debe ser un número entero válido mayor o igual a 0.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Categoria categoriaSeleccionada = categoriasDisponibles.get(comboCategoria.getSelectedIndex());

        Promocion promocionSeleccionada = obtenerPromocionSeleccionada(comboPromocion, promocionesDisponibles);

        Producto producto = new Producto(
                0,
                categoriaSeleccionada,
                txtNombre.getText().trim(),
                txtDescripcion.getText().trim(),
                precio,
                null,
                stock,
                stock > 0,
                0,
                0,
                chkDestacado.isSelected(),
                txtImagen.getText().trim().isEmpty() ? null : txtImagen.getText().trim(),
                null,
                true,
                null,
                promocionSeleccionada
        );

        boolean guardado = productoService.registrarProducto(producto);

        if (!guardado) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo guardar el producto.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    @Override
    protected void alEditar(Producto producto) {

        List<Categoria> categoriasDisponibles = categoriaService.listarActivas();

        // Si la categoría actual del producto ya no está activa
        // (fue desactivada desde PanelCategorias), la agregamos igual
        // a la lista para no perder el dato ni forzar un cambio no
        // pedido por el administrador.
        if (producto.getCategoria() != null
                && !categoriasDisponibles.contains(producto.getCategoria())) {
            categoriasDisponibles.add(producto.getCategoria());
        }

        List<Promocion> promocionesDisponibles = promocionService.listarPromocionesActivas();

        if (producto.getPromocion() != null
                && !promocionesDisponibles.contains(producto.getPromocion())) {
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

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                campos,
                "Editar producto",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (confirmacion != JOptionPane.OK_OPTION) {
            return;
        }

        if (Validaciones.estaVacio(txtNombre.getText())) {

            JOptionPane.showMessageDialog(
                    this,
                    "El nombre del producto es obligatorio.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        BigDecimal precio = parsearPrecio(txtPrecio.getText());

        if (precio == null || precio.compareTo(BigDecimal.ZERO) < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "El precio debe ser un número válido mayor o igual a 0.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Integer stock = parsearEntero(txtStock.getText());

        if (stock == null || stock < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "El stock debe ser un número entero válido mayor o igual a 0.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

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

        boolean actualizado = productoService.actualizarProducto(producto);

        if (!actualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar el producto.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    @Override
    protected boolean alEliminar(Producto producto) {

        // No hay borrado físico: un producto ya puede estar referenciado
        // en carritos o pedidos anteriores (mismo criterio que
        // PanelCategorias con las categorías). Se desactiva en vez de
        // eliminarlo de la tabla, así deja de aparecer en los paneles
        // del Cliente (listarProductosDisponibles() filtra por estado)
        // sin romper el historial de pedidos.
        producto.setEstado(false);

        return productoService.actualizarProducto(producto);
    }

    // ==========================================================
    // UTILITARIOS PROPIOS DE ESTA PANTALLA
    // ==========================================================

    private JComboBox<String> crearComboCategorias(List<Categoria> categorias) {

        List<String> nombres = new ArrayList<>();

        for (Categoria categoria : categorias) {
            nombres.add(categoria.getNombre());
        }

        return new JComboBox<>(nombres.toArray(new String[0]));
    }

    private JComboBox<String> crearComboPromociones(List<Promocion> promociones) {

        List<String> nombres = new ArrayList<>();

        nombres.add("Ninguna");

        for (Promocion promocion : promociones) {
            nombres.add(promocion.getNombre());
        }

        return new JComboBox<>(nombres.toArray(new String[0]));
    }

    private void preseleccionarCategoria(JComboBox<String> combo, List<Categoria> categorias, Categoria actual) {

        if (actual == null) {
            return;
        }

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
                // +1 porque el índice 0 del combo es "Ninguna"
                combo.setSelectedIndex(i + 1);
                return;
            }
        }
    }

    private Promocion obtenerPromocionSeleccionada(JComboBox<String> combo, List<Promocion> promociones) {

        int indice = combo.getSelectedIndex();

        // Índice 0 == "Ninguna"
        if (indice <= 0) {
            return null;
        }

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
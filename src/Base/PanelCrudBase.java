
package Base;

import View.Componentes.PanelFondo;
import View.Utils.FabricaBotones;
import View.Utils.FabricaTablas;
import View.Utils.UIConstants;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
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
 * Promociones). Es UNA sola clase que cada pantalla extiende,
 * implementando solo los métodos abstractos: qué columnas
 * mostrar, cómo listar, cómo convertir una entidad a fila, y qué
 * hacer al agregar/editar/eliminar.
 *
 * No depende de un IService en común porque IProductoService,
 * ICategoriaService e IPromocionService no comparten nombres de
 * método (registrarProducto / guardar / registrarPromocion) —
 * por eso cada subclase conecta su propio Service dentro de estos
 * métodos, en vez de que PanelCrudBase reciba un Service genérico.
 *
 * Ejemplo de uso para Categorías:
 *
 *     public class PanelCategoriasAdmin extends PanelCrudBase<Categoria> {
 *
 *         private final ICategoriaService categoriaService = new CategoriaServiceImpl();
 *
 *         public PanelCategoriasAdmin() {
 *             super();
 *         }
 *
 *         protected Object[] getColumnas() {
 *             return new Object[]{ "ID", "Nombre", "Orden", "Activa" };
 *         }
 *
 *         protected List<Categoria> listarTodos() {
 *             return categoriaService.listar();
 *         }
 *
 *         protected Object[] convertirFila(Categoria categoria) {
 *             return new Object[]{
 *                     categoria.getIdCategoria(),
 *                     categoria.getNombre(),
 *                     categoria.getOrden(),
 *                     categoria.isEstado() ? "Sí" : "No"
 *             };
 *         }
 *
 *         protected void alAgregar() {
 *             // abrir el formulario propio de Categoría y, si el
 *             // usuario confirma, llamar a categoriaService.guardar(...)
 *             // y luego cargarDatos();
 *         }
 *
 *         protected void alEditar(Categoria categoria) {
 *             // abrir el mismo formulario precargado con los datos
 *             // de "categoria" y llamar a categoriaService.actualizar(...)
 *         }
 *
 *         protected boolean alEliminar(Categoria categoria) {
 *             return categoriaService.cambiarEstado(categoria.getIdCategoria(), false);
 *         }
 *     }
 *
 * Productos y Promociones se implementan igual, cada uno con su
 * propio Service y su propio formulario (los formularios de alta
 * NO son responsabilidad de esta clase, porque los campos de
 * Categoría, Producto y Promoción son todos distintos).
 * ===============================================================
 */
public abstract class PanelCrudBase<T> extends PanelFondo {

    private JTable tabla;

    private DefaultTableModel modeloTabla;

    private List<T> entidadesActuales = new ArrayList<>();

    protected PanelCrudBase() {

        super();

        setOpaque(false);
        setLayout(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));

        add(crearBarraAcciones(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
    }

    // ==========================================================
    // MÉTODOS QUE CADA CRUD CONCRETO DEBE IMPLEMENTAR
    // ==========================================================

    protected abstract Object[] getColumnas();

    protected abstract List<T> listarTodos();

    protected abstract Object[] convertirFila(T entidad);

    protected abstract void alAgregar();

    protected abstract void alEditar(T entidad);

    protected abstract boolean alEliminar(T entidad);

    // ==========================================================
    // ESTRUCTURA
    // ==========================================================

    private JPanel crearBarraAcciones() {

        JPanel barra = new JPanel(new FlowLayout(
                FlowLayout.LEFT,
                UIConstants.ESPACIO_SUBTITULO,
                0
        ));

        barra.setOpaque(false);

        JButton btnAgregar = FabricaBotones.crearPrimario("Agregar");
        JButton btnEditar = FabricaBotones.crearSecundario("Editar");
        JButton btnEliminar = FabricaBotones.crearSecundario("Eliminar");

        btnAgregar.addActionListener(e -> {
            alAgregar();
            cargarDatos();
        });

        btnEditar.addActionListener(e -> editarSeleccionado());

        btnEliminar.addActionListener(e -> eliminarSeleccionado());

        barra.add(btnAgregar);
        barra.add(btnEditar);
        barra.add(btnEliminar);

        return barra;
    }

    private JPanel crearPanelTabla() {

        this.modeloTabla = FabricaTablas.crearModeloSoloLectura(getColumnas());

        this.tabla = FabricaTablas.crearTabla(modeloTabla);

        return FabricaTablas.crearPanelTabla(tabla);
    }

    // ==========================================================
    // CARGA DE DATOS
    // ==========================================================

    /**
     * Vuelve a traer todas las entidades y repinta la tabla.
     * Público para que el formulario de alta/edición pueda
     * llamarlo al cerrar y refrescar la lista.
     */
    public void cargarDatos() {

        this.entidadesActuales = listarTodos();

        modeloTabla.setRowCount(0);

        for (T entidad : entidadesActuales) {
            modeloTabla.addRow(convertirFila(entidad));
        }
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

        if (fila < 0 || fila >= entidadesActuales.size()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un elemento de la tabla.",
                    "Ningún elemento seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        return entidadesActuales.get(fila);
    }

}

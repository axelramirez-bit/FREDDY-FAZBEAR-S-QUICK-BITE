package View.Administrador.Panels;

import Base.PanelCrudBase;
import Model.Categoria;
import Service.Implement.CategoriaServiceImpl;
import Service.Interfaz.ICategoriaService;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.util.List;

/**
 * CRUD de Categorías del Administrador, implementado sobre
 * PanelCrudBase. El formulario de alta/edición aquí es deliberadamente
 * simple (JOptionPane con campos de texto) para que el ejemplo
 * compile y funcione; el equipo puede reemplazarlo por un diálogo
 * propio con FabricaEtiquetas/FabricaBotones sin tocar el resto de
 * la clase.
 */
public class PanelCategorias extends PanelCrudBase<Categoria> {

    private final ICategoriaService categoriaService = new CategoriaServiceImpl();

    public PanelCategorias() {

        super();

        cargarDatos();
    }

    // ==========================================================
    // MÉTODOS REQUERIDOS POR PanelCrudBase
    // ==========================================================

    @Override
    protected Object[] getColumnas() {

        return new Object[]{ "ID", "Nombre", "Orden", "Activa" };
    }

    @Override
    protected List<Categoria> listarTodos() {

        return categoriaService.listar();
    }

    @Override
    protected Object[] convertirFila(Categoria categoria) {

        return new Object[]{
                categoria.getIdCategoria(),
                categoria.getNombre(),
                categoria.getOrden(),
                categoria.isEstado() ? "Sí" : "No"
        };
    }

    @Override
    protected void alAgregar() {

        JTextField txtNombre = new JTextField();
        JTextField txtDescripcion = new JTextField();
        JTextField txtOrden = new JTextField("0");

        Object[] campos = {
                "Nombre:", txtNombre,
                "Descripción:", txtDescripcion,
                "Orden:", txtOrden
        };

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                campos,
                "Nueva categoría",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (confirmacion != JOptionPane.OK_OPTION) {
            return;
        }

        if (txtNombre.getText().isBlank()) {

            JOptionPane.showMessageDialog(
                    this,
                    "El nombre de la categoría es obligatorio.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Categoria categoria = new Categoria(
                0,
                txtNombre.getText(),
                txtDescripcion.getText(),
                null,
                null,
                null,
                parsearOrden(txtOrden.getText()),
                true
        );

        boolean guardada = categoriaService.guardar(categoria);

        if (!guardada) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo guardar la categoría.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    @Override
    protected void alEditar(Categoria categoria) {

        JTextField txtNombre = new JTextField(categoria.getNombre());
        JTextField txtDescripcion = new JTextField(categoria.getDescripcion());
        JTextField txtOrden = new JTextField(String.valueOf(categoria.getOrden()));

        Object[] campos = {
                "Nombre:", txtNombre,
                "Descripción:", txtDescripcion,
                "Orden:", txtOrden
        };

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                campos,
                "Editar categoría",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (confirmacion != JOptionPane.OK_OPTION) {
            return;
        }

        categoria.setNombre(txtNombre.getText());
        categoria.setDescripcion(txtDescripcion.getText());
        categoria.setOrden(parsearOrden(txtOrden.getText()));

        boolean actualizada = categoriaService.actualizar(categoria);

        if (!actualizada) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar la categoría.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    @Override
    protected boolean alEliminar(Categoria categoria) {

        // No hay borrado físico: se desactiva para no romper los
        // productos que ya la tienen asignada.
        return categoriaService.cambiarEstado(categoria.getIdCategoria(), false);
    }

    // ==========================================================
    // UTILITARIO PROPIO DE ESTA PANTALLA
    // ==========================================================

    private int parsearOrden(String texto) {

        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
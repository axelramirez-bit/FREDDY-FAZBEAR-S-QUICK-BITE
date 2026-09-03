package Service.Interfaz;

import Model.Producto;

import java.util.List;

public interface IProductoService {

    boolean registrarProducto(Producto producto);

    boolean actualizarProducto(Producto producto);

    /** Motivo exacto por el que un producto no se podría guardar (null si está bien). */
    String validar(Producto producto);

    boolean eliminarProducto(int idProducto);

    // Caso de uso "Desactivar producto": siempre posible.
    boolean desactivarProducto(int idProducto);

    boolean activarProducto(int idProducto);

    Producto obtenerProductoPorId(int idProducto);

    List<Producto> listarProductos();

    List<Producto> listarProductosDisponibles();

    boolean actualizarStock(int idProducto, int cantidad);

    /**
     * Asocia una categoría ADICIONAL a un producto (además de su
     * categoría principal), para que aparezca en más de un panel del
     * Cliente. Ej: asignarCategoriaAdicional(idComboDesayuno, idDesayunos)
     * si su categoría principal ya es "Combos".
     */
    boolean asignarCategoriaAdicional(int idProducto, int idCategoria);

    /** Quita una categoría adicional previamente asignada a un producto. */
    boolean quitarCategoriaAdicional(int idProducto, int idCategoria);

}
// Paquete Service.Interfaz
package Service.Interfaz;

// Importa el modelo Producto
import Model.Producto;

// Importa List
import java.util.List;

// Contrato del servicio de productos
public interface IProductoService {

    // Registra un producto
    boolean registrarProducto(Producto producto);

    // Actualiza un producto
    boolean actualizarProducto(Producto producto);

    /** Motivo exacto por el que un producto no se podría guardar (null si está bien). */
    // Valida y devuelve el motivo del error (null si está bien)
    String validar(Producto producto);

    // Elimina un producto por id
    boolean eliminarProducto(int idProducto);

    // Caso de uso "Desactivar producto": siempre posible.
    // Desactiva un producto
    boolean desactivarProducto(int idProducto);

    // Activa un producto
    boolean activarProducto(int idProducto);

    // Obtiene un producto por id
    Producto obtenerProductoPorId(int idProducto);

    // Lista todos los productos
    List<Producto> listarProductos();

    // Lista los productos disponibles
    List<Producto> listarProductosDisponibles();

    // Actualiza el stock de un producto
    boolean actualizarStock(int idProducto, int cantidad);

    /**
     * Asocia una categoría ADICIONAL a un producto (además de su
     * categoría principal), para que aparezca en más de un panel del
     * Cliente. Ej: asignarCategoriaAdicional(idComboDesayuno, idDesayunos)
     * si su categoría principal ya es "Combos".
     */
    // Asocia una categoría adicional a un producto
    boolean asignarCategoriaAdicional(int idProducto, int idCategoria);

    /** Quita una categoría adicional previamente asignada a un producto. */
    // Quita una categoría adicional de un producto
    boolean quitarCategoriaAdicional(int idProducto, int idCategoria);

}
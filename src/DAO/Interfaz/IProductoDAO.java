// Paquete DAO.Interfaz
package DAO.Interfaz;

// Importa el modelo Producto
import Model.Producto;
// Importa List
import java.util.List;

// Contrato de acceso a datos de productos
public interface IProductoDAO {

    // Inserta un producto
    boolean insertar(Producto producto);

    // Actualiza un producto
    boolean actualizar(Producto producto);

    // Elimina un producto por id
    boolean eliminar(int id);

    // Caso de uso "Desactivar producto": alterna estado sin
    // chocar con la FK detalle_pedido.id_producto (a diferencia
    // de eliminar(), que la BD bloquea si el producto ya fue
    // vendido alguna vez).
    // Activa o desactiva un producto
    boolean cambiarEstado(int idProducto, boolean estado);

    // Busca un producto por id
    Producto buscarPorId(int id);

    // Lista todos los productos
    List<Producto> listar();

}
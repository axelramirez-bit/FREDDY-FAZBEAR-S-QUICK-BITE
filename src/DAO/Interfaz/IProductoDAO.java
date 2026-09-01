package DAO.Interfaz;

import Model.Producto;
import java.util.List;

public interface IProductoDAO {

    boolean insertar(Producto producto);

    boolean actualizar(Producto producto);

    boolean eliminar(int id);

    // Caso de uso "Desactivar producto": alterna estado sin
    // chocar con la FK detalle_pedido.id_producto (a diferencia
    // de eliminar(), que la BD bloquea si el producto ya fue
    // vendido alguna vez).
    boolean cambiarEstado(int idProducto, boolean estado);

    Producto buscarPorId(int id);

    List<Producto> listar();

}
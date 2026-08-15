package Service.Interfaz;

import Model.Producto;

import java.util.List;

public interface IProductoService {

    boolean registrarProducto(Producto producto);

    boolean actualizarProducto(Producto producto);

    boolean eliminarProducto(int idProducto);

    Producto obtenerProductoPorId(int idProducto);

    List<Producto> listarProductos();

    List<Producto> listarProductosDisponibles();

    boolean actualizarStock(int idProducto, int cantidad);

}
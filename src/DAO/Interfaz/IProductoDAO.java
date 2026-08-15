package DAO.Interfaz;

import Model.Producto;
import java.util.List;

public interface IProductoDAO {

    boolean insertar(Producto producto);

    boolean actualizar(Producto producto);

    boolean eliminar(int id);

    Producto buscarPorId(int id);

    List<Producto> listar();

}

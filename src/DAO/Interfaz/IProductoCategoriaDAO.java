package DAO.Interfaz;

import Model.Categoria;

import java.util.List;
import java.util.Map;

public interface IProductoCategoriaDAO {

    /** Asocia una categoría adicional a un producto. */
    boolean asignarCategoria(int idProducto, int idCategoria);

    /** Quita una categoría adicional de un producto. */
    boolean quitarCategoria(int idProducto, int idCategoria);

    /** Categorías adicionales de un solo producto (sin incluir la principal). */
    List<Categoria> listarCategoriasPorProducto(int idProducto);

    /**
     * Trae, en una sola consulta, las categorías adicionales de TODOS
     * los productos, agrupadas por id_producto. Se usa desde
     * ProductoDAOImpl.listar() para no hacer una consulta extra por
     * cada producto (evitar el problema N+1).
     */
    Map<Integer, List<Categoria>> listarCategoriasPorTodosLosProductos();

}

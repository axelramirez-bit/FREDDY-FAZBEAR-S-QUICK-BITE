// Paquete DAO.Interfaz
package DAO.Interfaz;

// Importa el modelo Categoria
import Model.Categoria;

// Importa List
import java.util.List;
// Importa Map
import java.util.Map;

// Contrato de categorías adicionales de un producto
public interface IProductoCategoriaDAO {

    /** Asocia una categoría adicional a un producto. */
    // Asocia una categoría adicional a un producto
    boolean asignarCategoria(int idProducto, int idCategoria);

    /** Quita una categoría adicional de un producto. */
    // Quita una categoría adicional de un producto
    boolean quitarCategoria(int idProducto, int idCategoria);

    /** Categorías adicionales de un solo producto (sin incluir la principal). */
    // Lista las categorías adicionales de un producto
    List<Categoria> listarCategoriasPorProducto(int idProducto);

    /**
     * Trae, en una sola consulta, las categorías adicionales de TODOS
     * los productos, agrupadas por id_producto. Se usa desde
     * ProductoDAOImpl.listar() para no hacer una consulta extra por
     * cada producto (evitar el problema N+1).
     */
    // Categorías adicionales de todos los productos, agrupadas por id de producto
    Map<Integer, List<Categoria>> listarCategoriasPorTodosLosProductos();

}

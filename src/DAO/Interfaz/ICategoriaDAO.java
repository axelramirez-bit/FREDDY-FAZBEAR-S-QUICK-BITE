// Paquete DAO.Interfaz
package DAO.Interfaz;

// Importa el modelo Categoria
import Model.Categoria;
// Importa List
import java.util.List;

// Contrato de acceso a datos de categorías
public interface ICategoriaDAO {

    // Guarda una categoría nueva
    boolean guardar(Categoria categoria);

    // Actualiza una categoría
    boolean actualizar(Categoria categoria);

    // Activa o desactiva una categoría
    boolean cambiarEstado(int idCategoria, boolean estado);

    // Busca una categoría por su id
    Categoria buscarPorId(int idCategoria);

    // Lista todas las categorías
    List<Categoria> listar();

    // Lista solo las categorías activas
    List<Categoria> listarActivas();

}

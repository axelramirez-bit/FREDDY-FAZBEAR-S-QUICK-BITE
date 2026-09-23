// Paquete Service.Interfaz
package Service.Interfaz;

// Importa el modelo Categoria
import Model.Categoria;
// Importa List
import java.util.List;

// Contrato del servicio de categorías
public interface ICategoriaService {
    
    // Guarda una categoría
    boolean guardar(Categoria categoria);

    // Actualiza una categoría
    boolean actualizar(Categoria categoria);

    /** Motivo exacto por el que una categoría no se podría guardar (null si está bien). */
    // Valida y devuelve el motivo del error (null si está bien)
    String validar(Categoria categoria);

    // Activa o desactiva una categoría
    boolean cambiarEstado(int idCategoria, boolean estado);

    // Busca una categoría por id
    Categoria buscarPorId(int idCategoria);

    // Lista todas las categorías
    List<Categoria> listar();

    // Lista las categorías activas
    List<Categoria> listarActivas();
    
}
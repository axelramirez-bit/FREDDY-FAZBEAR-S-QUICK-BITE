// Paquete DAO.Interfaz
package DAO.Interfaz;

// Importa el modelo Promocion
import Model.Promocion;
// Importa List
import java.util.List;

// Contrato de acceso a datos de promociones
public interface IPromocionDAO {

    // Inserta una promoción
    boolean insertar(Promocion promocion);

    // Actualiza una promoción
    boolean actualizar(Promocion promocion);

    // Elimina una promoción por id
    boolean eliminar(int id);

    // Busca una promoción por id
    Promocion buscarPorId(int id);

    // Lista todas las promociones
    List<Promocion> listar();

}
// Paquete Service.Interfaz
package Service.Interfaz;

// Importa el modelo Promocion
import Model.Promocion;

// Importa List
import java.util.List;

// Contrato del servicio de promociones
public interface IPromocionService {

    // Registra una promoción
    boolean registrarPromocion(Promocion promocion);

    // Actualiza una promoción
    boolean actualizarPromocion(Promocion promocion);

    /** Motivo exacto por el que una promoción no se podría guardar (null si está bien). */
    // Valida y devuelve el motivo del error (null si está bien)
    String validar(Promocion promocion);

    // Elimina una promoción por id
    boolean eliminarPromocion(int idPromocion);

    // Obtiene una promoción por id
    Promocion obtenerPromocionPorId(int idPromocion);

    // Lista todas las promociones
    List<Promocion> listarPromociones();

    // Lista las promociones activas
    List<Promocion> listarPromocionesActivas();

}
package Service.Interfaz;

import Model.Promocion;

import java.util.List;

public interface IPromocionService {

    boolean registrarPromocion(Promocion promocion);

    boolean actualizarPromocion(Promocion promocion);

    /** Motivo exacto por el que una promoción no se podría guardar (null si está bien). */
    String validar(Promocion promocion);

    boolean eliminarPromocion(int idPromocion);

    Promocion obtenerPromocionPorId(int idPromocion);

    List<Promocion> listarPromociones();

    List<Promocion> listarPromocionesActivas();

}
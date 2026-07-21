package Service.Interfaz;

import Model.Promocion;

import java.util.List;

public interface IPromocionService {

    boolean registrarPromocion(Promocion promocion);

    boolean actualizarPromocion(Promocion promocion);

    boolean eliminarPromocion(int idPromocion);

    Promocion obtenerPromocionPorId(int idPromocion);

    List<Promocion> listarPromociones();

    List<Promocion> listarPromocionesActivas();

}
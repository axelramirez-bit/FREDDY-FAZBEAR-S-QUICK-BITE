package DAO.Interfaz;

import Model.Promocion;
import java.util.List;

public interface IPromocionDAO {

    boolean insertar(Promocion promocion);

    boolean actualizar(Promocion promocion);

    boolean eliminar(int id);

    Promocion buscarPorId(int id);

    List<Promocion> listar();

}
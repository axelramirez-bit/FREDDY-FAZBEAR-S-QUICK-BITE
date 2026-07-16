package DAO.Interfaz;

import Model.Pedido;
import java.util.List;

public interface IPedidoDAO {

    boolean insertar(Pedido pedido);

    boolean actualizar(Pedido pedido);

    boolean eliminar(int id);

    Pedido buscarPorId(int id);

    List<Pedido> listar();

}
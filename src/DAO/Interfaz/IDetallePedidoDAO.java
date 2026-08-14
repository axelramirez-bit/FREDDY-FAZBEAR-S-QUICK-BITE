package DAO.Interfaz;

import Model.DetallePedido;
import java.util.List;

public interface IDetallePedidoDAO {
    boolean insertar(DetallePedido detalle);
    boolean actualizar(DetallePedido detalle);
    boolean eliminar(int id);
    DetallePedido buscarPorId(int id);
    List<DetallePedido> listar();
    List<DetallePedido> listarPorPedido(int idPedido);
}

package Service.Interfaz;

import Model.Pedido;

import java.util.List;

public interface IPedidoService {

    boolean registrarPedido(Pedido pedido);

    boolean actualizarPedido(Pedido pedido);

    boolean eliminarPedido(int idPedido);

    Pedido obtenerPedidoPorId(int idPedido);

    List<Pedido> listarPedidos();

}
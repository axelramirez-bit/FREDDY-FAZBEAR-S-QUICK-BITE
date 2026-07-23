package Service.Interfaz;

import java.util.List;
import Model.Pago;
import Model.EstadoPago;

public interface IPagoService {
    
    boolean guardar(Pago pago);

    Pago buscarPorId(int idPago);

    Pago buscarPorPedido(int idPedido);

    List<Pago> listar();

    List<Pago> listarPorEstado(EstadoPago estado);

    boolean actualizarEstado(int idPago, EstadoPago estado);

}

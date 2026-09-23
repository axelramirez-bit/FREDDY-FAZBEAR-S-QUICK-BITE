// Paquete DAO.Interfaz
package DAO.Interfaz;

// Importa List
import java.util.List;
// Importa el modelo Pago
import Model.Pago;
// Importa el enum EstadoPago
import Model.EstadoPago;

// Contrato de acceso a datos de pagos
public interface IPagoDAO {

    // Guarda un pago
    boolean guardar(Pago pago);

    // Busca un pago por id
    Pago buscarPorId(int idPago);

    // Busca el pago de un pedido
    Pago buscarPorPedido(int idPedido);

    // Lista todos los pagos
    List<Pago> listar();

    // Lista los pagos de un estado
    List<Pago> listarPorEstado(EstadoPago estado);

    // Actualiza el estado de un pago
    boolean actualizarEstado(int idPago, EstadoPago estado);

}
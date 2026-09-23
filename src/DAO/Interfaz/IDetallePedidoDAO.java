// Paquete DAO.Interfaz
package DAO.Interfaz;

// Importa el modelo DetallePedido
import Model.DetallePedido;
// Importa List
import java.util.List;

// Contrato de acceso a datos de detalles de pedido
public interface IDetallePedidoDAO {
    // Inserta un detalle
    boolean insertar(DetallePedido detalle);
    // Actualiza un detalle
    boolean actualizar(DetallePedido detalle);
    // Elimina un detalle por id
    boolean eliminar(int id);
    // Busca un detalle por id
    DetallePedido buscarPorId(int id);
    // Lista todos los detalles
    List<DetallePedido> listar();
    // Lista los detalles de un pedido
    List<DetallePedido> listarPorPedido(int idPedido);
}

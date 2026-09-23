// Paquete Service.Interfaz
package Service.Interfaz;

// Importa el modelo Pedido
import Model.Pedido;

// Importa List
import java.util.List;

// Contrato del servicio de pedidos
public interface IPedidoService {

    // Registra un pedido
    boolean registrarPedido(Pedido pedido);

    // Actualiza un pedido
    boolean actualizarPedido(Pedido pedido);

    // Elimina un pedido por id
    boolean eliminarPedido(int idPedido);
    
    // Indica si ya existe un pedido para un carrito
    boolean existePedidoParaCarrito(int idCarrito);

    // Obtiene un pedido por id
    Pedido obtenerPedidoPorId(int idPedido);

    // Lista todos los pedidos
    List<Pedido> listarPedidos();

}
// Paquete DAO.Interfaz
package DAO.Interfaz;

// Importa el modelo Pedido
import Model.Pedido;
// Importa List
import java.util.List;

// Contrato de acceso a datos de pedidos
public interface IPedidoDAO {

    // Inserta un pedido
    boolean insertar(Pedido pedido);

    // Actualiza un pedido
    boolean actualizar(Pedido pedido);

    // Elimina un pedido por id
    boolean eliminar(int id);
    
    // Indica si ya existe un pedido para un carrito
    boolean existePedidoParaCarrito(int idCarrito);

    // Busca un pedido por id
    Pedido buscarPorId(int id);

    // Lista todos los pedidos
    List<Pedido> listar();

}
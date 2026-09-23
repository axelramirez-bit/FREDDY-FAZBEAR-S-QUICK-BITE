// Paquete DAO.Interfaz: contratos de acceso a datos
package DAO.Interfaz;

// Importa List
import java.util.List;
// Importa el modelo Carrito
import Model.Carrito;
// Importa el modelo CarritoDetalle
import Model.CarritoDetalle;
// Importa el enum EstadoCarrito
import Model.EstadoCarrito;

// Contrato de acceso a datos de carritos
public interface ICarritoDAO {

    // Crea un carrito
    boolean crear(Carrito carrito);

    // Busca un carrito por su id
    Carrito buscarPorId(int idCarrito);

    // Busca el carrito de un usuario
    Carrito buscarPorUsuario(int idUsuario);

    // Cambia el estado de un carrito
    boolean actualizarEstado(int idCarrito, EstadoCarrito estado);

    // Vacía un carrito
    boolean vaciarCarrito(int idCarrito);

    // Devuelve los detalles (productos) de un carrito
    List<CarritoDetalle> obtenerDetalles(int idCarrito);

}

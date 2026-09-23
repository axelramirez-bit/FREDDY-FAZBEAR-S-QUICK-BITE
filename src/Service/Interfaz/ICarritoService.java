// Paquete Service.Interfaz
package Service.Interfaz;

// Importa List
import java.util.List;
// Importa el modelo Carrito
import Model.Carrito;
// Importa el modelo CarritoDetalle
import Model.CarritoDetalle;
// Importa el enum EstadoCarrito
import Model.EstadoCarrito;

// Contrato del servicio de carritos
public interface ICarritoService {

    // Crea un carrito
    boolean crear(Carrito carrito);

    // Busca un carrito por id
    Carrito buscarPorId(int idCarrito);

    // Busca el carrito de un usuario
    Carrito buscarPorUsuario(int idUsuario);

    // Devuelve el carrito activo del usuario o crea uno nuevo
    Carrito obtenerOCrearCarritoActivo(int idUsuario);

    // Cambia el estado de un carrito
    boolean actualizarEstado(int idCarrito, EstadoCarrito estado);

    // Vacía un carrito
    boolean vaciarCarrito(int idCarrito);

    // Devuelve los detalles de un carrito
    List<CarritoDetalle> obtenerDetalles(int idCarrito);

}

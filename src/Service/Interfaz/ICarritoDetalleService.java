// Paquete Service.Interfaz: contratos de la lógica de negocio
package Service.Interfaz;

// Importa List
import java.util.List;
// Importa el modelo CarritoDetalle
import Model.CarritoDetalle;

// Contrato del servicio de detalles del carrito
public interface ICarritoDetalleService {
    
    // Agrega un producto al carrito
    boolean agregarProducto(CarritoDetalle detalle);

    // Cambia la cantidad de un detalle
    boolean actualizarCantidad(int idDetalle, int cantidad);

    // Elimina un producto del carrito
    boolean eliminarProducto(int idDetalle);

    // Lista los detalles de un carrito
    List<CarritoDetalle> listarPorCarrito(int idCarrito);
    
}

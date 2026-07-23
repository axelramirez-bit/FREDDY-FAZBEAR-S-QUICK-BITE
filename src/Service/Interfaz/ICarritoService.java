package Service.Interfaz;

import java.util.List;
import Model.Carrito;
import Model.CarritoDetalle;
import Model.EstadoCarrito;

public interface ICarritoService {
    
    boolean crear(Carrito carrito);

    Carrito buscarPorId(int idCarrito);

    Carrito buscarPorUsuario(int idUsuario);

    boolean actualizarEstado(int idCarrito, EstadoCarrito estado);

    boolean vaciarCarrito(int idCarrito);

    List<CarritoDetalle> obtenerDetalles(int idCarrito);
    
}

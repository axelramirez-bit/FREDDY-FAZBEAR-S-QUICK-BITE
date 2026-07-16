package DAO.Interfaz;

import java.util.List;
import Model.CarritoDetalle;

public interface ICarritoDetalleDAO {
    
    boolean agregarProducto(CarritoDetalle detalle);

    boolean actualizarCantidad(int idDetalle, int cantidad);

    boolean eliminarProducto(int idDetalle);

    List<CarritoDetalle> listarPorCarrito(int idCarrito);
    
}
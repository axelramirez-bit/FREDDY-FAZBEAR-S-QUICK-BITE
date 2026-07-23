package Service.Implement;

import DAO.Implement.CarritoDetalleDAOImpl;
import DAO.Interfaz.ICarritoDetalleDAO;
import Model.CarritoDetalle;
import Service.Interfaz.ICarritoDetalleService;

import java.util.List;

public class CarritoDetalleServiceImpl implements ICarritoDetalleService {

    private final ICarritoDetalleDAO carritoDetalleDAO;

    public CarritoDetalleServiceImpl() {
        this.carritoDetalleDAO = new CarritoDetalleDAOImpl();
    }

    @Override
    public boolean agregarProducto(CarritoDetalle detalle) {

        if (detalle == null) {
            return false;
        }

        if (detalle.getCarrito() == null) {
            return false;
        }

        if (detalle.getProducto() == null) {
            return false;
        }

        if (detalle.getCantidad() <= 0) {
            return false;
        }

        return carritoDetalleDAO.agregarProducto(detalle);
    }

    @Override
    public boolean actualizarCantidad(int idDetalle, int cantidad) {

        if (idDetalle <= 0) {
            return false;
        }

        if (cantidad <= 0) {
            return false;
        }

        return carritoDetalleDAO.actualizarCantidad(idDetalle, cantidad);
    }

    @Override
    public boolean eliminarProducto(int idDetalle) {

        if (idDetalle <= 0) {
            return false;
        }

        return carritoDetalleDAO.eliminarProducto(idDetalle);
    }

    @Override
    public List<CarritoDetalle> listarPorCarrito(int idCarrito) {

        if (idCarrito <= 0) {
            return List.of();
        }

        return carritoDetalleDAO.listarPorCarrito(idCarrito);
    }
}
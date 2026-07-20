package Service.Implement;

import DAO.Implement.CarritoDAOImpl;
import DAO.Interfaz.ICarritoDAO;
import Model.Carrito;
import Model.CarritoDetalle;
import Model.EstadoCarrito;
import Service.Interfaz.ICarritoService;

import java.util.List;

public class CarritoServiceImpl implements ICarritoService {

    private final ICarritoDAO carritoDAO;

    public CarritoServiceImpl() {
        this.carritoDAO = new CarritoDAOImpl();
    }

    @Override
    public boolean crear(Carrito carrito) {

        if (carrito == null) {
            return false;
        }

        if (carrito.getUsuario() == null) {
            return false;
        }

        return carritoDAO.crear(carrito);
    }

    @Override
    public Carrito buscarPorId(int idCarrito) {

        if (idCarrito <= 0) {
            return null;
        }

        return carritoDAO.buscarPorId(idCarrito);
    }

    @Override
    public Carrito buscarPorUsuario(int idUsuario) {

        if (idUsuario <= 0) {
            return null;
        }

        return carritoDAO.buscarPorUsuario(idUsuario);
    }

    @Override
    public boolean actualizarEstado(int idCarrito, EstadoCarrito estado) {

        if (idCarrito <= 0 || estado == null) {
            return false;
        }

        return carritoDAO.actualizarEstado(idCarrito, estado);
    }

    @Override
    public boolean vaciarCarrito(int idCarrito) {

        if (idCarrito <= 0) {
            return false;
        }

        return carritoDAO.vaciarCarrito(idCarrito);
    }

    @Override
    public List<CarritoDetalle> obtenerDetalles(int idCarrito) {

        if (idCarrito <= 0) {
            return List.of();
        }

        return carritoDAO.obtenerDetalles(idCarrito);
    }

}

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

        // BUG QUE ESTO CORRIGE: agregar el mismo producto dos veces al
        // carrito insertaba dos filas separadas en carrito_detalle
        // (cantidad=1 cada una) en vez de una sola fila con cantidad=2,
        // así que "Tu pedido" y la factura mostraban la misma línea
        // repetida (ver factura con "Pizza Party Personal" dos veces).
        // Si ya existe una línea para el mismo producto CON LAS MISMAS
        // observaciones, se suma la cantidad ahí en vez de insertar una
        // fila nueva. Si las observaciones son distintas, se deja como
        // línea aparte para no perder la nota específica de cada una.
        CarritoDetalle existente = buscarLineaExistente(detalle);

        if (existente != null) {
            return carritoDetalleDAO.actualizarCantidad(
                    existente.getIdCarritoDetalle(),
                    existente.getCantidad() + detalle.getCantidad()
            );
        }

        return carritoDetalleDAO.agregarProducto(detalle);
    }

    // Busca, entre las líneas ya guardadas del carrito, una que sea del
    // mismo producto y con las mismas observaciones que "nuevo".
    private CarritoDetalle buscarLineaExistente(CarritoDetalle nuevo) {

        List<CarritoDetalle> actuales = carritoDetalleDAO.listarPorCarrito(nuevo.getCarrito().getIdCarrito());
        String obsNuevo = normalizar(nuevo.getObservaciones());

        for (CarritoDetalle actual : actuales) {

            boolean mismoProducto = actual.getProducto().getIdProducto() == nuevo.getProducto().getIdProducto();
            boolean mismasObs = normalizar(actual.getObservaciones()).equals(obsNuevo);

            if (mismoProducto && mismasObs) {
                return actual;
            }
        }

        return null;
    }

    private String normalizar(String texto) {
        return texto == null ? "" : texto.trim();
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
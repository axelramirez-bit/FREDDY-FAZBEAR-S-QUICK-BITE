// Paquete Service.Implement
package Service.Implement;

// Importa CarritoDAOImpl
import DAO.Implement.CarritoDAOImpl;
// Importa ICarritoDAO
import DAO.Interfaz.ICarritoDAO;
// Importa Carrito
import Model.Carrito;
// Importa CarritoDetalle
import Model.CarritoDetalle;
// Importa EstadoCarrito
import Model.EstadoCarrito;
// Importa ICarritoService
import Service.Interfaz.ICarritoService;
// Importa Usuario
import Model.Usuario;

// Importa List
import java.util.List;

// Servicio de carritos
public class CarritoServiceImpl implements ICarritoService {

    // DAO de carritos
    private final ICarritoDAO carritoDAO;

    // Constructor: crea el DAO real
    public CarritoServiceImpl() {
        // Crea el DAO
        this.carritoDAO = new CarritoDAOImpl();
    }

    @Override
    // Devuelve el carrito activo del usuario o crea uno nuevo
    public Carrito obtenerOCrearCarritoActivo(int idUsuario) {

        // Si el id no es válido
        if (idUsuario <= 0) {
            // devuelve null
            return null;
        }

        // Busca el carrito activo. OJO: buscar y luego crear no es atómico; dos ventanas a la vez podrían crear dos carritos
        Carrito carrito = carritoDAO.buscarPorUsuario(idUsuario);

        // Si ya existe
        if (carrito != null) {
            // lo devuelve
            return carrito;
        }

        // Crea un usuario solo con el id
        Usuario usuario = new Usuario();
        // Asigna el id
        usuario.setIdUsuario(idUsuario);

        // Crea el carrito nuevo
        Carrito nuevoCarrito = new Carrito(usuario);

        // Lo guarda en la base de datos
        boolean creado = carritoDAO.crear(nuevoCarrito);

        // Si no se pudo crear
        if (!creado) {
            // devuelve null
            return null;
        }

        // Lo vuelve a leer con sus datos
        return carritoDAO.buscarPorUsuario(idUsuario);
    }

    @Override
    // Crea un carrito
    public boolean crear(Carrito carrito) {

        // Si es nulo
        if (carrito == null) {
            // devuelve false
            return false;
        }

        // Si no tiene usuario
        if (carrito.getUsuario() == null) {
            // devuelve false
            return false;
        }

        // Guarda el carrito
        return carritoDAO.crear(carrito);
    }

    @Override
    // Busca un carrito por id
    public Carrito buscarPorId(int idCarrito) {

        // Si el id no es válido
        if (idCarrito <= 0) {
            // devuelve null
            return null;
        }

        // Consulta al DAO
        return carritoDAO.buscarPorId(idCarrito);
    }

    @Override
    // Busca el carrito de un usuario
    public Carrito buscarPorUsuario(int idUsuario) {

        // Si el id no es válido
        if (idUsuario <= 0) {
            // devuelve null
            return null;
        }

        // Consulta al DAO
        return carritoDAO.buscarPorUsuario(idUsuario);
    }

    @Override
    // Cambia el estado de un carrito
    public boolean actualizarEstado(int idCarrito, EstadoCarrito estado) {

        // Si el id o el estado no son válidos
        if (idCarrito <= 0 || estado == null) {
            // devuelve false
            return false;
        }

        // Actualiza en el DAO
        return carritoDAO.actualizarEstado(idCarrito, estado);
    }

    @Override
    // Vacía un carrito
    public boolean vaciarCarrito(int idCarrito) {

        // Si el id no es válido
        if (idCarrito <= 0) {
            // devuelve false
            return false;
        }

        // Vacía en el DAO
        return carritoDAO.vaciarCarrito(idCarrito);
    }

    @Override
    // Devuelve los detalles de un carrito
    public List<CarritoDetalle> obtenerDetalles(int idCarrito) {

        // Si el id no es válido
        if (idCarrito <= 0) {
            // devuelve lista vacía
            return List.of();
        }

        // Consulta al DAO
        return carritoDAO.obtenerDetalles(idCarrito);
    }

}

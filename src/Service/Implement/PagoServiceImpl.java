// Paquete Service.Implement
package Service.Implement;

// Importa PagoDAOImpl
import DAO.Implement.PagoDAOImpl;
// Importa IPagoDAO
import DAO.Interfaz.IPagoDAO;
// Importa EstadoPago
import Model.EstadoPago;
// Importa Pago
import Model.Pago;
// Importa IPagoService
import Service.Interfaz.IPagoService;
// Importa BigDecimal
import java.math.BigDecimal;

// Importa List
import java.util.List;

// Servicio de pagos
public class PagoServiceImpl implements IPagoService {

    // DAO de pagos
    private final IPagoDAO pagoDAO;

    // Constructor: crea el DAO real
    public PagoServiceImpl() {
        // Crea el DAO
        this.pagoDAO = new PagoDAOImpl();
    }

    @Override
    // Guarda un pago tras validarlo
    public boolean guardar(Pago pago) {

        // Si el pago es nulo
        if (pago == null) {
            // devuelve false
            return false;
        }

        // Si no tiene pedido
        if (pago.getPedido() == null) {
            // devuelve false
            return false;
        }

        // Si no tiene método de pago
        if (pago.getMetodoPago() == null) {
            // devuelve false
            return false;
        }

        // Si no tiene estado
        if (pago.getEstado() == null) {
            // devuelve false
            return false;
        }

        // Si el monto es nulo o no es mayor que cero. OJO: un pago de monto 0 se rechaza
        if (pago.getMonto() == null
                || pago.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            // devuelve false
            return false;
        }
        // Guarda el pago
        return pagoDAO.guardar(pago);
    }

    @Override
    // Busca un pago por id
    public Pago buscarPorId(int idPago) {

        // Si el id no es válido
        if (idPago <= 0) {
            // devuelve null
            return null;
        }

        // Consulta al DAO
        return pagoDAO.buscarPorId(idPago);
    }

    @Override
    // Busca el pago de un pedido
    public Pago buscarPorPedido(int idPedido) {

        // Si el id no es válido
        if (idPedido <= 0) {
            // devuelve null
            return null;
        }

        // Consulta al DAO
        return pagoDAO.buscarPorPedido(idPedido);
    }

    @Override
    // Lista todos los pagos
    public List<Pago> listar() {

        // Consulta al DAO
        return pagoDAO.listar();
    }

    @Override
    // Lista los pagos de un estado
    public List<Pago> listarPorEstado(EstadoPago estado) {

        // Si el estado es nulo
        if (estado == null) {
            // devuelve lista vacía
            return List.of();
        }

        // Consulta al DAO
        return pagoDAO.listarPorEstado(estado);
    }

    @Override
    // Actualiza el estado de un pago
    public boolean actualizarEstado(int idPago, EstadoPago estado) {

        // Si el id no es válido
        if (idPago <= 0) {
            // devuelve false
            return false;
        }

        // Si el estado es nulo
        if (estado == null) {
            // devuelve false
            return false;
        }

        // Actualiza en el DAO
        return pagoDAO.actualizarEstado(idPago, estado);
    }
}

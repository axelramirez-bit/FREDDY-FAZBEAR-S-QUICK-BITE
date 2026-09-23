// Paquete Service.Implement
package Service.Implement;

// Importa FacturaDAOImpl
import DAO.Implement.FacturaDAOImpl;
// Importa IFacturaDAO
import DAO.Interfaz.IFacturaDAO;
// Importa Factura
import Model.Factura;
// Importa IFacturaService
import Service.Interfaz.IFacturaService;
// Importa BigDecimal
import java.math.BigDecimal;

// Importa List
import java.util.List;

// Servicio de facturas
public class FacturaServiceImpl implements IFacturaService {

    // DAO de facturas
    private final IFacturaDAO facturaDAO;

    // Constructor: crea el DAO real
    public FacturaServiceImpl() {
        // Crea el DAO
        this.facturaDAO = new FacturaDAOImpl();
    }

    @Override
    // Guarda una factura tras validarla
    public boolean guardar(Factura factura) {

        // Si es nula
        if (factura == null) {
            // devuelve false
            return false;
        }

        // Si no tiene pedido
        if (factura.getPedido() == null) {
            // devuelve false
            return false;
        }

        // Si no tiene número de factura
        if (factura.getNumeroFactura() == null
                || factura.getNumeroFactura().trim().isEmpty()) {
            // devuelve false
            return false;
        }

        // Si el subtotal es nulo o negativo
        if (factura.getSubtotal() == null
                || factura.getSubtotal().compareTo(BigDecimal.ZERO) < 0) {
            // devuelve false
            return false;
        }

        // Si el total es nulo o negativo
        if (factura.getTotal() == null
                || factura.getTotal().compareTo(BigDecimal.ZERO) < 0) {
            // devuelve false
            return false;
        }
        // Guarda la factura
        return facturaDAO.guardar(factura);
    }

    @Override
    // Busca una factura por id
    public Factura buscarPorId(int idFactura) {

        // Si el id no es válido
        if (idFactura <= 0) {
            // devuelve null
            return null;
        }

        // Consulta al DAO
        return facturaDAO.buscarPorId(idFactura);
    }

    @Override
    // Busca una factura por su número
    public Factura buscarPorNumero(String numeroFactura) {

        // Si el número es nulo o vacío
        if (numeroFactura == null
                || numeroFactura.trim().isEmpty()) {
            // devuelve null
            return null;
        }

        // Consulta al DAO
        return facturaDAO.buscarPorNumero(numeroFactura);
    }

    @Override
    // Busca la factura de un pedido
    public Factura buscarPorPedido(int idPedido) {

        // Si el id no es válido
        if (idPedido <= 0) {
            // devuelve null
            return null;
        }

        // Consulta al DAO
        return facturaDAO.buscarPorPedido(idPedido);
    }

    @Override
    // Lista todas las facturas
    public List<Factura> listar() {

        // Consulta al DAO
        return facturaDAO.listar();
    }

    @Override
    // Lista las facturas de un cliente
    public List<Factura> listarPorCliente(int idCliente) {

        // Si el id no es válido
        if (idCliente <= 0) {
            // devuelve lista vacía
            return List.of();
        }

        // Consulta al DAO
        return facturaDAO.listarPorCliente(idCliente);
    }
}

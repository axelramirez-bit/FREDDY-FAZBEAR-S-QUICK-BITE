// Paquete DAO.Interfaz
package DAO.Interfaz;

// Importa el modelo Factura
import Model.Factura;
// Importa List
import java.util.List;

// Contrato de acceso a datos de facturas
public interface IFacturaDAO {

    // Guarda una factura
    boolean guardar(Factura factura);

    // Busca una factura por id
    Factura buscarPorId(int idFactura);

    // Busca una factura por su número
    Factura buscarPorNumero(String numeroFactura);

    // Busca la factura de un pedido
    Factura buscarPorPedido(int idPedido);

    // Lista todas las facturas
    List<Factura> listar();

    // Lista las facturas de un cliente
    List<Factura> listarPorCliente(int idCliente);

}

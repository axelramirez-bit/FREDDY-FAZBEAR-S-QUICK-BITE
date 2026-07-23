package Service.Implement;

import DAO.Implement.FacturaDAOImpl;
import DAO.Interfaz.IFacturaDAO;
import Model.Factura;
import Service.Interfaz.IFacturaService;

import java.util.List;

public class FacturaServiceImpl implements IFacturaService {

    private final IFacturaDAO facturaDAO;

    public FacturaServiceImpl() {
        this.facturaDAO = new FacturaDAOImpl();
    }

    @Override
    public boolean guardar(Factura factura) {

        if (factura == null) {
            return false;
        }

        if (factura.getPedido() == null) {
            return false;
        }

        if (factura.getNumeroFactura() == null
                || factura.getNumeroFactura().trim().isEmpty()) {
            return false;
        }

        if (factura.getSubtotal() < 0) {
            return false;
        }

        if (factura.getTotal() < 0) {
            return false;
        }

        return facturaDAO.guardar(factura);
    }

    @Override
    public Factura buscarPorId(int idFactura) {

        if (idFactura <= 0) {
            return null;
        }

        return facturaDAO.buscarPorId(idFactura);
    }

    @Override
    public Factura buscarPorNumero(String numeroFactura) {

        if (numeroFactura == null
                || numeroFactura.trim().isEmpty()) {
            return null;
        }

        return facturaDAO.buscarPorNumero(numeroFactura);
    }

    @Override
    public Factura buscarPorPedido(int idPedido) {

        if (idPedido <= 0) {
            return null;
        }

        return facturaDAO.buscarPorPedido(idPedido);
    }

    @Override
    public List<Factura> listar() {

        return facturaDAO.listar();
    }

    @Override
    public List<Factura> listarPorCliente(int idCliente) {

        if (idCliente <= 0) {
            return List.of();
        }

        return facturaDAO.listarPorCliente(idCliente);
    }
}
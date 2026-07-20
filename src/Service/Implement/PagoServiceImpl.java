package Service.Implement;

import DAO.Implement.PagoDAOImpl;
import DAO.Interfaz.IPagoDAO;
import Model.EstadoPago;
import Model.Pago;
import Service.Interfaz.IPagoService;

import java.util.List;

public class PagoServiceImpl implements IPagoService {

    private final IPagoDAO pagoDAO;

    public PagoServiceImpl() {
        this.pagoDAO = new PagoDAOImpl();
    }

    @Override
    public boolean guardar(Pago pago) {

        if (pago == null) {
            return false;
        }

        if (pago.getPedido() == null) {
            return false;
        }

        if (pago.getMetodoPago() == null) {
            return false;
        }

        if (pago.getEstado() == null) {
            return false;
        }

        if (pago.getMonto() <= 0) {
            return false;
        }

        return pagoDAO.guardar(pago);
    }

    @Override
    public Pago buscarPorId(int idPago) {

        if (idPago <= 0) {
            return null;
        }

        return pagoDAO.buscarPorId(idPago);
    }

    @Override
    public Pago buscarPorPedido(int idPedido) {

        if (idPedido <= 0) {
            return null;
        }

        return pagoDAO.buscarPorPedido(idPedido);
    }

    @Override
    public List<Pago> listar() {

        return pagoDAO.listar();
    }

    @Override
    public List<Pago> listarPorEstado(EstadoPago estado) {

        if (estado == null) {
            return List.of();
        }

        return pagoDAO.listarPorEstado(estado);
    }

    @Override
    public boolean actualizarEstado(int idPago, EstadoPago estado) {

        if (idPago <= 0) {
            return false;
        }

        if (estado == null) {
            return false;
        }

        return pagoDAO.actualizarEstado(idPago, estado);
    }
}
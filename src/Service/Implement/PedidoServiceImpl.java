package Service.Implement;

import DAO.Implement.PedidoDAOImpl;
import DAO.Interfaz.IPedidoDAO;
import Model.EstadoPedido;
import Model.Pedido;
import Service.Interfaz.IPedidoService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PedidoServiceImpl implements IPedidoService {

    private final IPedidoDAO pedidoDAO;

    public PedidoServiceImpl() {
        this.pedidoDAO = new PedidoDAOImpl();
    }

    // Permite inyectar el DAO (útil para pruebas unitarias con mocks)
    public PedidoServiceImpl(IPedidoDAO pedidoDAO) {
        this.pedidoDAO = pedidoDAO;
    }

    @Override
    public boolean registrarPedido(Pedido pedido) {

        if (!validarPedido(pedido)) {
            return false;
        }

        if (pedido.getNumeroOrden() == null || pedido.getNumeroOrden().isBlank()) {
            pedido.setNumeroOrden(generarNumeroOrden());
        }

        if (pedido.getFecha() == null) {
            pedido.setFecha(LocalDateTime.now());
        }

        if (pedido.getEstado() == null) {
            pedido.setEstado(EstadoPedido.PENDIENTE);
        }

        calcularTotal(pedido);

        return pedidoDAO.insertar(pedido);

    }

    @Override
    public boolean actualizarPedido(Pedido pedido) {

        if (pedido.getIdPedido() <= 0) {
            return false;
        }

        if (!validarPedido(pedido)) {
            return false;
        }

        Pedido existente = pedidoDAO.buscarPorId(pedido.getIdPedido());

        if (existente == null) {
            return false;
        }

        calcularTotal(pedido);

        return pedidoDAO.actualizar(pedido);

    }

    @Override
    public boolean eliminarPedido(int idPedido) {

        if (idPedido <= 0) {
            return false;
        }

        return pedidoDAO.eliminar(idPedido);

    }

    @Override
    public Pedido obtenerPedidoPorId(int idPedido) {

        if (idPedido <= 0) {
            return null;
        }

        return pedidoDAO.buscarPorId(idPedido);

    }

    @Override
    public List<Pedido> listarPedidos() {
        return pedidoDAO.listar();
    }

    // ---------- Métodos auxiliares de negocio ----------

    private boolean validarPedido(Pedido pedido) {

        if (pedido == null) {
            return false;
        }

        if (pedido.getIdUsuario() == null || pedido.getIdUsuario().getIdUsuario() <= 0) {
            return false;
        }

        if (pedido.getTipoEntrega() == null) {
            return false;
        }

        if (pedido.getSubtotal() < 0 || pedido.getDescuento() < 0) {
            return false;
        }

        if (pedido.getDescuento() > pedido.getSubtotal()) {
            return false;
        }

        return true;

    }

    private void calcularTotal(Pedido pedido) {
        double total = pedido.getSubtotal() - pedido.getDescuento();
        pedido.setTotal(Math.max(total, 0));
    }

    private String generarNumeroOrden() {
        return "ORD-" + UUID.randomUUID().toString()
                .substring(0, 8)
                .toUpperCase();
    }

}
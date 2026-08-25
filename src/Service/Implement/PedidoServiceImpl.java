package Service.Implement;

import Config.Conexion;
import DAO.Implement.DetallePedidoDAOImpl;
import DAO.Implement.PedidoDAOImpl;
import DAO.Interfaz.IDetallePedidoDAO;
import DAO.Interfaz.IPedidoDAO;
import Model.DetallePedido;
import Model.EstadoPedido;
import Model.Pedido;
import Service.Interfaz.IPedidoService;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PedidoServiceImpl implements IPedidoService {

    private final IPedidoDAO pedidoDAO;
    private final IDetallePedidoDAO detallePedidoDAO;

    public PedidoServiceImpl() {
        this.pedidoDAO = new PedidoDAOImpl();
        this.detallePedidoDAO = new DetallePedidoDAOImpl();
    }

    // Permite inyectar los DAO (útil para pruebas unitarias con mocks)
    public PedidoServiceImpl(IPedidoDAO pedidoDAO, IDetallePedidoDAO detallePedidoDAO) {
        this.pedidoDAO = pedidoDAO;
        this.detallePedidoDAO = detallePedidoDAO;
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

        // Conexion es un singleton (un solo Connection real para toda
        // la app — ver Config/Conexion.java), así que este DAO y
        // DetallePedidoDAOImpl comparten la misma conexión real
        // aunque cada uno pida la suya por separado. Eso permite
        // envolver el INSERT del encabezado + sus líneas en una sola
        // transacción real, sin tener que rediseñar los DAO para que
        // reciban el Connection por parámetro.
        Connection con = Conexion.getInstancia().getConexion();

        try {
            con.setAutoCommit(false);

            boolean pedidoInsertado = pedidoDAO.insertar(pedido);

            if (!pedidoInsertado || pedido.getIdPedido() <= 0) {
                con.rollback();
                return false;
            }

            for (DetallePedido detalle : pedido.getDetalles()) {

                detalle.setPedido(pedido);

                if (!detallePedidoDAO.insertar(detalle)) {
                    con.rollback();
                    return false;
                }
            }

            con.commit();
            return true;

        } catch (SQLException e) {

            try {
                con.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }

            e.printStackTrace();
            return false;

        } finally {

            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

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

        if (pedido.getSubtotal() == null
                || pedido.getSubtotal().compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }

        if (pedido.getDescuento() == null
                || pedido.getDescuento().compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }

        if (pedido.getDescuento().compareTo(pedido.getSubtotal()) > 0) {
            return false;
        }

        return true;

    }

    private void calcularTotal(Pedido pedido) {

        BigDecimal total = pedido.getSubtotal()
                .subtract(pedido.getDescuento());

        if (total.compareTo(BigDecimal.ZERO) < 0) {
            total = BigDecimal.ZERO;
        }

        pedido.setTotal(total);

    }

    private String generarNumeroOrden() {
        return "ORD-" + UUID.randomUUID().toString()
                .substring(0, 8)
                .toUpperCase();
    }

}

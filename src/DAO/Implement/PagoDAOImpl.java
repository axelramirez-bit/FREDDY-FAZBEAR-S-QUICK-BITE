package DAO.Implement;

import Config.Conexion;
import DAO.Interfaz.IPagoDAO;
import Model.EstadoPago;
import Model.MetodoPago;
import Model.Pago;
import Model.Pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class PagoDAOImpl implements IPagoDAO {
    
    @Override
    public boolean guardar(Pago pago) {

        String sql = "INSERT INTO pago(id_pedido, metodo_pago, monto, fecha_pago, estado) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, pago.getPedido().getIdPedido());
            ps.setString(2, metodoPagoToDb(pago.getMetodoPago()));
            ps.setDouble(3, pago.getMonto());

            if (pago.getFechaPago() != null) {
                ps.setTimestamp(4, Timestamp.valueOf(pago.getFechaPago()));
            } else {
                ps.setNull(4, Types.TIMESTAMP);
            }

            ps.setString(5, estadoToDb(pago.getEstado()));

            boolean creado = ps.executeUpdate() > 0;

            if (creado) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        pago.setIdPago(rs.getInt(1));
                    }
                }
            }

            return creado;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Pago buscarPorId(int idPago) {

        String sql = "SELECT id_pago, id_pedido, metodo_pago, monto, fecha_pago, estado "
                + "FROM pago WHERE id_pago = ?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPago);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPago(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Pago buscarPorPedido(int idPedido) {
        
        String sql = "SELECT id_pago, id_pedido, metodo_pago, monto, fecha_pago, estado "
                + "FROM pago WHERE id_pedido = ?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPago(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Pago> listar() {

        List<Pago> pagos = new ArrayList<>();

        String sql = "SELECT id_pago, id_pedido, metodo_pago, monto, fecha_pago, estado FROM pago";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                pagos.add(mapearPago(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pagos;
    }

    @Override
    public List<Pago> listarPorEstado(EstadoPago estado) {

        List<Pago> pagos = new ArrayList<>();

        String sql = "SELECT id_pago, id_pedido, metodo_pago, monto, fecha_pago, estado "
                + "FROM pago WHERE estado = ?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estadoToDb(estado));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pagos.add(mapearPago(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pagos;
    }

    @Override
    public boolean actualizarEstado(int idPago, EstadoPago estado) {

        String sql = "UPDATE pago SET estado = ?, fecha_pago = ? WHERE id_pago = ?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estadoToDb(estado));

            // Si se confirma el pago, se registra la fecha_pago automáticamente.
            if (estado == EstadoPago.PAGADO) {
                ps.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            } else {
                ps.setNull(2, Types.TIMESTAMP);
            }

            ps.setInt(3, idPago);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==================== MÉTODOS DE APOYO ====================

    private Pago mapearPago(ResultSet rs) throws SQLException {

        Pedido pedido = new Pedido();
        pedido.setIdPedido(rs.getInt("id_pedido"));

        Pago pago = new Pago();
        pago.setIdPago(rs.getInt("id_pago"));
        pago.setPedido(pedido);
        pago.setMetodoPago(metodoPagoDesdeDb(rs.getString("metodo_pago")));
        pago.setMonto(rs.getDouble("monto"));

        Timestamp fechaPago = rs.getTimestamp("fecha_pago");
        pago.setFechaPago(fechaPago != null ? fechaPago.toLocalDateTime() : null);

        pago.setEstado(estadoDesdeDb(rs.getString("estado")));

        return pago;
    }

    private String estadoToDb(EstadoPago estado) {
        switch (estado) {
            case PENDIENTE:
                return "Pendiente";
            case PAGADO:
                return "Pagado";
            case RECHAZADO:
                return "Rechazado";
            default:
                throw new IllegalArgumentException("Estado no reconocido: " + estado);
        }
    }

    private EstadoPago estadoDesdeDb(String estado) {
        switch (estado) {
            case "Pendiente":
                return EstadoPago.PENDIENTE;
            case "Pagado":
                return EstadoPago.PAGADO;
            case "Rechazado":
                return EstadoPago.RECHAZADO;
            default:
                throw new IllegalArgumentException("Estado no reconocido: " + estado);
        }
    }

    private String metodoPagoToDb(MetodoPago metodo) {
        switch (metodo) {
            case EFECTIVO:
                return "Efectivo";
            case TARJETA:
                return "Tarjeta";
            case TRANSFERENCIA:
                return "TRANSFERENCIA";
            default:
                throw new IllegalArgumentException("Método de pago no reconocido: " + metodo);
        }
    }

    private MetodoPago metodoPagoDesdeDb(String metodo) {
        switch (metodo) {
            case "Efectivo":
                return MetodoPago.EFECTIVO;
            case "Tarjeta":
                return MetodoPago.TARJETA;
            case "TRANSFERENCIA":
                return MetodoPago.TRANSFERENCIA;
            default:
                throw new IllegalArgumentException("Método de pago no reconocido: " + metodo);
        }
    }

}
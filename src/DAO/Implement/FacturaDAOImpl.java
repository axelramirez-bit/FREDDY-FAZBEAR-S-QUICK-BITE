package DAO.Implement;

import Config.Conexion;
import DAO.Interfaz.IFacturaDAO;
import Model.Factura;
import Model.Pedido;
import Model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAOImpl implements IFacturaDAO {

    private static final double PORCENTAJE_IVA = 0.12;

    @Override
    public boolean guardar(Factura factura) {

        String sql = "INSERT INTO factura(id_pedido, numero_factura, fecha, nit, "
                + "nombre_cliente, direccion, subtotal, descuento, iva, total) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        double iva = factura.getSubtotal() * PORCENTAJE_IVA;

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, factura.getPedido().getIdPedido());
            ps.setString(2, factura.getNumeroFactura());
            ps.setTimestamp(3, Timestamp.valueOf(
                    factura.getFecha() != null ? factura.getFecha() : java.time.LocalDateTime.now()));
            ps.setNull(4, java.sql.Types.VARCHAR); // nit: no existe en el modelo
            ps.setString(5, factura.getCliente() != null ? factura.getCliente().getNombreCompleto() : null);
            ps.setString(6, factura.getDireccion());
            ps.setDouble(7, factura.getSubtotal());
            ps.setDouble(8, factura.getDescuento());
            ps.setDouble(9, iva);
            ps.setDouble(10, factura.getTotal());

            boolean creada = ps.executeUpdate() > 0;

            if (creada) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        factura.setIdFactura(rs.getInt(1));
                    }
                }
            }

            return creada;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Factura buscarPorId(int idFactura) {

        String sql = baseSelect() + " WHERE f.id_factura = ?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idFactura);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearFactura(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Factura buscarPorNumero(String numeroFactura) {

        String sql = baseSelect() + " WHERE f.numero_factura = ?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, numeroFactura);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearFactura(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Factura buscarPorPedido(int idPedido) {

        String sql = baseSelect() + " WHERE f.id_pedido = ?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearFactura(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Factura> listar() {

        List<Factura> facturas = new ArrayList<>();

        String sql = baseSelect() + " ORDER BY f.fecha DESC";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                facturas.add(mapearFactura(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return facturas;
    }

    @Override
    public List<Factura> listarPorCliente(int idCliente) {

        List<Factura> facturas = new ArrayList<>();

        String sql = baseSelect() + " WHERE p.id_usuario = ? ORDER BY f.fecha DESC";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    facturas.add(mapearFactura(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return facturas;
    }

    // ==================== MÉTODOS DE APOYO ====================

    private String baseSelect() {
        return "SELECT f.id_factura, f.id_pedido, f.numero_factura, f.fecha, "
                + "f.nombre_cliente, f.direccion, f.subtotal, f.descuento, f.iva, f.total, "
                + "p.id_usuario "
                + "FROM factura f "
                + "JOIN pedido p ON f.id_pedido = p.id_pedido";
    }

    private Factura mapearFactura(ResultSet rs) throws SQLException {

        Pedido pedido = new Pedido();
        pedido.setIdPedido(rs.getInt("id_pedido"));

        // Cliente mínimo: el nombre completo se guarda como texto plano en
        // `nombre_cliente`, se coloca en `nombre` porque Usuario no tiene
        // un único campo de nombre completo editable directamente.
        Usuario cliente = new Usuario();
        cliente.setIdUsuario(rs.getInt("id_usuario"));
        cliente.setNombre(rs.getString("nombre_cliente"));

        Factura factura = new Factura();
        factura.setIdFactura(rs.getInt("id_factura"));
        factura.setPedido(pedido);
        factura.setNumeroFactura(rs.getString("numero_factura"));
        factura.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
        factura.setCliente(cliente);
        factura.setDireccion(rs.getString("direccion"));
        factura.setSubtotal(rs.getDouble("subtotal"));
        factura.setDescuento(rs.getDouble("descuento"));
        factura.setTotal(rs.getDouble("total"));
        // f.iva se calcula al vuelo desde subtotal; no se expone porque
        // el modelo Factura no tiene ese campo.

        return factura;
    }

}
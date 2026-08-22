package DAO.Implement;

import Config.Conexion;
import Utils.AppLogger;
import DAO.Interfaz.IFacturaDAO;
import Model.Factura;
import Model.Pedido;
import Model.Usuario;
import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAOImpl implements IFacturaDAO {

    private static final BigDecimal IVA =
        new BigDecimal("0.12");

    @Override
    public boolean guardar(Factura factura) {

        String sql = "INSERT INTO factura(id_pedido, numero_factura, fecha, nit, "
                + "nombre_cliente, direccion, subtotal, descuento, iva, total) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Corrección: antes se recalculaba el IVA aquí mismo con una
        // constante propia (IVA = 0.12) en vez de usar el valor que
        // ya trae la factura (calculado en Factura.calcularIva(),
        // que es la única fuente de verdad para la tasa de IVA).
        // Si alguna vez cambia la tasa, antes había que tocarla en
        // dos lugares distintos.
        BigDecimal iva = factura.getIva() != null
                ? factura.getIva()
                : factura.getSubtotal().multiply(IVA);

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, factura.getPedido().getIdPedido());
            ps.setString(2, factura.getNumeroFactura());
            ps.setTimestamp(3, Timestamp.valueOf(
                    factura.getFecha() != null ? factura.getFecha() : java.time.LocalDateTime.now()));

            // Corrección: antes se guardaba siempre NULL con el
            // comentario "nit: no existe en el modelo", pero
            // Model.Factura sí tiene getNit()/setNit(). Ahora si el
            // NIT fue capturado (ver PanelFacturaWizard / Registro de
            // pago), se guarda; si no, queda NULL igual que antes.
            if (factura.getNit() != null && !factura.getNit().isBlank()) {
                ps.setString(4, factura.getNit());
            } else {
                ps.setNull(4, java.sql.Types.VARCHAR);
            }

            ps.setString(5, factura.getCliente() != null ? factura.getCliente().getNombreCompleto() : null);
            ps.setString(6, factura.getDireccion());
            ps.setBigDecimal(7, factura.getSubtotal());
            ps.setBigDecimal(8, factura.getDescuento());
            ps.setBigDecimal(9, iva);
            ps.setBigDecimal(10, factura.getTotal());

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
            AppLogger.error(getClass(), "Error de acceso a datos", e);
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
            AppLogger.error(getClass(), "Error de acceso a datos", e);
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
            AppLogger.error(getClass(), "Error de acceso a datos", e);
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
            AppLogger.error(getClass(), "Error de acceso a datos", e);
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
            AppLogger.error(getClass(), "Error de acceso a datos", e);
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
            AppLogger.error(getClass(), "Error de acceso a datos", e);
        }

        return facturas;
    }

    // ==================== MÉTODOS DE APOYO ====================

    private String baseSelect() {
        // Corrección: se agrega el JOIN con usuario para poder traer
        // u.correo. Sin esto, una Factura leída desde la BD nunca
        // tenía el correo del cliente, así que era imposible
        // reenviarla por correo (solo funcionaba justo al momento de
        // crearla, usando el Usuario que ya estaba en memoria).
        // También se agrega f.nit, que antes no se seleccionaba.
        return "SELECT f.id_factura, f.id_pedido, f.numero_factura, f.fecha, f.nit, "
                + "f.nombre_cliente, f.direccion, f.subtotal, f.descuento, f.iva, f.total, "
                + "p.id_usuario, u.correo AS correo_cliente "
                + "FROM factura f "
                + "JOIN pedido p ON f.id_pedido = p.id_pedido "
                + "JOIN usuario u ON p.id_usuario = u.id_usuario";
    }

    private Factura mapearFactura(ResultSet rs) throws SQLException {

        Pedido pedido = new Pedido();
        pedido.setIdPedido(rs.getInt("id_pedido"));

        // Cliente mínimo: el nombre completo se guarda como texto plano en
        // `nombre_cliente`, se coloca en `nombre` porque Usuario no tiene
        // un único campo de nombre completo editable directamente.
        // Corrección: ahora también se trae el correo (u.correo), que
        // antes se perdía por completo al leer una factura de la BD.
        Usuario cliente = new Usuario();
        cliente.setIdUsuario(rs.getInt("id_usuario"));
        cliente.setNombre(rs.getString("nombre_cliente"));
        cliente.setCorreo(rs.getString("correo_cliente"));

        Factura factura = new Factura();
        factura.setIdFactura(rs.getInt("id_factura"));
        factura.setPedido(pedido);
        factura.setNumeroFactura(rs.getString("numero_factura"));
        factura.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
        factura.setCliente(cliente);
        factura.setDireccion(rs.getString("direccion"));
        factura.setNit(rs.getString("nit"));
        factura.setSubtotal(rs.getBigDecimal("subtotal"));
        factura.setDescuento(rs.getBigDecimal("descuento"));
        factura.setTotal(rs.getBigDecimal("total"));
        // Corrección: antes no se llamaba setIva(), así que toda
        // Factura leída desde la BD (buscarPorId, listar, etc.)
        // quedaba con iva = 0 aunque la columna sí tuviera el valor
        // correcto guardado.
        factura.setIva(rs.getBigDecimal("iva"));

        return factura;
    }

}
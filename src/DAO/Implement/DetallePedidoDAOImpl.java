package DAO.Implement;

import Config.Conexion;
import DAO.Interfaz.IDetallePedidoDAO;
import Model.DetallePedido;
import Model.Pedido;
import Model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * DAO de detalle_pedido. Antes esta clase estaba vacía (no
 * implementaba IDetallePedidoDAO), por lo que PedidoServiceImpl no
 * podía persistir las líneas de un pedido, solo su encabezado.
 * ===============================================================
 */
public class DetallePedidoDAOImpl implements IDetallePedidoDAO {

    @Override
    public boolean insertar(DetallePedido detalle) {

        String sql = "INSERT INTO detalle_pedido (id_pedido,id_producto,cantidad,precio,subtotal) "
                + "VALUES (?,?,?,?,?)";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, detalle.getPedido().getIdPedido());
            ps.setInt(2, detalle.getProducto().getIdProducto());
            ps.setInt(3, detalle.getCantidad());
            ps.setBigDecimal(4, detalle.getPrecio());
            ps.setBigDecimal(5, detalle.getSubtotal());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        detalle.setIdDetalle(rs.getInt(1));
                    }
                }
            }

            return filas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(DetallePedido detalle) {

        String sql = "UPDATE detalle_pedido SET cantidad=?, precio=?, subtotal=? WHERE id_detalle=?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, detalle.getCantidad());
            ps.setBigDecimal(2, detalle.getPrecio());
            ps.setBigDecimal(3, detalle.getSubtotal());
            ps.setInt(4, detalle.getIdDetalle());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {

        String sql = "DELETE FROM detalle_pedido WHERE id_detalle=?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public DetallePedido buscarPorId(int id) {

        String sql = "SELECT * FROM detalle_pedido WHERE id_detalle=?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<DetallePedido> listar() {

        List<DetallePedido> lista = new ArrayList<>();

        String sql = "SELECT * FROM detalle_pedido";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<DetallePedido> listarPorPedido(int idPedido) {

        List<DetallePedido> lista = new ArrayList<>();

        // Se hace JOIN con producto para traer el nombre y precio
        // vigente; las tarjetas de "Tu pedido" en el wizard del
        // carrito necesitan el nombre del producto, no solo su id.
        String sql = "SELECT dp.*, pr.nombre, pr.imagen "
                + "FROM detalle_pedido dp "
                + "JOIN producto pr ON dp.id_producto = pr.id_producto "
                + "WHERE dp.id_pedido = ?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    Pedido pedido = new Pedido();
                    pedido.setIdPedido(idPedido);

                    Producto producto = new Producto();
                    producto.setIdProducto(rs.getInt("id_producto"));
                    producto.setNombre(rs.getString("nombre"));
                    producto.setImagenPrincipal(rs.getString("imagen"));
                    producto.setPrecio(rs.getBigDecimal("precio"));

                    DetallePedido detalle = new DetallePedido(
                            rs.getInt("id_detalle"),
                            pedido,
                            producto,
                            rs.getInt("cantidad"),
                            rs.getBigDecimal("precio"),
                            rs.getBigDecimal("subtotal")
                    );

                    lista.add(detalle);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // ==========================================================
    // APOYO
    // ==========================================================
    private DetallePedido mapear(ResultSet rs) throws SQLException {

        Pedido pedido = new Pedido();
        pedido.setIdPedido(rs.getInt("id_pedido"));

        Producto producto = new Producto();
        producto.setIdProducto(rs.getInt("id_producto"));
        producto.setPrecio(rs.getBigDecimal("precio"));

        return new DetallePedido(
                rs.getInt("id_detalle"),
                pedido,
                producto,
                rs.getInt("cantidad"),
                rs.getBigDecimal("precio"),
                rs.getBigDecimal("subtotal")
        );
    }

}

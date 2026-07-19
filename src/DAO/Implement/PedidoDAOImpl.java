package DAO.Implement;

import Config.Conexion;
import DAO.Interfaz.IPedidoDAO;
import Model.EstadoPedido;
import Model.Pedido;
import Model.TipoEntrega;
import Model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements IPedidoDAO {

    @Override
    public boolean insertar(Pedido pedido) {

        String sql = "INSERT INTO pedido "
                + "(numero_orden,id_usuario,fecha,tipo_entrega,estado,"
                + "subtotal,descuento,total) "
                + "VALUES (?,?,?,?,?,?,?,?)";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pedido.getNumeroOrden());
            ps.setInt(2, pedido.getIdUsuario().getIdUsuario());
            ps.setTimestamp(3, Timestamp.valueOf(pedido.getFecha()));
            ps.setString(4, pedido.getTipoEntrega().name());
            ps.setString(5, pedido.getEstado().name());
            ps.setDouble(6, pedido.getSubtotal());
            ps.setDouble(7, pedido.getDescuento());
            ps.setDouble(8, pedido.getTotal());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        }

    }

    @Override
    public boolean actualizar(Pedido pedido) {

        String sql = "UPDATE pedido SET "
                + "numero_orden=?,"
                + "id_usuario=?,"
                + "fecha=?,"
                + "tipo_entrega=?,"
                + "estado=?,"
                + "subtotal=?,"
                + "descuento=?,"
                + "total=? "
                + "WHERE id_pedido=?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pedido.getNumeroOrden());
            ps.setInt(2, pedido.getIdUsuario().getIdUsuario());
            ps.setTimestamp(3, Timestamp.valueOf(pedido.getFecha()));
            ps.setString(4, pedido.getTipoEntrega().name());
            ps.setString(5, pedido.getEstado().name());
            ps.setDouble(6, pedido.getSubtotal());
            ps.setDouble(7, pedido.getDescuento());
            ps.setDouble(8, pedido.getTotal());
            ps.setInt(9, pedido.getIdPedido());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        }

    }

    @Override
    public boolean eliminar(int idPedido) {

        String sql = "DELETE FROM pedido WHERE id_pedido=?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPedido);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        }

    }

    @Override
    public Pedido buscarPorId(int idPedido) {

        String sql = "SELECT * FROM pedido WHERE id_pedido=?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPedido);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Pedido pedido = new Pedido();

                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));

                pedido.setIdPedido(rs.getInt("id_pedido"));
                pedido.setNumeroOrden(rs.getString("numero_orden"));
                pedido.setIdUsuario(usuario);
                pedido.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                pedido.setTipoEntrega(
                        TipoEntrega.valueOf(rs.getString("tipo_entrega").toUpperCase().replace(" ", "_"))
                );
                pedido.setEstado(
                        EstadoPedido.valueOf(rs.getString("estado").toUpperCase())
                );
                pedido.setSubtotal(rs.getDouble("subtotal"));
                pedido.setDescuento(rs.getDouble("descuento"));
                pedido.setTotal(rs.getDouble("total"));

                return pedido;

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return null;

    }

    @Override
    public List<Pedido> listar() {

        List<Pedido> lista = new ArrayList<>();

        String sql = "SELECT * FROM pedido";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Pedido pedido = new Pedido();

                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));

                pedido.setIdPedido(rs.getInt("id_pedido"));
                pedido.setNumeroOrden(rs.getString("numero_orden"));
                pedido.setIdUsuario(usuario);
                pedido.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                pedido.setTipoEntrega(
                        TipoEntrega.valueOf(rs.getString("tipo_entrega").toUpperCase().replace(" ", "_"))
                );
                pedido.setEstado(
                        EstadoPedido.valueOf(rs.getString("estado").toUpperCase())
                );
                pedido.setSubtotal(rs.getDouble("subtotal"));
                pedido.setDescuento(rs.getDouble("descuento"));
                pedido.setTotal(rs.getDouble("total"));

                lista.add(pedido);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return lista;

    }

}
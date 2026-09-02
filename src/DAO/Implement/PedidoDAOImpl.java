package DAO.Implement;

import Config.Conexion;
import DAO.Interfaz.IPedidoDAO;
import Model.DetallePedido;
import Model.EstadoPedido;
import Model.Pedido;
import Model.TipoEntrega;
import Model.Usuario;
import Utils.AppLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements IPedidoDAO {

    // Corrección: buscarPorId()/listar() armaban el Pedido con
    // `new Pedido()` (detalles = lista vacía interna) y nunca la
    // llenaban, así que TODO Pedido leído de la BD volvía con
    // getDetalles() vacío aunque sí tuviera productos en
    // detalle_pedido. Eso rompía en silencio: "Unidades vendidas" y
    // "Ventas por categoría" del Dashboard, y "Top productos" /
    // "Unidades vendidas" del nuevo módulo de Ventas (siempre en 0 o
    // vacíos), porque ambos leen los pedidos con
    // IPedidoService.listarPedidos() y recorren pedido.getDetalles().
    // Se soluciona cargando los detalles reales con DetallePedidoDAOImpl
    // y pasándolos al Pedido a través del constructor que sí los acepta.
    private final DetallePedidoDAOImpl detallePedidoDAO = new DetallePedidoDAOImpl();

    @Override
    public boolean insertar(Pedido pedido) {

        String sql = "INSERT INTO pedido "
                + "(numero_orden,id_usuario,fecha,tipo_entrega,estado,"
                + "subtotal,descuento,total,costo_envio,direccion_entrega,referencia_entrega) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        // RETURN_GENERATED_KEYS: sin esto, pedido.getIdPedido() se
        // queda en 0 después de insertar, y el Pago/Factura que se
        // guardan justo después (ver PedidoController) no pueden
        // enlazar la FK id_pedido correctamente.
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, pedido.getNumeroOrden());
            ps.setInt(2, pedido.getIdUsuario().getIdUsuario());
            ps.setTimestamp(3, Timestamp.valueOf(pedido.getFecha()));
            ps.setString(4, tipoEntregaToDb(pedido.getTipoEntrega()));
            ps.setString(5, pedido.getEstado().name());
            ps.setBigDecimal(6, pedido.getSubtotal());
            ps.setBigDecimal(7, pedido.getDescuento());
            ps.setBigDecimal(8, pedido.getTotal());
            ps.setBigDecimal(9, pedido.getCostoEnvio());
            ps.setString(10, pedido.getDireccionEntrega());
            ps.setString(11, pedido.getReferenciaEntrega());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        pedido.setIdPedido(rs.getInt(1));
                    }
                }
            }

            return filas > 0;

        } catch (SQLException e) {

            AppLogger.error(PedidoDAOImpl.class,
                    "No se pudo insertar el pedido " + pedido.getNumeroOrden(), e);
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
                + "total=?,"
                + "costo_envio=?,"
                + "direccion_entrega=?,"
                + "referencia_entrega=? "
                + "WHERE id_pedido=?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pedido.getNumeroOrden());
            ps.setInt(2, pedido.getIdUsuario().getIdUsuario());
            ps.setTimestamp(3, Timestamp.valueOf(pedido.getFecha()));
            ps.setString(4, tipoEntregaToDb(pedido.getTipoEntrega()));
            ps.setString(5, pedido.getEstado().name());
            ps.setBigDecimal(6, pedido.getSubtotal());
            ps.setBigDecimal(7, pedido.getDescuento());
            ps.setBigDecimal(8, pedido.getTotal());
            ps.setBigDecimal(9, pedido.getCostoEnvio());
            ps.setString(10, pedido.getDireccionEntrega());
            ps.setString(11, pedido.getReferenciaEntrega());
            ps.setInt(12, pedido.getIdPedido());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            AppLogger.error(PedidoDAOImpl.class,
                    "No se pudo actualizar el pedido #" + pedido.getIdPedido(), e);
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

            AppLogger.error(PedidoDAOImpl.class,
                    "No se pudo eliminar el pedido #" + idPedido, e);
            return false;

        }

    }

    @Override
    public Pedido buscarPorId(int idPedido) {

        // Corrección: antes era "SELECT * FROM pedido" y el Usuario
        // del pedido solo traía el id (setIdUsuario), nunca nombre
        // ni apellido. Como Usuario.getNombreCompleto() concatena
        // nombre+apellido, la columna "Cliente" salía en blanco en
        // TODAS las tablas de pedidos (Trabajador y Administrador).
        // Se agrega JOIN con usuario para traer los datos reales.
        String sql = "SELECT p.*, u.nombre AS u_nombre, u.apellido AS u_apellido, "
                + "u.correo AS u_correo "
                + "FROM pedido p "
                + "JOIN usuario u ON u.id_usuario = p.id_usuario "
                + "WHERE p.id_pedido=?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPedido);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("u_nombre"));
                usuario.setApellido(rs.getString("u_apellido"));
                usuario.setCorreo(rs.getString("u_correo"));

                // Los detalles se cargan ANTES de construir el Pedido:
                // Pedido.detalles es `final`, así que la única forma de
                // que el objeto termine con sus líneas reales es
                // pasárselas al constructor que las acepta (no hay
                // setDetalles()).
                List<DetallePedido> detalles = detallePedidoDAO.listarPorPedido(idPedido);

                Pedido pedido = new Pedido(
                        rs.getInt("id_pedido"),
                        rs.getString("numero_orden"),
                        usuario,
                        rs.getTimestamp("fecha").toLocalDateTime(),
                        TipoEntrega.valueOf(rs.getString("tipo_entrega").toUpperCase().replace(" ", "_")),
                        EstadoPedido.valueOf(rs.getString("estado").toUpperCase()),
                        null,
                        rs.getBigDecimal("subtotal"),
                        rs.getBigDecimal("descuento"),
                        rs.getBigDecimal("total"),
                        detalles
                );

                pedido.setCostoEnvio(rs.getBigDecimal("costo_envio"));
                pedido.setDireccionEntrega(rs.getString("direccion_entrega"));
                pedido.setReferenciaEntrega(rs.getString("referencia_entrega"));

                return pedido;

            }

        } catch (SQLException e) {

            AppLogger.error(PedidoDAOImpl.class,
                    "No se pudo buscar el pedido #" + idPedido, e);

        }

        return null;

    }

    @Override
    public List<Pedido> listar() {

        List<Pedido> lista = new ArrayList<>();

        // Mismo fix que en buscarPorId(): se agrega el JOIN para
        // traer nombre/apellido del cliente y que las listas de
        // pedidos (Trabajador y Administrador) no salgan con la
        // columna "Cliente" vacía.
        String sql = "SELECT p.*, u.nombre AS u_nombre, u.apellido AS u_apellido, "
                + "u.correo AS u_correo "
                + "FROM pedido p "
                + "JOIN usuario u ON u.id_usuario = p.id_usuario";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("u_nombre"));
                usuario.setApellido(rs.getString("u_apellido"));
                usuario.setCorreo(rs.getString("u_correo"));

                int idPedido = rs.getInt("id_pedido");

                List<DetallePedido> detalles = detallePedidoDAO.listarPorPedido(idPedido);

                Pedido pedido = new Pedido(
                        idPedido,
                        rs.getString("numero_orden"),
                        usuario,
                        rs.getTimestamp("fecha").toLocalDateTime(),
                        TipoEntrega.valueOf(rs.getString("tipo_entrega").toUpperCase().replace(" ", "_")),
                        EstadoPedido.valueOf(rs.getString("estado").toUpperCase()),
                        null,
                        rs.getBigDecimal("subtotal"),
                        rs.getBigDecimal("descuento"),
                        rs.getBigDecimal("total"),
                        detalles
                );

                pedido.setCostoEnvio(rs.getBigDecimal("costo_envio"));
                pedido.setDireccionEntrega(rs.getString("direccion_entrega"));
                pedido.setReferenciaEntrega(rs.getString("referencia_entrega"));

                lista.add(pedido);

            }

        } catch (SQLException e) {

            AppLogger.error(PedidoDAOImpl.class,
                    "No se pudo listar los pedidos.", e);

        }

        return lista;

    }
    
    private String tipoEntregaToDb(TipoEntrega tipo) {
    switch (tipo) {
        case COMER_EN_RESTAURANTE:
            return "Comer en restaurante";
        case PARA_LLEVAR:
            return "Para llevar";
        case DOMICILIO:
            // La BD todavía no tiene este valor en el ENUM de tipo_entrega
            // (ver FreddyQuickBite.sql). Si vas a usar entregas a domicilio,
            // hace falta un ALTER TABLE para agregarlo (ver nota abajo).
            return "Domicilio";
        default:
            throw new IllegalArgumentException("Tipo de entrega no reconocido: " + tipo);
    }
}

}
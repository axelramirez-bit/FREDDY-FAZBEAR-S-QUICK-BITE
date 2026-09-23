// Paquete DAO.Implement
package DAO.Implement;

// Importa Conexion
import Config.Conexion;
// Importa IDetallePedidoDAO
import DAO.Interfaz.IDetallePedidoDAO;
// Importa DetallePedido
import Model.DetallePedido;
// Importa Pedido
import Model.Pedido;
// Importa Producto
import Model.Producto;
// Importa AppLogger
import Utils.AppLogger;

// Importa Connection
import java.sql.Connection;
// Importa PreparedStatement
import java.sql.PreparedStatement;
// Importa ResultSet
import java.sql.ResultSet;
// Importa SQLException
import java.sql.SQLException;
// Importa Statement
import java.sql.Statement;
// Importa ArrayList
import java.util.ArrayList;
// Importa List
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
// DAO de detalles de pedido (MySQL)
public class DetallePedidoDAOImpl implements IDetallePedidoDAO {

    @Override
    // Inserta un detalle de pedido
    public boolean insertar(DetallePedido detalle) {

        // SQL: INSERT de 6 columnas
        String sql = "INSERT INTO detalle_pedido (id_pedido,id_producto,cantidad,precio,subtotal,observaciones) "
                + "VALUES (?,?,?,?,?,?)";

        // Abre la conexión y prepara el INSERT pidiendo las llaves
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Id del pedido
            ps.setInt(1, detalle.getPedido().getIdPedido());
            // Id del producto
            ps.setInt(2, detalle.getProducto().getIdProducto());
            // Cantidad
            ps.setInt(3, detalle.getCantidad());
            // Precio
            ps.setBigDecimal(4, detalle.getPrecio());
            // Subtotal
            ps.setBigDecimal(5, detalle.getSubtotal());
            // Observaciones
            ps.setString(6, detalle.getObservaciones());

            // Ejecuta el INSERT. OJO: un trigger descuenta el stock; si no alcanza, el CHECK falla y llega como false sin motivo
            int filas = ps.executeUpdate();

            // Si insertó
            if (filas > 0) {
                // Lee la llave generada
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    // Si hay llave
                    if (rs.next()) {
                        // la asigna al detalle
                        detalle.setIdDetalle(rs.getInt(1));
                    }
                }
            }

            // Devuelve true si insertó
            return filas > 0;

        // Si falla el SQL
        } catch (SQLException e) {
            // Registra el error
            AppLogger.error(DetallePedidoDAOImpl.class, "No se pudo insertar el detalle del pedido", e);
            // Devuelve false
            return false;
        }
    }

    @Override
    // Actualiza un detalle
    public boolean actualizar(DetallePedido detalle) {

        // SQL: UPDATE del detalle
        String sql = "UPDATE detalle_pedido SET cantidad=?, precio=?, subtotal=?, observaciones=? WHERE id_detalle=?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Cantidad
            ps.setInt(1, detalle.getCantidad());
            // Precio
            ps.setBigDecimal(2, detalle.getPrecio());
            // Subtotal
            ps.setBigDecimal(3, detalle.getSubtotal());
            // Observaciones
            ps.setString(4, detalle.getObservaciones());
            // Id del detalle
            ps.setInt(5, detalle.getIdDetalle());

            // Devuelve true si actualizó
            return ps.executeUpdate() > 0;

        // Si falla el SQL
        } catch (SQLException e) {
            // Registra el error
            AppLogger.error(DetallePedidoDAOImpl.class, "No se pudo actualizar el detalle del pedido", e);
            // Devuelve false
            return false;
        }
    }

    @Override
    // Elimina un detalle por id
    public boolean eliminar(int id) {

        // SQL: DELETE por id
        String sql = "DELETE FROM detalle_pedido WHERE id_detalle=?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Asigna el id
            ps.setInt(1, id);

            // Devuelve true si eliminó
            return ps.executeUpdate() > 0;

        // Si falla el SQL
        } catch (SQLException e) {
            // Registra el error
            AppLogger.error(DetallePedidoDAOImpl.class, "No se pudo eliminar el detalle del pedido", e);
            // Devuelve false
            return false;
        }
    }

    @Override
    // Busca un detalle por id
    public DetallePedido buscarPorId(int id) {

        // SQL: SELECT por id
        String sql = "SELECT * FROM detalle_pedido WHERE id_detalle=?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Asigna el id
            ps.setInt(1, id);

            // Ejecuta la consulta
            try (ResultSet rs = ps.executeQuery()) {
                // Si hay fila
                if (rs.next()) {
                    // La convierte en DetallePedido
                    return mapear(rs);
                }
            }

        // Si falla el SQL
        } catch (SQLException e) {
            // Registra el error
            AppLogger.error(DetallePedidoDAOImpl.class, "No se pudo obtener el detalle del pedido por id", e);
        }

        // Devuelve null si no lo encontró
        return null;
    }

    @Override
    // Lista todos los detalles
    public List<DetallePedido> listar() {

        // Lista de resultados
        List<DetallePedido> lista = new ArrayList<>();

        // SQL: SELECT de todos
        String sql = "SELECT * FROM detalle_pedido";

        // Abre la conexión, prepara y ejecuta
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Recorre cada fila
            while (rs.next()) {
                // Convierte y agrega a la lista
                lista.add(mapear(rs));
            }

        // Si falla el SQL
        } catch (SQLException e) {
            // Registra el error
            AppLogger.error(DetallePedidoDAOImpl.class, "No se pudo listar los detalles de pedido", e);
        }

        // Devuelve la lista (vacía si hubo error)
        return lista;
    }

    @Override
    // Lista los detalles de un pedido
    public List<DetallePedido> listarPorPedido(int idPedido) {

        // Lista de resultados
        List<DetallePedido> lista = new ArrayList<>();

        // Se hace JOIN con producto para traer el nombre y precio
        // vigente; las tarjetas de "Tu pedido" en el wizard del
        // carrito necesitan el nombre del producto, no solo su id.
        // SQL: detalles unidos con el producto (nombre e imagen)
        String sql = "SELECT dp.*, pr.nombre, pr.imagen "
                + "FROM detalle_pedido dp "
                + "JOIN producto pr ON dp.id_producto = pr.id_producto "
                + "WHERE dp.id_pedido = ?";

        // Abre la conexión y prepara la consulta
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Asigna el id del pedido
            ps.setInt(1, idPedido);

            // Ejecuta la consulta
            try (ResultSet rs = ps.executeQuery()) {
                // Recorre cada fila
                while (rs.next()) {

                    // Crea un pedido solo con el id
                    Pedido pedido = new Pedido();
                    // Asigna el id
                    pedido.setIdPedido(idPedido);

                    // Crea el producto con datos básicos
                    Producto producto = new Producto();
                    // Id del producto
                    producto.setIdProducto(rs.getInt("id_producto"));
                    // Nombre
                    producto.setNombre(rs.getString("nombre"));
                    // Imagen
                    producto.setImagenPrincipal(rs.getString("imagen"));
                    // Precio
                    producto.setPrecio(rs.getBigDecimal("precio"));

                    // Crea el detalle
                    DetallePedido detalle = new DetallePedido(
                            // Id del detalle
                            rs.getInt("id_detalle"),
                            // Pedido
                            pedido,
                            // Producto
                            producto,
                            // Cantidad
                            rs.getInt("cantidad"),
                            // Precio
                            rs.getBigDecimal("precio"),
                            // Subtotal
                            rs.getBigDecimal("subtotal"),
                            // Observaciones
                            rs.getString("observaciones")
                    );

                    // Agrega el detalle a la lista
                    lista.add(detalle);
                }
            }

        // Si falla el SQL
        } catch (SQLException e) {
            // Registra el error
            AppLogger.error(DetallePedidoDAOImpl.class, "No se pudo listar los detalles del pedido", e);
        }

        // Devuelve la lista (vacía si hubo error)
        return lista;
    }

    // ==========================================================
    // APOYO
    // ==========================================================
    // Convierte una fila en DetallePedido
    private DetallePedido mapear(ResultSet rs) throws SQLException {

        // Crea un pedido solo con el id
        Pedido pedido = new Pedido();
        // Asigna el id
        pedido.setIdPedido(rs.getInt("id_pedido"));

        // Crea un producto solo con el id
        Producto producto = new Producto();
        // Asigna el id
        producto.setIdProducto(rs.getInt("id_producto"));
        // Asigna el precio. OJO: no carga nombre ni imagen del producto
        producto.setPrecio(rs.getBigDecimal("precio"));

        // Crea el detalle con los datos de la fila
        return new DetallePedido(
                rs.getInt("id_detalle"),
                pedido,
                producto,
                rs.getInt("cantidad"),
                rs.getBigDecimal("precio"),
                rs.getBigDecimal("subtotal"),
                rs.getString("observaciones")
        );
    }

}
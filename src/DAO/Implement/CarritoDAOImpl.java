
package DAO.Implement;
 
import Config.Conexion;
import DAO.Interfaz.ICarritoDAO;
import Model.Carrito;
import Model.CarritoDetalle;
import Model.Categoria;
import Model.EstadoCarrito;
import Model.Producto;
import Model.Usuario;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
 
public class CarritoDAOImpl implements ICarritoDAO {
 
    @Override
    public boolean crear(Carrito carrito) {
 
        String sql = "INSERT INTO carrito(id_usuario, fecha_creacion, estado) VALUES (?, ?, ?)";
 
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
 
            ps.setInt(1, carrito.getUsuario().getIdUsuario());
            ps.setTimestamp(2, Timestamp.valueOf(carrito.getFechaCreacion()));
            ps.setString(3, estadoToDb(carrito.getEstado()));
 
            return ps.executeUpdate() > 0;
 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
 
    }
 
    @Override
    public Carrito buscarPorId(int idCarrito) {
 
        String sql = "SELECT id_carrito, id_usuario, fecha_creacion, estado "
                + "FROM carrito WHERE id_carrito = ?";
 
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
 
            ps.setInt(1, idCarrito);
 
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCarrito(rs);
                }
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
 
        return null;
    }
 
    @Override
    public Carrito buscarPorUsuario(int idUsuario) {
 
        // Se asume que un usuario tiene, a lo sumo, un carrito Activo a la vez.
        String sql = "SELECT id_carrito, id_usuario, fecha_creacion, estado "
                + "FROM carrito WHERE id_usuario = ? AND estado = 'Activo' "
                + "ORDER BY fecha_creacion DESC LIMIT 1";
 
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
 
            ps.setInt(1, idUsuario);
 
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCarrito(rs);
                }
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
 
        return null;
    }
 
    @Override
    public boolean actualizarEstado(int idCarrito, EstadoCarrito estado) {
 
        String sql = "UPDATE carrito SET estado = ? WHERE id_carrito = ?";
 
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
 
            ps.setString(1, estadoToDb(estado));
            ps.setInt(2, idCarrito);
 
            return ps.executeUpdate() > 0;
 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    @Override
    public boolean vaciarCarrito(int idCarrito) {
 
        String sql = "DELETE FROM carrito_detalle WHERE id_carrito = ?";
 
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
 
            ps.setInt(1, idCarrito);
            ps.executeUpdate();
 
            // Aunque no existan filas que borrar (carrito ya vacío),
            // se considera una operación exitosa.
            return true;
 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    @Override
    public List<CarritoDetalle> obtenerDetalles(int idCarrito) {
 
        List<CarritoDetalle> detalles = new ArrayList<>();
 
        String sql
                = "SELECT cd.id_carrito_detalle, cd.id_carrito, cd.cantidad, cd.observaciones, "
                + "p.id_producto, p.id_categoria, p.nombre AS producto_nombre, "
                + "p.descripcion AS producto_descripcion, p.precio, p.stock, p.disponible, "
                + "p.imagen, p.estado AS producto_estado, "
                + "c.nombre AS categoria_nombre, c.descripcion AS categoria_descripcion, "
                + "c.icono, c.imagen AS categoria_imagen, c.estado AS categoria_estado "
                + "FROM carrito_detalle cd "
                + "JOIN producto p ON cd.id_producto = p.id_producto "
                + "JOIN categoria c ON p.id_categoria = c.id_categoria "
                + "WHERE cd.id_carrito = ?";
 
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
 
            ps.setInt(1, idCarrito);
 
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    detalles.add(mapearDetalle(rs, idCarrito));
                }
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
 
        return detalles;
    }
 
    // ==================== MÉTODOS DE APOYO ====================
 
    private Carrito mapearCarrito(ResultSet rs) throws SQLException {
 
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("id_usuario"));
 
        Carrito carrito = new Carrito();
        carrito.setIdCarrito(rs.getInt("id_carrito"));
        carrito.setUsuario(usuario);
        carrito.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        carrito.setEstado(estadoDesdeDb(rs.getString("estado")));
 
        return carrito;
    }
 
    private CarritoDetalle mapearDetalle(ResultSet rs, int idCarrito) throws SQLException {
 
        // NOTA: la tabla `categoria` no tiene columnas `color` ni `orden`,
        // por eso no se mapean aquí (quedan con su valor por defecto).
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(rs.getInt("id_categoria"));
        categoria.setNombre(rs.getString("categoria_nombre"));
        categoria.setDescripcion(rs.getString("categoria_descripcion"));
        categoria.setIcono(rs.getString("icono"));
        categoria.setImagen(rs.getString("categoria_imagen"));
        categoria.setEstado(rs.getBoolean("categoria_estado"));
 
        // NOTA: la tabla `producto` no tiene precioAnterior, calorias,
        // tiempoPreparacion, destacado, imagenBanner ni fecha_actualizacion.
        Producto producto = new Producto();
        producto.setIdProducto(rs.getInt("id_producto"));
        producto.setCategoria(categoria);
        producto.setNombre(rs.getString("producto_nombre"));
        producto.setDescripcion(rs.getString("producto_descripcion"));
        producto.setPrecio(rs.getDouble("precio"));
        producto.setStock(rs.getInt("stock"));
        producto.setDisponible(rs.getBoolean("disponible"));
        producto.setImagenPrincipal(rs.getString("imagen"));
        producto.setEstado(rs.getBoolean("producto_estado"));
 
        Carrito carritoRef = new Carrito();
        carritoRef.setIdCarrito(idCarrito);
 
        CarritoDetalle detalle = new CarritoDetalle();
        detalle.setIdCarritoDetalle(rs.getInt("id_carrito_detalle"));
        detalle.setCarrito(carritoRef);
        detalle.setProducto(producto);
        detalle.setCantidad(rs.getInt("cantidad"));
        detalle.setObservaciones(rs.getString("observaciones"));
 
        return detalle;
    }
 
    private String estadoToDb(EstadoCarrito estado) {
        switch (estado) {
            case ACTIVO:
                return "Activo";
            case FINALIZADO:
                return "Finalizado";
            case CANCELADO:
                return "Cancelado";
            default:
                throw new IllegalArgumentException("Estado no reconocido: " + estado);
        }
    }
 
    private EstadoCarrito estadoDesdeDb(String estado) {
        switch (estado) {
            case "Activo":
                return EstadoCarrito.ACTIVO;
            case "Finalizado":
                return EstadoCarrito.FINALIZADO;
            case "Cancelado":
                return EstadoCarrito.CANCELADO;
            default:
                throw new IllegalArgumentException("Estado no reconocido: " + estado);
        }
    }
 
}

package DAO.Implement;
 
import Config.Conexion;
import DAO.Interfaz.ICarritoDetalleDAO;
import Model.Carrito;
import Model.CarritoDetalle;
import Model.Categoria;
import Model.Producto;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
 
public class CarritoDetalleDAOImpl implements ICarritoDetalleDAO {
 
    @Override
    public boolean agregarProducto(CarritoDetalle detalle) {
 
        String sql = "INSERT INTO carrito_detalle(id_carrito, id_producto, cantidad, observaciones) "
                + "VALUES (?, ?, ?, ?)";
 
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
 
            ps.setInt(1, detalle.getCarrito().getIdCarrito());
            ps.setInt(2, detalle.getProducto().getIdProducto());
            ps.setInt(3, detalle.getCantidad());
            ps.setString(4, detalle.getObservaciones());
 
            boolean creado = ps.executeUpdate() > 0;
 
            if (creado) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        detalle.setIdCarritoDetalle(rs.getInt(1));
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
    public boolean actualizarCantidad(int idDetalle, int cantidad) {
 
        String sql = "UPDATE carrito_detalle SET cantidad = ? WHERE id_carrito_detalle = ?";
 
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
 
            ps.setInt(1, cantidad);
            ps.setInt(2, idDetalle);
 
            return ps.executeUpdate() > 0;
 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    @Override
    public boolean eliminarProducto(int idDetalle) {
 
        String sql = "DELETE FROM carrito_detalle WHERE id_carrito_detalle = ?";
 
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
 
            ps.setInt(1, idDetalle);
 
            return ps.executeUpdate() > 0;
 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    @Override
    public List<CarritoDetalle> listarPorCarrito(int idCarrito) {
 
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
                    detalles.add(mapearDetalle(rs));
                }
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
 
        return detalles;
    }
 
    private CarritoDetalle mapearDetalle(ResultSet rs) throws SQLException {
 
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(rs.getInt("id_categoria"));
        categoria.setNombre(rs.getString("categoria_nombre"));
        categoria.setDescripcion(rs.getString("categoria_descripcion"));
        categoria.setIcono(rs.getString("icono"));
        categoria.setImagen(rs.getString("categoria_imagen"));
        categoria.setEstado(rs.getBoolean("categoria_estado"));
 
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
 
        Carrito carrito = new Carrito();
        carrito.setIdCarrito(rs.getInt("id_carrito"));
 
        CarritoDetalle detalle = new CarritoDetalle();
        detalle.setIdCarritoDetalle(rs.getInt("id_carrito_detalle"));
        detalle.setCarrito(carrito);
        detalle.setProducto(producto);
        detalle.setCantidad(rs.getInt("cantidad"));
        detalle.setObservaciones(rs.getString("observaciones"));
 
        return detalle;
    }
 
}
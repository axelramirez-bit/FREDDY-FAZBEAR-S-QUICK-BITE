package DAO.Implement;

import Config.Conexion;
import DAO.Interfaz.IProductoDAO;
import Model.Categoria;
import Model.Producto;
import Model.Promocion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements IProductoDAO {

    @Override
    public boolean insertar(Producto producto) {

        String sql = "INSERT INTO producto "
                + "(id_categoria,id_promocion,nombre,descripcion,"
                + "precio,stock,disponible,estado) "
                + "VALUES (?,?,?,?,?,?,?,?)";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, producto.getCategoria().getIdCategoria());

            if (producto.getPromocion() == null) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, producto.getPromocion().getIdPromocion());
            }

            ps.setString(3, producto.getNombre());
            ps.setString(4, producto.getDescripcion());
            ps.setDouble(5, producto.getPrecio());
            ps.setInt(6, producto.getStock());
            ps.setBoolean(7, producto.isDisponible());
            ps.setBoolean(8, producto.isEstado());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        }

    }

    @Override
    public boolean actualizar(Producto producto) {

        String sql = "UPDATE producto SET "
                + "id_categoria=?,"
                + "id_promocion=?,"
                + "nombre=?,"
                + "descripcion=?,"
                + "precio=?,"
                + "stock=?,"
                + "disponible=?,"
                + "estado=? "
                + "WHERE id_producto=?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, producto.getCategoria().getIdCategoria());

            if (producto.getPromocion() == null) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, producto.getPromocion().getIdPromocion());
            }

            ps.setString(3, producto.getNombre());
            ps.setString(4, producto.getDescripcion());
            ps.setDouble(5, producto.getPrecio());
            ps.setInt(6, producto.getStock());
            ps.setBoolean(7, producto.isDisponible());
            ps.setBoolean(8, producto.isEstado());
            ps.setInt(9, producto.getIdProducto());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        }

    }

    @Override
    public boolean eliminar(int idProducto) {

        String sql = "DELETE FROM producto WHERE id_producto=?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        }

    }

    @Override
    public Producto buscarPorId(int idProducto) {

        String sql = "SELECT * FROM producto WHERE id_producto=?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Producto producto = new Producto();

                Categoria categoria = new Categoria();
                categoria.setIdCategoria(rs.getInt("id_categoria"));

                producto.setCategoria(categoria);

                if (rs.getObject("id_promocion") != null) {

                    Promocion promocion = new Promocion();
                    promocion.setIdPromocion(rs.getInt("id_promocion"));
                    producto.setPromocion(promocion);

                }

                producto.setIdProducto(rs.getInt("id_producto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setStock(rs.getInt("stock"));
                producto.setDisponible(rs.getBoolean("disponible"));
                producto.setEstado(rs.getBoolean("estado"));

                return producto;

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return null;

    }

    @Override
    public List<Producto> listar() {

        List<Producto> lista = new ArrayList<>();

        String sql = "SELECT * FROM producto";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Producto producto = new Producto();

                Categoria categoria = new Categoria();
                categoria.setIdCategoria(rs.getInt("id_categoria"));

                producto.setCategoria(categoria);

                if (rs.getObject("id_promocion") != null) {

                    Promocion promocion = new Promocion();
                    promocion.setIdPromocion(rs.getInt("id_promocion"));
                    producto.setPromocion(promocion);

                }

                producto.setIdProducto(rs.getInt("id_producto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setStock(rs.getInt("stock"));
                producto.setDisponible(rs.getBoolean("disponible"));
                producto.setEstado(rs.getBoolean("estado"));

                lista.add(producto);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return lista;

    }

}
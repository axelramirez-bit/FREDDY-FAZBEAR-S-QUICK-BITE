package DAO.Implement;

import Config.Conexion;
import DAO.Interfaz.IProductoCategoriaDAO;
import Model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoCategoriaDAOImpl implements IProductoCategoriaDAO {

    @Override
    public boolean asignarCategoria(int idProducto, int idCategoria) {

        // INSERT IGNORE: si ya existía esa combinación (misma PK
        // compuesta id_producto + id_categoria), no falla ni duplica.
        String sql = "INSERT IGNORE INTO producto_categoria (id_producto, id_categoria) VALUES (?, ?)";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            ps.setInt(2, idCategoria);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        }

    }

    @Override
    public boolean quitarCategoria(int idProducto, int idCategoria) {

        String sql = "DELETE FROM producto_categoria WHERE id_producto = ? AND id_categoria = ?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            ps.setInt(2, idCategoria);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        }

    }

    @Override
    public List<Categoria> listarCategoriasPorProducto(int idProducto) {

        List<Categoria> lista = new ArrayList<>();

        String sql = "SELECT c.* FROM producto_categoria pc "
                + "JOIN categoria c ON c.id_categoria = pc.id_categoria "
                + "WHERE pc.id_producto = ?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(mapearCategoria(rs));
                }

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return lista;

    }

    @Override
    public Map<Integer, List<Categoria>> listarCategoriasPorTodosLosProductos() {

        Map<Integer, List<Categoria>> mapa = new HashMap<>();

        String sql = "SELECT pc.id_producto, c.* FROM producto_categoria pc "
                + "JOIN categoria c ON c.id_categoria = pc.id_categoria";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                int idProducto = rs.getInt("id_producto");

                mapa.computeIfAbsent(idProducto, k -> new ArrayList<>())
                        .add(mapearCategoria(rs));

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return mapa;

    }

    private Categoria mapearCategoria(ResultSet rs) throws SQLException {

        Categoria categoria = new Categoria();
        categoria.setIdCategoria(rs.getInt("id_categoria"));
        categoria.setNombre(rs.getString("nombre"));
        categoria.setDescripcion(rs.getString("descripcion"));
        categoria.setIcono(rs.getString("icono"));
        categoria.setImagen(rs.getString("imagen"));
        categoria.setEstado(rs.getBoolean("estado"));

        return categoria;

    }

}

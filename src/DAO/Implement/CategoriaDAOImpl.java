package DAO.Implement;
 
import Config.Conexion;
import DAO.Interfaz.ICategoriaDAO;
import Model.Categoria;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
 
public class CategoriaDAOImpl implements ICategoriaDAO {
 
    @Override
    public boolean guardar(Categoria categoria) {
 
        String sql
                = "INSERT INTO categoria(nombre,descripcion,icono,imagen,estado) VALUES (?,?,?,?,?)";
 
        try (Connection con = Conexion.getInstancia().getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
 
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.setString(3, categoria.getIcono());
            ps.setString(4, categoria.getImagen());
            ps.setBoolean(5, categoria.isEstado());
 
            return ps.executeUpdate() > 0;
 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
 
    }
 
    @Override
    public boolean actualizar(Categoria categoria) {
 
        String sql = "UPDATE categoria SET nombre = ?, descripcion = ?, icono = ?, "
                + "imagen = ?, estado = ? WHERE id_categoria = ?";
 
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
 
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.setString(3, categoria.getIcono());
            ps.setString(4, categoria.getImagen());
            ps.setBoolean(5, categoria.isEstado());
            ps.setInt(6, categoria.getIdCategoria());
 
            return ps.executeUpdate() > 0;
 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    @Override
    public boolean cambiarEstado(int idCategoria, boolean estado) {
 
        String sql = "UPDATE categoria SET estado = ? WHERE id_categoria = ?";
 
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
 
            ps.setBoolean(1, estado);
            ps.setInt(2, idCategoria);
 
            return ps.executeUpdate() > 0;
 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    @Override
    public Categoria buscarPorId(int idCategoria) {
 
        String sql = "SELECT id_categoria, nombre, descripcion, icono, imagen, estado "
                + "FROM categoria WHERE id_categoria = ?";
 
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
 
            ps.setInt(1, idCategoria);
 
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCategoria(rs);
                }
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
 
        return null;
    }
 
    @Override
    public List<Categoria> listar() {
 
        List<Categoria> categorias = new ArrayList<>();
 
        String sql = "SELECT id_categoria, nombre, descripcion, icono, imagen, estado FROM categoria";
 
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
 
            while (rs.next()) {
                categorias.add(mapearCategoria(rs));
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
 
        return categorias;
    }
 
    @Override
    public List<Categoria> listarActivas() {
 
        List<Categoria> categorias = new ArrayList<>();
 
        String sql = "SELECT id_categoria, nombre, descripcion, icono, imagen, estado "
                + "FROM categoria WHERE estado = TRUE";
 
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
 
            while (rs.next()) {
                categorias.add(mapearCategoria(rs));
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
 
        return categorias;
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

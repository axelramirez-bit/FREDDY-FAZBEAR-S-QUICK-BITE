package DAO.Implement;

import Config.Conexion;
import DAO.Interfaz.IUsuarioDAO;
import Model.Rol;
import Model.Usuario;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements IUsuarioDAO {

    @Override
    public boolean insertar(Usuario usuario) {

        String sql = "INSERT INTO usuario "
                + "(id_rol,nombre,apellido,correo,telefono,password,"
                + "fecha_nacimiento,estado)"
                + "VALUES(?,?,?,?,?,?,?,?)";

        try (Connection con = Conexion.getInstancia().getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuario.getRol().getIdRol());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getApellido());
            ps.setString(4, usuario.getCorreo());
            ps.setString(5, usuario.getTelefono());
            ps.setString(6, usuario.getPassword());
            ps.setDate(7, Date.valueOf(usuario.getFechaNacimiento()));
            ps.setBoolean(8, usuario.isEstado());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        }

    }

    @Override
    public boolean actualizar(Usuario usuario) {

        String sql = "UPDATE usuario SET "
                + "id_rol=?,"
                + "nombre=?,"
                + "apellido=?,"
                + "correo=?,"
                + "telefono=?,"
                + "password=?,"
                + "fecha_nacimiento=?,"
                + "estado=? "
                + "WHERE id_usuario=?";

        try (Connection con = Conexion.getInstancia().getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuario.getRol().getIdRol());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getApellido());
            ps.setString(4, usuario.getCorreo());
            ps.setString(5, usuario.getTelefono());
            ps.setString(6, usuario.getPassword());
            ps.setDate(7, Date.valueOf(usuario.getFechaNacimiento()));
            ps.setBoolean(8, usuario.isEstado());
            ps.setInt(9, usuario.getIdUsuario());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        }

    }

    @Override
    public boolean eliminar(int idUsuario) {

        String sql = "DELETE FROM usuario WHERE id_usuario=?";

        try (Connection con = Conexion.getInstancia().getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        }

    }

    @Override
    public Usuario buscarPorId(int idUsuario) {

        String sql = "SELECT * FROM usuario WHERE id_usuario=?";

        try (Connection con = Conexion.getInstancia().getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Usuario usuario = new Usuario();

                Rol rol = new Rol();
                rol.setIdRol(rs.getInt("id_rol"));

                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setRol(rol);
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setPassword(rs.getString("password"));
                usuario.setFechaNacimiento(
                        rs.getDate("fecha_nacimiento").toLocalDate());
                usuario.setEstado(rs.getBoolean("estado"));

                return usuario;

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return null;

    }

    @Override
    public List<Usuario> listar() {

        List<Usuario> lista = new ArrayList<>();

        String sql = "SELECT * FROM usuario";

        try (Connection con = Conexion.getInstancia().getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Usuario usuario = new Usuario();

                Rol rol = new Rol();
                rol.setIdRol(rs.getInt("id_rol"));

                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setRol(rol);
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setPassword(rs.getString("password"));
                usuario.setFechaNacimiento(
                        rs.getDate("fecha_nacimiento").toLocalDate());
                usuario.setEstado(rs.getBoolean("estado"));

                lista.add(usuario);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return lista;

    }

    @Override
    public Usuario iniciarSesion(String correo, String password) {

        String sql = "SELECT * FROM usuario "
                + "WHERE correo=? AND password=?";

        try (Connection con = Conexion.getInstancia().getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Usuario usuario = new Usuario();

                Rol rol = new Rol();
                rol.setIdRol(rs.getInt("id_rol"));

                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setRol(rol);
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setPassword(rs.getString("password"));
                usuario.setFechaNacimiento(
                        rs.getDate("fecha_nacimiento").toLocalDate());
                usuario.setEstado(rs.getBoolean("estado"));

                return usuario;

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return null;

    }

}

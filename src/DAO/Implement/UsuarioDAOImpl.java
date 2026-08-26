package DAO.Implement;

import Config.Conexion;
import Utils.AppLogger;
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
                + "(id_rol, nombre, apellido, correo, telefono, turno, password, "
                + "fecha_nacimiento, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuario.getRol().getIdRol());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getApellido());
            ps.setString(4, usuario.getCorreo());
            ps.setString(5, usuario.getTelefono());
            ps.setString(6, usuario.getTurno()); // null para Cliente/Administrador, válido para Trabajador
            ps.setString(7, usuario.getPassword()); // Ya viene encriptado con BCrypt desde el Service
            ps.setDate(8, Date.valueOf(usuario.getFechaNacimiento()));
            ps.setBoolean(9, usuario.isEstado());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.error(getClass(), "Error de acceso a datos", e);
            return false;
        }
    }

    @Override
    public boolean actualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET "
                + "id_rol=?, nombre=?, apellido=?, correo=?, telefono=?, turno=?, "
                + "password=?, fecha_nacimiento=?, estado=? "
                + "WHERE id_usuario=?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuario.getRol().getIdRol());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getApellido());
            ps.setString(4, usuario.getCorreo());
            ps.setString(5, usuario.getTelefono());
            ps.setString(6, usuario.getTurno());
            ps.setString(7, usuario.getPassword());
            ps.setDate(8, Date.valueOf(usuario.getFechaNacimiento()));
            ps.setBoolean(9, usuario.isEstado());
            ps.setInt(10, usuario.getIdUsuario());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.error(getClass(), "Error de acceso a datos", e);
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
            AppLogger.error(getClass(), "Error de acceso a datos", e);
            return false;
        }
    }

    @Override
    public Usuario buscarPorId(int idUsuario) {
        // JOIN para traer el nombre del rol también
        String sql = "SELECT u.*, r.nombre AS nombre_rol "
                + "FROM usuario u "
                + "JOIN rol r ON r.id_rol = u.id_rol "
                + "WHERE u.id_usuario=?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearUsuario(rs);
            }

        } catch (SQLException e) {
            AppLogger.error(getClass(), "Error de acceso a datos", e);
        }
        return null;
    }

    @Override
    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        // JOIN para traer el nombre del rol en la lista también
        String sql = "SELECT u.*, r.nombre AS nombre_rol "
                + "FROM usuario u "
                + "JOIN rol r ON r.id_rol = u.id_rol";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }

        } catch (SQLException e) {
            AppLogger.error(getClass(), "Error de acceso a datos", e);
        }
        return lista;
    }

    // Este método ya no se usa porque el Service verifica BCrypt en Java,
    // pero lo dejamos corregido por si acaso.
    @Override
    public Usuario iniciarSesion(String correo, String password) {
        // Nota: Esto solo funcionaría si la contraseña NO estuviera encriptada.
        // Como usas BCrypt, el Service usa buscarPorCorreo + Encriptador.verificarPassword.
        return null; 
    }
    
    @Override
    public Usuario buscarPorCorreo(String correo) {
        // JOIN con rol: además del id_rol, se necesita el NOMBRE del
        // rol (Administrador/Trabajador/Cliente) para saber a cuál
        // Dashboard redirigir después del login.
        String sql = "SELECT u.*, r.nombre AS nombre_rol "
                + "FROM usuario u "
                + "JOIN rol r ON r.id_rol = u.id_rol "
                + "WHERE u.correo=? AND u.estado = TRUE";

        try (Connection con = Conexion.getInstancia().getConexion(); 
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }

        } catch (SQLException e) {
            AppLogger.error(getClass(), "Error de acceso a datos", e);
        }
        return null;
    }

    // ==========================================
    // MÉTODO AUXILIAR PARA MAPEAR (Evita repetir código)
    // ==========================================
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();

        Rol rol = new Rol();
        rol.setIdRol(rs.getInt("id_rol"));
        rol.setNombre(rs.getString("nombre_rol")); // Trae "Cliente", "Administrador", etc.

        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setRol(rol);
        usuario.setNombre(rs.getString("nombre"));
        usuario.setApellido(rs.getString("apellido"));
        usuario.setCorreo(rs.getString("correo"));
        usuario.setTelefono(rs.getString("telefono"));
        usuario.setTurno(rs.getString("turno")); // viene null si no aplica (Cliente/Administrador)
        usuario.setPassword(rs.getString("password")); // Trae el hash BCrypt
        
        // Corrección: Traer fecha y estado que faltaban en tu código original
        if (rs.getDate("fecha_nacimiento") != null) {
            usuario.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
        }
        usuario.setEstado(rs.getBoolean("estado"));

        return usuario;
    }
}
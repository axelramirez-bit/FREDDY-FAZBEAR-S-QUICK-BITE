package DAO.Implement;

import Config.Conexion;
import DAO.Interfaz.IPromocionDAO;
import Model.Promocion;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PromocionDAOImpl implements IPromocionDAO {

    @Override
    public boolean insertar(Promocion promocion) {

        String sql = "INSERT INTO promocion "
                + "(nombre, descripcion, descuento, fecha_inicio, fecha_fin, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, promocion.getNombre());
            ps.setString(2, promocion.getDescripcion());
            ps.setDouble(3, promocion.getDescuento());
            ps.setDate(4, Date.valueOf(promocion.getFechaInicio()));
            ps.setDate(5, Date.valueOf(promocion.getFechaFin()));
            ps.setBoolean(6, promocion.isEstado());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        }

    }

    @Override
    public boolean actualizar(Promocion promocion) {

        String sql = "UPDATE promocion SET "
                + "nombre=?, "
                + "descripcion=?, "
                + "descuento=?, "
                + "fecha_inicio=?, "
                + "fecha_fin=?, "
                + "estado=? "
                + "WHERE id_promocion=?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, promocion.getNombre());
            ps.setString(2, promocion.getDescripcion());
            ps.setDouble(3, promocion.getDescuento());
            ps.setDate(4, Date.valueOf(promocion.getFechaInicio()));
            ps.setDate(5, Date.valueOf(promocion.getFechaFin()));
            ps.setBoolean(6, promocion.isEstado());
            ps.setInt(7, promocion.getIdPromocion());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        }

    }

    @Override
    public boolean eliminar(int idPromocion) {

        String sql = "DELETE FROM promocion WHERE id_promocion=?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPromocion);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        }

    }

    @Override
    public Promocion buscarPorId(int idPromocion) {

        String sql = "SELECT * FROM promocion WHERE id_promocion=?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPromocion);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Promocion promocion = new Promocion();

                promocion.setIdPromocion(rs.getInt("id_promocion"));
                promocion.setNombre(rs.getString("nombre"));
                promocion.setDescripcion(rs.getString("descripcion"));
                promocion.setDescuento(rs.getDouble("descuento"));
                promocion.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
                promocion.setFechaFin(rs.getDate("fecha_fin").toLocalDate());
                promocion.setEstado(rs.getBoolean("estado"));

                return promocion;

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return null;

    }

    @Override
    public List<Promocion> listar() {

        List<Promocion> lista = new ArrayList<>();

        String sql = "SELECT * FROM promocion";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Promocion promocion = new Promocion();

                promocion.setIdPromocion(rs.getInt("id_promocion"));
                promocion.setNombre(rs.getString("nombre"));
                promocion.setDescripcion(rs.getString("descripcion"));
                promocion.setDescuento(rs.getDouble("descuento"));
                promocion.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
                promocion.setFechaFin(rs.getDate("fecha_fin").toLocalDate());
                promocion.setEstado(rs.getBoolean("estado"));

                lista.add(promocion);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return lista;

    }

}

package DAO.Implement;

import Config.Conexion;
import DAO.Interfaz.ICarritoDAO;
import Model.Carrito;
import Model.CarritoDetalle;
import Model.EstadoCarrito;
import java.sql.Timestamp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


public class CarritoDAOImpl implements ICarritoDAO {

    @Override
public boolean crear(Carrito carrito) {

    String sql = "INSERT INTO carrito(id_usuario, fecha_creacion, estado) VALUES (?, ?, ?)";

    try (Connection con = Conexion.getInstancia().getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, carrito.getUsuario().getIdUsuario());
        ps.setTimestamp(2, Timestamp.valueOf(carrito.getFechaCreacion()));
        ps.setString(3, carrito.getEstado().name());

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }


}

    @Override
    public Carrito buscarPorId(int idCarrito) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Carrito buscarPorUsuario(int idUsuario) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean actualizarEstado(int idCarrito, EstadoCarrito estado) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean vaciarCarrito(int idCarrito) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CarritoDetalle> obtenerDetalles(int idCarrito) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}

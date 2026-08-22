package DAO.Implement;

import Config.Conexion;
import Utils.AppLogger;
import DAO.Interfaz.IProductoCategoriaDAO;
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
import java.util.Map;

public class ProductoDAOImpl implements IProductoDAO {

    // DAO de la tabla intermedia producto_categoria, para resolver las
    // categorías ADICIONALES de cada producto (relación N:M). Se usa
    // solo aquí, dentro de la capa DAO, para que el resto del sistema
    // siga trabajando con Producto ya "armado" con todas sus categorías.
    private final IProductoCategoriaDAO productoCategoriaDAO = new ProductoCategoriaDAOImpl();
    
    @Override
    public boolean insertar(Producto producto) {

        String sql = "INSERT INTO producto "
                + "(id_categoria,id_promocion,nombre,descripcion,"
                + "precio,stock,disponible,estado,imagen) "
                + "VALUES (?,?,?,?,?,?,?,?,?)";

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
            ps.setBigDecimal(5, producto.getPrecio());
            ps.setInt(6, producto.getStock());
            ps.setBoolean(7, producto.isDisponible());
            ps.setBoolean(8, producto.isEstado());
            ps.setString(9, producto.getImagenPrincipal());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            AppLogger.error(getClass(), "Error de acceso a datos", e);
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
                + "estado=?,"
                + "imagen=? "
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
            ps.setBigDecimal(5, producto.getPrecio());
            ps.setInt(6, producto.getStock());
            ps.setBoolean(7, producto.isDisponible());
            ps.setBoolean(8, producto.isEstado());
            ps.setString(9, producto.getImagenPrincipal());
            ps.setInt(10, producto.getIdProducto());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            AppLogger.error(getClass(), "Error de acceso a datos", e);
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

            AppLogger.error(getClass(), "Error de acceso a datos", e);
            return false;

        }

    }

    // Consulta base reutilizada por buscarPorId() y listar().
    // JOIN con categoria y promocion para poder mapear el nombre de la
    // categoría (y los datos de la promoción) en el objeto Producto.
    // Antes esto se hacía con "SELECT * FROM producto" y solo se
    // guardaba el id_categoria, dejando categoria.getNombre() == null
    // siempre: eso rompía cualquier filtro que comparara nombres de
    // categoría (ver Base/PanelProductos y los paneles de Cliente).
    private static final String SQL_BASE =
            "SELECT p.*, "
            + "c.nombre AS nombre_categoria, "
            + "c.descripcion AS descripcion_categoria, "
            + "c.icono AS icono_categoria, "
            + "c.imagen AS imagen_categoria, "
            + "c.estado AS estado_categoria, "
            + "pr.nombre AS nombre_promocion, "
            + "pr.descuento AS descuento_promocion, "
            + "pr.estado AS estado_promocion "
            + "FROM producto p "
            + "JOIN categoria c ON c.id_categoria = p.id_categoria "
            + "LEFT JOIN promocion pr ON pr.id_promocion = p.id_promocion";

    @Override
    public Producto buscarPorId(int idProducto) {

        String sql = SQL_BASE + " WHERE p.id_producto = ?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Producto producto = mapearProducto(rs);

                    // Categorías adicionales de ESTE producto (relación N:M).
                    producto.setCategoriasAdicionales(
                            productoCategoriaDAO.listarCategoriasPorProducto(idProducto));

                    return producto;

                }

            }

        } catch (SQLException e) {

            AppLogger.error(getClass(), "Error de acceso a datos", e);

        }

        return null;

    }

    @Override
    public List<Producto> listar() {

        List<Producto> lista = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_BASE);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }

        } catch (SQLException e) {

            AppLogger.error(getClass(), "Error de acceso a datos", e);

        }

        // Una sola consulta extra para TODOS los productos (evita el
        // problema N+1 de pedir las categorías adicionales una por una).
        Map<Integer, List<Categoria>> categoriasPorProducto =
                productoCategoriaDAO.listarCategoriasPorTodosLosProductos();

        for (Producto producto : lista) {

            List<Categoria> adicionales = categoriasPorProducto.get(producto.getIdProducto());

            if (adicionales != null) {
                producto.setCategoriasAdicionales(adicionales);
            }

        }

        return lista;

    }

    // Construye un Producto completo (con su Categoria y, si aplica,
    // su Promocion ya con nombre/descuento) a partir de una fila del
    // SQL_BASE. Centralizado aquí para no duplicar el mapeo entre
    // listar() y buscarPorId().
    private Producto mapearProducto(ResultSet rs) throws SQLException {

        Producto producto = new Producto();

        Categoria categoria = new Categoria();
        categoria.setIdCategoria(rs.getInt("id_categoria"));
        categoria.setNombre(rs.getString("nombre_categoria"));
        categoria.setDescripcion(rs.getString("descripcion_categoria"));
        categoria.setIcono(rs.getString("icono_categoria"));
        categoria.setImagen(rs.getString("imagen_categoria"));
        categoria.setEstado(rs.getBoolean("estado_categoria"));

        producto.setCategoria(categoria);

        if (rs.getObject("id_promocion") != null) {

            Promocion promocion = new Promocion();
            promocion.setIdPromocion(rs.getInt("id_promocion"));
            promocion.setNombre(rs.getString("nombre_promocion"));
            promocion.setDescuento(rs.getBigDecimal("descuento_promocion"));
            promocion.setEstado(rs.getBoolean("estado_promocion"));
            producto.setPromocion(promocion);

        }

        producto.setIdProducto(rs.getInt("id_producto"));
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setPrecio(rs.getBigDecimal("precio"));
        producto.setStock(rs.getInt("stock"));
        producto.setDisponible(rs.getBoolean("disponible"));
        producto.setEstado(rs.getBoolean("estado"));
        // BUG: nunca se asignaba la imagen del producto, así que
        // TarjetaProducto siempre recibía imagenPrincipal == null y
        // UtilImagenes.producto(null,...) caía siempre en la imagen
        // genérica de respaldo ("Comidarealista.png"), sin importar
        // qué producto fuera. La columna real en MySQL es "imagen".
        producto.setImagenPrincipal(rs.getString("imagen"));

        return producto;

    }

}
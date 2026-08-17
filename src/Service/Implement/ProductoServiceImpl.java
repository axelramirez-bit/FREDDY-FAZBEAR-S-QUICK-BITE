package Service.Implement;

import DAO.Implement.ProductoCategoriaDAOImpl;
import DAO.Implement.ProductoDAOImpl;
import DAO.Interfaz.IProductoCategoriaDAO;
import DAO.Interfaz.IProductoDAO;
import Model.Producto;
import Service.Interfaz.IProductoService;
import java.math.BigDecimal;

import java.util.List;
import java.util.stream.Collectors;

public class ProductoServiceImpl implements IProductoService {

    private final IProductoDAO productoDAO;
    private final IProductoCategoriaDAO productoCategoriaDAO;

    public ProductoServiceImpl() {
        this.productoDAO = new ProductoDAOImpl();
        this.productoCategoriaDAO = new ProductoCategoriaDAOImpl();
    }

    public ProductoServiceImpl(IProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
        this.productoCategoriaDAO = new ProductoCategoriaDAOImpl();
    }

    @Override
    public boolean registrarProducto(Producto producto) {

        if (!validarProducto(producto)) {
            return false;
        }

        producto.setDisponible(producto.getStock() > 0);

        return productoDAO.insertar(producto);

    }

    @Override
    public boolean actualizarProducto(Producto producto) {

        if (producto.getIdProducto() <= 0) {
            return false;
        }

        if (!validarProducto(producto)) {
            return false;
        }

        Producto existente = productoDAO.buscarPorId(producto.getIdProducto());

        if (existente == null) {
            return false;
        }

        producto.setDisponible(producto.getStock() > 0);

        return productoDAO.actualizar(producto);

    }

    @Override
    public boolean eliminarProducto(int idProducto) {

        if (idProducto <= 0) {
            return false;
        }

        return productoDAO.eliminar(idProducto);

    }

    @Override
    public Producto obtenerProductoPorId(int idProducto) {

        if (idProducto <= 0) {
            return null;
        }

        return productoDAO.buscarPorId(idProducto);

    }

    @Override
    public List<Producto> listarProductos() {
        return productoDAO.listar();
    }

    @Override
    public List<Producto> listarProductosDisponibles() {

        return productoDAO.listar().stream()
                .filter(Producto::isEstado)
                .filter(Producto::isDisponible)
                .filter(p -> p.getStock() > 0)
                .collect(Collectors.toList());

    }

    @Override
    public boolean actualizarStock(int idProducto, int cantidad) {

        Producto producto = productoDAO.buscarPorId(idProducto);

        if (producto == null) {
            return false;
        }

        int nuevoStock = producto.getStock() + cantidad;

        if (nuevoStock < 0) {
            return false;
        }

        producto.setStock(nuevoStock);
        producto.setDisponible(nuevoStock > 0);

        return productoDAO.actualizar(producto);

    }

    // ---------- Categorías adicionales (relación N:M) ----------

    @Override
    public boolean asignarCategoriaAdicional(int idProducto, int idCategoria) {

        if (idProducto <= 0 || idCategoria <= 0) {
            return false;
        }

        return productoCategoriaDAO.asignarCategoria(idProducto, idCategoria);

    }

    @Override
    public boolean quitarCategoriaAdicional(int idProducto, int idCategoria) {

        if (idProducto <= 0 || idCategoria <= 0) {
            return false;
        }

        return productoCategoriaDAO.quitarCategoria(idProducto, idCategoria);

    }

    // ---------- Métodos auxiliares de negocio ----------
    private boolean validarProducto(Producto producto) {

        if (producto == null) {
            return false;
        }

        if (producto.getCategoria() == null || producto.getCategoria().getIdCategoria() <= 0) {
            return false;
        }

        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            return false;
        }

        if (producto.getPrecio() == null
                || producto.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }

        if (producto.getStock() < 0) {
            return false;
        }

        return true;

    }

}

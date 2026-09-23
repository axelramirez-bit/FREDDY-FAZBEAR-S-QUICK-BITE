// Paquete Service.Implement
package Service.Implement;

// Importa ProductoCategoriaDAOImpl
import DAO.Implement.ProductoCategoriaDAOImpl;
// Importa ProductoDAOImpl
import DAO.Implement.ProductoDAOImpl;
// Importa IProductoCategoriaDAO
import DAO.Interfaz.IProductoCategoriaDAO;
// Importa IProductoDAO
import DAO.Interfaz.IProductoDAO;
// Importa Producto
import Model.Producto;
// Importa IProductoService
import Service.Interfaz.IProductoService;
// Importa BigDecimal
import java.math.BigDecimal;

// Importa List
import java.util.List;
// Importa Collectors
import java.util.stream.Collectors;

// Servicio de productos: validación y consultas
public class ProductoServiceImpl implements IProductoService {

    // DAO de productos
    private final IProductoDAO productoDAO;
    // DAO de categorías adicionales
    private final IProductoCategoriaDAO productoCategoriaDAO;

    // Constructor normal
    public ProductoServiceImpl() {
        // Crea el DAO de productos
        this.productoDAO = new ProductoDAOImpl();
        // Crea el DAO de categorías adicionales
        this.productoCategoriaDAO = new ProductoCategoriaDAOImpl();
    }

    // Constructor para pruebas
    public ProductoServiceImpl(IProductoDAO productoDAO) {
        // Asigna el DAO recibido
        this.productoDAO = productoDAO;
        // Crea el DAO de categorías adicionales
        this.productoCategoriaDAO = new ProductoCategoriaDAOImpl();
    }

    @Override
    // Registra un producto
    public boolean registrarProducto(Producto producto) {

        // Si no es válido
        if (!validarProducto(producto)) {
            // devuelve false
            return false;
        }

        // Disponible solo si hay stock
        producto.setDisponible(producto.getStock() > 0);

        // Inserta el producto
        return productoDAO.insertar(producto);

    }

    @Override
    // Actualiza un producto
    public boolean actualizarProducto(Producto producto) {

        // Si el id no es válido
        if (producto.getIdProducto() <= 0) {
            // devuelve false
            return false;
        }

        // Si no pasa la validación
        if (!validarProducto(producto)) {
            // devuelve false
            return false;
        }

        // Busca el producto actual
        Producto existente = productoDAO.buscarPorId(producto.getIdProducto());

        // Si no existe
        if (existente == null) {
            // devuelve false
            return false;
        }

        // Disponible solo si hay stock
        producto.setDisponible(producto.getStock() > 0);

        // Guarda. OJO: sobrescribe el stock con el del formulario; puede pisar descuentos hechos por pedidos mientras estaba abierto
        return productoDAO.actualizar(producto);

    }

    @Override
    // Elimina un producto
    public boolean eliminarProducto(int idProducto) {

        // Si el id no es válido
        if (idProducto <= 0) {
            // devuelve false
            return false;
        }

        // Elimina en el DAO
        return productoDAO.eliminar(idProducto);

    }

    @Override
    // Desactiva un producto
    public boolean desactivarProducto(int idProducto) {

        // Si el id no es válido
        if (idProducto <= 0) {
            // devuelve false
            return false;
        }

        // Cambia el estado a inactivo
        return productoDAO.cambiarEstado(idProducto, false);
    }

    @Override
    // Activa un producto
    public boolean activarProducto(int idProducto) {

        // Si el id no es válido
        if (idProducto <= 0) {
            // devuelve false
            return false;
        }

        // Cambia el estado a activo
        return productoDAO.cambiarEstado(idProducto, true);
    }

    @Override
    // Obtiene un producto por id
    public Producto obtenerProductoPorId(int idProducto) {

        // Si el id no es válido
        if (idProducto <= 0) {
            // devuelve null
            return null;
        }

        // Consulta al DAO
        return productoDAO.buscarPorId(idProducto);

    }

    @Override
    // Lista todos los productos
    public List<Producto> listarProductos() {
        // Consulta al DAO
        return productoDAO.listar();
    }

    @Override
    // Lista solo los productos disponibles
    public List<Producto> listarProductosDisponibles() {

        // Trae todos los productos
        return productoDAO.listar().stream()
                // Solo activos
                .filter(Producto::isEstado)
                // Solo marcados como disponibles
                .filter(Producto::isDisponible)
                // Solo con stock
                .filter(p -> p.getStock() > 0)
                // Junta el resultado en una lista
                .collect(Collectors.toList());

    }

    @Override
    // Suma o resta stock (no se usa desde la interfaz)
    public boolean actualizarStock(int idProducto, int cantidad) {

        // Busca el producto
        Producto producto = productoDAO.buscarPorId(idProducto);

        // Si no existe
        if (producto == null) {
            // devuelve false
            return false;
        }

        // Calcula el nuevo stock. OJO: leer y luego escribir no es atómico; dos usuarios a la vez pueden perder una actualización
        int nuevoStock = producto.getStock() + cantidad;

        // Si quedaría negativo
        if (nuevoStock < 0) {
            // devuelve false
            return false;
        }

        // Guarda el nuevo stock
        producto.setStock(nuevoStock);
        // Actualiza la disponibilidad
        producto.setDisponible(nuevoStock > 0);

        // Guarda el producto
        return productoDAO.actualizar(producto);

    }

    // ---------- Categorías adicionales (relación N:M) ----------

    @Override
    // Asigna una categoría adicional a un producto
    public boolean asignarCategoriaAdicional(int idProducto, int idCategoria) {

        // Si algún id no es válido
        if (idProducto <= 0 || idCategoria <= 0) {
            // devuelve false
            return false;
        }

        // Guarda la relación
        return productoCategoriaDAO.asignarCategoria(idProducto, idCategoria);

    }

    @Override
    // Quita una categoría adicional de un producto
    public boolean quitarCategoriaAdicional(int idProducto, int idCategoria) {

        // Si algún id no es válido
        if (idProducto <= 0 || idCategoria <= 0) {
            // devuelve false
            return false;
        }

        // Elimina la relación
        return productoCategoriaDAO.quitarCategoria(idProducto, idCategoria);

    }

    // ---------- Métodos auxiliares de negocio ----------
    // Valida el producto (true si no hay error)
    private boolean validarProducto(Producto producto) {
        // Sin mensaje de error significa válido
        return validar(producto) == null;
    }

    @Override
    // Valida y devuelve el motivo del error (null si es válido)
    public String validar(Producto producto) {

        // Si el producto es nulo
        if (producto == null) {
            // devuelve el motivo
            return "Producto inválido.";
        }

        // Si no tiene categoría válida
        if (producto.getCategoria() == null || producto.getCategoria().getIdCategoria() <= 0) {
            // devuelve el motivo
            return "Debes seleccionar una categoría.";
        }

        // Si el nombre está vacío
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            // devuelve el motivo
            return "El nombre del producto es obligatorio.";
        }

        // Si el precio es nulo o no es mayor que cero
        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            // devuelve el motivo
            return "El precio debe ser mayor a Q0.00.";
        }

        // Si el stock es negativo
        if (producto.getStock() < 0) {
            // devuelve el motivo
            return "El stock no puede ser negativo.";
        }

        // Todo correcto: devuelve null
        return null;

    }

}
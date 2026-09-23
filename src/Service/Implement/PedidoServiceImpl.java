// Paquete Service.Implement
package Service.Implement;

// Importa Conexion
import Config.Conexion;
// Importa DetallePedidoDAOImpl
import DAO.Implement.DetallePedidoDAOImpl;
// Importa PedidoDAOImpl
import DAO.Implement.PedidoDAOImpl;
// Importa IDetallePedidoDAO
import DAO.Interfaz.IDetallePedidoDAO;
// Importa IPedidoDAO
import DAO.Interfaz.IPedidoDAO;
// Importa DetallePedido
import Model.DetallePedido;
// Importa EstadoPedido
import Model.EstadoPedido;
// Importa Pedido
import Model.Pedido;
// Importa IPedidoService
import Service.Interfaz.IPedidoService;
// Importa AppLogger
import Utils.AppLogger;
// Importa BigDecimal
import java.math.BigDecimal;
// Importa Connection
import java.sql.Connection;
// Importa SQLException
import java.sql.SQLException;

// Importa LocalDateTime
import java.time.LocalDateTime;
// Importa List
import java.util.List;
// Importa UUID
import java.util.UUID;

// Servicio de pedidos: valida, guarda con transacción y consulta
public class PedidoServiceImpl implements IPedidoService {

    // DAO de pedidos
    private final IPedidoDAO pedidoDAO;
    // DAO de detalles de pedido
    private final IDetallePedidoDAO detallePedidoDAO;

    // Constructor normal: crea los DAO reales
    public PedidoServiceImpl() {
        // Crea el DAO de pedidos
        this.pedidoDAO = new PedidoDAOImpl();
        // Crea el DAO de detalles
        this.detallePedidoDAO = new DetallePedidoDAOImpl();
    }

    // Permite inyectar los DAO (útil para pruebas unitarias con mocks)
    // Constructor para pruebas: recibe los DAO
    public PedidoServiceImpl(IPedidoDAO pedidoDAO, IDetallePedidoDAO detallePedidoDAO) {
        // Asigna el DAO de pedidos
        this.pedidoDAO = pedidoDAO;
        // Asigna el DAO de detalles
        this.detallePedidoDAO = detallePedidoDAO;
    }

    @Override
    // Registra un pedido y sus detalles en una sola transacción
    public boolean registrarPedido(Pedido pedido) {

        // Si el pedido no es válido
        if (!validarPedido(pedido)) {
            // devuelve false
            return false;
        }

        // Si no tiene número de orden
        if (pedido.getNumeroOrden() == null || pedido.getNumeroOrden().isBlank()) {
            // genera uno nuevo
            pedido.setNumeroOrden(generarNumeroOrden());
        }

        // Si no tiene fecha
        if (pedido.getFecha() == null) {
            // usa la fecha y hora actual
            pedido.setFecha(LocalDateTime.now());
        }

        // Si no tiene estado
        if (pedido.getEstado() == null) {
            // lo deja PENDIENTE
            pedido.setEstado(EstadoPedido.PENDIENTE);
        }

        // Recalcula el total. OJO: lo deja en subtotal - descuento (pierde el costo de envío)
        calcularTotal(pedido);

        // Conexion es un singleton (un solo Connection real para toda
        // la app — ver Config/Conexion.java), así que este DAO y
        // DetallePedidoDAOImpl comparten la misma conexión real
        // aunque cada uno pida la suya por separado. Eso permite
        // envolver el INSERT del encabezado + sus líneas en una sola
        // transacción real, sin tener que rediseñar los DAO para que
        // reciban el Connection por parámetro.
        // Obtiene la conexión compartida (singleton)
        Connection con = Conexion.getInstancia().getConexion();

        try {
            // Inicia la transacción. OJO: la conexión es compartida; otro hilo que use la BD ahora entra en esta transacción
            con.setAutoCommit(false);

            // Inserta el encabezado del pedido
            boolean pedidoInsertado = pedidoDAO.insertar(pedido);

            // Si falló o no se generó el id
            if (!pedidoInsertado || pedido.getIdPedido() <= 0) {
                // revierte la transacción
                con.rollback();
                // devuelve false
                return false;
            }

            // Recorre los detalles del pedido
            for (DetallePedido detalle : pedido.getDetalles()) {

                // Enlaza el detalle con el pedido ya guardado
                detalle.setPedido(pedido);

                // Inserta el detalle (un trigger de la BD descuenta el stock)
                if (!detallePedidoDAO.insertar(detalle)) {
                    // Si falla, revierte
                    con.rollback();
                    // devuelve false
                    return false;
                }
            }

            // Confirma la transacción
            con.commit();
            // Devuelve éxito
            return true;

        // Si hay error SQL
        } catch (SQLException e) {

            // Intenta revertir
            try {
                // Revierte la transacción
                con.rollback();
            // Si falla el rollback
            } catch (SQLException rollbackEx) {
                // Registra el error del rollback
                AppLogger.error(PedidoServiceImpl.class,
                        "Falló el rollback al registrar el pedido "
                        + pedido.getNumeroOrden(), rollbackEx);
            }

            // Registra el fallo del registro del pedido
            AppLogger.error(PedidoServiceImpl.class,
                    "No se pudo registrar el pedido " + pedido.getNumeroOrden()
                    + " (revertido por transacción).", e);
            // Devuelve false (el motivo no llega a la vista)
            return false;

        // Se ejecuta siempre al terminar
        } finally {

            // Intenta restaurar el autocommit
            try {
                // Vuelve al modo automático
                con.setAutoCommit(true);
            // Si falla la restauración
            } catch (SQLException e) {
                // Registra el error
                AppLogger.error(PedidoServiceImpl.class,
                        "No se pudo restaurar autoCommit tras registrar pedido.", e);
            }
        }

    }

    @Override
    // Indica si ya hay un pedido para un carrito
    public boolean existePedidoParaCarrito(int idCarrito) {

        // Si el id no es válido
        if (idCarrito <= 0) {
            // devuelve false
            return false;
        }

        // Consulta al DAO
        return pedidoDAO.existePedidoParaCarrito(idCarrito);
    }

    @Override
    // Actualiza un pedido existente
    public boolean actualizarPedido(Pedido pedido) {

        // Si no tiene id válido
        if (pedido.getIdPedido() <= 0) {
            // devuelve false
            return false;
        }

        // Si el pedido no es válido
        if (!validarPedido(pedido)) {
            // devuelve false
            return false;
        }

        // Busca el pedido guardado. OJO: también carga sus detalles (consultas extra solo para comprobar que existe)
        Pedido existente = pedidoDAO.buscarPorId(pedido.getIdPedido());

        // Si no existe
        if (existente == null) {
            // devuelve false
            return false;
        }

        // Recalcula el total (subtotal - descuento)
        calcularTotal(pedido);

        // Guarda los cambios
        return pedidoDAO.actualizar(pedido);

    }

    @Override
    // Elimina un pedido por id
    public boolean eliminarPedido(int idPedido) {

        // Si el id no es válido
        if (idPedido <= 0) {
            // devuelve false
            return false;
        }

        // Elimina en el DAO
        return pedidoDAO.eliminar(idPedido);

    }

    @Override
    // Obtiene un pedido por id
    public Pedido obtenerPedidoPorId(int idPedido) {

        // Si el id no es válido
        if (idPedido <= 0) {
            // devuelve null
            return null;
        }

        // Consulta al DAO
        return pedidoDAO.buscarPorId(idPedido);

    }

    @Override
    // Lista todos los pedidos
    public List<Pedido> listarPedidos() {
        // Consulta al DAO
        return pedidoDAO.listar();
    }

    // ---------- Métodos auxiliares de negocio ----------
    // Valida los datos mínimos del pedido
    private boolean validarPedido(Pedido pedido) {

        // Si es nulo
        if (pedido == null) {
            // no es válido
            return false;
        }

        // Si no tiene un usuario válido
        if (pedido.getIdUsuario() == null || pedido.getIdUsuario().getIdUsuario() <= 0) {
            // no es válido
            return false;
        }

        // Si no tiene tipo de entrega
        if (pedido.getTipoEntrega() == null) {
            // no es válido
            return false;
        }

        // Si el subtotal es nulo
        if (pedido.getSubtotal() == null
                // o negativo
                || pedido.getSubtotal().compareTo(BigDecimal.ZERO) < 0) {
            // no es válido
            return false;
        }

        // Si el descuento es nulo
        if (pedido.getDescuento() == null
                // o negativo
                || pedido.getDescuento().compareTo(BigDecimal.ZERO) < 0) {
            // no es válido
            return false;
        }

        // Si el descuento supera el subtotal
        if (pedido.getDescuento().compareTo(pedido.getSubtotal()) > 0) {
            // no es válido
            return false;
        }

        // Es válido
        return true;

    }

    // Calcula el total del pedido
    private void calcularTotal(Pedido pedido) {

        // Total = subtotal menos descuento
        BigDecimal total = pedido.getSubtotal()
                // Resta el descuento
                .subtract(pedido.getDescuento());

        // Si el total es negativo
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            // lo deja en cero
            total = BigDecimal.ZERO;
        }

        // Guarda el total en el pedido
        pedido.setTotal(total);

    }

    // Genera el número de orden
    private String generarNumeroOrden() {
        // "ORD-" más 8 caracteres aleatorios
        return "ORD-" + UUID.randomUUID().toString()
                // Toma los primeros 8
                .substring(0, 8)
                // En mayúsculas
                .toUpperCase();
    }

}

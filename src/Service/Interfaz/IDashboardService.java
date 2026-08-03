package Service.Interfaz;

import Model.EstadoPedido;

import java.math.BigDecimal;
import java.util.Map;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Agregaciones de negocio para los dashboards (Administrador y
 * Trabajador). No toca SQL directamente — se apoya en
 * IPedidoService, que ya trae cada Pedido con sus DetallePedido
 * cargados. Si algún día el volumen de pedidos crece demasiado
 * para calcular esto en memoria, estas mismas firmas se pueden
 * reimplementar con consultas SQL de agregación sin tocar quién
 * las usa (Controller/View no cambian).
 * ===============================================================
 */
public interface IDashboardService {

    /** Ventas totales por día, de los últimos N días (incluye hoy). */
    Map<String, BigDecimal> ventasPorDia(int diasAtras);

    /** Ventas totales agrupadas por nombre de categoría. */
    Map<String, BigDecimal> ventasPorCategoria();

    /** Los N productos con más unidades vendidas, ya ordenados de mayor a menor. */
    Map<String, Integer> productosMasVendidos(int cantidadTop);

    /** Cuántos pedidos hay en cada EstadoPedido (incluye los que están en 0). */
    Map<EstadoPedido, Long> pedidosPorEstado();

    /** Cuántos pedidos se recibieron en cada hora del día (0 a 23). */
    Map<Integer, Long> pedidosPorHora();

    /** Resumen para las tarjetas TarjetaKPI de arriba del dashboard. */
    ResumenDashboard obtenerResumen();

    /**
     * DTO simple de solo lectura, no es una entidad de BD — por
     * eso vive aquí y no en Model/.
     */
    class ResumenDashboard {

        public final BigDecimal ventasHoy;
        public final int totalPedidos;
        public final int pedidosPendientes;
        public final int pedidosCompletados;

        public ResumenDashboard(
                BigDecimal ventasHoy,
                int totalPedidos,
                int pedidosPendientes,
                int pedidosCompletados) {

            this.ventasHoy = ventasHoy;
            this.totalPedidos = totalPedidos;
            this.pedidosPendientes = pedidosPendientes;
            this.pedidosCompletados = pedidosCompletados;
        }
    }

}
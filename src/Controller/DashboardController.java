// Paquete Controller
package Controller;

// Importa el enum de estados de pedido
import Model.EstadoPedido;
// Importa el servicio de dashboard
import Service.Implement.DashboardServiceImpl;
// Importa el servicio de pedidos
import Service.Implement.PedidoServiceImpl;
// Importa la interfaz del servicio de dashboard
import Service.Interfaz.IDashboardService;

// Importa BigDecimal para montos
import java.math.BigDecimal;
// Importa LinkedHashMap (mapa que conserva el orden)
import java.util.LinkedHashMap;
// Importa Map
import java.util.Map;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Coordina DashboardAdministrador y DashboardTrabajador con
 * IDashboardService. No dibuja nada, no sabe qué es JFreeChart —
 * solo entrega Map<String, Number> listos para FabricaGraficas.
 * ===============================================================
 */
// Controlador que conecta las vistas del dashboard con el servicio
public class DashboardController {

    // Servicio de dashboard (por interfaz)
    private final IDashboardService dashboardService;

    // Constructor
    public DashboardController() {
        // Crea el servicio inyectándole un PedidoServiceImpl
        this.dashboardService = new DashboardServiceImpl(new PedidoServiceImpl());
    }

    // Devuelve el resumen general del dashboard
    public IDashboardService.ResumenDashboard obtenerResumen() {
        // Pide el resumen al servicio
        return dashboardService.obtenerResumen();
    }

    // Ventas de los últimos N días
    public Map<String, BigDecimal> ventasUltimosDias(int dias) {
        // Pide las ventas por día al servicio
        return dashboardService.ventasPorDia(dias);
    }

    // Ventas agrupadas por categoría
    public Map<String, BigDecimal> ventasPorCategoria() {
        // Pide las ventas por categoría al servicio
        return dashboardService.ventasPorCategoria();
    }

    // Productos más vendidos (top N)
    public Map<String, Integer> productosMasVendidos(int cantidadTop) {
        // Pide el top de productos al servicio
        return dashboardService.productosMasVendidos(cantidadTop);
    }

    /** Convierte las claves EstadoPedido a String legible, listo para el pastel de FabricaGraficas. */
    // Pedidos por estado con nombre legible
    public Map<String, Long> pedidosPorEstado() {

        // Mapa resultado que conserva el orden
        Map<String, Long> resultado = new LinkedHashMap<>();

        // Recorre los pedidos por estado que da el servicio
        for (Map.Entry<EstadoPedido, Long> entrada : dashboardService.pedidosPorEstado().entrySet()) {
            // Guarda el estado capitalizado y su cantidad
            resultado.put(capitalizar(entrada.getKey().name()), entrada.getValue());
        }

        // Devuelve el mapa
        return resultado;
    }

    /** Convierte las 24 horas a etiquetas "0h".."23h", listo para la línea de FabricaGraficas. */
    // Pedidos por hora con etiquetas "0h".."23h"
    public Map<String, Long> pedidosPorHora() {

        // Mapa resultado que conserva el orden
        Map<String, Long> resultado = new LinkedHashMap<>();

        // Recorre los pedidos por hora que da el servicio
        for (Map.Entry<Integer, Long> entrada : dashboardService.pedidosPorHora().entrySet()) {
            // Guarda la etiqueta (hora + "h") y su cantidad
            resultado.put(entrada.getKey() + "h", entrada.getValue());
        }

        // Devuelve el mapa
        return resultado;
    }

    // Convierte "TEXTO" en "Texto"
    private String capitalizar(String texto) {
        // Primera letra igual + resto en minúscula
        return texto.charAt(0) + texto.substring(1).toLowerCase();
    }

}
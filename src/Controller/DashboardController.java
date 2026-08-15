package Controller;

import Model.EstadoPedido;
import Service.Implement.DashboardServiceImpl;
import Service.Implement.PedidoServiceImpl;
import Service.Interfaz.IDashboardService;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
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
public class DashboardController {

    private final IDashboardService dashboardService;

    public DashboardController() {
        this.dashboardService = new DashboardServiceImpl(new PedidoServiceImpl());
    }

    public IDashboardService.ResumenDashboard obtenerResumen() {
        return dashboardService.obtenerResumen();
    }

    public Map<String, BigDecimal> ventasUltimosDias(int dias) {
        return dashboardService.ventasPorDia(dias);
    }

    public Map<String, BigDecimal> ventasPorCategoria() {
        return dashboardService.ventasPorCategoria();
    }

    public Map<String, Integer> productosMasVendidos(int cantidadTop) {
        return dashboardService.productosMasVendidos(cantidadTop);
    }

    /** Convierte las claves EstadoPedido a String legible, listo para el pastel de FabricaGraficas. */
    public Map<String, Long> pedidosPorEstado() {

        Map<String, Long> resultado = new LinkedHashMap<>();

        for (Map.Entry<EstadoPedido, Long> entrada : dashboardService.pedidosPorEstado().entrySet()) {
            resultado.put(capitalizar(entrada.getKey().name()), entrada.getValue());
        }

        return resultado;
    }

    /** Convierte las 24 horas a etiquetas "0h".."23h", listo para la línea de FabricaGraficas. */
    public Map<String, Long> pedidosPorHora() {

        Map<String, Long> resultado = new LinkedHashMap<>();

        for (Map.Entry<Integer, Long> entrada : dashboardService.pedidosPorHora().entrySet()) {
            resultado.put(entrada.getKey() + "h", entrada.getValue());
        }

        return resultado;
    }

    private String capitalizar(String texto) {
        return texto.charAt(0) + texto.substring(1).toLowerCase();
    }

}
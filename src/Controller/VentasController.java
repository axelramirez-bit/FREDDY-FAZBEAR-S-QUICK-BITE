// Paquete Controller
package Controller;

// Importa el servicio de pagos
import Service.Implement.PagoServiceImpl;
// Importa el servicio de pedidos
import Service.Implement.PedidoServiceImpl;
// Importa el servicio de ventas
import Service.Implement.VentasServiceImpl;
// Importa la interfaz del servicio de ventas
import Service.Interfaz.IVentasService;

// Importa BigDecimal
import java.math.BigDecimal;
// Importa LocalDate
import java.time.LocalDate;
// Importa Map
import java.util.Map;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Coordina PanelVentas con IVentasService. No dibuja nada, no sabe
 * qué es JFreeChart ni JTable — solo entrega datos ya calculados,
 * igual que DashboardController.
 * ===============================================================
 */
// Controlador que conecta PanelVentas con el servicio
public class VentasController {

    // Servicio de ventas (por interfaz)
    private final IVentasService ventasService;

    // Constructor
    public VentasController() {
        // Crea el servicio con los servicios de pedidos y pagos
        this.ventasService = new VentasServiceImpl(new PedidoServiceImpl(), new PagoServiceImpl());
    }

    // Devuelve el resumen de ventas entre dos fechas
    public IVentasService.ResumenVentas obtenerResumen(LocalDate desde, LocalDate hasta) {
        // Pide el resumen al servicio
        return ventasService.obtenerResumen(desde, hasta);
    }

    // Ventas por hora de un día
    public Map<String, BigDecimal> ventasPorHora(LocalDate dia) {
        // Pide las ventas por hora al servicio
        return ventasService.ventasPorHora(dia);
    }

    // Top de productos en un rango de fechas
    public Map<String, Integer> topProductos(int cantidadTop, LocalDate desde, LocalDate hasta) {
        // Pide el top al servicio
        return ventasService.topProductos(cantidadTop, desde, hasta);
    }

    // Ventas por categoría en un rango de fechas
    public Map<String, BigDecimal> ventasPorCategoria(LocalDate desde, LocalDate hasta) {
        // Pide las ventas por categoría al servicio
        return ventasService.ventasPorCategoria(desde, hasta);
    }

    // Historial de ventas según un filtro
    public java.util.List<IVentasService.VentaHistorial> historialVentas(IVentasService.FiltroVentas filtro) {
        // Pide el historial al servicio
        return ventasService.historialVentas(filtro);
    }
}
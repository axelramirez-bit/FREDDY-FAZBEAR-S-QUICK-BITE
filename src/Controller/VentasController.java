package Controller;

import Service.Implement.PagoServiceImpl;
import Service.Implement.PedidoServiceImpl;
import Service.Implement.VentasServiceImpl;
import Service.Interfaz.IVentasService;

import java.math.BigDecimal;
import java.time.LocalDate;
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
public class VentasController {

    private final IVentasService ventasService;

    public VentasController() {
        this.ventasService = new VentasServiceImpl(new PedidoServiceImpl(), new PagoServiceImpl());
    }

    public IVentasService.ResumenVentas obtenerResumen(LocalDate desde, LocalDate hasta) {
        return ventasService.obtenerResumen(desde, hasta);
    }

    public Map<String, BigDecimal> ventasPorHora(LocalDate dia) {
        return ventasService.ventasPorHora(dia);
    }

    public Map<String, Integer> topProductos(int cantidadTop, LocalDate desde, LocalDate hasta) {
        return ventasService.topProductos(cantidadTop, desde, hasta);
    }

    public Map<String, BigDecimal> ventasPorCategoria(LocalDate desde, LocalDate hasta) {
        return ventasService.ventasPorCategoria(desde, hasta);
    }

    public java.util.List<IVentasService.VentaHistorial> historialVentas(IVentasService.FiltroVentas filtro) {
        return ventasService.historialVentas(filtro);
    }
}
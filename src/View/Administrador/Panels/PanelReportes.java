package View.Administrador.Panels;

import Model.EstadoPago;
import Model.Pago;
import Model.Pedido;
import Service.Implement.DashboardServiceImpl;
import Service.Implement.PagoServiceImpl;
import Service.Implement.PedidoServiceImpl;
import Service.Interfaz.IDashboardService;
import Service.Interfaz.IPagoService;
import Service.Interfaz.IPedidoService;
import View.Componentes.PanelFondo;
import View.Componentes.TarjetaKPI;
import View.Utils.FabricaBotones;
import View.Utils.FabricaEtiquetas;
import View.Utils.FabricaGraficas;
import View.Utils.FabricaIconos;
import View.Utils.FabricaTablas;
import View.Utils.UIConstants;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Reportes y Exportación — interfaz 10 del boceto.
 *
 * Los KPIs y la gráfica SÍ funcionan con datos reales (se apoyan
 * en IPedidoService/IPagoService/IDashboardService, ya
 * implementados). Los botones de exportar están conectados pero
 * avisan que falta una pieza en vez de fingir que funcionan:
 *
 *   • Exportar Excel → falta agregar Apache POI a libreria/, no
 *     está en el proyecto todavía.
 *   • Exportar PDF → PDFBox SÍ está en libreria/, pero construir
 *     el documento en sí (layout, tablas, logo) es su propia
 *     tarea, no algo que deba improvisarse aquí sin que el equipo
 *     decida cómo debe verse el PDF.
 *
 * Cuando decidan encarar la exportación, dímelo y lo hacemos como
 * su propia entrega — mejor que un botón que "compila pero no
 * hace lo que promete".
 * ===============================================================
 */
public class PanelReportes extends PanelFondo {

    private final IPedidoService pedidoService = new PedidoServiceImpl();
    private final IPagoService pagoService = new PagoServiceImpl();
    private final IDashboardService dashboardService = new DashboardServiceImpl(pedidoService);

    private static final DecimalFormat FORMATO_Q = new DecimalFormat("Q#,##0.00");

    private TarjetaKPI tarjetaVentasTotales;
    private TarjetaKPI tarjetaPedidosTotales;
    private TarjetaKPI tarjetaTicketPromedio;
    private TarjetaKPI tarjetaProductosVendidos;

    private JPanel panelGrafica;

    public PanelReportes() {

        super();

        setOpaque(false);
        setLayout(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));

        JPanel norte = new JPanel(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));
        norte.setOpaque(false);
        norte.add(FabricaEtiquetas.crearTitulo("REPORTES"), BorderLayout.NORTH);
        norte.add(crearFilaKPI(), BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);

        panelGrafica = FabricaTablas.crearTarjeta(new BorderLayout());
        add(panelGrafica, BorderLayout.CENTER);

        add(crearPieExportar(), BorderLayout.SOUTH);

        cargarDatos();
    }

    private JPanel crearFilaKPI() {

        JPanel fila = new JPanel(new GridLayout(1, 4, UIConstants.ESPACIO_SUBTITULO, 0));
        fila.setOpaque(false);
        fila.setBorder(javax.swing.BorderFactory.createEmptyBorder(UIConstants.ESPACIO_SUBTITULO, 0, 0, 0));

        tarjetaVentasTotales = new TarjetaKPI(FabricaIconos.reportes(), "Ventas Totales (7 días)", "Q0.00", "");
        tarjetaPedidosTotales = new TarjetaKPI(FabricaIconos.pedidos(), "Pedidos Totales", "0", "");
        tarjetaTicketPromedio = new TarjetaKPI(FabricaIconos.reportes(), "Ticket Promedio", "Q0.00", "");
        tarjetaProductosVendidos = new TarjetaKPI(FabricaIconos.productos(), "Productos Vendidos", "0", "");

        fila.add(tarjetaVentasTotales);
        fila.add(tarjetaPedidosTotales);
        fila.add(tarjetaTicketPromedio);
        fila.add(tarjetaProductosVendidos);

        return fila;
    }

    private JPanel crearPieExportar() {

        JPanel pie = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, UIConstants.ESPACIO_SUBTITULO, 0));
        pie.setOpaque(false);

        JButton btnExportarExcel = FabricaBotones.crearPrimario("Exportar Excel (.xlsx)");
        JButton btnExportarPdf = FabricaBotones.crearSecundario("Exportar PDF (.pdf)");

        btnExportarExcel.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "Exportar a Excel requiere agregar Apache POI a libreria/ — todavía no está incluido en el proyecto.",
                "Exportación no disponible",
                JOptionPane.INFORMATION_MESSAGE
        ));

        btnExportarPdf.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "PDFBox ya está en libreria/, pero falta definir el diseño del PDF (qué tablas, logo, encabezado). "
                        + "Cuando lo decidan, se conecta aquí mismo.",
                "Exportación pendiente de diseño",
                JOptionPane.INFORMATION_MESSAGE
        ));

        pie.add(btnExportarExcel);
        pie.add(btnExportarPdf);

        return pie;
    }

    public void cargarDatos() {

        List<Pedido> pedidos = pedidoService.listarPedidos();
        List<Pago> pagos = pagoService.listar();

        BigDecimal ventasTotales = BigDecimal.ZERO;
        int productosVendidos = 0;

        for (Pago pago : pagos) {
            if (pago.getEstado() == EstadoPago.PAGADO) {
                ventasTotales = ventasTotales.add(pago.getMonto());
            }
        }

        for (Pedido pedido : pedidos) {
            for (var detalle : pedido.getDetalles()) {
                productosVendidos += detalle.getCantidad();
            }
        }

        tarjetaVentasTotales.actualizar(FORMATO_Q.format(ventasTotales), "");
        tarjetaPedidosTotales.actualizar(String.valueOf(pedidos.size()), "");

        BigDecimal ticketPromedio = pedidos.isEmpty()
                ? BigDecimal.ZERO
                : ventasTotales.divide(BigDecimal.valueOf(pedidos.size()), 2, java.math.RoundingMode.HALF_UP);

        tarjetaTicketPromedio.actualizar(FORMATO_Q.format(ticketPromedio), "");
        tarjetaProductosVendidos.actualizar(String.valueOf(productosVendidos), "");

        Map<String, BigDecimal> ventasPorDia = dashboardService.ventasPorDia(7);

        panelGrafica.removeAll();
        panelGrafica.add(FabricaEtiquetas.crearSubtitulo("Ventas por Día (Q) — últimos 7 días"), BorderLayout.NORTH);
        panelGrafica.add(FabricaGraficas.crearGraficaBarras(null, "Ventas (Q)", ventasPorDia), BorderLayout.CENTER);
        panelGrafica.revalidate();
        panelGrafica.repaint();
    }

}
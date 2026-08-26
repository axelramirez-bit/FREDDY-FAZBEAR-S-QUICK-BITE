package View.Utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.Color;
import java.awt.Font;
import java.util.Map;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Única puerta de entrada a JFreeChart en todo el proyecto.
 *
 * Nadie más debería escribir "new JFreeChart(...)" ni tocar un
 * CategoryPlot/PiePlot directamente — así como FabricaBotones es
 * la única que sabe cómo se ve un botón Freddy, esta es la única
 * que sabe cómo se ve una gráfica Freddy (colores de
 * PaletaColores, fuente Poppins/Aileron de AdministradorTema, sin
 * el aspecto por defecto gris de JFreeChart).
 *
 * JFreeChart, tal como advierte el documento de arquitectura del
 * equipo, no calcula nada de negocio: solo recibe un Map ya
 * calculado (por DashboardService) y lo dibuja. Estas tres
 * gráficas cubren todo lo que hoy necesita el proyecto:
 *
 *   crearGraficaBarras   -> ventas por categoría, top productos
 *   crearGraficaLineas   -> ventas por día, pedidos por hora
 *   crearGraficaCircular -> pedidos por estado
 *
 * Uso típico dentro de un panel de dashboard:
 *
 *     Map<String, BigDecimal> datos = dashboardService.ventasPorDia(7);
 *     panel.add(FabricaGraficas.crearGraficaLineas(
 *             "Ventas de la semana", "Ventas (Q)", datos));
 * ===============================================================
 */
public final class FabricaGraficas {

    private static final Color[] PALETA = {
        PaletaColores.PRINCIPAL,
        PaletaColores.SECUNDARIO,
        PaletaColores.ACENTO,
        PaletaColores.TEXTO,
        PaletaColores.PRINCIPAL.brighter(),
        PaletaColores.SECUNDARIO.darker(),
        PaletaColores.ACENTO.brighter(),
        PaletaColores.TEXTO.brighter()
    };

    private FabricaGraficas() {
    }

    // ==========================================================
    // BARRAS — ej. ventas por categoría, productos más vendidos
    // ==========================================================

    public static ChartPanel crearGraficaBarras(
            String titulo, String etiquetaValor, Map<String, ? extends Number> datos) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, ? extends Number> entrada : datos.entrySet()) {
            dataset.addValue(entrada.getValue(), etiquetaValor, entrada.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                titulo, null, etiquetaValor, dataset,
                PlotOrientation.VERTICAL, false, true, false
        );

        CategoryPlot plot = chart.getCategoryPlot();

        BarRenderer renderer = new BarRenderer();
        renderer.setSeriesPaint(0, PaletaColores.PRINCIPAL);
        renderer.setShadowVisible(false);
        renderer.setDrawBarOutline(false);
        plot.setRenderer(renderer);

        aplicarEstiloPlot(plot);

        return empacar(chart);
    }

    // ==========================================================
    // LÍNEAS — ej. ventas por día, pedidos por hora
    // ==========================================================

    public static ChartPanel crearGraficaLineas(
            String titulo, String etiquetaValor, Map<String, ? extends Number> datos) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, ? extends Number> entrada : datos.entrySet()) {
            dataset.addValue(entrada.getValue(), etiquetaValor, entrada.getKey());
        }

        JFreeChart chart = ChartFactory.createLineChart(
                titulo, null, etiquetaValor, dataset,
                PlotOrientation.VERTICAL, false, true, false
        );

        CategoryPlot plot = chart.getCategoryPlot();

        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesPaint(0, PaletaColores.PRINCIPAL);
        renderer.setSeriesStroke(0, new java.awt.BasicStroke(2.5f));
        renderer.setSeriesShapesVisible(0, true);
        plot.setRenderer(renderer);

        aplicarEstiloPlot(plot);

        return empacar(chart);
    }

    // ==========================================================
    // CIRCULAR — ej. pedidos por estado
    // ==========================================================

    public static ChartPanel crearGraficaCircular(String titulo, Map<String, ? extends Number> datos) {

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        for (Map.Entry<String, ? extends Number> entrada : datos.entrySet()) {
            dataset.setValue(entrada.getKey(), entrada.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(titulo, dataset, false, true, false);

        PiePlot<String> plot = (PiePlot<String>) chart.getPlot();

        int i = 0;
        for (Object clave : dataset.getKeys()) {
            plot.setSectionPaint((String) clave, PALETA[i % PALETA.length]);
            i++;
        }

        plot.setBackgroundPaint(null);
        plot.setOutlineVisible(false);
        plot.setLabelFont(AdministradorTema.fuentePequeña());
        plot.setLabelBackgroundPaint(PaletaColores.TARJETA);
        plot.setLabelOutlinePaint(null);
        plot.setShadowPaint(null);
        plot.setSimpleLabels(true);

        chart.getTitle().setFont(AdministradorTema.fuenteMedianaNegrita());
        chart.setBackgroundPaint(null);

        return empacar(chart);
    }

    // ==========================================================
    // DONUT — igual que la circular, pero con hueco en el centro
    // (ej. "Pedidos por Estado" en el boceto del Dashboard)
    // ==========================================================

    public static ChartPanel crearGraficaDonut(String titulo, Map<String, ? extends Number> datos) {

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        for (Map.Entry<String, ? extends Number> entrada : datos.entrySet()) {
            dataset.setValue(entrada.getKey(), entrada.getValue());
        }

        JFreeChart chart = ChartFactory.createRingChart(titulo, dataset, false, true, false);

        org.jfree.chart.plot.RingPlot plot = (org.jfree.chart.plot.RingPlot) chart.getPlot();

        int i = 0;
        for (Object clave : dataset.getKeys()) {
            plot.setSectionPaint((String) clave, PALETA[i % PALETA.length]);
            i++;
        }

        plot.setBackgroundPaint(null);
        plot.setOutlineVisible(false);
        plot.setLabelFont(AdministradorTema.fuentePequeña());
        plot.setLabelBackgroundPaint(PaletaColores.TARJETA);
        plot.setLabelOutlinePaint(null);
        plot.setShadowPaint(null);
        plot.setSimpleLabels(true);
        plot.setSectionDepth(0.35); // grosor del anillo — más chico = anillo más delgado
        plot.setSeparatorsVisible(false);

        chart.getTitle().setFont(AdministradorTema.fuenteMedianaNegrita());
        chart.setBackgroundPaint(null);

        return empacar(chart);
    }

    // ==========================================================
    // ESTILO COMPARTIDO
    // ==========================================================

    private static void aplicarEstiloPlot(CategoryPlot plot) {

        Font fuenteEjes = AdministradorTema.fuentePequeña();

        CategoryAxis ejeX = plot.getDomainAxis();
        ejeX.setTickLabelFont(fuenteEjes);
        ejeX.setLabelFont(fuenteEjes);

        NumberAxis ejeY = (NumberAxis) plot.getRangeAxis();
        ejeY.setTickLabelFont(fuenteEjes);
        ejeY.setLabelFont(fuenteEjes);

        plot.setBackgroundPaint(null);
        plot.setOutlineVisible(false);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinePaint(PaletaColores.BORDE);

        plot.getRenderer().setDefaultItemLabelFont(fuenteEjes);
    }

    private static ChartPanel empacar(JFreeChart chart) {

        chart.setBackgroundPaint(null);
        chart.getTitle().setFont(AdministradorTema.fuenteMedianaNegrita());
        chart.removeLegend();

        ChartPanel panel = new ChartPanel(chart);
        panel.setOpaque(false);
        panel.setMouseWheelEnabled(false);
        panel.setPopupMenu(null);

        return panel;
    }

}
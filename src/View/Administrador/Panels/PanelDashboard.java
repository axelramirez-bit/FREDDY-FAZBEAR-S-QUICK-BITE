package View.Administrador.Panels;

import Model.EstadoPedido;
import Service.Implement.DashboardServiceImpl;
import Service.Implement.PedidoServiceImpl;
import Service.Implement.ProductoServiceImpl;
import Service.Interfaz.IDashboardService;
import Service.Interfaz.IProductoService;
import View.Componentes.EtiquetaEstado;
import View.Componentes.PanelFondo;
import View.Componentes.TarjetaKPI;
import View.Utils.AdministradorTema;
import View.Utils.FabricaBotones;
import View.Utils.FabricaEtiquetas;
import View.Utils.FabricaGraficas;
import View.Utils.FabricaIconos;
import View.Utils.FabricaTablas;
import View.Utils.UIConstants;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Dashboard del Administrador — interfaz 1 del boceto.
 *
 * Toda la información viene de IDashboardService (ya implementado
 * en DashboardServiceImpl) y de IProductoService, esta clase NO
 * calcula nada de negocio, solo arma la vista con lo que esos
 * Services ya devuelven.
 *
 * BLOQUEADOR CONOCIDO (no es un bug de este archivo): mientras
 * PedidoDAOImpl.listar() no rellene Pedido.detalles con su JOIN a
 * detalle_pedido/producto/categoria, estas tres secciones se ven
 * en 0 o vacías aun con pedidos reales en la base de datos:
 *   - "Ventas por Categoría" (no aplica en este dashboard, pero
 *     sí en Ventas/Reportes)
 *   - "Top 5 Productos más vendidos"
 *   - Cualquier total que dependa de detalle_pedido.subtotal
 * "Ventas del día", "Pedidos del día" y "Pedidos por Estado" SÍ
 * funcionan ya, porque solo dependen de la tabla pedido misma.
 * ===============================================================
 */
public class PanelDashboard extends PanelFondo {

    private final IDashboardService dashboardService =
            new DashboardServiceImpl(new PedidoServiceImpl());

    private final IProductoService productoService = new ProductoServiceImpl();

    private static final DecimalFormat FORMATO_Q = new DecimalFormat("Q#,##0.00");

    // ---- tarjetas KPI (se guardan para poder refrescar sin recrear todo) ----
    private TarjetaKPI tarjetaVentasHoy;
    private TarjetaKPI tarjetaPedidosHoy;
    private TarjetaKPI tarjetaStockBajo;
    private TarjetaKPI tarjetaAlertas;

    private JPanel panelGraficas;
    private JPanel panelInferior;

    public PanelDashboard() {

        super();

        setOpaque(false);
        setLayout(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));

        JLabel titulo = FabricaEtiquetas.crearTitulo("DASHBOARD");
        titulo.setForeground(AdministradorTema.colorTexto());
        titulo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, UIConstants.ESPACIO_SUBTITULO, 0));

        JPanel norte = new JPanel(new BorderLayout());
        norte.setOpaque(false);
        norte.add(titulo, BorderLayout.NORTH);
        norte.add(crearFilaKPI(), BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));
        centro.setOpaque(false);

        panelGraficas = new JPanel(new GridLayout(1, 2, UIConstants.ESPACIO_SUBTITULO, 0));
        panelGraficas.setOpaque(false);
        centro.add(panelGraficas, BorderLayout.NORTH);

        panelInferior = new JPanel(new GridLayout(1, 2, UIConstants.ESPACIO_SUBTITULO, 0));
        panelInferior.setOpaque(false);
        centro.add(panelInferior, BorderLayout.CENTER);

        add(centro, BorderLayout.CENTER);

        cargarDatos();
    }

    // ==========================================================
    // FILA DE TARJETAS KPI
    // ==========================================================

    private JPanel crearFilaKPI() {

        JPanel fila = new JPanel(new GridLayout(1, 4, UIConstants.ESPACIO_SUBTITULO, 0));
        fila.setOpaque(false);

        tarjetaVentasHoy = new TarjetaKPI(
                FabricaIconos.dashboard(), "Ventas del día", "Q0.00", "");

        tarjetaPedidosHoy = new TarjetaKPI(
                FabricaIconos.pedidos(), "Pedidos del día", "0", "");

        tarjetaStockBajo = new TarjetaKPI(
                FabricaIconos.productos(), "Stock bajo", "0", "Productos");

        tarjetaAlertas = new TarjetaKPI(
                FabricaIconos.pedidosPendientes(), "Alertas críticas", "0", "Requieren atención");

        fila.add(tarjetaVentasHoy);
        fila.add(tarjetaPedidosHoy);
        fila.add(tarjetaStockBajo);
        fila.add(tarjetaAlertas);

        return fila;
    }

    // ==========================================================
    // CARGA / REFRESCO DE DATOS
    // ==========================================================

    /**
     * Público para que se pueda refrescar el dashboard al volver
     * a esta pestaña, sin recrear el panel completo.
     */
    public void cargarDatos() {

        IDashboardService.ResumenDashboard resumen = dashboardService.obtenerResumen();

        tarjetaVentasHoy.actualizar(FORMATO_Q.format(resumen.ventasHoy), "");
        tarjetaPedidosHoy.actualizar(String.valueOf(resumen.totalPedidos), "");

        int stockBajo = contarProductosConStockBajo();
        tarjetaStockBajo.actualizar(String.valueOf(stockBajo), "Productos");

        int alertas = stockBajo + resumen.pedidosPendientes;
        tarjetaAlertas.actualizar(String.valueOf(alertas), "Requieren atención");

        if (alertas > 0) {
            tarjetaAlertas.colorDetalle(AdministradorTema.colorEstadoPeligro());
        }

        panelGraficas.removeAll();
        panelGraficas.add(crearTarjetaGraficaLineas());
        panelGraficas.add(crearTarjetaGraficaDonut());
        panelGraficas.revalidate();
        panelGraficas.repaint();

        panelInferior.removeAll();
        panelInferior.add(crearTarjetaTopProductos());
        panelInferior.add(crearTarjetaAlertasCriticas());
        panelInferior.revalidate();
        panelInferior.repaint();
    }

    /**
     * "Stock bajo" en el boceto no tiene todavía una regla oficial
     * (falta la columna producto.stock_minimo — ver conversación
     * de arquitectura). Mientras esa columna no exista, se usa un
     * umbral fijo de 10 unidades, IGUAL al que ya se usaba en
     * Inventario, para no inventar dos criterios distintos.
     */
    private int contarProductosConStockBajo() {

        int contador = 0;

        for (Model.Producto producto : productoService.listarProductos()) {
            if (producto.getStock() > 0 && producto.getStock() < 10) {
                contador++;
            }
        }

        return contador;
    }

    // ==========================================================
    // GRÁFICA DE LÍNEA — VENTAS ÚLTIMOS 7 DÍAS
    // ==========================================================

    private JPanel crearTarjetaGraficaLineas() {

        JPanel tarjeta = FabricaTablas.crearTarjeta(new BorderLayout());

        tarjeta.add(
                FabricaEtiquetas.crearSubtitulo("Ventas (últimos 7 días)"),
                BorderLayout.NORTH);

        Map<String, BigDecimal> ventasPorDia = dashboardService.ventasPorDia(7);

        tarjeta.add(
                FabricaGraficas.crearGraficaLineas(null, "Ventas (Q)", ventasPorDia),
                BorderLayout.CENTER);

        return tarjeta;
    }

    // ==========================================================
    // DONUT — PEDIDOS POR ESTADO
    // ==========================================================

    private JPanel crearTarjetaGraficaDonut() {

        JPanel tarjeta = FabricaTablas.crearTarjeta(new BorderLayout());

        tarjeta.add(
                FabricaEtiquetas.crearSubtitulo("Pedidos por Estado"),
                BorderLayout.NORTH);

        Map<EstadoPedido, Long> conteos = dashboardService.pedidosPorEstado();

        java.util.Map<String, Long> datos = new java.util.LinkedHashMap<>();
        for (Map.Entry<EstadoPedido, Long> entrada : conteos.entrySet()) {
            datos.put(capitalizar(entrada.getKey().name()), entrada.getValue());
        }

        tarjeta.add(
                FabricaGraficas.crearGraficaDonut(null, datos),
                BorderLayout.CENTER);

        return tarjeta;
    }

    // ==========================================================
    // TOP 5 PRODUCTOS MÁS VENDIDOS
    // ==========================================================

    private JPanel crearTarjetaTopProductos() {

        JPanel tarjeta = FabricaTablas.crearTarjeta(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));

        tarjeta.add(
                FabricaEtiquetas.crearSubtitulo("Top 5 Productos más vendidos"),
                BorderLayout.NORTH);

        Map<String, Integer> topVendidos = dashboardService.productosMasVendidos(5);
        Map<String, BigDecimal> ingresos = dashboardService.ingresosPorProducto();

        Object[] columnas = {"#", "Producto", "Vendidos", "Ingresos"};

        DefaultTableModel modelo = FabricaTablas.crearModeloSoloLectura(columnas);

        int posicion = 1;
        for (Map.Entry<String, Integer> entrada : topVendidos.entrySet()) {

            String nombreProducto = entrada.getKey();
            BigDecimal ingreso = ingresos.getOrDefault(nombreProducto, BigDecimal.ZERO);

            modelo.addRow(new Object[]{
                    posicion,
                    nombreProducto,
                    entrada.getValue(),
                    FORMATO_Q.format(ingreso)
            });

            posicion++;
        }

        JTable tabla = FabricaTablas.crearTabla(modelo);

        tarjeta.add(FabricaTablas.crearPanelTabla(tabla), BorderLayout.CENTER);

        return tarjeta;
    }

    // ==========================================================
    // ALERTAS CRÍTICAS
    // ==========================================================

    private JPanel crearTarjetaAlertasCriticas() {

        JPanel tarjeta = FabricaTablas.crearTarjeta(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));

        tarjeta.add(
                FabricaEtiquetas.crearSubtitulo("Alertas críticas"),
                BorderLayout.NORTH);

        JPanel listaAlertas = new JPanel();
        listaAlertas.setOpaque(false);
        listaAlertas.setLayout(new javax.swing.BoxLayout(listaAlertas, javax.swing.BoxLayout.Y_AXIS));

        int stockBajo = contarProductosConStockBajo();

        if (stockBajo > 0) {
            listaAlertas.add(crearFilaAlerta(
                    stockBajo + " productos con stock bajo",
                    "Revisión recomendada",
                    EtiquetaEstado.advertencia("!")));
        }

        long pendientes = dashboardService.pedidosPorEstado().getOrDefault(EstadoPedido.PENDIENTE, 0L);

        if (pendientes > 0) {
            listaAlertas.add(crearFilaAlerta(
                    pendientes + " pedidos pendientes",
                    "Revisa la cola de Pedidos",
                    EtiquetaEstado.peligro("!")));
        }

        if (stockBajo == 0 && pendientes == 0) {
            listaAlertas.add(FabricaEtiquetas.crearTexto("Sin alertas por ahora."));
        }

        tarjeta.add(listaAlertas, BorderLayout.CENTER);

        JButton btnVerTodas = FabricaBotones.crearSecundario("Ver todas las alertas");
        tarjeta.add(btnVerTodas, BorderLayout.SOUTH);

        return tarjeta;
    }

    private JPanel crearFilaAlerta(String titulo, String detalle, EtiquetaEstado icono) {

        JPanel fila = new JPanel(new BorderLayout(UIConstants.ESPACIO_SUBTITULO, 0));
        fila.setOpaque(false);
        fila.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 0, 4, 0));

        fila.add(icono, BorderLayout.WEST);

        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setOpaque(false);
        textos.add(FabricaEtiquetas.crearTexto(titulo));
        textos.add(FabricaEtiquetas.crearPequeño(detalle));

        fila.add(textos, BorderLayout.CENTER);

        return fila;
    }

    // ==========================================================
    // UTILIDAD
    // ==========================================================

    private String capitalizar(String texto) {

        if (texto == null || texto.isEmpty()) {
            return texto;
        }

        return texto.charAt(0) + texto.substring(1).toLowerCase();
    }

}
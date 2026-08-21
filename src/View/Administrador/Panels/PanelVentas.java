package View.Administrador.Panels;

import Controller.VentasController;
import Model.EstadoPedido;
import Service.Interfaz.IVentasService;
import View.Componentes.PanelFondo;
import View.Componentes.PanelRedondeado;
import View.Componentes.TarjetaKPI;
import View.Utils.AdministradorTema;
import View.Utils.FabricaBotones;
import View.Utils.FabricaCampos;
import View.Utils.FabricaEtiquetas;
import View.Utils.FabricaGraficas;
import View.Utils.FabricaTablas;
import View.Utils.UtilImagenes;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Panel VENTAS del Administrador. Antes era un PanelTemporal vacío
 * ("VENTAS" a secas); esta versión ya arma: 4 TarjetaKPI, la
 * gráfica de ventas del día y la tabla de historial, usando
 * VentasController -> IVentasService (no toca SQL ni JFreeChart
 * directamente, igual que el resto de paneles del proyecto).
 *
 * Lo que la maqueta original mostraba y esta primera versión NO
 * incluye todavía (ver QueFalta.md que te mandé aparte):
 *   - Filtro "Cajero" (no existe esa columna en la BD).
 *   - Paginación real de la tabla (aquí se listan todas las filas
 *     del rango filtrado; agregar LIMIT/OFFSET es el siguiente paso).
 *   - Botones "Ver detalle" / "Ver factura" por fila (dependen de
 *     un panel de detalle que aún no existe).
 * ===============================================================
 */
public class PanelVentas extends PanelFondo {

    private final VentasController controller = new VentasController();

    private JLabel lblVentasHoy;
    private JLabel lblPedidosHoy;
    private JLabel lblTicketPromedio;
    private JLabel lblProductosVendidos;

    private JPanel panelGrafica;
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;

    private LocalDate desde = LocalDate.now();
    private LocalDate hasta = LocalDate.now();

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public PanelVentas() {

        super();

        setLayout(new BorderLayout(0, AdministradorTema.espacioMediano()));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(construirEncabezado(), BorderLayout.NORTH);
        add(construirCentro(), BorderLayout.CENTER);

        cargarDatos();
    }

    // ==========================================================
    // ENCABEZADO: título + tarjetas KPI
    // ==========================================================

    private JPanel construirEncabezado() {

        JPanel contenedor = new JPanel(new BorderLayout(0, AdministradorTema.espacioMediano()));
        contenedor.setOpaque(false);

        JPanel filaTitulo = new JPanel(new BorderLayout());
        filaTitulo.setOpaque(false);

        JLabel titulo = FabricaEtiquetas.crearTitulo("VENTAS");
        titulo.setForeground(AdministradorTema.colorTexto());
        filaTitulo.add(titulo, BorderLayout.WEST);

        var btnActualizar = FabricaBotones.crearSecundario("Actualizar");
        btnActualizar.addActionListener(e -> cargarDatos());
        filaTitulo.add(btnActualizar, BorderLayout.EAST);

        contenedor.add(filaTitulo, BorderLayout.NORTH);
        contenedor.add(construirTarjetasKPI(), BorderLayout.CENTER);

        return contenedor;
    }

    private JPanel construirTarjetasKPI() {

        JPanel fila = new JPanel(new GridLayout(1, 4, AdministradorTema.espacioMediano(), 0));
        fila.setOpaque(false);

        lblVentasHoy = null; // las tarjetas guardan su propio JLabel interno

        TarjetaKPI tarjetaVentas = new TarjetaKPI(
                UtilImagenes.icono("Ventas", 24, 24),
                "Ventas de hoy", "Q0.00", "Total vendido hoy"
        );

        TarjetaKPI tarjetaPedidos = new TarjetaKPI(
                UtilImagenes.icono("Pedidos", 24, 24),
                "Pedidos de hoy", "0", "Total de pedidos"
        );

        TarjetaKPI tarjetaTicket = new TarjetaKPI(
                UtilImagenes.icono("Ticket", 24, 24),
                "Ticket promedio", "Q0.00", "Ventas / pedidos"
        );

        TarjetaKPI tarjetaProductos = new TarjetaKPI(
                UtilImagenes.icono("Productos", 24, 24),
                "Productos vendidos", "0", "Unidades vendidas"
        );

        this.tarjetaVentas = tarjetaVentas;
        this.tarjetaPedidos = tarjetaPedidos;
        this.tarjetaTicket = tarjetaTicket;
        this.tarjetaProductos = tarjetaProductos;

        fila.add(tarjetaVentas);
        fila.add(tarjetaPedidos);
        fila.add(tarjetaTicket);
        fila.add(tarjetaProductos);

        return fila;
    }

    // guardadas para poder llamar tarjeta.actualizar(...) desde cargarDatos()
    private TarjetaKPI tarjetaVentas;
    private TarjetaKPI tarjetaPedidos;
    private TarjetaKPI tarjetaTicket;
    private TarjetaKPI tarjetaProductos;

    // ==========================================================
    // CENTRO: gráfica + tabla de historial
    // ==========================================================

    private JPanel construirCentro() {

        JPanel contenedor = new JPanel(new BorderLayout(0, AdministradorTema.espacioMediano()));
        contenedor.setOpaque(false);

        panelGrafica = new PanelRedondeado();
        panelGrafica.setLayout(new BorderLayout());
        panelGrafica.setPreferredSize(new java.awt.Dimension(0, 260));

        contenedor.add(panelGrafica, BorderLayout.NORTH);
        contenedor.add(construirTabla(), BorderLayout.CENTER);

        return contenedor;
    }

    private JPanel construirTabla() {

        Object[] columnas = {
                "ID", "Fecha", "Cliente", "Total", "Método de pago", "Estado", "Productos"
        };

        modeloTabla = FabricaTablas.crearModeloSoloLectura(columnas);
        tablaHistorial = FabricaTablas.crearTabla(modeloTabla);

        JScrollPane scroll = FabricaTablas.crearScrollTabla(tablaHistorial);

        JPanel panel = FabricaTablas.crearPanelTabla(tablaHistorial);
        panel.removeAll();
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // ==========================================================
    // CARGA DE DATOS
    // ==========================================================

    private void cargarDatos() {

        IVentasService.ResumenVentas resumen = controller.obtenerResumen(desde, hasta);

        tarjetaVentas.actualizar(
                "Q" + resumen.totalVentas.setScale(2, java.math.RoundingMode.HALF_UP),
                "Total vendido hoy"
        );
        tarjetaPedidos.actualizar(String.valueOf(resumen.totalPedidos), "Total de pedidos");
        tarjetaTicket.actualizar(
                "Q" + resumen.ticketPromedio.setScale(2, java.math.RoundingMode.HALF_UP),
                "Ventas / pedidos"
        );
        tarjetaProductos.actualizar(String.valueOf(resumen.productosVendidos), "Unidades vendidas");

        actualizarGrafica();
        actualizarTabla();
    }

    private void actualizarGrafica() {

        panelGrafica.removeAll();

        var datos = controller.ventasPorHora(hasta);

        panelGrafica.add(
                FabricaGraficas.crearGraficaLineas("Ventas durante el día", "Ventas (Q)", datos),
                BorderLayout.CENTER
        );

        panelGrafica.revalidate();
        panelGrafica.repaint();
    }

    private void actualizarTabla() {

        modeloTabla.setRowCount(0);

        IVentasService.FiltroVentas filtro = new IVentasService.FiltroVentas(desde, hasta);

        List<IVentasService.VentaHistorial> historial = controller.historialVentas(filtro);

        for (IVentasService.VentaHistorial venta : historial) {

            modeloTabla.addRow(new Object[]{
                    venta.idPedido,
                    venta.fecha.format(FORMATO_FECHA),
                    venta.cliente,
                    "Q" + venta.total,
                    venta.metodoPago != null ? capitalizar(venta.metodoPago.name()) : "-",
                    capitalizar(venta.estado.name()),
                    venta.cantidadProductos
            });
        }
    }

    private String capitalizar(String texto) {
        if (texto == null || texto.isBlank()) return texto;
        return texto.charAt(0) + texto.substring(1).toLowerCase();
    }
}
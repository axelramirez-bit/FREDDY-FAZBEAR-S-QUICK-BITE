package View.Administrador.Panels;

import Model.EstadoPago;
import Model.MetodoPago;
import Model.Pago;
import Service.Implement.PagoServiceImpl;
import Service.Interfaz.IPagoService;
import View.Componentes.BarraBusqueda;
import View.Componentes.PanelFondo;
import View.Componentes.TarjetaKPI;
import View.Utils.FabricaCampos;
import View.Utils.FabricaEtiquetas;
import View.Utils.FabricaGraficas;
import View.Utils.FabricaIconos;
import View.Utils.FabricaTablas;
import View.Utils.RenderizadorEstado;
import View.Utils.UIConstants;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Gestión de Pagos — interfaz 8 del boceto: KPIs del día + donut
 * "Por Método de Pago" + tabla filtrable por estado.
 *
 * pago.estado real (EstadoPago) es PENDIENTE/PAGADO/RECHAZADO —
 * la tabla usa exactamente esos 3 textos, no "Completado" (ese
 * valor no existe en el ENUM real, ya se corrigió en el boceto).
 * ===============================================================
 */
public class PanelPagos extends PanelFondo {

    private final IPagoService pagoService = new PagoServiceImpl();

    private static final int COLUMNA_ESTADO = 5;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DecimalFormat FORMATO_Q = new DecimalFormat("Q#,##0.00");

    private List<Pago> todosLosPagos = new ArrayList<>();
    private List<Pago> pagosFiltrados = new ArrayList<>();

    private TarjetaKPI tarjetaCobradoHoy;
    private TarjetaKPI tarjetaPendientes;
    private TarjetaKPI tarjetaPagadosHoy;

    private JPanel panelDonut;

    private BarraBusqueda barraBusqueda;
    private JComboBox<String> comboEstado;

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    public PanelPagos() {

        super();

        setOpaque(false);
        setLayout(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));

        JPanel norte = new JPanel(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));
        norte.setOpaque(false);
        norte.add(FabricaEtiquetas.crearTitulo("PAGOS"), BorderLayout.NORTH);
        norte.add(crearFilaKPI(), BorderLayout.CENTER);
        norte.add(crearBarraAcciones(), BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));
        centro.setOpaque(false);

        panelDonut = FabricaTablas.crearTarjeta(new BorderLayout());
        panelDonut.setPreferredSize(new java.awt.Dimension(320, 260));
        centro.add(panelDonut, BorderLayout.WEST);
        centro.add(crearPanelTabla(), BorderLayout.CENTER);

        add(centro, BorderLayout.CENTER);

        cargarDatos();
    }

    private JPanel crearFilaKPI() {

        JPanel fila = new JPanel(new GridLayout(1, 3, UIConstants.ESPACIO_SUBTITULO, 0));
        fila.setOpaque(false);
        fila.setBorder(javax.swing.BorderFactory.createEmptyBorder(UIConstants.ESPACIO_SUBTITULO, 0, 0, 0));

        tarjetaCobradoHoy = new TarjetaKPI(FabricaIconos.pagos(), "Total Cobrado Hoy", "Q0.00", "");
        tarjetaPendientes = new TarjetaKPI(FabricaIconos.pagos(), "Pagos Pendientes", "0", "");
        tarjetaPagadosHoy = new TarjetaKPI(FabricaIconos.pagos(), "Pagos Pagados Hoy", "0", "");

        fila.add(tarjetaCobradoHoy);
        fila.add(tarjetaPendientes);
        fila.add(tarjetaPagadosHoy);

        return fila;
    }

    private JPanel crearBarraAcciones() {

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, UIConstants.ESPACIO_SUBTITULO, 0));
        barra.setOpaque(false);

        barraBusqueda = new BarraBusqueda("Buscar pago...");
        barraBusqueda.getCampo().getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {
                    @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
                    @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
                    @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
                });

        comboEstado = FabricaCampos.crearCombo();
        comboEstado.addItem("Estado: Todos");
        for (EstadoPago estado : EstadoPago.values()) {
            comboEstado.addItem(capitalizar(estado.name()));
        }
        comboEstado.addActionListener(e -> aplicarFiltros());

        barra.add(barraBusqueda);
        barra.add(comboEstado);

        return barra;
    }

    private JPanel crearPanelTabla() {

        Object[] columnas = {"ID", "Fecha", "Pedido", "Método", "Monto", "Estado"};

        modeloTabla = FabricaTablas.crearModeloSoloLectura(columnas);
        tabla = FabricaTablas.crearTabla(modeloTabla);

        tabla.getColumnModel().getColumn(COLUMNA_ESTADO).setCellRenderer(new RenderizadorEstado());

        return FabricaTablas.crearPanelTabla(tabla);
    }

    public void cargarDatos() {

        try {
            todosLosPagos = pagoService.listar();
        } catch (IllegalArgumentException ex) {
            // Mismo caso que PanelPedidos: PagoDAOImpl.listar() traduce
            // estado/método de pago de la BD a enum, y un valor que no
            // coincida con ninguna opción conocida revienta en vez de
            // devolver una lista vacía. Se avisa la causa real (dato
            // inconsistente) en vez de un error de conexión genérico.
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los pagos: hay un valor inesperado en la base de datos ("
                            + ex.getMessage() + "). Repórtalo al equipo de desarrollo.",
                    "Datos inconsistentes", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los pagos. Verifica tu conexión e inténtalo de nuevo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate hoy = LocalDate.now();

        BigDecimal cobradoHoy = BigDecimal.ZERO;
        int pendientes = 0;
        int pagadosHoy = 0;

        Map<String, BigDecimal> porMetodo = new LinkedHashMap<>();
        porMetodo.put("Efectivo", BigDecimal.ZERO);
        porMetodo.put("Tarjeta", BigDecimal.ZERO);
        porMetodo.put("Transferencia", BigDecimal.ZERO);

        for (Pago pago : todosLosPagos) {

            boolean esDeHoy = pago.getFechaPago() != null && pago.getFechaPago().toLocalDate().isEqual(hoy);

            if (pago.getEstado() == EstadoPago.PENDIENTE) {
                pendientes++;
            }

            if (pago.getEstado() == EstadoPago.PAGADO && esDeHoy) {

                pagadosHoy++;
                cobradoHoy = cobradoHoy.add(pago.getMonto());

                String nombreMetodo = capitalizar(pago.getMetodoPago().name());
                porMetodo.put(nombreMetodo, porMetodo.get(nombreMetodo).add(pago.getMonto()));
            }
        }

        tarjetaCobradoHoy.actualizar(FORMATO_Q.format(cobradoHoy), "");
        tarjetaPendientes.actualizar(String.valueOf(pendientes), "");
        tarjetaPagadosHoy.actualizar(String.valueOf(pagadosHoy), "");

        panelDonut.removeAll();
        panelDonut.add(FabricaEtiquetas.crearSubtitulo("Por Método de Pago (Hoy)"), BorderLayout.NORTH);
        panelDonut.add(FabricaGraficas.crearGraficaDonut(null, porMetodo), BorderLayout.CENTER);
        panelDonut.revalidate();
        panelDonut.repaint();

        aplicarFiltros();
    }

    private void aplicarFiltros() {

        String texto = barraBusqueda == null ? "" : barraBusqueda.getTexto().toLowerCase();

        String estadoSeleccionado =
                (comboEstado == null || comboEstado.getSelectedIndex() <= 0)
                        ? null
                        : (String) comboEstado.getSelectedItem();

        pagosFiltrados = new ArrayList<>();

        for (Pago pago : todosLosPagos) {

            String idPedidoTexto = pago.getPedido() == null ? "" : String.valueOf(pago.getPedido().getIdPedido());

            boolean pasaBusqueda = texto.isEmpty()
                    || String.valueOf(pago.getIdPago()).contains(texto)
                    || idPedidoTexto.contains(texto);

            boolean pasaEstado = estadoSeleccionado == null
                    || capitalizar(pago.getEstado().name()).equalsIgnoreCase(estadoSeleccionado);

            if (pasaBusqueda && pasaEstado) {
                pagosFiltrados.add(pago);
            }
        }

        repintarTabla();
    }

    private void repintarTabla() {

        modeloTabla.setRowCount(0);

        for (Pago pago : pagosFiltrados) {

            modeloTabla.addRow(new Object[]{
                    pago.getIdPago(),
                    pago.getFechaPago() == null ? "-" : pago.getFechaPago().format(FORMATO_FECHA),
                    pago.getPedido() == null ? "-" : "#" + pago.getPedido().getIdPedido(),
                    capitalizar(pago.getMetodoPago().name()),
                    FORMATO_Q.format(pago.getMonto()),
                    capitalizar(pago.getEstado().name())
            });
        }
    }

    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }
        return texto.charAt(0) + texto.substring(1).toLowerCase();
    }

}
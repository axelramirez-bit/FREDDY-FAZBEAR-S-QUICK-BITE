package View.PedidosProceso.Panels;

import Model.EstadoPedido;
import Model.MetodoPago;
import Model.Pago;
import Model.Pedido;
import Model.TipoEntrega;
import Service.Implement.PagoServiceImpl;
import Service.Implement.PedidoServiceImpl;
import Service.Interfaz.IPagoService;
import Service.Interfaz.IPedidoService;
import View.Componentes.BarraBusqueda;
import View.Componentes.ColumnaAccionTabla;
import View.Componentes.DialogoDetallePedido;
import View.Componentes.PanelFondo;
import View.Componentes.Refrescable;
import View.Utils.AdministradorTema;
import View.Utils.FabricaBotones;
import View.Utils.FabricaCampos;
import View.Utils.FabricaEtiquetas;
import View.Utils.FabricaTablas;
import View.Utils.PaletaColores;
import View.Utils.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Pantalla 5 del mockup: historial de pedidos ya atendidos
 * (ENTREGADO o CANCELADO), con filtros de fecha, estado, método de
 * pago y búsqueda, más paginación simple.
 *
 * A propósito NO tiene botón de exportar (PDF/Excel): en el
 * diagrama de casos de uso, "Generar/exportar reporte de ventas"
 * es exclusivo del Administrador (caso 7). Que el Trabajador no lo
 * tenga aquí es coherencia con ese diseño, no un olvido.
 * ===============================================================
 */
public class PanelHistorial extends PanelFondo implements Refrescable {

    private static final int TAMANO_PAGINA = 5;
    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");

    private final IPedidoService pedidoService = new PedidoServiceImpl();
    private final IPagoService pagoService = new PagoServiceImpl();

    private JSpinner spinnerFechaInicio;
    private JSpinner spinnerFechaFin;
    private JComboBox<String> comboEstado;
    private JComboBox<String> comboMetodoPago;
    private BarraBusqueda barraBusqueda;

    private DefaultTableModel modeloTabla;
    private JLabel lblResumenPagina;
    private JButton btnAnterior;
    private JButton btnSiguiente;

    private List<Pedido> resultadosFiltrados = new ArrayList<>();
    private List<Pedido> filasPaginaActual = new ArrayList<>();
    private int paginaActual = 0;

    public PanelHistorial() {

        super();

        setLayout(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));

        setBorder(BorderFactory.createEmptyBorder(
                AdministradorTema.espacioGrande(), AdministradorTema.espacioGrande(),
                AdministradorTema.espacioGrande(), AdministradorTema.espacioGrande()));

        add(crearBarraFiltros(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPiePaginacion(), BorderLayout.SOUTH);

        cargarDatos();
    }

    // ==========================================================
    // FILTROS
    // ==========================================================
    private JPanel crearBarraFiltros() {

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, AdministradorTema.espacioMediano(), 0));
        barra.setOpaque(false);

        spinnerFechaInicio = crearSpinnerFecha(LocalDate.now().minusDays(7));
        spinnerFechaFin = crearSpinnerFecha(LocalDate.now());

        comboEstado = FabricaCampos.crearCombo();
        comboEstado.setModel(new DefaultComboBoxModel<>(
                new String[]{"Todos", "Entregado", "Cancelado"}));

        comboMetodoPago = FabricaCampos.crearCombo();
        comboMetodoPago.setModel(new DefaultComboBoxModel<>(
                new String[]{"Todos", "Efectivo", "Tarjeta", "Transferencia"}));

        barraBusqueda = new BarraBusqueda("Buscar pedido o cliente...");
        barraBusqueda.agregarListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override public void removeUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override public void changedUpdate(DocumentEvent e) { aplicarFiltros(); }
        });

        spinnerFechaInicio.addChangeListener(e -> aplicarFiltros());
        spinnerFechaFin.addChangeListener(e -> aplicarFiltros());
        comboEstado.addActionListener(e -> aplicarFiltros());
        comboMetodoPago.addActionListener(e -> aplicarFiltros());

        JButton btnRefrescar = FabricaBotones.crearSecundario("↻");
        btnRefrescar.addActionListener(e -> cargarDatos());

        barra.add(FabricaEtiquetas.crearTexto("Fecha inicio:"));
        barra.add(spinnerFechaInicio);
        barra.add(FabricaEtiquetas.crearTexto("Fecha fin:"));
        barra.add(spinnerFechaFin);
        barra.add(FabricaEtiquetas.crearTexto("Estado:"));
        barra.add(comboEstado);
        barra.add(FabricaEtiquetas.crearTexto("Método de pago:"));
        barra.add(comboMetodoPago);
        barra.add(barraBusqueda);
        barra.add(btnRefrescar);

        return barra;
    }

    private JSpinner crearSpinnerFecha(LocalDate valorInicial) {

        SpinnerDateModel modelo = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(modelo);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));

        Date fecha = Date.from(valorInicial.atStartOfDay(ZoneId.systemDefault()).toInstant());
        spinner.setValue(fecha);

        return spinner;
    }

    // ==========================================================
    // TABLA
    // ==========================================================
    // La columna "Acción" (índice 7) es la única editable: es lo
    // que activa el CellEditor de ColumnaAccionTabla. Un modelo
    // creado con FabricaTablas.crearModeloSoloLectura() forzaría
    // isCellEditable(...) = false en TODAS las celdas, y entonces
    // JTable nunca dispara el editor — el botón "Ver detalle" se
    // vería pero un clic no haría nada.
    private static final int COLUMNA_ACCION = 7;

    private JPanel crearPanelTabla() {

        Object[] columnas = {
                "Pedido", "Cliente", "Fecha", "Estado", "Tipo de entrega", "Método de pago", "Total", "Acción"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return columna == COLUMNA_ACCION;
            }
        };

        var tabla = FabricaTablas.crearTabla(modeloTabla);

        ColumnaAccionTabla.instalar(
                tabla, COLUMNA_ACCION, "Ver detalle", PaletaColores.SECUNDARIO,
                fila -> mostrarDetalle(filasPaginaActual.get(fila))
        );

        return FabricaTablas.crearPanelTabla(tabla);
    }

    private void mostrarDetalle(Pedido pedido) {
        Pago pago = pagoService.buscarPorPedido(pedido.getIdPedido());
        DialogoDetallePedido.mostrar(this, pedido, pago);
    }

    // ==========================================================
    // PIE — PAGINACIÓN
    // ==========================================================
    private JPanel crearPiePaginacion() {

        JPanel pie = new JPanel(new BorderLayout());
        pie.setOpaque(false);

        lblResumenPagina = FabricaEtiquetas.crearPequeño("Mostrando 0 de 0 pedidos");

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, AdministradorTema.espacioPequeño(), 0));
        botones.setOpaque(false);

        btnAnterior = FabricaBotones.crearSecundario("<");
        btnSiguiente = FabricaBotones.crearSecundario(">");

        btnAnterior.addActionListener(e -> {
            if (paginaActual > 0) {
                paginaActual--;
                repintarTabla();
            }
        });

        btnSiguiente.addActionListener(e -> {
            int totalPaginas = calcularTotalPaginas();
            if (paginaActual < totalPaginas - 1) {
                paginaActual++;
                repintarTabla();
            }
        });

        botones.add(btnAnterior);
        botones.add(btnSiguiente);

        pie.add(lblResumenPagina, BorderLayout.WEST);
        pie.add(botones, BorderLayout.EAST);

        return pie;
    }

    // ==========================================================
    // CARGA + FILTROS
    // ==========================================================
    private List<Pedido> historialCompleto = new ArrayList<>();

    public void cargarDatos() {

        List<Pedido> todos = pedidoService.listarPedidos();

        historialCompleto = new ArrayList<>();
        for (Pedido p : todos) {
            if (p.getEstado() == EstadoPedido.ENTREGADO || p.getEstado() == EstadoPedido.CANCELADO) {
                historialCompleto.add(p);
            }
        }

        paginaActual = 0;
        aplicarFiltros();
    }

    private void aplicarFiltros() {

        LocalDate fechaInicio = convertir((Date) spinnerFechaInicio.getValue());
        LocalDate fechaFin = convertir((Date) spinnerFechaFin.getValue());
        String estadoSeleccionado = (String) comboEstado.getSelectedItem();
        String metodoSeleccionado = (String) comboMetodoPago.getSelectedItem();
        String texto = barraBusqueda.getTexto().trim().toLowerCase();

        List<Pedido> resultado = new ArrayList<>();

        for (Pedido pedido : historialCompleto) {

            if (pedido.getFecha() == null) {
                continue;
            }

            LocalDate fechaPedido = pedido.getFecha().toLocalDate();

            if (fechaPedido.isBefore(fechaInicio) || fechaPedido.isAfter(fechaFin)) {
                continue;
            }

            if (estadoSeleccionado != null && !"Todos".equals(estadoSeleccionado)
                    && !estadoSeleccionado.equals(nombreLegible(pedido.getEstado()))) {
                continue;
            }

            if (metodoSeleccionado != null && !"Todos".equals(metodoSeleccionado)) {

                Pago pago = pagoService.buscarPorPedido(pedido.getIdPedido());
                String metodoPedido = pago != null ? nombreLegible(pago.getMetodoPago()) : "";

                if (!metodoSeleccionado.equals(metodoPedido)) {
                    continue;
                }
            }

            String cliente = pedido.getUsuario() != null ? pedido.getUsuario().getNombreCompleto() : "";
            boolean coincideTexto = texto.isEmpty()
                    || ("#" + pedido.getIdPedido()).contains(texto)
                    || cliente.toLowerCase().contains(texto);

            if (!coincideTexto) {
                continue;
            }

            resultado.add(pedido);
        }

        resultado.sort(Comparator.comparing(Pedido::getFecha).reversed());

        resultadosFiltrados = resultado;
        paginaActual = 0;

        repintarTabla();
    }

    private void repintarTabla() {

        int totalPaginas = calcularTotalPaginas();

        int desde = paginaActual * TAMANO_PAGINA;
        int hasta = Math.min(desde + TAMANO_PAGINA, resultadosFiltrados.size());

        filasPaginaActual = desde < hasta
                ? resultadosFiltrados.subList(desde, hasta)
                : new ArrayList<>();

        modeloTabla.setRowCount(0);

        for (Pedido pedido : filasPaginaActual) {

            Pago pago = pagoService.buscarPorPedido(pedido.getIdPedido());

            modeloTabla.addRow(new Object[]{
                    "#" + pedido.getIdPedido(),
                    pedido.getUsuario() != null ? pedido.getUsuario().getNombreCompleto() : "-",
                    pedido.getFecha() != null ? pedido.getFecha().format(FORMATO_FECHA_HORA) : "-",
                    nombreLegible(pedido.getEstado()),
                    nombreLegible(pedido.getTipoEntrega()),
                    pago != null ? nombreLegible(pago.getMetodoPago()) : "-",
                    "Q" + pedido.getTotal(),
                    "Ver detalle"
            });
        }

        lblResumenPagina.setText(
                resultadosFiltrados.isEmpty()
                        ? "Sin pedidos para los filtros seleccionados"
                        : "Mostrando " + (desde + 1) + " a " + hasta + " de " + resultadosFiltrados.size()
                          + " pedidos (página " + (paginaActual + 1) + " de " + totalPaginas + ")"
        );

        btnAnterior.setEnabled(paginaActual > 0);
        btnSiguiente.setEnabled(paginaActual < totalPaginas - 1);
    }

    private int calcularTotalPaginas() {
        return Math.max(1, (int) Math.ceil(resultadosFiltrados.size() / (double) TAMANO_PAGINA));
    }

    // ==========================================================
    // UTILITARIOS
    // ==========================================================
    private LocalDate convertir(Date fecha) {
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private String nombreLegible(EstadoPedido estado) {
        if (estado == null) return "-";
        switch (estado) {
            case PENDIENTE: return "Pendiente";
            case PREPARACION: return "En preparación";
            case LISTO: return "Listo";
            case ENTREGADO: return "Entregado";
            case CANCELADO: return "Cancelado";
            default: return estado.name();
        }
    }

    private String nombreLegible(TipoEntrega tipo) {
        if (tipo == null) return "-";
        return tipo == TipoEntrega.PARA_LLEVAR ? "Para llevar" : "Comer en local";
    }

    private String nombreLegible(MetodoPago metodo) {
        if (metodo == null) return "-";
        switch (metodo) {
            case EFECTIVO: return "Efectivo";
            case TARJETA: return "Tarjeta";
            case TRANSFERENCIA: return "Transferencia";
            default: return metodo.name();
        }
    }
}
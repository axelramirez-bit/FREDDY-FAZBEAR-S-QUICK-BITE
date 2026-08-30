package View.Administrador.Panels;

import Model.EstadoPedido;
import Model.Pedido;
import Service.Implement.PedidoServiceImpl;
import Service.Interfaz.IPedidoService;
import View.Componentes.BarraBusqueda;
import View.Componentes.PanelFondo;
import View.Componentes.TarjetaKPI;
import View.Utils.FabricaCampos;
import View.Utils.FabricaEtiquetas;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Gestión de Pedidos — interfaz 7 del boceto: 5 tarjetas, una por
 * cada valor del ENUM pedido.estado, + tabla filtrable.
 *
 * Esta pantalla NO tiene botón "Agregar" — un pedido nuevo se
 * origina desde el flujo de autoservicio/cliente, no desde el
 * panel de Administrador. Aquí solo se consulta y se avanza el
 * estado (doble clic en una fila).
 * ===============================================================
 */
public class PanelPedidos extends PanelFondo {

    private final IPedidoService pedidoService = new PedidoServiceImpl();

    private static final int COLUMNA_ESTADO = 5;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private List<Pedido> todosLosPedidos = new ArrayList<>();
    private List<Pedido> pedidosFiltrados = new ArrayList<>();

    private TarjetaKPI tarjetaPendientes;
    private TarjetaKPI tarjetaEnPreparacion;
    private TarjetaKPI tarjetaListos;
    private TarjetaKPI tarjetaEntregadosHoy;
    private TarjetaKPI tarjetaCancelados;

    private BarraBusqueda barraBusqueda;
    private JComboBox<String> comboEstado;

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    public PanelPedidos() {

        super();

        setOpaque(false);
        setLayout(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));

        JPanel norte = new JPanel(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));
        norte.setOpaque(false);
        norte.add(FabricaEtiquetas.crearTitulo("PEDIDOS"), BorderLayout.NORTH);
        norte.add(crearFilaKPI(), BorderLayout.CENTER);
        norte.add(crearBarraAcciones(), BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);

        JPanel pie = new JPanel(new BorderLayout());
        pie.setOpaque(false);
        pie.add(FabricaEtiquetas.crearPequeño(
                "Estados: Pendiente, En preparación, Listo, Entregado, Cancelado. Doble clic en una fila para avanzar el estado."
        ), BorderLayout.WEST);
        add(pie, BorderLayout.SOUTH);

        cargarDatos();
    }

    private JPanel crearFilaKPI() {

        JPanel fila = new JPanel(new GridLayout(1, 5, UIConstants.ESPACIO_SUBTITULO, 0));
        fila.setOpaque(false);
        fila.setBorder(javax.swing.BorderFactory.createEmptyBorder(UIConstants.ESPACIO_SUBTITULO, 0, 0, 0));

        tarjetaPendientes = new TarjetaKPI(FabricaIconos.pendiente(), "Pendientes", "0", "");
        tarjetaEnPreparacion = new TarjetaKPI(FabricaIconos.preparando(), "En Preparación", "0", "");
        tarjetaListos = new TarjetaKPI(FabricaIconos.listo(), "Listos", "0", "");
        tarjetaEntregadosHoy = new TarjetaKPI(FabricaIconos.pedidosListos(), "Entregados Hoy", "0", "");
        tarjetaCancelados = new TarjetaKPI(FabricaIconos.cancelar(), "Cancelados", "0", "");

        fila.add(tarjetaPendientes);
        fila.add(tarjetaEnPreparacion);
        fila.add(tarjetaListos);
        fila.add(tarjetaEntregadosHoy);
        fila.add(tarjetaCancelados);

        return fila;
    }

    private JPanel crearBarraAcciones() {

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, UIConstants.ESPACIO_SUBTITULO, 0));
        barra.setOpaque(false);

        barraBusqueda = new BarraBusqueda("Buscar pedido...");
        barraBusqueda.getCampo().getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {
                    @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
                    @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
                    @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
                });

        comboEstado = FabricaCampos.crearCombo();
        comboEstado.addItem("Estado: Todos");
        for (EstadoPedido estado : EstadoPedido.values()) {
            comboEstado.addItem(capitalizar(estado.name()));
        }
        comboEstado.addActionListener(e -> aplicarFiltros());

        barra.add(barraBusqueda);
        barra.add(comboEstado);

        return barra;
    }

    private JPanel crearPanelTabla() {

        Object[] columnas = {"ID", "Fecha", "Cliente", "Total", "Entrega", "Estado"};

        modeloTabla = FabricaTablas.crearModeloSoloLectura(columnas);
        tabla = FabricaTablas.crearTabla(modeloTabla);

        tabla.getColumnModel().getColumn(COLUMNA_ESTADO).setCellRenderer(new RenderizadorEstado());

        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evento) {
                if (evento.getClickCount() == 2) {
                    avanzarEstadoSeleccionado();
                }
            }
        });

        return FabricaTablas.crearPanelTabla(tabla);
    }

    public void cargarDatos() {

        todosLosPedidos = pedidoService.listarPedidos();

        int pendientes = 0, enPreparacion = 0, listos = 0, entregadosHoy = 0, cancelados = 0;

        LocalDate hoy = LocalDate.now();

        for (Pedido pedido : todosLosPedidos) {

            switch (pedido.getEstado()) {
                case PENDIENTE -> pendientes++;
                case PREPARACION -> enPreparacion++;
                case LISTO -> listos++;
                case CANCELADO -> cancelados++;
                case ENTREGADO -> {
                    if (pedido.getFecha() != null && pedido.getFecha().toLocalDate().isEqual(hoy)) {
                        entregadosHoy++;
                    }
                }
            }
        }

        tarjetaPendientes.actualizar(String.valueOf(pendientes), "");
        tarjetaEnPreparacion.actualizar(String.valueOf(enPreparacion), "");
        tarjetaListos.actualizar(String.valueOf(listos), "");
        tarjetaEntregadosHoy.actualizar(String.valueOf(entregadosHoy), "");
        tarjetaCancelados.actualizar(String.valueOf(cancelados), "");

        aplicarFiltros();
    }

    private void aplicarFiltros() {

        String texto = barraBusqueda == null ? "" : barraBusqueda.getTexto().toLowerCase();

        String estadoSeleccionado =
                (comboEstado == null || comboEstado.getSelectedIndex() <= 0)
                        ? null
                        : (String) comboEstado.getSelectedItem();

        pedidosFiltrados = new ArrayList<>();

        for (Pedido pedido : todosLosPedidos) {

            String cliente = pedido.getUsuario() == null ? "" : pedido.getUsuario().getNombreCompleto();

            boolean pasaBusqueda = texto.isEmpty()
                    || String.valueOf(pedido.getIdPedido()).contains(texto)
                    || cliente.toLowerCase().contains(texto);

            boolean pasaEstado = estadoSeleccionado == null
                    || capitalizar(pedido.getEstado().name()).equalsIgnoreCase(estadoSeleccionado);

            if (pasaBusqueda && pasaEstado) {
                pedidosFiltrados.add(pedido);
            }
        }

        repintarTabla();
    }

    private void repintarTabla() {

        modeloTabla.setRowCount(0);

        for (Pedido pedido : pedidosFiltrados) {

            String cliente = pedido.getUsuario() == null ? "-" : pedido.getUsuario().getNombreCompleto();
            BigDecimal total = pedido.getTotal() == null ? BigDecimal.ZERO : pedido.getTotal();

            modeloTabla.addRow(new Object[]{
                    pedido.getIdPedido(),
                    pedido.getFecha() == null ? "-" : pedido.getFecha().format(FORMATO_FECHA),
                    cliente,
                    "Q" + total,
                    pedido.getTipoEntrega() == null ? "-" : pedido.getTipoEntrega().name(),
                    capitalizar(pedido.getEstado().name())
            });
        }
    }

    private void avanzarEstadoSeleccionado() {

        int fila = tabla.getSelectedRow();

        if (fila < 0 || fila >= pedidosFiltrados.size()) {
            return;
        }

        Pedido pedido = pedidosFiltrados.get(fila);

        EstadoPedido siguiente = siguienteEstado(pedido.getEstado());

        if (siguiente == null) {
            return; // ya está Entregado o Cancelado, no hay "siguiente" natural
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Cambiar el pedido #" + pedido.getIdPedido() + " a \"" + capitalizar(siguiente.name()) + "\"?",
                "Confirmar cambio de estado",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        pedido.setEstado(siguiente);

        if (!pedidoService.actualizarPedido(pedido)) {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el estado del pedido.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        cargarDatos();
    }

    private EstadoPedido siguienteEstado(EstadoPedido actual) {
        return switch (actual) {
            case PENDIENTE -> EstadoPedido.PREPARACION;
            case PREPARACION -> EstadoPedido.LISTO;
            case LISTO -> EstadoPedido.ENTREGADO;
            default -> null; // ENTREGADO y CANCELADO son estados finales
        };
    }

    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }
        return texto.charAt(0) + texto.substring(1).toLowerCase();
    }

}
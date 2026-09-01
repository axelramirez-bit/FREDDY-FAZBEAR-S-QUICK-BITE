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
import View.Componentes.PanelFondo;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Pantalla 2 del mockup: lista de pedidos PENDIENTE con búsqueda,
 * filtro por método de pago, orden y el botón "Atender" que avanza
 * el pedido a EN_PREPARACION.
 *
 * Sin paginación: la cantidad de pedidos pendientes a la vez es
 * naturalmente chica (son los que aún no se empiezan a preparar),
 * así que una sola tabla con scroll es suficiente. Si el volumen
 * crece mucho, es la misma paginación que ya tiene PanelHistorial.
 * ===============================================================
 */
public class PanelPedidosPendientes extends PanelFondo {

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("hh:mm a");

    private final IPedidoService pedidoService = new PedidoServiceImpl();
    private final IPagoService pagoService = new PagoServiceImpl();

    private BarraBusqueda barraBusqueda;
    private JComboBox<String> comboMetodoPago;
    private JComboBox<String> comboOrden;
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private JPanel panelPie;

    private List<Pedido> pendientesCompletos = new ArrayList<>();
    private List<Pedido> pedidosVisibles = new ArrayList<>();

    public PanelPedidosPendientes() {

        super();

        setLayout(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));

        setBorder(BorderFactory.createEmptyBorder(
                AdministradorTema.espacioGrande(), AdministradorTema.espacioGrande(),
                AdministradorTema.espacioGrande(), AdministradorTema.espacioGrande()));

        add(crearBarraFiltros(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);

        cargarDatos();
    }

    // ==========================================================
    // FILTROS
    // ==========================================================
    private JPanel crearBarraFiltros() {

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, AdministradorTema.espacioMediano(), 0));
        barra.setOpaque(false);

        barraBusqueda = new BarraBusqueda("Buscar pedido o cliente...");
        barraBusqueda.agregarListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override public void removeUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override public void changedUpdate(DocumentEvent e) { aplicarFiltros(); }
        });

        comboMetodoPago = FabricaCampos.crearCombo();
        comboMetodoPago.setModel(new DefaultComboBoxModel<>(
                new String[]{"Todos", "Efectivo", "Tarjeta", "Transferencia"}));
        comboMetodoPago.addActionListener(e -> aplicarFiltros());

        comboOrden = FabricaCampos.crearCombo();
        comboOrden.setModel(new DefaultComboBoxModel<>(
                new String[]{"Más antiguos", "Más recientes"}));
        comboOrden.addActionListener(e -> aplicarFiltros());

        JButton btnRefrescar = FabricaBotones.crearSecundario("↻");
        btnRefrescar.addActionListener(e -> cargarDatos());

        barra.add(barraBusqueda);
        barra.add(FabricaEtiquetas.crearTexto("Método de pago:"));
        barra.add(comboMetodoPago);
        barra.add(FabricaEtiquetas.crearTexto("Ordenar por:"));
        barra.add(comboOrden);
        barra.add(btnRefrescar);

        return barra;
    }

    // ==========================================================
    // TABLA
    // ==========================================================
    private JPanel crearPanelTabla() {

        JPanel contenedor = new JPanel(new BorderLayout(0, AdministradorTema.espacioPequeño()));
        contenedor.setOpaque(false);

        modeloTabla = FabricaTablas.crearModeloSoloLectura(new Object[]{
                "Pedido", "Cliente", "Tipo de entrega", "Hora", "Método de pago", "Tiempo de espera", "Acción"
        });

        tabla = FabricaTablas.crearTabla(modeloTabla);

        ColumnaAccionTabla.instalar(
                tabla, 6, "Atender", PaletaColores.PRINCIPAL,
                fila -> atenderPedido(pedidosVisibles.get(fila))
        );

        contenedor.add(FabricaTablas.crearScrollTabla(tabla), BorderLayout.CENTER);

        panelPie = new JPanel(new BorderLayout());
        panelPie.setOpaque(false);
        contenedor.add(panelPie, BorderLayout.SOUTH);

        return contenedor;
    }

    // ==========================================================
    // ACCIÓN: Atender (PENDIENTE -> PREPARACION)
    // ==========================================================
    private void atenderPedido(Pedido pedido) {

        pedido.cambiarEstado(EstadoPedido.PREPARACION);

        boolean actualizado = pedidoService.actualizarPedido(pedido);

        if (!actualizado) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar el pedido #" + pedido.getIdPedido() + ".",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        cargarDatos();
    }

    // ==========================================================
    // CARGA + FILTROS
    // ==========================================================
    public void cargarDatos() {

        List<Pedido> todos = pedidoService.listarPedidos();

        pendientesCompletos = new ArrayList<>();
        for (Pedido p : todos) {
            if (p.getEstado() == EstadoPedido.PENDIENTE) {
                pendientesCompletos.add(p);
            }
        }

        aplicarFiltros();
    }

    private void aplicarFiltros() {

        String texto = barraBusqueda.getTexto().trim().toLowerCase();
        String metodoSeleccionado = (String) comboMetodoPago.getSelectedItem();
        boolean masRecientesPrimero = "Más recientes".equals(comboOrden.getSelectedItem());

        List<Pedido> resultado = new ArrayList<>();

        for (Pedido pedido : pendientesCompletos) {

            String cliente = pedido.getUsuario() != null ? pedido.getUsuario().getNombreCompleto() : "";
            boolean coincideTexto = texto.isEmpty()
                    || ("#" + pedido.getIdPedido()).contains(texto)
                    || cliente.toLowerCase().contains(texto);

            if (!coincideTexto) {
                continue;
            }

            if (metodoSeleccionado != null && !"Todos".equals(metodoSeleccionado)) {

                Pago pago = pagoService.buscarPorPedido(pedido.getIdPedido());
                String metodoPedido = pago != null ? nombreLegible(pago.getMetodoPago()) : "";

                if (!metodoSeleccionado.equals(metodoPedido)) {
                    continue;
                }
            }

            resultado.add(pedido);
        }

        resultado.sort(masRecientesPrimero
                ? Comparator.comparing(Pedido::getFecha).reversed()
                : Comparator.comparing(Pedido::getFecha));

        pedidosVisibles = resultado;

        repintarTabla();
    }

    private void repintarTabla() {

        modeloTabla.setRowCount(0);

        for (Pedido pedido : pedidosVisibles) {

            Pago pago = pagoService.buscarPorPedido(pedido.getIdPedido());

            modeloTabla.addRow(new Object[]{
                    "#" + pedido.getIdPedido(),
                    pedido.getUsuario() != null ? pedido.getUsuario().getNombreCompleto() : "-",
                    nombreLegible(pedido.getTipoEntrega()),
                    pedido.getFecha() != null ? pedido.getFecha().format(FORMATO_HORA) : "-",
                    pago != null ? nombreLegible(pago.getMetodoPago()) : "-",
                    formatearEspera(pedido.getFecha()),
                    "Atender"
            });
        }

        panelPie.removeAll();
        panelPie.add(
                FabricaEtiquetas.crearPequeño(
                        "Mostrando " + pedidosVisibles.size() + " de " + pendientesCompletos.size() + " pedidos pendientes"
                ),
                BorderLayout.WEST
        );
        panelPie.revalidate();
        panelPie.repaint();
    }

    // ==========================================================
    // UTILITARIOS
    // ==========================================================
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

    private String formatearEspera(LocalDateTime fecha) {

        if (fecha == null) {
            return "-";
        }

        long minutos = Duration.between(fecha, LocalDateTime.now()).toMinutes();

        return minutos < 1 ? "< 1 min" : minutos + " min";
    }
}

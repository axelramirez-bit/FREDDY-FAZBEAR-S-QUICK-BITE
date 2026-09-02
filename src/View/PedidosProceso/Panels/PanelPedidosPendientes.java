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
import View.Utils.FabricaDialogos;
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
 * filtro por método de pago, orden y las acciones "Ver", "Atender"
 * (PENDIENTE -> PREPARACION, caso de uso 6.1) y "Cancelar" (caso
 * de uso 6.4).
 *
 * CORRECCIÓN IMPORTANTE respecto a la versión anterior: el modelo
 * de la tabla se creaba con FabricaTablas.crearModeloSoloLectura(),
 * que fuerza isCellEditable(...) = false para TODAS las celdas.
 * JTable solo invoca al CellEditor de una columna cuando el modelo
 * dice que esa celda es editable (JTable.editCellAt -> isCellEditable),
 * así que aunque ColumnaAccionTabla pintaba el botón, un clic nunca
 * llegaba a dispararlo: los botones se veían pero no hacían nada.
 * Aquí el modelo se construye a mano y solo declara editables las
 * columnas de acción, que es lo que realmente activa los botones.
 *
 * Sin paginación: la cantidad de pedidos pendientes a la vez es
 * naturalmente chica (son los que aún no se empiezan a preparar),
 * así que una sola tabla con scroll es suficiente. Si el volumen
 * crece mucho, es la misma paginación que ya tiene PanelHistorial.
 * ===============================================================
 */
public class PanelPedidosPendientes extends PanelFondo implements Refrescable {

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("hh:mm a");

    private static final int COLUMNA_DETALLE = 6;
    private static final int COLUMNA_ACCION = 7;
    private static final int COLUMNA_CANCELAR = 8;

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

        Object[] columnas = {
                "Pedido", "Cliente", "Tipo de entrega", "Hora", "Método de pago", "Tiempo de espera",
                "Detalle", "Acción", "Cancelar"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return columna == COLUMNA_DETALLE || columna == COLUMNA_ACCION || columna == COLUMNA_CANCELAR;
            }
        };

        tabla = FabricaTablas.crearTabla(modeloTabla);

        ColumnaAccionTabla.instalar(
                tabla, COLUMNA_DETALLE, "Ver", PaletaColores.SECUNDARIO,
                fila -> verDetalle(pedidosVisibles.get(fila))
        );

        ColumnaAccionTabla.instalar(
                tabla, COLUMNA_ACCION, "Atender", PaletaColores.PRINCIPAL,
                fila -> atenderPedido(pedidosVisibles.get(fila))
        );

        ColumnaAccionTabla.instalar(
                tabla, COLUMNA_CANCELAR, "Cancelar", PaletaColores.ESTADO_PELIGRO,
                fila -> cancelarPedido(pedidosVisibles.get(fila))
        );

        contenedor.add(FabricaTablas.crearScrollTabla(tabla), BorderLayout.CENTER);

        panelPie = new JPanel(new BorderLayout());
        panelPie.setOpaque(false);
        contenedor.add(panelPie, BorderLayout.SOUTH);

        return contenedor;
    }

    // ==========================================================
    // ACCIÓN: Ver detalle (caso de uso 2 — consultar pedidos)
    // ==========================================================
    private void verDetalle(Pedido pedido) {
        Pago pago = pagoService.buscarPorPedido(pedido.getIdPedido());
        DialogoDetallePedido.mostrar(this, pedido, pago);
    }

    // ==========================================================
    // ACCIÓN: Atender (PENDIENTE -> PREPARACION, caso 6.1)
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
    // ACCIÓN: Cancelar pedido (caso 6.4)
    // ==========================================================
    private void cancelarPedido(Pedido pedido) {

        boolean confirma = FabricaDialogos.confirmar(
                this,
                "¿Cancelar el pedido #" + pedido.getIdPedido() + "? Esta acción no se puede deshacer."
        );

        if (!confirma) {
            return;
        }

        pedido.cambiarEstado(EstadoPedido.CANCELADO);

        boolean actualizado = pedidoService.actualizarPedido(pedido);

        if (!actualizado) {
            FabricaDialogos.error(this, "No se pudo cancelar el pedido #" + pedido.getIdPedido() + ".");
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
                    "Ver",
                    "Atender",
                    "Cancelar"
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
package View.PedidosProceso.Panels;

import Model.EstadoPedido;
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
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Pantalla 3 del mockup: pedidos EN_PREPARACION, con "Ver" (caso 2),
 * "Marcar listo" (PREPARACION -> LISTO, caso 6.2) y "Cancelar"
 * (caso 6.4, por si el pedido ya no se puede completar).
 *
 * CORRECCIÓN IMPORTANTE respecto a la versión anterior: el modelo
 * de tabla se creaba con FabricaTablas.crearModeloSoloLectura(),
 * que fuerza isCellEditable(...) = false para TODAS las celdas.
 * JTable solo invoca al CellEditor de una columna cuando el modelo
 * dice que esa celda es editable, así que los botones se veían
 * pero un clic nunca los disparaba. Aquí el modelo se construye a
 * mano y solo declara editables las columnas de acción.
 * ===============================================================
 */
public class PanelPedidosEnPreparacion extends PanelFondo {

    private static final Color NARANJA_LISTO = new Color(0xE07C1A);

    private static final int COLUMNA_DETALLE = 4;
    private static final int COLUMNA_ACCION = 5;
    private static final int COLUMNA_CANCELAR = 6;

    private final IPedidoService pedidoService = new PedidoServiceImpl();
    private final IPagoService pagoService = new PagoServiceImpl();

    private BarraBusqueda barraBusqueda;
    private JComboBox<String> comboOrden;
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private JPanel panelPie;

    private List<Pedido> enPreparacionCompletos = new ArrayList<>();
    private List<Pedido> visibles = new ArrayList<>();

    public PanelPedidosEnPreparacion() {

        super();

        setLayout(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));

        setBorder(BorderFactory.createEmptyBorder(
                AdministradorTema.espacioGrande(), AdministradorTema.espacioGrande(),
                AdministradorTema.espacioGrande(), AdministradorTema.espacioGrande()));

        add(crearBarraFiltros(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);

        cargarDatos();
    }

    private JPanel crearBarraFiltros() {

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, AdministradorTema.espacioMediano(), 0));
        barra.setOpaque(false);

        barraBusqueda = new BarraBusqueda("Buscar pedido o cliente...");
        barraBusqueda.agregarListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override public void removeUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override public void changedUpdate(DocumentEvent e) { aplicarFiltros(); }
        });

        comboOrden = FabricaCampos.crearCombo();
        comboOrden.setModel(new DefaultComboBoxModel<>(new String[]{"Más antiguos", "Más recientes"}));
        comboOrden.addActionListener(e -> aplicarFiltros());

        JButton btnRefrescar = FabricaBotones.crearSecundario("↻");
        btnRefrescar.addActionListener(e -> cargarDatos());

        barra.add(barraBusqueda);
        barra.add(FabricaEtiquetas.crearTexto("Ordenar por:"));
        barra.add(comboOrden);
        barra.add(btnRefrescar);

        return barra;
    }

    private JPanel crearPanelTabla() {

        JPanel contenedor = new JPanel(new BorderLayout(0, AdministradorTema.espacioPequeño()));
        contenedor.setOpaque(false);

        Object[] columnas = {
                "Pedido", "Cliente", "Tipo de entrega", "Tiempo en preparación",
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
                fila -> verDetalle(visibles.get(fila))
        );

        ColumnaAccionTabla.instalar(
                tabla, COLUMNA_ACCION, "Marcar listo", NARANJA_LISTO,
                fila -> marcarComoListo(visibles.get(fila))
        );

        ColumnaAccionTabla.instalar(
                tabla, COLUMNA_CANCELAR, "Cancelar", PaletaColores.ESTADO_PELIGRO,
                fila -> cancelarPedido(visibles.get(fila))
        );

        contenedor.add(FabricaTablas.crearScrollTabla(tabla), BorderLayout.CENTER);

        panelPie = new JPanel(new BorderLayout());
        panelPie.setOpaque(false);
        contenedor.add(panelPie, BorderLayout.SOUTH);

        return contenedor;
    }

    // ==========================================================
    // ACCIÓN: Ver detalle (caso 2)
    // ==========================================================
    private void verDetalle(Pedido pedido) {
        try {
            Pago pago = pagoService.buscarPorPedido(pedido.getIdPedido());
            DialogoDetallePedido.mostrar(this, pedido, pago);
        } catch (Exception ex) {
            FabricaDialogos.excepcion(
                    this, PanelPedidosEnPreparacion.class,
                    "No se pudo mostrar el detalle del pedido #" + pedido.getIdPedido() + ".",
                    ex
            );
        }
    }

    // ==========================================================
    // ACCIÓN: Marcar listo (PREPARACION -> LISTO, caso 6.2)
    // ==========================================================
    private void marcarComoListo(Pedido pedido) {

        try {
            pedido.cambiarEstado(EstadoPedido.LISTO);

            boolean actualizado = pedidoService.actualizarPedido(pedido);

            if (!actualizado) {
                FabricaDialogos.error(this, "No se pudo marcar como listo el pedido #" + pedido.getIdPedido() + ".");
                return;
            }

            cargarDatos();

        } catch (Exception ex) {
            FabricaDialogos.excepcion(
                    this, PanelPedidosEnPreparacion.class,
                    "No se pudo marcar como listo el pedido #" + pedido.getIdPedido()
                            + ". Verifica tu conexión e inténtalo de nuevo.",
                    ex
            );
        }
    }

    // ==========================================================
    // ACCIÓN: Cancelar pedido (caso 6.4)
    // ==========================================================
    private void cancelarPedido(Pedido pedido) {

        boolean confirma = FabricaDialogos.confirmar(
                this,
                "¿Cancelar el pedido #" + pedido.getIdPedido() + "? Ya está en preparación; "
                        + "esta acción no se puede deshacer."
        );

        if (!confirma) {
            return;
        }

        try {
            pedido.cambiarEstado(EstadoPedido.CANCELADO);

            boolean actualizado = pedidoService.actualizarPedido(pedido);

            if (!actualizado) {
                FabricaDialogos.error(this, "No se pudo cancelar el pedido #" + pedido.getIdPedido() + ".");
                return;
            }

            cargarDatos();

        } catch (Exception ex) {
            FabricaDialogos.excepcion(
                    this, PanelPedidosEnPreparacion.class,
                    "No se pudo cancelar el pedido #" + pedido.getIdPedido()
                            + ". Verifica tu conexión e inténtalo de nuevo.",
                    ex
            );
        }
    }

    public void cargarDatos() {

        List<Pedido> todos;

        try {
            todos = pedidoService.listarPedidos();
        } catch (Exception ex) {
            FabricaDialogos.excepcion(
                    this, PanelPedidosEnPreparacion.class,
                    "No se pudieron cargar los pedidos en preparación. Verifica tu conexión e inténtalo de nuevo.",
                    ex
            );
            return;
        }

        enPreparacionCompletos = new ArrayList<>();
        for (Pedido p : todos) {
            if (p.getEstado() == EstadoPedido.PREPARACION) {
                enPreparacionCompletos.add(p);
            }
        }

        aplicarFiltros();
    }

    private void aplicarFiltros() {

        String texto = barraBusqueda.getTexto().trim().toLowerCase();
        boolean masRecientesPrimero = "Más recientes".equals(comboOrden.getSelectedItem());

        List<Pedido> resultado = new ArrayList<>();

        for (Pedido pedido : enPreparacionCompletos) {

            String cliente = pedido.getUsuario() != null ? pedido.getUsuario().getNombreCompleto() : "";
            boolean coincide = texto.isEmpty()
                    || ("#" + pedido.getIdPedido()).contains(texto)
                    || cliente.toLowerCase().contains(texto);

            if (coincide) {
                resultado.add(pedido);
            }
        }

        resultado.sort(masRecientesPrimero
                ? Comparator.comparing(Pedido::getFecha).reversed()
                : Comparator.comparing(Pedido::getFecha));

        visibles = resultado;

        repintarTabla();
    }

    private void repintarTabla() {

        modeloTabla.setRowCount(0);

        for (Pedido pedido : visibles) {
            modeloTabla.addRow(new Object[]{
                    "#" + pedido.getIdPedido(),
                    pedido.getUsuario() != null ? pedido.getUsuario().getNombreCompleto() : "-",
                    nombreLegible(pedido.getTipoEntrega()),
                    formatearTiempo(pedido.getFecha()),
                    "Ver",
                    "Marcar listo",
                    "Cancelar"
            });
        }

        panelPie.removeAll();
        panelPie.add(
                FabricaEtiquetas.crearPequeño(
                        "Mostrando " + visibles.size() + " de " + enPreparacionCompletos.size() + " pedidos en preparación"
                ),
                BorderLayout.WEST
        );
        panelPie.revalidate();
        panelPie.repaint();
    }

    private String nombreLegible(TipoEntrega tipo) {
        if (tipo == null) return "-";
        return tipo == TipoEntrega.PARA_LLEVAR ? "Para llevar" : "Comer en local";
    }

    private String formatearTiempo(LocalDateTime fecha) {
        if (fecha == null) return "-";
        long minutos = Duration.between(fecha, LocalDateTime.now()).toMinutes();
        return minutos < 1 ? "< 1 min" : minutos + " min";
    }
}
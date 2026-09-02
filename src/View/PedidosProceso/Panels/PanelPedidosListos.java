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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Pantalla 4 del mockup: pedidos LISTO, con "Ver" (caso 2),
 * "Entregar" (LISTO -> ENTREGADO, caso 6.3) y "Cancelar" (caso
 * 6.4, por si el cliente ya no lo recoge). "Entregar" sigue
 * pidiendo confirmación porque es una acción que no se puede
 * deshacer con un botón — una vez entregado, solo Historial lo
 * vuelve a mostrar.
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
public class PanelPedidosListos extends PanelFondo implements Refrescable {

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("hh:mm a");

    private static final int COLUMNA_DETALLE = 5;
    private static final int COLUMNA_ACCION = 6;
    private static final int COLUMNA_CANCELAR = 7;

    private final IPedidoService pedidoService = new PedidoServiceImpl();
    private final IPagoService pagoService = new PagoServiceImpl();

    private BarraBusqueda barraBusqueda;
    private JComboBox<String> comboOrden;
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private JPanel panelPie;

    private List<Pedido> listosCompletos = new ArrayList<>();
    private List<Pedido> visibles = new ArrayList<>();

    public PanelPedidosListos() {

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
                "Pedido", "Cliente", "Tipo de entrega", "Listo desde", "Método de pago",
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
                tabla, COLUMNA_ACCION, "Entregar", PaletaColores.ACENTO,
                fila -> confirmarEntrega(visibles.get(fila))
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
        Pago pago = pagoService.buscarPorPedido(pedido.getIdPedido());
        DialogoDetallePedido.mostrar(this, pedido, pago);
    }

    // ==========================================================
    // ACCIÓN: Entregar (LISTO -> ENTREGADO, caso 6.3)
    // ==========================================================
    private void confirmarEntrega(Pedido pedido) {

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Confirmas la entrega del pedido #" + pedido.getIdPedido() + " al cliente?",
                "Confirmar entrega",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        pedido.cambiarEstado(EstadoPedido.ENTREGADO);

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
                "¿Cancelar el pedido #" + pedido.getIdPedido() + "? Ya está listo para entregar; "
                        + "esta acción no se puede deshacer."
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

    public void cargarDatos() {

        List<Pedido> todos = pedidoService.listarPedidos();

        listosCompletos = new ArrayList<>();
        for (Pedido p : todos) {
            if (p.getEstado() == EstadoPedido.LISTO) {
                listosCompletos.add(p);
            }
        }

        aplicarFiltros();
    }

    private void aplicarFiltros() {

        String texto = barraBusqueda.getTexto().trim().toLowerCase();
        boolean masRecientesPrimero = "Más recientes".equals(comboOrden.getSelectedItem());

        List<Pedido> resultado = new ArrayList<>();

        for (Pedido pedido : listosCompletos) {

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

            Pago pago = pagoService.buscarPorPedido(pedido.getIdPedido());

            modeloTabla.addRow(new Object[]{
                    "#" + pedido.getIdPedido(),
                    pedido.getUsuario() != null ? pedido.getUsuario().getNombreCompleto() : "-",
                    nombreLegible(pedido.getTipoEntrega()),
                    pedido.getFecha() != null ? pedido.getFecha().format(FORMATO_HORA) : "-",
                    pago != null ? nombreLegible(pago.getMetodoPago()) : "-",
                    "Ver",
                    "Entregar",
                    "Cancelar"
            });
        }

        panelPie.removeAll();
        panelPie.add(
                FabricaEtiquetas.crearPequeño(
                        "Mostrando " + visibles.size() + " de " + listosCompletos.size() + " pedidos listos"
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
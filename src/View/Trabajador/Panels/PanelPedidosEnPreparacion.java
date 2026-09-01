package View.Trabajador.Panels;

import Model.EstadoPedido;
import Model.Pedido;
import Model.TipoEntrega;
import Service.Implement.PedidoServiceImpl;
import Service.Interfaz.IPedidoService;
import View.Componentes.BarraBusqueda;
import View.Componentes.ColumnaAccionTabla;
import View.Componentes.PanelFondo;
import View.Utils.AdministradorTema;
import View.Utils.FabricaBotones;
import View.Utils.FabricaCampos;
import View.Utils.FabricaEtiquetas;
import View.Utils.FabricaTablas;
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
 * Pantalla 3 del mockup: pedidos EN_PREPARACION.
 *
 * CORRECCIÓN respecto al mockup original que se revisó: antes esta
 * pantalla solo tenía "Ver detalle" (obligaba a un clic extra para
 * llegar a la acción). Ahora tiene el botón directo "Marcar listo"
 * (PREPARACION -> LISTO), igual patrón de un clic que "Atender" y
 * "Entregar" en las otras pantallas.
 * ===============================================================
 */
public class PanelPedidosEnPreparacion extends PanelFondo {

    private static final Color NARANJA_LISTO = new Color(0xE07C1A);

    private final IPedidoService pedidoService = new PedidoServiceImpl();

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

        modeloTabla = FabricaTablas.crearModeloSoloLectura(new Object[]{
                "Pedido", "Cliente", "Tipo de entrega", "Tiempo en preparación", "Acción"
        });

        tabla = FabricaTablas.crearTabla(modeloTabla);

        ColumnaAccionTabla.instalar(
                tabla, 4, "Marcar listo", NARANJA_LISTO,
                fila -> marcarComoListo(visibles.get(fila))
        );

        contenedor.add(FabricaTablas.crearScrollTabla(tabla), BorderLayout.CENTER);

        panelPie = new JPanel(new BorderLayout());
        panelPie.setOpaque(false);
        contenedor.add(panelPie, BorderLayout.SOUTH);

        return contenedor;
    }

    private void marcarComoListo(Pedido pedido) {

        pedido.cambiarEstado(EstadoPedido.LISTO);

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

    public void cargarDatos() {

        List<Pedido> todos = pedidoService.listarPedidos();

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
                    "Marcar listo"
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

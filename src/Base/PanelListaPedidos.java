
package Base;


import Model.EstadoPedido;
import Model.Pedido;
import Service.Implement.PedidoServiceImpl;
import Service.Interfaz.IPedidoService;
import View.Componentes.PanelFondo;
import View.Utils.FabricaBotones;
import View.Utils.FabricaTablas;
import View.Utils.UIConstants;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Panel genérico para las tres colas de trabajo del Trabajador:
 * Pendientes, En preparación y Listos. Es UNA sola clase — las
 * tres pantallas solo cambian en el EstadoPedido que filtran y en
 * a qué estado avanza el botón de acción.
 *
 * Ejemplo de uso:
 *
 *     public class PanelPedidosPendientes extends PanelListaPedidos {
 *         public PanelPedidosPendientes() {
 *             super(EstadoPedido.PENDIENTE, EstadoPedido.PREPARACION,
 *                     "Iniciar preparación");
 *         }
 *     }
 *
 *     public class PanelPedidosEnPreparacion extends PanelListaPedidos {
 *         public PanelPedidosEnPreparacion() {
 *             super(EstadoPedido.PREPARACION, EstadoPedido.LISTO,
 *                     "Marcar como listo");
 *         }
 *     }
 *
 *     public class PanelPedidosListos extends PanelListaPedidos {
 *         public PanelPedidosListos() {
 *             super(EstadoPedido.LISTO, EstadoPedido.ENTREGADO,
 *                     "Marcar como entregado");
 *         }
 *     }
 *
 * AVISO: IPedidoService todavía no tiene listarPorEstado(). Este
 * panel filtra en memoria sobre listarPedidos() mientras esa
 * consulta no exista en Service/DAO.
 * ===============================================================
 */
public class PanelListaPedidos extends PanelFondo {

    private static final DateTimeFormatter FORMATO_HORA =
            DateTimeFormatter.ofPattern("HH:mm");

    private final IPedidoService pedidoService;

    private final EstadoPedido estadoFiltro;

    private final EstadoPedido estadoSiguiente;

    private final String textoBotonAccion;

    private JTable tabla;

    private DefaultTableModel modeloTabla;

    public PanelListaPedidos(
            EstadoPedido estadoFiltro,
            EstadoPedido estadoSiguiente,
            String textoBotonAccion) {

        super();

        this.pedidoService = new PedidoServiceImpl();
        this.estadoFiltro = estadoFiltro;
        this.estadoSiguiente = estadoSiguiente;
        this.textoBotonAccion = textoBotonAccion;

        setOpaque(false);
        setLayout(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));

        add(crearPanelTabla(), BorderLayout.CENTER);

        cargarPedidos();
    }

    // ==========================================================
    // ESTRUCTURA
    // ==========================================================

    private JPanel crearPanelTabla() {

        Object[] columnas = {
                "N° orden",
                "Cliente",
                "Tipo de entrega",
                "Hora",
                "Total",
                ""
        };

        this.modeloTabla = FabricaTablas.crearModeloSoloLectura(columnas);

        this.tabla = FabricaTablas.crearTabla(modeloTabla);

        return FabricaTablas.crearPanelTabla(tabla);
    }

    // ==========================================================
    // CARGA Y ACCIÓN
    // ==========================================================

    /**
     * Vuelve a traer los pedidos en el estado filtrado y repinta
     * la tabla. Público para refrescar tras avanzar un pedido.
     */
    public void cargarPedidos() {

        List<Pedido> pedidos = pedidoService
                .listarPedidos()
                .stream()
                .filter(pedido -> pedido.getEstado() == estadoFiltro)
                .sorted(Comparator.comparing(Pedido::getFecha))
                .collect(Collectors.toList());

        modeloTabla.setRowCount(0);

        for (Pedido pedido : pedidos) {

            modeloTabla.addRow(new Object[]{
                    pedido.getNumeroOrden(),
                    pedido.getUsuario() != null
                            ? pedido.getUsuario().getNombreCompleto()
                            : "-",
                    pedido.getTipoEntrega(),
                    pedido.getFecha() != null
                            ? pedido.getFecha().format(FORMATO_HORA)
                            : "-",
                    "Q" + pedido.getTotal(),
                    pedido.getIdPedido()
            });
        }

        agregarBotonesAccion(pedidos);
    }

    /**
     * FabricaTablas no soporta una columna de botones nativa, así
     * que la acción se resuelve con un botón único debajo de la
     * tabla que actúa sobre la fila seleccionada. Si el equipo
     * agrega un renderer de botones por fila más adelante, este
     * método es el que hay que reemplazar.
     */
    private void agregarBotonesAccion(List<Pedido> pedidos) {

        if (getComponentCount() > 1) {
            remove(1);
        }

        JButton btnAccion = FabricaBotones.crearPrimario(textoBotonAccion);

        btnAccion.addActionListener(e -> avanzarPedidoSeleccionado(pedidos));

        add(btnAccion, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void avanzarPedidoSeleccionado(List<Pedido> pedidos) {

        int fila = tabla.getSelectedRow();

        if (fila < 0 || fila >= pedidos.size()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un pedido de la lista.",
                    "Ningún pedido seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Pedido pedido = pedidos.get(fila);

        pedido.cambiarEstado(estadoSiguiente);

        boolean actualizado = pedidoService.actualizarPedido(pedido);

        if (!actualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar el pedido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        cargarPedidos();
    }

}

// Paquete Base
package Base;


// Importa el enum de estados de pedido
import Model.EstadoPedido;
// Importa el modelo Pedido
import Model.Pedido;
// Importa el servicio de pedidos
import Service.Implement.PedidoServiceImpl;
// Importa la interfaz del servicio de pedidos
import Service.Interfaz.IPedidoService;
// Importa PanelFondo (panel con imagen de fondo)
import View.Componentes.PanelFondo;
// Importa la fábrica de botones
import View.Utils.FabricaBotones;
// Importa la fábrica de tablas
import View.Utils.FabricaTablas;
// Importa las constantes de UI
import View.Utils.UIConstants;

// Importa JButton
import javax.swing.JButton;
// Importa JOptionPane (diálogos)
import javax.swing.JOptionPane;
// Importa JPanel
import javax.swing.JPanel;
// Importa JTable
import javax.swing.JTable;
// Importa DefaultTableModel
import javax.swing.table.DefaultTableModel;
// Importa BorderLayout
import java.awt.BorderLayout;
// Importa DateTimeFormatter
import java.time.format.DateTimeFormatter;
// Importa Comparator
import java.util.Comparator;
// Importa List
import java.util.List;
// Importa Collectors
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
// Panel genérico de cola de pedidos del Trabajador
public class PanelListaPedidos extends PanelFondo {

    // Formato de hora "HH:mm"
    private static final DateTimeFormatter FORMATO_HORA =
            // Patrón de la hora
            DateTimeFormatter.ofPattern("HH:mm");

    // Servicio de pedidos
    private final IPedidoService pedidoService;

    // Estado de los pedidos que se muestran
    private final EstadoPedido estadoFiltro;

    // Estado al que avanza el pedido
    private final EstadoPedido estadoSiguiente;

    // Texto del botón de acción
    private final String textoBotonAccion;

    // Tabla de pedidos
    private JTable tabla;

    // Modelo de datos de la tabla
    private DefaultTableModel modeloTabla;

    // Constructor: recibe estado a filtrar, siguiente estado y texto del botón
    public PanelListaPedidos(
            EstadoPedido estadoFiltro,
            EstadoPedido estadoSiguiente,
            String textoBotonAccion) {

        // Llama al constructor de PanelFondo
        super();

        // Crea el servicio de pedidos
        this.pedidoService = new PedidoServiceImpl();
        // Guarda el estado a filtrar
        this.estadoFiltro = estadoFiltro;
        // Guarda el estado siguiente
        this.estadoSiguiente = estadoSiguiente;
        // Guarda el texto del botón
        this.textoBotonAccion = textoBotonAccion;

        // Fondo transparente
        setOpaque(false);
        // Usa BorderLayout con espacio vertical
        setLayout(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));

        // Agrega la tabla al centro
        add(crearPanelTabla(), BorderLayout.CENTER);

        // Carga los pedidos en la tabla
        cargarPedidos();
    }

    // ==========================================================
    // ESTRUCTURA
    // ==========================================================

    // Crea el panel con la tabla
    private JPanel crearPanelTabla() {

        // Nombres de las columnas
        Object[] columnas = {
                // Número de orden
                "N° orden",
                // Cliente
                "Cliente",
                // Tipo de entrega
                "Tipo de entrega",
                // Hora
                "Hora",
                // Total
                "Total",
                // Última columna (sin título)
                ""
        };

        // Crea el modelo de tabla de solo lectura
        this.modeloTabla = FabricaTablas.crearModeloSoloLectura(columnas);

        // Crea la tabla con ese modelo
        this.tabla = FabricaTablas.crearTabla(modeloTabla);

        // Devuelve el panel que contiene la tabla
        return FabricaTablas.crearPanelTabla(tabla);
    }

    // ==========================================================
    // CARGA Y ACCIÓN
    // ==========================================================

    /**
     * Vuelve a traer los pedidos en el estado filtrado y repinta
     * la tabla. Público para refrescar tras avanzar un pedido.
     */
    // Carga y repinta los pedidos del estado filtrado
    public void cargarPedidos() {

        // Lista de pedidos filtrados
        List<Pedido> pedidos = pedidoService
                // Trae todos los pedidos
                .listarPedidos()
                // Los convierte en stream
                .stream()
                // Se queda con los del estado filtrado
                .filter(pedido -> pedido.getEstado() == estadoFiltro)
                // Los ordena por fecha
                .sorted(Comparator.comparing(Pedido::getFecha))
                // Los junta en una lista
                .collect(Collectors.toList());

        // Vacía la tabla
        modeloTabla.setRowCount(0);

        // Recorre los pedidos
        for (Pedido pedido : pedidos) {

            // Agrega una fila a la tabla
            modeloTabla.addRow(new Object[]{
                    // Número de orden
                    pedido.getNumeroOrden(),
                    // Si hay cliente
                    pedido.getUsuario() != null
                            // muestra su nombre completo
                            ? pedido.getUsuario().getNombreCompleto()
                            // si no, muestra "-"
                            : "-",
                    // Tipo de entrega
                    pedido.getTipoEntrega(),
                    // Si hay fecha
                    pedido.getFecha() != null
                            // muestra la hora formateada
                            ? pedido.getFecha().format(FORMATO_HORA)
                            // si no, muestra "-"
                            : "-",
                    // Total con prefijo "Q"
                    "Q" + pedido.getTotal(),
                    // Id del pedido (última columna)
                    pedido.getIdPedido()
            });
        }

        // Agrega el botón de acción
        agregarBotonesAccion(pedidos);
    }

    /**
     * FabricaTablas no soporta una columna de botones nativa, así
     * que la acción se resuelve con un botón único debajo de la
     * tabla que actúa sobre la fila seleccionada. Si el equipo
     * agrega un renderer de botones por fila más adelante, este
     * método es el que hay que reemplazar.
     */
    // Agrega el botón que avanza el pedido seleccionado
    private void agregarBotonesAccion(List<Pedido> pedidos) {

        // Si ya había un botón agregado
        if (getComponentCount() > 1) {
            // lo quita
            remove(1);
        }

        // Crea el botón primario con el texto de acción
        JButton btnAccion = FabricaBotones.crearPrimario(textoBotonAccion);

        // Al hacer clic, avanza el pedido seleccionado
        btnAccion.addActionListener(e -> avanzarPedidoSeleccionado(pedidos));

        // Agrega el botón abajo
        add(btnAccion, BorderLayout.SOUTH);

        // Recalcula el layout
        revalidate();
        // Repinta el panel
        repaint();
    }

    // Avanza el pedido seleccionado al siguiente estado
    private void avanzarPedidoSeleccionado(List<Pedido> pedidos) {

        // Obtiene la fila seleccionada
        int fila = tabla.getSelectedRow();

        // Si no hay fila válida seleccionada
        if (fila < 0 || fila >= pedidos.size()) {

            // Muestra un aviso
            JOptionPane.showMessageDialog(
                    // Ventana padre
                    this,
                    // Mensaje
                    "Selecciona un pedido de la lista.",
                    // Título
                    "Ningún pedido seleccionado",
                    // Tipo advertencia
                    JOptionPane.WARNING_MESSAGE
            );

            // Termina el método
            return;
        }

        // Toma el pedido de esa fila
        Pedido pedido = pedidos.get(fila);

        // Cambia su estado al siguiente
        pedido.cambiarEstado(estadoSiguiente);

        // Guarda el cambio en la base de datos
        boolean actualizado = pedidoService.actualizarPedido(pedido);

        // Si no se pudo actualizar
        if (!actualizado) {

            // Muestra un error
            JOptionPane.showMessageDialog(
                    // Ventana padre
                    this,
                    // Mensaje
                    "No se pudo actualizar el pedido.",
                    // Título
                    "Error",
                    // Tipo error
                    JOptionPane.ERROR_MESSAGE
            );

            // Termina el método
            return;
        }

        // Recarga la tabla
        cargarPedidos();
    }

}
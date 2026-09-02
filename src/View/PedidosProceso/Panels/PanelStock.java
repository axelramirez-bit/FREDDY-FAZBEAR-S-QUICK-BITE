package View.PedidosProceso.Panels;

import Model.Producto;
import Service.Implement.ProductoServiceImpl;
import Service.Interfaz.IProductoService;
import View.Componentes.AlertaStockBajo;
import View.Componentes.BarraBusqueda;
import View.Componentes.PanelFondo;
import View.Componentes.Refrescable;
import View.Utils.AdministradorTema;
import View.Utils.FabricaBotones;
import View.Utils.FabricaCampos;
import View.Utils.FabricaEtiquetas;
import View.Utils.FabricaTablas;
import View.Utils.RenderizadorEstado;
import View.Utils.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Panel de Stock para el Trabajador — vista de solo lectura de las
 * cantidades reales de cada producto.
 *
 * Antes de esto, el Trabajador solo veía AlertaStockBajo en Inicio
 * (un texto tipo "3 productos en stock bajo (Papas, Coca-Cola...)")
 * sin poder ver cantidades exactas ni la lista completa. Este panel
 * no reemplaza esa alerta — la complementa: la alerta es el aviso
 * rápido en el dashboard, este panel es "quiero ver el detalle".
 *
 * A propósito es de SOLO LECTURA: modificar el stock (editar,
 * reponer, dar de baja un producto) sigue siendo trabajo exclusivo
 * de Administrador -> PanelProductos, coherente con el diagrama de
 * casos de uso (el Trabajador nunca aparece asociado a "Gestionar
 * productos"). Por eso aquí no hace falta el truco de
 * isCellEditable() por columna que sí necesitan Pendientes/En
 * preparación/Listos/Historial (esos SÍ tienen botones de acción
 * por fila; este panel no tiene ninguno).
 * ===============================================================
 */
public class PanelStock extends PanelFondo implements Refrescable {

    private final IProductoService productoService = new ProductoServiceImpl();

    private BarraBusqueda barraBusqueda;
    private JComboBox<String> comboOrden;
    private DefaultTableModel modeloTabla;
    private JLabel lblResumen;

    private List<Producto> productosCompletos = new ArrayList<>();
    private List<Producto> visibles = new ArrayList<>();

    public PanelStock() {

        super();

        setLayout(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));

        setBorder(BorderFactory.createEmptyBorder(
                AdministradorTema.espacioGrande(), AdministradorTema.espacioGrande(),
                AdministradorTema.espacioGrande(), AdministradorTema.espacioGrande()));

        add(crearBarraFiltros(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPie(), BorderLayout.SOUTH);

        cargarDatos();
    }

    // ==========================================================
    // FILTROS
    // ==========================================================
    private JPanel crearBarraFiltros() {

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, AdministradorTema.espacioMediano(), 0));
        barra.setOpaque(false);

        barraBusqueda = new BarraBusqueda("Buscar producto...");
        barraBusqueda.agregarListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override public void removeUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override public void changedUpdate(DocumentEvent e) { aplicarFiltros(); }
        });

        comboOrden = FabricaCampos.crearCombo();
        comboOrden.setModel(new DefaultComboBoxModel<>(
                new String[]{"Stock: menor a mayor", "Stock: mayor a menor", "Nombre (A-Z)"}));
        comboOrden.addActionListener(e -> aplicarFiltros());

        JButton btnRefrescar = FabricaBotones.crearSecundario("↻");
        btnRefrescar.addActionListener(e -> cargarDatos());

        barra.add(barraBusqueda);
        barra.add(FabricaEtiquetas.crearTexto("Ordenar por:"));
        barra.add(comboOrden);
        barra.add(btnRefrescar);

        return barra;
    }

    // ==========================================================
    // TABLA (solo lectura — sin columnas de acción, sin necesidad
    // de tocar isCellEditable)
    // ==========================================================
    private JPanel crearPanelTabla() {

        modeloTabla = FabricaTablas.crearModeloSoloLectura(
                new Object[]{"Producto", "Categoría", "Precio", "Stock", "Estado"});

        JTable tabla = FabricaTablas.crearTabla(modeloTabla);

        tabla.getColumnModel().getColumn(4).setCellRenderer(new RenderizadorEstado());

        return FabricaTablas.crearPanelTabla(tabla);
    }

    private JPanel crearPie() {

        JPanel pie = new JPanel(new BorderLayout());
        pie.setOpaque(false);

        lblResumen = FabricaEtiquetas.crearPequeño("Mostrando 0 de 0 productos");

        pie.add(lblResumen, BorderLayout.WEST);

        return pie;
    }

    // ==========================================================
    // CARGA + FILTROS
    // ==========================================================
    @Override
    public void cargarDatos() {

        productosCompletos = productoService.listarProductos();

        aplicarFiltros();
    }

    private void aplicarFiltros() {

        String texto = barraBusqueda.getTexto().trim().toLowerCase();
        String orden = (String) comboOrden.getSelectedItem();

        List<Producto> resultado = new ArrayList<>();

        for (Producto producto : productosCompletos) {

            boolean coincide = texto.isEmpty()
                    || producto.getNombre().toLowerCase().contains(texto);

            if (coincide) {
                resultado.add(producto);
            }
        }

        if ("Stock: mayor a menor".equals(orden)) {
            resultado.sort(Comparator.comparingInt(Producto::getStock).reversed());
        } else if ("Nombre (A-Z)".equals(orden)) {
            resultado.sort(Comparator.comparing(Producto::getNombre, String.CASE_INSENSITIVE_ORDER));
        } else {
            // "Stock: menor a mayor" (por defecto): lo más urgente primero.
            resultado.sort(Comparator.comparingInt(Producto::getStock));
        }

        visibles = resultado;

        repintarTabla();
    }

    private void repintarTabla() {

        modeloTabla.setRowCount(0);

        for (Producto producto : visibles) {
            modeloTabla.addRow(new Object[]{
                    producto.getNombre(),
                    producto.getCategoria() != null ? producto.getCategoria().getNombre() : "-",
                    "Q" + producto.getPrecio(),
                    producto.getStock(),
                    estadoDeStock(producto)
            });
        }

        lblResumen.setText("Mostrando " + visibles.size() + " de " + productosCompletos.size() + " productos");
    }

    /**
     * Mismo umbral que AlertaStockBajo, para que "3 productos en
     * stock bajo" (Inicio) y lo que se ve aquí como "Stock bajo"
     * sean siempre el mismo criterio, un solo lugar donde vive el
     * número (AlertaStockBajo.UMBRAL_STOCK_BAJO).
     */
    private String estadoDeStock(Producto producto) {

        if (!producto.hayStock()) {
            return "Agotado";
        }

        if (producto.getStock() <= AlertaStockBajo.UMBRAL_STOCK_BAJO) {
            return "Stock bajo";
        }

        return "Disponible";
    }
}
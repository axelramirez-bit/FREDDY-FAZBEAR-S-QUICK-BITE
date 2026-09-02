package View.PedidosProceso.Panels;

import Model.Producto;
import Service.Implement.ProductoServiceImpl;
import Service.Interfaz.IProductoService;
import View.Componentes.AlertaStockBajo;
import View.Componentes.BarraBusqueda;
import View.Componentes.PanelFondo;
import View.Utils.AdministradorTema;
import View.Utils.FabricaBotones;
import View.Utils.FabricaCampos;
import View.Utils.FabricaDialogos;
import View.Utils.FabricaEtiquetas;
import View.Utils.FabricaIconos;
import View.Utils.FabricaTablas;
import View.Utils.FormateadorMoneda;
import View.Utils.RenderizadorEstado;
import View.Utils.UIConstants;
import View.Componentes.TarjetaKPI;

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
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Pantalla nueva del Trabajador: consulta de stock de productos.
 *
 * No estaba en las 4 pantallas originales del mockup, pero sí hace
 * falta un lugar donde el Cajero vea cantidades reales — antes solo
 * existía AlertaStockBajo (un texto de 3 nombres como máximo en el
 * panel Inicio), sin tabla ni cifras exactas.
 *
 * DELIBERADAMENTE de solo lectura: el Trabajador necesita saber
 * cuánto queda para avisarle al cliente o priorizar qué preparar,
 * pero quien de verdad repone/ajusta inventario es el Administrador
 * desde PanelProductos (interfaz 6 del boceto de Admin). Por eso
 * esta tabla no tiene columna de acción ni botones de edición — ver
 * AlertaStockBajo.java, que documenta la misma separación de
 * responsabilidades.
 *
 * Reutiliza el mismo umbral de "stock bajo" que AlertaStockBajo
 * (AlertaStockBajo.UMBRAL_STOCK_BAJO) para que la tarjeta KPI, la
 * fila de alerta del Inicio y esta tabla nunca se desincronicen.
 * ===============================================================
 */
public class PanelStock extends PanelFondo {

    private static final int COLUMNA_ESTADO = 4;

    private final IProductoService productoService = new ProductoServiceImpl();

    private BarraBusqueda barraBusqueda;
    private JComboBox<String> comboCategoria;
    private JComboBox<String> comboEstadoStock;
    private JComboBox<String> comboOrden;

    private TarjetaKPI tarjetaTotal;
    private TarjetaKPI tarjetaDisponibles;
    private TarjetaKPI tarjetaStockBajo;
    private TarjetaKPI tarjetaSinStock;

    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private JPanel panelPie;

    private List<Producto> productosCompletos = new ArrayList<>();
    private List<Producto> productosVisibles = new ArrayList<>();

    public PanelStock() {

        super();

        setLayout(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));

        setBorder(BorderFactory.createEmptyBorder(
                AdministradorTema.espacioGrande(), AdministradorTema.espacioGrande(),
                AdministradorTema.espacioGrande(), AdministradorTema.espacioGrande()));

        JPanel norte = new JPanel(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));
        norte.setOpaque(false);
        norte.add(crearFilaKPI(), BorderLayout.NORTH);
        norte.add(crearBarraFiltros(), BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);

        cargarDatos();
    }

    // ==========================================================
    // KPI
    // ==========================================================
    private JPanel crearFilaKPI() {

        JPanel fila = new JPanel(new GridLayout(1, 4, AdministradorTema.espacioMediano(), 0));
        fila.setOpaque(false);
        fila.setBorder(BorderFactory.createEmptyBorder(0, 0, AdministradorTema.espacioMediano(), 0));

        tarjetaTotal = new TarjetaKPI(FabricaIconos.productos(), "Total productos", "0", "Activos");
        tarjetaDisponibles = new TarjetaKPI(FabricaIconos.productos(), "Disponibles", "0", "Con stock");
        tarjetaStockBajo = new TarjetaKPI(FabricaIconos.productos(), "Stock bajo", "0",
                "≤ " + AlertaStockBajo.UMBRAL_STOCK_BAJO + " unidades");
        tarjetaSinStock = new TarjetaKPI(FabricaIconos.productos(), "Sin stock", "0", "Agotados");

        fila.add(tarjetaTotal);
        fila.add(tarjetaDisponibles);
        fila.add(tarjetaStockBajo);
        fila.add(tarjetaSinStock);

        return fila;
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

        comboCategoria = FabricaCampos.crearCombo();
        comboCategoria.setModel(new DefaultComboBoxModel<>(new String[]{"Todas"}));
        comboCategoria.addActionListener(e -> aplicarFiltros());

        comboEstadoStock = FabricaCampos.crearCombo();
        comboEstadoStock.setModel(new DefaultComboBoxModel<>(
                new String[]{"Todos", "Disponible", "Stock bajo", "Sin stock"}));
        comboEstadoStock.addActionListener(e -> aplicarFiltros());

        comboOrden = FabricaCampos.crearCombo();
        comboOrden.setModel(new DefaultComboBoxModel<>(
                new String[]{"Menor stock primero", "Mayor stock primero", "Nombre (A-Z)"}));
        comboOrden.addActionListener(e -> aplicarFiltros());

        JButton btnRefrescar = FabricaBotones.crearSecundario("↻");
        btnRefrescar.addActionListener(e -> cargarDatos());

        barra.add(barraBusqueda);
        barra.add(FabricaEtiquetas.crearTexto("Categoría:"));
        barra.add(comboCategoria);
        barra.add(FabricaEtiquetas.crearTexto("Estado:"));
        barra.add(comboEstadoStock);
        barra.add(FabricaEtiquetas.crearTexto("Ordenar por:"));
        barra.add(comboOrden);
        barra.add(btnRefrescar);

        return barra;
    }

    // ==========================================================
    // TABLA (solo lectura: el Trabajador consulta, no edita)
    // ==========================================================
    private JPanel crearPanelTabla() {

        JPanel contenedor = new JPanel(new BorderLayout(0, AdministradorTema.espacioPequeño()));
        contenedor.setOpaque(false);

        modeloTabla = FabricaTablas.crearModeloSoloLectura(new Object[]{
                "Producto", "Categoría", "Precio", "Stock", "Estado"
        });

        tabla = FabricaTablas.crearTabla(modeloTabla);
        tabla.getColumnModel().getColumn(COLUMNA_ESTADO).setCellRenderer(new RenderizadorEstado());

        contenedor.add(FabricaTablas.crearScrollTabla(tabla), BorderLayout.CENTER);

        panelPie = new JPanel(new BorderLayout());
        panelPie.setOpaque(false);
        contenedor.add(panelPie, BorderLayout.SOUTH);

        return contenedor;
    }

    // ==========================================================
    // CARGA + FILTROS
    // ==========================================================
    public void cargarDatos() {

        try {
            productosCompletos = productoService.listarProductosDisponibles();
        } catch (Exception ex) {
            FabricaDialogos.excepcion(
                    this, PanelStock.class,
                    "No se pudo cargar el stock de productos. Verifica tu conexión e inténtalo de nuevo.",
                    ex
            );
            return;
        }

        actualizarComboCategorias();
        actualizarKPI();
        aplicarFiltros();
    }

    private void actualizarComboCategorias() {

        String seleccionActual = (String) comboCategoria.getSelectedItem();

        Set<String> nombres = new LinkedHashSet<>();
        nombres.add("Todas");

        productosCompletos.stream()
                .map(p -> p.getCategoria() != null ? p.getCategoria().getNombre() : null)
                .filter(nombre -> nombre != null && !nombre.isBlank())
                .sorted()
                .forEach(nombres::add);

        comboCategoria.setModel(new DefaultComboBoxModel<>(nombres.toArray(new String[0])));

        if (seleccionActual != null && nombres.contains(seleccionActual)) {
            comboCategoria.setSelectedItem(seleccionActual);
        } else {
            comboCategoria.setSelectedItem("Todas");
        }
    }

    private void actualizarKPI() {

        int total = productosCompletos.size();
        int disponibles = 0;
        int stockBajo = 0;
        int sinStock = 0;

        for (Producto producto : productosCompletos) {
            if (producto.getStock() <= 0) {
                sinStock++;
            } else if (producto.getStock() <= AlertaStockBajo.UMBRAL_STOCK_BAJO) {
                stockBajo++;
            } else {
                disponibles++;
            }
        }

        tarjetaTotal.actualizar(String.valueOf(total), "Activos");
        tarjetaDisponibles.actualizar(String.valueOf(disponibles), "Con stock");
        tarjetaStockBajo.actualizar(String.valueOf(stockBajo), "≤ " + AlertaStockBajo.UMBRAL_STOCK_BAJO + " unidades");
        tarjetaSinStock.actualizar(String.valueOf(sinStock), "Agotados");
    }

    private void aplicarFiltros() {

        String texto = barraBusqueda.getTexto().trim().toLowerCase();
        String categoriaSeleccionada = (String) comboCategoria.getSelectedItem();
        String estadoSeleccionado = (String) comboEstadoStock.getSelectedItem();
        String orden = (String) comboOrden.getSelectedItem();

        List<Producto> resultado = new ArrayList<>();

        for (Producto producto : productosCompletos) {

            boolean coincideTexto = texto.isEmpty()
                    || producto.getNombre().toLowerCase().contains(texto);

            if (!coincideTexto) {
                continue;
            }

            if (categoriaSeleccionada != null && !"Todas".equals(categoriaSeleccionada)) {
                String categoriaProducto = producto.getCategoria() != null ? producto.getCategoria().getNombre() : "";
                if (!categoriaSeleccionada.equals(categoriaProducto)) {
                    continue;
                }
            }

            if (estadoSeleccionado != null && !"Todos".equals(estadoSeleccionado)
                    && !estadoSeleccionado.equals(estadoStock(producto))) {
                continue;
            }

            resultado.add(producto);
        }

        if ("Mayor stock primero".equals(orden)) {
            resultado.sort(Comparator.comparingInt(Producto::getStock).reversed());
        } else if ("Nombre (A-Z)".equals(orden)) {
            resultado.sort(Comparator.comparing(Producto::getNombre, String.CASE_INSENSITIVE_ORDER));
        } else {
            resultado.sort(Comparator.comparingInt(Producto::getStock));
        }

        productosVisibles = resultado;

        repintarTabla();
    }

    private void repintarTabla() {

        modeloTabla.setRowCount(0);

        for (Producto producto : productosVisibles) {

            modeloTabla.addRow(new Object[]{
                    producto.getNombre(),
                    producto.getCategoria() != null ? producto.getCategoria().getNombre() : "-",
                    FormateadorMoneda.formatear(producto.getPrecio()),
                    producto.getStock(),
                    estadoStock(producto)
            });
        }

        panelPie.removeAll();
        panelPie.add(
                FabricaEtiquetas.crearPequeño(
                        "Mostrando " + productosVisibles.size() + " de " + productosCompletos.size() + " productos"
                ),
                BorderLayout.WEST
        );
        panelPie.revalidate();
        panelPie.repaint();
    }

    // ==========================================================
    // UTILITARIOS
    // ==========================================================

    /**
     * Mismo criterio de 3 niveles que ya usa AlertaStockBajo, expresado
     * como texto para que RenderizadorEstado (vía EtiquetaEstado.automatico)
     * lo pinte solo: "sin stock" → rojo, "bajo" → ámbar, "disponible" → verde.
     */
    private String estadoStock(Producto producto) {

        if (producto.getStock() <= 0) {
            return "Sin stock";
        }

        if (producto.getStock() <= AlertaStockBajo.UMBRAL_STOCK_BAJO) {
            return "Stock bajo";
        }

        return "Disponible";
    }
}
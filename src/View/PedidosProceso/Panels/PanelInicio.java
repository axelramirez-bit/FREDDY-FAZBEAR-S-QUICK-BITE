package View.PedidosProceso.Panels;

import Base.OpcionesTrabajador;
import Model.EstadoPedido;
import Model.Pedido;
import Service.Implement.PedidoServiceImpl;
import Service.Interfaz.IPedidoService;
import View.Componentes.AlertaStockBajo;
import View.Componentes.PanelFondo;
import View.Componentes.TarjetaKPI;
import View.Utils.AdministradorTema;
import View.Utils.FabricaBotones;
import View.Utils.FabricaEtiquetas;
import View.Utils.FabricaGraficas;
import View.Utils.FabricaIconos;
import View.Utils.FabricaPaneles;
import View.Utils.FabricaTablas;
import View.Utils.PaletaColores;
import View.Utils.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Inicio / Dashboard del Trabajador. Reproduce la pantalla 1 del
 * mockup aprobado: 4 KPI, dona de "pedidos por estado", tabla de
 * "próximos por atender", alertas (tiempo de espera alto + stock
 * bajo) y accesos rápidos a las otras 4 vistas.
 *
 * Umbral de "tiempo de espera alto": un pedido PENDIENTE que lleva
 * más de MINUTOS_ESPERA_ALTA minutos esperando. Es una constante
 * de esta pantalla porque solo aquí se usa para decidir la alerta;
 * las columnas de "tiempo de espera" de PanelPedidosPendientes
 * calculan lo mismo de forma independiente, así que si cambian este
 * número, cambien también el de allá.
 * ===============================================================
 */
public class PanelInicio extends PanelFondo {

    private static final int MINUTOS_ESPERA_ALTA = 15;

    private final IPedidoService pedidoService = new PedidoServiceImpl();

    private final AlertaStockBajo alertaStockBajo = new AlertaStockBajo();

    private JLabel lblAlertaEspera;
    private TarjetaKPI tarjetaPendientes;
    private TarjetaKPI tarjetaEnPreparacion;
    private TarjetaKPI tarjetaListos;
    private TarjetaKPI tarjetaTotal;
    private DefaultTableModel modeloProximos;
    private JPanel contenedorDona;

    /**
     * navegador: callback para los botones de "Accesos rápidos".
     * DashboardTrabajador lo construye con
     * {@code new PanelInicio(this::onOpcionSeleccionada)}.
     */
    public PanelInicio(Consumer<String> navegador) {

        super();

        setLayout(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));

        setBorder(BorderFactory.createEmptyBorder(
                AdministradorTema.espacioGrande(),
                AdministradorTema.espacioGrande(),
                AdministradorTema.espacioGrande(),
                AdministradorTema.espacioGrande()
        ));

        add(crearFilaKPI(), BorderLayout.NORTH);
        add(crearFilaCentral(), BorderLayout.CENTER);
        add(crearFilaInferior(navegador), BorderLayout.SOUTH);

        cargarDatos();
    }

    /** Constructor sin navegación, por si se usa fuera de DashboardTrabajador (pruebas). */
    public PanelInicio() {
        this(idVista -> { /* sin navegación configurada */ });
    }

    // ==========================================================
    // FILA SUPERIOR — 4 TARJETAS KPI
    // ==========================================================
    private JPanel crearFilaKPI() {

        JPanel fila = new JPanel(new GridLayout(1, 4, AdministradorTema.espacioMediano(), 0));
        fila.setOpaque(false);

        tarjetaPendientes = new TarjetaKPI(FabricaIconos.pedidosPendientes(), "Pendientes", "0", "Por atender");
        tarjetaEnPreparacion = new TarjetaKPI(FabricaIconos.enPreparacion(), "En preparación", "0", "En proceso");
        tarjetaListos = new TarjetaKPI(FabricaIconos.pedidosListos(), "Listos", "0", "Para entregar");
        tarjetaTotal = new TarjetaKPI(FabricaIconos.historial(), "Total pedidos", "0", "Hoy");

        fila.add(tarjetaPendientes);
        fila.add(tarjetaEnPreparacion);
        fila.add(tarjetaListos);
        fila.add(tarjetaTotal);

        return fila;
    }

    // ==========================================================
    // FILA CENTRAL — DONA "Pedidos por estado" + "Próximos por atender"
    // ==========================================================
    private JPanel crearFilaCentral() {

        JPanel fila = new JPanel(new GridLayout(1, 2, AdministradorTema.espacioMediano(), 0));
        fila.setOpaque(false);

        // ---- Pedidos por estado (dona) ----
        JPanel tarjetaDona = FabricaPaneles.crearTarjeta(new BorderLayout());
        tarjetaDona.setBorder(BorderFactory.createEmptyBorder(
                AdministradorTema.espacioMediano(), AdministradorTema.espacioMediano(),
                AdministradorTema.espacioMediano(), AdministradorTema.espacioMediano()));

        tarjetaDona.add(FabricaEtiquetas.crearSubtitulo("Pedidos por estado"), BorderLayout.NORTH);

        contenedorDona = new JPanel(new BorderLayout());
        contenedorDona.setOpaque(false);
        tarjetaDona.add(contenedorDona, BorderLayout.CENTER);

        // ---- Próximos por atender (tabla) ----
        JPanel tarjetaProximos = FabricaPaneles.crearTarjeta(new BorderLayout(0, AdministradorTema.espacioMediano()));
        tarjetaProximos.setBorder(BorderFactory.createEmptyBorder(
                AdministradorTema.espacioMediano(), AdministradorTema.espacioMediano(),
                AdministradorTema.espacioMediano(), AdministradorTema.espacioMediano()));

        tarjetaProximos.add(FabricaEtiquetas.crearSubtitulo("Próximos por atender"), BorderLayout.NORTH);

        modeloProximos = FabricaTablas.crearModeloSoloLectura(
                new Object[]{"Pedido", "Cliente", "Tiempo de espera"});

        JTable tablaProximos = FabricaTablas.crearTabla(modeloProximos);
        tarjetaProximos.add(FabricaTablas.crearScrollTabla(tablaProximos), BorderLayout.CENTER);

        fila.add(tarjetaDona);
        fila.add(tarjetaProximos);

        return fila;
    }

    // ==========================================================
    // FILA INFERIOR — Alertas importantes + Accesos rápidos
    // ==========================================================
    private JPanel crearFilaInferior(Consumer<String> navegador) {

        JPanel fila = new JPanel(new GridLayout(1, 2, AdministradorTema.espacioMediano(), 0));
        fila.setOpaque(false);

        // ---- Alertas importantes ----
        JPanel tarjetaAlertas = FabricaPaneles.crearTarjeta(new BorderLayout());
        tarjetaAlertas.setBorder(BorderFactory.createEmptyBorder(
                AdministradorTema.espacioMediano(), AdministradorTema.espacioMediano(),
                AdministradorTema.espacioMediano(), AdministradorTema.espacioMediano()));

        JPanel listaAlertas = new JPanel();
        listaAlertas.setOpaque(false);
        listaAlertas.setLayout(new BoxLayout(listaAlertas, BoxLayout.Y_AXIS));

        lblAlertaEspera = new JLabel("0 pedidos con tiempo de espera alto");
        lblAlertaEspera.setFont(AdministradorTema.fuentePequeña());

        listaAlertas.add(lblAlertaEspera);
        listaAlertas.add(alertaStockBajo);

        tarjetaAlertas.add(FabricaEtiquetas.crearSubtitulo("Alertas importantes"), BorderLayout.NORTH);
        tarjetaAlertas.add(listaAlertas, BorderLayout.CENTER);

        // ---- Accesos rápidos ----
        JPanel tarjetaAccesos = FabricaPaneles.crearTarjeta(new BorderLayout(0, AdministradorTema.espacioMediano()));
        tarjetaAccesos.setBorder(BorderFactory.createEmptyBorder(
                AdministradorTema.espacioMediano(), AdministradorTema.espacioMediano(),
                AdministradorTema.espacioMediano(), AdministradorTema.espacioMediano()));

        tarjetaAccesos.add(FabricaEtiquetas.crearSubtitulo("Accesos rápidos"), BorderLayout.NORTH);

        JPanel botones = new JPanel(new GridLayout(1, 4, AdministradorTema.espacioMediano(), 0));
        botones.setOpaque(false);

        botones.add(crearAccesoRapido("Pedidos pendientes", OpcionesTrabajador.PENDIENTES, navegador));
        botones.add(crearAccesoRapido("En preparación", OpcionesTrabajador.EN_PREPARACION, navegador));
        botones.add(crearAccesoRapido("Pedidos listos", OpcionesTrabajador.LISTOS, navegador));
        botones.add(crearAccesoRapido("Historial", OpcionesTrabajador.HISTORIAL, navegador));

        tarjetaAccesos.add(botones, BorderLayout.CENTER);

        fila.add(tarjetaAlertas);
        fila.add(tarjetaAccesos);

        return fila;
    }

    private JButton crearAccesoRapido(String texto, OpcionesTrabajador opcion, Consumer<String> navegador) {

        JButton boton = FabricaBotones.crearSecundario(texto);
        boton.addActionListener(e -> navegador.accept(opcion.getIdVista()));
        return boton;
    }

    // ==========================================================
    // CARGA DE DATOS
    // ==========================================================

    /**
     * Público para que el botón de refrescar (si se agrega más
     * adelante) o la navegación de vuelta a Inicio puedan pedir
     * datos frescos sin reconstruir el panel completo.
     */
    public void cargarDatos() {

        List<Pedido> pedidos = pedidoService.listarPedidos();

        List<Pedido> pendientes = filtrarPorEstado(pedidos, EstadoPedido.PENDIENTE);
        List<Pedido> enPreparacion = filtrarPorEstado(pedidos, EstadoPedido.PREPARACION);
        List<Pedido> listos = filtrarPorEstado(pedidos, EstadoPedido.LISTO);

        tarjetaPendientes.actualizar(String.valueOf(pendientes.size()), "Por atender");
        tarjetaEnPreparacion.actualizar(String.valueOf(enPreparacion.size()), "En proceso");
        tarjetaListos.actualizar(String.valueOf(listos.size()), "Para entregar");
        tarjetaTotal.actualizar(String.valueOf(pedidos.size()), "Hoy");

        actualizarDona(pedidos);
        actualizarProximos(pendientes);
        actualizarAlertas(pendientes);

        alertaStockBajo.actualizar();
    }

    private List<Pedido> filtrarPorEstado(List<Pedido> pedidos, EstadoPedido estado) {
        return pedidos.stream()
                .filter(p -> p.getEstado() == estado)
                .collect(Collectors.toList());
    }

    private void actualizarDona(List<Pedido> pedidos) {

        Map<String, Integer> conteoPorEstado = new LinkedHashMap<>();

        for (EstadoPedido estado : EstadoPedido.values()) {

            long cantidad = pedidos.stream().filter(p -> p.getEstado() == estado).count();

            if (cantidad > 0) {
                conteoPorEstado.put(nombreLegible(estado), (int) cantidad);
            }
        }

        contenedorDona.removeAll();

        if (conteoPorEstado.isEmpty()) {
            contenedorDona.add(FabricaEtiquetas.crearTexto("Sin pedidos registrados todavía."), BorderLayout.CENTER);
        } else {
            contenedorDona.add(FabricaGraficas.crearGraficaCircular("", conteoPorEstado), BorderLayout.CENTER);
        }

        contenedorDona.revalidate();
        contenedorDona.repaint();
    }

    private String nombreLegible(EstadoPedido estado) {
        switch (estado) {
            case PENDIENTE: return "Pendientes";
            case PREPARACION: return "En preparación";
            case LISTO: return "Listos";
            case ENTREGADO: return "Entregados";
            case CANCELADO: return "Cancelados";
            default: return estado.name();
        }
    }

    private void actualizarProximos(List<Pedido> pendientes) {

        modeloProximos.setRowCount(0);

        pendientes.stream()
                .sorted(Comparator.comparing(Pedido::getFecha))
                .limit(5)
                .forEach(p -> modeloProximos.addRow(new Object[]{
                        "#" + p.getIdPedido(),
                        p.getUsuario() != null ? p.getUsuario().getNombreCompleto() : "-",
                        formatearEspera(p.getFecha())
                }));
    }

    private void actualizarAlertas(List<Pedido> pendientes) {

        long conEsperaAlta = pendientes.stream()
                .filter(p -> minutosDeEspera(p.getFecha()) >= MINUTOS_ESPERA_ALTA)
                .count();

        lblAlertaEspera.setText(
                conEsperaAlta == 0
                        ? "✔ Ningún pedido con tiempo de espera alto."
                        : "⚠ " + conEsperaAlta + " pedido" + (conEsperaAlta == 1 ? "" : "s")
                          + " con tiempo de espera alto (más de " + MINUTOS_ESPERA_ALTA + " min)."
        );

        lblAlertaEspera.setForeground(conEsperaAlta == 0 ? PaletaColores.ACENTO : PaletaColores.PRINCIPAL);
    }

    private long minutosDeEspera(LocalDateTime fecha) {
        if (fecha == null) {
            return 0;
        }
        return Duration.between(fecha, LocalDateTime.now()).toMinutes();
    }

    private String formatearEspera(LocalDateTime fecha) {

        long minutos = minutosDeEspera(fecha);

        if (minutos < 1) {
            return "Recién tomado";
        }

        return minutos + " min";
    }
}

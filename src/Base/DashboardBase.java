package Base;

import Utils.Sesion;
import View.Componentes.PanelContenido;
import View.Utils.UtilPantalla;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Base compartida por los tres dashboards (Cliente, Trabajador,
 * Administrador).
 *
 * NOTA IMPORTANTE: esta versión usa PanelContenido directamente
 * (agregarVista/mostrar) en vez de ControlNavegacion, porque
 * ControlNavegacion ya no existe en el proyecto — PanelContenido
 * absorbió esa responsabilidad. Si en algún momento recuperan
 * ControlNavegacion, solo hay que cambiar los dos métodos
 * marcados abajo; el resto de la clase no se entera.
 *
 * Ensambla: BarraLateral (izquierda) + Encabezado (arriba
 * derecha) + PanelContenido (centro derecha). Cada DashboardX
 * solo necesita implementar 2 métodos:
 *
 *     public class DashboardCliente extends DashboardBase {
 *
 *         public DashboardCliente() {
 *             super(Rol.CLIENTE, "Freddy Fazbear's Quick Bite - Cliente");
 *         }
 *
 *         protected void registrarPaneles() {
 *             registrarVista("DESAYUNOS", new PanelDesayunos());
 *             registrarVista("BEBIDAS", new PanelBebidas());
 *             registrarVista("CARRITO", new PanelCarrito());
 *             // ... el resto de categorías
 *         }
 *
 *         protected String vistaInicial() {
 *             return "DESAYUNOS";
 *         }
 *     }
 *
 * DashboardTrabajador y DashboardAdministrador siguen exactamente
 * el mismo patrón, con su propio Rol y sus propios paneles.
 * ===============================================================
 */
public abstract class DashboardBase extends JFrame implements BarraLateralListener {

    private final Rol rol;

    private BarraLateral barraLateral;
    private Encabezado encabezado;
    private PanelContenido panelContenido;

    /**
     * Texto de cada opción de menú, indexado por idVista, para
     * que el Encabezado pueda mostrar el título correcto sin que
     * cada panel tenga que saber su propio título.
     */
    private final Map<String, String> titulosPorVista = new HashMap<>();

    // ==========================================================
    // CONSTRUCTOR
    // ==========================================================
    protected DashboardBase(Rol rol, String tituloVentana) {

        this.rol = rol;

        configurarVentana(tituloVentana);

        construirInterfaz();

        cargarTitulosDeMenu();

        registrarPaneles();

        abrirVistaInicial();
    }

    // ==========================================================
    // CONFIGURACIÓN DE LA VENTANA
    // ==========================================================
    private void configurarVentana(String tituloVentana) {

        setTitle(tituloVentana);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        UtilPantalla.aplicarTamañoMinimo(this);

        UtilPantalla.pantallaCompleta(this);
    }

    // ==========================================================
    // ENSAMBLAJE — BarraLateral + Encabezado + PanelContenido
    // ==========================================================
    private void construirInterfaz() {

        barraLateral = new BarraLateral(rol, this);

        encabezado = new Encabezado();

        panelContenido = new PanelContenido();

        JPanel panelDerecho = new JPanel(new BorderLayout());

        panelDerecho.add(encabezado, BorderLayout.NORTH);

        panelDerecho.add(panelContenido, BorderLayout.CENTER);

        add(barraLateral, BorderLayout.WEST);

        add(panelDerecho, BorderLayout.CENTER);
    }

    // ==========================================================
    // TÍTULOS (para el Encabezado)
    // ==========================================================
    private void cargarTitulosDeMenu() {

        for (OpcionMenu opcion : MenuPorRol.obtener(rol)) {
            titulosPorVista.put(opcion.getIdVista(), opcion.getTexto());
        }
    }

    // ==========================================================
    // API PARA LAS SUBCLASES
    // ==========================================================
    /**
     * Registra un panel bajo un idVista. Llámalo desde
     * registrarPaneles() en cada DashboardX, una vez por panel.
     *
     * (Si en el futuro vuelve ControlNavegacion, este es el único
     * método que cambiaría de implementación.)
     */
    protected void registrarVista(String idVista, java.awt.Component componente) {
        panelContenido.agregarVista(idVista, componente);
    }

    /**
     * Cada DashboardX registra aquí todos sus paneles con
     * registrarVista(...).
     */
    protected abstract void registrarPaneles();

    /**
     * idVista que se debe mostrar apenas se abre el dashboard.
     */
    protected abstract String vistaInicial();

    private void abrirVistaInicial() {
        onOpcionSeleccionada(vistaInicial());
    }

    // ==========================================================
    // BarraLateralListener
    // ==========================================================
    @Override
    public void onOpcionSeleccionada(String idVista) {

        panelContenido.mostrar(idVista);

        encabezado.setTitulo(
                titulosPorVista.getOrDefault(idVista, "")
        );
    }

    @Override
    public void onConfiguracion() {

        // Si el rol todavía no registró un panel de configuración,
        // CardLayout simplemente no muestra nada (no truena) hasta
        // que se agregue con registrarVista(IdVistaEspecial.CONFIGURACION, panel).
        panelContenido.mostrar(IdVistaEspecial.CONFIGURACION);

        encabezado.setTitulo("Configuración");
    }

    @Override
    public void onCerrarSesion() {

        Sesion.getInstancia().cerrarSesion();

        dispose();

        // TODO: cuando Integrante 2 termine Login/Bienvenida, aquí
        // se abre esa ventana en vez de solo cerrar esta.
    }

}
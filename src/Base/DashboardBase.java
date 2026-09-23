// Paquete Base
package Base;

// Importa FranjaHoraria
import Utils.FranjaHoraria;
// Importa Sesion (sesión activa)
import Utils.Sesion;
// Importa PanelContenido (zona de vistas)
import View.Componentes.PanelContenido;
// Importa UtilPantalla (tamaño de ventana)
import View.Utils.UtilPantalla;

// Importa JFrame
import javax.swing.JFrame;
// Importa JPanel
import javax.swing.JPanel;
// Importa BorderLayout
import java.awt.BorderLayout;
// Importa HashMap
import java.util.HashMap;
// Importa Map
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
// Ventana base abstracta de los tres dashboards; escucha la barra lateral
public abstract class DashboardBase extends JFrame implements BarraLateralListener {

    // Rol del dashboard
    private final Rol rol;

    // Barra lateral (izquierda)
    private BarraLateral barraLateral;
    // Encabezado (arriba a la derecha)
    private Encabezado encabezado;
    // Panel donde se muestran las vistas
    private PanelContenido panelContenido;

    /**
     * Texto de cada opción de menú, indexado por idVista, para
     * que el Encabezado pueda mostrar el título correcto sin que
     * cada panel tenga que saber su propio título.
     */
    // Título de cada opción del menú, por id de vista
    private final Map<String, String> titulosPorVista = new HashMap<>();

    // ==========================================================
    // CONSTRUCTOR
    // ==========================================================
    // Constructor: recibe el rol y el título de la ventana
    protected DashboardBase(Rol rol, String tituloVentana) {

        // Guarda el rol
        this.rol = rol;

        // Configura la ventana
        configurarVentana(tituloVentana);

        // Construye la interfaz
        construirInterfaz();

        // Carga los títulos del menú
        cargarTitulosDeMenu();

        // Registra los paneles (los define cada subclase)
        registrarPaneles();

        // Abre la vista inicial
        abrirVistaInicial();
    }

    // ==========================================================
    // CONFIGURACIÓN DE LA VENTANA
    // ==========================================================
    // Configura la ventana
    private void configurarVentana(String tituloVentana) {

        // Pone el título de la ventana
        setTitle(tituloVentana);

        // Cierra la aplicación al cerrar la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Usa BorderLayout
        setLayout(new BorderLayout());

        // Aplica el tamaño mínimo
        UtilPantalla.aplicarTamañoMinimo(this);

        // Pone la ventana en pantalla completa
        UtilPantalla.pantallaCompleta(this);
    }

    // ==========================================================
    // ENSAMBLAJE — BarraLateral + Encabezado + PanelContenido
    // ==========================================================
    // Ensambla barra lateral, encabezado y contenido
    private void construirInterfaz() {

        // Crea la barra lateral
        barraLateral = new BarraLateral(rol, this);

        // Crea el encabezado
        encabezado = new Encabezado();

        // Crea el panel de contenido
        panelContenido = new PanelContenido();

        // Crea el panel derecho con BorderLayout
        JPanel panelDerecho = new JPanel(new BorderLayout());

        // Encabezado arriba
        panelDerecho.add(encabezado, BorderLayout.NORTH);

        // Contenido al centro
        panelDerecho.add(panelContenido, BorderLayout.CENTER);

        // Barra lateral a la izquierda
        add(barraLateral, BorderLayout.WEST);

        // Panel derecho al centro
        add(panelDerecho, BorderLayout.CENTER);
    }

    // ==========================================================
    // TÍTULOS (para el Encabezado)
    // ==========================================================
    // Llena el mapa de títulos del menú
    private void cargarTitulosDeMenu() {

        // Recorre las opciones del rol
        for (OpcionMenu opcion : MenuPorRol.obtener(rol)) {
            // Guarda id de vista y texto
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
    // Registra un panel bajo un id de vista
    protected void registrarVista(String idVista, java.awt.Component componente) {
        // Lo agrega al panel de contenido
        panelContenido.agregarVista(idVista, componente);
    }

    /**
     * Cada DashboardX registra aquí todos sus paneles con
     * registrarVista(...).
     */
    // Cada subclase registra aquí sus paneles
    protected abstract void registrarPaneles();

    /**
     * idVista que se debe mostrar apenas se abre el dashboard.
     */
    // Cada subclase indica su vista inicial
    protected abstract String vistaInicial();

    // Abre la vista inicial
    private void abrirVistaInicial() {
        // Simula seleccionar la opción inicial
        onOpcionSeleccionada(vistaInicial());
    }

    // ==========================================================
    // BarraLateralListener
    // ==========================================================
    // Implementa el método del listener
    @Override
    // Se llama al elegir una opción del menú
    public void onOpcionSeleccionada(String idVista) {

        // Muestra el panel de esa vista
        panelContenido.mostrar(idVista);

        // Actualiza el título del encabezado
        encabezado.setTitulo(tituloDeVista(idVista));
    }

    /**
     * Título a mostrar en el Encabezado para el idVista dado.
     *
     * "DESAYUNOS_CENAS" es especial: titulosPorVista se llena UNA
     * sola vez en cargarTitulosDeMenu() (al abrir el dashboard), así
     * que si se usara ese mapa tal cual, el título quedaría
     * congelado en la franja horaria que había al iniciar sesión.
     * Aquí se recalcula contra la hora local actual para que
     * coincida siempre con lo que BarraLateral ya actualiza en el
     * ítem del menú (ver BarraLateral.actualizarOpcionSiEsDinamica).
     */
    // Devuelve el título de una vista
    private String tituloDeVista(String idVista) {

        // Si es la opción dinámica Desayunos/Cenas
        if ("DESAYUNOS_CENAS".equals(idVista)) {
            // recalcula el texto según la hora actual
            return FranjaHoraria.texto();
        }

        // Devuelve el título guardado o vacío
        return titulosPorVista.getOrDefault(idVista, "");
    }

    // Implementa el método del listener
    @Override
    public void onConfiguracion() {

        // Si el rol todavía no registró un panel de configuración,
        // CardLayout simplemente no muestra nada (no truena) hasta
        // que se agregue con registrarVista(IdVistaEspecial.CONFIGURACION, panel).
        // Muestra el panel de Configuración
        panelContenido.mostrar(IdVistaEspecial.CONFIGURACION);

        // Cambia el título a "Configuración"
        encabezado.setTitulo("Configuración");
    }

    // Implementa el método del listener
    @Override
    public void onCerrarSesion() {

        // Borra la sesión del usuario
        Sesion.getInstancia().cerrarSesion();

        // Cierra esta ventana
        dispose();

        // TODO: cuando Integrante 2 termine Login/Bienvenida, aquí
        // se abre esa ventana en vez de solo cerrar esta.
    }

}
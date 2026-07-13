package View.Utils;

import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Administrador de navegación de la aplicación.
 *
 * Responsabilidades:
 *
 * • Registrar paneles.
 * • Cambiar entre paneles.
 * • Evitar código repetido.
 * • Centralizar la navegación.
 *
 * Todas las vistas deben cambiar de panel utilizando esta clase.
 * ===============================================================
 */
public final class ControlNavegacion {

    /**
     * Panel principal donde se encuentran todos los paneles.
     */
    private static JPanel contenedor;

    /**
     * Administrador del CardLayout.
     */
    private static CardLayout administrador;

    /**
     * Lista de paneles registrados.
     */
    private static final Map<String, JPanel> PANELES = new HashMap<>();

    /**
     * Constructor privado.
     */
    private ControlNavegacion() {
    }

    // ==========================================================
    // INICIALIZAR
    // ==========================================================

    /**
     * Inicializa el administrador de navegación.
     *
     * Debe ejecutarse una sola vez desde DashboardBase.
     *
     * @param panelContenedor Panel principal.
     */
    public static void inicializar(JPanel panelContenedor) {

        contenedor = panelContenedor;

        administrador = (CardLayout) panelContenedor.getLayout();

    }

    // ==========================================================
    // REGISTRAR PANEL
    // ==========================================================

    /**
     * Registra un panel dentro del CardLayout.
     *
     * @param nombre Nombre del panel.
     * @param panel Panel.
     */
    public static void registrarPanel(
            String nombre,
            JPanel panel) {

        if (contenedor == null) {
            throw new IllegalStateException(
                    "ControlNavegacion no ha sido inicializado."
            );
        }

        PANELES.put(nombre, panel);

        contenedor.add(panel, nombre);

    }

    // ==========================================================
    // ABRIR PANEL
    // ==========================================================

    /**
     * Cambia al panel indicado.
     *
     * @param nombre Nombre del panel.
     */
    public static void abrir(String nombre) {

        if (!PANELES.containsKey(nombre)) {

            throw new IllegalArgumentException(
                    "El panel \"" + nombre + "\" no está registrado."
            );

        }

        administrador.show(contenedor, nombre);

        contenedor.revalidate();

        contenedor.repaint();

    }

    // ==========================================================
    // CONSULTAS
    // ==========================================================

    /**
     * Verifica si un panel está registrado.
     *
     * @param nombre Nombre del panel.
     * @return true si existe.
     */
    public static boolean existe(String nombre) {

        return PANELES.containsKey(nombre);

    }

    /**
     * Devuelve un panel registrado.
     *
     * @param nombre Nombre.
     * @return JPanel.
     */
    public static JPanel obtenerPanel(String nombre) {

        return PANELES.get(nombre);

    }

    /**
     * Devuelve la cantidad de paneles registrados.
     *
     * @return Número de paneles.
     */
    public static int totalPaneles() {

        return PANELES.size();

    }

    // ==========================================================
    // ELIMINAR
    // ==========================================================

    /**
     * Elimina un panel.
     *
     * @param nombre Nombre.
     */
    public static void eliminarPanel(String nombre) {

        JPanel panel = PANELES.remove(nombre);

        if (panel != null) {

            contenedor.remove(panel);

            contenedor.revalidate();

            contenedor.repaint();

        }

    }

    /**
     * Elimina todos los paneles registrados.
     */
    public static void limpiar() {

        PANELES.clear();

        if (contenedor != null) {

            contenedor.removeAll();

            contenedor.revalidate();

            contenedor.repaint();

        }

    }

}
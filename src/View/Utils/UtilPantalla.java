package View.Utils;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * Utilidades relacionadas con la pantalla y las ventanas.
 *
 * @author Axel
 */
public final class UtilPantalla {

    private UtilPantalla() {
    }

    //==========================================================
    // PANTALLA
    //==========================================================

    private static final Dimension PANTALLA =
            Toolkit.getDefaultToolkit().getScreenSize();

    public static int getAnchoPantalla() {
        return PANTALLA.width;
    }

    public static int getAltoPantalla() {
        return PANTALLA.height;
    }

    //==========================================================
    // PORCENTAJES
    //==========================================================

    public static int porcentajeAncho(double porcentaje) {

        return (int) (getAnchoPantalla() * porcentaje / 100);
    }

    public static int porcentajeAlto(double porcentaje) {

        return (int) (getAltoPantalla() * porcentaje / 100);
    }

    //==========================================================
    // CENTRAR VENTANA
    //==========================================================

    public static void centrar(Window ventana) {

        ventana.setLocationRelativeTo(null);
    }

    //==========================================================
    // MAXIMIZAR
    //==========================================================

    public static void maximizar(JFrame ventana) {

        ventana.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    //==========================================================
    // TAMAÑO MÍNIMO
    //==========================================================

    public static void aplicarTamañoMinimo(JFrame ventana) {

        ventana.setMinimumSize(new Dimension(
                UIConstants.ANCHO_MINIMO,
                UIConstants.ALTO_MINIMO
        ));
    }

    //==========================================================
    // TAMAÑO PERSONALIZADO
    //==========================================================

    public static void establecerTamaño(
            JFrame ventana,
            int ancho,
            int alto) {

        ventana.setSize(ancho, alto);
    }
    public static int escalarAncho(int tamaño) {

    return (int) (tamaño * getAnchoPantalla() / 1920.0);

}
    public static int escalarAlto(int tamaño) {

    return (int) (tamaño * getAltoPantalla() / 1080.0);

}
    public static void pantallaCompleta(JFrame ventana) {

    ventana.setExtendedState(JFrame.MAXIMIZED_BOTH);

    ventana.setUndecorated(false);

}

    //==========================================================
    // RELAYOUT EN VIVO
    //==========================================================

    /**
     * BUG QUE ESTO CORRIGE: DisenoAdaptable escala fuentes y medidas
     * según la resolución del MONITOR, una sola vez al iniciar la
     * aplicación — eso resuelve que la app se vea proporcional en
     * una laptop de 1366px o en un monitor 4K. Pero si el usuario
     * DESPUÉS desmaximiza la ventana y la achica a mano, ese cambio
     * de tamaño de VENTANA (no de monitor) no dispara ningún
     * recalculo: hoy solo RejillaResponsiva reacciona (porque Swing
     * vuelve a llamar a su LayoutManager en cada resize), y en
     * resizes grandes o rápidos (arrastrar el borde de golpe) puede
     * quedar el repintado a medias hasta el siguiente evento.
     *
     * Esto agrega un listener a la ventana principal que, con un
     * pequeño "debounce" (Timer de Swing: espera a que el usuario
     * deje de arrastrar el borde antes de actuar, en vez de
     * recalcular en cada píxel del resize), fuerza un
     * revalidate() + repaint() de todo el árbol de componentes al
     * terminar el resize. No recalcula fuentes ni medidas "de
     * diseño" (eso sigue dependiendo de la resolución del monitor,
     * ver DisenoAdaptable) — solo garantiza que TODOS los
     * LayoutManager de la ventana (RejillaResponsiva, BoxLayout,
     * GridLayout, BorderLayout) vuelvan a acomodar sus componentes
     * al tamaño real que tenga la ventana en cualquier momento.
     *
     * Se llama una sola vez, en DashboardBase.configurarVentana().
     */
    public static void activarRelayoutEnVivo(JFrame ventana) {

        Timer temporizador = new Timer(120, e -> {
            ventana.revalidate();
            ventana.repaint();
        });
        temporizador.setRepeats(false);

        ventana.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                temporizador.restart();
            }
        });
    }
}
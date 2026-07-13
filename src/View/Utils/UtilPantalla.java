package View.Utils;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import javax.swing.JFrame;

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
}
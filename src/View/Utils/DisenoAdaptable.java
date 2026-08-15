package View.Utils;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * --------------------------------------------------------------- Administrador
 * del diseño adaptable (Responsive).
 *
 * Responsabilidades:
 *
 * • Adaptar la interfaz a cualquier resolución. • Escalar tamaños. • Escalar
 * fuentes. • Escalar imágenes. • Escalar componentes.
 *
 * Toda la aplicación debe utilizar esta clase para obtener tamaños adaptables.
 * ===============================================================
 */
public final class DisenoAdaptable {

    /**
     * Resolución base utilizada durante el diseño.
     */
    private static final int ANCHO_BASE = 1920;

    private static final int ALTO_BASE = 1080;

    /**
     * Resolución actual.
     */
    private static final Dimension PANTALLA
            = Toolkit.getDefaultToolkit().getScreenSize();

    private static final int ANCHO_ACTUAL = PANTALLA.width;

    private static final int ALTO_ACTUAL = PANTALLA.height;

    /**
     * Factores de escala.
     */
    private static final double ESCALA_X
            = (double) ANCHO_ACTUAL / ANCHO_BASE;

    private static final double ESCALA_Y
            = (double) ALTO_ACTUAL / ALTO_BASE;

    /**
     * Constructor privado.
     */
    private DisenoAdaptable() {
    }

    // ==========================================================
    // ESCALA HORIZONTAL
    // ==========================================================
    /**
     * Escala un valor horizontal.
     *
     * @param valor Valor base.
     * @return Valor escalado.
     */
    public static int escalarAncho(int valor) {

        return (int) Math.round(valor * ESCALA_X);

    }

    // ==========================================================
    // ESCALA VERTICAL
    // ==========================================================
    /**
     * Escala un valor vertical.
     *
     * @param valor Valor base.
     * @return Valor escalado.
     */
    public static int escalarAlto(int valor) {

        return (int) Math.round(valor * ESCALA_Y);

    }

    // ==========================================================
    // ESCALA GENERAL
    // ==========================================================
    /**
     * Escala un valor utilizando el promedio de ambas escalas.
     *
     * Muy útil para:
     *
     * • Iconos • Bordes • Espaciados • Radios
     *
     * @param valor Valor base.
     * @return Valor escalado.
     */
    public static int escalar(int valor) {

        double escala = (ESCALA_X + ESCALA_Y) / 2;

        return (int) Math.round(valor * escala);

    }

    // ==========================================================
    // DIMENSIONES
    // ==========================================================
    /**
     * Devuelve una dimensión escalada.
     *
     * @param ancho Ancho base.
     * @param alto Alto base.
     * @return Dimension.
     */
    public static Dimension dimension(
            int ancho,
            int alto) {

        return new Dimension(
                escalarAncho(ancho),
                escalarAlto(alto)
        );

    }

    // ==========================================================
    // INFORMACIÓN
    // ==========================================================
    public static int getAnchoPantalla() {
        return ANCHO_ACTUAL;
    }

    public static int getAltoPantalla() {
        return ALTO_ACTUAL;
    }

    public static double getEscalaHorizontal() {
        return ESCALA_X;
    }

    public static double getEscalaVertical() {
        return ESCALA_Y;
    }

    /**
     * Indica si la resolución es Full HD.
     */
    public static boolean esFullHD() {

        return ANCHO_ACTUAL >= 1920;

    }

    /**
     * Indica si la resolución corresponde a una laptop.
     */
    public static boolean esLaptop() {

        return ANCHO_ACTUAL < 1600;

    }

    /**
     * Devuelve la resolución actual.
     */
    public static Dimension getResolucion() {

        return PANTALLA;

    }

}

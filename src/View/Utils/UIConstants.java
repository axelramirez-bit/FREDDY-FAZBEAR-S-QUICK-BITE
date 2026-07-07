package View.Utils;

import java.awt.*;


/**
 * Constantes de la interfaz gráfica.
 *
 * Centraliza tamaños, márgenes y dimensiones utilizadas
 * en toda la aplicación.
 *
 */
public final class UIConstants {

    private UIConstants() {
    }

    // ==========================================================
    // PANTALLA
    // ==========================================================

    /** Resolución del monitor. */
    public static final Dimension PANTALLA =
            Toolkit.getDefaultToolkit().getScreenSize();

    /** Ancho del monitor. */
    public static final int ANCHO_PANTALLA = PANTALLA.width;

    /** Alto del monitor. */
    public static final int ALTO_PANTALLA = PANTALLA.height;

    // ==========================================================
    // VENTANA
    // ==========================================================

    /** Ancho mínimo de la aplicación. */
    public static final int ANCHO_MINIMO = 1200;

    /** Alto mínimo de la aplicación. */
    public static final int ALTO_MINIMO = 700;

    // ==========================================================
    // MENÚ LATERAL (SIDEBAR)
    // ==========================================================

    /** Porcentaje del ancho que ocupará el menú lateral. */
    public static final double PORCENTAJE_MENU = 0.18;

    /** Ancho del menú lateral. */
    public static final int ANCHO_MENU =
            (int) (ANCHO_PANTALLA * PORCENTAJE_MENU);

    // ==========================================================
    // ENCABEZADO (HEADER)
    // ==========================================================

    /** Altura del encabezado. */
    public static final int ALTURA_ENCABEZADO = 95;

    // ==========================================================
    // ICONOS
    // ==========================================================

    public static final int ICONO_PEQUEÑO = 18;

    public static final int ICONO_MEDIANO = 24;

    public static final int ICONO_GRANDE = 32;

    public static final int TAMAÑO_LOGO = 110;

    // ==========================================================
    // BOTONES
    // ==========================================================

    /** Altura de botones normales. */
    public static final int ALTURA_BOTON = 42;

    /** Altura de botones del menú. */
    public static final int ALTURA_BOTON_MENU = 46;

    /** Radio de los botones. */
    public static final int RADIO_BOTON = 12;

    // ==========================================================
    // TARJETAS
    // ==========================================================

    /** Ancho de las tarjetas. */
    public static final int ANCHO_TARJETA = 360;

    /** Alto de las tarjetas. */
    public static final int ALTO_TARJETA = 185;

    /** Radio de las tarjetas. */
    public static final int RADIO_TARJETA = 18;

    // ==========================================================
    // ESPACIADOS
    // ==========================================================

    public static final int ESPACIADO_PEQUEÑO = 8;

    public static final int ESPACIADO_MEDIANO = 16;

    public static final int ESPACIADO_GRANDE = 24;

    public static final int SEPARACION_PEQUEÑA = 10;

    public static final int SEPARACION_MEDIANA = 18;

    public static final int SEPARACION_GRANDE = 30;

    // ==========================================================
    // BORDES
    // ==========================================================

    /** Radio general de paneles. */
    public static final int RADIO_BORDE = 18;

    // ==========================================================
    // SOMBRAS
    // ==========================================================

    /** Tamaño de la sombra. */
    public static final int TAMAÑO_SOMBRA = 8;

    // ==========================================================
    // FUENTES
    // ==========================================================

    /** Fuente principal del sistema. */
    public static final String FUENTE = "Segoe UI";

    public static final int FUENTE_PEQUEÑA = 12;

    public static final int FUENTE_NORMAL = 15;

    public static final int FUENTE_MEDIANA = 18;

    public static final int FUENTE_TITULO = 34;

}
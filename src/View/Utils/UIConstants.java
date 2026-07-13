package View.Utils;

import java.awt.*;

public final class UIConstants {

    private UIConstants() {
    }

    // ==========================================================
    // PANTALLA
    // ==========================================================
    /**
     * Resolución del monitor.
     */
    public static final Dimension PANTALLA
            = Toolkit.getDefaultToolkit().getScreenSize();

    /**
     * Ancho del monitor.
     */
    public static final int ANCHO_PANTALLA = PANTALLA.width;

    /**
     * Alto del monitor.
     */
    public static final int ALTO_PANTALLA = PANTALLA.height;

    // ==========================================================
    // VENTANA
    // ==========================================================
    /**
     * Ancho mínimo de la aplicación.
     */
    public static final int ANCHO_MINIMO = 1200;

    /**
     * Alto mínimo de la aplicación.
     */
    public static final int ALTO_MINIMO = 700;

    // ==========================================================
    // MENÚ LATERAL (SIDEBAR)
    // ==========================================================
    /**
     * Porcentaje del ancho que ocupará el menú lateral.
     */
    public static final double PORCENTAJE_MENU = 0.18;

    /**
     * Ancho del menú lateral.
     */
    public static final int ANCHO_MENU
            = (int) (ANCHO_PANTALLA * PORCENTAJE_MENU);

    // ==========================================================
    // ENCABEZADO (HEADER)
    // ==========================================================
    /**
     * Altura del encabezado.
     */
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
    /**
     * Altura de botones normales.
     */
    public static final int ALTURA_BOTON = 42;

    /**
     * Altura de botones del menú.
     */
    public static final int ALTURA_BOTON_MENU = 46;

    /**
     * Radio de los botones.
     */
    public static final int RADIO_BOTON = 12;

    // ==========================================================
    // TARJETAS
    // ==========================================================
    /**
     * Ancho de las tarjetas.
     */
    public static final int ANCHO_TARJETA = 360;

    /**
     * Alto de las tarjetas.
     */
    public static final int ALTO_TARJETA = 185;

    /**
     * Radio de las tarjetas.
     */
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
    /**
     * Radio general de paneles.
     */
    public static final int RADIO_BORDE = 18;

    // ==========================================================
    // SOMBRAS
    // ==========================================================
    /**
     * Tamaño de la sombra.
     */
    public static final int TAMAÑO_SOMBRA = 8;

    // ==========================================================
    // FUENTES
    // ==========================================================
    /**
     * Fuente principal del sistema.
     */
    public static final String FUENTE = "Segoe UI";

    public static final int FUENTE_PEQUEÑA = 12;

    public static final int FUENTE_NORMAL = 15;

    public static final int FUENTE_MEDIANA = 18;

    public static final int FUENTE_TITULO = 34;
// ==========================================================
// ANCHOS DE BOTONES
// ==========================================================

    public static final int ANCHO_BOTON = 180;

    public static final int ANCHO_BOTON_ICONO = 170;

    public static final int ANCHO_MINIMO_BOTON = 140;

    public static final int ANCHO_MAXIMO_BOTON = 220;
    // ==========================================================
// MENÚ
// ==========================================================

public static final int MARGEN_MENU = 20;

public static final int ESPACIO_ICONO_MENU = 12;

public static final int ESPACIO_MENU_SUPERIOR = 25;
// ==========================================================
// CAMPOS DE TEXTO
// ==========================================================

public static final int ALTURA_CAMPO = 40;

public static final int ANCHO_CAMPO = 280;

// ==========================================================
// BARRA DE BÚSQUEDA
// ==========================================================

public static final int ANCHO_BUSQUEDA = 320;

public static final int ALTURA_BUSQUEDA = 42;

public static final int RADIO_BUSQUEDA = 20;
// ==========================================================
// TABLAS
// ==========================================================

public static final int ALTURA_FILA = 36;

public static final int ALTURA_HEADER_TABLA = 45;
// ==========================================================
// SCROLL
// ==========================================================

public static final int VELOCIDAD_SCROLL = 18;

public static final int ANCHO_SCROLL = 12;
// ==========================================================
// TARJETAS PRODUCTOS
// ==========================================================

public static final int ANCHO_TARJETA_PRODUCTO = 250;

public static final int ALTO_TARJETA_PRODUCTO = 340;

// ==========================================================
// DIÁLOGOS
// ==========================================================

public static final int ANCHO_DIALOGO = 500;

public static final int ALTO_DIALOGO = 350;
// ==========================================================
// LOGIN
// ==========================================================

public static final int ANCHO_LOGIN = 950;

public static final int ALTO_LOGIN = 600;
// ==========================================================
// DASHBOARD
// ==========================================================

public static final int PADDING_CONTENIDO = 25;

public static final int ESPACIO_ENTRE_TARJETAS = 20;

public static final int MARGEN_PANEL = 20;


// ==========================================================
// ESPACIADOS ENTRE COMPONENTES
// ==========================================================

/** Separación entre icono y texto. */
public static final int ESPACIO_ICONO = 12;

/** Separación pequeña entre icono y texto. */
public static final int ESPACIO_ICONO_PEQUEÑO = 10;



// ==========================================================
// ÁREA DE TEXTO
// ==========================================================

/** Número de filas por defecto. */
public static final int TEXTAREA_FILAS = 5;

/** Número de columnas por defecto. */
public static final int TEXTAREA_COLUMNAS = 25;

// ==========================================================
// TABLAS
// ==========================================================

/** Altura de cada fila de una JTable. */
public static final int ALTURA_FILA_TABLA = 35;
//==========================================================
// PADDING DE BOTONES
//==========================================================

public static final int PADDING_BOTON_VERTICAL = 8;
public static final int PADDING_BOTON_HORIZONTAL = 15;



public static final int ANCHO_COMBO = 220;
public static final int ANCHO_SPINNER = 90;
public static final int PADDING_CAMPO_VERTICAL = 8;
public static final int PADDING_CAMPO_HORIZONTAL = 10;
public static final int PADDING_COMBO_VERTICAL = 4;
public static final int PADDING_COMBO_HORIZONTAL = 8;
// ==========================================================
// CATÁLOGO DE PRODUCTOS
// ==========================================================

public static final int COLUMNAS_PRODUCTOS = 4;

public static final int ESPACIO_PRODUCTOS = 20;

public static final int ANCHO_IMAGEN_PRODUCTO = 200;

public static final int ALTO_IMAGEN_PRODUCTO = 170;

public static final int RADIO_TARJETA_PRODUCTO = 20;



public static final int PADDING_SPINNER_VERTICAL = 2;

public static final int ESPACIO_TITULO = 16;

public static final int ESPACIO_SUBTITULO = 10;

public static final int PADDING_LABEL = 5;
//==========================================================
// LABELS
//==========================================================

public static final int LABEL_TITULO = 34;

public static final int LABEL_SUBTITULO = 18;

public static final int LABEL_NORMAL = 15;

public static final int LABEL_PEQUEÑO = 12;

public static final int MARGEN_TITULO = 16;
//==========================================================
// PANELES
//==========================================================

public static final int PADDING_PANEL = 20;

public static final int PADDING_TARJETA = 24;

public static final int RADIO_PANEL = 18;

public static final int ESPACIO_PANEL = 16;

public static final int CHECKBOX_ICONO = 18;

public static final int RADIOBUTTON_ICONO = 18;

public static final int ANCHO_COLUMNA_ID = 70;

public static final int ANCHO_COLUMNA_ACCIONES = 130;

public static final int PADDING_DIALOGO = 20;

public static final int ESPACIO_DIALOGO = 18;

public static final int ESPACIO_FORMULARIO = 18;

public static final int ANCHO_LABEL = 140;

public static final int ALTO_FILA_FORMULARIO = 42;

public static final int ANCHO_TOAST = 340;

public static final int ALTO_TOAST = 80;

public static final int TIEMPO_TOAST = 3000;

public static final int DURACION_ANIMACION = 250;

public static final int FPS_ANIMACION = 60;

public static final int SOMBRA_X = 3;

public static final int SOMBRA_Y = 3;

public static final int SOMBRA_BLUR = 8;

public static final int ICONO_BUSQUEDA = 18;

public static final int SCROLL_HORIZONTAL = 12;

public static final int SCROLL_VERTICAL = 12;

public static final int IMAGEN_USUARIO = 90;

public static final int IMAGEN_PRODUCTO = 170;

public static final int IMAGEN_BANNER = 320;

public static final int PADDING_DASHBOARD = 25;

public static final int ESPACIO_DASHBOARD = 20;

public static final int COLUMNAS_DASHBOARD = 4;

public static final int ANCHO_ICONO_MENU = 24;

public static final int ALTO_ITEM_MENU = 46;

public static final int PADDING_MENU = 16;
}

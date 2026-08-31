package View.Utils;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Todas las medidas y constantes visuales del proyecto viven aquí.
 * Ningún panel, componente o fábrica debe escribir un número
 * suelto — siempre a través de esta clase (o de AdministradorTema,
 * que es la puerta de entrada recomendada desde las vistas).
 *
 * NOTA DE MANTENIMIENTO (leer antes de agregar una constante
 * nueva): este archivo tenía varios grupos de constantes con
 * nombres distintos pero representando la MISMA medida (ej. tres
 * constantes de radio de esquina, todas en 18px). Se dejaron
 * TODOS los nombres que ya existían — para no romper nada que ya
 * los use — pero ahora varias son simples alias de una constante
 * canónica, marcadas con "// alias de X". Si vas a usar una de
 * estas medidas en código nuevo, usa siempre la constante
 * canónica, no el alias.
 * ===============================================================
 */
public final class UIConstants {

    private UIConstants() {
    }

    // ==========================================================
    // PANTALLA
    // ==========================================================
    public static final Dimension PANTALLA
            = Toolkit.getDefaultToolkit().getScreenSize();

    public static final int ANCHO_PANTALLA = PANTALLA.width;

    public static final int ALTO_PANTALLA = PANTALLA.height;

    // ==========================================================
    // VENTANA
    // ==========================================================
    public static final int ANCHO_MINIMO = 1200;

    public static final int ALTO_MINIMO = 700;

    // ==========================================================
    // MENÚ LATERAL (SIDEBAR)
    // ==========================================================
    /**
     * Porcentaje del ancho de pantalla que ocupa el sidebar.
     * ANCHO_MENU se calcula a partir de esto — así el sidebar se
     * ve proporcional en un monitor de 1366px y en uno de 1920px,
     * en vez de quedar fijo en un tamaño pensado para una sola
     * resolución.
     */
    public static final double PORCENTAJE_MENU = 0.18;

    /**
     * Ancho real del sidebar. Constante CANÓNICA — usa esta en
     * vez de un número fijo cuando necesites el ancho del menú
     * lateral (ej. AdministradorTema.anchoMenuLateral()).
     */
    public static final int ANCHO_MENU
            = (int) (ANCHO_PANTALLA * PORCENTAJE_MENU);

    public static final int MARGEN_MENU = 20;

    public static final int ESPACIO_ICONO_MENU = 12;

    public static final int ESPACIO_MENU_SUPERIOR = 25;

    public static final int ALTURA_BOTON_MENU = 46; // alias de ALTO_ITEM_MENU

    public static final int RADIO_MENU = 16;

    public static final int PADDING_HORIZONTAL_MENU = 18;

    public static final int PADDING_VERTICAL_MENU = 12;

    // ==========================================================
    // ENCABEZADO (HEADER)
    // ==========================================================
    public static final int ALTURA_ENCABEZADO = 120;

    // ==========================================================
    // ICONOS
    // ==========================================================
    public static final int ICONO_PEQUEÑO = 18;

    public static final int ICONO_MEDIANO = 24;

    public static final int ICONO_GRANDE = 32;

    public static final int TAMAÑO_LOGO = 160;

    /** alias de ICONO_PEQUEÑO */
    public static final int ICONO_BUSQUEDA = ICONO_PEQUEÑO;

    /** alias de ICONO_PEQUEÑO */
    public static final int CHECKBOX_ICONO = ICONO_PEQUEÑO;

    /** alias de ICONO_PEQUEÑO */
    public static final int RADIOBUTTON_ICONO = ICONO_PEQUEÑO;

    /** alias de ICONO_MEDIANO */
    public static final int ANCHO_ICONO_MENU = ICONO_MEDIANO;

    // ==========================================================
    // BOTONES
    // ==========================================================
    public static final int ALTURA_BOTON = 42;

    public static final int RADIO_BOTON = 12;

    public static final int ANCHO_BOTON = 180;

    public static final int ANCHO_BOTON_ICONO = 170;

    public static final int ANCHO_MINIMO_BOTON = 140;

    public static final int ANCHO_MAXIMO_BOTON = 220;

    public static final int PADDING_BOTON_VERTICAL = 8;

    public static final int PADDING_BOTON_HORIZONTAL = 15;

    // ==========================================================
    // SELECTOR DE CANTIDAD (antes hardcodeado en AdministradorTema)
    // ==========================================================
    public static final int ANCHO_SELECTOR_CANTIDAD = 110;

    public static final int ALTO_SELECTOR_CANTIDAD = 38;

    public static final int ANCHO_BOTON_CANTIDAD = 28;

    public static final int ALTO_BOTON_CANTIDAD = 28;

    // ==========================================================
    // RADIOS DE ESQUINA
    //
    // RADIO_BORDE es la constante CANÓNICA. RADIO_PANEL y
    // RADIO_TARJETA valían lo mismo (18px) sin motivo aparente
    // para ser distintos — se dejaron como alias. Si en algún
    // momento un diseño pide un radio distinto para tarjetas,
    // cambia RADIO_TARJETA de alias a su propio valor.
    // ==========================================================
    public static final int RADIO_BORDE = 18;

    /** alias de RADIO_BORDE */
    public static final int RADIO_PANEL = RADIO_BORDE;

    /** alias de RADIO_BORDE */
    public static final int RADIO_TARJETA = RADIO_BORDE;

    public static final int RADIO_TARJETA_PRODUCTO = 20;

    // ==========================================================
    // SOMBRAS
    // ==========================================================
    public static final int TAMAÑO_SOMBRA = 8;

    public static final int SOMBRA_X = 3;

    public static final int SOMBRA_Y = 3;

    public static final int SOMBRA_BLUR = 8;

    // ==========================================================
    // TIPOGRAFÍA — FUENTE BASE Y ESCALA ANTIGUA (compatibilidad)
    // ==========================================================
    /** Fuente de respaldo si Quicksand/Poppins no llegaran a cargar. */
    public static final String FUENTE = "Segoe UI";

    public static final String FUENTE_RESPALDO = FUENTE;

    public static final int FUENTE_PEQUEÑA = 12;

    public static final int FUENTE_NORMAL = 15;

    public static final int FUENTE_MEDIANA = 18;

    public static final int FUENTE_TITULO = 34;

    /** alias de FUENTE_TITULO */
    public static final int LABEL_TITULO = FUENTE_TITULO;

    public static final int LABEL_SUBTITULO = 18;

    /** alias de FUENTE_NORMAL */
    public static final int LABEL_NORMAL = FUENTE_NORMAL;

    /** alias de FUENTE_PEQUEÑA */
    public static final int LABEL_PEQUEÑO = FUENTE_PEQUEÑA;

    public static final int MARGEN_TITULO = 16;

    // ==========================================================
    // TIPOGRAFÍA — ESCALA DE DISEÑO NUEVA (Quicksand + Poppins)
    //
    // Ver UtilFuentes / CargadorFuentes. Estos tamaños vienen de
    // la guía tipográfica del proyecto.
    // ==========================================================
    public static final int TAMANO_TITULO_SECCION = 40;        // 36-44px

    public static final int TAMANO_SUBTITULO_HEADER = 14;      // 13-14px

    public static final int TAMANO_TITULO_PRODUCTO = 19;       // 18-20px

    public static final int TAMANO_DESCRIPCION_PRODUCTO = 13;  // 13-14px

    public static final int TAMANO_PRECIO = 17;                // 16-18px

    public static final int TAMANO_BOTON = 14;                 // 14-15px

    public static final int TAMANO_MENU_LATERAL = 17;          // 16-18px

    public static final int TAMANO_LOGO_ESLOGAN = 20;          // 10-12px

    // ==========================================================
    // CAMPOS DE TEXTO
    // ==========================================================
    public static final int ALTURA_CAMPO = 40;

    public static final int ANCHO_CAMPO = 280;

    public static final int PADDING_CAMPO_VERTICAL = 8;

    public static final int PADDING_CAMPO_HORIZONTAL = 10;

    // ==========================================================
    // BARRA DE BÚSQUEDA
    // ==========================================================
    public static final int ANCHO_BUSQUEDA = 320;

    public static final int ALTURA_BUSQUEDA = 56;

    public static final int RADIO_BUSQUEDA = 20;

    // ==========================================================
    // COMBOBOX / SPINNER
    // ==========================================================
    public static final int ANCHO_COMBO = 220;

    public static final int PADDING_COMBO_VERTICAL = 4;

    public static final int PADDING_COMBO_HORIZONTAL = 8;

    public static final int ANCHO_SPINNER = 90;

    public static final int PADDING_SPINNER_VERTICAL = 2;

    // ==========================================================
    // TABLAS
    //
    // ALTURA_FILA y ALTURA_FILA_TABLA representaban lo mismo con
    // valores distintos (36 vs 35) — eso era un bug de
    // inconsistencia, no dos casos de uso reales. Se unificaron
    // en 36px; ALTURA_FILA_TABLA queda como alias.
    // ==========================================================
    public static final int ALTURA_FILA = 36;

    /** alias de ALTURA_FILA (antes valía 35 por error) */
    public static final int ALTURA_FILA_TABLA = ALTURA_FILA;

    public static final int ALTURA_HEADER_TABLA = 45;

    public static final int ANCHO_COLUMNA_ID = 70;

    public static final int ANCHO_COLUMNA_ACCIONES = 130;

    // ==========================================================
    // SCROLL
    // ==========================================================
    public static final int VELOCIDAD_SCROLL = 18;

    public static final int ANCHO_SCROLL = 12;

    /** alias de ANCHO_SCROLL */
    public static final int SCROLL_HORIZONTAL = ANCHO_SCROLL;

    /** alias de ANCHO_SCROLL */
    public static final int SCROLL_VERTICAL = ANCHO_SCROLL;

    // ==========================================================
    // ÁREA DE TEXTO
    // ==========================================================
    public static final int TEXTAREA_FILAS = 5;

    public static final int TEXTAREA_COLUMNAS = 25;

    // ==========================================================
    // TARJETAS
    // ==========================================================
    public static final int ANCHO_TARJETA = 360;

    public static final int ALTO_TARJETA = 185;

    // ANTES: ANCHO_TARJETA_PRODUCTO = 250 y ANCHO_IMAGEN_PRODUCTO = 200.
    // BUG QUE ESTO CORRIGE: TarjetaProducto pone la imagen a la
    // IZQUIERDA (BorderLayout.WEST, ancho fijo) y el resto del
    // contenido (nombre, descripción, precio, cantidad y botón) al
    // CENTRO. Con esos valores, la imagen (200px) + su borde (16px
    // de hgap) ya casi llenaban el ancho útil de la tarjeta
    // (250 - 32 de padding = 218px), dejando prácticamente 0px para
    // el panel de contenido: por eso las tarjetas del Cliente solo
    // mostraban la imagen, sin nombre/descripción/precio/cantidad/
    // botón "Agregar al carrito". Se agranda la tarjeta y se achica
    // un poco la imagen para que el contenido tenga espacio real.
    public static final int ANCHO_TARJETA_PRODUCTO = 720;

    // ANTES: 220px. BUG QUE ESTO CORRIGE: con nombre + descripción
    // (2 líneas) + precio/badge de descuento + fila de Cantidad +
    // botón "Agregar al carrito", el contenido real necesita ~250-
    // 260px. Con 220px fijos, el BoxLayout de TarjetaProducto no
    // tenía a dónde reducir ese sobrante y lo último (el selector
    // de cantidad) quedaba cortado/invisible. Se sube a 280px con
    // margen real para que quepa todo, incluyendo el caso con
    // promoción (badge + precio tachado + precio final en la misma
    // fila, sin altura extra).
    public static final int ALTO_TARJETA_PRODUCTO = 220;

    public static final int PADDING_TARJETA = 24;

    public static final int ESPACIO_ENTRE_TARJETAS = 20;

    // ==========================================================
    // CATÁLOGO DE PRODUCTOS
    // ==========================================================
    public static final int COLUMNAS_PRODUCTOS = 4;

    public static final int ESPACIO_PRODUCTOS = 20;

    public static final int ANCHO_IMAGEN_PRODUCTO = 150;

    public static final int ALTO_IMAGEN_PRODUCTO = 150;

    /** alias de ALTO_IMAGEN_PRODUCTO */
    public static final int IMAGEN_PRODUCTO = ALTO_IMAGEN_PRODUCTO;

    public static final int IMAGEN_USUARIO = 90;

    public static final int IMAGEN_BANNER = 320;

    // ==========================================================
    // DIÁLOGOS
    // ==========================================================
    public static final int ANCHO_DIALOGO = 500;

    public static final int ALTO_DIALOGO = 350;

    public static final int PADDING_DIALOGO = 20;

    public static final int ESPACIO_DIALOGO = 18;

    public static final int ESPACIO_FORMULARIO = 18;

    public static final int ANCHO_LABEL = 140;

    public static final int ALTO_FILA_FORMULARIO = 42;

    // ==========================================================
    // LOGIN
    // ==========================================================
    public static final int ANCHO_LOGIN = 950;

    public static final int ALTO_LOGIN = 600;

    // ==========================================================
    // DASHBOARD / PANELES
    //
    // PADDING_CONTENIDO y PADDING_DASHBOARD eran lo mismo (25px);
    // MARGEN_PANEL y PADDING_PANEL eran lo mismo (20px). Se dejó
    // un canónico por grupo.
    // ==========================================================
    public static final int PADDING_CONTENIDO = 25;

    /** alias de PADDING_CONTENIDO */
    public static final int PADDING_DASHBOARD = PADDING_CONTENIDO;

    public static final int MARGEN_PANEL = 20;

    /** alias de MARGEN_PANEL */
    public static final int PADDING_PANEL = MARGEN_PANEL;

    /** alias de MARGEN_PANEL */
    public static final int ESPACIO_PANEL = MARGEN_PANEL;

    public static final int ESPACIO_DASHBOARD = 20;

    public static final int COLUMNAS_DASHBOARD = 4;

    public static final int ALTO_ITEM_MENU = 46;

    public static final int PADDING_MENU = 16;

    public static final int PADDING_LABEL = 5;

    /** alias de ESPACIADO_MEDIANO (declarada más abajo) */
    public static final int ESPACIO_TITULO = 16;

    public static final int ESPACIO_SUBTITULO = 10;

    // ==========================================================
    // ESPACIADOS GENERALES
    // ==========================================================
    public static final int ESPACIADO_PEQUEÑO = 8;

    public static final int ESPACIADO_MEDIANO = 16;

    public static final int ESPACIADO_GRANDE = 24;

    public static final int SEPARACION_PEQUEÑA = 10;

    public static final int SEPARACION_MEDIANA = 18;

    public static final int SEPARACION_GRANDE = 30;

    public static final int ESPACIO_ICONO = 12;

    public static final int ESPACIO_ICONO_PEQUEÑO = 10;

    // ==========================================================
    // TOAST / ANIMACIONES
    // ==========================================================
    public static final int ANCHO_TOAST = 340;

    public static final int ALTO_TOAST = 80;

    public static final int TIEMPO_TOAST = 3000;

    public static final int DURACION_ANIMACION = 250;

    public static final int FPS_ANIMACION = 60;

    // ==========================================================
    // IDENTIDAD DE LA APLICACIÓN
    // ==========================================================
    public static final String NOMBRE_APLICACION = "FREDDY-FAZBEAR'S QUICK BITE";

    public static final String VERSION = "1.0";

    public static final String EMPRESA = "Freddy Fazbear Entertainment";

    public static final String COPYRIGHT = "© 2026 Freddy Fazbear Entertainment";

}
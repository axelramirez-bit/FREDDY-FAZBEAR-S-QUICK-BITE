package View.Utils;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import Config.Configuracion;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;
 
 
// ---- agregar como primer método público de la clase ----
 
/**
 * Aplica FlatLaf como base visual de todo Swing, y lo afina para
 * que combine con la paleta pirata (PaletaColores) en vez de
 * quedar con los colores por defecto de FlatLaf.
 *
 * Debe llamarse UNA sola vez, al inicio de Main, antes de crear
 * cualquier ventana:
 *
 *     AdministradorTema.inicializar();
 *     new Bienvenida().setVisible(true);
 */
/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * --------------------------------------------------------------- Administrador
 * del tema visual de la aplicación.
 *
 * Esta clase centraliza el acceso a:
 *
 * - Colores (delegando en PaletaColores) - Fuentes (delegando en UtilFuentes) -
 * Bordes - Espaciados, radios, tamaños de icono, medidas principales (delegando
 * en UIConstants)
 *
 * No almacena información propia — es la única puerta de entrada que las vistas
 * deben usar. Ninguna vista debe importar PaletaColores, UIConstants o
 * UtilFuentes directamente; siempre a través de AdministradorTema.
 *
 * PREPARADA PARA TEMA OSCURO: todos los colores pasan por PaletaColores.*,
 * nunca por un `new Color(...)` suelto aquí. El día que se implemente un modo
 * oscuro, solo hay que hacer que PaletaColores devuelva un set de colores
 * distinto según el modo activo — esta clase y las vistas que la usan no
 * cambian.
 *
 * PREPARADA PARA BADGES: ver la sección "EXTENSIÓN — BADGES" al final. No se
 * implementaron todavía porque ItemMenu no los soporta aún, pero el
 * tamaño/color que usarían ya está aquí.
 * ===============================================================
 */
public final class AdministradorTema {

    private AdministradorTema() {
    }

    // ==========================================================
    // IDENTIDAD DE LA APLICACIÓN
    // ==========================================================
    public static String nombreAplicacion() {
        return UIConstants.NOMBRE_APLICACION;
    }

    public static String version() {
        return UIConstants.VERSION;
    }

    public static String empresa() {
        return UIConstants.EMPRESA;
    }

    public static String copyright() {
        return UIConstants.COPYRIGHT;
    }

    // ==========================================================
    // COLORES
    // ==========================================================
    public static Color colorPrincipal() {
        return PaletaColores.PRINCIPAL;
    }

    public static Color colorSecundario() {
        return PaletaColores.SECUNDARIO;
    }

    public static Color colorFondo() {
        return PaletaColores.FONDO;
    }

    public static Color colorTarjeta() {
        return PaletaColores.TARJETA;
    }

    public static Color colorTexto() {
        return PaletaColores.TEXTO;
    }

    public static Color colorTextoBlanco() {
        return PaletaColores.TEXTO_BLANCO;
    }

    public static Color colorAcento() {
        return PaletaColores.ACENTO;
    }

    public static Color colorBorde() {
        return PaletaColores.BORDE;
    }

    public static Color colorSombra() {
        return PaletaColores.SOMBRA;
    }

    public static Color colorPrincipalHover() {
        return PaletaColores.PRINCIPAL_HOVER;
    }

    public static Color colorSecundarioHover() {
        return PaletaColores.SECUNDARIO_HOVER;
    }

    public static Color colorFondoSecundario() {
        return PaletaColores.FONDO_SECUNDARIO;
    }

    // ==========================================================
    // MENÚ LATERAL — COLORES
    // ==========================================================
    public static Color colorHoverMenu() {
        return colorPrincipal().brighter();
    }

    public static Color colorMenuSeleccionado() {
        return colorPrincipal();
    }

    public static Color colorMenuNormal() {
        return colorFondo();
    }

    // ==========================================================
    // FUENTES — ESCALA ANTIGUA (compatibilidad con código existente)
    // ==========================================================
    public static Font fuentePequeña() {
        return UtilFuentes.pequeña();
    }

    public static Font fuenteNormal() {
        return UtilFuentes.normal();
    }

    public static Font fuenteMediana() {
        return UtilFuentes.mediana();
    }

    public static Font fuenteTitulo() {
        return UtilFuentes.titulo();
    }

    public static Font fuentePequeñaNegrita() {
        return UtilFuentes.pequeñaNegrita();
    }

    public static Font fuenteNormalNegrita() {
        return UtilFuentes.normalNegrita();
    }

    public static Font fuenteMedianaNegrita() {
        return UtilFuentes.medianaNegrita();
    }

    public static Font fuenteTituloNegrita() {
        return UtilFuentes.tituloNegrita();
    }

    // ==========================================================
    // FUENTES — ESCALA DE DISEÑO NUEVA (Quicksand + Poppins)
    //
    // Usa estos 8 métodos en código nuevo en vez de los de arriba.
    // ==========================================================
    public static Font fuenteTituloSeccion() {
        return UtilFuentes.tituloSeccion();
    }

    public static Font fuenteSubtituloHeader() {
        return UtilFuentes.subtituloHeader();
    }

    public static Font fuenteTituloProducto() {
        return UtilFuentes.tituloProducto();
    }

    public static Font fuenteDescripcionProducto() {
        return UtilFuentes.descripcionProducto();
    }

    public static Font fuentePrecio() {
        return UtilFuentes.precio();
    }

    public static Font fuenteBoton() {
        return UtilFuentes.boton();
    }

    public static Font fuenteMenuLateral() {
        return UtilFuentes.menuLateral();
    }

    public static Font fuenteLogoEslogan() {
        return UtilFuentes.logoEslogan();
    }

    // ==========================================================
    // BORDES
    // ==========================================================
    public static Border bordeGeneral() {
        return BorderFactory.createEmptyBorder(
                UIConstants.ESPACIADO_MEDIANO,
                UIConstants.ESPACIADO_MEDIANO,
                UIConstants.ESPACIADO_MEDIANO,
                UIConstants.ESPACIADO_MEDIANO
        );
    }

    public static Border bordeTarjeta() {
        return BorderFactory.createEmptyBorder(
                UIConstants.ESPACIADO_GRANDE,
                UIConstants.ESPACIADO_GRANDE,
                UIConstants.ESPACIADO_GRANDE,
                UIConstants.ESPACIADO_GRANDE
        );
    }

    public static Border bordePequeño() {
        return BorderFactory.createEmptyBorder(
                UIConstants.ESPACIADO_PEQUEÑO,
                UIConstants.ESPACIADO_PEQUEÑO,
                UIConstants.ESPACIADO_PEQUEÑO,
                UIConstants.ESPACIADO_PEQUEÑO
        );
    }

    // ==========================================================
    // BOTONES
    // ==========================================================
    public static int anchoBoton() {
        return UIConstants.ANCHO_BOTON;
    }

    public static int anchoBotonIcono() {
        return UIConstants.ANCHO_BOTON_ICONO;
    }

    public static int anchoBotonMinimo() {
        return UIConstants.ANCHO_MINIMO_BOTON;
    }

    public static int anchoBotonMaximo() {
        return UIConstants.ANCHO_MAXIMO_BOTON;
    }

    public static int alturaBoton() {
        return UIConstants.ALTURA_BOTON;
    }

    public static int alturaBotonMenu() {
        return UIConstants.ALTURA_BOTON_MENU;
    }

    public static int radioBoton() {
        return UIConstants.RADIO_BOTON;
    }

    public static int paddingBotonVertical() {
        return UIConstants.PADDING_BOTON_VERTICAL;
    }

    public static int paddingBotonHorizontal() {
        return UIConstants.PADDING_BOTON_HORIZONTAL;
    }

    // ==========================================================
    // MENÚ LATERAL (SIDEBAR)
    // ==========================================================
    /**
     * Ancho del sidebar. Ya NO es un número fijo (antes 260px hardcodeado aquí)
     * — ahora es un porcentaje del ancho de pantalla (UIConstants.ANCHO_MENU),
     * así se ve proporcional en cualquier resolución, incluyendo pantallas
     * táctiles de kiosco más grandes o más pequeñas que un monitor normal.
     */
    public static int anchoMenuLateral() {
        return UIConstants.ANCHO_MENU;
    }

    public static int margenMenu() {
        return UIConstants.MARGEN_MENU;
    }

    public static int espacioMenuSuperior() {
        return UIConstants.ESPACIO_MENU_SUPERIOR;
    }

    public static int espacioIconoMenu() {
        return UIConstants.ESPACIO_ICONO_MENU;
    }

    public static int altoBotonMenu() {
        return UIConstants.ALTO_ITEM_MENU;
    }

    public static int radioMenu() {
        return UIConstants.RADIO_MENU;
    }

    public static int paddingHorizontalMenu() {
        return UIConstants.PADDING_HORIZONTAL_MENU;
    }

    public static int paddingVerticalMenu() {
        return UIConstants.PADDING_VERTICAL_MENU;
    }

    // ==========================================================
    // CAMPOS
    // ==========================================================
    public static int anchoCampo() {
        return UIConstants.ANCHO_CAMPO;
    }

    public static int alturaCampo() {
        return UIConstants.ALTURA_CAMPO;
    }

    public static int paddingCampoVertical() {
        return UIConstants.PADDING_CAMPO_VERTICAL;
    }

    public static int paddingCampoHorizontal() {
        return UIConstants.PADDING_CAMPO_HORIZONTAL;
    }

    // ==========================================================
    // BÚSQUEDA
    // ==========================================================
    public static int anchoBusqueda() {
        return UIConstants.ANCHO_BUSQUEDA;
    }

    public static int alturaBusqueda() {
        return UIConstants.ALTURA_BUSQUEDA;
    }

    public static int radioBusqueda() {
        return UIConstants.RADIO_BUSQUEDA;
    }

    // ==========================================================
    // COMBOBOX / SPINNER
    // ==========================================================
    public static int anchoCombo() {
        return UIConstants.ANCHO_COMBO;
    }

    public static int paddingComboVertical() {
        return UIConstants.PADDING_COMBO_VERTICAL;
    }

    public static int paddingComboHorizontal() {
        return UIConstants.PADDING_COMBO_HORIZONTAL;
    }

    public static int anchoSpinner() {
        return UIConstants.ANCHO_SPINNER;
    }

    // ==========================================================
    // SELECTOR DE CANTIDAD
    // ==========================================================
    public static int anchoSelectorCantidad() {
        return UIConstants.ANCHO_SELECTOR_CANTIDAD;
    }

    public static int altoSelectorCantidad() {
        return UIConstants.ALTO_SELECTOR_CANTIDAD;
    }

    public static int anchoBotonCantidad() {
        return UIConstants.ANCHO_BOTON_CANTIDAD;
    }

    public static int altoBotonCantidad() {
        return UIConstants.ALTO_BOTON_CANTIDAD;
    }

    // ==========================================================
    // TABLAS
    // ==========================================================
    public static int alturaFilaTabla() {
        return UIConstants.ALTURA_FILA_TABLA;
    }

    public static int alturaHeaderTabla() {
        return UIConstants.ALTURA_HEADER_TABLA;
    }

    // ==========================================================
    // SCROLL
    // ==========================================================
    public static int velocidadScroll() {
        return UIConstants.VELOCIDAD_SCROLL;
    }

    public static int anchoScroll() {
        return UIConstants.ANCHO_SCROLL;
    }

    // ==========================================================
    // TEXTAREA
    // ==========================================================
    public static int filasTextArea() {
        return UIConstants.TEXTAREA_FILAS;
    }

    public static int columnasTextArea() {
        return UIConstants.TEXTAREA_COLUMNAS;
    }

    // ==========================================================
    // TARJETAS
    // ==========================================================
    public static int anchoTarjeta() {
        return UIConstants.ANCHO_TARJETA;
    }

    public static int altoTarjeta() {
        return UIConstants.ALTO_TARJETA;
    }

    public static int radioTarjeta() {
        return UIConstants.RADIO_TARJETA;
    }

    // Estas 4 medidas SÍ se escalan según la resolución del monitor
    // (a diferencia del resto de la clase, que usa los valores fijos
    // de UIConstants). anchoTarjetaProducto()/altoTarjetaProducto()
    // ya no determinan el ancho final de la tarjeta en pantalla —
    // eso lo decide GridLayout(0,2) en Base.PanelProductos para
    // garantizar siempre 2 columnas — pero sí definen su ALTO base y
    // el tamaño de la imagen, que deben verse proporcionalmente más
    // grandes en un monitor grande y más chicos en una laptop.
    // DisenoAdaptable usa como referencia un diseño pensado para
    // 1920x1080 y escala hacia arriba o hacia abajo según la
    // resolución real detectada al iniciar la aplicación.
    public static int anchoTarjetaProducto() {
        return DisenoAdaptable.escalarAncho(UIConstants.ANCHO_TARJETA_PRODUCTO);
    }

    public static int altoTarjetaProducto() {
        return DisenoAdaptable.escalarAlto(UIConstants.ALTO_TARJETA_PRODUCTO);
    }

    public static int altoImagenProducto() {
        return DisenoAdaptable.escalarAlto(UIConstants.ALTO_IMAGEN_PRODUCTO);
    }

    public static int anchoImagenProducto() {
        return DisenoAdaptable.escalarAncho(UIConstants.ANCHO_IMAGEN_PRODUCTO);
    }

    // ==========================================================
    // PANELES
    // ==========================================================
    public static int radioPanel() {
        return UIConstants.RADIO_BORDE;
    }

    public static int margenPanel() {
        return UIConstants.MARGEN_PANEL;
    }

    public static int paddingContenido() {
        return UIConstants.PADDING_CONTENIDO;
    }

    public static int espacioEntreTarjetas() {
        return UIConstants.ESPACIO_ENTRE_TARJETAS;
    }

    // ==========================================================
    // ESPACIADOS
//    // ==========================================================
    public static int espacioPequeño() {
        return UIConstants.ESPACIADO_PEQUEÑO;
    }

    public static int espacioMediano() {
        return UIConstants.ESPACIADO_MEDIANO;
    }

    public static int espacioGrande() {
        return UIConstants.ESPACIADO_GRANDE;
    }

    public static int separacionPequeña() {
        return UIConstants.SEPARACION_PEQUEÑA;
    }

    public static int separacionMediana() {
        return UIConstants.SEPARACION_MEDIANA;
    }

    public static int separacionGrande() {
        return UIConstants.SEPARACION_GRANDE;
    }

    // ==========================================================
    // ICONOS
    // ==========================================================
    public static int iconoPequeño() {
        return UIConstants.ICONO_PEQUEÑO;
    }

    public static int iconoMediano() {
        return UIConstants.ICONO_MEDIANO;
    }

    public static int iconoGrande() {
        return UIConstants.ICONO_GRANDE;
    }

    public static int tamañoLogo() {
        return UIConstants.TAMAÑO_LOGO;
    }

    public static int iconoMenu() {
        return UIConstants.ICONO_MEDIANO;
    }

    public static int espacioIcono() {
        return UIConstants.ESPACIO_ICONO;
    }

    public static int espacioIconoPequeño() {
        return UIConstants.ESPACIO_ICONO_PEQUEÑO;
    }

    // ==========================================================
    // HEADER
    // ==========================================================
    public static int alturaEncabezado() {
        return UIConstants.ALTURA_ENCABEZADO;
    }

    // ==========================================================
    // VENTANA
    // ==========================================================
    public static int anchoMinimo() {
        return UIConstants.ANCHO_MINIMO;
    }

    public static int altoMinimo() {
        return UIConstants.ALTO_MINIMO;
    }

    public static int anchoLogin() {
        return UIConstants.ANCHO_LOGIN;
    }

    public static int altoLogin() {
        return UIConstants.ALTO_LOGIN;
    }

    public static int anchoDialogo() {
        return UIConstants.ANCHO_DIALOGO;
    }

    public static int altoDialogo() {
        return UIConstants.ALTO_DIALOGO;
    }

    // ==========================================================
    // EXTENSIÓN — BADGES (no implementado todavía)
    //
    // ItemMenu no soporta badges hoy. Cuando se agregue
    // setBadge(int) a ItemMenu, estos son los valores de diseño
    // que debería usar (tamaño de círculo, color) para que no se
    // inventen números nuevos en ese momento.
    // ==========================================================
    public static int tamañoBadge() {
        return UIConstants.ICONO_PEQUEÑO;
    }

    public static Color colorBadge() {
        return PaletaColores.ACENTO;
    }

    public static Color colorTextoBadge() {
        return PaletaColores.TEXTO_BLANCO;
    }

    public static void inicializar() {

        // Un solo color controla selección, foco, enlaces y sliders
        // en todo lo que FlatLaf administra (JComboBox, JCheckBox,
        // JTabbedPane, scrollbars, etc.) — verificado en
        // FlatLightLaf.properties: "@accentColor = ...".
        UIManager.put("@accentColor", aHex(colorPrincipal()));

        // Esquinas: mismas medidas que ya usan tus PanelRedondeado,
        // para que un JButton de FabricaBotones y un JComboBox sin
        // tocar se vean con la misma "redondez".
        UIManager.put("Button.arc", UIConstants.RADIO_BOTON);
        UIManager.put("Component.arc", UIConstants.RADIO_BOTON);
        UIManager.put("TextComponent.arc", UIConstants.RADIO_BOTON);
        UIManager.put("ScrollBar.thumbArc", 999); // 999 = totalmente redondo

        // Foco: FlatLaf por defecto dibuja un halo de color alrededor
        // del componente enfocado; con el borde ya definido en
        // EstilosComponentes alcanza, así que lo apagamos para que no
        // se dupliquen dos indicadores de foco distintos.
        UIManager.put("Component.focusWidth", 0);

        boolean temaOscuro = "dark".equalsIgnoreCase(Configuracion.getTema());

        boolean aplicado = temaOscuro ? FlatDarkLaf.setup() : FlatLightLaf.setup();

        if (!aplicado) {
            System.err.println("No se pudo aplicar FlatLaf, se usará el Look & Feel por defecto.");
        }
    }

    /**
     * Convierte un Color de Java a "RRGGBB", formato que espera @accentColor.
     */
    private static String aHex(java.awt.Color color) {
        return String.format("%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }
}
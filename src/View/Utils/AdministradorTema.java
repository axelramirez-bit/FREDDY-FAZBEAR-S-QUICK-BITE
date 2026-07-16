package View.Utils;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Administrador del tema visual de la aplicación.
 *
 * Esta clase centraliza el acceso a:
 *
 * • Colores
 * • Fuentes
 * • Bordes
 * • Espaciados
 * • Radios
 * • Tamaños de iconos
 * • Medidas principales
 *
 * No almacena información propia; reutiliza las clases:
 *
 * - PaletaColores
 * - UIConstants
 * - UtilFuentes
 *
 * De esta forma toda la interfaz mantiene un mismo estilo visual.
 * ===============================================================
 */
public final class AdministradorTema {

    private AdministradorTema() {
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
    // FUENTES
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
    // BORDES
    // ==========================================================

    /**
     * Borde interno estándar.
     */
    public static Border bordeGeneral() {

        return BorderFactory.createEmptyBorder(
                UIConstants.ESPACIADO_MEDIANO,
                UIConstants.ESPACIADO_MEDIANO,
                UIConstants.ESPACIADO_MEDIANO,
                UIConstants.ESPACIADO_MEDIANO
        );

    }

    /**
     * Borde interno para tarjetas.
     */
    public static Border bordeTarjeta() {

        return BorderFactory.createEmptyBorder(
                UIConstants.ESPACIADO_GRANDE,
                UIConstants.ESPACIADO_GRANDE,
                UIConstants.ESPACIADO_GRANDE,
                UIConstants.ESPACIADO_GRANDE
        );

    }

    /**
     * Borde interno pequeño.
     */
    public static Border bordePequeño() {

        return BorderFactory.createEmptyBorder(
                UIConstants.ESPACIADO_PEQUEÑO,
                UIConstants.ESPACIADO_PEQUEÑO,
                UIConstants.ESPACIADO_PEQUEÑO,
                UIConstants.ESPACIADO_PEQUEÑO
        );

    }



// ==========================================================
// IDENTIDAD DE LA APLICACIÓN
// ==========================================================

public static final String NOMBRE_APLICACION =
        "FREDDY-FAZBEAR'S QUICK BITE";

public static final String VERSION =
        "1.0";

public static final String EMPRESA =
        "Freddy Fazbear Entertainment";

public static final String COPYRIGHT =
        "© 2026 Freddy Fazbear Entertainment";
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
// ==========================================================
// MENÚ
// ==========================================================

public static int anchoMenu() {
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
// COMBOBOX
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

// ==========================================================
// SPINNER
// ==========================================================

public static int anchoSpinner() {
    return UIConstants.ANCHO_SPINNER;
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

public static int anchoTarjetaProducto() {
    return UIConstants.ANCHO_TARJETA_PRODUCTO;
}

public static int altoTarjetaProducto() {
    return UIConstants.ALTO_TARJETA_PRODUCTO;
}

public static int altoImagenProducto() {
    return UIConstants.ALTO_IMAGEN_PRODUCTO;
}

public static int anchoImagenProducto() {
    return UIConstants.ANCHO_IMAGEN_PRODUCTO ;
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
// ==========================================================

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


public static int anchoSelectorCantidad() {
    return 110;
}

public static int altoSelectorCantidad() {
    return 38;
}
public static int anchoBotonCantidad() {
    return 28;
}

public static int altoBotonCantidad() {
    return 28;

}
public static int anchoMenuLateral() {

    return 260;

}

public static int altoBotonMenu() {

    return 50;

}
}
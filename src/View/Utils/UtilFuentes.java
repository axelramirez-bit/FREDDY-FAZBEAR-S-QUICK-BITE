package View.Utils;

import java.awt.Font;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Utilidad para gestionar las fuentes del sistema.
 *
 * Todas las vistas deben obtener las fuentes desde esta clase
 * (nunca escribir "new Font(...)" en un panel) para mantener una
 * apariencia uniforme y para que un cambio de tipografía a futuro
 * se haga en un solo lugar.
 *
 * Depende de CargadorFuentes, que debe registrarse una sola vez
 * al inicio de la aplicación (ver Main.main()). Si por algún
 * motivo no se registró, cada método de aquí cae de forma segura
 * a la fuente de respaldo del sistema — nunca lanza una excepción.
 *
 * ESCALA DE DISEÑO (ver tabla de referencia del equipo):
 *
 * Elemento                    Fuente              Tamaño
 * ------------------------------------------------------------
 * Título de sección           Quicksand Bold      36-44px
 * Fecha / subtítulo header    Poppins Regular     13-14px
 * Título de producto          Quicksand SemiBold  18-20px
 * Descripción de producto     Poppins Regular     13-14px
 * Precio                      Poppins Bold        16-18px
 * Texto de botones            Poppins Medium      14-15px
 * Menú lateral (items)        Quicksand SemiBold  16-18px
 * Logo / eslogan (mayúsculas) Poppins SemiBold    10-12px
 * ===============================================================
 */
public final class UtilFuentes {

    private UtilFuentes() {
    }

    // ==========================================================
    // TÍTULO DE SECCIÓN
    // Ej: "Bebidas", "Antojos", "Ventas"
    // ==========================================================
    public static Font tituloSeccion() {
        return CargadorFuentes.obtener(
                "QUICKSAND_BOLD",
                UIConstants.TAMANO_TITULO_SECCION
        );
    }

    // ==========================================================
    // FECHA / SUBTÍTULO DEL HEADER
    // Ej: "Sábado, 27 de junio del 2026"
    // ==========================================================
    public static Font subtituloHeader() {
        return CargadorFuentes.obtener(
                "POPPINS_REGULAR",
                UIConstants.TAMANO_SUBTITULO_HEADER
        );
    }

    // ==========================================================
    // TÍTULO DEL PRODUCTO
    // Ej: "Granizado de Arándano"
    // ==========================================================
    public static Font tituloProducto() {
        return CargadorFuentes.obtener(
                "QUICKSAND_SEMIBOLD",
                UIConstants.TAMANO_TITULO_PRODUCTO
        );
    }

    // ==========================================================
    // DESCRIPCIÓN DE PRODUCTO
    // ==========================================================
    public static Font descripcionProducto() {
        return CargadorFuentes.obtener(
                "POPPINS_REGULAR",
                UIConstants.TAMANO_DESCRIPCION_PRODUCTO
        );
    }

    // ==========================================================
    // PRECIO
    // Ej: "Q38.00"
    // ==========================================================
    public static Font precio() {
        return CargadorFuentes.obtener(
                "POPPINS_BOLD",
                UIConstants.TAMANO_PRECIO
        );
    }

    // ==========================================================
    // TEXTO DE BOTONES
    // Ej: "Agregar al carrito"
    // ==========================================================
    public static Font boton() {
        return CargadorFuentes.obtener(
                "POPPINS_MEDIUM",
                UIConstants.TAMANO_BOTON
        );
    }

    // ==========================================================
    // MENÚ LATERAL (ITEMS)
    // Ej: "Desayunos", "Bebidas"
    // ==========================================================
    public static Font menuLateral() {
        return CargadorFuentes.obtener(
                "QUICKSAND_SEMIBOLD",
                UIConstants.TAMANO_MENU_LATERAL
        );
    }

    // ==========================================================
    // LOGO / ESLOGAN
    // Ej: "DIVERSIÓN Y SABOR EN CADA BOCADO" (escribir en mayúsculas
    // en la vista; esta clase solo controla fuente y tamaño)
    // ==========================================================
    public static Font logoEslogan() {
        return CargadorFuentes.obtener(
                "POPPINS_SEMIBOLD",
                UIConstants.TAMANO_LOGO_ESLOGAN
        );
    }

    // ==========================================================
    // COMPATIBILIDAD CON CÓDIGO YA ESCRITO
    //
    // Estos métodos ya existían y varias clases del proyecto
    // (ItemMenu, paneles, etc.) los llaman. Se mantienen para no
    // romper nada, pero ahora usan Quicksand/Poppins por debajo en
    // vez de la fuente única anterior. Si vas a escribir código
    // nuevo, usa los métodos semánticos de arriba
    // (tituloProducto(), precio(), etc.) en vez de estos.
    // ==========================================================

    public static Font pequeña() {
        return descripcionProducto();
    }

    public static Font normal() {
        return CargadorFuentes.obtener(
                "POPPINS_REGULAR",
                UIConstants.FUENTE_NORMAL
        );
    }

    public static Font mediana() {
        return CargadorFuentes.obtener(
                "POPPINS_MEDIUM",
                UIConstants.FUENTE_MEDIANA
        );
    }

    public static Font titulo() {
        return tituloSeccion();
    }

    public static Font pequeñaNegrita() {
        return CargadorFuentes.obtener(
                "POPPINS_SEMIBOLD",
                UIConstants.FUENTE_PEQUEÑA
        );
    }

    public static Font normalNegrita() {
        return CargadorFuentes.obtener(
                "POPPINS_SEMIBOLD",
                UIConstants.FUENTE_NORMAL
        );
    }

    public static Font medianaNegrita() {
        return CargadorFuentes.obtener(
                "QUICKSAND_SEMIBOLD",
                UIConstants.FUENTE_MEDIANA
        );
    }

    public static Font tituloNegrita() {
        return tituloSeccion();
    }

    /**
     * Método genérico anterior. Se mantiene por compatibilidad;
     * internamente ahora usa Poppins Regular o Bold según el
     * estilo pedido.
     *
     * @param estilo Font.PLAIN, Font.BOLD o Font.ITALIC
     * @param tamaño Tamaño de la fuente
     * @return Fuente correspondiente
     */
    public static Font obtenerFuente(int estilo, int tamaño) {

        String clave = (estilo == Font.BOLD)
                ? "POPPINS_BOLD"
                : "POPPINS_REGULAR";

        return CargadorFuentes.obtener(clave, tamaño);
    }

}
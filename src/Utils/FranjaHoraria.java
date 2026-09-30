// Paquete Utils
package Utils;

// Importa LocalTime (hora local)
import java.time.LocalTime;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Calcula, a partir de la hora local del equipo (LocalTime.now()),
 * si en este momento corresponde mostrar "Desayunos" o "Cenas" en
 * la opción dinámica del menú del Cliente (ver
 * Base.OpcionesCliente.DESAYUNOS_CENAS y
 * View.Autoservicio.Panels.PanelDesayunosCenas).
 *
 * MAPEO DE CATEGORÍAS: cada franja usa una categoría real de la
 * tabla categoria (ver FreddyQuickBite.sql):
 *
 *     Antes de HORA_CORTE  -> "Desayunos"
 *     Desde HORA_CORTE     -> "Almuerzos y Cenas"
 *
 * Un producto puede estar en varias categorías a la vez (tabla
 * producto_categoria), por eso, por ejemplo, una hamburguesa aparece
 * en "Hamburguesas" y también en "Almuerzos y Cenas", y un café
 * aparece en "McCafe" y también en "Desayunos".
 *
 * CORTE POR DEFECTO: antes de HORA_CORTE (12:00 mediodía, hora
 * local del equipo) se considera horario de Desayuno; desde esa
 * hora en adelante, horario de Cena. Ajusta HORA_CORTE si el
 * negocio necesita otro punto de corte (por ejemplo, mover el
 * cambio a las 11:00 o a las 15:00).
 * ===============================================================
 */
// Clase final que calcula la franja del día
public final class FranjaHoraria {

    // Constructor privado
    private FranjaHoraria() {
    }

    // Hora de corte entre desayuno y cena: 12:00
    private static final LocalTime HORA_CORTE = LocalTime.of(12, 0);

    // Enum con las dos franjas posibles
    public enum Franja {
        // Franja de desayuno
        DESAYUNO,
        // Franja de cena
        CENA
    }

    /**
     * Franja horaria actual según la hora local del equipo.
     */
    // Devuelve la franja actual
    public static Franja actual() {

        // Si la hora actual es anterior a la hora de corte
        return LocalTime.now().isBefore(HORA_CORTE)
                // devuelve DESAYUNO
                ? Franja.DESAYUNO
                // si no, devuelve CENA
                : Franja.CENA;
    }

    /**
     * Texto que debe mostrar la opción de menú ahora mismo.
     */
    // Devuelve el texto de la opción de menú
    public static String texto() {

        // "Desayunos" o "Cenas" según la franja
        return actual() == Franja.DESAYUNO ? "Desayunos" : "Cenas";
    }

    /**
     * Nombre del ícono (sin ruta ni extensión, tal como lo espera
     * View.Utils.UtilImagenes.icono/CacheImagenes) que corresponde
     * a la franja horaria actual. Ambos íconos ya existen en
     * Resources/Iconos, no hace falta agregar imágenes nuevas.
     */
    // Devuelve el nombre del ícono según la franja
    public static String nombreIcono() {

        // Si es desayuno
        return actual() == Franja.DESAYUNO
                // usa icon_desayunos
                ? "icon_desayunos"
                // si no, usa icon_almuerzoscenas
                : "icon_almuerzoscenas";
    }

    /**
     * Nombre real de la categoría (tal como está en la tabla
     * categoria) que corresponde a la franja horaria actual.
     *
     * @deprecated usar {@link #nombresCategoria()}.
     */
    // Devuelve la categoría a filtrar según la franja
    @Deprecated
    public static String nombreCategoria() {

        // "Desayunos" en la mañana, "Almuerzos y Cenas" desde el mediodía
        return actual() == Franja.DESAYUNO ? "Desayunos" : "Almuerzos y Cenas";
    }

    /**
     * Categorías reales a filtrar según la franja horaria actual
     * (ver el mapeo en el comentario de la clase). Devuelve un
     * arreglo para que PanelDesayunosCenas pueda filtrar contra
     * varias categorías si el negocio agrega más en el futuro.
     */
    // Devuelve las categorías a filtrar según la franja
    public static String[] nombresCategoria() {

        // "Desayunos" en la mañana; "Almuerzos y Cenas" desde el mediodía
        return actual() == Franja.DESAYUNO
                ? new String[]{"Desayunos"}
                : new String[]{"Almuerzos y Cenas"};
    }

}

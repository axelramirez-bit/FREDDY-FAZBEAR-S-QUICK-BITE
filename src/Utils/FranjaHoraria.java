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
 * MAPEO DE CATEGORÍAS: la migración que corrigió las categorías
 * (ver sp_migrar_categorias_hamburguesas_pizzas en
 * FreddyQuickBite.sql) dejó la base de datos así:
 *
 *     "Almuerzos y Cenas" -> "Hamburguesas" (las 4 hamburguesas)
 *     "Pizza Party Personal" se separó en su propia categoría
 *     nueva, "Pizzas".
 *
 * Hoy NO existen categorías llamadas literalmente "Desayunos" ni
 * "Cenas" conectadas a ningún panel activo del Autoservicio. Por
 * eso esta clase reutiliza las categorías ya migradas en vez de
 * inventar nombres que no traerían ningún producto: filtrar por
 * "Desayunos" a secas dejaría el panel vacío. Si el negocio llega a
 * crear categorías reales llamadas "Desayunos"/"Cenas", basta con
 * cambiar nombreCategoria() aquí abajo.
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
     * categoria) que hay que usar para filtrar productos según la
     * franja horaria actual. Ver el aviso de mapeo de categorías en
     * el comentario de la clase.
     */
    // Devuelve la categoría a filtrar según la franja
    public static String nombreCategoria() {

        // "Hamburguesas" en desayuno, "Pizzas" en cena
        return actual() == Franja.DESAYUNO ? "Hamburguesas" : "Pizzas";
    }

}

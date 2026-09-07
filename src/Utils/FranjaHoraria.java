package Utils;

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
 * MAPEO DE CATEGORÍAS: la migración que renombró categorías (ver
 * comentarios en PanelHamburguesas/PanelPizzas) dejó la base de
 * datos así:
 *
 *     "Desayunos"         -> "Hamburguesas" (id_categoria 1)
 *     "Almuerzos y Cenas" -> "Pizzas"       (id_categoria 2)
 *
 * Hoy NO existen categorías llamadas literalmente "Desayunos" ni
 * "Cenas" en FreddyQuickBite.sql. Por eso esta clase reutiliza las
 * categorías ya migradas en vez de inventar nombres que no
 * traerían ningún producto: filtrar por "Desayunos" a secas dejaría
 * el panel vacío. Si el negocio llega a crear categorías reales
 * llamadas "Desayunos"/"Cenas", basta con cambiar nombreCategoria()
 * aquí abajo.
 *
 * CORTE POR DEFECTO: antes de HORA_CORTE (12:00 mediodía, hora
 * local del equipo) se considera horario de Desayuno; desde esa
 * hora en adelante, horario de Cena. Ajusta HORA_CORTE si el
 * negocio necesita otro punto de corte (por ejemplo, mover el
 * cambio a las 11:00 o a las 15:00).
 * ===============================================================
 */
public final class FranjaHoraria {

    private FranjaHoraria() {
    }

    private static final LocalTime HORA_CORTE = LocalTime.of(12, 0);

    public enum Franja {
        DESAYUNO,
        CENA
    }

    /**
     * Franja horaria actual según la hora local del equipo.
     */
    public static Franja actual() {

        return LocalTime.now().isBefore(HORA_CORTE)
                ? Franja.DESAYUNO
                : Franja.CENA;
    }

    /**
     * Texto que debe mostrar la opción de menú ahora mismo.
     */
    public static String texto() {

        return actual() == Franja.DESAYUNO ? "Desayunos" : "Cenas";
    }

    /**
     * Nombre del ícono (sin ruta ni extensión, tal como lo espera
     * View.Utils.UtilImagenes.icono/CacheImagenes) que corresponde
     * a la franja horaria actual. Ambos íconos ya existen en
     * Resources/Iconos, no hace falta agregar imágenes nuevas.
     */
    public static String nombreIcono() {

        return actual() == Franja.DESAYUNO
                ? "icon_desayunos"
                : "icon_almuerzoscenas";
    }

    /**
     * Nombre real de la categoría (tal como está en la tabla
     * categoria) que hay que usar para filtrar productos según la
     * franja horaria actual. Ver el aviso de mapeo de categorías en
     * el comentario de la clase.
     */
    public static String nombreCategoria() {

        return actual() == Franja.DESAYUNO ? "Hamburguesas" : "Pizzas";
    }

}

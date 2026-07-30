package Base;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Identificadores de las dos opciones que existen igual en los
 * tres roles y que no navegan dentro del CardLayout como las
 * demás: Configuración y Cerrar sesión.
 *
 * Se separan de OpcionMenu porque su comportamiento es distinto
 * (Cerrar sesión no abre un panel, cierra la aplicación actual y
 * regresa a Bienvenida) — mezclarlas con las opciones normales
 * de navegación sería forzar un mismo contrato para dos
 * responsabilidades diferentes.
 * ===============================================================
 */
public final class IdVistaEspecial {

    private IdVistaEspecial() {
    }

    public static final String CONFIGURACION = "CONFIGURACION";

    public static final String CERRAR_SESION = "CERRAR_SESION";

}
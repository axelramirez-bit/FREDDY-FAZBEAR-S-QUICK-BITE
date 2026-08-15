package Base;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Roles del sistema.
 *
 * Se usa únicamente para que BarraLateral sepa qué conjunto de
 * opciones de menú debe construir (ver MenuPorRol). No representa
 * el rol de negocio completo (eso vive en Model.Rol / la tabla
 * "rol" de la base de datos) — este enum es solo para la capa
 * visual.
 * ===============================================================
 */
public enum Rol {

    CLIENTE,
    TRABAJADOR,
    ADMINISTRADOR

}
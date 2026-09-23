// Paquete Base: componentes comunes de los dashboards
package Base;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * --------------------------------------------------------------- Contrato para
 * reaccionar a lo que ocurre en BarraLateral.
 *
 * BarraLateral NUNCA llama directamente a ControlNavegacion. Solo avisa "se
 * presionó esta opción" y quien la contiene (normalmente DashboardBase) decide
 * qué hacer con eso.
 *
 * Esto permite reutilizar BarraLateral en cualquier contexto (incluso una
 * pantalla de pruebas) sin que dependa de que ControlNavegacion esté
 * inicializado. ===============================================================
 */
// Interfaz que avisa lo que ocurre en la barra lateral
public interface BarraLateralListener {

    /**
     * Se presionó una opción de navegación normal.
     *
     * @param idVista Identificador registrado en ControlNavegacion.
     */
    // Se llama al presionar una opción normal (recibe el id de la vista)
    void onOpcionSeleccionada(String idVista);

    /**
     * Se presionó "Configuración".
     */
    // Se llama al presionar Configuración
    void onConfiguracion();

    /**
     * Se presionó "Cerrar sesión".
     */
    // Se llama al presionar Cerrar sesión
    void onCerrarSesion();

}

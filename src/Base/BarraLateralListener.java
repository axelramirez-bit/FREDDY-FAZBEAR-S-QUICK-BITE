package Base;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Contrato para reaccionar a lo que ocurre en BarraLateral.
 *
 * BarraLateral NUNCA llama directamente a ControlNavegacion.
 * Solo avisa "se presionó esta opción" y quien la contiene
 * (normalmente DashboardBase) decide qué hacer con eso.
 *
 * Esto permite reutilizar BarraLateral en cualquier contexto
 * (incluso una pantalla de pruebas) sin que dependa de que
 * ControlNavegacion esté inicializado.
 * ===============================================================
 */
public interface BarraLateralListener {

    /**
     * Se presionó una opción de navegación normal.
     *
     * @param idVista Identificador registrado en ControlNavegacion.
     */
    void onOpcionSeleccionada(String idVista);

    /**
     * Se presionó "Configuración".
     */
    void onConfiguracion();

    /**
     * Se presionó "Cerrar sesión".
     */
    void onCerrarSesion();

}
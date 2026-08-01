package Base;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Opciones de menú del rol Trabajador.
 *
 * Nombres de icono verificados contra Resources/Iconos real.
 * ===============================================================
 */
public enum OpcionesTrabajador implements OpcionMenu {

    INICIO("Inicio", "icon_inicio", "INICIO_TRABAJADOR"),
    PENDIENTES("Pedidos pendientes", "icon_pedidos", "PEDIDOS_PENDIENTES"),
    EN_PREPARACION("En preparación", "icon_enpreparacion", "EN_PREPARACION"),
    LISTOS("Pedidos listos", "icon_pedidoslistos", "PEDIDOS_LISTOS"),
    HISTORIAL("Historial", "icon_historial", "HISTORIAL_TRABAJADOR");

    private final String texto;
    private final String nombreIcono;
    private final String idVista;

    OpcionesTrabajador(String texto, String nombreIcono, String idVista) {
        this.texto = texto;
        this.nombreIcono = nombreIcono;
        this.idVista = idVista;
    }

    @Override
    public String getTexto() {
        return texto;
    }

    @Override
    public String getNombreIcono() {
        return nombreIcono;
    }

    @Override
    public String getIdVista() {
        return idVista;
    }

}
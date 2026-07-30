package Base;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Opciones de menú del rol Cliente.
 *
 * Ajusta los idVista para que coincidan exactamente con los
 * nombres que registres en ControlNavegacion.registrarPanel(...)
 * al construir DashboardCliente.
 * ===============================================================
 */
public enum OpcionesCliente implements OpcionMenu {

    INICIO("Inicio", "icon_inicio", "INICIO_CLIENTE"),
    PRODUCTOS("Productos", "icon_productos", "PRODUCTOS"),
    PROMOCIONES("Promociones", "icon_promociones", "PROMOCIONES"),
    CARRITO("Carrito", "icon_carrito", "CARRITO");

    private final String texto;
    private final String nombreIcono;
    private final String idVista;

    OpcionesCliente(String texto, String nombreIcono, String idVista) {
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
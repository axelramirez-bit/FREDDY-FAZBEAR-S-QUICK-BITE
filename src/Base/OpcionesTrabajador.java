// Paquete Base
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
// Enum con las opciones del menú del Trabajador
public enum OpcionesTrabajador implements OpcionMenu {

    // Opción Inicio
    INICIO("Inicio", "icon_inicio", "INICIO_TRABAJADOR"),
    // Opción Pedidos pendientes
    PENDIENTES("Pedidos pendientes", "icon_pedidos", "PEDIDOS_PENDIENTES"),
    // Opción En preparación
    EN_PREPARACION("En preparación", "icon_enpreparacion", "EN_PREPARACION"),
    // Opción Pedidos listos
    LISTOS("Pedidos listos", "icon_pedidoslistos", "PEDIDOS_LISTOS"),
    // Opción Stock de productos
    STOCK("Stock de productos", "icon_comida", "STOCK_TRABAJADOR"),
    // Opción Historial
    HISTORIAL("Historial", "icon_historial", "HISTORIAL_TRABAJADOR");

    // Texto visible de la opción
    private final String texto;
    // Nombre del ícono
    private final String nombreIcono;
    // Id de la vista destino
    private final String idVista;

    // Constructor: recibe los tres datos de la opción
    OpcionesTrabajador(String texto, String nombreIcono, String idVista) {
        // Guarda el texto
        this.texto = texto;
        // Guarda el ícono
        this.nombreIcono = nombreIcono;
        // Guarda el id de la vista
        this.idVista = idVista;
    }

    // Implementa getTexto de OpcionMenu
    @Override
    public String getTexto() {
        // Devuelve el texto
        return texto;
    }

    // Implementa getNombreIcono de OpcionMenu
    @Override
    public String getNombreIcono() {
        // Devuelve el nombre del ícono
        return nombreIcono;
    }

    // Implementa getIdVista de OpcionMenu
    @Override
    public String getIdVista() {
        // Devuelve el id de la vista
        return idVista;
    }

}
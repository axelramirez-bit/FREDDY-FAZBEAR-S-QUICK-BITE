// Paquete Base
package Base;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Opciones de menú del rol Administrador.
 *
 * CORREGIDO: "icon_dashboard", "icon_productos" e
 * "icon_categorias" no existen en Resources/Iconos. Se
 * reemplazaron por iconos que sí existen (icon_admin,
 * icon_comida) donde tenía sentido reusarlos. "icon_categorias"
 * de verdad no tiene un ícono parecido disponible — hay que
 * diseñarlo/descargarlo y agregarlo a Resources/Iconos. Mientras
 * tanto no truena la app (UtilImagenes ya maneja el caso de
 * ícono faltante devolviendo uno vacío), pero se ve sin ícono.
 * ===============================================================
 */
// Enum con las opciones del menú del Administrador
public enum OpcionesAdministrador implements OpcionMenu {

    // Opción Dashboard (texto, ícono, id de vista)
    DASHBOARD("Dashboard", "icon_admin", "DASHBOARD_ADMIN"),
    // Opción Usuarios
    USUARIOS("Usuarios", "icon_usuarios", "USUARIOS"),
    // Opción Trabajadores
    TRABAJADORES("Trabajadores", "icon_trabajadores", "TRABAJADORES"),
    // Opción Productos
    PRODUCTOS("Productos", "icon_comida", "PRODUCTOS_ADMIN"),
    // Opción Categorías
    CATEGORIAS("Categorías", "icon_categorias", "CATEGORIAS"), // TODO: falta este archivo en Resources/Iconos
    // Opción Promociones
    PROMOCIONES("Promociones", "icon_promociones", "PROMOCIONES_ADMIN"),
    // Opción Pedidos
    PEDIDOS("Pedidos", "icon_pedidos", "PEDIDOS_ADMIN"),
    // Opción Pagos
    PAGOS("Pagos", "icon_pagos", "PAGOS"),
    // Opción Ventas
    VENTAS("Ventas", "icon_ventas", "VENTAS"),
    // Opción Reportes
    REPORTES("Reportes", "icon_reportes", "REPORTES");

    // Texto visible de la opción
    private final String texto;
    // Nombre del ícono
    private final String nombreIcono;
    // Id de la vista destino
    private final String idVista;

    // Constructor: recibe los tres datos de la opción
    OpcionesAdministrador(String texto, String nombreIcono, String idVista) {
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
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
public enum OpcionesAdministrador implements OpcionMenu {

    DASHBOARD("Dashboard", "icon_admin", "DASHBOARD_ADMIN"),
    USUARIOS("Usuarios", "icon_usuarios", "USUARIOS"),
    TRABAJADORES("Trabajadores", "icon_trabajadores", "TRABAJADORES"),
    PRODUCTOS("Productos", "icon_comida", "PRODUCTOS_ADMIN"),
    CATEGORIAS("Categorías", "icon_categorias", "CATEGORIAS"), // TODO: falta este archivo en Resources/Iconos
    PROMOCIONES("Promociones", "icon_promociones", "PROMOCIONES_ADMIN"),
    PEDIDOS("Pedidos", "icon_pedidos", "PEDIDOS_ADMIN"),
    PAGOS("Pagos", "icon_pagos", "PAGOS"),
    VENTAS("Ventas", "icon_ventas", "VENTAS"),
    REPORTES("Reportes", "icon_reportes", "REPORTES");

    private final String texto;
    private final String nombreIcono;
    private final String idVista;

    OpcionesAdministrador(String texto, String nombreIcono, String idVista) {
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
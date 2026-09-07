package Base;

import Utils.FranjaHoraria;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Opciones de menú del rol Cliente.
 *
 * Cubre los 12 paneles que ya existen en View/Panels/Cliente
 * (PanelInicio, PanelHamburguesas, ... PanelCarrito, PanelMisPedidos).
 *
 * "Combos" y "Mis pedidos" no tienen un ícono dedicado todavía en
 * Resources/Iconos — usé icon_comida e icon_historial como
 * temporales. Cambia el texto/orden/ícono libremente, es tu
 * decisión de diseño, no una regla técnica.
 * ===============================================================
 */
public enum OpcionesCliente implements OpcionMenu {

    INICIO("Inicio", "icon_inicio", "INICIO"),

    /**
     * Opción de HORA LOCAL: no tiene texto ni ícono fijos. Se
     * recalculan contra FranjaHoraria (hora local del equipo) cada
     * vez que se leen, así que muestra "Desayunos" + icon_desayunos
     * antes del mediodía y "Cenas" + icon_almuerzoscenas desde el
     * mediodía en adelante. El idVista SÍ es fijo (DESAYUNOS_CENAS):
     * es el mismo panel el que decide, también según la hora, qué
     * categoría de productos cargar (ver
     * View.Autoservicio.Panels.PanelDesayunosCenas).
     */
    DESAYUNOS_CENAS("Desayunos", "icon_desayunos", "DESAYUNOS_CENAS") {
        @Override
        public String getTexto() {
            return FranjaHoraria.texto();
        }

        @Override
        public String getNombreIcono() {
            return FranjaHoraria.nombreIcono();
        }
    },

    HAMBURGUESAS("Hamburguesas", "icon_comida", "HAMBURGUESAS"), // TODO: ícono propio pendiente (antes "Desayunos")
    PIZZAS("Pizzas", "icon_comida", "PIZZAS"), // TODO: ícono propio pendiente (antes "Almuerzos y cenas")
    POSTRES("Postres", "icon_postres", "POSTRES"),
    MCCAFE("McCafé", "icon_mccafe", "MCCAFE"),
    BEBIDAS("Bebidas", "icon_bebidas", "BEBIDAS"),
    ANTOJOS("Antojos", "icon_antojos", "ANTOJOS"),
    CAJITA_FELIZ("Cajita Feliz", "icon_cajitafeliz", "CAJITA_FELIZ"),
    COMBOS("Combos", "icon_comida", "COMBOS"), // TODO: ícono propio pendiente
    PROMOCIONES("Promociones", "icon_promociones", "PROMOCIONES_CLIENTE"),
    CARRITO("Carrito", "icon_carrito", "CARRITO"); // TODO: ícono propio pendiente

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
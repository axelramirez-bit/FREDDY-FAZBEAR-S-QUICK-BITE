// Paquete Base
package Base;

// Importa FranjaHoraria para el texto y el ícono dinámicos
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
// Enum con las opciones del menú del Cliente
public enum OpcionesCliente implements OpcionMenu {

    // Opción Inicio
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
    // Opción dinámica: Desayunos o Cenas según la hora
    DESAYUNOS_CENAS("Desayunos", "icon_desayunos", "DESAYUNOS_CENAS") {
        // Reemplaza el texto fijo
        @Override
        public String getTexto() {
            // Devuelve el texto según la franja horaria
            return FranjaHoraria.texto();
        }

        // Reemplaza el ícono fijo
        @Override
        public String getNombreIcono() {
            // Devuelve el ícono según la franja horaria
            return FranjaHoraria.nombreIcono();
        }
    },

    // Opción Hamburguesas
    HAMBURGUESAS("Hamburguesas", "icon_comida", "HAMBURGUESAS"), // TODO: ícono propio pendiente (antes "Desayunos")
    // Opción Pizzas
    PIZZAS("Pizzas", "icon_comida", "PIZZAS"), // TODO: ícono propio pendiente (antes "Almuerzos y cenas")
    // Opción Postres
    POSTRES("Postres", "icon_postres", "POSTRES"),
    // Opción McCafé
    MCCAFE("McCafé", "icon_mccafe", "MCCAFE"),
    // Opción Bebidas
    BEBIDAS("Bebidas", "icon_bebidas", "BEBIDAS"),
    // Opción Antojos
    ANTOJOS("Antojos", "icon_antojos", "ANTOJOS"),
    // Opción Cajita Feliz
    CAJITA_FELIZ("Cajita Feliz", "icon_cajitafeliz", "CAJITA_FELIZ"),
    // Opción Combos
    COMBOS("Combos", "icon_comida", "COMBOS"), // TODO: ícono propio pendiente
    // Opción Promociones
    PROMOCIONES("Promociones", "icon_promociones", "PROMOCIONES_CLIENTE"),
    // Opción Carrito
    CARRITO("Carrito", "icon_carrito", "CARRITO"); // TODO: ícono propio pendiente

    // Texto visible de la opción
    private final String texto;
    // Nombre del ícono
    private final String nombreIcono;
    // Id de la vista destino
    private final String idVista;

    // Constructor: recibe los tres datos de la opción
    OpcionesCliente(String texto, String nombreIcono, String idVista) {
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
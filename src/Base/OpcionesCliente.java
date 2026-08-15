package Base;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Opciones de menú del rol Cliente.
 *
 * Cubre los 12 paneles que ya existen en View/Panels/Cliente
 * (PanelInicio, PanelDesayunos, ... PanelCarrito, PanelMisPedidos).
 *
 * "Combos" y "Mis pedidos" no tienen un ícono dedicado todavía en
 * Resources/Iconos — usé icon_comida e icon_historial como
 * temporales. Cambia el texto/orden/ícono libremente, es tu
 * decisión de diseño, no una regla técnica.
 * ===============================================================
 */
public enum OpcionesCliente implements OpcionMenu {

    INICIO("Inicio", "icon_inicio", "INICIO"),
    DESAYUNOS("Desayunos", "icon_desayunos", "DESAYUNOS"),
    ALMUERZOS("Almuerzos y cenas", "icon_almuerzoscenas", "ALMUERZOS"),
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
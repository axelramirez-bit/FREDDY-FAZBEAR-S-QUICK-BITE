package View.Cliente.Panels;

import Base.PanelProductos;

public class PanelCombos extends PanelProductos {

    // NOTA: esta categoría "Combos" todavía no existe en la tabla
    // categoria de la base de datos (solo hay Desayunos, Almuerzos y
    // Cenas, Postres, McCafe, Bebidas, Antojos, Cajita Feliz y
    // Promociones). Este panel seguirá vacío hasta que se agregue
    // la categoría "Combos" y se reasignen los productos tipo combo
    // que hoy están mezclados dentro de "Cajita Feliz". Ver el script
    // SQL de migración que agrega esta categoría.
    public PanelCombos() {

        super(producto -> producto.getCategoria() != null
                && "Combos".equalsIgnoreCase(producto.getCategoria().getNombre()));
    }


}
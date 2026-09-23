package View.Autoservicio.Panels;

import Base.PanelProductos;

public class PanelCombos extends PanelProductos {

    // La categoría "Combos" ya existe en la tabla categoria y trae
    // sus propios productos ("Combo Golden Pizza-Burger", "Combo
    // Bonnie-Nuggets", "Combo Freddy Fazbear") desde el seed data
    // de FreddyQuickBite.sql. Las categorías completas de hoy son:
    // Hamburguesas, Pizzas, Postres, McCafe, Bebidas, Antojos,
    // Cajita Feliz, Combos y Promociones (ver
    // sp_migrar_categorias_hamburguesas_pizzas en FreddyQuickBite.sql
    // para el detalle de cómo se corrigieron Hamburguesas/Pizzas).
    public PanelCombos() {

        super(producto -> producto.perteneceACategoria("Combos"));
    }


}
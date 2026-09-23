package View.Autoservicio.Panels;

import Base.PanelProductos;

/**
 * Catálogo de la categoría "Hamburguesas" (antes "Almuerzos y
 * Cenas" — ver sp_migrar_categorias_hamburguesas_pizzas en
 * FreddyQuickBite.sql, o migracion_categorias_hamburguesas_pizzas.sql
 * si ya tienes datos y no quieres recrear la base).
 * Toda la lógica de carga, tarjetas y carrito vive en PanelProductos;
 * esta clase solo define el filtro.
 */
public class PanelHamburguesas extends PanelProductos {

    public PanelHamburguesas() {

        super(producto -> producto.perteneceACategoria("Hamburguesas"));
    }

}
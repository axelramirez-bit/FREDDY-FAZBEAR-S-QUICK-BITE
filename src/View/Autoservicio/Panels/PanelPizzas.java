package View.Autoservicio.Panels;

import Base.PanelProductos;

/**
 * Catálogo de la categoría "Pizzas" (antes "Almuerzos y Cenas" — ver
 * migracion_categorias_hamburguesas_pizzas.sql).
 * Toda la lógica de carga, tarjetas y carrito vive en PanelProductos;
 * esta clase solo define el filtro.
 */
public class PanelPizzas extends PanelProductos {

    public PanelPizzas() {

        super(producto -> producto.perteneceACategoria("Pizzas"));
    }

}
package View.Autoservicio.Panels;

import Base.PanelProductos;

/**
 * Catálogo de la categoría "Hamburguesas" (antes "Desayunos" — ver
 * migracion_categorias_hamburguesas_pizzas.sql).
 * Toda la lógica de carga, tarjetas y carrito vive en PanelProductos;
 * esta clase solo define el filtro.
 */
public class PanelHamburguesas extends PanelProductos {

    public PanelHamburguesas() {

        super(producto -> producto.perteneceACategoria("Hamburguesas"));
    }

}
package View.Cliente.Panels;

import Base.PanelProductos;

/**
 * Catálogo de la categoría "Desayunos".
 * Toda la lógica de carga, tarjetas y carrito vive en PanelProductos;
 * esta clase solo define el filtro.
 */
public class PanelDesayunos extends PanelProductos {

    public PanelDesayunos() {

        super(producto -> producto.perteneceACategoria("Desayunos"));
    }

}
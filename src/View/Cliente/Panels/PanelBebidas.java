package View.Cliente.Panels;

import Base.PanelProductos;


public class PanelBebidas extends PanelProductos {

    public PanelBebidas() {

        super(producto -> producto.perteneceACategoria("Bebidas"));
    }


}
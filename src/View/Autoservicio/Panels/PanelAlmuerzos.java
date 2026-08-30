package View.Autoservicio.Panels;

import Base.PanelProductos;


public class PanelAlmuerzos extends PanelProductos {

    public PanelAlmuerzos() {

        super(producto -> producto.perteneceACategoria("Almuerzos y Cenas"));
    }

}
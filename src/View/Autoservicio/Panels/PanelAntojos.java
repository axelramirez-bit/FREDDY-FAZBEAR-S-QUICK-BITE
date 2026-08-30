package View.Autoservicio.Panels;

import Base.PanelProductos;


public class PanelAntojos extends PanelProductos {

    public PanelAntojos() {

        super(producto -> producto.perteneceACategoria("Antojos"));
    }


}
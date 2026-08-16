package View.Cliente.Panels;

import Base.PanelProductos;


public class PanelAntojos extends PanelProductos {

    public PanelAntojos() {

        super(producto -> producto.getCategoria() != null
                && "Antojos".equalsIgnoreCase(producto.getCategoria().getNombre()));
    }


}
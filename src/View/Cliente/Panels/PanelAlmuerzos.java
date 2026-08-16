package View.Cliente.Panels;

import Base.PanelProductos;


public class PanelAlmuerzos extends PanelProductos {

    public PanelAlmuerzos() {

        super(producto -> producto.getCategoria() != null
                && "Almuerzos y Cenas".equalsIgnoreCase(producto.getCategoria().getNombre()));
    }

}
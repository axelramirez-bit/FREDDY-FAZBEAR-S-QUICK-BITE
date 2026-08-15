package View.Cliente.Panels;

import Base.PanelProductos;

public class PanelCombos extends PanelProductos {

    public PanelCombos() {

        super(producto -> producto.getPromocion() != null);
    }


}
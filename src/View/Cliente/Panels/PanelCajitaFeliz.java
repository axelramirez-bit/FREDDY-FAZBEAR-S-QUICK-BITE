package View.Cliente.Panels;

import Base.PanelProductos;


public class PanelCajitaFeliz extends PanelProductos {

    public PanelCajitaFeliz() {

        super(producto -> producto.getPromocion() != null);
    }


}
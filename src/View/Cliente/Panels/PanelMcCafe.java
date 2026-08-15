package View.Cliente.Panels;
import Base.PanelProductos;


public class PanelMcCafe extends PanelProductos {

    public PanelMcCafe() {

        super(producto -> producto.getPromocion() != null);
    }


}
package View.Cliente.Panels;
import Base.PanelProductos;



public class PanelInicio extends PanelProductos {

    public PanelInicio() {

        super(producto -> producto.getPromocion() != null);
    }

}
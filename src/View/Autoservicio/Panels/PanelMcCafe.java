package View.Autoservicio.Panels;
import Base.PanelProductos;


public class PanelMcCafe extends PanelProductos {

    public PanelMcCafe() {

        super(producto -> producto.perteneceACategoria("McCafe"));
    }


}
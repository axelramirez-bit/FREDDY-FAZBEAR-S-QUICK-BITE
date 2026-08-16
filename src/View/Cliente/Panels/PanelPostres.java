package View.Cliente.Panels;
import Base.PanelProductos;
public class PanelPostres extends PanelProductos {

    public PanelPostres() {

        super(producto -> producto.getCategoria() != null
                && "Postres".equalsIgnoreCase(producto.getCategoria().getNombre()));
    }

}
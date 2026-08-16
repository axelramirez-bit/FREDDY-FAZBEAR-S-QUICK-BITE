package View.Cliente.Panels;

import Base.PanelProductos;


public class PanelCajitaFeliz extends PanelProductos {

    public PanelCajitaFeliz() {

        super(producto -> producto.getCategoria() != null
                && "Cajita Feliz".equalsIgnoreCase(producto.getCategoria().getNombre()));
    }


}
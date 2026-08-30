package View.Autoservicio.Panels;

import Base.PanelProductos;


public class PanelCajitaFeliz extends PanelProductos {

    public PanelCajitaFeliz() {

        super(producto -> producto.perteneceACategoria("Cajita Feliz"));
    }


}
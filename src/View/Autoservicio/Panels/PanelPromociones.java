package View.Autoservicio.Panels;

import Base.PanelProductos;

/**
 * Productos con promoción activa. No filtra por categoría, sino por
 * si el producto tiene una Promocion asociada — muestra que el
 * filtro de PanelProductos no está atado únicamente a Categoria.
 */
public class PanelPromociones extends PanelProductos {

    public PanelPromociones() {

        super(producto -> producto.getPromocion() != null);
    }

}
package View.Autoservicio.Panels;

import Base.PanelProductos;
import Model.Producto;

/**
 * Productos con promoción activa. No filtra por categoría, sino por
 * si el producto tiene una Promocion asociada — muestra que el
 * filtro de PanelProductos no está atado únicamente a Categoria.
 *
 * BUG QUE ESTO CORRIGE: el filtro anterior era
 * "producto.getPromocion() != null", que solo comprueba que el
 * producto TENGA asignada una promoción en la base de datos, sin
 * importar si esa promoción ya venció, todavía no empieza o está
 * desactivada (estado = false). Con eso, un producto con una
 * promoción vencida seguía apareciendo en este panel como si
 * estuviera en oferta. Producto.tienePromocion() sí valida todo eso
 * (llama a Promocion.estaVigente()), así que es el filtro correcto
 * para "promociones que de verdad deben servirse ahora".
 */
public class PanelPromociones extends PanelProductos {

    public PanelPromociones() {

        super(Producto::tienePromocion);
    }

}
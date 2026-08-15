package Service;

import Model.Producto;
import java.util.ArrayList;
import java.util.List;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Servicio encargado de realizar búsquedas y filtros del sistema.
 *
 * Responsabilidades:
 *
 * • Buscar productos.
 * • Buscar usuarios.
 * • Buscar pedidos.
 * • Buscar promociones.
 * • Filtrar información.
 *
 * No contiene código de interfaz gráfica.
 * ===============================================================
 */
public final class ServicioBusqueda {

    /**
     * Constructor privado.
     */
    private ServicioBusqueda() {
    }

    // ==========================================================
    // PRODUCTOS
    // ==========================================================

    /**
     * Busca productos por nombre.
     *
     * @param productos Lista completa.
     * @param texto Texto buscado.
     * @return Productos encontrados.
     */
    public static List<Producto> buscarProductos(
            List<Producto> productos,
            String texto) {

        List<Producto> resultado = new ArrayList<>();

        if (texto == null || texto.isBlank()) {
            return productos;
        }

        texto = texto.toLowerCase();

        for (Producto producto : productos) {

            if (producto.getNombre()
                    .toLowerCase()
                    .contains(texto)) {

                resultado.add(producto);

            }

        }

        return resultado;

    }

}
// Paquete Service
package Service;

// Importa el modelo Producto
import Model.Producto;
// Importa ArrayList
import java.util.ArrayList;
// Importa List
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
// Clase final con búsquedas y filtros
public final class ServicioBusqueda {

    /**
     * Constructor privado.
     */
    // Constructor privado: no se instancia
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
    // Busca productos por nombre
    public static List<Producto> buscarProductos(
            // Lista completa de productos
            List<Producto> productos,
            // Texto buscado
            String texto) {

        // Crea la lista de resultados
        List<Producto> resultado = new ArrayList<>();

        // Si el texto es nulo o está vacío
        if (texto == null || texto.isBlank()) {
            // devuelve la lista completa
            return productos;
        }

        // Pasa el texto a minúsculas
        texto = texto.toLowerCase();

        // Recorre los productos
        for (Producto producto : productos) {

            // Si el nombre del producto
            if (producto.getNombre()
                    // en minúsculas
                    .toLowerCase()
                    // contiene el texto buscado
                    .contains(texto)) {

                // lo agrega al resultado
                resultado.add(producto);

            }

        }

        // Devuelve los productos encontrados
        return resultado;

    }

}
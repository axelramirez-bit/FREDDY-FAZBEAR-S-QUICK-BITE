package View.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.swing.ImageIcon;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Administrador del caché de imágenes.
 *
 * Responsabilidades:
 *
 * • Cargar imágenes una única vez.
 * • Guardarlas en memoria.
 * • Reutilizarlas durante toda la ejecución.
 * • Evitar cargar varias veces el mismo archivo.
 * • Mejorar el rendimiento de la aplicación.
 *
 * Esta clase trabaja junto con:
 *
 * UtilImagenes
 * FabricaIconos
 * ThemeManager
 *
 * Ninguna vista debe cargar imágenes directamente.
 * ===============================================================
 */
public final class CacheImagenes {

    /**
     * Caché de imágenes.
     */
    private static final Map<String, ImageIcon> CACHE = new HashMap<>();

    /**
     * Constructor privado.
     */
    private CacheImagenes() {
    }

    //==========================================================
    // MÉTODO GENERAL
    //==========================================================

    /**
     * Obtiene una imagen del caché.
     *
     * Si no existe, la carga utilizando el Supplier recibido,
     * la almacena y posteriormente la devuelve.
     *
     * @param clave Identificador único del recurso.
     * @param cargador Función que carga la imagen.
     * @return ImageIcon.
     */
    private static ImageIcon obtener(
            String clave,
            Supplier<ImageIcon> cargador) {

        ImageIcon imagen = CACHE.get(clave);

        if (imagen != null) {
            return imagen;
        }

        imagen = cargador.get();

        if (imagen != null) {
            CACHE.put(clave, imagen);
        }

        return imagen;

    }

    //==========================================================
    // ICONOS
    //==========================================================

    /**
     * Obtiene un icono del sistema.
     *
     * @param nombre Nombre del icono.
     * @param tamaño Tamaño.
     * @return ImageIcon.
     */
    public static ImageIcon obtenerIcono(
            String nombre,
            int tamaño) {

        return obtener(
                "ICONO_" + nombre + "_" + tamaño,
                () -> UtilImagenes.icono(nombre, tamaño)
        );

    }

    //==========================================================
    // PRODUCTOS
    //==========================================================

    /**
     * Obtiene la imagen de un producto.
     *
     * @param nombre Nombre del producto.
     * @param ancho Ancho.
     * @param alto Alto.
     * @return ImageIcon.
     */
    public static ImageIcon obtenerProducto(
            String nombre,
            int ancho,
            int alto) {

        return obtener(
                "PRODUCTO_" + nombre + "_" + ancho + "x" + alto,
                () -> UtilImagenes.producto(
                        nombre,
                        ancho,
                        alto
                )
        );

    }

    //==========================================================
    // IMÁGENES GENERALES
    //==========================================================

    /**
     * Obtiene una imagen general.
     *
     * @param nombre Nombre de la imagen.
     * @param ancho Ancho.
     * @param alto Alto.
     * @return ImageIcon.
     */
    public static ImageIcon obtenerImagen(
            String nombre,
            int ancho,
            int alto) {

        return obtener(
                "IMAGEN_" + nombre + "_" + ancho + "x" + alto,
                () -> UtilImagenes.imagen(
                        nombre,
                        ancho,
                        alto
                )
        );

    }

    //==========================================================
    // LOGOTIPO
    //==========================================================

    /**
     * Obtiene el logotipo oficial del sistema.
     *
     * @return ImageIcon.
     */
    public static ImageIcon obtenerLogotipo() {

        return obtener(
                "LOGOTIPO",
                UtilImagenes::logotipo
        );

    }

    //==========================================================
    // CONSULTAS
    //==========================================================

    /**
     * Indica si un recurso ya está cargado.
     *
     * @param clave Clave del recurso.
     * @return true si existe.
     */
    public static boolean existe(String clave) {

        return CACHE.containsKey(clave);

    }

    /**
     * Devuelve la cantidad de imágenes almacenadas.
     *
     * @return Total de imágenes.
     */
    public static int totalImagenes() {

        return CACHE.size();

    }

    //==========================================================
    // ADMINISTRACIÓN
    //==========================================================

    /**
     * Elimina un recurso del caché.
     *
     * @param clave Clave del recurso.
     */
    public static void eliminar(String clave) {

        CACHE.remove(clave);

    }

    /**
     * Vacía completamente el caché.
     */
    public static void limpiar() {

        CACHE.clear();

    }

}
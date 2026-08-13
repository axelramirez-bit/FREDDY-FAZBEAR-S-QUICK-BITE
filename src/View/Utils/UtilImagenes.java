package View.Utils;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 * Utilidades para cargar imágenes e iconos del proyecto.
 *
 * Todas las imágenes deben cargarse utilizando esta clase.
 *
 */
public final class UtilImagenes {
private static final String RUTA_ICONOS = "/Resources/Iconos/";
private static final String RUTA_IMAGENES = "/Resources/Imagenes/";
private static final String RUTA_PRODUCTOS = "/Resources/Productos/";
private static final String EXTENSION = ".png";
    private UtilImagenes() {
    }

    // ==========================================================
    // CARGAR IMAGEN
    // ==========================================================
    /**
     * Carga una imagen desde la carpeta Resources.
     *
     * @param ruta Ruta de la imagen.
     * @return ImageIcon
     */
    public static ImageIcon cargarImagen(String ruta) {

        URL url = UtilImagenes.class.getResource(ruta);

        if (url == null) {
            System.err.println("No se encontró la imagen: " + ruta);
            return new ImageIcon();
        }

        return new ImageIcon(url);
    }

    // ==========================================================
    // CARGAR IMAGEN ESCALADA
    // ==========================================================
    /**
     * Carga una imagen escalada.
     *
     * @param ruta Ruta de la imagen.
     * @param ancho Ancho deseado.
     * @param alto Alto deseado.
     * @return ImageIcon
     */
    public static ImageIcon cargarImagen(
            String ruta,
            int ancho,
            int alto) {

        ImageIcon icono = cargarImagen(ruta);

        Image imagen = icono.getImage().getScaledInstance(
                ancho,
                alto,
                Image.SCALE_SMOOTH);

        return new ImageIcon(imagen);
    }

public static ImageIcon icono(String nombre) {

    return cargarImagen(
            RUTA_ICONOS + nombre + EXTENSION);
}

public static ImageIcon icono(
        String nombre,
        int tamaño) {

    return cargarImagen(
            RUTA_ICONOS + nombre + EXTENSION,
            tamaño,
            tamaño);
}

public static ImageIcon icono(
        String nombre,
        int ancho,
        int alto) {

    return cargarImagen(
            RUTA_ICONOS + nombre + EXTENSION,
            ancho,
            alto);
}

    // ==========================================================
    // PRODUCTOS
    // ==========================================================
    /**
     * Carga una imagen de producto.
     *
     * @param nombre Nombre de la imagen.
     * @return ImageIcon
     */
public static ImageIcon producto(String nombre) {

    if (nombre == null || nombre.isBlank()) {
        return cargarImagen(RUTA_IMAGENES + "Comidarealista" + EXTENSION);
    }

    ImageIcon icono = cargarImagen(RUTA_PRODUCTOS + nombre + EXTENSION);

    if (icono.getIconWidth() <= 0) {
        // El producto tiene nombre de imagen pero el archivo no
        // existe en Resources/Productos (nombre desactualizado o
        // archivo faltante) -> imagen genérica de respaldo, en vez
        // de dejar la tarjeta en blanco.
        return cargarImagen(RUTA_IMAGENES + "Comidarealista" + EXTENSION);
    }

    return icono;
}

public static ImageIcon producto(
        String nombre,
        int ancho,
        int alto) {

    if (nombre == null || nombre.isBlank()) {
        return cargarImagen(RUTA_IMAGENES + "Comidarealista" + EXTENSION, ancho, alto);
    }

    ImageIcon icono = cargarImagen(RUTA_PRODUCTOS + nombre + EXTENSION);

    if (icono.getIconWidth() <= 0) {
        return cargarImagen(RUTA_IMAGENES + "Comidarealista" + EXTENSION, ancho, alto);
    }

    return cargarImagen(RUTA_PRODUCTOS + nombre + EXTENSION, ancho, alto);
}

    // ==========================================================
    // IMÁGENES GENERALES
    // ==========================================================
    /**
     * Carga una imagen de la carpeta Imagenes.
     *
     * @param nombre Nombre de la imagen.
     * @return ImageIcon
     */
public static ImageIcon imagen(String nombre) {

    return cargarImagen(
            RUTA_IMAGENES + nombre + EXTENSION);
}

public static ImageIcon imagen(
        String nombre,
        int ancho,
        int alto) {

    return cargarImagen(
            RUTA_IMAGENES + nombre + EXTENSION,
            ancho,
            alto);
}

    // ==========================================================
    // LOGOTIPO
    // ==========================================================
    /**
     * Devuelve el logotipo principal del sistema.
     *
     * @return ImageIcon
     */
    public static ImageIcon logotipo() {

        return imagen(
                "Logotipo",
                UIConstants.TAMAÑO_LOGO,
                UIConstants.TAMAÑO_LOGO);
    }

    public static ImageIcon imagenProducto(
        String nombre,
        int ancho,
        int alto) {

    return producto(nombre, ancho, alto);

}
}
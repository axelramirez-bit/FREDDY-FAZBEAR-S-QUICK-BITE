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

    // ==========================================================
    // ICONOS
    // ==========================================================
    /**
     * Carga un icono de la carpeta Icon.
     *
     * @param nombre Nombre del icono.
     * @return ImageIcon
     */
    public static ImageIcon icono(String nombre) {

        return cargarImagen("/Icon/" + nombre + ".png");
    }

    /**
     * Carga un icono escalado.
     *
     * @param nombre Nombre del icono.
     * @param tamaño Tamaño del icono.
     * @return ImageIcon
     */
    public static ImageIcon icono(
            String nombre,
            int tamaño) {

        return cargarImagen(
                "/Icon/" + nombre + ".png",
                tamaño,
                tamaño);
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

        return cargarImagen("/Productos/" + nombre + ".png");
    }

    /**
     * Carga una imagen de producto escalada.
     *
     * @param nombre Nombre del producto.
     * @param ancho Ancho.
     * @param alto Alto.
     * @return ImageIcon
     */
    public static ImageIcon producto(
            String nombre,
            int ancho,
            int alto) {

        return cargarImagen(
                "/Productos/" + nombre + ".png",
                ancho,
                alto);
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

        return cargarImagen("/Imagenes/" + nombre + ".png");
    }

    /**
     * Carga una imagen escalada de la carpeta Imagenes.
     *
     * @param nombre Nombre de la imagen.
     * @param ancho Ancho.
     * @param alto Alto.
     * @return ImageIcon
     */
    public static ImageIcon imagen(
            String nombre,
            int ancho,
            int alto) {

        return cargarImagen(
                "/Imagenes/" + nombre + ".png",
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

}

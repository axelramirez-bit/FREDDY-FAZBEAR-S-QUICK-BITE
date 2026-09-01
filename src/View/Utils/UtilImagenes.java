package View.Utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
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
    private static final String RUTA_BARRA_CARGA = "/Resources/BarraCarga/";
    private static final String EXTENSION = ".png";

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

        if (icono.getIconWidth() <= 0) {
            // Imagen no encontrada
            return icono;
        }

        Image imagen = icono.getImage().getScaledInstance(
                ancho,
                alto,
                Image.SCALE_SMOOTH);

        return new ImageIcon(imagen);
    }

    // ==========================================================
    // ICONOS
    // ==========================================================

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
    // BARRA DE CARGA
    // ==========================================================

    /**
     * Carga una imagen de la animación de la barra de carga.
     *
     * @param numero Número del frame.
     * @return ImageIcon del frame.
     */
    public static ImageIcon cargando(int numero) {

        return cargarImagen(
                RUTA_BARRA_CARGA + numero + EXTENSION);
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
            return cargarImagen(
                    RUTA_IMAGENES + "Comidarealista" + EXTENSION);
        }

        ImageIcon icono = cargarImagen(
                RUTA_PRODUCTOS + nombre + EXTENSION);

        if (icono.getIconWidth() <= 0) {

            return cargarImagen(
                    RUTA_IMAGENES + "Comidarealista" + EXTENSION);
        }

        return icono;
    }

    public static ImageIcon producto(
            String nombre,
            int ancho,
            int alto) {

        if (nombre == null || nombre.isBlank()) {

            return cargarImagen(
                    RUTA_IMAGENES + "Comidarealista" + EXTENSION,
                    ancho,
                    alto);
        }

        ImageIcon icono = cargarImagen(
                RUTA_PRODUCTOS + nombre + EXTENSION);

        if (icono.getIconWidth() <= 0) {

            return cargarImagen(
                    RUTA_IMAGENES + "Comidarealista" + EXTENSION,
                    ancho,
                    alto);
        }

        return cargarImagen(
                RUTA_PRODUCTOS + nombre + EXTENSION,
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

    // ==========================================================
    // IMAGEN DE PRODUCTO
    // ==========================================================

    public static ImageIcon imagenProducto(
            String nombre,
            int ancho,
            int alto) {

        return producto(nombre, ancho, alto);
    }

    // ==========================================================
    // IMAGEN DE PRODUCTO CUADRADA (SIN DEFORMAR)
    // ==========================================================

    /**
     * Carga una imagen de producto y la recorta al centro en forma
     * cuadrada ANTES de escalarla.
     *
     * BUG QUE ESTO CORRIGE: cargarImagen(ruta, ancho, alto) usa
     * getScaledInstance(ancho, alto), que ESTIRA la imagen al
     * tamaño exacto pedido sin respetar su proporción original. Si
     * la foto no es cuadrada (la mayoría de fotos reales no lo
     * son), el resultado sale deformado dentro del marco cuadrado
     * de la tarjeta. Este método recorta primero un cuadrado del
     * centro de la imagen (el lado más corto define el tamaño del
     * recorte) y solo entonces escala, así el resultado siempre es
     * cuadrado y nunca se ve "achatado".
     *
     * @param nombre Nombre de la imagen (sin extensión).
     * @param tamaño Ancho y alto final deseado (el resultado es
     * siempre cuadrado, tamaño x tamaño).
     * @return ImageIcon cuadrado, recortado al centro.
     */
    public static ImageIcon imagenProductoCuadrada(
            String nombre,
            int tamaño) {

        ImageIcon original = producto(nombre);

        if (original.getIconWidth() <= 0) {
            return original;
        }

        Image imagenBase = original.getImage();

        int anchoOriginal = original.getIconWidth();
        int altoOriginal = original.getIconHeight();
        int lado = Math.min(anchoOriginal, altoOriginal);

        int x = (anchoOriginal - lado) / 2;
        int y = (altoOriginal - lado) / 2;

        BufferedImage recorte = new BufferedImage(
                lado,
                lado,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = recorte.createGraphics();

        g2.drawImage(
                imagenBase,
                0, 0, lado, lado,
                x, y, x + lado, y + lado,
                null);

        g2.dispose();

        Image escalada = recorte.getScaledInstance(
                tamaño,
                tamaño,
                Image.SCALE_SMOOTH);

        return new ImageIcon(escalada);
    }
}
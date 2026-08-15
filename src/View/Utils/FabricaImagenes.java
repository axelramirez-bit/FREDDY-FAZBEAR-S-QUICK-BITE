package View.Utils;

import javax.swing.ImageIcon;

/**
 * ===============================================================
 * FREDDY FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Fábrica oficial de imágenes.
 *
 * Centraliza la carga de fotografías del sistema.
 * ===============================================================
 */
public final class FabricaImagenes {

    private FabricaImagenes() {
    }
    public static ImageIcon producto(String imagen) {

    return UtilImagenes.imagenProducto(
            imagen,
            AdministradorTema.anchoImagenProducto(),
            AdministradorTema.altoImagenProducto());

}


    //==========================================================
    // PRODUCTO PERSONALIZADO
    //==========================================================

    public static ImageIcon producto(
            String nombreImagen,
            int ancho,
            int alto) {

        return UtilImagenes.imagen(
                nombreImagen,
                ancho,
                alto);

    }

    //==========================================================
    // PERFIL
    //==========================================================

    public static ImageIcon perfil(String nombreImagen) {

        return UtilImagenes.imagen(
                nombreImagen,
                AdministradorTema.iconoGrande(),
                AdministradorTema.iconoGrande());

    }

    //==========================================================
    // FONDO
    //==========================================================

    public static ImageIcon fondo(
            String nombreImagen,
            int ancho,
            int alto) {

        return UtilImagenes.imagen(
                nombreImagen,
                ancho,
                alto);

    }
    
}
package View.Utils;

import java.util.regex.Pattern;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * --------------------------------------------------------------- Utilidad para
 * validar información ingresada por el usuario.
 *
 * Todas las ventanas deben utilizar esta clase para validar datos.
 * ===============================================================
 */
public final class Validaciones {

    private Validaciones() {
    }

    //==========================================================
    // CAMPOS VACÍOS
    //==========================================================
    public static boolean estaVacio(String texto) {

        return texto == null || texto.trim().isEmpty();

    }

    //==========================================================
    // CORREO
    //==========================================================
    private static final Pattern PATRON_CORREO
            = Pattern.compile(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
            );

    public static boolean correo(String correo) {

        if (estaVacio(correo)) {
            return false;
        }

        return PATRON_CORREO.matcher(correo).matches();

    }

    //==========================================================
    // CONTRASEÑA
    //==========================================================
    public static boolean contraseña(String contraseña) {

        if (estaVacio(contraseña)) {
            return false;
        }

        return contraseña.length() >= 8;

    }

    //==========================================================
    // TELÉFONO
    //==========================================================
    public static boolean telefono(String telefono) {

        if (estaVacio(telefono)) {
            return false;
        }

        return telefono.matches("\\d{8}");

    }

    //==========================================================
    // DPI GUATEMALA
    //==========================================================
    public static boolean dpi(String dpi) {

        if (estaVacio(dpi)) {
            return false;
        }

        return dpi.matches("\\d{13}");

    }

    //==========================================================
    // NIT
    //==========================================================
    public static boolean nit(String nit) {

        if (estaVacio(nit)) {
            return false;
        }

        return nit.matches("[0-9Kk-]+");

    }

    //==========================================================
    // NOMBRE
    //==========================================================
    public static boolean nombre(String nombre) {

        if (estaVacio(nombre)) {
            return false;
        }

        return nombre.length() >= 3;

    }

    //==========================================================
    // NÚMEROS POSITIVOS
    //==========================================================
    public static boolean numeroPositivo(int numero) {

        return numero > 0;

    }

    public static boolean decimalPositivo(double numero) {

        return numero > 0;

    }

}

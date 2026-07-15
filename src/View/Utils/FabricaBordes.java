package View.Utils;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Fábrica de bordes reutilizables.
 *
 * Centraliza todos los bordes utilizados
 * en la interfaz.
 * ===============================================================
 */
public final class FabricaBordes {

    private FabricaBordes() {
    }

    // ==========================================================
    // BORDES VACÍOS
    // ==========================================================

    public static Border vacio() {

        return BorderFactory.createEmptyBorder();

    }

    public static Border pequeño() {

        return BorderFactory.createEmptyBorder(
                UIConstants.ESPACIADO_PEQUEÑO,
                UIConstants.ESPACIADO_PEQUEÑO,
                UIConstants.ESPACIADO_PEQUEÑO,
                UIConstants.ESPACIADO_PEQUEÑO);

    }

    public static Border mediano() {

        return BorderFactory.createEmptyBorder(
                UIConstants.ESPACIADO_MEDIANO,
                UIConstants.ESPACIADO_MEDIANO,
                UIConstants.ESPACIADO_MEDIANO,
                UIConstants.ESPACIADO_MEDIANO);

    }

    public static Border grande() {

        return BorderFactory.createEmptyBorder(
                UIConstants.ESPACIADO_GRANDE,
                UIConstants.ESPACIADO_GRANDE,
                UIConstants.ESPACIADO_GRANDE,
                UIConstants.ESPACIADO_GRANDE);

    }

    // ==========================================================
    // LÍNEA
    // ==========================================================

    public static Border linea() {

        return BorderFactory.createLineBorder(
                AdministradorTema.colorBorde());

    }

    public static Border linea(Color color) {

        return BorderFactory.createLineBorder(color);

    }

    public static Border linea(Color color, int grosor) {

        return BorderFactory.createLineBorder(
                color,
                grosor);

    }

    // ==========================================================
    // COMPUESTO
    // ==========================================================

    public static Border compuesto(
            Border exterior,
            Border interior) {

        return BorderFactory.createCompoundBorder(
                exterior,
                interior);

    }

    // ==========================================================
    // CAMPOS
    // ==========================================================

    public static Border campo() {

        return compuesto(
                linea(),
                BorderFactory.createEmptyBorder(
                        UIConstants.PADDING_CAMPO_VERTICAL,
                        UIConstants.PADDING_CAMPO_HORIZONTAL,
                        UIConstants.PADDING_CAMPO_VERTICAL,
                        UIConstants.PADDING_CAMPO_HORIZONTAL));

    }

    // ==========================================================
    // COMBO
    // ==========================================================

    public static Border combo() {

        return compuesto(
                linea(),
                BorderFactory.createEmptyBorder(
                        UIConstants.PADDING_COMBO_VERTICAL,
                        UIConstants.PADDING_COMBO_HORIZONTAL,
                        UIConstants.PADDING_COMBO_VERTICAL,
                        UIConstants.PADDING_COMBO_HORIZONTAL));

    }

    // ==========================================================
    // BOTÓN
    // ==========================================================

    public static Border boton() {

        return BorderFactory.createEmptyBorder(
                UIConstants.PADDING_BOTON_VERTICAL,
                UIConstants.PADDING_BOTON_HORIZONTAL,
                UIConstants.PADDING_BOTON_VERTICAL,
                UIConstants.PADDING_BOTON_HORIZONTAL);

    }

}
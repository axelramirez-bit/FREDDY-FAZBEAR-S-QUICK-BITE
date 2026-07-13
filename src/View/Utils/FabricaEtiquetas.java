package View.Utils;

import javax.swing.*;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Fábrica especializada en la creación de etiquetas.
 *
 * Responsabilidades:
 *
 * • Títulos
 * • Subtítulos
 * • Texto
 * • Texto pequeño
 * • Etiquetas centradas
 *
 * Toda la apariencia es aplicada por EstilosComponentes.
 * ===============================================================
 */
public final class FabricaEtiquetas {

    private FabricaEtiquetas() {
    }

    // ==========================================================
    // TÍTULO
    // ==========================================================

    public static JLabel crearTitulo(String texto) {

        JLabel label = new JLabel(texto);

        EstilosComponentes.aplicarEtiquetaTitulo(label);

        return label;

    }

    // ==========================================================
    // SUBTÍTULO
    // ==========================================================

    public static JLabel crearSubtitulo(String texto) {

        JLabel label = new JLabel(texto);

        EstilosComponentes.aplicarEtiquetaSubtitulo(label);

        return label;

    }

    // ==========================================================
    // TEXTO
    // ==========================================================

    public static JLabel crearTexto(String texto) {

        JLabel label = new JLabel(texto);

        EstilosComponentes.aplicarEtiquetaTexto(label);

        return label;

    }

    // ==========================================================
    // TEXTO PEQUEÑO
    // ==========================================================

    public static JLabel crearPequeño(String texto) {

        JLabel label = new JLabel(texto);

        EstilosComponentes.aplicarEtiquetaPequeña(label);

        return label;

    }

    // ==========================================================
    // TEXTO CENTRADO
    // ==========================================================

    public static JLabel crearCentrado(String texto) {

        JLabel label = crearTexto(texto);

        label.setHorizontalAlignment(SwingConstants.CENTER);

        return label;

    }

    // ==========================================================
    // TÍTULO CENTRADO
    // ==========================================================

    public static JLabel crearTituloCentrado(String texto) {

        JLabel label = crearTitulo(texto);

        label.setHorizontalAlignment(SwingConstants.CENTER);

        return label;

    }

    // ==========================================================
    // SUBTÍTULO CENTRADO
    // ==========================================================

    public static JLabel crearSubtituloCentrado(String texto) {

        JLabel label = crearSubtitulo(texto);

        label.setHorizontalAlignment(SwingConstants.CENTER);

        return label;

    }

    // ==========================================================
    // ETIQUETA PERSONALIZADA
    // ==========================================================

    public static JLabel crearPersonalizada(String texto) {

        return new JLabel(texto);

    }

}
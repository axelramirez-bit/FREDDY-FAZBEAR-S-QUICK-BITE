package View.Utils;

import javax.swing.*;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Fábrica de componentes de selección.
 *
 * Contiene:
 *
 * • JCheckBox
 * • JRadioButton
 * • ButtonGroup
 * • JComboBox
 * • JSpinner
 *
 * Toda la apariencia es aplicada mediante EstilosComponentes.
 * ===============================================================
 */
public final class FabricaSeleccion {

    private FabricaSeleccion() {
    }

    // ==========================================================
    // CHECKBOX
    // ==========================================================

    public static JCheckBox crearCheckBox(String texto) {

        JCheckBox check = new JCheckBox(texto);

        EstilosComponentes.aplicarEstiloCheckBox(check);

        return check;
    }

    public static JCheckBox crearCheckBox(
            String texto,
            boolean seleccionado) {

        JCheckBox check = crearCheckBox(texto);

        check.setSelected(seleccionado);

        return check;
    }

    // ==========================================================
    // RADIO BUTTON
    // ==========================================================

    public static JRadioButton crearRadioButton(
            String texto) {

        JRadioButton radio =
                new JRadioButton(texto);

        EstilosComponentes.aplicarEstiloRadioButton(radio);

        return radio;
    }

    public static JRadioButton crearRadioButton(
            String texto,
            boolean seleccionado) {

        JRadioButton radio =
                crearRadioButton(texto);

        radio.setSelected(seleccionado);

        return radio;
    }

    // ==========================================================
    // BUTTON GROUP
    // ==========================================================

    /**
     * Crea un grupo de RadioButtons.
     */
    public static ButtonGroup crearGrupo(
            JRadioButton... radios) {

        ButtonGroup grupo = new ButtonGroup();

        for (JRadioButton radio : radios) {
            grupo.add(radio);
        }

        return grupo;
    }

    // ==========================================================
    // COMBO BOX
    // ==========================================================

    public static <T> JComboBox<T> crearCombo() {

        JComboBox<T> combo =
                new JComboBox<>();

        EstilosComponentes.aplicarEstiloCombo(combo);

        EstilosComponentes.aplicarTamañoPreferido(
                combo,
                AdministradorTema.anchoCombo(),
                AdministradorTema.alturaCampo());

        return combo;
    }

    public static <T> JComboBox<T> crearCombo(
            T[] elementos) {

        JComboBox<T> combo =
                new JComboBox<>(elementos);

        EstilosComponentes.aplicarEstiloCombo(combo);

        EstilosComponentes.aplicarTamañoPreferido(
                combo,
                AdministradorTema.anchoCombo(),
                AdministradorTema.alturaCampo());

        return combo;
    }

    // ==========================================================
    // SPINNER
    // ==========================================================

    public static JSpinner crearSpinner() {

        SpinnerNumberModel modelo =
                new SpinnerNumberModel(
                        1,
                        1,
                        9999,
                        1);

        JSpinner spinner =
                new JSpinner(modelo);

        EstilosComponentes.aplicarEstiloSpinner(spinner);

        EstilosComponentes.aplicarTamañoPreferido(
                spinner,
                UIConstants.ANCHO_SPINNER,
                AdministradorTema.alturaCampo());

        return spinner;
    }

    public static JSpinner crearSpinner(
            int valor,
            int minimo,
            int maximo,
            int paso) {

        SpinnerNumberModel modelo =
                new SpinnerNumberModel(
                        valor,
                        minimo,
                        maximo,
                        paso);

        JSpinner spinner =
                new JSpinner(modelo);

        EstilosComponentes.aplicarEstiloSpinner(spinner);

        EstilosComponentes.aplicarTamañoPreferido(
                spinner,
                UIConstants.ANCHO_SPINNER,
                AdministradorTema.alturaCampo());

        return spinner;
    }

}
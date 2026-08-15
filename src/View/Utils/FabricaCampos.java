package View.Utils;

import javax.swing.*;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Fábrica especializada en la creación de campos de entrada.
 *
 * Responsabilidades:
 *
 * • JTextField
 * • JPasswordField
 * • JTextArea
 * • JComboBox
 * • JSpinner
 *
 * Toda la apariencia es aplicada por EstilosComponentes.
 * ===============================================================
 */
public final class FabricaCampos {

    private FabricaCampos() {
    }

    // ==========================================================
    // CAMPO DE TEXTO
    // ==========================================================

    public static JTextField crearCampo() {

        JTextField campo = new JTextField();

        EstilosComponentes.aplicarEstiloCampo(campo);

        EstilosComponentes.aplicarTamañoPreferido(
                campo,
                AdministradorTema.anchoCampo(),
                AdministradorTema.alturaCampo());

        return campo;

    }

    // ==========================================================
    // CAMPO DE BÚSQUEDA
    // ==========================================================

    public static JTextField crearBusqueda() {

        JTextField campo = crearCampo();

        campo.setToolTipText("Buscar...");

        return campo;

    }

    // ==========================================================
    // PASSWORD
    // ==========================================================

    public static JPasswordField crearPassword() {

        JPasswordField password = new JPasswordField();

        EstilosComponentes.aplicarEstiloCampo(password);

        EstilosComponentes.aplicarTamañoPreferido(
                password,
                AdministradorTema.anchoCampo(),
                AdministradorTema.alturaCampo());

        return password;

    }

    // ==========================================================
    // COMBO
    // ==========================================================

    public static <T> JComboBox<T> crearCombo() {

        JComboBox<T> combo = new JComboBox<>();

        EstilosComponentes.aplicarEstiloCombo(combo);

        EstilosComponentes.aplicarTamañoPreferido(
                combo,
                AdministradorTema.anchoCombo(),
                AdministradorTema.alturaBoton());

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
                AdministradorTema.alturaBoton());

        return spinner;

    }

    // ==========================================================
    // ÁREA DE TEXTO
    // ==========================================================

    public static JTextArea crearTextArea() {

        JTextArea area = new JTextArea();

        EstilosComponentes.aplicarEstiloTextArea(area);

        return area;

    }

    // ==========================================================
    // ÁREA DE TEXTO CON SCROLL
    // ==========================================================

    public static JScrollPane crearTextAreaScroll() {

        JTextArea area = crearTextArea();

        return FabricaScroll.crear(area);

    }
    // ==========================================================
// CAMPO DE TEXTO
// ==========================================================

/**
 * Crea un JTextField con el estilo oficial del sistema.
 *
 * @return JTextField configurado.
 */
public static JTextField crearTexto() {

    JTextField campo = new JTextField();

    EstilosComponentes.aplicarEstiloCampo(campo);

    EstilosComponentes.aplicarTamaño(
            campo,
            AdministradorTema.anchoCampo(),
            AdministradorTema.alturaCampo());

    return campo;

}
}
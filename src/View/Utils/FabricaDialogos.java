package View.Utils;

import Utils.AppLogger;

import java.awt.*;
import javax.swing.*;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Fábrica de diálogos reutilizables.
 *
 * Contiene:
 *
 * • JOptionPane
 * • JDialog
 * • Confirmaciones
 * • Mensajes
 * • Errores
 * • Advertencias
 * • Excepciones (loguea + muestra el error en un solo paso)
 *
 * ===============================================================
 */
public final class FabricaDialogos {

    private FabricaDialogos() {
    }

    // ==========================================================
    // MENSAJES
    // ==========================================================

    public static void informacion(
            Component padre,
            String mensaje) {

        JOptionPane.showMessageDialog(
                padre,
                mensaje,
                UIConstants.NOMBRE_APLICACION,
                JOptionPane.INFORMATION_MESSAGE);

    }

    public static void advertencia(
            Component padre,
            String mensaje) {

        JOptionPane.showMessageDialog(
                padre,
                mensaje,
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);

    }

    public static void error(
            Component padre,
            String mensaje) {

        JOptionPane.showMessageDialog(
                padre,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE);

    }

    // ==========================================================
    // CONFIRMACIONES
    // ==========================================================

    public static boolean confirmar(
            Component padre,
            String mensaje) {

        return JOptionPane.showConfirmDialog(
                padre,
                mensaje,
                "Confirmar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE)
                == JOptionPane.YES_OPTION;

    }

    // ==========================================================
    // ENTRADA
    // ==========================================================

    public static String solicitarTexto(
            Component padre,
            String mensaje) {

        return JOptionPane.showInputDialog(
                padre,
                mensaje);

    }

    public static String solicitarTexto(
            Component padre,
            String mensaje,
            String valorInicial) {

        return JOptionPane.showInputDialog(
                padre,
                mensaje,
                valorInicial);

    }

    // ==========================================================
    // EXCEPCIONES
    // ==========================================================

    /**
     * Registra la excepción con AppLogger (queda el stacktrace
     * completo en el log, con fecha y clase de origen) y además
     * muestra el mensaje al usuario en un diálogo de error — en un
     * solo paso, para no repetir AppLogger.error(...) + error(...)
     * en cada catch.
     */
    public static void excepcion(
            Component padre,
            Class<?> clase,
            String mensaje,
            Throwable causa) {

        AppLogger.error(clase, mensaje, causa);

        error(padre, mensaje);
    }

    // ==========================================================
    // DIÁLOGO PERSONALIZADO
    // ==========================================================

    public static JDialog crearDialogo(
            Window owner,
            String titulo,
            JPanel contenido) {

        JDialog dialogo =
                new JDialog(owner, titulo,
                        Dialog.ModalityType.APPLICATION_MODAL);

        dialogo.setContentPane(contenido);

        dialogo.setSize(
                UIConstants.ANCHO_DIALOGO,
                UIConstants.ALTO_DIALOGO);

        dialogo.setLocationRelativeTo(owner);

        dialogo.setResizable(false);

        dialogo.setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE);

        return dialogo;

    }

}
package View.Utils;

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
                AdministradorTema.NOMBRE_APLICACION,
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
package View.Utils;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Administrador central de notificaciones.
 *
 * Responsabilidades:
 *
 * • Mostrar mensajes de información.
 * • Mostrar mensajes de éxito.
 * • Mostrar advertencias.
 * • Mostrar errores.
 * • Solicitar confirmaciones.
 * • Solicitar texto al usuario.
 *
 * Todas las vistas deben utilizar esta clase para mostrar
 * mensajes, evitando el uso directo de JOptionPane.
 * ===============================================================
 */
public final class AdministradorNotificaciones {

    /**
     * Constructor privado.
     */
    private AdministradorNotificaciones() {
    }

    // ==========================================================
    // INFORMACIÓN
    // ==========================================================

    public static void informacion(
            Component padre,
            String mensaje) {

        JOptionPane.showMessageDialog(
                padre,
                mensaje,
                AdministradorTema.NOMBRE_APLICACION,
                JOptionPane.INFORMATION_MESSAGE
        );

    }

    // ==========================================================
    // ÉXITO
    // ==========================================================

    public static void exito(
            Component padre,
            String mensaje) {

        JOptionPane.showMessageDialog(
                padre,
                mensaje,
                AdministradorTema.NOMBRE_APLICACION,
                JOptionPane.INFORMATION_MESSAGE
        );

    }

    // ==========================================================
    // ADVERTENCIA
    // ==========================================================

    public static void advertencia(
            Component padre,
            String mensaje) {

        JOptionPane.showMessageDialog(
                padre,
                mensaje,
                AdministradorTema.NOMBRE_APLICACION,
                JOptionPane.WARNING_MESSAGE
        );

    }

    // ==========================================================
    // ERROR
    // ==========================================================

    public static void error(
            Component padre,
            String mensaje) {

        JOptionPane.showMessageDialog(
                padre,
                mensaje,
                AdministradorTema.NOMBRE_APLICACION,
                JOptionPane.ERROR_MESSAGE
        );

    }

    // ==========================================================
    // CONFIRMAR
    // ==========================================================

    public static boolean confirmar(
            Component padre,
            String mensaje) {

        return JOptionPane.showConfirmDialog(
                padre,
                mensaje,
                AdministradorTema.NOMBRE_APLICACION,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        ) == JOptionPane.YES_OPTION;

    }

    // ==========================================================
    // CONFIRMAR ELIMINACIÓN
    // ==========================================================

    public static boolean confirmarEliminacion(
            Component padre,
            String elemento) {

        return confirmar(
                padre,
                "¿Está seguro que desea eliminar " + elemento + "?"
        );

    }

    // ==========================================================
    // SOLICITAR TEXTO
    // ==========================================================

    public static String solicitarTexto(
            Component padre,
            String mensaje) {

        return JOptionPane.showInputDialog(
                padre,
                mensaje
        );

    }

    // ==========================================================
    // OPCIONES
    // ==========================================================

    public static int opciones(
            Component padre,
            String mensaje,
            String[] opciones) {

        return JOptionPane.showOptionDialog(
                padre,
                mensaje,
                AdministradorTema.NOMBRE_APLICACION,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

    }

}
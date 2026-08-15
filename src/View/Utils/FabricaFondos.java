package View.Utils;

import java.awt.Color;

import javax.swing.JComponent;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Fábrica de fondos.
 *
 * Centraliza la aplicación de colores de fondo
 * para todos los componentes Swing.
 * ===============================================================
 */
public final class FabricaFondos {

    private FabricaFondos() {
    }

    // ==========================================================
    // FONDOS DEL SISTEMA
    // ==========================================================

    public static void aplicarPrincipal(JComponent componente) {

        componente.setBackground(
                AdministradorTema.colorPrincipal());

    }

    public static void aplicarSecundario(JComponent componente) {

        componente.setBackground(
                AdministradorTema.colorSecundario());

    }

    public static void aplicarFondo(JComponent componente) {

        componente.setBackground(
                AdministradorTema.colorFondo());

    }

    public static void aplicarTarjeta(JComponent componente) {

        componente.setBackground(
                AdministradorTema.colorTarjeta());

    }

    public static void aplicarAcento(JComponent componente) {

        componente.setBackground(
                AdministradorTema.colorAcento());

    }

    public static void aplicarPersonalizado(
            JComponent componente,
            Color color) {

        componente.setBackground(color);

    }

}
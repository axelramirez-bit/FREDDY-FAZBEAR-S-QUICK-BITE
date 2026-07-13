package View.Utils;

import java.awt.*;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Fábrica de Layouts reutilizables.
 *
 * Centraliza la creación de los LayoutManager utilizados
 * por toda la aplicación.
 *
 * Todos los espaciados provienen de UIConstants.
 * ===============================================================
 */
public final class FabricaLayouts {

    private FabricaLayouts() {
    }

    // ==========================================================
    // BORDER LAYOUT
    // ==========================================================

    /**
     * BorderLayout estándar.
     */
    public static BorderLayout crearBorder() {

        return new BorderLayout(
                UIConstants.ESPACIADO_MEDIANO,
                UIConstants.ESPACIADO_MEDIANO);

    }

    /**
     * BorderLayout personalizado.
     */
    public static BorderLayout crearBorder(
            int horizontal,
            int vertical) {

        return new BorderLayout(horizontal, vertical);

    }

    // ==========================================================
    // FLOW LAYOUT
    // ==========================================================

    public static FlowLayout crearFlowIzquierda() {

        return new FlowLayout(
                FlowLayout.LEFT,
                UIConstants.ESPACIADO_MEDIANO,
                UIConstants.ESPACIADO_MEDIANO);

    }

    public static FlowLayout crearFlowCentro() {

        return new FlowLayout(
                FlowLayout.CENTER,
                UIConstants.ESPACIADO_MEDIANO,
                UIConstants.ESPACIADO_MEDIANO);

    }

    public static FlowLayout crearFlowDerecha() {

        return new FlowLayout(
                FlowLayout.RIGHT,
                UIConstants.ESPACIADO_MEDIANO,
                UIConstants.ESPACIADO_MEDIANO);

    }

    public static FlowLayout crearFlow(
            int alineacion) {

        return new FlowLayout(
                alineacion,
                UIConstants.ESPACIADO_MEDIANO,
                UIConstants.ESPACIADO_MEDIANO);

    }

    public static FlowLayout crearFlow(
            int alineacion,
            int horizontal,
            int vertical) {

        return new FlowLayout(
                alineacion,
                horizontal,
                vertical);

    }

    // ==========================================================
    // GRID LAYOUT
    // ==========================================================

    public static GridLayout crearGrid(
            int filas,
            int columnas) {

        return new GridLayout(
                filas,
                columnas,
                UIConstants.ESPACIADO_MEDIANO,
                UIConstants.ESPACIADO_MEDIANO);

    }

    public static GridLayout crearGrid(
            int filas,
            int columnas,
            int horizontal,
            int vertical) {

        return new GridLayout(
                filas,
                columnas,
                horizontal,
                vertical);

    }

    // ==========================================================
    // GRID BAG
    // ==========================================================

    /**
     * GridBagLayout vacío.
     */
    public static GridBagLayout crearGridBag() {

        return new GridBagLayout();

    }

    /**
     * Constraints básicos.
     */
    public static GridBagConstraints crearConstraints() {

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets = new Insets(
                UIConstants.ESPACIADO_PEQUEÑO,
                UIConstants.ESPACIADO_PEQUEÑO,
                UIConstants.ESPACIADO_PEQUEÑO,
                UIConstants.ESPACIADO_PEQUEÑO);

        gbc.anchor = GridBagConstraints.WEST;

        gbc.fill = GridBagConstraints.HORIZONTAL;

        return gbc;

    }

    // ==========================================================
    // BOX LAYOUT
    // ==========================================================

    public static BoxLayout crearBoxVertical(
            JPanel panel) {

        return new BoxLayout(
                panel,
                BoxLayout.Y_AXIS);

    }

    public static BoxLayout crearBoxHorizontal(
            JPanel panel) {

        return new BoxLayout(
                panel,
                BoxLayout.X_AXIS);

    }

    // ==========================================================
    // CARD LAYOUT
    // ==========================================================

    public static CardLayout crearCard() {

        return new CardLayout(
                UIConstants.ESPACIADO_MEDIANO,
                UIConstants.ESPACIADO_MEDIANO);

    }

    // ==========================================================
    // OVERLAY
    // ==========================================================

    public static OverlayLayout crearOverlay(
            JPanel panel) {

        return new OverlayLayout(panel);

    }

}
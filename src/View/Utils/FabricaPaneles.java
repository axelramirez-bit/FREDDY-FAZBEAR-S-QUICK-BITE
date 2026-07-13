package View.Utils;

import View.Componentes.PanelRedondeado;
import java.awt.*;
import javax.swing.*;


/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Fábrica de paneles reutilizables.
 *
 * Contiene:
 *
 * • Paneles normales
 * • Paneles redondeados
 * • Tarjetas
 * • Paneles con título
 * • Paneles horizontales
 * • Paneles BorderLayout
 * ===============================================================
 */
public final class FabricaPaneles {

    private FabricaPaneles() {
    }

    // ==========================================================
    // PANEL CON TÍTULO
    // ==========================================================

    public static JPanel crearPanelTitulo(String titulo) {

        JPanel panel = new JPanel(new BorderLayout());

        panel.setOpaque(false);

        JLabel lblTitulo =
                FabricaEtiquetas.crearTitulo(titulo);

        lblTitulo.setBorder(
                AdministradorTema.bordePequeño());

        panel.add(lblTitulo, BorderLayout.NORTH);

        return panel;

    }

    // ==========================================================
    // TARJETA
    // ==========================================================

    public static JPanel crearTarjeta() {

        PanelRedondeado tarjeta =
                new PanelRedondeado(
                        AdministradorTema.radioTarjeta());

        EstilosComponentes.aplicarEstiloTarjeta(tarjeta);

        tarjeta.setLayout(new BorderLayout());

        return tarjeta;

    }

    // ==========================================================
    // TARJETA PERSONALIZADA
    // ==========================================================

    public static JPanel crearTarjeta(
            int ancho,
            int alto) {

        JPanel tarjeta = crearTarjeta();

        tarjeta.setPreferredSize(
                new Dimension(ancho, alto));

        return tarjeta;

    }

    // ==========================================================
    // TARJETA CON LAYOUT
    // ==========================================================

    public static JPanel crearTarjeta(
            LayoutManager layout) {

        PanelRedondeado tarjeta =
                new PanelRedondeado(
                        AdministradorTema.radioTarjeta());

        EstilosComponentes.aplicarEstiloTarjeta(tarjeta);

        tarjeta.setLayout(layout);

        return tarjeta;

    }

    // ==========================================================
    // PANEL REDONDEADO
    // ==========================================================

    public static PanelRedondeado crearPanelRedondeado() {

        PanelRedondeado panel =
                new PanelRedondeado(
                        AdministradorTema.radioPanel());

        EstilosComponentes.aplicarEstiloPanel(panel);

        return panel;

    }

    // ==========================================================
    // PANEL REDONDEADO CON COLOR
    // ==========================================================

    public static PanelRedondeado crearPanelRedondeado(
            Color color) {

        PanelRedondeado panel =
                crearPanelRedondeado();

        panel.setBackground(color);

        return panel;

    }

    // ==========================================================
    // PANEL REDONDEADO CON LAYOUT
    // ==========================================================

    public static PanelRedondeado crearPanelRedondeado(
            LayoutManager layout) {

        PanelRedondeado panel =
                crearPanelRedondeado();

        panel.setLayout(layout);

        return panel;

    }

    // ==========================================================
    // PANEL REDONDEADO PERSONALIZADO
    // ==========================================================

    public static PanelRedondeado crearPanelRedondeado(
            int radio) {

        PanelRedondeado panel =
                new PanelRedondeado(radio);

        EstilosComponentes.aplicarEstiloPanel(panel);

        return panel;

    }

    // ==========================================================
    // PANEL HORIZONTAL
    // ==========================================================

    public static JPanel crearPanelHorizontal() {

        JPanel panel = crearPanelRedondeado();

        panel.setLayout(new FlowLayout(
                FlowLayout.LEFT,
                AdministradorTema.espacioMediano(),
                AdministradorTema.espacioMediano()));

        return panel;

    }

    // ==========================================================
    // PANEL BORDER
    // ==========================================================

    public static JPanel crearPanelBorder() {

        JPanel panel = crearPanelRedondeado();

        panel.setLayout(new BorderLayout());

        return panel;

    }
    // ==========================================================
// PANEL BORDERLAYOUT
// ==========================================================

public static JPanel crearBorder() {

    JPanel panel = crearPanelRedondeado();

    panel.setLayout(new BorderLayout());

    return panel;

}
// ==========================================================
// PANEL FLOWLAYOUT
// ==========================================================

public static JPanel crearFlow() {

    JPanel panel = crearPanelRedondeado();

    panel.setLayout(
            new FlowLayout(
                    FlowLayout.LEFT,
                    AdministradorTema.espacioMediano(),
                    AdministradorTema.espacioMediano()));

    return panel;

}
// ==========================================================
// PANEL GRIDLAYOUT
// ==========================================================

public static JPanel crearGrid(
        int filas,
        int columnas) {

    JPanel panel = crearPanelRedondeado();

    panel.setLayout(
            new GridLayout(
                    filas,
                    columnas,
                    AdministradorTema.espacioMediano(),
                    AdministradorTema.espacioMediano()));

    return panel;

}
// ==========================================================
// PANEL BOXLAYOUT VERTICAL
// ==========================================================

public static JPanel crearVertical() {

    JPanel panel = crearPanelRedondeado();

    panel.setLayout(
            new BoxLayout(
                    panel,
                    BoxLayout.Y_AXIS));

    return panel;

}
// ==========================================================
// PANEL BOXLAYOUT HORIZONTAL
// ==========================================================

public static JPanel crearHorizontalBox() {

    JPanel panel = crearPanelRedondeado();

    panel.setLayout(
            new BoxLayout(
                    panel,
                    BoxLayout.X_AXIS));

    return panel;

}
// ==========================================================
// PANEL VACÍO
// ==========================================================

public static JPanel crear() {

    return crearPanelRedondeado();

}
// ==========================================================
// PANEL TRANSPARENTE
// ==========================================================

public static JPanel crearTransparente() {

    JPanel panel = new JPanel();

    panel.setOpaque(false);

    return panel;

}
// ==========================================================
// PANEL TRANSPARENTE BORDERLAYOUT
// ==========================================================

public static JPanel crearTransparenteBorder() {

    JPanel panel = crearTransparente();

    panel.setLayout(new BorderLayout());

    return panel;

}
}
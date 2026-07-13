package View.Componentes;

import View.Utils.PaletaColores;
import View.Utils.UIConstants;
import java.awt.*;
import javax.swing.JPanel;

/**
 * Panel reutilizable con esquinas redondeadas.
 *
 * Puede utilizarse para:
 * • Tarjetas
 * • Formularios
 * • Header
 * • Sidebar
 * • Contenedores
 *
 */
public class PanelRedondeado extends JPanel {

    //==========================================================
    // ATRIBUTOS
    //==========================================================

    private int radio;

    private Color colorFondo;

    private Color colorBorde;

    private int grosorBorde;

    private boolean mostrarBorde;

    //==========================================================
    // CONSTRUCTOR
    //==========================================================

    public PanelRedondeado() {

        this(
                UIConstants.RADIO_BORDE,
                PaletaColores.TARJETA
        );
    }

    public PanelRedondeado(int radio) {

        this(
                radio,
                PaletaColores.TARJETA
        );
    }

    public PanelRedondeado(Color colorFondo) {

        this(
                UIConstants.RADIO_BORDE,
                colorFondo
        );
    }

    public PanelRedondeado(
            int radio,
            Color colorFondo) {

        this.radio = radio;
        this.colorFondo = colorFondo;

        colorBorde = PaletaColores.BORDE;

        grosorBorde = 1;

        mostrarBorde = false;

        setOpaque(false);
    }

    //==========================================================
    // DIBUJAR PANEL
    //==========================================================

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // Fondo
        g2.setColor(colorFondo);

        g2.fillRoundRect(
                0,
                0,
                getWidth() - 1,
                getHeight() - 1,
                radio,
                radio
        );

        // Borde
        if (mostrarBorde) {

            g2.setStroke(
                    new BasicStroke(grosorBorde)
            );

            g2.setColor(colorBorde);

            g2.drawRoundRect(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    radio,
                    radio
            );
        }

        g2.dispose();

        super.paintComponent(g);
    }

    //==========================================================
    // GETTERS
    //==========================================================

    public int getRadio() {
        return radio;
    }

    public Color getColorFondo() {
        return colorFondo;
    }

    public Color getColorBorde() {
        return colorBorde;
    }

    public int getGrosorBorde() {
        return grosorBorde;
    }

    public boolean isMostrarBorde() {
        return mostrarBorde;
    }

    //==========================================================
    // SETTERS
    //==========================================================

    public void setRadio(int radio) {

        this.radio = radio;

        repaint();
    }

    public void setColorFondo(Color colorFondo) {

        this.colorFondo = colorFondo;

        repaint();
    }

    public void setColorBorde(Color colorBorde) {

        this.colorBorde = colorBorde;

        repaint();
    }

    public void setGrosorBorde(int grosorBorde) {

        this.grosorBorde = grosorBorde;

        repaint();
    }

    public void setMostrarBorde(boolean mostrarBorde) {

        this.mostrarBorde = mostrarBorde;

        repaint();
    }

}
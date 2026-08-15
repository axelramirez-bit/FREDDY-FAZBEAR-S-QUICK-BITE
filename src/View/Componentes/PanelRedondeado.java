package View.Componentes;

import View.Utils.AdministradorTema;
import java.awt.*;
import javax.swing.JPanel;

/**
 * =============================================================== PANEL
 * REDONDEADO ---------------------------------------------------------------
 * Panel reutilizable con esquinas redondeadas.
 *
 * Utilizado por:
 *
 * • Tarjetas • Formularios • Sidebar • Header • Contenedores
 *
 * Todo el estilo proviene del AdministradorTema.
 * ===============================================================
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
    // CONSTRUCTORES
    //==========================================================
    public PanelRedondeado() {

        this(
                AdministradorTema.radioPanel(),
                AdministradorTema.colorTarjeta()
        );

    }

    public PanelRedondeado(int radio) {

        this(
                radio,
                AdministradorTema.colorTarjeta()
        );

    }

    public PanelRedondeado(Color fondo) {

        this(
                AdministradorTema.radioPanel(),
                fondo
        );

    }

    public PanelRedondeado(
            int radio,
            Color fondo) {

        this.radio = radio;

        this.colorFondo = fondo;

        this.colorBorde
                = AdministradorTema.colorBorde();

        this.grosorBorde = 1;

        this.mostrarBorde = false;

        setOpaque(false);

    }

    //==========================================================
    // DIBUJADO
    //==========================================================
    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2
                = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(colorFondo);

        g2.fillRoundRect(
                0,
                0,
                getWidth() - 1,
                getHeight() - 1,
                radio,
                radio);

        if (mostrarBorde) {

            g2.setStroke(
                    new BasicStroke(grosorBorde));

            g2.setColor(colorBorde);

            g2.drawRoundRect(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    radio,
                    radio);

        }

        g2.dispose();

        super.paintComponent(g);

    }

    //==========================================================
    // CONFIGURACIÓN
    //==========================================================
    public void mostrarBorde(boolean mostrar) {

        this.mostrarBorde = mostrar;

        repaint();

    }

    public void colorFondo(Color color) {

        this.colorFondo = color;

        repaint();

    }

    public void colorBorde(Color color) {

        this.colorBorde = color;

        repaint();

    }

    public void grosorBorde(int grosor) {

        this.grosorBorde = grosor;

        repaint();

    }

    public void radio(int radio) {

        this.radio = radio;

        repaint();

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

    public void setColorFondo(Color color) {
        this.colorFondo = color;
        repaint();
    }

    public void setColorBorde(Color color) {
        this.colorBorde = color;
        repaint();
    }

    public void setMostrarBorde(boolean mostrar) {
        this.mostrarBorde = mostrar;
        repaint();
    }
}

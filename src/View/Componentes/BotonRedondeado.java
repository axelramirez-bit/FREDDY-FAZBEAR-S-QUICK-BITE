package View.Componentes;

import View.Utils.PaletaColores;
import View.Utils.UIConstants;
import View.Utils.UtilFuentes;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

/**
 * Botón reutilizable con esquinas redondeadas.
 *
 * Características:
 * • Bordes redondeados.
 * • Cambio de color al pasar el mouse.
 * • Cursor tipo mano.
 * • Colores personalizables.
 * • Compatible con iconos.
 *
 */
public class BotonRedondeado extends JButton {

    //==========================================================
    // ATRIBUTOS
    //==========================================================

    private int radio;

    private Color colorFondo;

    private Color colorHover;

    private Color colorTexto;

    private Color colorBorde;

    private int grosorBorde;

    private boolean mostrarBorde;

    private boolean mouseEncima;

    //==========================================================
    // CONSTRUCTORES
    //==========================================================

    public BotonRedondeado() {
        this("");
    }

    public BotonRedondeado(String texto) {

        super(texto);

        radio = UIConstants.RADIO_BOTON;

        colorFondo = PaletaColores.SECUNDARIO;

        colorHover = PaletaColores.SECUNDARIO_HOVER;

        colorTexto = PaletaColores.TEXTO;

        colorBorde = PaletaColores.BORDE;

        grosorBorde = 1;

        mostrarBorde = false;

        mouseEncima = false;

        configurarBoton();

        agregarEventos();
    }

    //==========================================================
    // CONFIGURACIÓN
    //==========================================================

    private void configurarBoton() {

        setFont(UtilFuentes.medianaNegrita());

        setForeground(colorTexto);

        setBorder(new EmptyBorder(
                10,
                20,
                10,
                20));

        setFocusPainted(false);

        setContentAreaFilled(false);

        setBorderPainted(false);

        setOpaque(false);

        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    //==========================================================
    // EVENTOS
    //==========================================================

    private void agregarEventos() {

        addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {

                mouseEncima = true;

                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {

                mouseEncima = false;

                repaint();
            }

        });

    }

    //==========================================================
    // DIBUJAR BOTÓN
    //==========================================================

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo
        g2.setColor(mouseEncima ? colorHover : colorFondo);

        g2.fillRoundRect(
                0,
                0,
                getWidth() - 1,
                getHeight() - 1,
                radio,
                radio);

        // Borde
        if (mostrarBorde) {

            g2.setColor(colorBorde);

            g2.setStroke(new BasicStroke(grosorBorde));

            g2.drawRoundRect(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    radio,
                    radio);

        }

        // Texto
        g2.setColor(colorTexto);

        g2.setFont(getFont());

        FontMetrics fm = g2.getFontMetrics();

        int x = (getWidth() - fm.stringWidth(getText())) / 2;

        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

        g2.drawString(getText(), x, y);

        g2.dispose();
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

    public Color getColorHover() {
        return colorHover;
    }

    public Color getColorTexto() {
        return colorTexto;
    }

    public Color getColorBorde() {
        return colorBorde;
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

    public void setColorHover(Color colorHover) {

        this.colorHover = colorHover;

        repaint();
    }

    public void setColorTexto(Color colorTexto) {

        this.colorTexto = colorTexto;

        setForeground(colorTexto);

        repaint();
    }

    public void setMostrarBorde(boolean mostrarBorde) {

        this.mostrarBorde = mostrarBorde;

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

}
package View.Componentes;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class FondoPanel extends JPanel {

    private Image imagenFondo;

    public FondoPanel() {
    }

    public FondoPanel(String rutaImagen) {
        this.imagenFondo = Toolkit.getDefaultToolkit().getImage(rutaImagen);
    }

    public void setImagenFondo(String rutaImagen) {
        this.imagenFondo = Toolkit.getDefaultToolkit().getImage(rutaImagen);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
package View.Cliente.Panels.Carrito;

import View.Utils.AdministradorTema;
import View.Utils.PaletaColores;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Indicador de pasos del wizard de compra:
 *
 *   (1) Carrito ── (2) Entrega y Pago ── (3) Confirmación ── (4) Factura
 *
 * Estados por paso: PENDIENTE (gris), ACTUAL (rojo, número) y
 * COMPLETADO (rojo, check). Se usa dentro de PanelCarrito, arriba
 * del contenido de cada paso.
 * ===============================================================
 */
public class StepperCarrito extends JPanel {

    private static final String[] TITULOS = {
        "Carrito", "Entrega y Pago", "Confirmación", "Factura"
    };

    private final Circulo[] circulos = new Circulo[TITULOS.length];
    private final JLabel[] etiquetas = new JLabel[TITULOS.length];

    public StepperCarrito() {

        setOpaque(false);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < TITULOS.length; i++) {

            gbc.gridx = i * 2;
            gbc.weightx = 0;
            add(construirItem(i), gbc);

            if (i < TITULOS.length - 1) {
                gbc.gridx = i * 2 + 1;
                gbc.weightx = 1;
                add(new LineaConectora(), gbc);
            }
        }

        setPasoActual(1);
    }

    private JPanel construirItem(int indice) {

        JPanel item = new JPanel(new BorderLayout(0, 6));
        item.setOpaque(false);

        Circulo circulo = new Circulo(indice + 1);
        circulos[indice] = circulo;

        JPanel envoltorioCirculo = new JPanel();
        envoltorioCirculo.setOpaque(false);
        envoltorioCirculo.add(circulo);

        JLabel etiqueta = new JLabel(TITULOS[indice], SwingConstants.CENTER);
        etiqueta.setFont(AdministradorTema.fuentePequeñaNegrita());
        etiquetas[indice] = etiqueta;

        item.add(envoltorioCirculo, BorderLayout.NORTH);
        item.add(etiqueta, BorderLayout.SOUTH);

        return item;
    }

    /**
     * Marca el paso indicado (1 a 4) como actual; los anteriores
     * quedan como completados (check verde/rojo) y los siguientes
     * como pendientes (gris).
     */
    public void setPasoActual(int numeroPaso) {

        for (int i = 0; i < circulos.length; i++) {

            int paso = i + 1;

            if (paso < numeroPaso) {
                circulos[i].setEstado(EstadoPaso.COMPLETADO);
                etiquetas[i].setForeground(AdministradorTema.colorPrincipal());
            } else if (paso == numeroPaso) {
                circulos[i].setEstado(EstadoPaso.ACTUAL);
                etiquetas[i].setForeground(AdministradorTema.colorPrincipal());
            } else {
                circulos[i].setEstado(EstadoPaso.PENDIENTE);
                etiquetas[i].setForeground(Color.GRAY);
            }
        }

        repaint();
    }

    private enum EstadoPaso {
        PENDIENTE, ACTUAL, COMPLETADO
    }

    /**
     * Círculo numerado (o con check si el paso ya se completó).
     */
    private static class Circulo extends JComponent {

        private static final int DIAMETRO = 34;

        private final int numero;
        private EstadoPaso estado = EstadoPaso.PENDIENTE;

        Circulo(int numero) {
            this.numero = numero;
            setPreferredSize(new Dimension(DIAMETRO, DIAMETRO));
        }

        void setEstado(EstadoPaso estado) {
            this.estado = estado;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            boolean activo = estado != EstadoPaso.PENDIENTE;

            Color fondo = activo ? AdministradorTema.colorPrincipal() : Color.WHITE;
            Color borde = activo ? AdministradorTema.colorPrincipal() : PaletaColores.BORDE;

            g2.setColor(fondo);
            g2.fillOval(0, 0, DIAMETRO - 1, DIAMETRO - 1);

            g2.setColor(borde);
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(0, 0, DIAMETRO - 1, DIAMETRO - 1);

            g2.setColor(activo ? Color.WHITE : Color.GRAY);
            g2.setFont(AdministradorTema.fuenteMedianaNegrita());

            String texto = estado == EstadoPaso.COMPLETADO ? "\u2713" : String.valueOf(numero);

            FontMetrics fm = g2.getFontMetrics();
            int x = (DIAMETRO - fm.stringWidth(texto)) / 2;
            int y = (DIAMETRO - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(texto, x, y);

            g2.dispose();
        }
    }

    /**
     * Línea horizontal que conecta dos círculos consecutivos.
     */
    private static class LineaConectora extends JComponent {

        LineaConectora() {
            setPreferredSize(new Dimension(40, Circulo.DIAMETRO));
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(PaletaColores.BORDE);
            g2.setStroke(new BasicStroke(2f));

            int y = getHeight() / 2;
            g2.drawLine(0, y, getWidth(), y);

            g2.dispose();
        }
    }
}

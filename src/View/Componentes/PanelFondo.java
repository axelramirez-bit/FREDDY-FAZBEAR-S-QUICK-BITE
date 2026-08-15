package View.Componentes;

import View.Utils.UtilImagenes;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Panel base que pinta como fondo la imagen
 * Resources/Imagenes/FondoPanel.png, estirada para llenar todo el
 * panel.
 *
 * Úsalo como clase base de cualquier panel de contenido que deba
 * verse con ese fondo (los paneles de categoría del Cliente, por
 * ejemplo):
 *
 *     public class PanelDesayunos extends PanelFondo {
 *         public PanelDesayunos() {
 *             super();
 *             // agregar aquí los componentes propios del panel
 *         }
 *     }
 *
 * AVISO: ya existe View.Componentes.FondoPanel.java en el
 * proyecto, con el mismo propósito (pintar una imagen de fondo),
 * solo que recibe la ruta de la imagen por parámetro en vez de
 * traer una fija. Antes de usar las dos, decide cuál se queda:
 * lo más limpio es tener una sola clase para esto, no dos con
 * nombres parecidos. Si prefieres seguir con FondoPanel.java,
 * dile a FondoPanel que apunte a "/Resources/Imagenes/FondoPanel.png"
 * por defecto y bórrame — el resultado visual es idéntico.
 * ===============================================================
 */
public class PanelFondo extends JPanel {

    private Image imagenFondo;

    public PanelFondo() {

        setOpaque(false);

        cargarFondo();
    }

    private void cargarFondo() {

        // UtilImagenes.imagen("FondoPanel") ya construye la ruta
        // completa (/Resources/Imagenes/FondoPanel.png) — no hace
        // falta repetir la ruta a mano aquí.
        ImageIcon icono = UtilImagenes.imagen("FondoPanel");

        this.imagenFondo = icono.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (imagenFondo != null) {

            g.drawImage(
                    imagenFondo,
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    this
            );
        }
    }

}
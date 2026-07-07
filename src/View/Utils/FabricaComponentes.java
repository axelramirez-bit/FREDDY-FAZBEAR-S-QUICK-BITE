package View.Utils;

import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Fabrica de componentes reutilizables.
 *
 * Todos los componentes creados desde aquí
 * tendrán el mismo estilo.
 *
 * @author Axel
 */
public final class FabricaComponentes {

    private FabricaComponentes() {
    }

    //==========================================================
    // BOTONES
    //==========================================================

    public static JButton crearBoton(String texto) {

        JButton boton = new JButton(texto);

        boton.setFont(UtilFuentes.medianaNegrita());

        boton.setBackground(PaletaColores.SECUNDARIO);

        boton.setForeground(PaletaColores.TEXTO);

        boton.setFocusPainted(false);

        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.setBorder(BorderFactory.createEmptyBorder(
                8,
                15,
                8,
                15));

        return boton;
    }

    //==========================================================
    // LABEL
    //==========================================================

    public static JLabel crearEtiqueta(String texto) {

        JLabel etiqueta = new JLabel(texto);

        etiqueta.setFont(UtilFuentes.normal());

        etiqueta.setForeground(PaletaColores.TEXTO);

        return etiqueta;
    }

    //==========================================================
    // TITULO
    //==========================================================

    public static JLabel crearTitulo(String texto) {

        JLabel titulo = new JLabel(texto);

        titulo.setFont(UtilFuentes.tituloNegrita());

        titulo.setForeground(PaletaColores.PRINCIPAL);

        return titulo;
    }

    //==========================================================
    // PANEL
    //==========================================================

    public static JPanel crearPanel() {

        JPanel panel = new JPanel();

        panel.setBackground(PaletaColores.FONDO);

        return panel;
    }

    //==========================================================
    // PANEL BLANCO
    //==========================================================

    public static JPanel crearPanelTarjeta() {

        JPanel panel = new JPanel();

        panel.setBackground(PaletaColores.TARJETA);

        return panel;
    }

    //==========================================================
    // TEXTFIELD
    //==========================================================

    public static JTextField crearCampoTexto() {

        JTextField campo = new JTextField();

        campo.setFont(UtilFuentes.normal());

        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        PaletaColores.BORDE),
                BorderFactory.createEmptyBorder(
                        8,
                        10,
                        8,
                        10)));

        return campo;
    }

    //==========================================================
    // BOTÓN CON ICONO
    //==========================================================

    public static JButton crearBoton(
            String texto,
            String icono) {

        JButton boton = crearBoton(texto);

        boton.setIcon(
                UtilImagenes.icono(
                        icono,
                        UIConstants.ICONO_MEDIANO));

        return boton;
    }

    //==========================================================
    // TAMAÑO
    //==========================================================

    public static void tamañoFijo(
            JButton boton,
            int ancho,
            int alto) {

        Dimension tamaño =
                new Dimension(ancho, alto);

        boton.setPreferredSize(tamaño);

        boton.setMinimumSize(tamaño);

        boton.setMaximumSize(tamaño);
    }

}
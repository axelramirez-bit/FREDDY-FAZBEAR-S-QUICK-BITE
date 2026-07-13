package View.Componentes;

import View.Utils.PaletaColores;
import View.Utils.UIConstants;
import View.Utils.UtilFuentes;
import View.Utils.UtilImagenes;
import java.awt.*;
import javax.swing.*;
/**
 * Botón utilizado exclusivamente en el Panel Lateral.
 *
 * @author Axel
 */
public class BotonMenu extends BotonRedondeado {

    //==========================================================
    // ATRIBUTOS
    //==========================================================

    private boolean seleccionado;

    //==========================================================
    // CONSTRUCTORES
    //==========================================================

    public BotonMenu(String texto, String icono) {

        super(texto);

        configurar();

        setIcon(UtilImagenes.icono(
                icono,
                UIConstants.ICONO_MEDIANO));

    }

    //==========================================================
    // CONFIGURACIÓN
    //==========================================================

    private void configurar() {

        seleccionado = false;

        setHorizontalAlignment(SwingConstants.LEFT);

        setHorizontalTextPosition(SwingConstants.RIGHT);

        setIconTextGap(15);

        setFont(UtilFuentes.normalNegrita());

        setColorFondo(PaletaColores.FONDO);

        setColorHover(PaletaColores.SECUNDARIO);

        setColorTexto(PaletaColores.TEXTO);

        setBorder(BorderFactory.createEmptyBorder(
                10,
                20,
                10,
                20));

        setCursor(new Cursor(Cursor.HAND_CURSOR));

        setPreferredSize(new Dimension(
                UIConstants.ANCHO_MENU - 20,
                UIConstants.ALTURA_BOTON_MENU));

    }

    //==========================================================
    // SELECCIÓN
    //==========================================================

    public void setSeleccionado(boolean seleccionado) {

        this.seleccionado = seleccionado;

        if (seleccionado) {

            setColorFondo(PaletaColores.PRINCIPAL);

            setColorTexto(PaletaColores.TEXTO_BLANCO);

        } else {

            setColorFondo(PaletaColores.FONDO);

            setColorTexto(PaletaColores.TEXTO);

        }

        repaint();

    }

    public boolean isSeleccionado() {

        return seleccionado;

    }

}
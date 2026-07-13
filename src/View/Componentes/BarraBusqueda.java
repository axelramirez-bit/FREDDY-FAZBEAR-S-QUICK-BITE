package View.Componentes;

import View.Utils.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Barra de búsqueda reutilizable.
 *
 * Incluye:
 * - Icono de búsqueda.
 * - Campo de texto.
 * - Placeholder.
 * - Diseño moderno.
 *

 */
public class BarraBusqueda extends PanelRedondeado {

    //==========================================================
    // COMPONENTES
    //==========================================================

    private JLabel lblIcono;

    private JTextField txtBuscar;

    //==========================================================
    // CONSTRUCTOR
    //==========================================================

    public BarraBusqueda() {

        inicializarComponentes();

    }

    //==========================================================
    // INICIALIZAR
    //==========================================================

    private void inicializarComponentes() {

        configurarPanel();

        crearIcono();

        crearCampoTexto();

    }

    //==========================================================
    // PANEL
    //==========================================================

    private void configurarPanel() {

        setLayout(new BorderLayout(10, 0));

        setColorFondo(Color.WHITE);

        setMostrarBorde(true);

        setBorder(new EmptyBorder(
                8,
                15,
                8,
                15));

        setPreferredSize(new Dimension(
                300,
                45));

    }

    //==========================================================
    // ICONO
    //==========================================================

    private void crearIcono() {

        lblIcono = new JLabel();

        ImageIcon icono = UtilImagenes.icono(
                "icon_buscar",
                UIConstants.ICONO_MEDIANO);

        lblIcono.setIcon(icono);

        add(lblIcono, BorderLayout.WEST);

    }

    //==========================================================
    // CAMPO
    //==========================================================

    private void crearCampoTexto() {

        txtBuscar = new JTextField();

        txtBuscar.setBorder(null);

        txtBuscar.setOpaque(false);

        txtBuscar.setForeground(PaletaColores.TEXTO);

        txtBuscar.setFont(UtilFuentes.normal());

        txtBuscar.setText("Buscar...");

        txtBuscar.setCaretColor(PaletaColores.PRINCIPAL);

        txtBuscar.addFocusListener(new java.awt.event.FocusAdapter() {

            @Override
            public void focusGained(java.awt.event.FocusEvent e) {

                if (txtBuscar.getText().equals("Buscar...")) {

                    txtBuscar.setText("");

                }

            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {

                if (txtBuscar.getText().trim().isEmpty()) {

                    txtBuscar.setText("Buscar...");

                }

            }

        });

        add(txtBuscar, BorderLayout.CENTER);

    }

    //==========================================================
    // GETTERS
    //==========================================================

    /**
     * Devuelve el texto escrito por el usuario.
     *
     * @return String
     */
    public String getTexto() {

        if (txtBuscar.getText().equals("Buscar...")) {

            return "";

        }

        return txtBuscar.getText();

    }

    /**
     * Devuelve el JTextField.
     *
     * Permite agregar DocumentListener o KeyListener.
     */
    public JTextField getCampoTexto() {

        return txtBuscar;

    }

    //==========================================================
    // SETTERS
    //==========================================================

    /**
     * Cambia el texto del placeholder.
     *
     * @param texto Placeholder
     */
    public void setPlaceholder(String texto) {

        txtBuscar.setText(texto);

    }

    /**
     * Limpia la búsqueda.
     */
    public void limpiar() {

        txtBuscar.setText("");

    }

}
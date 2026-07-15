package View.Componentes;

import View.Utils.*;

import java.awt.*;

import javax.swing.*;

import javax.swing.event.DocumentListener;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Barra de búsqueda reutilizable.
 *
 * Características:
 *
 * • Panel redondeado.
 * • Icono configurable.
 * • Placeholder configurable.
 * • Campo reutilizable.
 * • Compatible con DocumentListener.
 * • Compatible con filtros en tiempo real.
 * ===============================================================
 */
public class BarraBusqueda extends PanelRedondeado {

    //==========================================================
    // COMPONENTES
    //==========================================================

    private JLabel lblIcono;

    private JTextField txtBuscar;

    private String placeholder;

    //==========================================================
    // CONSTRUCTORES
    //==========================================================

    public BarraBusqueda() {

        this("Buscar...");

    }

    public BarraBusqueda(String placeholder) {

        this(placeholder, "icon_buscar");

    }

    public BarraBusqueda(
            String placeholder,
            String icono) {

        this.placeholder = placeholder;

        inicializar(icono);

    }

    //==========================================================
    // INICIALIZACIÓN
    //==========================================================

    private void inicializar(String icono) {

        configurarPanel();

        crearIcono(icono);

        crearCampo();

    }

    //==========================================================
    // PANEL
    //==========================================================

    private void configurarPanel() {

        setLayout(
                new BorderLayout(
                        AdministradorTema.espacioPequeño(),
                        0));

        setColorFondo(
                AdministradorTema.colorTarjeta());

        setMostrarBorde(true);

        setColorBorde(
                AdministradorTema.colorBorde());

        setBorder(
                FabricaBordes.mediano());

        EstilosComponentes.aplicarTamañoPreferido(
                this,
                UIConstants.ANCHO_BUSQUEDA,
                UIConstants.ALTURA_BUSQUEDA);

    }

    //==========================================================
    // ICONO
    //==========================================================

    private void crearIcono(String nombreIcono) {

        lblIcono = new JLabel();

        ImageIcon icono =
                FabricaIconos.crear(
                        nombreIcono,
                        AdministradorTema.iconoMediano());

        lblIcono.setIcon(icono);

        add(lblIcono, BorderLayout.WEST);

    }

    //==========================================================
    // CAMPO
    //==========================================================

    private void crearCampo() {

        txtBuscar =
                FabricaCampos.crearTexto();

        txtBuscar.setOpaque(false);

        txtBuscar.setBorder(null);

        txtBuscar.setText(placeholder);

        txtBuscar.addFocusListener(
                new java.awt.event.FocusAdapter() {

            @Override
            public void focusGained(
                    java.awt.event.FocusEvent e) {

                if (txtBuscar.getText().equals(placeholder)) {

                    txtBuscar.setText("");

                }

            }

            @Override
            public void focusLost(
                    java.awt.event.FocusEvent e) {

                if (txtBuscar.getText().isBlank()) {

                    txtBuscar.setText(placeholder);

                }

            }

        });

        add(txtBuscar, BorderLayout.CENTER);

    }

    //==========================================================
    // GETTERS
    //==========================================================

    /**
     * Devuelve el texto escrito.
     */
    public String getTexto() {

        if (txtBuscar.getText().equals(placeholder)) {

            return "";

        }

        return txtBuscar.getText().trim();

    }

    /**
     * Devuelve el JTextField.
     */
    public JTextField getCampo() {

        return txtBuscar;

    }

    //==========================================================
    // DOCUMENT LISTENER
    //==========================================================

    /**
     * Agrega un DocumentListener.
     */
    public void agregarListener(
            DocumentListener listener) {

        txtBuscar.getDocument()
                .addDocumentListener(listener);

    }

    //==========================================================
    // UTILIDADES
    //==========================================================

    /**
     * Limpia la búsqueda.
     */
    public void limpiar() {

        txtBuscar.setText("");

    }

    /**
     * Cambia el placeholder.
     */
    public void setPlaceholder(String texto) {

        placeholder = texto;

        txtBuscar.setText(texto);

    }

    /**
     * Cambia el icono.
     */
    public void setIcono(String nombreIcono) {

        lblIcono.setIcon(
                FabricaIconos.crear(
                        nombreIcono,
                        AdministradorTema.iconoMediano()));

    }

}
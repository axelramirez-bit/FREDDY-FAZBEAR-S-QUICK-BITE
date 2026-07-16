package View.Componentes;

import View.Utils.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ItemMenu extends PanelRedondeado {

    //==================================================
    // COMPONENTES
    //==================================================
    private JLabel lblIcono;

    private JLabel lblTexto;

    //==================================================
    // ATRIBUTOS
    //==================================================
    private boolean seleccionado;

    private String idVista;

    //==================================================
    // CONSTRUCTOR
    //==================================================
    public ItemMenu(
            String texto,
            ImageIcon icono,
            String idVista) {

        this.idVista = idVista;

        inicializar(texto, icono);

    }

    //==================================================
    // INICIALIZAR
    //==================================================
    private void inicializar(
            String texto,
            ImageIcon icono) {

        configurarPanel();

        crearIcono(icono);

        crearTexto(texto);

        setSeleccionado(false);

    }

    //==================================================
    // PANEL
    //==================================================
    private void configurarPanel() {

        setLayout(new GridBagLayout());

        setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR));

        EstilosComponentes.aplicarTamaño(
                this,
                AdministradorTema.anchoMenuLateral(),
                AdministradorTema.altoBotonMenu());

    }

    //==================================================
    // ICONO
    //==================================================
    private void crearIcono(ImageIcon icono) {

        lblIcono = new JLabel(icono);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;

        gbc.gridy = 0;

        gbc.insets = new Insets(
                0,
                15,
                0,
                10);

        add(lblIcono, gbc);

    }

    //==================================================
    // TEXTO
    //==================================================
    private void crearTexto(String texto) {

        lblTexto =
                FabricaEtiquetas.crearTexto(texto);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 1;

        gbc.gridy = 0;

        gbc.weightx = 1;

        gbc.anchor = GridBagConstraints.WEST;

        add(lblTexto, gbc);

    }

    //==================================================
    // SELECCIÓN
    //==================================================
    public void setSeleccionado(boolean seleccionado) {

        this.seleccionado = seleccionado;

        if (seleccionado) {

            setColorFondo(
                    AdministradorTema.colorPrincipal());

            lblTexto.setForeground(Color.WHITE);

        } else {

            setColorFondo(
                    new Color(0,0,0,0));

            lblTexto.setForeground(
                    AdministradorTema.colorTexto());

        }

        repaint();

    }

    public boolean isSeleccionado() {

        return seleccionado;

    }

    //==================================================
    // EVENTOS
    //==================================================
    public void addActionListener(
            ActionListener listener) {

        MouseAdapter adapter =
                new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                listener.actionPerformed(
                        new ActionEvent(
                                ItemMenu.this,
                                ActionEvent.ACTION_PERFORMED,
                                idVista));

            }

        };

        addMouseListener(adapter);

        lblTexto.addMouseListener(adapter);

        lblIcono.addMouseListener(adapter);

    }

    //==================================================
    // GETTERS
    //==================================================
    public String getIdVista() {

        return idVista;

    }

    public JLabel getLabelTexto() {

        return lblTexto;

    }

    public JLabel getLabelIcono() {

        return lblIcono;

    }

}
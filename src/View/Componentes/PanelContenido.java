package View.Componentes;

import java.awt.*;
import javax.swing.*;

/**
 * ===============================================================
 * PANEL CONTENIDO
 * ---------------------------------------------------------------
 * Contenedor principal que administra todas las vistas mediante
 * CardLayout.
 * ===============================================================
 */
public class PanelContenido extends JPanel {

    private CardLayout cardLayout;

    public PanelContenido() {

        inicializar();

    }

    private void inicializar() {

        cardLayout = new CardLayout();

        setLayout(cardLayout);

    }

    public void agregarVista(String id, JPanel panel){

        add(panel,id);

    }

    public void mostrar(String id){

        cardLayout.show(this,id);

    }

}
package View.Componentes;

import View.Utils.*;
import java.awt.*;
import javax.swing.*;

/**
 * ===============================================================
 * PANEL TEMPORAL
 * ---------------------------------------------------------------
 * Panel reutilizable mientras se desarrolla cada módulo.
 * Muestra únicamente un título centrado.
 * ===============================================================
 */
public class PanelTemporal extends FondoPanel {

    private JLabel lblTitulo;

    public PanelTemporal(String titulo) {

        inicializar(titulo);

    }

    private void inicializar(String titulo) {

        setLayout(new GridBagLayout());

        lblTitulo = FabricaEtiquetas.crearTitulo(titulo);

        lblTitulo.setForeground(
                AdministradorTema.colorTexto());

        add(lblTitulo);

    }

    public void setTitulo(String titulo){

        lblTitulo.setText(titulo);

    }

}
package View.Componentes;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

/**
 * ===============================================================
 * PANEL CONTENIDO
 * ---------------------------------------------------------------
 * Contenedor principal que administra todas las vistas mediante
 * CardLayout.
 *
 * CORRECCIÓN: antes mostrar(id) solo hacía cardLayout.show(...),
 * así que un panel construido una vez al abrir sesión se quedaba
 * con los datos de ese momento para siempre — cualquier cambio
 * hecho en OTRO panel (ej. atender un pedido en "Pendientes") no
 * se reflejaba en este hasta reiniciar la app entera o presionar
 * ↻ a mano. Ahora, si el componente registrado bajo ese id
 * implementa Refrescable, se le pide recargar datos justo antes
 * de mostrarlo — automático, sin que cada Dashboard tenga que
 * saber nada de esto.
 * ===============================================================
 */
public class PanelContenido extends JPanel {

    private CardLayout cardLayout;

    /** Vistas registradas por id, para poder refrescarlas al mostrarlas. */
    private final Map<String, Component> vistas = new HashMap<>();

    public PanelContenido() {

        inicializar();

    }

    private void inicializar() {

        cardLayout = new CardLayout();

        setLayout(cardLayout);

    }

    public void agregarVista(String id, java.awt.Component componente){

        add(componente, id);

        vistas.put(id, componente);

    }

    public void mostrar(String id){

        Component vista = vistas.get(id);

        if (vista instanceof Refrescable refrescable) {
            refrescable.cargarDatos();
        }

        cardLayout.show(this, id);

    }

}
package View.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Fábrica de JScrollPane reutilizables.
 * ===============================================================
 */
public final class FabricaScroll {

    private FabricaScroll() {
    }

    // ==========================================================
    // SCROLL GENÉRICO
    // ==========================================================

    /**
     * Crea un JScrollPane para cualquier componente.
     */
    public static JScrollPane crear(Component componente) {

        JScrollPane scroll = new JScrollPane(componente);

        EstilosComponentes.aplicarEstiloScroll(scroll);

        return scroll;
    }

    // ==========================================================
    // SCROLL PARA TABLAS
    // ==========================================================

    public static JScrollPane crearTabla(JTable tabla) {

        return crear(tabla);
    }

    // ==========================================================
    // SCROLL PARA ÁREAS DE TEXTO
    // ==========================================================

    public static JScrollPane crearTextArea(JTextArea area) {

        return crear(area);
    }

    // ==========================================================
    // SCROLL PARA PANELES
    // ==========================================================

    public static JScrollPane crearPanel(JPanel panel) {

        panel.setOpaque(false);

        JScrollPane scroll = crear(panel);

        scroll.getViewport().setOpaque(false);

        return scroll;
    }

    // ==========================================================
    // SCROLL PARA LISTAS
    // ==========================================================

    public static <T> JScrollPane crearLista(JList<T> lista) {

        return crear(lista);
    }

    // ==========================================================
    // SCROLL PARA ÁRBOLES
    // ==========================================================

    public static JScrollPane crearArbol(JTree arbol) {

        return crear(arbol);
    }

}
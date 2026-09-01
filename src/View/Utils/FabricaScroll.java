
package View.Utils;

import javax.swing.*;
import java.awt.*;


import javax.swing.JPanel;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Fábrica de JScrollPane reutilizables.
 *
 * Permite envolver paneles, tablas, listas, árboles y áreas de
 * texto dentro de JScrollPane manteniendo el estilo general
 * del proyecto.
 * ===============================================================
 */
public final class FabricaScroll {

    private FabricaScroll() {
        // Evita instanciar esta clase.
    }

    // ==========================================================
    // SCROLL GENÉRICO
    // ==========================================================

    /**
     * Crea un JScrollPane para cualquier componente.
     *
     * @param componente componente que se colocará dentro del scroll
     * @return JScrollPane configurado
     */
    public static JScrollPane crear(Component componente) {

        if (componente == null) {
            throw new IllegalArgumentException(
                    "El componente no puede ser null."
            );
        }

        JScrollPane scroll = new JScrollPane(componente);

        // Estilo general del proyecto.
        EstilosComponentes.aplicarEstiloScroll(scroll);

        // No permitir scroll horizontal.
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        // Mostrar vertical solamente cuando sea necesario.
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        return scroll;
    }

    // ==========================================================
    // SCROLL PARA TABLAS
    // ==========================================================

    /**
     * Crea un JScrollPane para una JTable.
     *
     * @param tabla tabla que se desea envolver
     * @return JScrollPane con la tabla
     */
    public static JScrollPane crearTabla(JTable tabla) {

        if (tabla == null) {
            throw new IllegalArgumentException(
                    "La tabla no puede ser null."
            );
        }

        JScrollPane scroll = crear(tabla);

        // La tabla puede ocupar todo el ancho disponible.
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        return scroll;
    }

    // ==========================================================
    // SCROLL PARA ÁREAS DE TEXTO
    // ==========================================================

    /**
     * Crea un JScrollPane para un JTextArea.
     *
     * @param area área de texto
     * @return JScrollPane con el área de texto
     */
    public static JScrollPane crearTextArea(JTextArea area) {

        if (area == null) {
            throw new IllegalArgumentException(
                    "El área de texto no puede ser null."
            );
        }

        return crear(area);
    }

    // ==========================================================
    // SCROLL PARA PANELES
    // ==========================================================

    /**
     * Crea un JScrollPane para cualquiera de los paneles
     * administrativos.
     *
     * Paneles utilizados actualmente:
     *
     * - PanelDashboard
     * - PanelUsuarios
     * - PanelTrabajadores
     * - PanelProductos
     * - PanelCategorias
     * - PanelPromociones
     * - PanelPedidos
     * - PanelPagos
     * - PanelVentas
     * - PanelReportes
     *
     * @param panel JPanel que se desea envolver
     * @return JScrollPane con el panel
     */
    public static JScrollPane crearPanel(JPanel panel) {

        if (panel == null) {
            throw new IllegalArgumentException(
                    "El panel no puede ser null."
            );
        }

        // Permite que se vea el fondo definido por el panel padre.
        panel.setOpaque(false);

        JScrollPane scroll = crear(panel);

        // Fondo transparente para integrarse con el diseño.
        scroll.getViewport().setOpaque(false);

        // Evita que aparezca scroll horizontal en los paneles.
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        // Scroll vertical únicamente cuando el contenido
        // sobrepasa la altura disponible.
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        return scroll;
    }

    // ==========================================================
    // SCROLL PARA LISTAS
    // ==========================================================

    /**
     * Crea un JScrollPane para un JList.
     *
     * @param lista lista que se desea envolver
     * @param <T> tipo de datos de la lista
     * @return JScrollPane con la lista
     */
    public static <T> JScrollPane crearLista(JList<T> lista) {

        if (lista == null) {
            throw new IllegalArgumentException(
                    "La lista no puede ser null."
            );
        }

        return crear(lista);
    }

    // ==========================================================
    // SCROLL PARA ÁRBOLES
    // ==========================================================

    /**
     * Crea un JScrollPane para un JTree.
     *
     * @param arbol árbol que se desea envolver
     * @return JScrollPane con el árbol
     */
    public static JScrollPane crearArbol(JTree arbol) {

        if (arbol == null) {
            throw new IllegalArgumentException(
                    "El árbol no puede ser null."
            );
        }

        return crear(arbol);
    }

    // ==========================================================
    // SCROLL PARA LOS PANELES DEL ADMINISTRADOR
    // ==========================================================

    /**
     * Métodos específicos para los paneles administrativos.
     *
     * Estos métodos son opcionales, pero permiten escribir código
     * más claro en DashboardAdministrador.
     */

    public static JScrollPane crearDashboard(JPanel panel) {
        return crearPanel(panel);
    }

    public static JScrollPane crearUsuarios(JPanel panel) {
        return crearPanel(panel);
    }

    public static JScrollPane crearTrabajadores(JPanel panel) {
        return crearPanel(panel);
    }

    public static JScrollPane crearProductos(JPanel panel) {
        return crearPanel(panel);
    }

    public static JScrollPane crearCategorias(JPanel panel) {
        return crearPanel(panel);
    }

    public static JScrollPane crearPromociones(JPanel panel) {
        return crearPanel(panel);
    }

    public static JScrollPane crearPedidos(JPanel panel) {
        return crearPanel(panel);
    }

    public static JScrollPane crearPagos(JPanel panel) {
        return crearPanel(panel);
    }

    public static JScrollPane crearVentas(JPanel panel) {
        return crearPanel(panel);
    }

    public static JScrollPane crearReportes(JPanel panel) {
        return crearPanel(panel);
    }
}

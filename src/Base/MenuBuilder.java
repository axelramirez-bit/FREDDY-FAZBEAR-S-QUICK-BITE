package Base;

import View.Componentes.ItemMenu;
import View.Utils.AdministradorTema;
import View.Utils.UtilImagenes;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.util.Map;
import java.util.function.Consumer;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Construye el panel con los ItemMenu de un rol.
 *
 * Responsabilidad única: ensamblar componentes ItemMenu a partir
 * de OpcionMenu[]. No sabe qué es un Rol, no sabe qué es
 * ControlNavegacion, no sabe qué es BarraLateral. Por eso puede
 * reutilizarse en cualquier otro menú del proyecto si algún día
 * lo necesitas (por ejemplo un menú de opciones dentro de un
 * diálogo).
 * ===============================================================
 */
public final class MenuBuilder {

    private MenuBuilder() {
    }

    /**
     * Crea el panel de menú.
     *
     * @param opciones     Opciones a mostrar, en el orden que se
     *                     deben ver.
     * @param alSeleccionar Se invoca con el idVista de la opción
     *                      presionada. No navega por sí mismo —
     *                      solo notifica.
     * @param cache         Mapa donde se registra cada ItemMenu
     *                      creado, indexado por idVista, para que
     *                      BarraLateral pueda encontrarlo luego
     *                      sin recorrer una lista (ver punto de
     *                      "cache de ItemMenu" del review).
     * @return Panel con todos los ItemMenu ya armados.
     */
    public static JPanel crear(
            OpcionMenu[] opciones,
            Consumer<String> alSeleccionar,
            Map<String, ItemMenu> cache) {

        JPanel panelMenu = new JPanel();

        panelMenu.setOpaque(false);

        panelMenu.setLayout(
                new BoxLayout(panelMenu, BoxLayout.Y_AXIS)
        );

        for (OpcionMenu opcion : opciones) {

            ItemMenu item = crearItem(opcion, alSeleccionar);

            cache.put(opcion.getIdVista(), item);

            panelMenu.add(item);

            panelMenu.add(
                    crearEspacio()
            );
        }

        return panelMenu;
    }

    // ==========================================================
    // ITEM INDIVIDUAL
    // ==========================================================
    private static ItemMenu crearItem(
            OpcionMenu opcion,
            Consumer<String> alSeleccionar) {

        ItemMenu item = new ItemMenu(
        opcion.getTexto(),
        opcion.getIdVista(),
        opcion.getNombreIcono()
);

item.setItemMenuListener(
        menu -> alSeleccionar.accept(menu.getPanelDestino())
);

        return item;
    }

    // ==========================================================
    // ESPACIADO ENTRE ITEMS (nunca un número suelto)
    // ==========================================================
    private static java.awt.Component crearEspacio() {

        return javax.swing.Box.createRigidArea(
                new Dimension(0, AdministradorTema.espacioPequeño())
        );
    }

}
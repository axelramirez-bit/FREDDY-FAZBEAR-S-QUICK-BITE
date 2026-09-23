// Paquete Base
package Base;

// Importa ItemMenu (botón del menú)
import View.Componentes.ItemMenu;
// Importa el administrador de tema
import View.Utils.AdministradorTema;

// Importa BoxLayout
import javax.swing.BoxLayout;
// Importa JPanel
import javax.swing.JPanel;
// Importa Dimension
import java.awt.Dimension;
// Importa Map
import java.util.Map;
// Importa Consumer
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
// Clase final que arma el panel de menú
public final class MenuBuilder {

    // Constructor privado: no se instancia
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
    // Crea el panel de menú de un rol
    public static JPanel crear(
            // Opciones a mostrar
            OpcionMenu[] opciones,
            // Acción a ejecutar al seleccionar una opción
            Consumer<String> alSeleccionar,
            // Caché de ItemMenu indexada por id de vista
            Map<String, ItemMenu> cache) {

        // Crea el panel contenedor
        JPanel panelMenu = new JPanel();

        // Fondo transparente
        panelMenu.setOpaque(false);

        // Asigna layout vertical
        panelMenu.setLayout(
                // Apila los ítems de arriba hacia abajo
                new BoxLayout(panelMenu, BoxLayout.Y_AXIS)
        );

        // Recorre cada opción
        for (OpcionMenu opcion : opciones) {

            // Crea el ítem visual de la opción
            ItemMenu item = crearItem(opcion, alSeleccionar);

            // Guarda el ítem en la caché
            cache.put(opcion.getIdVista(), item);

            // Agrega el ítem al panel
            panelMenu.add(item);

            // Agrega un espacio entre ítems
            panelMenu.add(
                    crearEspacio()
            );
        }

        // Devuelve el panel armado
        return panelMenu;
    }

    // ==========================================================
    // ITEM INDIVIDUAL
    // ==========================================================
    // Crea un ítem individual del menú
    private static ItemMenu crearItem(
            // Opción de menú de origen
            OpcionMenu opcion,
            // Acción al seleccionar
            Consumer<String> alSeleccionar) {

        // Crea el ItemMenu
        ItemMenu item = new ItemMenu(
                // Texto visible
                opcion.getTexto(),
                // Id de la vista destino
                opcion.getIdVista(),
                // Nombre del ícono
                opcion.getNombreIcono()
        );

        // Registra el listener del clic
        item.setItemMenuListener(
                // Al hacer clic, notifica el panel destino
                it -> alSeleccionar.accept(it.getPanelDestino())
        );

        // Devuelve el ítem
        return item;
    }

    // ==========================================================
    // ESPACIADO ENTRE ITEMS (nunca un número suelto)
    // ==========================================================
    // Crea el espacio vertical entre ítems
    private static java.awt.Component crearEspacio() {

        // Crea un área rígida invisible
        return javax.swing.Box.createRigidArea(
                // Alto igual al espacio pequeño del tema (ancho 0)
                new Dimension(0, AdministradorTema.espacioPequeño())
        );
    }

}
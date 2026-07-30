package Base;

import View.Componentes.ItemMenu;
import View.Utils.AdministradorTema;
import View.Utils.UtilImagenes;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Menú lateral (sidebar) compartido por los tres dashboards.
 *
 * Responsabilidad única de esta clase: ensamblar tres bloques
 * (logo+eslogan, menú del rol, configuración+cerrar sesión) y
 * gestionar qué ItemMenu está seleccionado visualmente. No sabe
 * construir ItemMenu individuales (eso es de MenuBuilder), no
 * sabe qué opciones tiene cada rol (eso es de MenuPorRol) y no
 * conoce ControlNavegacion (eso es de quien la escuche a través
 * de BarraLateralListener).
 *
 * Uso típico desde DashboardBase:
 *
 *     BarraLateral barra = new BarraLateral(
 *             Rol.CLIENTE,
 *             new BarraLateralListener() {
 *                 public void onOpcionSeleccionada(String idVista) {
 *                     ControlNavegacion.abrir(idVista);
 *                 }
 *                 public void onConfiguracion() {
 *                     ControlNavegacion.abrir("CONFIGURACION_CLIENTE");
 *                 }
 *                 public void onCerrarSesion() {
 *                     Sesion.getInstancia().cerrarSesion();
 *                     // volver a la pantalla de Bienvenida
 *                 }
 *             }
 *     );
 *     barra.seleccionarPorDefecto("INICIO_CLIENTE");
 * ===============================================================
 */
public class BarraLateral extends JPanel {

    // ==========================================================
    // CONFIGURACIÓN
    // ==========================================================
    private final Rol rol;

    private final BarraLateralListener listener;

    // ==========================================================
    // ESTADO / CACHE
    // ==========================================================
    /**
     * Un ItemMenu por idVista, llenado por MenuBuilder. Permite
     * encontrar el ítem a marcar como seleccionado en O(1) en vez
     * de recorrer una lista.
     */
    private final Map<String, ItemMenu> itemsPorVista
            = new LinkedHashMap<>();

    private ItemMenu itemSeleccionadoActual;

    // ==========================================================
    // CONSTRUCTOR
    // ==========================================================

    /**
     * @param rol      Rol para el cual se construye el menú
     *                 (determina qué opciones aparecen).
     * @param listener Quien reacciona a las selecciones. Nunca
     *                 puede ser null: sin listener, la barra no
     *                 tendría forma de avisar que se presionó algo.
     */
    public BarraLateral(Rol rol, BarraLateralListener listener) {

        if (listener == null) {
            throw new IllegalArgumentException(
                    "BarraLateral necesita un BarraLateralListener."
            );
        }

        this.rol = rol;

        this.listener = listener;

        configurarPanel();

        construir();
    }

    // ==========================================================
    // CONFIGURACIÓN DEL PANEL
    // ==========================================================
    private void configurarPanel() {

        setLayout(new BorderLayout());

        setPreferredSize(
                new Dimension(AdministradorTema.anchoMenuLateral(), 0)
        );

        setBackground(AdministradorTema.colorPrincipal());
    }

    // ==========================================================
    // ENSAMBLAJE (los únicos tres bloques que existen)
    // ==========================================================
    private void construir() {

        add(crearPanelSuperior(), BorderLayout.NORTH);

        add(crearPanelMenu(), BorderLayout.CENTER);

        add(crearPanelInferior(), BorderLayout.SOUTH);
    }

    // ==========================================================
    // BLOQUE 1 — LOGO + ESLOGAN
    // ==========================================================
    private JPanel crearPanelSuperior() {

        JPanel panel = new JPanel();

        panel.setOpaque(false);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        AdministradorTema.espacioGrande(),
                        AdministradorTema.espacioMediano(),
                        AdministradorTema.espacioPequeño(),
                        AdministradorTema.espacioMediano()
                )
        );

        JLabel lblLogo = new JLabel(UtilImagenes.logotipo());

        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblEslogan = new JLabel(
                "<html><center>Diversión y sabor "
                + "en cada bocado</center></html>"
        );

        lblEslogan.setForeground(AdministradorTema.colorTextoBlanco());

        lblEslogan.setFont(AdministradorTema.fuentePequeña());

        lblEslogan.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblLogo);

        panel.add(Box.createRigidArea(
                new Dimension(0, AdministradorTema.espacioPequeño())
        ));

        panel.add(lblEslogan);

        panel.add(Box.createRigidArea(
                new Dimension(0, AdministradorTema.espacioMenuSuperior())
        ));

        return panel;
    }

    // ==========================================================
    // BLOQUE 2 — MENÚ DEL ROL (con scroll, por si crecen las
    // opciones de Administrador y no caben en pantallas pequeñas)
    // ==========================================================
    private JScrollPane crearPanelMenu() {

        JPanel panelMenu = MenuBuilder.crear(
                MenuPorRol.obtener(rol),
                this::seleccionarOpcion,
                itemsPorVista
        );

        JScrollPane scroll = new JScrollPane(panelMenu);

        scroll.setBorder(null);

        scroll.setOpaque(false);

        scroll.getViewport().setOpaque(false);

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.getVerticalScrollBar().setUnitIncrement(
                AdministradorTema.velocidadScroll()
        );

        return scroll;
    }

    // ==========================================================
    // BLOQUE 3 — CONFIGURACIÓN + CERRAR SESIÓN
    //
    // Se manejan aparte del menú normal porque su comportamiento
    // es distinto: no navegan dentro del CardLayout como las
    // demás opciones (ver IdVistaEspecial).
    // ==========================================================
    private JPanel crearPanelInferior() {

        JPanel panel = new JPanel();

        panel.setOpaque(false);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        AdministradorTema.espacioPequeño(),
                        AdministradorTema.espacioMediano(),
                        AdministradorTema.espacioGrande(),
                        AdministradorTema.espacioMediano()
                )
        );

        ItemMenu itemConfiguracion = new ItemMenu(
        "Configuración",
        IdVistaEspecial.CONFIGURACION,
        "icon_configuracion"
);

itemConfiguracion.setItemMenuListener(
        item -> listener.onConfiguracion()
);

       ItemMenu itemCerrarSesion = new ItemMenu(
        "Cerrar sesión",
        IdVistaEspecial.CERRAR_SESION,
        "icon_cerrar_sesion"
);

itemCerrarSesion.setItemMenuListener(
        item -> listener.onCerrarSesion()
);

        panel.add(itemConfiguracion);

        panel.add(Box.createRigidArea(
                new Dimension(0, AdministradorTema.espacioPequeño())
        ));

        panel.add(itemCerrarSesion);

        return panel;
    }

    // ==========================================================
    // NAVEGACIÓN
    // ==========================================================
    /**
     * Se llama cuando se presiona cualquier ItemMenu normal
     * (no Configuración ni Cerrar sesión). Actualiza el color de
     * selección y avisa al listener — nunca llama a
     * ControlNavegacion directamente.
     */
    private void seleccionarOpcion(String idVista) {

        marcarComoSeleccionado(idVista);

        listener.onOpcionSeleccionada(idVista);
    }

    /**
     * Quita el resaltado del ítem anterior y resalta el nuevo.
     * Público porque DashboardBase puede necesitarlo para marcar
     * la vista inicial al abrir el dashboard (ver
     * seleccionarPorDefecto).
     */
    private void marcarComoSeleccionado(String idVista) {

        if (itemSeleccionadoActual != null) {
            itemSeleccionadoActual.setSeleccionado(false);
        }

        ItemMenu nuevoSeleccionado = itemsPorVista.get(idVista);

        if (nuevoSeleccionado == null) {
            // Configuración/Cerrar sesión no están en el cache:
            // es normal, no navegan dentro del CardLayout.
            itemSeleccionadoActual = null;
            return;
        }

        nuevoSeleccionado.setSeleccionado(true);

        itemSeleccionadoActual = nuevoSeleccionado;
    }

    // ==========================================================
    // API PÚBLICA
    // ==========================================================
    /**
     * Marca visualmente una opción como seleccionada sin disparar
     * el listener. Úsalo justo después de crear la BarraLateral
     * para que la vista inicial del dashboard aparezca resaltada
     * en el menú desde el primer instante.
     *
     * @param idVista idVista de la opción que abre el dashboard
     *                por defecto (ej. "INICIO_CLIENTE").
     */
    public void seleccionarPorDefecto(String idVista) {
        marcarComoSeleccionado(idVista);
    }

}
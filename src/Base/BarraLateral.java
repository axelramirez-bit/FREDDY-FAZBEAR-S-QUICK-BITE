// Paquete Base
package Base;

// Importa FranjaHoraria
import Utils.FranjaHoraria;
// Importa ItemMenu (botón del menú)
import View.Componentes.ItemMenu;
// Importa el administrador de tema
import View.Utils.AdministradorTema;
// Importa UtilImagenes (logo)
import View.Utils.UtilImagenes;

// Importa BorderFactory
import javax.swing.BorderFactory;
// Importa BoxLayout
import javax.swing.BoxLayout;
// Importa Box (espacios)
import javax.swing.Box;
// Importa JLabel
import javax.swing.JLabel;
// Importa JPanel
import javax.swing.JPanel;
// Importa JScrollPane
import javax.swing.JScrollPane;
// Importa BorderLayout
import java.awt.BorderLayout;
// Importa Component
import java.awt.Component;
// Importa Dimension
import java.awt.Dimension;
// Importa LinkedHashMap (mapa que conserva el orden)
import java.util.LinkedHashMap;
// Importa Map
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
// Panel del menú lateral compartido por los tres dashboards
public class BarraLateral extends JPanel {

    // ==========================================================
    // CONFIGURACIÓN
    // ==========================================================
    // Rol que determina las opciones del menú
    private final Rol rol;

    // Quien recibe los avisos de la barra
    private final BarraLateralListener listener;

    // ==========================================================
    // ESTADO / CACHE
    // ==========================================================
    /**
     * Un ItemMenu por idVista, llenado por MenuBuilder. Permite
     * encontrar el ítem a marcar como seleccionado en O(1) en vez
     * de recorrer una lista.
     */
    // Ítems del menú indexados por id de vista
    private final Map<String, ItemMenu> itemsPorVista
            // Mapa que conserva el orden de inserción
            = new LinkedHashMap<>();

    // Ítem seleccionado actualmente
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
    // Constructor: recibe el rol y el listener
    public BarraLateral(Rol rol, BarraLateralListener listener) {

        // Si no hay listener
        if (listener == null) {
            // Lanza error
            throw new IllegalArgumentException(
                    // Mensaje del error
                    "BarraLateral necesita un BarraLateralListener."
            );
        }

        // Guarda el rol
        this.rol = rol;

        // Guarda el listener
        this.listener = listener;

        // Configura el panel
        configurarPanel();

        // Construye los tres bloques
        construir();
    }

    // ==========================================================
    // CONFIGURACIÓN DEL PANEL
    // ==========================================================
    // Configura el panel
    private void configurarPanel() {

        // Usa BorderLayout
        setLayout(new BorderLayout());

        // Define el tamaño preferido
        setPreferredSize(
                // Ancho del menú lateral, alto libre
                new Dimension(AdministradorTema.anchoMenuLateral(), 0)
        );

        // Fondo con el color del tema
        setBackground(AdministradorTema.colorFondo());
    }

    // ==========================================================
    // ENSAMBLAJE (los únicos tres bloques que existen)
    // ==========================================================
    // Ensambla los tres bloques
    private void construir() {

        // Bloque superior: logo y eslogan
        add(crearPanelSuperior(), BorderLayout.NORTH);

        // Bloque central: menú del rol
        add(crearPanelMenu(), BorderLayout.CENTER);

        // Bloque inferior: configuración y cerrar sesión
        add(crearPanelInferior(), BorderLayout.SOUTH);
    }

    // ==========================================================
    // BLOQUE 1 — LOGO + ESLOGAN
    // ==========================================================
    // Crea el bloque superior (logo + eslogan)
    private JPanel crearPanelSuperior() {

        // Crea el panel
        JPanel panel = new JPanel();

        // Fondo transparente
        panel.setOpaque(false);

        // Layout vertical
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Define los márgenes
        panel.setBorder(
                // Crea un borde vacío
                BorderFactory.createEmptyBorder(
                        // Margen superior
                        AdministradorTema.espacioGrande(),
                        // Margen izquierdo
                        AdministradorTema.espacioMediano(),
                        // Margen inferior
                        AdministradorTema.espacioPequeño(),
                        // Margen derecho
                        AdministradorTema.espacioMediano()
                )
        );

        // Etiqueta con el logotipo
        JLabel lblLogo = new JLabel(UtilImagenes.logotipo());

        // Centra el logo
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Etiqueta con el eslogan
        JLabel lblEslogan = new JLabel(
                // Texto centrado en HTML
                "<html><center>DIVERSIÓN Y SABOR "
                // Continuación del eslogan
                + "EN CADA BOCADO</center></html>"
        );

        // Color de texto del tema
        lblEslogan.setForeground(AdministradorTema.colorTexto());

        // Fuente del eslogan
        lblEslogan.setFont(AdministradorTema.fuenteLogoEslogan());

        // Centra el eslogan
        lblEslogan.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Agrega el logo
        panel.add(lblLogo);

        // Agrega un espacio vertical
        panel.add(Box.createRigidArea(
                // Alto igual al espacio pequeño
                new Dimension(0, AdministradorTema.espacioPequeño())
        ));

        // Agrega el eslogan
        panel.add(lblEslogan);

        // Agrega un espacio vertical
        panel.add(Box.createRigidArea(
                // Alto igual al espacio bajo el eslogan
                new Dimension(0, AdministradorTema.espacioMenuSuperior())
        ));

        // Devuelve el panel
        return panel;
    }

    // ==========================================================
    // BLOQUE 2 — MENÚ DEL ROL (con scroll, por si crecen las
    // opciones de Administrador y no caben en pantallas pequeñas)
    // ==========================================================
    // Crea el bloque central (menú con scroll)
    private JScrollPane crearPanelMenu() {

        // Arma el panel de menú
        JPanel panelMenu = MenuBuilder.crear(
                // Con las opciones del rol
                MenuPorRol.obtener(rol),
                // Al seleccionar llama a seleccionarOpcion
                this::seleccionarOpcion,
                // Guarda los ítems en el mapa
                itemsPorVista
        );

        // Envuelve el menú en un scroll
        JScrollPane scroll = new JScrollPane(panelMenu);

        // Sin borde
        scroll.setBorder(null);

        // Fondo transparente
        scroll.setOpaque(false);

        // Viewport transparente
        scroll.getViewport().setOpaque(false);

        // Política del scroll horizontal
        scroll.setHorizontalScrollBarPolicy(
                // Nunca se muestra
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        // Velocidad del scroll vertical
        scroll.getVerticalScrollBar().setUnitIncrement(
                // Tomada del tema
                AdministradorTema.velocidadScroll()
        );

        // Devuelve el scroll
        return scroll;
    }

    // ==========================================================
    // BLOQUE 3 — CONFIGURACIÓN + CERRAR SESIÓN
    //
    // Se manejan aparte del menú normal porque su comportamiento
    // es distinto: no navegan dentro del CardLayout como las
    // demás opciones (ver IdVistaEspecial).
    // ==========================================================
    // Crea el bloque inferior (configuración y cerrar sesión)
    private JPanel crearPanelInferior() {

        // Crea el panel
        JPanel panel = new JPanel();

        // Fondo transparente
        panel.setOpaque(false);

        // Layout vertical
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Define los márgenes
        panel.setBorder(
                // Crea un borde vacío
                BorderFactory.createEmptyBorder(
                        // Margen superior
                        AdministradorTema.espacioPequeño(),
                        // Margen izquierdo
                        AdministradorTema.espacioMediano(),
                        // Margen inferior
                        AdministradorTema.espacioGrande(),
                        // Margen derecho
                        AdministradorTema.espacioMediano()
                )
        );

        // Crea el ítem de la opción especial
        ItemMenu itemConfiguracion = new ItemMenu(
                // Texto según el rol
                textoOpcionEspecial(),
                // Id de la opción Configuración
                IdVistaEspecial.CONFIGURACION,
                // Ícono según el rol
                iconoOpcionEspecial()
        );

        // Registra el listener del clic
        itemConfiguracion.setItemMenuListener(
                // Al hacer clic avisa onConfiguracion
                it -> listener.onConfiguracion()
        );

        // Crea el ítem Cerrar sesión
        ItemMenu itemCerrarSesion = new ItemMenu(
                // Texto
                "Cerrar sesión",
                // Id de la opción Cerrar sesión
                IdVistaEspecial.CERRAR_SESION,
                // Nombre del ícono
                "icon_cerrar_sesion"
        );

        // Registra el listener del clic
        itemCerrarSesion.setItemMenuListener(
                // Al hacer clic avisa onCerrarSesion
                it -> listener.onCerrarSesion()
        );

        // Agrega el ítem de configuración
        panel.add(itemConfiguracion);

        // Agrega un espacio vertical
        panel.add(Box.createRigidArea(
                // Alto igual al espacio pequeño
                new Dimension(0, AdministradorTema.espacioPequeño())
        ));

        // Agrega el ítem de cerrar sesión
        panel.add(itemCerrarSesion);

        // Devuelve el panel
        return panel;
    }

    // ==========================================================
    // TEXTO/ÍCONO DEL BOTÓN ESPECIAL (antes siempre "Configuración")
    //
    // Con el modelo de 2 actores, el mismo Cajero opera tanto la
    // ventana de catálogo+carrito (rol CLIENTE) como la de gestión
    // de pedidos (rol TRABAJADOR), y este botón —que antes abría
    // una "Configuración" sin panel propio— ahora es el atajo para
    // saltar de una ventana a la otra (ver
    // DashboardCliente/DashboardTrabajador.onConfiguracion()).
    // Administrador no tiene ese salto, así que conserva el texto
    // y el ícono originales sin ningún cambio.
    // ==========================================================
    // Devuelve el texto del botón especial según el rol
    private String textoOpcionEspecial() {

        // Evalúa el rol
        switch (rol) {

            // Si es CLIENTE
            case CLIENTE:
                // devuelve "Gestionar pedidos"
                return "Gestionar pedidos";

            // Si es TRABAJADOR
            case TRABAJADOR:
                // devuelve "Punto de venta"
                return "Punto de venta";

            // Cualquier otro rol
            default:
                // devuelve "Configuración"
                return "Configuración";
        }
    }

    // Devuelve el ícono del botón especial según el rol
    private String iconoOpcionEspecial() {

        // Evalúa el rol
        switch (rol) {

            // Si es CLIENTE
            case CLIENTE:
                // devuelve icon_pedidos
                return "icon_pedidos";

            // Si es TRABAJADOR
            case TRABAJADOR:
                // devuelve icon_carrito
                return "icon_carrito";

            // Cualquier otro rol
            default:
                // devuelve icon_configuracion
                return "icon_configuracion";
        }
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
    // Se ejecuta al presionar una opción normal
    private void seleccionarOpcion(String idVista) {

        // Actualiza texto e ícono si la opción es dinámica
        actualizarOpcionSiEsDinamica(idVista);

        // Marca la opción como seleccionada
        marcarComoSeleccionado(idVista);

        // Avisa al listener
        listener.onOpcionSeleccionada(idVista);
    }

    /**
     * La opción "DESAYUNOS_CENAS" (ver OpcionesCliente) no tiene
     * texto ni ícono fijos: se recalculan contra la hora local cada
     * vez que el Cliente le hace clic, para que alguien que entra a
     * las 11:55 y vuelve a esa opción a las 12:05 vea el cambio de
     * "Desayunos" a "Cenas" (con su ícono correspondiente) sin
     * tener que cerrar sesión y volver a entrar.
     */
    // Actualiza la opción Desayunos/Cenas según la hora
    private void actualizarOpcionSiEsDinamica(String idVista) {

        // Si no es la opción dinámica
        if (!"DESAYUNOS_CENAS".equals(idVista)) {
            // no hace nada
            return;
        }

        // Busca el ítem en el mapa
        ItemMenu item = itemsPorVista.get(idVista);

        // Si no existe
        if (item == null) {
            // no hace nada
            return;
        }

        // Actualiza el texto según la franja
        item.setTexto(FranjaHoraria.texto());

        // Actualiza el ícono según la franja
        item.setIcono(FranjaHoraria.nombreIcono());
    }

    /**
     * Quita el resaltado del ítem anterior y resalta el nuevo.
     * Público porque DashboardBase puede necesitarlo para marcar
     * la vista inicial al abrir el dashboard (ver
     * seleccionarPorDefecto).
     */
    // Resalta el nuevo ítem y quita el resaltado del anterior
    private void marcarComoSeleccionado(String idVista) {

        // Si había un ítem seleccionado
        if (itemSeleccionadoActual != null) {
            // le quita la selección
            itemSeleccionadoActual.setSeleccionado(false);
        }

        // Busca el nuevo ítem en el mapa
        ItemMenu nuevoSeleccionado = itemsPorVista.get(idVista);

        // Si no está en el mapa
        if (nuevoSeleccionado == null) {
            // Configuración/Cerrar sesión no están en el cache:
            // es normal, no navegan dentro del CardLayout.
            // no hay ítem seleccionado
            itemSeleccionadoActual = null;
            // termina el método
            return;
        }

        // Marca el nuevo ítem como seleccionado
        nuevoSeleccionado.setSeleccionado(true);

        // Lo guarda como el actual
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
    // Selecciona una opción sin disparar el listener
    public void seleccionarPorDefecto(String idVista) {
        // Marca la opción como seleccionada
        marcarComoSeleccionado(idVista);
    }

}
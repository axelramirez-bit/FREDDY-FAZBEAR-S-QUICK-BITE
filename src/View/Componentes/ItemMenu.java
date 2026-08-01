package View.Componentes;

import View.Utils.AdministradorTema;
import View.Utils.CacheImagenes;

import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * --------------------------------------------------------------- Componente
 * reutilizable para representar una opción del menú lateral de la aplicación.
 *
 * Este componente será utilizado por:
 *
 * • DashboardCliente • DashboardTrabajador • DashboardAdministrador
 *
 * Responsabilidades:
 *
 * • Mostrar un icono. • Mostrar el nombre de la opción. • Mantener el mismo
 * tamaño para todos los elementos. • Mostrar los estados: - Normal - Hover -
 * Seleccionado
 *
 * La navegación será controlada por BarraLateral mediante ControlNavegacion.
 * ===============================================================
 */
public class ItemMenu extends JPanel {

    // ==========================================================
    // INFORMACIÓN
    // ==========================================================
    /**
     * Texto que se muestra en el menú.
     */
    private final String titulo;

    /**
     * Nombre del panel registrado en ControlNavegacion.
     */
    private final String panelDestino;

    /**
     * Nombre del icono normal.
     */
    private final String iconoNormal;

    // ==========================================================
    // ESTADO
    // ==========================================================
    /**
     * Indica si el elemento está seleccionado.
     */
    private boolean seleccionado;
    /**
     * Indica si el Item puede utilizarse.
     */
    private boolean habilitado = true;

    /**
     * Radio utilizado para dibujar las esquinas.
     */
    private int radioBorde
            = AdministradorTema.radioMenu();
// ==========================================================
// CONSTANTES
// ==========================================================

    /**
     * Cursor utilizado cuando el Item está habilitado.
     */
    private static final Cursor CURSOR_MANO
            = new Cursor(Cursor.HAND_CURSOR);

    /**
     * Cursor utilizado cuando el Item está deshabilitado.
     */
    private static final Cursor CURSOR_NORMAL
            = new Cursor(Cursor.DEFAULT_CURSOR);
    // ==========================================================
    // COMPONENTES
    // ==========================================================
    /**
     * Icono del menú.
     */
    private JLabel lblIcono;

    /**
     * Texto del menú.
     */
    private JLabel lblTexto;


    /**
     * Separación izquierda.
     */
    private static final int PADDING_HORIZONTAL = 16;

    /**
     * Separación entre icono y texto.
     */
    private static final int ESPACIO_ICONO = 12;

    // ==========================================================
    // CONSTRUCTOR
    // ==========================================================
    /**
     * Crea un nuevo Item del menú lateral.
     *
     * @param titulo Texto del menú.
     * @param panelDestino Nombre del panel registrado.
     * @param iconoNormal Icono normal.
     * @param iconoSeleccionado Icono cuando está seleccionado.
     */
    // ==========================================================
// LISTENER
// ==========================================================

    /**
     * Listener del ItemMenu.
     */
    public interface ItemMenuListener {

        /**
         * Se ejecuta cuando el usuario selecciona el Item.
         *
         * @param item Item seleccionado.
         */
        void alSeleccionar(ItemMenu item);

    }

    /**
     * Listener registrado.
     */
    private ItemMenuListener listener;

    /**
     * Registra el listener del Item.
     *
     * @param listener Listener.
     */
    public void setItemMenuListener(
            ItemMenuListener listener) {

        this.listener = listener;

    }

    public ItemMenu(
            String titulo,
            String panelDestino,
            String iconoNormal) {

        this.titulo = titulo;
        this.panelDestino = panelDestino;
        this.iconoNormal = iconoNormal;

        inicializarComponente();

        actualizarApariencia();

        registrarEventos();

    }

    // ==========================================================
    // INICIALIZAR
    // ==========================================================
    /**
     * Construye el componente.
     */
    private void inicializarComponente() {
        configurarPanel();

        crearComponentes();

        agregarComponentes();
    }

    // ==========================================================
    // CONFIGURAR PANEL
    // ==========================================================
    /**
     * Configuración general del panel.
     */
    private void configurarPanel() {

        setLayout(new BorderLayout());
        setOpaque(false);

        setBackground(
                AdministradorTema.colorFondo());

        setCursor(CURSOR_MANO);

        setAlignmentX(Component.LEFT_ALIGNMENT);

        setPreferredSize(
                new Dimension(
                        AdministradorTema.anchoMenuLateral()
                        - AdministradorTema.margenMenu(),
                        AdministradorTema.altoBotonMenu()));

        setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        AdministradorTema.altoBotonMenu()));

    }

    // ==========================================================
    // CREAR COMPONENTES
    // ==========================================================
    /**
     * Crea los componentes internos.
     */
    private void crearComponentes() {

        lblIcono = new JLabel(
                CacheImagenes.obtenerIcono(
                        iconoNormal,
                        AdministradorTema.iconoMenu()));

        lblIcono.setHorizontalAlignment(
                SwingConstants.CENTER);

        lblTexto = new JLabel(titulo);

        lblTexto.setFont(
                AdministradorTema.fuenteNormalNegrita());

        lblTexto.setForeground(
                AdministradorTema.colorTexto());
        setToolTipText(titulo);
        getAccessibleContext()
                .setAccessibleName(titulo);

        getAccessibleContext()
                .setAccessibleDescription(
                        "Opción del menú lateral");
    }

    /**
     * Solicita el foco del Item.
     */
    public void solicitarFoco() {

        requestFocusInWindow();

    }

    /**
     * Indica si este Item corresponde al panel indicado.
     *
     * @param nombrePanel Nombre del panel.
     * @return true si corresponde.
     */
    public boolean esPanel(String nombrePanel) {

        return panelDestino.equals(nombrePanel);

    }

    /**
     * Habilita o deshabilita el Item.
     *
     * @param habilitado Estado.
     */
    /**
     * Habilita o deshabilita el Item.
     *
     * @param habilitado Estado.
     */
    public void setHabilitado(boolean habilitado) {

        this.habilitado = habilitado;

        setEnabled(habilitado);

        lblTexto.setEnabled(habilitado);

        lblIcono.setEnabled(habilitado);

        if (habilitado) {

            setCursor(CURSOR_MANO);

        } else {

            setCursor(CURSOR_NORMAL);

        }

        actualizarApariencia();

    }

    /**
     * Actualiza el texto del tooltip.
     */
    public void actualizarTooltip() {

        setToolTipText(titulo);

    }

    /**
     * Actualiza completamente el componente.
     */
    private void actualizarComponente() {

        revalidate();

        repaint();

    }

    /**
     * Indica si el Item está habilitado.
     *
     * @return true si está habilitado.
     */
    public boolean estaHabilitado() {

        return habilitado;

    }

    /**
     * Actualiza el componente cuando cambia el tema.
     */
    public void actualizarTema() {

        actualizarApariencia();

        repaint();

    }

    /**
     * Registra el MouseListener en un componente.
     *
     * @param componente Componente.
     * @param eventos Listener.
     */
    private void registrarMouse(
            Component componente,
            MouseAdapter eventos) {

        componente.addMouseListener(eventos);

    }

    // ==========================================================
    // AGREGAR COMPONENTES
    // ==========================================================
    /**
     * Agrega los componentes al panel.
     */
    private void agregarComponentes() {

        JPanel panelContenido = new JPanel();

        panelContenido.setOpaque(false);

        panelContenido.setLayout(
                new BoxLayout(
                        panelContenido,
                        BoxLayout.X_AXIS));

        panelContenido.add(
                Box.createHorizontalStrut(
                        PADDING_HORIZONTAL));

        panelContenido.add(lblIcono);

        panelContenido.add(
                Box.createHorizontalStrut(
                        ESPACIO_ICONO));

        panelContenido.add(lblTexto);

        panelContenido.add(Box.createHorizontalGlue());

        add(panelContenido, BorderLayout.CENTER);

    }
// ==========================================================
// APARIENCIA
// ==========================================================

    private void actualizarApariencia() {

        if (seleccionado) {

            aplicarEstadoSeleccionado();

        } else {

            aplicarEstadoNormal();

        }

        actualizarComponente();

    }
// ==========================================================
// ESTADO NORMAL
// ==========================================================

    /**
     * Restaura el aspecto normal del ItemMenu.
     */
    private void aplicarEstadoNormal() {

        setBackground(
                AdministradorTema.colorFondo());

        lblTexto.setForeground(
                AdministradorTema.colorTexto());

        lblIcono.setIcon(
                CacheImagenes.obtenerIcono(
                        iconoNormal,
                        AdministradorTema.iconoMenu()));

    }

// ==========================================================
// ESTADO SELECCIONADO
// ==========================================================
    /**
     * Aplica el aspecto cuando el elemento está seleccionado.
     */
private void aplicarEstadoSeleccionado() {

    setBackground(
            AdministradorTema.colorPrincipal());

    lblTexto.setForeground(
            AdministradorTema.colorTextoBlanco());

    lblIcono.setIcon(
            CacheImagenes.obtenerIcono(
                    iconoNormal,
                    AdministradorTema.iconoMenu()));

}
// ==========================================================
// HOVER
// ==========================================================
    /**
     * Apariencia cuando el mouse entra al componente.
     */
    private void aplicarHover() {

        if (seleccionado) {
            return;
        }

        setBackground(
                AdministradorTema.colorSecundario());

        lblTexto.setForeground(
                AdministradorTema.colorTexto());

    }

// ==========================================================
// RESTAURAR HOVER
// ==========================================================
    /**
     * Restaura el aspecto al salir el mouse.
     */
    private void restaurarHover() {

        if (seleccionado) {
            return;
        }

        aplicarEstadoNormal();

    }

// ==========================================================
// ESTADO
// ==========================================================
    /**
     * Cambia el estado seleccionado del componente.
     *
     * @param seleccionado true si debe quedar seleccionado.
     */
    public void setSeleccionado(boolean seleccionado) {

        if (seleccionado) {

            seleccionar();

        } else {

            deseleccionar();

        }

    }

    /**
     * Indica si el componente está seleccionado.
     *
     * @return true si está seleccionado.
     */
    public boolean estaSeleccionado() {

        return seleccionado;

    }

// ==========================================================
// INFORMACIÓN
// ==========================================================
    /**
     * Devuelve el nombre del panel asociado.
     *
     * @return Nombre del panel.
     */
    public String getPanelDestino() {

        return panelDestino;

    }

    /**
     * Devuelve el título del Item.
     *
     * @return Texto mostrado.
     */
    public String getTitulo() {

        return titulo;

    }
    // ==========================================================
// EVENTOS
// ==========================================================

    /**
     * Registra todos los eventos del componente.
     */
    private void registrarEventos() {

        MouseAdapter eventos = new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                aplicarHover();

            }

            @Override
            public void mouseExited(MouseEvent e) {

                restaurarHover();

            }

            @Override
            public void mousePressed(MouseEvent e) {

                if (!habilitado) {
                    return;
                }

                if (listener != null) {

                    listener.alSeleccionar(ItemMenu.this);

                }
            }

        };

        registrarMouse(this, eventos);

        registrarMouse(lblTexto, eventos);

        registrarMouse(lblIcono, eventos);

    }

// ==========================================================
// SELECCIÓN
// ==========================================================
    /**
     * Selecciona el Item.
     */
    public void seleccionar() {

        if (seleccionado) {
            return;
        }

        seleccionado = true;

        actualizarApariencia();

    }

    /**
     * Deselecciona el Item.
     */
    public void deseleccionar() {

        if (!seleccionado) {
            return;
        }

        seleccionado = false;

        actualizarApariencia();

    }

// ==========================================================
// ==========================================================
// MÉTODOS DE UTILIDAD
// ==========================================================
    /**
     * Activa el ItemMenu.
     */
    public void activar() {

        seleccionar();

    }

    /**
     * Desactiva el ItemMenu.
     */
    public void desactivar() {

        deseleccionar();

    }

    /**
     * Cambia el panel asociado al Item.
     *
     * Este método existe para mantener compatibilidad futura aunque actualmente
     * el panelDestino es final.
     *
     * @return Nombre del panel.
     */


    /**
     * Devuelve el nombre del icono normal.
     *
     * @return Nombre del icono.
     */
    public String getIconoNormal() {

        return iconoNormal;

    }

    /**
     * Cambia únicamente el texto mostrado.
     *
     * @param texto Nuevo texto.
     */
    public void setTexto(String texto) {

        lblTexto.setText(texto);

    }

    /**
     * Devuelve el JLabel del icono.
     *
     * @return JLabel del icono.
     */
    public JLabel getLabelIcono() {

        return lblIcono;

    }

// ==========================================================
// LIMPIAR
// ==========================================================
    /**
     * Restaura completamente el componente a su estado inicial.
     */
    public void limpiar() {

        seleccionado = false;

        habilitado = true;

        actualizarApariencia();

    }

    /**
     * Solicita la actualización completa del componente.
     */
    public void actualizar() {

        actualizarApariencia();

    }

// ==========================================================
// REINICIAR
// ==========================================================
    /**
     * Reinicia únicamente el estado visual.
     */
    public void reiniciarEstado() {

        deseleccionar();

    }

// ==========================================================
// TOSTRING
// ==========================================================
    @Override
    public String toString() {

        return "ItemMenu{"
                + "titulo='"
                + titulo
                + '\''
                + ", panelDestino='"
                + panelDestino
                + '\''
                + ", seleccionado="
                + seleccionado
                + '}';

    }

// ==========================================================
// PINTADO
// ==========================================================
@Override
protected void paintComponent(Graphics g) {

    Graphics2D g2 = (Graphics2D) g.create();

    g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

    g2.setColor(getBackground());

    g2.fillRoundRect(
            0,
            0,
            getWidth() - 1,
            getHeight() - 1,
            radioBorde,
            radioBorde);

    g2.setColor(colorBorde());

    g2.setStroke(new BasicStroke(1f));

    g2.drawRoundRect(
            0,
            0,
            getWidth() - 1,
            getHeight() - 1,
            radioBorde,
            radioBorde);

    g2.dispose();

    super.paintComponent(g);

}
// ==========================================================
// REVALIDAR APARIENCIA
// ==========================================================
    @Override
    public void updateUI() {

        super.updateUI();

        if (lblTexto != null) {

            actualizarApariencia();

        }

    }

    @Override
    public Dimension getMaximumSize() {

        return getPreferredSize();

    }

    private Color colorBorde() {

        if (seleccionado) {

            return AdministradorTema.colorPrincipal();

        }

        return new Color(220, 220, 220);

    }
}

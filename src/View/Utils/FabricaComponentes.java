package View.Utils;

import View.Componentes.PanelRedondeado;
import java.awt.*;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * --------------------------------------------------------------- Fábrica de
 * componentes reutilizables.
 *
 *
 * Contiene la creación de los botones principales del sistema.
 *
 * Todos los botones mantienen el mismo estilo visual para lograr una interfaz
 * uniforme en toda la aplicación.
 * ===============================================================
 */
public final class FabricaComponentes {

    private FabricaComponentes() {
    }

    // ==========================================================
    // BOTÓN PRIMARIO
    // ==========================================================
    /**
     * Crea el botón principal del sistema.
     *
     * Se utiliza para: • Guardar • Comprar • Confirmar • Iniciar sesión •
     * Registrar
     */
    public static JButton crearBotonPrimario(String texto) {

        JButton boton = new JButton(texto);

        configurarBotonBase(boton);

        boton.setBackground(
                AdministradorTema.colorPrincipal());

        boton.setForeground(
                AdministradorTema.colorTextoBlanco());

        boton.setPreferredSize(
                new Dimension(
    AdministradorTema.anchoBoton(),
    AdministradorTema.alturaBoton()));

        return boton;

    }

    // ==========================================================
    // BOTÓN SECUNDARIO
    // ==========================================================
    /**
     * Botón secundario.
     *
     * Se utiliza para: • Cancelar • Regresar • Editar • Acciones alternativas
     */
    public static JButton crearBotonSecundario(String texto) {

        JButton boton = new JButton(texto);

        configurarBotonBase(boton);

        boton.setBackground(
                AdministradorTema.colorSecundario());

        boton.setForeground(
                AdministradorTema.colorTexto());

        boton.setPreferredSize(
                new Dimension(
                        UIConstants.ANCHO_BOTON,
                        UIConstants.ALTURA_BOTON
                )
        );
        return boton;

    }

    // ==========================================================
    // BOTÓN DEL MENÚ
    // ==========================================================
    /**
     * Botón utilizado exclusivamente por la barra lateral.
     *
     * Contiene:
     *
     * Icono + Texto
     */
    public static JButton crearBotonMenu(
            String texto,
            ImageIcon icono) {

        JButton boton = new JButton(texto);

        configurarBotonBase(boton);

        boton.setIcon(icono);

        boton.setHorizontalAlignment(
                SwingConstants.LEFT);

        boton.setHorizontalTextPosition(
                SwingConstants.RIGHT);

        boton.setIconTextGap(
                UIConstants.ESPACIO_ICONO_MENU);

        boton.setBackground(
                AdministradorTema.colorPrincipal());

        boton.setForeground(
                AdministradorTema.colorTextoBlanco());

        boton.setPreferredSize(
                new Dimension(
                        AdministradorTema.anchoMenu() - AdministradorTema.margenMenu(),
                        AdministradorTema.alturaBotonMenu()));

        return boton;

    }

    // ==========================================================
    // BOTÓN CON ICONO
    // ==========================================================
    /**
     * Crea un botón con icono.
     *
     * Se utiliza para:
     *
     * Buscar Agregar Eliminar Editar Guardar
     */
    public static JButton crearBotonIcono(
            String texto,
            ImageIcon icono) {

        JButton boton = new JButton(texto);

        configurarBotonBase(boton);

        boton.setIcon(icono);

        boton.setIconTextGap(
    AdministradorTema.espacioIcono()
);

        boton.setHorizontalAlignment(
                SwingConstants.CENTER);

        boton.setBackground(
                AdministradorTema.colorSecundario());

        boton.setForeground(
                AdministradorTema.colorTexto());

        boton.setPreferredSize(
                new Dimension(
                        UIConstants.ANCHO_BOTON_ICONO,
                        UIConstants.ALTURA_BOTON));

        return boton;

    }

    // ==========================================================
    // CONFIGURACIÓN BASE
    // ==========================================================
    /**
     * Configuración común para todos los botones.
     */
    private static void configurarBotonBase(JButton boton) {

        boton.setFont(
                AdministradorTema.fuenteMedianaNegrita());

        boton.setFocusPainted(false);

        boton.setBorderPainted(false);

        boton.setContentAreaFilled(true);

        boton.setOpaque(true);

        boton.setCursor(
                new Cursor(Cursor.HAND_CURSOR));

        boton.setBorder(
                BorderFactory.createEmptyBorder(
        UIConstants.PADDING_BOTON_VERTICAL,
        UIConstants.PADDING_BOTON_HORIZONTAL,
        UIConstants.PADDING_BOTON_VERTICAL,
        UIConstants.PADDING_BOTON_HORIZONTAL));

    }
    // ==========================================================
// CAMPO DE BÚSQUEDA
// ==========================================================

    /**
     * Crea un campo de búsqueda con el estilo oficial del sistema.
     *
     * Este componente será utilizado en:
     *
     * • Usuarios • Productos • Pedidos • Reportes • Historial
     */
    public static JTextField crearCampoBusqueda() {

        JTextField campo = new JTextField();

        configurarCampoBase(campo);

        campo.setPreferredSize(
                new Dimension(
        AdministradorTema.anchoCampo(),
        AdministradorTema.alturaCampo()));

        campo.setToolTipText("Buscar...");

        return campo;

    }

// ==========================================================
// PASSWORD FIELD
// ==========================================================
    /**
     * Crea un campo para contraseñas.
     *
     * Utilizado en:
     *
     * • Login • Registro • Cambio de contraseña
     */
    public static JPasswordField crearPassword() {

        JPasswordField password = new JPasswordField();

        configurarCampoBase(password);

        password.setPreferredSize(
                new Dimension(
                        AdministradorTema.anchoCampo(),
                        AdministradorTema.alturaBoton()));

        return password;

    }

// ==========================================================
// COMBO BOX
// ==========================================================
    /**
     * Crea un JComboBox con el estilo oficial.
     *
     * Ejemplos:
     *
     * Rol
     *
     * Categoría
     *
     * Estado
     *
     * Método de pago
     */
    public static <T> JComboBox<T> crearCombo() {

        JComboBox<T> combo = new JComboBox<>();

        combo.setFont(
                AdministradorTema.fuenteNormal());

        combo.setBackground(
                AdministradorTema.colorTarjeta());

        combo.setForeground(
                AdministradorTema.colorTexto());

        combo.setFocusable(false);

        combo.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                AdministradorTema.colorBorde()),
                        BorderFactory.createEmptyBorder(
                                4,
                                8,
                                4,
                                8)));

        combo.setPreferredSize(
                new Dimension(
                        AdministradorTema.anchoCombo (),
                        AdministradorTema.alturaBoton()));

        return combo;

    }

// ==========================================================
// SPINNER
// ==========================================================
    /**
     * Crea un JSpinner con el estilo del sistema.
     *
     * Se utiliza para:
     *
     * Cantidad
     *
     * Stock
     *
     * Precio
     */
    public static JSpinner crearSpinner() {

        SpinnerNumberModel modelo
                = new SpinnerNumberModel(
                        1,
                        1,
                        9999,
                        1);

        JSpinner spinner
                = new JSpinner(modelo);

        spinner.setFont(
                AdministradorTema.fuenteNormal());

        spinner.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                AdministradorTema.colorBorde()),
                        BorderFactory.createEmptyBorder(
                                2,
                                8,
                                2,
                                8)));

        spinner.setPreferredSize(
                new Dimension(
                        90,
                        AdministradorTema.alturaBoton()));

        return spinner;

    }

// ==========================================================
// CONFIGURACIÓN BASE DE CAMPOS
// ==========================================================
    /**
     * Configuración común para todos los campos de texto.
     */
    private static void configurarCampoBase(JTextField campo) {

        campo.setFont(
                AdministradorTema.fuenteNormal());

        campo.setBackground(
                AdministradorTema.colorTarjeta());

        campo.setForeground(
                AdministradorTema.colorTexto());

        campo.setCaretColor(
                AdministradorTema.colorPrincipal());

        campo.setSelectionColor(
                AdministradorTema.colorSecundario());

        campo.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                AdministradorTema.colorBorde()),
                        BorderFactory.createEmptyBorder(
                                8,
                                10,
                                8,
                                10)));

    }
// ==========================================================
// SCROLL
// ==========================================================

    /**
     * Crea un JScrollPane con el estilo oficial del sistema.
     *
     * Se utiliza para:
     *
     * • Tablas • Listas • Paneles • Catálogo de productos
     */
    public static JScrollPane crearScroll(Component componente) {

        JScrollPane scroll = new JScrollPane(componente);

        scroll.setBorder(BorderFactory.createEmptyBorder());

        scroll.getViewport().setBackground(
                AdministradorTema.colorFondo());

        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scroll.getVerticalScrollBar().setUnitIncrement(16);

        return scroll;

    }

// ==========================================================
// TABLA
// ==========================================================
    /**
     * Crea una JTable con el estilo oficial del sistema.
     *
     * Todas las tablas del sistema deberán utilizar este método.
     */
    public static JTable crearTabla() {

        JTable tabla = new JTable();

        tabla.setFont(
                AdministradorTema.fuenteNormal());

        tabla.setForeground(
                AdministradorTema.colorTexto());

        tabla.setBackground(
                AdministradorTema.colorTarjeta());

        tabla.setRowHeight(35);

        tabla.setGridColor(
                AdministradorTema.colorBorde());

        tabla.setSelectionBackground(
                AdministradorTema.colorPrincipal());

        tabla.setSelectionForeground(
                AdministradorTema.colorTextoBlanco());

        tabla.getTableHeader().setFont(
                AdministradorTema.fuenteNormalNegrita());

        tabla.getTableHeader().setBackground(
                AdministradorTema.colorPrincipal());

        tabla.getTableHeader().setForeground(
                AdministradorTema.colorTextoBlanco());

        tabla.getTableHeader().setReorderingAllowed(false);

        tabla.getTableHeader().setResizingAllowed(false);

        tabla.setFillsViewportHeight(true);

        return tabla;

    }

// ==========================================================
// SEPARADOR
// ==========================================================
    /**
     * Crea un separador horizontal.
     *
     * Se utiliza para dividir secciones visuales.
     */
    public static JSeparator crearSeparador() {

        JSeparator separador = new JSeparator();

        separador.setForeground(
                AdministradorTema.colorBorde());

        separador.setBackground(
                AdministradorTema.colorBorde());

        return separador;

    }

// ==========================================================
// ÁREA DE TEXTO
// ==========================================================
    /**
     * Crea un JTextArea con el estilo del sistema.
     *
     * Utilizado para:
     *
     * • Descripciones • Observaciones • Comentarios
     */
    public static JTextArea crearTextArea() {

        JTextArea area = new JTextArea();

        area.setFont(
                AdministradorTema.fuenteNormal());

        area.setBackground(
                AdministradorTema.colorTarjeta());

        area.setForeground(
                AdministradorTema.colorTexto());

        area.setCaretColor(
                AdministradorTema.colorPrincipal());

        area.setLineWrap(true);

        area.setWrapStyleWord(true);

        area.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                AdministradorTema.colorBorde()),
                        BorderFactory.createEmptyBorder(
                                8,
                                10,
                                8,
                                10)));

        return area;

    }

// ==========================================================
// SCROLL PANEL
// ==========================================================
    /**
     * Crea un panel preparado para contener muchos componentes utilizando
     * desplazamiento vertical.
     *
     * Será utilizado principalmente para:
     *
     * • Catálogo de productos • Promociones • Historial • Reportes
     */
    public static JScrollPane crearScrollPanel(JPanel panel) {

        panel.setOpaque(false);

        JScrollPane scroll = new JScrollPane(panel);

        scroll.setBorder(BorderFactory.createEmptyBorder());

        scroll.getViewport().setOpaque(false);

        scroll.getViewport().setBackground(
                AdministradorTema.colorFondo());

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        scroll.getVerticalScrollBar().setUnitIncrement(18);

        return scroll;

    }
//==========================================================
// PANELES
//==========================================================

    //==========================================================
    // PANEL CON TÍTULO
    //==========================================================
    /**
     * Crea un panel con un título superior.
     */
    public static JPanel crearPanelTitulo(String titulo) {

        JPanel contenedor = new JPanel(new BorderLayout());

        contenedor.setOpaque(false);

        JLabel lblTitulo = new JLabel(titulo);

        lblTitulo.setFont(
                AdministradorTema.fuenteTituloNegrita());

        lblTitulo.setForeground(
                AdministradorTema.colorPrincipal());

        lblTitulo.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        0,
                        AdministradorTema.espacioMediano(),
                        0));

        contenedor.add(lblTitulo, BorderLayout.NORTH);

        return contenedor;

    }

    //==========================================================
    // TARJETA
    //==========================================================
    /**
     * Tarjeta blanca reutilizable.
     */
    public static JPanel crearTarjeta() {

        PanelRedondeado tarjeta
                = new PanelRedondeado(
                        AdministradorTema.radioTarjeta());

        tarjeta.setBackground(
                AdministradorTema.colorTarjeta());

        tarjeta.setBorder(
                AdministradorTema.bordeTarjeta());

        tarjeta.setLayout(new BorderLayout());

        return tarjeta;

    }

    /**
     * Tarjeta con tamaño fijo.
     */
    public static JPanel crearTarjeta(
            int ancho,
            int alto) {

        JPanel tarjeta = crearTarjeta();

        tarjeta.setPreferredSize(
                new Dimension(ancho, alto));

        return tarjeta;

    }

    /**
     * Tarjeta con Layout personalizado.
     */
    public static JPanel crearTarjeta(
            java.awt.LayoutManager layout) {

        PanelRedondeado tarjeta
                = new PanelRedondeado(
                        AdministradorTema.radioTarjeta());

        tarjeta.setBackground(
                AdministradorTema.colorTarjeta());

        tarjeta.setBorder(
                AdministradorTema.bordeTarjeta());

        tarjeta.setLayout(layout);

        return tarjeta;

    }

    //==========================================================
    // PANEL REDONDEADO
    //==========================================================
    /**
     * Panel redondeado estándar.
     */
    public static PanelRedondeado crearPanelRedondeado() {

        PanelRedondeado panel
                = new PanelRedondeado(
                        AdministradorTema.radioPanel());

        panel.setBackground(
                AdministradorTema.colorTarjeta());

        panel.setBorder(
                AdministradorTema.bordeGeneral());

        return panel;

    }

    /**
     * Panel redondeado con color personalizado.
     */
    public static PanelRedondeado crearPanelRedondeado(
            java.awt.Color color) {

        PanelRedondeado panel
                = crearPanelRedondeado();

        panel.setBackground(color);

        return panel;

    }

    /**
     * Panel redondeado con Layout personalizado.
     */
    public static PanelRedondeado crearPanelRedondeado(
            java.awt.LayoutManager layout) {

        PanelRedondeado panel
                = crearPanelRedondeado();

        panel.setLayout(layout);

        return panel;

    }

    /**
     * Panel redondeado con radio personalizado.
     */
    public static PanelRedondeado crearPanelRedondeado(
            int radio) {

        PanelRedondeado panel
                = new PanelRedondeado(radio);

        panel.setBackground(
                AdministradorTema.colorTarjeta());

        panel.setBorder(
                AdministradorTema.bordeGeneral());

        return panel;

    }

    /**
     * Panel horizontal.
     */
    public static JPanel crearPanelHorizontal() {

        JPanel panel = crearPanelRedondeado();

        panel.setLayout(
                new FlowLayout(
                        FlowLayout.LEFT,
                        AdministradorTema.espacioMediano(),
                        AdministradorTema.espacioMediano()));

        return panel;

    }

    /**
     * Panel BorderLayout.
     */
    public static JPanel crearPanelBorder() {

        JPanel panel = crearPanelRedondeado();

        panel.setLayout(new BorderLayout());

        return panel;

    }

//==========================================================
// ETIQUETAS
//==========================================================
    /**
     * Crea una etiqueta de título principal.
     *
     * @param texto Texto del título.
     * @return JLabel estilizado.
     */
    public static JLabel crearLabelTitulo(String texto) {

        JLabel etiqueta = new JLabel(texto);

        etiqueta.setFont(
                AdministradorTema.fuenteTituloNegrita());

        etiqueta.setForeground(
                AdministradorTema.colorPrincipal());

        return etiqueta;

    }

    /**
     * Crea una etiqueta de subtítulo.
     *
     * @param texto Texto del subtítulo.
     * @return JLabel estilizado.
     */
    public static JLabel crearLabelSubtitulo(String texto) {

        JLabel etiqueta = new JLabel(texto);

        etiqueta.setFont(
                AdministradorTema.fuenteMedianaNegrita());

        etiqueta.setForeground(
                AdministradorTema.colorTexto());

        return etiqueta;

    }

    /**
     * Crea una etiqueta de texto normal.
     *
     * @param texto Texto.
     * @return JLabel estilizado.
     */
    public static JLabel crearLabelTexto(String texto) {

        JLabel etiqueta = new JLabel(texto);

        etiqueta.setFont(
                AdministradorTema.fuenteNormal());

        etiqueta.setForeground(
                AdministradorTema.colorTexto());

        return etiqueta;

    }

    /**
     * Crea una etiqueta de texto pequeño.
     *
     * @param texto Texto.
     * @return JLabel estilizado.
     */
    public static JLabel crearLabelPequeño(String texto) {

        JLabel etiqueta = new JLabel(texto);

        etiqueta.setFont(
                AdministradorTema.fuentePequeña());

        etiqueta.setForeground(
                AdministradorTema.colorTexto());

        return etiqueta;

    }

    /**
     * Crea una etiqueta centrada.
     *
     * @param texto Texto.
     * @return JLabel estilizado.
     */
    public static JLabel crearLabelCentrado(String texto) {

        JLabel etiqueta = crearLabelTexto(texto);

        etiqueta.setHorizontalAlignment(JLabel.CENTER);

        return etiqueta;

    }

//==========================================================
// CHECKBOX
//==========================================================
    /**
     * Crea un CheckBox con el estilo del sistema.
     *
     * @param texto Texto.
     * @return JCheckBox estilizado.
     */
    public static JCheckBox crearCheckBox(String texto) {

        JCheckBox check = new JCheckBox(texto);

        check.setOpaque(false);

        check.setFocusPainted(false);

        check.setFont(
                AdministradorTema.fuenteNormal());

        check.setForeground(
                AdministradorTema.colorTexto());

        check.setCursor(
                new Cursor(Cursor.HAND_CURSOR));

        return check;

    }

    /**
     * Crea un CheckBox seleccionado.
     *
     * @param texto Texto.
     * @param seleccionado Estado inicial.
     * @return JCheckBox estilizado.
     */
    public static JCheckBox crearCheckBox(
            String texto,
            boolean seleccionado) {

        JCheckBox check = crearCheckBox(texto);

        check.setSelected(seleccionado);

        return check;

    }

//==========================================================
// RADIO BUTTON
//==========================================================
    /**
     * Crea un RadioButton con el estilo del sistema.
     *
     * @param texto Texto.
     * @return JRadioButton estilizado.
     */
    public static JRadioButton crearRadioButton(String texto) {

        JRadioButton radio = new JRadioButton(texto);

        radio.setOpaque(false);

        radio.setFocusPainted(false);

        radio.setFont(
                AdministradorTema.fuenteNormal());

        radio.setForeground(
                AdministradorTema.colorTexto());

        radio.setCursor(
                new Cursor(Cursor.HAND_CURSOR));

        return radio;

    }

    /**
     * Crea un RadioButton con estado inicial.
     *
     * @param texto Texto.
     * @param seleccionado Estado inicial.
     * @return JRadioButton estilizado.
     */
    public static JRadioButton crearRadioButton(
            String texto,
            boolean seleccionado) {

        JRadioButton radio = crearRadioButton(texto);

        radio.setSelected(seleccionado);

        return radio;

    }
    //==========================================================
// MÉTODOS PRIVADOS
//==========================================================

    /**
     * Configura una fuente y color a una etiqueta.
     */
    private static void configurarLabel(
            JLabel label,
            Font fuente,
            Color color) {

        label.setFont(fuente);
        label.setForeground(color);

    }

    /**
     * Configura una fuente para un componente.
     */
    private static void configurarFuente(
            JComponent componente,
            Font fuente) {

        componente.setFont(fuente);

    }

    /**
     * Configura el cursor tipo mano.
     */
    private static void configurarCursorMano(
            JComponent componente) {

        componente.setCursor(
                new Cursor(Cursor.HAND_CURSOR));

    }

    /**
     * Aplica un borde vacío estándar.
     */
    private static void aplicarBorde(
            JComponent componente,
            int arriba,
            int izquierda,
            int abajo,
            int derecha) {

        componente.setBorder(
                BorderFactory.createEmptyBorder(
                        arriba,
                        izquierda,
                        abajo,
                        derecha));

    }

    /**
     * Aplica el borde general del sistema.
     */
    private static void aplicarBordeGeneral(
            JComponent componente) {

        componente.setBorder(
                AdministradorTema.bordeGeneral());

    }

    /**
     * Configura el fondo del componente.
     */
    private static void configurarFondo(
            JComponent componente,
            Color color) {

        componente.setBackground(color);

    }

    /**
     * Configura el color del texto.
     */
    private static void configurarTexto(
            JComponent componente,
            Color color) {

        componente.setForeground(color);

    }

    /**
     * Configura un botón básico.
     */
    private static void configurarBoton(
            JButton boton,
            Color fondo,
            Color texto,
            Font fuente) {

        boton.setBackground(fondo);

        boton.setForeground(texto);

        boton.setFont(fuente);

        boton.setFocusPainted(false);

        boton.setCursor(
                new Cursor(Cursor.HAND_CURSOR));

    }

    /**
     * Configura un JTextComponent.
     */
    private static void configurarCampoTexto(
            JTextComponent campo) {

        campo.setFont(
                AdministradorTema.fuenteNormal());

        campo.setForeground(
                AdministradorTema.colorTexto());

        campo.setBackground(
                AdministradorTema.colorTarjeta());

    }

    /**
     * Configura cualquier panel.
     */
    private static void configurarPanel(
            JPanel panel,
            LayoutManager layout,
            Color color) {

        panel.setLayout(layout);

        panel.setBackground(color);

    }

    /**
     * Establece un tamaño fijo.
     */
    private static void tamañoFijo(
            JComponent componente,
            int ancho,
            int alto) {

        Dimension tamaño
                = new Dimension(ancho, alto);

        componente.setPreferredSize(tamaño);

        componente.setMinimumSize(tamaño);

        componente.setMaximumSize(tamaño);

    }

    /**
     * Centra horizontalmente un JLabel.
     */
    private static void centrar(
            JLabel label) {

        label.setHorizontalAlignment(
                SwingConstants.CENTER);

    }

    /**
     * Hace transparente un componente.
     */
    private static void transparente(
            JComponent componente) {

        componente.setOpaque(false);

    }

    /**
     * Desactiva el foco.
     */
    private static void quitarFocus(
            AbstractButton boton) {

        boton.setFocusPainted(false);

    }
}

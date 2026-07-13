package View.Utils;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * --------------------------------------------------------------- Administrador
 * de estilos visuales.
 *
 * Esta clase aplica la apariencia oficial del sistema a todos los componentes
 * Swing.
 *
 * NO crea componentes. NO contiene lógica. Solamente aplica estilos.
 * ===============================================================
 */
public final class EstilosComponentes {

    private EstilosComponentes() {
    }

    // ==========================================================
    // BOTONES
    // ==========================================================
    /**
     * Aplica el estilo base para cualquier JButton.
     */
    public static void aplicarEstiloBoton(JButton boton) {

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
    // CAMPOS
    // ==========================================================
    /**
     * Estilo para cualquier JTextComponent.
     */
    public static void aplicarEstiloCampo(
            JTextComponent campo) {

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

        campo.setSelectedTextColor(
                AdministradorTema.colorTextoBlanco());

        campo.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                AdministradorTema.colorBorde()),
                        BorderFactory.createEmptyBorder(
                                UIConstants.PADDING_CAMPO_VERTICAL,
                                UIConstants.PADDING_CAMPO_HORIZONTAL,
                                UIConstants.PADDING_CAMPO_VERTICAL,
                                UIConstants.PADDING_CAMPO_HORIZONTAL)));

    }

    // ==========================================================
    // COMBOBOX
    // ==========================================================
    public static void aplicarEstiloCombo(
            JComboBox<?> combo) {

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
                                UIConstants.PADDING_COMBO_VERTICAL,
                                UIConstants.PADDING_COMBO_HORIZONTAL,
                                UIConstants.PADDING_COMBO_VERTICAL,
                                UIConstants.PADDING_COMBO_HORIZONTAL)));

    }

    // ==========================================================
    // SPINNER
    // ==========================================================
    public static void aplicarEstiloSpinner(
            JSpinner spinner) {

        spinner.setFont(
                AdministradorTema.fuenteNormal());

        spinner.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                AdministradorTema.colorBorde()),
                        BorderFactory.createEmptyBorder(
                                UIConstants.PADDING_SPINNER_VERTICAL,
                                UIConstants.PADDING_COMBO_HORIZONTAL,
                                UIConstants.PADDING_SPINNER_VERTICAL,
                                UIConstants.PADDING_COMBO_HORIZONTAL)));

    }

    // ==========================================================
    // TABLAS
    // ==========================================================
    public static void aplicarEstiloTabla(
            JTable tabla) {

        tabla.setFont(
                AdministradorTema.fuenteNormal());

        tabla.setForeground(
                AdministradorTema.colorTexto());

        tabla.setBackground(
                AdministradorTema.colorTarjeta());

        tabla.setGridColor(
                AdministradorTema.colorBorde());

        tabla.setRowHeight(
                UIConstants.ALTURA_FILA_TABLA);

        tabla.setSelectionBackground(
                AdministradorTema.colorPrincipal());

        tabla.setSelectionForeground(
                AdministradorTema.colorTextoBlanco());

        tabla.setFillsViewportHeight(true);

        JTableHeader header
                = tabla.getTableHeader();

        header.setFont(
                AdministradorTema.fuenteNormalNegrita());

        header.setBackground(
                AdministradorTema.colorPrincipal());

        header.setForeground(
                AdministradorTema.colorTextoBlanco());

        header.setReorderingAllowed(false);

        header.setResizingAllowed(false);

    }
    // ==========================================================
// SCROLL
// ==========================================================

    /**
     * Aplica el estilo oficial a un JScrollPane.
     */
    public static void aplicarEstiloScroll(JScrollPane scroll) {

        scroll.setBorder(BorderFactory.createEmptyBorder());

        scroll.getViewport().setBackground(
                AdministradorTema.colorFondo());

        scroll.getViewport().setOpaque(true);

        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scroll.getVerticalScrollBar().setUnitIncrement(
                UIConstants.VELOCIDAD_SCROLL);

    }

// ==========================================================
// TEXT AREA
// ==========================================================
    /**
     * Aplica el estilo oficial a un JTextArea.
     */
    public static void aplicarEstiloTextArea(
            JTextArea area) {

        aplicarEstiloCampo(area);

        area.setRows(
                UIConstants.TEXTAREA_FILAS);

        area.setColumns(
                UIConstants.TEXTAREA_COLUMNAS);

        area.setLineWrap(true);

        area.setWrapStyleWord(true);

    }

// ==========================================================
// TARJETA
// ==========================================================
    /**
     * Aplica el estilo visual de una tarjeta.
     */
    public static void aplicarEstiloTarjeta(
            JPanel panel) {

        panel.setBackground(
                AdministradorTema.colorTarjeta());

        panel.setBorder(
                AdministradorTema.bordeTarjeta());

    }

// ==========================================================
// PANEL
// ==========================================================
    /**
     * Aplica el estilo estándar de un panel.
     */
    public static void aplicarEstiloPanel(
            JPanel panel) {

        panel.setBackground(
                AdministradorTema.colorTarjeta());

        panel.setBorder(
                AdministradorTema.bordeGeneral());

    }

// ==========================================================
// ETIQUETA
// ==========================================================
    /**
     * Aplica un estilo personalizado a una etiqueta.
     */
    public static void aplicarEstiloEtiqueta(
            JLabel etiqueta,
            Font fuente,
            Color color) {

        etiqueta.setFont(fuente);

        etiqueta.setForeground(color);

    }

// ==========================================================
// CHECK BOX
// ==========================================================
    /**
     * Aplica el estilo oficial a un JCheckBox.
     */
    public static void aplicarEstiloCheckBox(
            JCheckBox checkBox) {

        checkBox.setOpaque(false);

        checkBox.setFocusPainted(false);

        checkBox.setCursor(
                new Cursor(Cursor.HAND_CURSOR));

        checkBox.setFont(
                AdministradorTema.fuenteNormal());

        checkBox.setForeground(
                AdministradorTema.colorTexto());

    }

// ==========================================================
// RADIO BUTTON
// ==========================================================
    /**
     * Aplica el estilo oficial a un JRadioButton.
     */
    public static void aplicarEstiloRadioButton(
            JRadioButton radioButton) {

        radioButton.setOpaque(false);

        radioButton.setFocusPainted(false);

        radioButton.setCursor(
                new Cursor(Cursor.HAND_CURSOR));

        radioButton.setFont(
                AdministradorTema.fuenteNormal());

        radioButton.setForeground(
                AdministradorTema.colorTexto());

    }

    public static void aplicarBotonPrimario(JButton boton) {

        aplicarEstiloBoton(boton);

        boton.setBackground(
                AdministradorTema.colorPrincipal());

        boton.setForeground(
                AdministradorTema.colorTextoBlanco());

    }

    public static void aplicarBotonSecundario(JButton boton) {

        aplicarEstiloBoton(boton);

        boton.setBackground(
                AdministradorTema.colorSecundario());

        boton.setForeground(
                AdministradorTema.colorTexto());

    }

    public static void aplicarBotonMenu(JButton boton) {

        aplicarBotonPrimario(boton);

        boton.setHorizontalAlignment(
                SwingConstants.LEFT);

        boton.setHorizontalTextPosition(
                SwingConstants.RIGHT);

        boton.setIconTextGap(
                UIConstants.ESPACIO_ICONO_MENU);

    }

    public static void aplicarBotonIcono(JButton boton) {

        aplicarBotonSecundario(boton);

        boton.setHorizontalAlignment(
                SwingConstants.CENTER);

        boton.setIconTextGap(
                UIConstants.ESPACIO_ICONO);

    }
// ==========================================================
// TAMAÑO
// ==========================================================

    /**
     * Aplica un tamaño fijo a cualquier componente Swing.
     */
    public static void aplicarTamaño(
            JComponent componente,
            int ancho,
            int alto) {

        Dimension dimension = new Dimension(ancho, alto);

        componente.setPreferredSize(dimension);
        componente.setMinimumSize(dimension);
        componente.setMaximumSize(dimension);

    }
    // ==========================================================
// TAMAÑO PREFERIDO
// ==========================================================

/**
 * Establece únicamente el tamaño preferido del componente.
 */
public static void aplicarTamañoPreferido(
        JComponent componente,
        int ancho,
        int alto) {

    componente.setPreferredSize(
            new Dimension(ancho, alto));

}
// ==========================================================
// ANCHO
// ==========================================================

/**
 * Cambia únicamente el ancho del componente.
 */
public static void aplicarAncho(
        JComponent componente,
        int ancho) {

    Dimension tamaño = componente.getPreferredSize();

    componente.setPreferredSize(
            new Dimension(ancho, tamaño.height));

}
// ==========================================================
// ALTO
// ==========================================================

/**
 * Cambia únicamente la altura del componente.
 */
public static void aplicarAlto(
        JComponent componente,
        int alto) {

    Dimension tamaño = componente.getPreferredSize();

    componente.setPreferredSize(
            new Dimension(tamaño.width, alto));

}
// ==========================================================
// ETIQUETA TÍTULO
// ==========================================================

public static void aplicarEtiquetaTitulo(
        JLabel etiqueta) {

    aplicarEstiloEtiqueta(
            etiqueta,
            AdministradorTema.fuenteTituloNegrita(),
            AdministradorTema.colorPrincipal());

}
// ==========================================================
// ETIQUETA SUBTÍTULO
// ==========================================================

public static void aplicarEtiquetaSubtitulo(
        JLabel etiqueta) {

    aplicarEstiloEtiqueta(
            etiqueta,
            AdministradorTema.fuenteMedianaNegrita(),
            AdministradorTema.colorTexto());

}
    // ==========================================================
// ETIQUETA TEXTO
// ==========================================================

public static void aplicarEtiquetaTexto(
        JLabel etiqueta) {

    aplicarEstiloEtiqueta(
            etiqueta,
            AdministradorTema.fuenteNormal(),
            AdministradorTema.colorTexto());

}
// ==========================================================
// ETIQUETA PEQUEÑA
// ==========================================================

public static void aplicarEtiquetaPequeña(
        JLabel etiqueta) {

    aplicarEstiloEtiqueta(
            etiqueta,
            AdministradorTema.fuentePequeña(),
            AdministradorTema.colorTexto());

}
}

package View.Utils;

import View.Componentes.PanelRedondeado;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Fábrica especializada en la creación de tablas.
 *
 * Responsabilidades:
 *
 * • Crear JTable.
 * • Crear modelos de tabla.
 * • Crear ScrollPane para tablas.
 * • Crear tablas editables.
 * • Crear tablas de solo lectura.
 *
 * Toda la apariencia visual es aplicada por
 * EstilosComponentes.
 * ===============================================================
 */
public final class FabricaTablas {

    private FabricaTablas() {
    }

    // ==========================================================
    // TABLA VACÍA
    // ==========================================================

    /**
     * Crea una JTable vacía con el estilo oficial.
     */
    public static JTable crearTabla() {

        JTable tabla = new JTable();

        EstilosComponentes.aplicarEstiloTabla(tabla);

        return tabla;

    }

    // ==========================================================
    // TABLA CON MODELO
    // ==========================================================

    /**
     * Crea una JTable utilizando un modelo.
     */
    public static JTable crearTabla(TableModel modelo) {

        JTable tabla = new JTable(modelo);

        EstilosComponentes.aplicarEstiloTabla(tabla);

        return tabla;

    }

    // ==========================================================
    // MODELO DE TABLA
    // ==========================================================

    /**
     * Modelo editable.
     */
    public static DefaultTableModel crearModelo(
            Object[] columnas) {

        return new DefaultTableModel(
                null,
                columnas);

    }

    /**
     * Modelo editable con datos.
     */
    public static DefaultTableModel crearModelo(
            Object[][] datos,
            Object[] columnas) {

        return new DefaultTableModel(
                datos,
                columnas);

    }

    // ==========================================================
    // TABLA SOLO LECTURA
    // ==========================================================

    /**
     * Crea un modelo donde ninguna celda puede editarse.
     */
    public static DefaultTableModel crearModeloSoloLectura(
            Object[] columnas) {

        return new DefaultTableModel(
                null,
                columnas) {

            @Override
            public boolean isCellEditable(
                    int fila,
                    int columna) {

                return false;

            }

        };

    }

    // ==========================================================
    // TABLA SOLO LECTURA CON DATOS
    // ==========================================================

    public static JTable crearTablaSoloLectura(
            Object[][] datos,
            Object[] columnas) {

        DefaultTableModel modelo =
                new DefaultTableModel(
                        datos,
                        columnas) {

            @Override
            public boolean isCellEditable(
                    int fila,
                    int columna) {

                return false;

            }

        };

        return crearTabla(modelo);

    }

    // ==========================================================
    // TABLA EDITABLE
    // ==========================================================

    /**
     * Crea una tabla editable.
     */
    public static JTable crearTablaEditable(
            Object[][] datos,
            Object[] columnas) {

        DefaultTableModel modelo =
                new DefaultTableModel(
                        datos,
                        columnas);

        return crearTabla(modelo);

    }

    // ==========================================================
    // SCROLL DE TABLA
    // ==========================================================

    /**
     * Crea un JScrollPane para una tabla.
     */
    public static JScrollPane crearScrollTabla(
            JTable tabla) {

        JScrollPane scroll =
                new JScrollPane(tabla);

        EstilosComponentes.aplicarEstiloScroll(scroll);

        return scroll;

    }

    // ==========================================================
    // PANEL DE TABLA
    // ==========================================================

    /**
     * Crea un panel que contiene únicamente la tabla.
     */
    public static JPanel crearPanelTabla(
            JTable tabla) {

        JPanel panel =
                FabricaPaneles.crearBorder();

        panel.add(
                crearScrollTabla(tabla),
                BorderLayout.CENTER);

        return panel;

    }
private static PanelRedondeado crearPanel(
        int radio,
        LayoutManager layout,
        Color color,
        Border border) {

    PanelRedondeado panel =
            new PanelRedondeado(radio);

    panel.setLayout(layout);

    panel.setBackground(color);

    panel.setBorder(border);

    return panel;

}
public static PanelRedondeado crearPanelRedondeado() {

    return crearPanel(
            AdministradorTema.radioPanel(),
            new FlowLayout(),
            AdministradorTema.colorTarjeta(),
            AdministradorTema.bordeGeneral());

}
public static PanelRedondeado crearPanelRedondeado(
        LayoutManager layout) {

    return crearPanel(
            AdministradorTema.radioPanel(),
            layout,
            AdministradorTema.colorTarjeta(),
            AdministradorTema.bordeGeneral());

}
public static PanelRedondeado crearPanelRedondeado(
        Color color) {

    return crearPanel(
            AdministradorTema.radioPanel(),
            new FlowLayout(),
            color,
            AdministradorTema.bordeGeneral());

}
public static PanelRedondeado crearPanelRedondeado(
        int radio) {

    return crearPanel(
            radio,
            new FlowLayout(),
            AdministradorTema.colorTarjeta(),
            AdministradorTema.bordeGeneral());

}
public static JPanel crearTarjeta() {

    return crearPanel(
            AdministradorTema.radioTarjeta(),
            new BorderLayout(),
            AdministradorTema.colorTarjeta(),
            AdministradorTema.bordeTarjeta());

}
public static JPanel crearTarjeta(
        LayoutManager layout) {

    return crearPanel(
            AdministradorTema.radioTarjeta(),
            layout,
            AdministradorTema.colorTarjeta(),
            AdministradorTema.bordeTarjeta());

}
public static JPanel crearBorder() {

    return crearPanelRedondeado(
            new BorderLayout());

}
public static JPanel crearFlow() {

    return crearPanelRedondeado(
            new FlowLayout(
                    FlowLayout.LEFT,
                    AdministradorTema.espacioMediano(),
                    AdministradorTema.espacioMediano()));

}
}
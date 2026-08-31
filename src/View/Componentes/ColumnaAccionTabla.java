package View.Componentes;

import View.Utils.FabricaBotones;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Instala una columna de "botón de acción" (Atender / Marcar listo /
 * Entregar / Ver detalle) dentro de una JTable ya construida con
 * FabricaTablas.
 *
 * Es UNA sola clase para las 4 pantallas del Trabajador que
 * necesitan esto (Pendientes, En preparación, Listos, Historial)
 * en vez de reescribir el mismo par TableCellRenderer/TableCellEditor
 * cuatro veces — el mismo criterio que ya usa TarjetaKPI para las
 * tarjetas.
 *
 * Uso típico, después de crear la tabla:
 *
 *     ColumnaAccionTabla.instalar(
 *             tabla,
 *             indiceColumnaAccion,
 *             "Atender",
 *             PaletaColores.PRINCIPAL,
 *             fila -> atenderPedido(pedidosActuales.get(fila))
 *     );
 * ===============================================================
 */
public final class ColumnaAccionTabla {

    private ColumnaAccionTabla() {
    }

    /**
     * @param tabla        la JTable ya creada (con su modelo asignado).
     * @param columna      índice de la columna "Acción".
     * @param texto        texto fijo del botón (ej. "Atender").
     * @param colorFondo   color de fondo del botón (según el estado que representa).
     * @param alPresionar  qué hacer cuando se presiona, recibe la fila del modelo (0-based).
     */
    public static void instalar(
            JTable tabla,
            int columna,
            String texto,
            Color colorFondo,
            Consumer<Integer> alPresionar) {

        instalar(tabla, columna, fila -> texto, fila -> colorFondo, fila -> true, alPresionar);
    }

    /**
     * Variante donde el texto/color del botón puede depender de la fila
     * (ej. Historial: "Ver detalle" siempre visible, pero deshabilitado
     * si no aplica).
     */
    public static void instalar(
            JTable tabla,
            int columna,
            IntFunction<String> texto,
            IntFunction<Color> color,
            IntPredicate habilitado,
            Consumer<Integer> alPresionar) {

        tabla.getColumnModel().getColumn(columna).setCellRenderer(
                (TableCellRenderer) (t, value, isSelected, hasFocus, row, col) -> {

                    JButton boton = FabricaBotones.crearAccion(texto.apply(row));
                    boton.setBackground(color.apply(row));
                    boton.setEnabled(habilitado.test(row));
                    return boton;
                }
        );

        tabla.getColumnModel().getColumn(columna).setCellEditor(
                new BotonEditor(texto, color, habilitado, alPresionar)
        );
    }

    /**
     * Editor real de la celda-botón. Tiene que ser una clase con
     * nombre (no anónima): en Java una clase anónima solo puede
     * "extends" una clase O "implements" una interfaz, nunca las dos
     * a la vez — y AbstractCellEditor (que ya trae resuelto
     * addCellEditorListener/stopCellEditing/etc.) no implementa por
     * sí solo TableCellEditor, así que hay que declarar ambas cosas
     * explícitamente aquí.
     */
    private static final class BotonEditor extends AbstractCellEditor implements TableCellEditor {

        private final IntFunction<String> texto;
        private final IntFunction<Color> color;
        private final IntPredicate habilitado;
        private final Consumer<Integer> alPresionar;

        private BotonEditor(
                IntFunction<String> texto,
                IntFunction<Color> color,
                IntPredicate habilitado,
                Consumer<Integer> alPresionar) {

            this.texto = texto;
            this.color = color;
            this.habilitado = habilitado;
            this.alPresionar = alPresionar;
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable t, Object value, boolean isSelected, int row, int col) {

            JButton boton = FabricaBotones.crearAccion(texto.apply(row));
            boton.setBackground(color.apply(row));
            boton.setEnabled(habilitado.test(row));

            boton.addActionListener(e -> {
                fireEditingStopped();
                alPresionar.accept(row);
            });

            return boton;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
}

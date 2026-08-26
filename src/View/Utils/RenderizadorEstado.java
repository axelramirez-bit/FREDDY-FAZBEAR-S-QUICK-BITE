package View.Utils;

import View.Componentes.EtiquetaEstado;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JPanel;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Pinta UNA columna de una JTable como badges de EtiquetaEstado
 * en vez de texto plano. Se aplica por columna, no por tabla
 * completa, porque una tabla puede tener "Estado" como badge y
 * "Precio" como texto normal en la misma fila.
 *
 * USO (dentro del panel que arma la tabla, típicamente en
 * PanelCrudBase.crearPanelTabla() o donde ya tengas la JTable):
 *
 *     int columnaEstado = 4; // el índice de "Estado" en getColumnas()
 *
 *     tabla.getColumnModel()
 *          .getColumn(columnaEstado)
 *          .setCellRenderer(new RenderizadorEstado());
 *
 * Por defecto usa EtiquetaEstado.automatico(texto), que ya cubre
 * los estados reales del proyecto (Pendiente, Preparacion, Listo,
 * Entregado, Cancelado, Pagado, Rechazado, Activa, Programada,
 * Vencida, Disponible, Agotado...). Si una pantalla necesita su
 * propio mapeo de color (poco común), pasa un
 * RenderizadorEstado.Colorizador personalizado al segundo
 * constructor en vez de tocar EtiquetaEstado.
 * ===============================================================
 */
public class RenderizadorEstado extends JPanel implements TableCellRenderer {

    /**
     * Permite que una pantalla puntual decida su propio color en
     * vez de depender de EtiquetaEstado.automatico(). Casi nunca
     * hace falta implementarlo — está aquí para el día que sí.
     */
    @FunctionalInterface
    public interface Colorizador {
        EtiquetaEstado colorear(String valor);
    }

    private final Colorizador colorizador;

    public RenderizadorEstado() {
        this(valor -> EtiquetaEstado.automatico(valor));
    }

    public RenderizadorEstado(Colorizador colorizador) {

        super(new FlowLayout(FlowLayout.CENTER, 0, 0));

        this.colorizador = colorizador;

        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable tabla,
            Object valor,
            boolean seleccionado,
            boolean conFoco,
            int fila,
            int columna) {

        removeAll();

        setBackground(
                seleccionado
                        ? tabla.getSelectionBackground()
                        : tabla.getBackground());

        String texto = valor == null ? "" : valor.toString();

        if (texto.isEmpty()) {

            JLabel vacio = new JLabel("-");
            vacio.setHorizontalAlignment(SwingConstants.CENTER);
            add(vacio);

            return this;
        }

        add(colorizador.colorear(texto));

        return this;
    }

}
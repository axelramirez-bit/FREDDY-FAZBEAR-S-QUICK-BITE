package View.Componentes;

import View.Utils.FabricaEtiquetas;
import View.Utils.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Tarjeta KPI reutilizable: un icono, una etiqueta, un valor
 * grande y un detalle pequeño (ej. "+3 hoy", "↑ 12%").
 *
 * Es UNA sola clase para las tarjetas de Trabajador (Pendientes,
 * Preparación, Completados, Ventas) y Administrador (Ventas,
 * Pedidos, Clientes, Productos, Trabajadores, Alertas) — no una
 * clase por tarjeta. Cada dashboard solo cambia los datos que le
 * pasa, igual que TarjetaProducto no tiene una versión distinta
 * por categoría.
 *
 * Uso típico dentro de registrarPaneles() o de un panel de
 * "Inicio":
 *
 *     TarjetaKPI pendientes = new TarjetaKPI(
 *             FabricaIconos.pedidosPendientes(),
 *             "Pendientes",
 *             "12",
 *             "+3 hoy"
 *     );
 *
 * Cuando lleguen datos reales del Service correspondiente, se
 * actualiza sin recrear el componente:
 *
 *     pendientes.actualizar("15", "+6 hoy");
 * ===============================================================
 */
public class TarjetaKPI extends PanelRedondeado {

    private final JLabel lblEtiqueta;

    private final JLabel lblValor;

    private final JLabel lblDetalle;

    public TarjetaKPI(
            ImageIcon icono,
            String etiqueta,
            String valorInicial,
            String detalleInicial) {

        super();

        setLayout(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));

        setBorder(BorderFactory.createEmptyBorder(
                UIConstants.ESPACIO_TITULO,
                UIConstants.ESPACIO_TITULO,
                UIConstants.ESPACIO_TITULO,
                UIConstants.ESPACIO_TITULO
        ));

        // ---- fila superior: icono + etiqueta ----
        JPanel filaSuperior = new JPanel(
                new FlowLayout(FlowLayout.LEFT, UIConstants.ESPACIO_ICONO_PEQUEÑO, 0)
        );
        filaSuperior.setOpaque(false);

        JLabel lblIcono = new JLabel(icono);

        lblEtiqueta = FabricaEtiquetas.crearPequeño(etiqueta.toUpperCase());

        filaSuperior.add(lblIcono);
        filaSuperior.add(lblEtiqueta);

        // ---- valor grande ----
        lblValor = FabricaEtiquetas.crearTitulo(valorInicial);

        // ---- detalle pequeño ----
        lblDetalle = FabricaEtiquetas.crearPequeño(detalleInicial);

        add(filaSuperior, BorderLayout.NORTH);
        add(lblValor, BorderLayout.CENTER);
        add(lblDetalle, BorderLayout.SOUTH);
    }

    /**
     * Actualiza el valor y el detalle sin recrear la tarjeta.
     * Úsalo cuando el Service traiga datos nuevos (ej. al abrir
     * el dashboard o al refrescar).
     */
    public void actualizar(String valor, String detalle) {
        lblValor.setText(valor);
        lblDetalle.setText(detalle);
    }

    /**
     * Opcional: colorea el detalle (ej. verde para "↑ 12%", rojo
     * para "2 urgentes"). Si tu equipo termina usando esto mucho,
     * vale la pena agregar colorExito()/colorError() a
     * AdministradorTema en vez de pasar colores sueltos — pero no
     * lo crees todavía si solo lo vas a usar una o dos veces.
     */
    public void colorDetalle(Color color) {
        lblDetalle.setForeground(color);
    }

}
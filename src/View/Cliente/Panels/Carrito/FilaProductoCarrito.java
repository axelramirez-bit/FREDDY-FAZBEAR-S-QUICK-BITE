package View.Cliente.Panels.Carrito;

import Model.CarritoDetalle;
import View.Componentes.BotonRedondeado;
import View.Componentes.PanelRedondeado;
import View.Componentes.SelectorCantidad;
import View.Utils.AdministradorTema;
import View.Utils.FabricaEtiquetas;
import View.Utils.FormateadorMoneda;
import View.Utils.PaletaColores;
import View.Utils.UtilImagenes;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Una fila del listado "Productos en tu carrito" (Paso 1):
 * imagen, nombre, precio unitario, "Editar" / "Eliminar",
 * SelectorCantidad y subtotal de la línea.
 * ===============================================================
 */
public class FilaProductoCarrito extends PanelRedondeado {

    public interface FilaCarritoListener {

        void onCantidadCambiada(CarritoDetalle detalle, int nuevaCantidad);

        void onEliminar(CarritoDetalle detalle);
    }

    private static final int TAMANO_IMAGEN = 64;

    private final CarritoDetalle detalle;
    private FilaCarritoListener listener;

    private JLabel lblSubtotal;
    private SelectorCantidad selector;

    public FilaProductoCarrito(CarritoDetalle detalle) {

        super();

        this.detalle = detalle;

        setLayout(new BorderLayout(AdministradorTema.espacioMediano(), 0));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        add(construirImagen(), BorderLayout.WEST);
        add(construirCentro(), BorderLayout.CENTER);
        add(construirDerecha(), BorderLayout.EAST);
    }

    public void setListener(FilaCarritoListener listener) {
        this.listener = listener;
    }

    public CarritoDetalle getDetalle() {
        return detalle;
    }

    // ==========================================================
    // IMAGEN
    // ==========================================================
    private JLabel construirImagen() {

        ImageIcon icono = UtilImagenes.producto(
                detalle.getProducto().getImagenPrincipal(),
                TAMANO_IMAGEN,
                TAMANO_IMAGEN
        );

        JLabel lblImagen = new JLabel(icono);
        lblImagen.setPreferredSize(new Dimension(TAMANO_IMAGEN, TAMANO_IMAGEN));

        return lblImagen;
    }

    // ==========================================================
    // CENTRO: nombre + precio unitario + editar/eliminar
    // ==========================================================
    private JPanel construirCentro() {

        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        JLabel lblNombre = FabricaEtiquetas.crearSubtitulo(detalle.getProducto().getNombre());
        lblNombre.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblPrecio = FabricaEtiquetas.crearPequeño(
                FormateadorMoneda.formatear(detalle.getProducto().getPrecio()) + " c/u"
        );
        lblPrecio.setForeground(java.awt.Color.GRAY);
        lblPrecio.setAlignmentX(LEFT_ALIGNMENT);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        acciones.setOpaque(false);
        acciones.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lnkEditar = crearEnlace("Editar", AdministradorTema.colorTexto());
        lnkEditar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                editarObservaciones();
            }
        });

        JLabel lnkEliminar = crearEnlace("Eliminar", PaletaColores.PRINCIPAL);
        lnkEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (listener != null) {
                    listener.onEliminar(detalle);
                }
            }
        });

        acciones.add(lnkEditar);
        acciones.add(lnkEliminar);

        centro.add(lblNombre);
        centro.add(Box.createVerticalStrut(4));
        centro.add(lblPrecio);
        centro.add(Box.createVerticalStrut(4));
        centro.add(acciones);

        return centro;
    }

    private JLabel crearEnlace(String texto, java.awt.Color color) {

        JLabel enlace = new JLabel(texto);
        enlace.setFont(AdministradorTema.fuentePequeñaNegrita());
        enlace.setForeground(color);
        enlace.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        return enlace;
    }

    private void editarObservaciones() {

        String actual = detalle.getObservaciones() != null ? detalle.getObservaciones() : "";

        String nuevo = JOptionPane.showInputDialog(
                this,
                "Indicaciones para \"" + detalle.getProducto().getNombre() + "\" (ej. sin cebolla):",
                actual
        );

        if (nuevo != null) {
            detalle.setObservaciones(nuevo.isBlank() ? null : nuevo.trim());
        }
    }

    // ==========================================================
    // DERECHA: selector de cantidad + subtotal
    // ==========================================================
    private JPanel construirDerecha() {

        JPanel derecha = new JPanel();
        derecha.setOpaque(false);
        derecha.setLayout(new BoxLayout(derecha, BoxLayout.Y_AXIS));

        selector = new SelectorCantidad();
        selector.configurarLimites(1, 20);
        selector.setCantidad(detalle.getCantidad());
        selector.setAlignmentX(CENTER_ALIGNMENT);

        selector.setCantidadListener(cantidad -> {
            actualizarSubtotal(cantidad);
            if (listener != null) {
                listener.onCantidadCambiada(detalle, cantidad);
            }
        });

        lblSubtotal = FabricaEtiquetas.crearSubtitulo(FormateadorMoneda.formatear(detalle.getSubtotal()));
        lblSubtotal.setHorizontalAlignment(SwingConstants.CENTER);
        lblSubtotal.setAlignmentX(CENTER_ALIGNMENT);

        derecha.add(selector);
        derecha.add(Box.createVerticalStrut(8));
        derecha.add(lblSubtotal);

        return derecha;
    }

    private void actualizarSubtotal(int cantidad) {

        java.math.BigDecimal subtotal = detalle.getProducto().getPrecio()
                .multiply(java.math.BigDecimal.valueOf(cantidad));

        lblSubtotal.setText(FormateadorMoneda.formatear(subtotal));
    }
}

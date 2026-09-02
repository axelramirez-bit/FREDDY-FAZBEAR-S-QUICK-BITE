package View.Componentes;

import Model.DetallePedido;
import Model.EstadoPedido;
import Model.MetodoPago;
import Model.Pago;
import Model.Pedido;
import Model.TipoEntrega;
import View.Utils.AdministradorTema;
import View.Utils.FabricaDialogos;
import View.Utils.FabricaEtiquetas;
import View.Utils.FabricaTablas;
import View.Utils.FormateadorMoneda;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Window;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * "Ver detalle" de un pedido — caso de uso 2 del diagrama
 * (Trabajador/Cajero: "Consultar pedidos, listar y ver detalle").
 *
 * Es UN solo componente compartido por las 4 pantallas del
 * Trabajador que necesitan mostrar el detalle de un pedido
 * (Pendientes, En preparación, Listos, Historial), en vez de que
 * cada panel arme su propio JOptionPane de texto plano — mismo
 * criterio que ya usa ColumnaAccionTabla para los botones de
 * acción y EtiquetaEstado para los badges de estado.
 *
 * USO:
 *
 *     DialogoDetallePedido.mostrar(this, pedido, pago);
 * ===============================================================
 */
public final class DialogoDetallePedido {

    private static final DateTimeFormatter FORMATO_FECHA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");

    private DialogoDetallePedido() {
    }

    public static void mostrar(Component padre, Pedido pedido, Pago pago) {

        Window propietario = SwingUtilities.getWindowAncestor(padre);

        JPanel contenido = construirContenido(pedido, pago);

        JDialog dialogo = FabricaDialogos.crearDialogo(
                propietario,
                "Detalle del pedido #" + pedido.getIdPedido(),
                contenido
        );

        dialogo.setSize(560, 520);
        dialogo.setResizable(true);
        dialogo.setLocationRelativeTo(propietario);
        dialogo.setVisible(true);
    }

    // ==========================================================
    // CONTENIDO
    // ==========================================================
    private static JPanel construirContenido(Pedido pedido, Pago pago) {

        JPanel raiz = new JPanel(new BorderLayout(0, AdministradorTema.espacioMediano()));

        raiz.setBorder(BorderFactory.createEmptyBorder(
                AdministradorTema.espacioGrande(), AdministradorTema.espacioGrande(),
                AdministradorTema.espacioGrande(), AdministradorTema.espacioGrande()));

        raiz.add(crearEncabezado(pedido), BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(0, AdministradorTema.espacioMediano()));
        centro.setOpaque(false);
        centro.add(crearFichaInformacion(pedido, pago), BorderLayout.NORTH);
        centro.add(crearTablaProductos(pedido), BorderLayout.CENTER);

        raiz.add(centro, BorderLayout.CENTER);
        raiz.add(crearPieTotales(pedido), BorderLayout.SOUTH);

        return raiz;
    }

    // ---- Encabezado: número de pedido + badge de estado ----
    private static JPanel crearEncabezado(Pedido pedido) {

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);

        JLabel titulo = FabricaEtiquetas.crearSubtitulo(
                "Pedido #" + pedido.getIdPedido()
                        + (pedido.getNumeroOrden() != null ? " · " + pedido.getNumeroOrden() : ""));

        encabezado.add(titulo, BorderLayout.WEST);
        encabezado.add(EtiquetaEstado.automatico(nombreLegible(pedido.getEstado())), BorderLayout.EAST);

        return encabezado;
    }

    // ---- Ficha de datos: cliente, fecha, entrega, pago ----
    private static JPanel crearFichaInformacion(Pedido pedido, Pago pago) {

        boolean esDomicilio = pedido.getTipoEntrega() == TipoEntrega.DOMICILIO;

        JPanel ficha = new JPanel(new GridLayout(esDomicilio ? 4 : 3, 2, AdministradorTema.espacioMediano(), 4));
        ficha.setOpaque(false);
        ficha.setBorder(BorderFactory.createEmptyBorder(0, 0, AdministradorTema.espacioPequeño(), 0));

        agregarCampo(ficha, "Cliente",
                pedido.getUsuario() != null ? pedido.getUsuario().getNombreCompleto() : "-");

        agregarCampo(ficha, "Fecha",
                pedido.getFecha() != null ? pedido.getFecha().format(FORMATO_FECHA_HORA) : "-");

        agregarCampo(ficha, "Tipo de entrega", nombreLegible(pedido.getTipoEntrega()));

        agregarCampo(ficha, "Método de pago",
                pago != null ? nombreLegible(pago.getMetodoPago()) : "Sin registrar");

        if (esDomicilio) {
            String direccion = pedido.getDireccionEntrega() != null ? pedido.getDireccionEntrega() : "-";
            String referencia = pedido.getReferenciaEntrega();
            agregarCampo(ficha, "Dirección",
                    referencia != null && !referencia.isBlank() ? direccion + " (" + referencia + ")" : direccion);
        }

        return ficha;
    }

    private static void agregarCampo(JPanel ficha, String etiqueta, String valor) {

        JLabel lblEtiqueta = FabricaEtiquetas.crearPequeño(etiqueta + ":");
        JLabel lblValor = FabricaEtiquetas.crearTexto(valor == null || valor.isBlank() ? "-" : valor);

        JPanel campo = new JPanel(new BorderLayout());
        campo.setOpaque(false);
        campo.add(lblEtiqueta, BorderLayout.NORTH);
        campo.add(lblValor, BorderLayout.SOUTH);

        ficha.add(campo);
    }

    // ---- Tabla de productos del pedido ----
    private static JPanel crearTablaProductos(Pedido pedido) {

        DefaultTableModel modelo = FabricaTablas.crearModeloSoloLectura(
                new Object[]{"Producto", "Cantidad", "Precio", "Subtotal"});

        JTable tabla = FabricaTablas.crearTabla(modelo);

        List<DetallePedido> detalles = pedido.getDetalles();

        if (detalles.isEmpty()) {
            modelo.addRow(new Object[]{"Sin productos registrados para este pedido.", "-", "-", "-"});
        } else {
            for (DetallePedido detalle : detalles) {
                modelo.addRow(new Object[]{
                        detalle.getProducto() != null ? detalle.getProducto().getNombre() : "-",
                        detalle.getCantidad(),
                        FormateadorMoneda.formatear(detalle.getPrecio()),
                        FormateadorMoneda.formatear(detalle.getSubtotal())
                });
            }
        }

        tabla.getColumnModel().getColumn(1).setCellRenderer(centrado());
        tabla.getColumnModel().getColumn(2).setCellRenderer(centrado());
        tabla.getColumnModel().getColumn(3).setCellRenderer(centrado());

        JPanel contenedor = FabricaTablas.crearPanelTabla(tabla);
        contenedor.setBorder(BorderFactory.createEmptyBorder(
                0, 0, AdministradorTema.espacioPequeño(), 0));

        return contenedor;
    }

    private static javax.swing.table.TableCellRenderer centrado() {
        return (t, valor, seleccionado, foco, fila, columna) -> {
            JLabel etiqueta = new JLabel(valor == null ? "" : valor.toString(), SwingConstants.CENTER);
            etiqueta.setOpaque(true);
            etiqueta.setBackground(seleccionado ? t.getSelectionBackground() : t.getBackground());
            etiqueta.setForeground(seleccionado ? t.getSelectionForeground() : t.getForeground());
            etiqueta.setFont(t.getFont());
            return etiqueta;
        };
    }

    // ---- Pie: subtotal, descuento, envío, total ----
    private static JPanel crearPieTotales(Pedido pedido) {

        JPanel pie = new JPanel();
        pie.setOpaque(false);
        pie.setLayout(new javax.swing.BoxLayout(pie, javax.swing.BoxLayout.Y_AXIS));
        pie.setBorder(BorderFactory.createEmptyBorder(
                AdministradorTema.espacioPequeño(), 0, 0, 0));

        pie.add(crearFilaTotal("Subtotal", pedido.getSubtotal(), false));

        if (pedido.getDescuento() != null && pedido.getDescuento().compareTo(BigDecimal.ZERO) > 0) {
            pie.add(crearFilaTotal("Descuento", pedido.getDescuento().negate(), false));
        }

        if (pedido.getCostoEnvio() != null && pedido.getCostoEnvio().compareTo(BigDecimal.ZERO) > 0) {
            pie.add(crearFilaTotal("Costo de envío", pedido.getCostoEnvio(), false));
        }

        pie.add(crearFilaTotal("Total", pedido.getTotal(), true));

        return pie;
    }

    private static JPanel crearFilaTotal(String etiqueta, BigDecimal valor, boolean destacado) {

        JPanel fila = new JPanel(new BorderLayout());
        fila.setOpaque(false);

        JLabel lblEtiqueta = destacado
                ? FabricaEtiquetas.crearSubtitulo(etiqueta)
                : FabricaEtiquetas.crearTexto(etiqueta);

        JLabel lblValor = destacado
                ? FabricaEtiquetas.crearSubtitulo(FormateadorMoneda.formatear(valor))
                : FabricaEtiquetas.crearTexto(FormateadorMoneda.formatear(valor));

        lblValor.setHorizontalAlignment(SwingConstants.RIGHT);

        fila.add(lblEtiqueta, BorderLayout.WEST);
        fila.add(lblValor, BorderLayout.EAST);

        return fila;
    }

    // ==========================================================
    // UTILITARIOS
    // ==========================================================
    private static String nombreLegible(EstadoPedido estado) {
        if (estado == null) return "-";
        switch (estado) {
            case PENDIENTE: return "Pendiente";
            case PREPARACION: return "En preparación";
            case LISTO: return "Listo";
            case ENTREGADO: return "Entregado";
            case CANCELADO: return "Cancelado";
            default: return estado.name();
        }
    }

    private static String nombreLegible(TipoEntrega tipo) {
        if (tipo == null) return "-";
        switch (tipo) {
            case PARA_LLEVAR: return "Para llevar";
            case DOMICILIO: return "A domicilio";
            case COMER_EN_RESTAURANTE: return "Comer en local";
            default: return tipo.name();
        }
    }

    private static String nombreLegible(MetodoPago metodo) {
        if (metodo == null) return "-";
        switch (metodo) {
            case EFECTIVO: return "Efectivo";
            case TARJETA: return "Tarjeta";
            case TRANSFERENCIA: return "Transferencia";
            default: return metodo.name();
        }
    }
}

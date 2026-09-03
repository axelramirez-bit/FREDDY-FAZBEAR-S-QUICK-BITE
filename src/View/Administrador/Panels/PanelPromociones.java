package View.Administrador.Panels;

import Model.Promocion;
import Service.Implement.PromocionServiceImpl;
import Service.Interfaz.IPromocionService;
import View.Componentes.BarraBusqueda;
import View.Componentes.PanelFondo;
import View.Componentes.TarjetaKPI;
import View.Utils.FabricaBotones;
import View.Utils.FabricaCampos;
import View.Utils.FabricaEtiquetas;
import View.Utils.FabricaIconos;
import View.Utils.FabricaTablas;
import View.Utils.RenderizadorEstado;
import View.Utils.UIConstants;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Gestión de Promociones — interfaz 6 del boceto.
 *
 * "Activa / Programada / Vencida" NO son valores guardados en la
 * base de datos — promocion.estado es un simple boolean
 * (activo/inactivo). Los 3 estados que se ven en la tabla se
 * CALCULAN aquí comparando fecha_inicio/fecha_fin contra hoy, tal
 * como quedó acordado. Si algún día cambian de opinión y quieren
 * guardar el estado calculado en la base de datos, este es el
 * único método que hay que tocar: calcularEstadoVisual().
 *
 * Recordatorio de la restricción real del negocio: solo se
 * soportan descuentos porcentuales (0%-100%), por el
 * CHECK(descuento >= 0 AND descuento <= 100) de la tabla — no
 * existe un tipo "monto fijo".
 * ===============================================================
 */
public class PanelPromociones extends PanelFondo {

    private final IPromocionService promocionService = new PromocionServiceImpl();

    private static final int COLUMNA_ESTADO = 5;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private List<Promocion> todasLasPromociones = new ArrayList<>();
    private List<Promocion> promocionesFiltradas = new ArrayList<>();

    private TarjetaKPI tarjetaActivas;
    private TarjetaKPI tarjetaProximasAVencer;
    private TarjetaKPI tarjetaDescuentoPromedio;

    private BarraBusqueda barraBusqueda;

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    public PanelPromociones() {

        super();

        setOpaque(false);
        setLayout(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));

        JPanel norte = new JPanel(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));
        norte.setOpaque(false);
        norte.add(FabricaEtiquetas.crearTitulo("PROMOCIONES"), BorderLayout.NORTH);
        norte.add(crearFilaKPI(), BorderLayout.CENTER);
        norte.add(crearBarraAcciones(), BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);

        JPanel pie = new JPanel(new BorderLayout());
        pie.setOpaque(false);
        pie.add(FabricaEtiquetas.crearPequeño(
                "Estados: Activa (vigente), Programada (futura), Vencida (expirada). "
                        + "Solo se soportan promociones porcentuales (0%-100%)."
        ), BorderLayout.WEST);
        add(pie, BorderLayout.SOUTH);

        cargarDatos();
    }

    private JPanel crearFilaKPI() {

        JPanel fila = new JPanel(new GridLayout(1, 3, UIConstants.ESPACIO_SUBTITULO, 0));
        fila.setOpaque(false);
        fila.setBorder(javax.swing.BorderFactory.createEmptyBorder(UIConstants.ESPACIO_SUBTITULO, 0, 0, 0));

        tarjetaActivas = new TarjetaKPI(FabricaIconos.promociones(), "Promociones Activas", "0", "");
        tarjetaProximasAVencer = new TarjetaKPI(FabricaIconos.promociones(), "Próximas a vencer", "0", "7 días");
        tarjetaDescuentoPromedio = new TarjetaKPI(FabricaIconos.promociones(), "Descuento Promedio", "0%", "");

        fila.add(tarjetaActivas);
        fila.add(tarjetaProximasAVencer);
        fila.add(tarjetaDescuentoPromedio);

        return fila;
    }

    private JPanel crearBarraAcciones() {

        JPanel barra = new JPanel(new BorderLayout());
        barra.setOpaque(false);

        JPanel ladoIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, UIConstants.ESPACIO_SUBTITULO, 0));
        ladoIzquierdo.setOpaque(false);

        barraBusqueda = new BarraBusqueda("Buscar promoción...");
        barraBusqueda.getCampo().getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {
                    @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
                    @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
                    @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
                });

        ladoIzquierdo.add(barraBusqueda);

        JPanel ladoDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, UIConstants.ESPACIO_SUBTITULO, 0));
        ladoDerecho.setOpaque(false);

        JButton btnEditar = FabricaBotones.crearSecundario("Editar");
        JButton btnEliminar = FabricaBotones.crearSecundario("Eliminar");
        JButton btnNueva = FabricaBotones.crearPrimario("Nueva Promoción");

        btnEditar.addActionListener(e -> editarSeleccionada());
        btnEliminar.addActionListener(e -> eliminarSeleccionada());
        btnNueva.addActionListener(e -> abrirFormulario(null));

        ladoDerecho.add(btnEditar);
        ladoDerecho.add(btnEliminar);
        ladoDerecho.add(btnNueva);

        barra.add(ladoIzquierdo, BorderLayout.WEST);
        barra.add(ladoDerecho, BorderLayout.EAST);

        return barra;
    }

    private JPanel crearPanelTabla() {

        Object[] columnas = {"ID", "Promoción", "Descuento", "Inicio", "Fin", "Estado"};

        modeloTabla = FabricaTablas.crearModeloSoloLectura(columnas);
        tabla = FabricaTablas.crearTabla(modeloTabla);

        tabla.getColumnModel().getColumn(COLUMNA_ESTADO).setCellRenderer(new RenderizadorEstado());

        return FabricaTablas.crearPanelTabla(tabla);
    }

    public void cargarDatos() {

        todasLasPromociones = promocionService.listarPromociones();

        int activas = 0;
        int proximasAVencer = 0;
        BigDecimal sumaDescuentos = BigDecimal.ZERO;
        int conDescuento = 0;

        LocalDate hoy = LocalDate.now();

        for (Promocion promocion : todasLasPromociones) {

            String estadoVisual = calcularEstadoVisual(promocion);

            if (estadoVisual.equals("Activa")) {
                activas++;

                if (promocion.getFechaFin() != null
                        && !promocion.getFechaFin().isBefore(hoy)
                        && promocion.getFechaFin().isBefore(hoy.plusDays(7))) {
                    proximasAVencer++;
                }
            }

            if (promocion.getDescuento() != null) {
                sumaDescuentos = sumaDescuentos.add(promocion.getDescuento());
                conDescuento++;
            }
        }

        tarjetaActivas.actualizar(String.valueOf(activas), "");
        tarjetaProximasAVencer.actualizar(String.valueOf(proximasAVencer), "7 días");

        String promedioTexto = conDescuento == 0
                ? "0%"
                : sumaDescuentos.divide(BigDecimal.valueOf(conDescuento), 0, java.math.RoundingMode.HALF_UP) + "%";

        tarjetaDescuentoPromedio.actualizar(promedioTexto, "");

        aplicarFiltros();
    }

    /**
     * Calcula el estado visual (Activa/Programada/Vencida/Inactiva)
     * a partir de fechas + el boolean real de la base de datos.
     * No se guarda en ningún lado, se recalcula cada vez.
     */
    private String calcularEstadoVisual(Promocion promocion) {

        if (!promocion.isEstado()) {
            return "Inactiva";
        }

        LocalDate hoy = LocalDate.now();

        if (promocion.getFechaInicio() != null && promocion.getFechaInicio().isAfter(hoy)) {
            return "Programada";
        }

        if (promocion.getFechaFin() != null && promocion.getFechaFin().isBefore(hoy)) {
            return "Vencida";
        }

        return "Activa";
    }

    private void aplicarFiltros() {

        String texto = barraBusqueda == null ? "" : barraBusqueda.getTexto().toLowerCase();

        promocionesFiltradas = new ArrayList<>();

        for (Promocion promocion : todasLasPromociones) {
            if (texto.isEmpty() || promocion.getNombre().toLowerCase().contains(texto)) {
                promocionesFiltradas.add(promocion);
            }
        }

        repintarTabla();
    }

    private void repintarTabla() {

        modeloTabla.setRowCount(0);

        for (Promocion promocion : promocionesFiltradas) {

            modeloTabla.addRow(new Object[]{
                    promocion.getIdPromocion(),
                    promocion.getNombre(),
                    (promocion.getDescuento() == null ? "-" : promocion.getDescuento() + "%"),
                    (promocion.getFechaInicio() == null ? "-" : promocion.getFechaInicio().format(FORMATO_FECHA)),
                    (promocion.getFechaFin() == null ? "-" : promocion.getFechaFin().format(FORMATO_FECHA)),
                    calcularEstadoVisual(promocion)
            });
        }
    }

    private Promocion obtenerSeleccionada() {

        int fila = tabla.getSelectedRow();

        if (fila < 0 || fila >= promocionesFiltradas.size()) {
            JOptionPane.showMessageDialog(this, "Selecciona una promoción de la tabla.",
                    "Ninguna promoción seleccionada", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return promocionesFiltradas.get(fila);
    }

    private void editarSeleccionada() {
        Promocion promocion = obtenerSeleccionada();
        if (promocion != null) {
            abrirFormulario(promocion);
        }
    }

    private void eliminarSeleccionada() {

        Promocion promocion = obtenerSeleccionada();

        if (promocion == null) {
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar \"" + promocion.getNombre() + "\"?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        if (!promocionService.eliminarPromocion(promocion.getIdPromocion())) {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar la promoción.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        cargarDatos();
    }

    private void abrirFormulario(Promocion promocionExistente) {

        boolean esEdicion = promocionExistente != null;

        javax.swing.JTextField campoNombre = FabricaCampos.crearCampo();
        javax.swing.JTextField campoDescripcion = FabricaCampos.crearCampo();
        javax.swing.JTextField campoDescuento = FabricaCampos.crearCampo();
        javax.swing.JTextField campoFechaInicio = FabricaCampos.crearCampo();
        javax.swing.JTextField campoFechaFin = FabricaCampos.crearCampo();

        if (esEdicion) {
            campoNombre.setText(promocionExistente.getNombre());
            campoDescripcion.setText(promocionExistente.getDescripcion());
            campoDescuento.setText(
                    promocionExistente.getDescuento() == null ? "" : promocionExistente.getDescuento().toString());
            campoFechaInicio.setText(
                    promocionExistente.getFechaInicio() == null ? "" : promocionExistente.getFechaInicio().format(FORMATO_FECHA));
            campoFechaFin.setText(
                    promocionExistente.getFechaFin() == null ? "" : promocionExistente.getFechaFin().format(FORMATO_FECHA));
        }

        JPanel formulario = new JPanel(new GridLayout(0, 2, UIConstants.ESPACIO_SUBTITULO, UIConstants.ESPACIO_SUBTITULO));
        formulario.add(new JLabel("Nombre:"));
        formulario.add(campoNombre);
        formulario.add(new JLabel("Descripción:"));
        formulario.add(campoDescripcion);
        formulario.add(new JLabel("Descuento (0-100%):"));
        formulario.add(campoDescuento);
        formulario.add(new JLabel("Fecha inicio (dd/MM/yyyy):"));
        formulario.add(campoFechaInicio);
        formulario.add(new JLabel("Fecha fin (dd/MM/yyyy):"));
        formulario.add(campoFechaFin);

        int resultado = JOptionPane.showConfirmDialog(
                this,
                formulario,
                esEdicion ? "Editar Promoción" : "Nueva Promoción",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resultado != JOptionPane.OK_OPTION) {
            return;
        }

        if (campoNombre.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal descuento;
        LocalDate fechaInicio;
        LocalDate fechaFin;

        try {
            descuento = new BigDecimal(campoDescuento.getText().trim());

            if (descuento.compareTo(BigDecimal.ZERO) < 0 || descuento.compareTo(BigDecimal.valueOf(100)) > 0) {
                throw new NumberFormatException("fuera de rango");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El descuento debe ser un número entre 0 y 100.",
                    "Descuento inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            fechaInicio = LocalDate.parse(campoFechaInicio.getText().trim(), FORMATO_FECHA);
            fechaFin = LocalDate.parse(campoFechaFin.getText().trim(), FORMATO_FECHA);

            if (fechaInicio.isAfter(fechaFin)) {
                throw new java.time.format.DateTimeParseException("inicio > fin", campoFechaFin.getText(), 0);
            }
        } catch (java.time.format.DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Revisa las fechas (formato dd/MM/yyyy, inicio no puede ser después de fin).",
                    "Fechas inválidas", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Promocion promocion = esEdicion ? promocionExistente : new Promocion();

        promocion.setNombre(campoNombre.getText().trim());
        promocion.setDescripcion(campoDescripcion.getText().trim());
        promocion.setDescuento(descuento);
        promocion.setFechaInicio(fechaInicio);
        promocion.setFechaFin(fechaFin);

        if (!esEdicion) {
            promocion.setEstado(true);
        }

        String motivoInvalido = promocionService.validar(promocion);
        if (motivoInvalido != null) {
            JOptionPane.showMessageDialog(this, motivoInvalido,
                    "No se pudo guardar la promoción", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean exito = esEdicion
                ? promocionService.actualizarPromocion(promocion)
                : promocionService.registrarPromocion(promocion);

        if (!exito) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar la promoción. Verifica tu conexión e inténtalo de nuevo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        cargarDatos();
    }

}
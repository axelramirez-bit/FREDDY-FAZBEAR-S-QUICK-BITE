package View.Administrador.Panels;

import Model.Usuario;
import Service.Implement.UsuarioServiceImpl;
import Service.Interfaz.IUsuarioService;
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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Gestión de Trabajadores — interfaz 3 del boceto. Es un
 * subconjunto de usuario (rol = "Trabajador") con una columna
 * extra que Usuarios no necesita: turno.
 *
 * BLOQUEADOR CONOCIDO: "Pedidos Atendidos Hoy" NO se puede
 * calcular todavía — pedido.id_usuario no distingue si guarda al
 * cliente o al trabajador que atendió (mismo problema del
 * Dashboard/Pedidos). Se muestra "—" en vez de inventar un número.
 * ===============================================================
 */
public class PanelTrabajadores extends PanelFondo {

    private final IUsuarioService usuarioService = new UsuarioServiceImpl();

    private static final int COLUMNA_ESTADO = 5;

    private List<Usuario> todosLosTrabajadores = new ArrayList<>();
    private List<Usuario> trabajadoresFiltrados = new ArrayList<>();

    private TarjetaKPI tarjetaActivos;
    private TarjetaKPI tarjetaTurnoActual;
    private TarjetaKPI tarjetaPedidosAtendidos;

    private BarraBusqueda barraBusqueda;
    private JComboBox<String> comboTurno;

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    public PanelTrabajadores() {

        super();

        setOpaque(false);
        setLayout(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));

        JPanel norte = new JPanel(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));
        norte.setOpaque(false);
        norte.add(FabricaEtiquetas.crearTitulo("TRABAJADORES"), BorderLayout.NORTH);
        norte.add(crearFilaKPI(), BorderLayout.CENTER);
        norte.add(crearBarraAcciones(), BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);

        JPanel pie = new JPanel(new BorderLayout());
        pie.setOpaque(false);
        pie.add(FabricaEtiquetas.crearPequeño(
                "Nota: los pedidos atendidos por trabajador aún no están diferenciados en la base de datos."
        ), BorderLayout.WEST);
        add(pie, BorderLayout.SOUTH);

        cargarDatos();
    }

    private JPanel crearFilaKPI() {

        JPanel fila = new JPanel(new GridLayout(1, 3, UIConstants.ESPACIO_SUBTITULO, 0));
        fila.setOpaque(false);
        fila.setBorder(javax.swing.BorderFactory.createEmptyBorder(UIConstants.ESPACIO_SUBTITULO, 0, 0, 0));

        tarjetaActivos = new TarjetaKPI(FabricaIconos.trabajadores(), "Trabajadores Activos", "0", "");
        tarjetaTurnoActual = new TarjetaKPI(FabricaIconos.trabajadores(), "Turno Actual", "-", "En servicio");
        tarjetaPedidosAtendidos = new TarjetaKPI(FabricaIconos.pedidos(), "Pedidos Atendidos Hoy", "—", "No disponible aún");

        fila.add(tarjetaActivos);
        fila.add(tarjetaTurnoActual);
        fila.add(tarjetaPedidosAtendidos);

        return fila;
    }

    private JPanel crearBarraAcciones() {

        JPanel barra = new JPanel(new BorderLayout());
        barra.setOpaque(false);

        JPanel ladoIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, UIConstants.ESPACIO_SUBTITULO, 0));
        ladoIzquierdo.setOpaque(false);

        barraBusqueda = new BarraBusqueda("Buscar trabajador...");
        barraBusqueda.getCampo().getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {
                    @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
                    @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
                    @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
                });

        comboTurno = FabricaCampos.crearCombo();
        comboTurno.addItem("Turno: Todos");
        comboTurno.addItem("Mañana");
        comboTurno.addItem("Tarde");
        comboTurno.addItem("Noche");
        comboTurno.addActionListener(e -> aplicarFiltros());

        ladoIzquierdo.add(barraBusqueda);
        ladoIzquierdo.add(comboTurno);

        JPanel ladoDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, UIConstants.ESPACIO_SUBTITULO, 0));
        ladoDerecho.setOpaque(false);

        JButton btnEditar = FabricaBotones.crearSecundario("Editar");
        JButton btnEliminar = FabricaBotones.crearSecundario("Eliminar");
        JButton btnNuevo = FabricaBotones.crearPrimario("Nuevo Trabajador");

        btnEditar.addActionListener(e -> editarSeleccionado());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        btnNuevo.addActionListener(e -> abrirFormulario(null));

        ladoDerecho.add(btnEditar);
        ladoDerecho.add(btnEliminar);
        ladoDerecho.add(btnNuevo);

        barra.add(ladoIzquierdo, BorderLayout.WEST);
        barra.add(ladoDerecho, BorderLayout.EAST);

        return barra;
    }

    private JPanel crearPanelTabla() {

        Object[] columnas = {"ID", "Nombre", "Turno", "Correo", "Rol", "Estado"};

        modeloTabla = FabricaTablas.crearModeloSoloLectura(columnas);
        tabla = FabricaTablas.crearTabla(modeloTabla);

        tabla.getColumnModel().getColumn(COLUMNA_ESTADO).setCellRenderer(new RenderizadorEstado());

        return FabricaTablas.crearPanelTabla(tabla);
    }

    public void cargarDatos() {

        todosLosTrabajadores = new ArrayList<>();

        for (Usuario usuario : usuarioService.listarUsuarios()) {
            if (usuario.esTrabajador()) {
                todosLosTrabajadores.add(usuario);
            }
        }

        int activos = 0;
        for (Usuario trabajador : todosLosTrabajadores) {
            if (trabajador.isEstado()) {
                activos++;
            }
        }

        tarjetaActivos.actualizar(String.valueOf(activos), "");

        // "Turno actual" = cuántos trabajadores activos tienen asignado
        // el turno que corresponde a la hora del día ahora mismo.
        String turnoDeHoy = turnoSegunHoraActual();
        int enEsteTurno = 0;
        for (Usuario trabajador : todosLosTrabajadores) {
            if (trabajador.isEstado() && turnoDeHoy.equalsIgnoreCase(trabajador.getTurno())) {
                enEsteTurno++;
            }
        }
        tarjetaTurnoActual.actualizar(String.valueOf(enEsteTurno), turnoDeHoy + " · en servicio");

        aplicarFiltros();
    }

    private String turnoSegunHoraActual() {

        int hora = java.time.LocalTime.now().getHour();

        if (hora >= 6 && hora < 13) {
            return "Mañana";
        }

        if (hora >= 13 && hora < 20) {
            return "Tarde";
        }

        return "Noche";
    }

    private void aplicarFiltros() {

        String texto = barraBusqueda == null ? "" : barraBusqueda.getTexto().toLowerCase();

        String turnoSeleccionado =
                (comboTurno == null || comboTurno.getSelectedIndex() <= 0)
                        ? null
                        : (String) comboTurno.getSelectedItem();

        trabajadoresFiltrados = new ArrayList<>();

        for (Usuario trabajador : todosLosTrabajadores) {

            boolean pasaBusqueda = texto.isEmpty()
                    || trabajador.getNombreCompleto().toLowerCase().contains(texto)
                    || trabajador.getCorreo().toLowerCase().contains(texto);

            boolean pasaTurno = turnoSeleccionado == null
                    || turnoSeleccionado.equalsIgnoreCase(trabajador.getTurno());

            if (pasaBusqueda && pasaTurno) {
                trabajadoresFiltrados.add(trabajador);
            }
        }

        repintarTabla();
    }

    private void repintarTabla() {

        modeloTabla.setRowCount(0);

        for (Usuario trabajador : trabajadoresFiltrados) {

            modeloTabla.addRow(new Object[]{
                    trabajador.getIdUsuario(),
                    trabajador.getNombreCompleto(),
                    trabajador.getTurno() == null ? "Sin asignar" : trabajador.getTurno(),
                    trabajador.getCorreo(),
                    trabajador.getRol().getNombre(),
                    trabajador.isEstado() ? "Activo" : "Inactivo"
            });
        }
    }

    private Usuario obtenerSeleccionado() {

        int fila = tabla.getSelectedRow();

        if (fila < 0 || fila >= trabajadoresFiltrados.size()) {
            JOptionPane.showMessageDialog(this, "Selecciona un trabajador de la tabla.",
                    "Ningún trabajador seleccionado", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return trabajadoresFiltrados.get(fila);
    }

    private void editarSeleccionado() {
        Usuario trabajador = obtenerSeleccionado();
        if (trabajador != null) {
            abrirFormulario(trabajador);
        }
    }

    private void eliminarSeleccionado() {

        Usuario trabajador = obtenerSeleccionado();

        if (trabajador == null) {
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar a " + trabajador.getNombreCompleto() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        if (!usuarioService.eliminarUsuario(trabajador.getIdUsuario())) {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar el trabajador.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        cargarDatos();
    }

    private void abrirFormulario(Usuario trabajadorExistente) {

        boolean esEdicion = trabajadorExistente != null;

        javax.swing.JTextField campoNombre = FabricaCampos.crearCampo();
        javax.swing.JTextField campoApellido = FabricaCampos.crearCampo();
        javax.swing.JTextField campoCorreo = FabricaCampos.crearCampo();
        javax.swing.JTextField campoTelefono = FabricaCampos.crearCampo();
        javax.swing.JPasswordField campoPassword = FabricaCampos.crearPassword();

        JComboBox<String> comboTurnoForm = FabricaCampos.crearCombo();
        comboTurnoForm.addItem("Mañana");
        comboTurnoForm.addItem("Tarde");
        comboTurnoForm.addItem("Noche");

        if (esEdicion) {
            campoNombre.setText(trabajadorExistente.getNombre());
            campoApellido.setText(trabajadorExistente.getApellido());
            campoCorreo.setText(trabajadorExistente.getCorreo());
            campoTelefono.setText(trabajadorExistente.getTelefono());
            if (trabajadorExistente.getTurno() != null) {
                comboTurnoForm.setSelectedItem(trabajadorExistente.getTurno());
            }
        }

        JPanel formulario = new JPanel(new GridLayout(0, 2, UIConstants.ESPACIO_SUBTITULO, UIConstants.ESPACIO_SUBTITULO));
        formulario.add(new JLabel("Nombre:"));
        formulario.add(campoNombre);
        formulario.add(new JLabel("Apellido:"));
        formulario.add(campoApellido);
        formulario.add(new JLabel("Correo:"));
        formulario.add(campoCorreo);
        formulario.add(new JLabel("Teléfono:"));
        formulario.add(campoTelefono);
        formulario.add(new JLabel("Turno:"));
        formulario.add(comboTurnoForm);
        formulario.add(new JLabel(esEdicion ? "Nueva contraseña (opcional):" : "Contraseña:"));
        formulario.add(campoPassword);

        int resultado = JOptionPane.showConfirmDialog(
                this,
                formulario,
                esEdicion ? "Editar Trabajador" : "Nuevo Trabajador",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resultado != JOptionPane.OK_OPTION) {
            return;
        }

        if (campoNombre.getText().isBlank() || campoCorreo.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Nombre y correo son obligatorios.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Model.Rol rolTrabajador = new Model.Rol();
        rolTrabajador.setIdRol(2); // ver nota de mapeo fijo en PanelUsuarios.abrirFormulario()
        rolTrabajador.setNombre("Trabajador");

        Usuario trabajador = esEdicion ? trabajadorExistente : new Usuario();

        trabajador.setNombre(campoNombre.getText().trim());
        trabajador.setApellido(campoApellido.getText().trim());
        trabajador.setCorreo(campoCorreo.getText().trim());
        trabajador.setTelefono(campoTelefono.getText().trim());
        trabajador.setTurno((String) comboTurnoForm.getSelectedItem());
        trabajador.setRol(rolTrabajador);

        if (!esEdicion) {
            trabajador.setEstado(true);
            trabajador.setFechaNacimiento(LocalDate.now().minusYears(18));
        }

        String nuevaClave = new String(campoPassword.getPassword());
        if (!nuevaClave.isBlank()) {
            trabajador.setPassword(
                    esEdicion
                            ? Utils.Encriptador.hashPassword(nuevaClave) // actualizarUsuario() no encripta, ver nota en PanelUsuarios
                            : nuevaClave // registrarUsuario() sí encripta
            );
        }

        boolean exito = esEdicion
                ? usuarioService.actualizarUsuario(trabajador)
                : usuarioService.registrarUsuario(trabajador);

        if (!exito) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el trabajador.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        cargarDatos();
    }

}
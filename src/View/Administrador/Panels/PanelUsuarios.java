package View.Administrador.Panels;

import Base.Rol;
import Model.Usuario;
import Service.Implement.UsuarioServiceImpl;
import Service.Interfaz.IUsuarioService;
import View.Componentes.BarraBusqueda;
import View.Componentes.PanelFondo;
import View.Componentes.TarjetaKPI;
import View.Utils.AdministradorTema;
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
 * Gestión de Usuarios — interfaz 2 del boceto. Muestra los 3
 * roles mezclados (Cliente/Trabajador/Administrador), a
 * diferencia de PanelTrabajadores que es la vista especializada
 * solo para Trabajador (con turno).
 *
 * Igual que PanelDashboard: sin PanelCrudBase, construido a mano
 * siguiendo el mismo patrón visual (KPI + búsqueda/filtro + tabla
 * con badges + paginación básica).
 * ===============================================================
 */
public class PanelUsuarios extends PanelFondo {

    private final IUsuarioService usuarioService = new UsuarioServiceImpl();

    private static final int COLUMNA_ESTADO = 4;

    private List<Usuario> todosLosUsuarios = new ArrayList<>();
    private List<Usuario> usuariosFiltrados = new ArrayList<>();

    private TarjetaKPI tarjetaActivos;
    private TarjetaKPI tarjetaInactivos;
    private TarjetaKPI tarjetaTotal;

    private BarraBusqueda barraBusqueda;
    private JComboBox<String> comboRol;

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    public PanelUsuarios() {

        super();

        setOpaque(false);
        setLayout(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));

        JPanel norte = new JPanel(new BorderLayout(0, UIConstants.ESPACIO_SUBTITULO));
        norte.setOpaque(false);

        JLabel titulo = FabricaEtiquetas.crearTitulo("USUARIOS");
        norte.add(titulo, BorderLayout.NORTH);
        norte.add(crearFilaKPI(), BorderLayout.CENTER);
        norte.add(crearBarraAcciones(), BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);

        cargarDatos();
    }

    // ==========================================================
    // KPI
    // ==========================================================

    private JPanel crearFilaKPI() {

        JPanel fila = new JPanel(new GridLayout(1, 3, UIConstants.ESPACIO_SUBTITULO, 0));
        fila.setOpaque(false);
        fila.setBorder(javax.swing.BorderFactory.createEmptyBorder(UIConstants.ESPACIO_SUBTITULO, 0, 0, 0));

        tarjetaActivos = new TarjetaKPI(FabricaIconos.usuarios(), "Usuarios Activos", "0", "");
        tarjetaInactivos = new TarjetaKPI(FabricaIconos.usuarios(), "Usuarios Inactivos", "0", "");
        tarjetaTotal = new TarjetaKPI(FabricaIconos.usuarios(), "Total Usuarios", "0", "");

        fila.add(tarjetaActivos);
        fila.add(tarjetaInactivos);
        fila.add(tarjetaTotal);

        return fila;
    }

    // ==========================================================
    // BARRA DE BÚSQUEDA + FILTRO + ACCIONES
    // ==========================================================

    private JPanel crearBarraAcciones() {

        JPanel barra = new JPanel(new BorderLayout());
        barra.setOpaque(false);

        JPanel ladoIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, UIConstants.ESPACIO_SUBTITULO, 0));
        ladoIzquierdo.setOpaque(false);

        barraBusqueda = new BarraBusqueda("Buscar por correo o nombre...");
        barraBusqueda.getCampo().getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {
                    @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
                    @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
                    @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
                });

        comboRol = FabricaCampos.crearCombo();
        comboRol.addItem("Todos los roles");
        comboRol.addItem("Cliente");
        comboRol.addItem("Trabajador");
        comboRol.addItem("Administrador");
        comboRol.addActionListener(e -> aplicarFiltros());

        ladoIzquierdo.add(barraBusqueda);
        ladoIzquierdo.add(comboRol);

        JPanel ladoDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, UIConstants.ESPACIO_SUBTITULO, 0));
        ladoDerecho.setOpaque(false);

        JButton btnEditar = FabricaBotones.crearSecundario("Editar");
        // Caso de uso 2.4: "Desactivar" siempre es posible (a
        // diferencia de "Eliminar", que la BD puede bloquear).
        // El texto cambia según el estado del usuario seleccionado.
        JButton btnEstado = FabricaBotones.crearSecundario("Desactivar");
        JButton btnEliminar = FabricaBotones.crearSecundario("Eliminar");
        JButton btnNuevo = FabricaBotones.crearPrimario("Nuevo Usuario");

        btnEditar.addActionListener(e -> editarSeleccionado());
        btnEstado.addActionListener(e -> cambiarEstadoSeleccionado());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        btnNuevo.addActionListener(e -> abrirFormulario(null));

        ladoDerecho.add(btnEditar);
        ladoDerecho.add(btnEstado);
        ladoDerecho.add(btnEliminar);
        ladoDerecho.add(btnNuevo);

        barra.add(ladoIzquierdo, BorderLayout.WEST);
        barra.add(ladoDerecho, BorderLayout.EAST);

        return barra;
    }

    // ==========================================================
    // TABLA
    // ==========================================================

    private JPanel crearPanelTabla() {

        Object[] columnas = {"ID", "Correo", "Nombre", "Rol", "Estado"};

        modeloTabla = FabricaTablas.crearModeloSoloLectura(columnas);
        tabla = FabricaTablas.crearTabla(modeloTabla);

        tabla.getColumnModel().getColumn(COLUMNA_ESTADO).setCellRenderer(new RenderizadorEstado());

        return FabricaTablas.crearPanelTabla(tabla);
    }

    // ==========================================================
    // DATOS
    // ==========================================================

    public void cargarDatos() {

        todosLosUsuarios = usuarioService.listarUsuarios();

        int activos = 0;
        int inactivos = 0;

        for (Usuario usuario : todosLosUsuarios) {
            if (usuario.isEstado()) {
                activos++;
            } else {
                inactivos++;
            }
        }

        tarjetaActivos.actualizar(String.valueOf(activos), "");
        tarjetaInactivos.actualizar(String.valueOf(inactivos), "");
        tarjetaTotal.actualizar(String.valueOf(todosLosUsuarios.size()), "");

        aplicarFiltros();
    }

    private void aplicarFiltros() {

        String texto = barraBusqueda == null ? "" : barraBusqueda.getTexto().toLowerCase();

        String rolSeleccionado =
                (comboRol == null || comboRol.getSelectedIndex() <= 0)
                        ? null
                        : (String) comboRol.getSelectedItem();

        usuariosFiltrados = new ArrayList<>();

        for (Usuario usuario : todosLosUsuarios) {

            boolean pasaBusqueda = texto.isEmpty()
                    || usuario.getCorreo().toLowerCase().contains(texto)
                    || usuario.getNombreCompleto().toLowerCase().contains(texto);

            boolean pasaRol = rolSeleccionado == null
                    || (usuario.getRol() != null
                        && rolSeleccionado.equalsIgnoreCase(usuario.getRol().getNombre()));

            if (pasaBusqueda && pasaRol) {
                usuariosFiltrados.add(usuario);
            }
        }

        repintarTabla();
    }

    private void repintarTabla() {

        modeloTabla.setRowCount(0);

        for (Usuario usuario : usuariosFiltrados) {

            modeloTabla.addRow(new Object[]{
                    usuario.getIdUsuario(),
                    usuario.getCorreo(),
                    usuario.getNombreCompleto(),
                    usuario.getRol() == null ? "-" : usuario.getRol().getNombre(),
                    usuario.isEstado() ? "Activo" : "Inactivo"
            });
        }
    }

    // ==========================================================
    // EDITAR / ELIMINAR
    // ==========================================================

    private Usuario obtenerSeleccionado() {

        int fila = tabla.getSelectedRow();

        if (fila < 0 || fila >= usuariosFiltrados.size()) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario de la tabla.",
                    "Ningún usuario seleccionado", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return usuariosFiltrados.get(fila);
    }

    private void editarSeleccionado() {

        Usuario usuario = obtenerSeleccionado();

        if (usuario != null) {
            abrirFormulario(usuario);
        }
    }

    private void eliminarSeleccionado() {

        Usuario usuario = obtenerSeleccionado();

        if (usuario == null) {
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar a " + usuario.getNombreCompleto() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean eliminado = usuarioService.eliminarUsuario(usuario.getIdUsuario());

            if (!eliminado) {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el usuario.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

        } catch (IllegalStateException ex) {
            // El usuario ya procesó pedidos: la BD bloqueó el
            // DELETE por la FK pedido.id_usuario. Se informa la
            // causa real, en vez de un error genérico.
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "No se puede eliminar", JOptionPane.WARNING_MESSAGE);
            return;
        }

        cargarDatos();
    }

    /**
     * Caso de uso 2.4 "Desactivar usuario": alterna estado=true/false
     * según el estado actual del usuario seleccionado. Siempre es
     * posible porque solo actualiza el flag, sin tocar la FK.
     */
    private void cambiarEstadoSeleccionado() {

        Usuario usuario = obtenerSeleccionado();

        if (usuario == null) {
            return;
        }

        boolean nuevoEstado = !usuario.isEstado();
        String accion = nuevoEstado ? "activar" : "desactivar";

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas " + accion + " a " + usuario.getNombreCompleto() + "?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        boolean actualizado = nuevoEstado
                ? usuarioService.activarUsuario(usuario.getIdUsuario())
                : usuarioService.desactivarUsuario(usuario.getIdUsuario());

        if (!actualizado) {
            JOptionPane.showMessageDialog(this, "No se pudo " + accion + " el usuario.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        cargarDatos();
    }

    /**
     * Formulario simple de alta/edición. Es un JOptionPane con
     * varios campos, no una ventana propia — suficiente para el
     * alcance académico del proyecto. Si más adelante quieren un
     * JDialog con mejor diseño, esta es la única función que hay
     * que reemplazar.
     */
    private void abrirFormulario(Usuario usuarioExistente) {

        boolean esEdicion = usuarioExistente != null;

        javax.swing.JTextField campoNombre = FabricaCampos.crearCampo();
        javax.swing.JTextField campoApellido = FabricaCampos.crearCampo();
        javax.swing.JTextField campoCorreo = FabricaCampos.crearCampo();
        javax.swing.JTextField campoTelefono = FabricaCampos.crearCampo();
        javax.swing.JPasswordField campoPassword = FabricaCampos.crearPassword();

        JComboBox<String> comboRolForm = FabricaCampos.crearCombo();
        comboRolForm.addItem("Cliente");
        comboRolForm.addItem("Trabajador");
        comboRolForm.addItem("Administrador");

        if (esEdicion) {
            campoNombre.setText(usuarioExistente.getNombre());
            campoApellido.setText(usuarioExistente.getApellido());
            campoCorreo.setText(usuarioExistente.getCorreo());
            campoTelefono.setText(usuarioExistente.getTelefono());
            if (usuarioExistente.getRol() != null) {
                comboRolForm.setSelectedItem(usuarioExistente.getRol().getNombre());
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
        formulario.add(new JLabel("Rol:"));
        formulario.add(comboRolForm);
        formulario.add(new JLabel(esEdicion ? "Nueva contraseña (opcional):" : "Contraseña:"));
        formulario.add(campoPassword);

        int resultado = JOptionPane.showConfirmDialog(
                this,
                formulario,
                esEdicion ? "Editar Usuario" : "Nuevo Usuario",
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

        Model.Rol rolElegido = new Model.Rol();
        // El id real del rol lo debería resolver un IRolService contra la
        // tabla "rol" — como todavía no existe ese Service, se asume el
        // mapeo fijo de la siembra inicial (1=Administrador, 2=Trabajador,
        // 3=Cliente). Si cambian esos IDs en la base de datos, hay que
        // actualizar este mapeo.
        String rolTexto = (String) comboRolForm.getSelectedItem();
        switch (rolTexto) {
            case "Administrador" -> rolElegido.setIdRol(1);
            case "Trabajador" -> rolElegido.setIdRol(2);
            default -> rolElegido.setIdRol(3);
        }
        rolElegido.setNombre(rolTexto);

        Usuario usuario = esEdicion ? usuarioExistente : new Usuario();

        usuario.setNombre(campoNombre.getText().trim());
        usuario.setApellido(campoApellido.getText().trim());
        usuario.setCorreo(campoCorreo.getText().trim());
        usuario.setTelefono(campoTelefono.getText().trim());
        usuario.setRol(rolElegido);

        if (!esEdicion) {
            usuario.setEstado(true);
            usuario.setFechaNacimiento(LocalDate.now().minusYears(18)); // placeholder: agregar campo real si el proyecto lo pide
        }

        String nuevaClave = new String(campoPassword.getPassword());
        if (!nuevaClave.isBlank()) {
            if (esEdicion) {
                // IMPORTANTE: actualizarUsuario() en UsuarioServiceImpl NO
                // encripta la contraseña (a diferencia de registrarUsuario,
                // que sí lo hace). Si no se encripta aquí, se guardaría en
                // texto plano al editar. Si algún día "arreglan" el Service
                // para que también encripte en la actualización, hay que
                // quitar esta línea o vas a terminar con doble hash.
                usuario.setPassword(Utils.Encriptador.hashPassword(nuevaClave));
            } else {
                usuario.setPassword(nuevaClave); // registrarUsuario() sí la encripta
            }
        }

        boolean exito = esEdicion
                ? usuarioService.actualizarUsuario(usuario)
                : usuarioService.registrarUsuario(usuario);

        if (!exito) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el usuario.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        cargarDatos();
    }

}
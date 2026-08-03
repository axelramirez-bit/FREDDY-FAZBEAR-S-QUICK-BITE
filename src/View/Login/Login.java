package View.Login;

import Model.Usuario;
import Service.Implement.UsuarioServiceImpl;
import Service.Interfaz.IUsuarioService;
import Utils.Sesion;
import View.Administrador.DashboardAdministrador;
import View.Cliente.DashboardCliente;
import View.Trabajador.DashboardTrabajador;
import View.Utils.UtilPantalla;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Esqueleto funcional de Login. La lógica de negocio (validar
 * credenciales, guardar sesión, abrir el dashboard correcto) ya
 * está completa aquí — lo único que falta es la parte visual
 * (colores, fuentes, layout con FabricaX) que le corresponde a
 * Integrante 2.
 *
 * REQUIERE el parche de UsuarioLogin-corregido.java aplicado
 * primero (buscarPorCorreo + hash de contraseña), o el login
 * nunca va a coincidir con las contraseñas guardadas.
 * ===============================================================
 */
public class Login extends JFrame {

    private final IUsuarioService usuarioService = new UsuarioServiceImpl();

    // Estos 2 campos los crea/estiliza Integrante 2 con FabricaCampos;
    // aquí solo se muestra dónde se conectan a la lógica.
    private JTextField txtCorreo;
    private JPasswordField txtPassword;

    public Login() {
        setTitle("Freddy Fazbear's Quick Bite - Iniciar sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Esto era lo que faltaba: sin esto, el JFrame se queda con
        // el tamaño mínimo por defecto de Java (por eso se veía
        // "miniatura"). Mismas utilidades que ya usa DashboardBase,
        // para que Login se vea consistente con el resto de la app.
        UtilPantalla.aplicarTamañoMinimo(this);
        UtilPantalla.pantallaCompleta(this);

        // TODO (Integrante 2): construir la interfaz real aquí
        // (logo, campos, botón) usando FabricaCampos/FabricaBotones/
        // AdministradorTema — este constructor es solo el punto de
        // entrada.
    }

    // ==========================================================
    // ACCIÓN DEL BOTÓN "INICIAR SESIÓN"
    // ==========================================================
    private void alPresionarIniciarSesion() {

        String correo = txtCorreo.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (correo.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingresa tu correo y contraseña.",
                    "Campos vacíos",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Usuario usuario = usuarioService.iniciarSesion(correo, password);

        if (usuario == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Correo o contraseña incorrectos.",
                    "No se pudo iniciar sesión",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Sesion.getInstancia().iniciarSesion(usuario);

        abrirDashboardSegunRol(usuario);

        dispose(); // cierra la ventana de Login
    }

    // ==========================================================
    // REDIRECCIÓN SEGÚN ROL
    // ==========================================================
    private void abrirDashboardSegunRol(Usuario usuario) {

        String nombreRol = usuario.getRol().getNombre();

        SwingUtilities.invokeLater(() -> {

            switch (nombreRol) {

                case "Cliente":
                    new DashboardCliente().setVisible(true);
                    break;

                case "Trabajador":
                    new DashboardTrabajador().setVisible(true);
                    break;

                case "Administrador":
                    new DashboardAdministrador().setVisible(true);
                    break;

                default:
                    JOptionPane.showMessageDialog(
                            this,
                            "Rol no reconocido: " + nombreRol,
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
            }
        });
    }

    // ==========================================================
    // PRUEBA AISLADA
    // ==========================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }

}
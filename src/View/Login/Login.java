package View.Login;

import Model.Usuario;
import Service.Implement.UsuarioServiceImpl;
import Service.Interfaz.IUsuarioService;
import Utils.Sesion;
import View.Administrador.DashboardAdministrador;
import View.Cliente.DashboardCliente;
import View.Trabajador.DashboardTrabajador;
import View.Utils.UtilPantalla;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE - LOGIN (TEXTO REGISTRO GRANDE)
 * ===============================================================
 */
public class Login extends JFrame {

    private final IUsuarioService usuarioService = new UsuarioServiceImpl();

    // Campos de texto y componentes
    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    private JButton btnIniciarSesion;
    private JCheckBox chkRecordarme;
    
    // ── BOTONES SOCIALES Y REGISTRO ─────────────────────────
    private JButton btnGoogle;
    private JButton btnFacebook;
    private JButton btnRegistrate;
    
    private JPanel panelFondo;
    private Image imagenFondo;

    // ✅ CONSTANTES ACTUALIZADAS PARA MAYOR TAMAÑO
    private static final int FUENTE_BASE_CAMPOS = 20;      
    private static final int FUENTE_BASE_REGISTRO = 26;    // 👈 AUMENTADO DE 18 A 26
    private static final int FUENTE_BASE_BOTON_PRINCIPAL = 18; 

    public Login() {
        setTitle("Freddy Fazbear's Quick Bite - Iniciar sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        UtilPantalla.aplicarTamañoMinimo(this);
        UtilPantalla.pantallaCompleta(this);

        // ══════════════════════════════════════════════════════
        // 1. CARGAR IMAGEN DE FONDO
        // ══════════════════════════════════════════════════════
        try {
            java.net.URL url = getClass().getResource("/Imagenes/Fondo_Login.png");
            if (url != null) {
                imagenFondo = new ImageIcon(url).getImage();
            } else {
                System.out.println("❌ No se encontró '/Imagenes/Fondo_Login.png'");
            }
        } catch (Exception e) {
            System.out.println("Error al cargar imagen: " + e.getMessage());
        }

        // ══════════════════════════════════════════════════════
        // 2. PANEL DE FONDO ESCALABLE
        // ══════════════════════════════════════════════════════
        panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenFondo != null) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(30, 30, 30));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        panelFondo.setLayout(null);
        setContentPane(panelFondo);

        // ══════════════════════════════════════════════════════
        // 3. CREAR COMPONENTES VISUALES
        // ══════════════════════════════════════════════════════
        
        txtCorreo   = crearCampoTexto("Correo electrónico");
        txtPassword = crearCampoPassword("Contraseña");

        chkRecordarme = new JCheckBox();
        chkRecordarme.setOpaque(false);
        chkRecordarme.setContentAreaFilled(false);
        chkRecordarme.setBorderPainted(false);
        chkRecordarme.setFocusPainted(false);
        chkRecordarme.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        chkRecordarme.addActionListener(e -> {
            if (chkRecordarme.isSelected()) {
                txtPassword.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar('•');
            }
        });

        btnIniciarSesion = new BotonTransparente();
        btnIniciarSesion.setFont(new Font("Segoe UI", Font.BOLD, FUENTE_BASE_BOTON_PRINCIPAL));
        btnIniciarSesion.setForeground(Color.WHITE);
        btnIniciarSesion.setText("INICIAR SESIÓN");
        btnIniciarSesion.addActionListener(e -> alPresionarIniciarSesion());

        btnGoogle = new BotonTransparente();
        btnGoogle.setText(""); 
        btnGoogle.addActionListener(e -> alPresionarGoogle());

        btnFacebook = new BotonTransparente();
        btnFacebook.setText(""); 
        btnFacebook.addActionListener(e -> alPresionarFacebook());

        // --- BOTÓN REGÍSTRATE AQUÍ (CONFIGURACIÓN INICIAL) ---
        btnRegistrate = new BotonTransparente();
        // La fuente real se ajustará en posicionarComponentes, pero ponemos una base grande
        btnRegistrate.setFont(new Font("Segoe UI", Font.BOLD, FUENTE_BASE_REGISTRO));
        btnRegistrate.setForeground(new Color(34, 120, 50)); // Verde oscuro para contraste
        btnRegistrate.setText("Regístrate aquí");
        btnRegistrate.setHorizontalAlignment(SwingConstants.CENTER);
        btnRegistrate.addActionListener(e -> alPresionarRegistrate());

        panelFondo.add(txtCorreo);
        panelFondo.add(txtPassword);
        panelFondo.add(chkRecordarme);
        panelFondo.add(btnIniciarSesion);
        panelFondo.add(btnGoogle);      
        panelFondo.add(btnFacebook);    
        panelFondo.add(btnRegistrate);  

        // ══════════════════════════════════════════════════════
        // 4. POSICIONAMIENTO PROPORCIONAL + ESCALADO DE FUENTES
        // ══════════════════════════════════════════════════════
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        posicionarComponentes(pantalla.width, pantalla.height);

        panelFondo.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                posicionarComponentes(panelFondo.getWidth(), panelFondo.getHeight());
            }
        });

        setVisible(true);
    }

    // ══════════════════════════════════════════════════════════
    // MÉTODO: Posiciona y ESCALA todos los componentes
    // ══════════════════════════════════════════════════════════
    private void posicionarComponentes(int ancho, int alto) {
        if (ancho <= 0 || alto <= 0) return;

        // Factor de escala basado en altura (referencia 1080p)
        float escala = alto / 1080f;

        // ── POSICIONES DE COMPONENTES ──
        ponerBounds(txtCorreo,         0.563,  0.360,  0.340,  0.065);
        ponerBounds(txtPassword,       0.53,  0.480,  0.340,  0.065);
        ponerBounds(btnIniciarSesion,  0.563,  0.629,  0.340,  0.070);
        ponerBounds(chkRecordarme,     0.563,  0.575,  0.035,  0.035);

        ponerBounds(btnGoogle,         0.563,  0.730,  0.160,  0.060);
        ponerBounds(btnFacebook,       0.743,  0.730,  0.160,  0.060);

        // ✅ AJUSTE DE POSICIÓN PARA "REGÍSTRATE AQUÍ"
        // Lo movemos ligeramente hacia abajo y aumentamos su altura para que la letra grande quepa bien
        // x=0.740 (más a la izquierda para centrar bajo facebook/google), y=0.815, w=0.180 (más ancho), h=0.050 (más alto)
        ponerBounds(btnRegistrate,     0.790,  0.813,  0.115,  0.035);

        // ═══════════════════════════════════════════════════════
        // ✅ APLICAR TAMAÑO DE FUENTE DINÁMICO
        // ═══════════════════════════════════════════════════════
        
        // 1. Campos de texto
        int sizeCampos = Math.max(14, (int) (FUENTE_BASE_CAMPOS * escala));
        Font fontCampos = new Font("Segoe UI", Font.PLAIN, sizeCampos);
        txtCorreo.setFont(fontCampos);
        txtPassword.setFont(fontCampos);
        
        int paddingIzq = (int) (40 * escala);
        txtCorreo.setBorder(BorderFactory.createEmptyBorder(0, paddingIzq, 0, 10));
        txtPassword.setBorder(BorderFactory.createEmptyBorder(0, paddingIzq, 0, 10));

        // 2. Botón Principal
        int sizeBtnPrincipal = Math.max(14, (int) (FUENTE_BASE_BOTON_PRINCIPAL * escala));
        btnIniciarSesion.setFont(new Font("Segoe UI", Font.BOLD, sizeBtnPrincipal));

        // 3. ✅ BOTÓN REGÍSTRATE (LETRA GRANDE)
        // Calculamos un tamaño mínimo de 16px y escalamos hasta 26px o más en pantallas grandes
        int sizeRegistro = Math.max(16, (int) (FUENTE_BASE_REGISTRO * escala));
        btnRegistrate.setFont(new Font("Segoe UI", Font.BOLD, sizeRegistro));
        
        // Opcional: Si quieres que el texto sea aún más llamativo, puedes cambiar el estilo a PLAIN o ITALIC
        // btnRegistrate.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, sizeRegistro));
    }

    private void ponerBounds(JComponent comp, double xP, double yP, double wP, double hP) {
        comp.setBounds((int)(xP * panelFondo.getWidth()),
                       (int)(yP * panelFondo.getHeight()),
                       (int)(wP * panelFondo.getWidth()),
                       (int)(hP * panelFondo.getHeight()));
    }

    // ══════════════════════════════════════════════════════════
    // HELPERS VISUALES
    // ══════════════════════════════════════════════════════════

    private JTextField crearCampoTexto(String placeholder) {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, FUENTE_BASE_CAMPOS));
        txt.setForeground(new Color(50, 50, 50));
        txt.setCaretColor(new Color(50, 50, 50));
        txt.setOpaque(false); 
        txt.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 10)); 
        return txt;
    }

    private JPasswordField crearCampoPassword(String placeholder) {
        JPasswordField txt = new JPasswordField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, FUENTE_BASE_CAMPOS));
        txt.setForeground(new Color(50, 50, 50));
        txt.setCaretColor(new Color(50, 50, 50));
        txt.setOpaque(false);
        txt.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 10));
        txt.setEchoChar('•'); 
        return txt;
    }

    // ══════════════════════════════════════════════════════════
    // CLASE INTERNA: Botón transparente
    // ══════════════════════════════════════════════════════════
    class BotonTransparente extends JButton {
        private boolean hover   = false;
        private boolean pressed = false;

        public BotonTransparente() {
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e)  { hover = true;             repaint(); }
                @Override public void mouseExited(MouseEvent e)   { hover = false; pressed = false; repaint(); }
                @Override public void mousePressed(MouseEvent e)  { pressed = true;           repaint(); }
                @Override public void mouseReleased(MouseEvent e) { pressed = false;          repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (hover || pressed) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(pressed ? new Color(255, 255, 255, 70) : new Color(255, 255, 255, 35));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }

    // ══════════════════════════════════════════════════════════
    // ACCIONES
    // ══════════════════════════════════════════════════════════

    private void alPresionarGoogle() {
        JOptionPane.showMessageDialog(this, "Inicio de sesión con Google próximamente.", "Google", JOptionPane.INFORMATION_MESSAGE);
    }

    private void alPresionarFacebook() {
        JOptionPane.showMessageDialog(this, "Inicio de sesión con Facebook próximamente.", "Facebook", JOptionPane.INFORMATION_MESSAGE);
    }

    private void alPresionarRegistrate() {
        System.out.println("📝 Clic en Regístrate aquí");
        // TODO: Navegación real
        // new Registro().setVisible(true);
        // this.dispose();
        JOptionPane.showMessageDialog(this, "Ventana de registro próximamente.", "Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    private void alPresionarIniciarSesion() {
        String correo   = txtCorreo.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (correo.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa tu correo y contraseña.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuario = usuarioService.iniciarSesion(correo, password);

        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "Correo o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Sesion.getInstancia().iniciarSesion(usuario);
        abrirDashboardSegunRol(usuario);
        dispose();
    }

    private void abrirDashboardSegunRol(Usuario usuario) {
        String nombreRol = usuario.getRol().getNombre();
        SwingUtilities.invokeLater(() -> {
            switch (nombreRol) {
                case "Cliente":       new DashboardCliente().setVisible(true);       break;
                case "Trabajador":    new DashboardTrabajador().setVisible(true);    break;
                case "Administrador": new DashboardAdministrador().setVisible(true); break;
                default:
                    JOptionPane.showMessageDialog(this, "Rol no reconocido: " + nombreRol, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
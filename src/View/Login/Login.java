package View.Login;

import Model.Usuario;
import Service.Implement.UsuarioServiceImpl;
import Service.Interfaz.IUsuarioService;
import Utils.Sesion;
import View.Administrador.DashboardAdministrador;
import View.Cliente.DashboardCliente;
import View.Registro.Registro;
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
 * FREDDY-FAZBEAR'S QUICK BITE - LOGIN (RESPONSIVO TOTAL)
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

    // ✅ CONSTANTES DE FUENTE BASE (Referencia 1080p)
    private static final int FUENTE_BASE_CAMPOS = 20;      
    private static final int FUENTE_BASE_REGISTRO = 26;    
    private static final int FUENTE_BASE_BOTON_PRINCIPAL = 18; 

    public Login() {
        setTitle("Freddy Fazbear's Quick Bite - Iniciar sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ══════════════════════════════════════════════════════
        // CONFIGURACIÓN DE VENTANA RESPONSIVA
        // ══════════════════════════════════════════════════════
        // 1. Aplicar tamaño mínimo para evitar que se rompa si es muy pequeña
        UtilPantalla.aplicarTamañoMinimo(this);
        
        // 2. NO usar pantallaCompleta fija si queremos que el usuario pueda redimensionar.
        //    En su lugar, iniciamos maximizada pero permitiendo cambio de tamaño.
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        // Si prefieres que inicie en tamaño normal pero adaptable, comenta la línea de arriba y usa:
        // setSize(1280, 720); setLocationRelativeTo(null);

        // ══════════════════════════════════════════════════════
        // 1. CARGAR IMAGEN DE FONDO
        // ══════════════════════════════════════════════════════
        try {
            java.net.URL url = getClass().getResource("/Imagenes/Fondo_Login.jpg");
            if (url != null) {
                imagenFondo = new ImageIcon(url).getImage();
            } else {
                System.out.println("❌ No se encontró '/Imagenes/Fondo_Login.jpg'");
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
                    // Calidad alta para que la imagen no se pixela al estirar
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
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

        btnRegistrate = new BotonTransparente();
        btnRegistrate.setFont(new Font("Segoe UI", Font.BOLD, FUENTE_BASE_REGISTRO));
        btnRegistrate.setForeground(new Color(34, 120, 50)); 
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
        // 4. LISTENER DE REDIMENSIONAMIENTO (LA CLAVE DE LA ADAPTABILIDAD)
        // ══════════════════════════════════════════════════════
        // Esto asegura que cada vez que cambies el tamaño de la ventana (manualmente o por monitor),
        // se recalculen las posiciones y tamaños basados en los NUEVOS dimensiones.
        panelFondo.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Llamamos al método con el tamaño ACTUAL del panel
                posicionarComponentes(panelFondo.getWidth(), panelFondo.getHeight());
            }
        });

        // Configuración inicial visible
        setVisible(true);
        
        // Forzar una primera actualización después de que la ventana sea visible
        SwingUtilities.invokeLater(() -> {
            posicionarComponentes(panelFondo.getWidth(), panelFondo.getHeight());
        });
    }

    // ══════════════════════════════════════════════════════════
    // MÉTODO: Posiciona y ESCALA todos los componentes
    // NOTA: LOS VALORES NUMÉRICOS (0.563, 0.340, etc.) SON EXACTAMENTE LOS TUYOS.
    //       NO SE HAN MODIFICADO LAS POSICIONES NI PROPORCIONES.
    // ══════════════════════════════════════════════════════════
    private void posicionarComponentes(int ancho, int alto) {
        if (ancho <= 0 || alto <= 0) return;

        // Factor de escala dinámico:
        // Usamos el menor factor entre ancho y alto para asegurar que nada se salga de la pantalla
        // Referencia base: 1920x1080 (Full HD estándar)
        float escalaAncho = ancho / 1920f;
        float escalaAlto = alto / 1080f;
        float escala = Math.min(escalaAncho, escalaAlto); 
        
        // Evitar escalas demasiado pequeñas que hagan ilegible el texto
        if (escala < 0.5f) escala = 0.5f;

        // ── POSICIONES DE COMPONENTES (TUS VALORES ORIGINALES RESPETADOS) ──
        ponerBounds(txtCorreo,         0.563,  0.360,  0.340,  0.065);
        ponerBounds(txtPassword,       0.543,  0.480,  0.340,  0.065);
        ponerBounds(btnIniciarSesion,  0.563,  0.629,  0.340,  0.070);
        ponerBounds(chkRecordarme,     0.553,  0.565,  0.035,  0.035);

        ponerBounds(btnGoogle,         0.563,  0.730,  0.160,  0.060);
        ponerBounds(btnFacebook,       0.743,  0.730,  0.160,  0.060);

        ponerBounds(btnRegistrate,     0.790,  0.813,  0.115,  0.035);

        // ═══════════════════════════════════════════════════════
        // ✅ APLICAR TAMAÑO DE FUENTE DINÁMICO BASADO EN LA ESCALA REAL
        // ═══════════════════════════════════════════════════════
        
        // 1. Campos de texto
        int sizeCampos = Math.max(12, (int) (FUENTE_BASE_CAMPOS * escala));
        Font fontCampos = new Font("Segoe UI", Font.PLAIN, sizeCampos);
        txtCorreo.setFont(fontCampos);
        txtPassword.setFont(fontCampos);
        
        // El padding también escala para que el icono/texto no quede pegado al borde en pantallas chicas
        int paddingIzq = (int) (40 * escala);
        txtCorreo.setBorder(BorderFactory.createEmptyBorder(0, paddingIzq, 0, 10));
        txtPassword.setBorder(BorderFactory.createEmptyBorder(0, paddingIzq, 0, 10));

        // 2. Botón Principal
        int sizeBtnPrincipal = Math.max(12, (int) (FUENTE_BASE_BOTON_PRINCIPAL * escala));
        btnIniciarSesion.setFont(new Font("Segoe UI", Font.BOLD, sizeBtnPrincipal));

        // 3. Botón Regístrate
        int sizeRegistro = Math.max(14, (int) (FUENTE_BASE_REGISTRO * escala));
        btnRegistrate.setFont(new Font("Segoe UI", Font.BOLD, sizeRegistro));
    }

    private void ponerBounds(JComponent comp, double xP, double yP, double wP, double hP) {
        // Esta función multiplica tus porcentajes fijos por el tamaño ACTUAL de la ventana
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
        System.out.println("📝 Clic en Regístrate aquí - Abriendo ventana de Registro...");
        this.dispose();
        SwingUtilities.invokeLater(() -> {
            try {
                Registro ventanaRegistro = new Registro();
                ventanaRegistro.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Error al abrir la ventana de registro: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                SwingUtilities.invokeLater(() -> new Login().setVisible(true));
            }
        });
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
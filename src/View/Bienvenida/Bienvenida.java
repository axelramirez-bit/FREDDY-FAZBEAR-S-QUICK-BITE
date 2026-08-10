package View.Bienvenida;

import View.Login.Login;
import View.Registro.Registro;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Bienvenida extends JFrame {

    private JPanel panelFondo;
    private Image imagenFondo;

    // ✅ Solo quedan los botones de acción
    private JButton btnIniciarSesion;
    private JButton btnRegistrarse;

    // Fuente base para escalar
    private static final String FUENTE_BASE = "Segoe UI";
    private static final int TAMANO_FUENTE_BASE = 20; // Tamaño de referencia para 1080p

    class BotonTransparente extends JButton {

        private boolean hover = false;
        private boolean pressed = false;

        public BotonTransparente() {
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            // La fuente se ajustará dinámicamente en posicionarBotones
            setFont(new Font(FUENTE_BASE, Font.BOLD, TAMANO_FUENTE_BASE));

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hover = true;   repaint(); }
                @Override public void mouseExited (MouseEvent e) { hover = false; pressed = false; repaint(); }
                @Override public void mousePressed(MouseEvent e) { pressed = true;  repaint(); }
                @Override public void mouseReleased(MouseEvent e){ pressed = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (hover || pressed) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(pressed ? new Color(255, 255, 255, 70) : new Color(255, 255, 255, 35));
                
                // ✅ Radio del borde redondeado proporcional al alto del botón
                int radio = Math.max(10, getHeight() / 4); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);
                g2.dispose();
            }
        }
        
        // Método para actualizar la fuente según el tamaño actual
        public void actualizarFuente(int alturaVentana) {
            // Escala lineal: si la altura es 1080, usa 20. Si es 720, usa ~13.
            float escala = alturaVentana / 1080f;
            int nuevoTamano = Math.max(12, (int) (TAMANO_FUENTE_BASE * escala));
            setFont(new Font(FUENTE_BASE, Font.BOLD, nuevoTamano));
        }
    }

    public Bienvenida() {
        setTitle("Freddy Fazbear's Pizza Suni - Bienvenida");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ── Cargar imagen ────────────────────────────────────
        try {
            java.net.URL url = getClass().getResource("/Imagenes/Bienvenida_Fondo.png");
            if (url != null) {
                imagenFondo = new ImageIcon(url).getImage();
            } else {
                System.out.println("❌ No se encontró la imagen.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // ── Panel de fondo escalable ─────────────────────────
        panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenFondo != null) {
                    Graphics2D g2d = (Graphics2D) g;
                    // ✅ Mejor calidad de interpolación para escalado
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                            RenderingHints.VALUE_RENDER_QUALITY);
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    g2d.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        panelFondo.setLayout(null);
        setContentPane(panelFondo);

        // ── Crear solo los botones de acción ─────────────────
        btnIniciarSesion = crearBotonAccion();
        btnRegistrarse = crearBotonAccion();

        panelFondo.add(btnIniciarSesion);
        panelFondo.add(btnRegistrarse);

        // ── Acciones ──────────────────────────────────────────
        btnIniciarSesion.addActionListener(e -> {
            System.out.println("🔑 Navegando a Login...");
            
            // Opción A: Cerrar bienvenida y abrir Login
            dispose(); // Cierra esta ventana
            
      
            SwingUtilities.invokeLater(() -> {
                new Login().setVisible(true); 
            });
        });
        
       btnRegistrarse.addActionListener(e -> {
            System.out.println("📝 Navegando a Registro...");
            
            // Opción A: Cerrar bienvenida y abrir Registro
            dispose(); // Cierra esta ventana
            
            // Abre la nueva ventana en el hilo de Swing
            SwingUtilities.invokeLater(() -> {
                new Registro().setVisible(true);
            });
       });
        // ── Posicionamiento proporcional ──────────────────────
        // Usamos un listener que se dispara al inicio y al redimensionar
        panelFondo.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                posicionarBotones(panelFondo.getWidth(), panelFondo.getHeight());
            }
        });

        // ✅ Configuración inicial de ventana
        setSize(1280, 720); // Tamaño inicial razonable
        setLocationRelativeTo(null); // Centrar en pantalla
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizar al iniciar
        setVisible(true);
        
        // Forzar una primera actualización de fuentes después de ser visible
        SwingUtilities.invokeLater(() -> {
            posicionarBotones(panelFondo.getWidth(), panelFondo.getHeight());
        });
    }

    private void posicionarBotones(int ancho, int alto) {
        if (ancho <= 0 || alto <= 0) return; // Evitar errores al inicio

        // ✅ Posiciones proporcionales (ajustadas para mejor centrado visual)
        ponerBounds(btnIniciarSesion, 0.270, 0.625, 0.220, 0.085);
        ponerBounds(btnRegistrarse,   0.528, 0.625, 0.220, 0.085);

        // ✅ Actualizar tamaño de fuente según la altura actual
        ((BotonTransparente) btnIniciarSesion).actualizarFuente(alto);
        ((BotonTransparente) btnRegistrarse).actualizarFuente(alto);
    }

    private void ponerBounds(JButton btn, double xP, double yP, double wP, double hP) {
        btn.setBounds((int) (xP * panelFondo.getWidth()),
                      (int) (yP * panelFondo.getHeight()),
                      (int) (wP * panelFondo.getWidth()),
                      (int) (hP * panelFondo.getHeight()));
    }

    private JButton crearBotonAccion() {
        return new BotonTransparente();
    }

    public static void main(String[] args) {
        // ✅ Asegurar que la fuente Segoe UI esté disponible o usar fallback
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        boolean fuenteDisponible = false;
        for (String f : ge.getAvailableFontFamilyNames()) {
            if (f.equalsIgnoreCase(FUENTE_BASE)) {
                fuenteDisponible = true;
                break;
            }
        }
        if (!fuenteDisponible) {
            System.out.println("⚠️ Fuente 'Segoe UI' no disponible, usando SansSerif");
            // Podrías cambiar la constante FUENTE_BASE aquí si quieres
        }

        SwingUtilities.invokeLater(() -> new Bienvenida());
    }
}
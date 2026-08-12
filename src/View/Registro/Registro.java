package View.Registro;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import View.Login.Login;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Registro extends JFrame {

    private JPanel panelFondo;
    private Image imagenFondo;

    // Declaración de componentes
    private JTextField txtNombre, txtApellido, txtCorreo, txtTelefono, txtFecha;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JComboBox<String> cbRol;
    private JButton btnGuardar, btnCancelar, btnVerPassword; // Renombrados para claridad

    // Estado para saber si la contraseña es visible o no
    private boolean passwordVisible = false;
    // Guardamos el echo char original (el asterisco)
    private char echoCharOriginal;

    // Resolución "de diseño" sobre la que calibraste las posiciones originales.
    // Todas las proporciones (xP, yP, wP, hP) se calcularon dividiendo tus
    // valores originales de setBounds entre estos números.
    private static final double ANCHO_DISENO = 1280.0;
    private static final double ALTO_DISENO = 720.0;

    public Registro() {
        setTitle("Freddy Fazbear's Quick Bite -Registro" );
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        
        // ── Cargar imagen ────────────────────────────────────
        try {
            URL url = getClass().getResource("/Imagenes/Fondo_Registro.png");
            if (url != null) {
                imagenFondo = new ImageIcon(url).getImage();
            } else {
                System.out.println("❌ No se encontró la imagen Fondo_Registro.png");
            }
        } catch (Exception e) {
            System.out.println("Error cargando imagen: " + e.getMessage());
        }

        // ── Panel de fondo escalable ─────────────────────────
        panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenFondo != null) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        
        panelFondo.setLayout(null); 
        panelFondo.setOpaque(false);
        setContentPane(panelFondo);

        // --- INICIALIZACIÓN DE COMPONENTES ---
        txtNombre = crearTextFieldTransparente();
        txtApellido = crearTextFieldTransparente();
        txtCorreo = crearTextFieldTransparente();
        txtTelefono = crearTextFieldTransparente();
        txtFecha = crearTextFieldTransparente();

        // 🔧 Restringir el campo de teléfono para que solo acepte dígitos numéricos
        aplicarSoloNumeros(txtTelefono, 8); // 8 = longitud máxima permitida, ajústalo a tu necesidad
        
        txtPassword = crearPasswordFieldTransparente();
        txtConfirmPassword = crearPasswordFieldTransparente();
        // Guardamos el carácter de eco por defecto (usualmente '•')
        echoCharOriginal = txtPassword.getEchoChar();

        cbRol = crearComboBoxTransparente();
        cbRol.addItem("");
        cbRol.addItem("Usuario ");
        cbRol.addItem("Administrador");
        cbRol.addItem("Empleado");

        btnGuardar = crearBotonTransparente();
        btnCancelar = crearBotonTransparente();      // Antes btnTablePlaque
        btnVerPassword = crearBotonTransparente();   // Antes btnTablePlaohets

        // --- AGREGAR AL PANEL ---
        panelFondo.add(txtNombre);
        panelFondo.add(txtApellido);
        panelFondo.add(txtCorreo);
        panelFondo.add(txtTelefono);
        panelFondo.add(txtFecha);
        panelFondo.add(txtPassword);
        panelFondo.add(txtConfirmPassword);
        panelFondo.add(cbRol);
        panelFondo.add(btnCancelar);
        panelFondo.add(btnGuardar);
        panelFondo.add(btnVerPassword);

        // ── LISTENER PARA REDIMENSIONAMIENTO (RESPONSIVO) ─────
        panelFondo.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                posicionarComponentes(panelFondo.getWidth(), panelFondo.getHeight());
            }
        });

        // ── ACCIONES DE LOS BOTONES ──────────────────────────

        // 1. Acción Botón CANCELAR: Regresar al Login
        btnCancelar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Seguro que deseas cancelar? Se perderán los datos.", 
                "Confirmar Cancelación", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose(); // Cierra la ventana de Registro
                
                // Abrir la ventana de Login
                SwingUtilities.invokeLater(() -> {
                    try {
                        // Asumiendo que tu clase Login está en View.Login y tiene constructor vacío
                        Login loginFrame = new Login(); 
                        loginFrame.setVisible(true);
                    } catch (Exception ex) {
                        // Si no encuentra la clase Login, muestra error pero no crashea
                        JOptionPane.showMessageDialog(null, 
                            "Error: No se encontró la clase Login. Verifica el import.", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                });
            }
        });

        // 2. Acción Botón VER CONTRASEÑA: Toggle Visibilidad
        btnVerPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                togglePasswordVisibility();
            }
        });

        // 3. Acción Botón GUARDAR
        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText();
            String fecha = txtFecha.getText();
            String telefono = txtTelefono.getText();
            String rol = cbRol.getSelectedItem() != null ? cbRol.getSelectedItem().toString() : "";
            
            // Validación simple de contraseñas coincidentes
            String pass1 = new String(txtPassword.getPassword());
            String pass2 = new String(txtConfirmPassword.getPassword());
            
            if (!pass1.equals(pass2)) {
                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, 
                "✅ Datos Guardados Correctamente\n\n" +
                "Nombre: " + nombre + "\n" +
                "Fecha Nac: " + fecha + "\n" +
                "Teléfono: " + telefono + "\n" +
                "Rol: " + rol, 
                "Registro Exitoso", 
                JOptionPane.INFORMATION_MESSAGE);
                
            // Opcional: Limpiar campos o ir al login después de guardar
            // this.dispose();
            // new Login().setVisible(true);
        });

        // ✅ Configuración inicial
        setSize(1280, 720); 
        setVisible(true);
        
        SwingUtilities.invokeLater(() -> {
            posicionarComponentes(panelFondo.getWidth(), panelFondo.getHeight());
        });
    }

    // --- LÓGICA DE VISIBILIDAD DE CONTRASEÑA ---
    private void togglePasswordVisibility() {
        passwordVisible = !passwordVisible; // Invertir estado

        if (passwordVisible) {
            // Mostrar texto: echo char 0 significa "no ocultar"
            txtPassword.setEchoChar((char) 0);
            txtConfirmPassword.setEchoChar((char) 0);
            // Opcional: Cambiar tooltip o color del botón para indicar estado
            btnVerPassword.setToolTipText("Ocultar contraseña");
        } else {
            // Ocultar texto: restaurar el echo char original (asterisco/bullet)
            txtPassword.setEchoChar(echoCharOriginal);
            txtConfirmPassword.setEchoChar(echoCharOriginal);
            btnVerPassword.setToolTipText("Ver contraseña");
        }
        
        // Forzar repintado para asegurar cambio visual inmediato
        txtPassword.repaint();
        txtConfirmPassword.repaint();
    }


    private void posicionarComponentes(int ancho, int alto) {
        if (ancho <= 0 || alto <= 0) return;

       

    
        // Fila 1: Nombre y Apellido
        ponerBounds(txtNombre,    477 / ANCHO_DISENO, 268 / ALTO_DISENO, 220 / ANCHO_DISENO, 35 / ALTO_DISENO);
        ponerBounds(txtApellido,  695 / ANCHO_DISENO, 268 / ALTO_DISENO, 220 / ANCHO_DISENO, 35 / ALTO_DISENO);

        // Fila 2: Correo y Teléfono
        ponerBounds(txtCorreo,    477 / ANCHO_DISENO, 340 / ALTO_DISENO, 220 / ANCHO_DISENO, 35 / ALTO_DISENO);
        ponerBounds(txtTelefono,  695 / ANCHO_DISENO, 340 / ALTO_DISENO, 220 / ANCHO_DISENO, 35 / ALTO_DISENO);

        // Fila 3: Contraseñas
        ponerBounds(txtPassword,        477 / ANCHO_DISENO, 410 / ALTO_DISENO, 220 / ANCHO_DISENO, 35 / ALTO_DISENO);
        ponerBounds(txtConfirmPassword, 695 / ANCHO_DISENO, 410 / ALTO_DISENO, 220 / ANCHO_DISENO, 35 / ALTO_DISENO);

        // Fila 4: Fecha y Rol
        ponerBounds(txtFecha, 477 / ANCHO_DISENO, 480 / ALTO_DISENO, 220 / ANCHO_DISENO, 35 / ALTO_DISENO);
        ponerBounds(cbRol,    660 / ANCHO_DISENO, 478 / ALTO_DISENO, 200 / ANCHO_DISENO, 35 / ALTO_DISENO);

        // Botones Inferiores
        ponerBounds(btnCancelar,    320 / ANCHO_DISENO, 655 / ALTO_DISENO, 200 / ANCHO_DISENO, 55 / ALTO_DISENO);
        ponerBounds(btnGuardar,     600 / ANCHO_DISENO, 660 / ALTO_DISENO, 250 / ANCHO_DISENO, 65 / ALTO_DISENO);
        ponerBounds(btnVerPassword, 930 / ANCHO_DISENO, 655 / ALTO_DISENO, 200 / ANCHO_DISENO, 55 / ALTO_DISENO);

        actualizarFuentes(ancho, alto);
    }

    private void ponerBounds(Component c, double xP, double yP, double wP, double hP) {
        int x = (int) (xP * panelFondo.getWidth());
        int y = (int) (yP * panelFondo.getHeight());
        int w = (int) (wP * panelFondo.getWidth());
        int h = (int) (hP * panelFondo.getHeight());
        c.setBounds(x, y, w, h);
    }

    private void actualizarFuentes(int anchoVentana, int altoVentana) {
        double escalaBase = Math.min(anchoVentana / ANCHO_DISENO, altoVentana / ALTO_DISENO);
        int nuevoTamano = (int) (14 * escalaBase);
        nuevoTamano = Math.max(10, Math.min(nuevoTamano, 24)); 
        
        Font nuevaFuente = new Font("Arial", Font.BOLD, nuevoTamano);

        txtNombre.setFont(nuevaFuente);
        txtApellido.setFont(nuevaFuente);
        txtCorreo.setFont(nuevaFuente);
        txtTelefono.setFont(nuevaFuente);
        txtFecha.setFont(nuevaFuente);
        txtPassword.setFont(nuevaFuente);
        txtConfirmPassword.setFont(nuevaFuente);
        cbRol.setFont(nuevaFuente);
        
        int padding = (int) (5 * escalaBase);
        txtNombre.setMargin(new Insets(0, padding, 0, 0));
        txtApellido.setMargin(new Insets(0, padding, 0, 0));
        txtCorreo.setMargin(new Insets(0, padding, 0, 0));
        txtTelefono.setMargin(new Insets(0, padding, 0, 0));
        txtFecha.setMargin(new Insets(0, padding, 0, 0));
        txtPassword.setMargin(new Insets(0, padding, 0, 0));
        txtConfirmPassword.setMargin(new Insets(0, padding, 0, 0));
    }

    // --- MÉTODOS AUXILIARES PARA TRANSPARENCIA ---

    private JTextField crearTextFieldTransparente() {
        JTextField txt = new JTextField();
        txt.setOpaque(false); 
        txt.setBorder(null);  
        txt.setForeground(Color.BLACK); 
        txt.setHorizontalAlignment(JTextField.LEFT);
        txt.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        return txt;
    }

    private JPasswordField crearPasswordFieldTransparente() {
        JPasswordField txt = new JPasswordField();
        txt.setOpaque(false);
        txt.setBorder(null);
        txt.setForeground(Color.BLACK);
        txt.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        return txt;
    }

    // 🔧 NUEVO: Restringe un JTextField para que solo acepte dígitos (0-9),
    // usando un DocumentFilter. Esto bloquea letras y símbolos tanto al
    // escribir como al pegar texto, y opcionalmente limita la longitud máxima.
    private void aplicarSoloNumeros(JTextField campo, int longitudMaxima) {
        ((PlainDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                String filtrado = string.replaceAll("[^0-9]", "");
                if (filtrado.isEmpty()) return;
                if (longitudMaxima > 0 && fb.getDocument().getLength() + filtrado.length() > longitudMaxima) {
                    int espacioDisponible = longitudMaxima - fb.getDocument().getLength();
                    if (espacioDisponible <= 0) return;
                    filtrado = filtrado.substring(0, espacioDisponible);
                }
                super.insertString(fb, offset, filtrado, attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) {
                    super.replace(fb, offset, length, text, attrs);
                    return;
                }
                String filtrado = text.replaceAll("[^0-9]", "");
                int longitudResultante = fb.getDocument().getLength() - length + filtrado.length();
                if (longitudMaxima > 0 && longitudResultante > longitudMaxima) {
                    int espacioDisponible = longitudMaxima - (fb.getDocument().getLength() - length);
                    if (espacioDisponible <= 0) return;
                    filtrado = filtrado.substring(0, Math.min(filtrado.length(), espacioDisponible));
                }
                super.replace(fb, offset, length, filtrado, attrs);
            }
        });
    }

    private JComboBox<String> crearComboBoxTransparente() {
        JComboBox<String> cb = new JComboBox<>();
        
        // 1. Configuración básica transparente para el campo principal
        cb.setOpaque(false);
        cb.setBorder(null);
        cb.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cb.setForeground(Color.BLACK); // Texto negro para contraste
        
        // 2. Colores personalizados para combinar con el fondo FNAF
        // Color de fondo de la lista desplegable (Marrón Pergamino suave)
        Color colorFondoLista = new Color(210, 180, 140); // Tono café/pergamino (#D2B48C)
        // Color cuando se selecciona/pasa el mouse (Marrón más oscuro)
        Color colorSeleccion = new Color(160, 120, 80);   // Tono café oscuro (#A07850)
        // Color del texto
        Color colorTexto = new Color(40, 20, 10);         // Marrón casi negro para mejor lectura que el negro puro

        // Aplicar color de fondo al popup (la lista que se abre)
        // Nota: En algunos LookAndFeels esto requiere ajustar el UI, pero setBackground suele funcionar en el popup
        cb.setBackground(colorFondoLista);
        
        // 3. Renderizador personalizado para controlar cada fila de la lista
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                setBorder(null); // Sin bordes feos
                setForeground(colorTexto); // Texto marrón oscuro
                
                if (isSelected) {
                    // Si está seleccionado (mouse encima o elegido): Fondo marrón oscuro, texto claro opcional
                    setBackground(colorSeleccion);
                    setForeground(Color.WHITE); // Texto blanco resalta mejor sobre marrón oscuro
                    setOpaque(true);
                } else {
                    // Si no está seleccionado: Fondo marrón pergamino (igual al de la lista)
                    setBackground(colorFondoLista);
                    setOpaque(true); // Importante: debe ser opaco para pintar el color de fondo
                }
                
                return this;
            }
        });

        // 4. Ajuste adicional para asegurar que el popup use nuestros colores
        // Esto fuerza al componente básico del combo box a usar nuestro color de fondo
        cb.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE); // Ayuda en algunos L&F
        
        // Truco para cambiar el color del popup en Nimbus/Windows L&F si el setBackground no basta:
        UIManager.put("ComboBox.background", colorFondoLista);
        UIManager.put("ComboBox.selectionBackground", colorSeleccion);
        UIManager.put("ComboBox.foreground", colorTexto);
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);

        return cb;
    }

    private JButton crearBotonTransparente() {
        JButton btn = new JButton();
        btn.setOpaque(false);       
        btn.setContentAreaFilled(false); 
        btn.setBorderPainted(false);     
        btn.setFocusPainted(false);      
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        return btn;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new Registro();
        });
    }
}
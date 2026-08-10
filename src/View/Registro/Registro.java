package View.Registro;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Registro extends JFrame {

    private JPanel panelFondo;

    // Declaración de componentes
    private JTextField txtNombre, txtApellido, txtCorreo, txtTelefono;
    private JTextField txtFecha; // CAMBIO: Ahora es JTextField en lugar de JComboBox
    private JPasswordField txtPassword, txtConfirmPassword;
    private JComboBox<String> cbRol;
    private JButton btnGuardar, btnTablePlaque, btnTablePlaohets;

    public Registro() {
        setTitle("Registro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Iniciar maximizada
        setLayout(new BorderLayout());

        try {
            // Cargar imagen
            java.awt.Image imagenFondo = ImageIO.read(getClass().getResource("/Imagenes/Fondo_Registro.png"));

            panelFondo = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (imagenFondo != null) {
                        // Dibujar imagen ajustada al tamaño de la ventana
                        g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                    }
                }
            };
            
            // Layout nulo para posicionamiento absoluto sobre la imagen
            panelFondo.setLayout(null); 
            panelFondo.setOpaque(false);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar imagen", "Error", JOptionPane.ERROR_MESSAGE);
            panelFondo = new JPanel();
        }

        // --- CONFIGURACIÓN DE COMPONENTES TRANSPARENTES ---
        
        // 1. Campos de Texto
        txtNombre = crearTextFieldTransparente();
        txtApellido = crearTextFieldTransparente();
        txtCorreo = crearTextFieldTransparente();
        txtTelefono = crearTextFieldTransparente();
        txtPassword = crearPasswordFieldTransparente();
        txtConfirmPassword = crearPasswordFieldTransparente();
        
        // CAMBIO: Inicializamos txtFecha como campo de texto transparente
        txtFecha = crearTextFieldTransparente(); 
        
        // 2. ComboBox (Solo Rol ahora)
        cbRol = crearComboBoxTransparente();

        // 3. Botones
        btnGuardar = crearBotonTransparente();
        btnTablePlaque = crearBotonTransparente();
        btnTablePlaohets = crearBotonTransparente();

        // --- POSICIONAMIENTO (COORDENADAS) ---
        // NOTA: Se mantienen TUS coordenadas exactas.
        
        // Fila 1: Nombre y Apellido
        txtNombre.setBounds(505, 277, 220, 35); 
        txtApellido.setBounds(738, 277, 220, 35);

        // Fila 2: Correo y Teléfono
        txtCorreo.setBounds(505, 350, 220, 35);
        txtTelefono.setBounds(738, 350, 220, 35);

        // Fila 3: Contraseñas
        txtPassword.setBounds(505, 422, 220, 35);
        txtConfirmPassword.setBounds(738, 422, 220, 35);

        // Fila 4: Fecha y Rol
        txtFecha.setBounds(505, 493, 220, 35); 
        cbRol.setBounds(700, 494, 220, 35);

        // Botones Inferiores
        btnTablePlaque.setBounds(320, 680, 200, 50);
        btnGuardar.setBounds(600, 680, 250, 60); // Botón central más grande
        btnTablePlaohets.setBounds(930, 680, 200, 50);

        // --- AGREGAR AL PANEL ---
        panelFondo.add(txtNombre);
        panelFondo.add(txtApellido);
        panelFondo.add(txtCorreo);
        panelFondo.add(txtTelefono);
        panelFondo.add(txtPassword);
        panelFondo.add(txtConfirmPassword);
        panelFondo.add(txtFecha); // CAMBIO: Agregamos el nuevo campo de texto
        panelFondo.add(cbRol);
        panelFondo.add(btnTablePlaque);
        panelFondo.add(btnGuardar);
        panelFondo.add(btnTablePlaohets);

        add(panelFondo, BorderLayout.CENTER);
        setVisible(true);
    }

    // --- MÉTODOS AUXILIARES PARA TRANSPARENCIA ---

    private JTextField crearTextFieldTransparente() {
        JTextField txt = new JTextField();
        txt.setOpaque(false); // Fondo transparente
        txt.setBorder(null);  // Sin bordes
        txt.setForeground(Color.BLACK); // Color del texto
        txt.setFont(new Font("Arial", Font.PLAIN, 14));
        txt.setHorizontalAlignment(JTextField.LEFT);
        return txt;
    }

    private JPasswordField crearPasswordFieldTransparente() {
        JPasswordField txt = new JPasswordField();
        txt.setOpaque(false);
        txt.setBorder(null);
        txt.setForeground(Color.BLACK);
        txt.setFont(new Font("Arial", Font.PLAIN, 14));
        return txt;
    }

    private JComboBox<String> crearComboBoxTransparente() {
        JComboBox<String> cb = new JComboBox<>();
        cb.setOpaque(false);
        // Hacer transparente el renderizador (la parte visual)
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setOpaque(false);
                setForeground(Color.BLACK);
                setBorder(null);
                return this;
            }
        });
        cb.setBorder(null);
        cb.setFont(new Font("Arial", Font.PLAIN, 14));
        return cb;
    }

    private JButton crearBotonTransparente() {
        JButton btn = new JButton();
        btn.setOpaque(false);       // Sin fondo
        btn.setContentAreaFilled(false); // No pintar el área
        btn.setBorderPainted(false);     // Sin borde
        btn.setFocusPainted(false);      // Sin borde al hacer click
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Manito al pasar el mouse
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Registro r = new Registro();
            
            // Agregar acción al botón guardar aquí para tener acceso a las variables
            r.btnGuardar.addActionListener(e -> {
                String nombre = r.txtNombre.getText();
                String apellido = r.txtApellido.getText();
                String fecha = r.txtFecha.getText(); // Ahora podemos obtener la fecha escrita
                
                // Ejemplo de uso
                System.out.println("Fecha ingresada: " + fecha);
                
                JOptionPane.showMessageDialog(r, "Datos guardados:\nNombre del Usuario creado:\n " + nombre );
            });
        });
    }
}
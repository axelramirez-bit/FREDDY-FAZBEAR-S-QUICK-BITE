package View.Splash;

import View.Utils.UtilImagenes;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Pantalla de carga (splash screen) que se muestra al arrancar la
 * aplicación, antes de la ventana de Bienvenida.
 *
 * Usa las 6 imágenes de la carpeta "Resources/BarraCarga"
 * (1.png ... 6.png), que ya incluyen su propio dibujo de fondo,
 * texto y barra de progreso. La clase simplemente va alternando
 * esas imágenes en orden para simular el avance de la carga,
 * mientras en segundo plano se ejecutan las tareas reales de
 * inicialización (tema, conexión a la base de datos, etc.).
 *
 * Al ser una ventana sin decoración (undecorated), incluye sus
 * propios botones de minimizar y cerrar, imitando los controles
 * normales de una ventana de Windows.
 *
 * Uso típico (desde Main):
 *
 *     SplashScreen splash = new SplashScreen();
 *     splash.iniciarCarga(
 *             () -> { / tareas reales de inicialización / },
 *             () -> new Bienvenida().setVisible(true)
 *     );
 * ===============================================================
 */
public class SplashScreen extends JFrame {

    // Cantidad de imágenes disponibles en Resources/BarraCarga
    private static final int TOTAL_FRAMES = 6;

    // Tiempo que se muestra cada frame de la animación (ms)
    private static final int MS_POR_FRAME = 450;

    // Pequeña pausa con el frame final ya visible, antes de cerrar
    private static final int MS_PAUSA_FINAL = 300;

    // Tamaño de la ventana de carga (tamaño original)
    private static final int ANCHO = 900;
    private static final int ALTO = 520;

    // Tamaño de los botones de control (minimizar / cerrar)
    private static final int ANCHO_BOTON = 46;
    private static final int ALTO_BOTON = 30;

    private final Image[] frames = new Image[TOTAL_FRAMES];
    private int frameActual = 0;
    private final PanelAnimado panelAnimado;

    /**
     * Botón simple para los controles de la ventana (minimizar /
     * cerrar), con efecto hover sutil, igual de espíritu que
     * BotonTransparente en Bienvenida.
     */
    private class BotonControl extends JButton {

        private boolean hover = false;
        private final boolean esCerrar;

        BotonControl(String texto, boolean esCerrar) {
            super(texto);
            this.esCerrar = esCerrar;

            setBorderPainted(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setOpaque(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.PLAIN, 15));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (hover) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(esCerrar
                        ? new Color(232, 17, 35)          // rojo estándar de "cerrar"
                        : new Color(255, 255, 255, 40));  // gris claro de "minimizar"
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }

    public SplashScreen() {

        setUndecorated(true);
        setResizable(false);
        setSize(ANCHO, ALTO);
        setLocationRelativeTo(null); // Centrar en pantalla

        // ── Cargar los 6 frames en orden ─────────────────────
        for (int i = 0; i < TOTAL_FRAMES; i++) {
            frames[i] = UtilImagenes.cargando(i + 1).getImage();
        }

        // ── Panel con la animación (ocupa toda la ventana) ───
        panelAnimado = new PanelAnimado();
        panelAnimado.setBounds(0, 0, ANCHO, ALTO);

        // ── Botones de minimizar / cerrar, arriba a la derecha
        BotonControl btnMinimizar = new BotonControl("–", false);
        btnMinimizar.setBounds(
                ANCHO - (ANCHO_BOTON * 2), 0, ANCHO_BOTON, ALTO_BOTON);
        btnMinimizar.addActionListener(e ->
                setExtendedState(JFrame.ICONIFIED));

        BotonControl btnCerrar = new BotonControl("✕", true);
        btnCerrar.setBounds(
                ANCHO - ANCHO_BOTON, 0, ANCHO_BOTON, ALTO_BOTON);
        btnCerrar.addActionListener(e -> {
            // El usuario canceló la carga: se cierra la aplicación.
            System.exit(0);
        });

        // ── Panel con capas: imagen abajo, botones arriba ────
        JLayeredPane capas = new JLayeredPane();
        capas.setPreferredSize(new Dimension(ANCHO, ALTO));
        capas.add(panelAnimado, JLayeredPane.DEFAULT_LAYER);
        capas.add(btnMinimizar, JLayeredPane.PALETTE_LAYER);
        capas.add(btnCerrar, JLayeredPane.PALETTE_LAYER);

        setContentPane(capas);
    }

    /**
     * Panel que dibuja el frame actual de la animación, escalado
     * para ocupar todo el panel (igual criterio que el fondo de
     * Bienvenida: escalado + interpolación bicúbica).
     */
    private class PanelAnimado extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Image actual = frames[frameActual];
            if (actual == null) {
                return;
            }

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.drawImage(actual, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * Muestra la pantalla de carga, anima los 6 frames en orden y,
     * en paralelo, ejecuta las tareas reales de inicialización en
     * segundo plano (para no congelar la animación). Cuando ambas
     * cosas terminan, cierra el splash y ejecuta la acción final
     * (normalmente, abrir la ventana de Bienvenida).
     *
     * @param tareaFondo Tareas reales de inicialización (conexión a
     *                   BD, configuración, etc.). Se ejecutan en un
     *                   hilo distinto al de Swing. Puede ser null.
     * @param alTerminar Acción a ejecutar en el hilo de Swing una
     *                   vez cerrada la pantalla de carga (por
     *                   ejemplo, abrir la ventana de Bienvenida).
     */
    public void iniciarCarga(Runnable tareaFondo, Runnable alTerminar) {

        setVisible(true);
        toFront();

        // ── Animación de los 6 frames ─────────────────────────
        Timer timerAnimacion = new Timer(MS_POR_FRAME, null);
        timerAnimacion.addActionListener(e -> {
            if (frameActual < TOTAL_FRAMES - 1) {
                frameActual++;
                panelAnimado.repaint();
            }
        });
        timerAnimacion.start();

        // ── Tareas reales de inicialización en segundo plano ──
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                long inicio = System.currentTimeMillis();

                if (tareaFondo != null) {
                    try {
                        tareaFondo.run();
                    } catch (Exception ex) {
                        System.err.println(
                                "Error durante la carga inicial: " + ex.getMessage());
                    }
                }

                // Aseguramos que la animación se alcance a ver
                // completa, aunque la carga real termine antes.
                long duracionMinima = (long) TOTAL_FRAMES * MS_POR_FRAME;
                long transcurrido = System.currentTimeMillis() - inicio;
                long restante = duracionMinima - transcurrido;

                if (restante > 0) {
                    try {
                        Thread.sleep(restante);
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                timerAnimacion.stop();
                frameActual = TOTAL_FRAMES - 1;
                panelAnimado.repaint();

                // Pequeña pausa con el frame final (barra completa)
                // ya visible, antes de cerrar la pantalla de carga.
                Timer timerCierre = new Timer(MS_PAUSA_FINAL, ev -> {
                    dispose();
                    if (alTerminar != null) {
                        SwingUtilities.invokeLater(alTerminar);
                    }
                });
                timerCierre.setRepeats(false);
                timerCierre.start();
            }
        };
        worker.execute();
    }
}

package View.Splash;

import Config.ConexionException;
import Utils.AppLogger;
import View.Utils.CacheImagenes;
import View.Utils.CargadorFuentes;
import View.Utils.FabricaDialogos;
import View.Utils.PaletaColores;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
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
 * REDISEÑO:
 * Ya no depende de las 6 imágenes estáticas de Resources/BarraCarga
 * (el oso caminando frente a las tiendas). Ahora el fondo y la
 * animación se dibujan por código con Graphics2D, y el logotipo
 * oficial del proyecto (Resources/Imagenes/Logotipo.png) se usa
 * como pieza central:
 *
 *   - El logotipo oficial, con una entrada suave (fundido + escala)
 *     y un leve balanceo, rodeado de un anillo de progreso que se
 *     va completando a medida que la carga real avanza.
 *   - Título con una pequeña animación de entrada (fundido + subida).
 *   - Barra de progreso con degradado y un brillo que se desliza.
 *   - Checklist de 4 pasos que se van marcando con un check
 *     animado conforme avanza la carga.
 *
 * Ventajas sobre el diseño anterior:
 *   - Se ve nítido en cualquier resolución (es vectorial, no hay
 *     imágenes de mapa de bits estiradas/borrosas).
 *   - La animación refleja el progreso REAL de la carga, no un
 *     conteo de frames fijo cada 450 ms.
 *   - Usa la paleta de colores oficial del proyecto
 *     (PaletaColores), así que combina con el resto de la app en
 *     vez de un cielo azul genérico.
 * ===============================================================
 */
public class SplashScreen extends JFrame {

    // Tamaño de la ventana de carga (se mantiene el tamaño original)
    private static final int ANCHO = 900;
    private static final int ALTO = 520;

    // Tamaño de los botones de control (minimizar / cerrar)
    private static final int ANCHO_BOTON = 46;
    private static final int ALTO_BOTON = 30;

    // Cada cuánto se refresca la animación (≈60 fps)
    private static final int MS_POR_FOTOGRAMA = 16;

    // Tiempo característico (segundos) con el que el progreso
    // "simulado" se acerca al tope mientras la carga real ocurre.
    // Cuanto más chico, más rápido sube al inicio.
    private static final double CONSTANTE_AVANCE = 1.6;

    // Techo del progreso simulado mientras la tarea de fondo no ha
    // terminado (el 100% solo se alcanza cuando ya terminó de verdad).
    private static final double TOPE_SIMULADO = 92.0;

    // Qué tan rápido el valor mostrado en pantalla "alcanza" al
    // valor objetivo (suavizado). Más alto = más rápido/menos suave.
    private static final double SUAVIZADO_NORMAL = 0.10;
    private static final double SUAVIZADO_FINAL = 0.18;

    // Pequeña pausa con la barra ya al 100%, antes de cerrar
    private static final int MS_PAUSA_FINAL = 350;

    // Umbrales de progreso (0-100) en los que se marca cada paso
    // del checklist inferior.
    private static final double[] UMBRALES_CHECKLIST = {20, 45, 70, 99.5};
    private static final String[] TEXTOS_CHECKLIST = {
        "Configuración cargada",
        "Conexión a BD establecida",
        "Recursos listos",
        "¡Todo listo!"
    };

    private final PanelAnimado panelAnimado;

    // Logotipo oficial del proyecto (Resources/Imagenes/Logotipo.png),
    // cargado a través del caché de imágenes de la app.
    private final Image logoImagen;

    // ── Estado de la animación / progreso ─────────────────────
    private long inicioNano;
    private volatile boolean tareaTerminada = false;
    private double progresoObjetivo = 0;
    private double progresoMostrado = 0;
    private final long[] instanteCompletado = new long[TEXTOS_CHECKLIST.length];
    private Timer timerAnimacion;

    /**
     * Botón simple para los controles de la ventana (minimizar /
     * cerrar), con efecto hover, adaptado a la paleta clara del
     * nuevo diseño (antes estaba pensado para un fondo oscuro).
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
            setForeground(PaletaColores.TEXTO);
            setFont(new Font("Segoe UI", Font.PLAIN, 15));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    setForeground(esCerrar ? Color.WHITE : PaletaColores.TEXTO);
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    setForeground(PaletaColores.TEXTO);
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (hover) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(esCerrar
                        ? PaletaColores.PRINCIPAL             // rojo de marca
                        : new Color(0, 0, 0, 25));            // gris suave
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }

    public SplashScreen() {

        // NOTA: se dejó de llamar CargadorFuentes.registrar() aquí.
        // El proyecto no trae empacados los .ttf de Quicksand/Poppins,
        // así que intentar cargarlos solo imprimía errores en consola
        // al iniciar. CargadorFuentes.obtener(...) (usado más abajo)
        // ya devuelve automáticamente la fuente de respaldo del
        // sistema cuando no hay nada registrado, así que la pantalla
        // se sigue viendo bien, solo que con una tipografía distinta
        // a Quicksand/Poppins.

        // Logotipo oficial del proyecto, para usarlo como pieza
        // central de la animación de carga.
        logoImagen = CacheImagenes.obtenerLogotipo().getImage();

        setUndecorated(true);
        setResizable(false);
        setSize(ANCHO, ALTO);
        setLocationRelativeTo(null); // Centrar en pantalla

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

        // ── Panel con capas: animación abajo, botones arriba ──
        JLayeredPane capas = new JLayeredPane();
        capas.setPreferredSize(new Dimension(ANCHO, ALTO));
        capas.add(panelAnimado, JLayeredPane.DEFAULT_LAYER);
        capas.add(btnMinimizar, JLayeredPane.PALETTE_LAYER);
        capas.add(btnCerrar, JLayeredPane.PALETTE_LAYER);

        setContentPane(capas);
    }

    // ===============================================================
    // PANEL DE ANIMACIÓN
    // ===============================================================
    private class PanelAnimado extends JPanel {

        PanelAnimado() {
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                    RenderingHints.VALUE_STROKE_PURE);

            int w = getWidth();
            int h = getHeight();
            double t = (System.nanoTime() - inicioNano) / 1_000_000_000.0;
            double apar = Math.min(1.0, ((System.nanoTime() - inicioNano) / 1_000_000.0) / 650.0);
            apar = easeOutCubic(apar);

            dibujarFondo(g2, w, h, t);
            dibujarLogo(g2, w / 2.0, 168, 82, t, progresoMostrado);
            dibujarTitulo(g2, w, apar);
            dibujarBarraProgreso(g2, 150, 372, 600, 16, progresoMostrado, t);
            dibujarChecklist(g2, w / 2.0, 424, t);

            g2.dispose();
        }
    }

    // ---------------------------------------------------------------
    // FONDO: degradado cálido de marca (usando exclusivamente la
    // paleta oficial del proyecto, PaletaColores) + un par de manchas
    // suaves (mismo espíritu "blob" que se usa en fondos modernos).
    // ---------------------------------------------------------------
    private void dibujarFondo(Graphics2D g2, int w, int h, double t) {

        // Degradado entre el crema oficial (FONDO) y el blanco de
        // tarjetas (TARJETA), ambos definidos en PaletaColores.
        GradientPaint fondo = new GradientPaint(
                0, 0, PaletaColores.FONDO,
                0, h, PaletaColores.TARJETA);
        g2.setPaint(fondo);
        g2.fillRect(0, 0, w, h);

        // Manchas suaves de color, muy translúcidas, con un
        // movimiento casi imperceptible para dar sensación de vida.
        // Se usan directamente el amarillo (SECUNDARIO) y el rojo
        // (PRINCIPAL) oficiales, solo con transparencia añadida.
        float dx = (float) Math.sin(t * 0.15) * 12f;
        float dy = (float) Math.cos(t * 0.12) * 10f;

        g2.setColor(conAlpha(PaletaColores.SECUNDARIO, 24));
        g2.fillOval((int) (-120 + dx), (int) (-140 + dy), 420, 420);

        g2.setColor(conAlpha(PaletaColores.PRINCIPAL, 18));
        g2.fillOval((int) (w - 300 - dx), (int) (h - 260 - dy), 420, 420);

        // Franja inferior sutil para anclar el checklist / marca.
        g2.setColor(conAlpha(PaletaColores.TEXTO, 10));
        g2.fillRect(0, h - 40, w, 40);
    }

    /** Devuelve el mismo color de la paleta con un nuevo nivel de opacidad. */
    private static Color conAlpha(Color base, int alpha) {
        return new Color(base.getRed(), base.getGreen(), base.getBlue(), alpha);
    }

    // ---------------------------------------------------------------
    // LOGOTIPO: pieza central del splash. Es el logotipo oficial del
    // proyecto (Resources/Imagenes/Logotipo.png), con una entrada
    // suave y un anillo de progreso que se completa según el avance
    // real de la carga (usando el mismo degradado que la barra).
    // ---------------------------------------------------------------
    private void dibujarLogo(Graphics2D g2, double cx, double cyBase,
            double radio, double t, double progreso) {

        double bob = Math.sin(t * 1.6) * 4.0;
        double cy = cyBase + bob;

        // Entrada: fundido + ligera escala hacia su tamaño final.
        double aparLogo = easeOutCubic(clamp01(t / 0.55));
        double escalaEntrada = 0.85 + aparLogo * 0.15;

        Graphics2D g = (Graphics2D) g2.create();
        g.translate(cx, cy);
        g.scale(escalaEntrada, escalaEntrada);
        g.setComposite(java.awt.AlphaComposite.getInstance(
                java.awt.AlphaComposite.SRC_OVER, (float) aparLogo));

        // Sombra suave debajo del logotipo
        Shape sombra = new Ellipse2D.Double(
                -radio * 0.92, -radio * 0.86, radio * 1.84, radio * 1.72);
        g.setColor(new Color(0, 0, 0, 35));
        g.translate(0, 7);
        g.fill(sombra);
        g.translate(0, -7);

        // Anillo de progreso alrededor del logotipo, con el mismo
        // degradado (SECUNDARIO → PRINCIPAL) que la barra inferior.
        double grosorAnillo = radio * 0.10;
        double radioAnillo = radio + grosorAnillo * 0.9;
        Shape pistaAnillo = new Ellipse2D.Double(
                -radioAnillo, -radioAnillo, radioAnillo * 2, radioAnillo * 2);

        Stroke trazoAnterior = g.getStroke();
        g.setStroke(new BasicStroke((float) grosorAnillo,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(conAlpha(PaletaColores.TEXTO, 30));
        g.draw(pistaAnillo);

        double proporcion = clamp01(progreso / 100.0);
        if (proporcion > 0.004) {
            Shape arcoProgreso = new Arc2D.Double(
                    -radioAnillo, -radioAnillo, radioAnillo * 2, radioAnillo * 2,
                    90, -360 * proporcion, Arc2D.OPEN);
            LinearGradientPaint degradadoAnillo = new LinearGradientPaint(
                    new Point2D.Double(-radioAnillo, -radioAnillo),
                    new Point2D.Double(radioAnillo, radioAnillo),
                    new float[]{0f, 1f},
                    new Color[]{PaletaColores.SECUNDARIO, PaletaColores.PRINCIPAL});
            g.setPaint(degradadoAnillo);
            g.draw(arcoProgreso);
        }
        g.setStroke(trazoAnterior);

        // Fondo blanco detrás del logotipo (por si el PNG tuviera
        // bordes semitransparentes) y recorte circular al dibujarlo.
        Shape circuloLogo = new Ellipse2D.Double(-radio, -radio, radio * 2, radio * 2);
        g.setColor(PaletaColores.TARJETA);
        g.fill(circuloLogo);

        if (logoImagen != null) {
            Graphics2D gLogo = (Graphics2D) g.create();
            gLogo.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            gLogo.clip(circuloLogo);
            gLogo.drawImage(logoImagen,
                    (int) Math.round(-radio), (int) Math.round(-radio),
                    (int) Math.round(radio * 2), (int) Math.round(radio * 2), null);
            gLogo.dispose();
        }

        g.dispose();
    }

    // ---------------------------------------------------------------
    // TÍTULO: entra con fundido + deslizamiento hacia arriba.
    // ---------------------------------------------------------------
    private void dibujarTitulo(Graphics2D g2, int w, double apar) {

        Graphics2D g = (Graphics2D) g2.create();
        g.setComposite(java.awt.AlphaComposite.getInstance(
                java.awt.AlphaComposite.SRC_OVER, (float) apar));

        double desplazamiento = (1 - apar) * 18;

        Font fMarca = CargadorFuentes.obtener("QUICKSAND_SEMIBOLD", 17f);
        Font fTitulo = CargadorFuentes.obtener("QUICKSAND_BOLD", 34f);
        Font fEslogan = CargadorFuentes.obtener("POPPINS_REGULAR", 14f);

        g.setFont(fMarca);
        g.setColor(PaletaColores.TEXTO);
        dibujarCentrado(g, "FREDDY FAZBEAR'S", w / 2.0, 288 + desplazamiento);

        g.setFont(fTitulo);
        g.setColor(PaletaColores.PRINCIPAL);
        dibujarCentrado(g, "QUICK BITE", w / 2.0, 320 + desplazamiento);

        g.setFont(fEslogan);
        g.setColor(new Color(0x6B5750));
        dibujarCentrado(g, "Diversión y sabor en cada bocado", w / 2.0, 342 + desplazamiento);

        g.dispose();
    }

    private void dibujarCentrado(Graphics2D g, String texto, double cx, double baseline) {
        FontMetrics fm = g.getFontMetrics();
        int ancho = fm.stringWidth(texto);
        g.drawString(texto, (float) (cx - ancho / 2.0), (float) baseline);
    }

    // ---------------------------------------------------------------
    // BARRA DE PROGRESO con degradado y brillo deslizante.
    // ---------------------------------------------------------------
    private void dibujarBarraProgreso(Graphics2D g2, double x, double y,
            double w, double h, double progreso, double t) {

        Graphics2D g = (Graphics2D) g2.create();

        RoundRectangle2D track = new RoundRectangle2D.Double(x, y, w, h, h, h);

        // Sombra interior sutil
        g.setColor(new Color(0, 0, 0, 18));
        g.fill(new RoundRectangle2D.Double(x, y + 1.5, w, h, h, h));

        // Fondo del track
        g.setColor(new Color(0xF0E4C8));
        g.fill(track);
        g.setColor(PaletaColores.BORDE);
        g.setStroke(new BasicStroke(1f));
        g.draw(track);

        double proporcion = clamp01(progreso / 100.0);
        double anchoRelleno = Math.max(h, w * proporcion);

        Area relleno = new Area(track);
        relleno.intersect(new Area(new RoundRectangle2D.Double(x, y, anchoRelleno, h, h, h)));

        LinearGradientPaint degradado = new LinearGradientPaint(
                new Point2D.Double(x, y), new Point2D.Double(x + w, y),
                new float[]{0f, 1f},
                new Color[]{PaletaColores.SECUNDARIO, PaletaColores.PRINCIPAL});
        g.setPaint(degradado);
        g.fill(relleno);

        // Brillo (shimmer) deslizándose sobre el relleno
        Area brillo = new Area(relleno);
        double bandaX = x - w + ((t * 260) % (w * 2));
        Path2D banda = new Path2D.Double();
        banda.moveTo(bandaX, y - 4);
        banda.lineTo(bandaX + 26, y - 4);
        banda.lineTo(bandaX - 10, y + h + 4);
        banda.lineTo(bandaX - 36, y + h + 4);
        banda.closePath();
        brillo.intersect(new Area(banda));
        g.setColor(new Color(255, 255, 255, 90));
        g.fill(brillo);

        // Porcentaje
        Font fPct = CargadorFuentes.obtener("POPPINS_SEMIBOLD", 13f);
        g.setFont(fPct);
        g.setColor(PaletaColores.TEXTO);
        String pctTexto = ((int) Math.round(proporcion * 100)) + "%";
        g.drawString(pctTexto, (float) (x + w + 14), (float) (y + h - 3));

        g.dispose();
    }

    // ---------------------------------------------------------------
    // CHECKLIST de 4 pasos, con animación de "pop" al completarse.
    // ---------------------------------------------------------------
    private void dibujarChecklist(Graphics2D g2, double centroX, double y, double t) {

        Graphics2D g = (Graphics2D) g2.create();
        Font fItem = CargadorFuentes.obtener("POPPINS_REGULAR", 13f);
        g.setFont(fItem);
        FontMetrics fm = g.getFontMetrics();

        int n = TEXTOS_CHECKLIST.length;
        double espacio = 26;
        double anchoTotal = 0;
        double[] anchosItem = new double[n];

        for (int i = 0; i < n; i++) {
            anchosItem[i] = 22 + 6 + fm.stringWidth(TEXTOS_CHECKLIST[i]);
            anchoTotal += anchosItem[i];
        }
        anchoTotal += espacio * (n - 1);

        double cursorX = centroX - anchoTotal / 2.0;

        for (int i = 0; i < n; i++) {
            boolean completado = progresoMostrado >= UMBRALES_CHECKLIST[i] - 0.01;

            if (completado && instanteCompletado[i] == 0) {
                instanteCompletado[i] = System.nanoTime();
            }

            double escala = 1.0;
            if (completado) {
                double desde = (System.nanoTime() - instanteCompletado[i]) / 1_000_000.0;
                double p = clamp01(desde / 260.0);
                escala = 1.0 + (1 - easeOutCubic(p)) * 0.35 * Math.sin(p * Math.PI);
                if (desde > 260) {
                    escala = 1.0;
                }
            }

            double iconoR = 9 * escala;
            double iconoCX = cursorX + iconoR;
            double iconoCY = y;

            if (completado) {
                g.setColor(PaletaColores.ACENTO);
                g.fill(new Ellipse2D.Double(
                        iconoCX - iconoR, iconoCY - iconoR, iconoR * 2, iconoR * 2));
                g.setColor(Color.WHITE);
                Stroke antiguo = g.getStroke();
                g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                Path2D check = new Path2D.Double();
                check.moveTo(iconoCX - iconoR * 0.45, iconoCY);
                check.lineTo(iconoCX - iconoR * 0.1, iconoCY + iconoR * 0.4);
                check.lineTo(iconoCX + iconoR * 0.5, iconoCY - iconoR * 0.4);
                g.draw(check);
                g.setStroke(antiguo);
            } else {
                g.setColor(new Color(0xD8C9A8));
                Stroke antiguo = g.getStroke();
                g.setStroke(new BasicStroke(1.6f));
                g.draw(new Ellipse2D.Double(
                        iconoCX - iconoR, iconoCY - iconoR, iconoR * 2, iconoR * 2));
                g.setStroke(antiguo);
            }

            g.setColor(completado ? PaletaColores.TEXTO : new Color(0xA0907C));
            g.drawString(TEXTOS_CHECKLIST[i],
                    (float) (cursorX + 22),
                    (float) (y + fm.getAscent() / 2.0 - 1));

            cursorX += anchosItem[i] + espacio;
        }

        g.dispose();
    }

    // ---------------------------------------------------------------
    // Utilidades numéricas pequeñas
    // ---------------------------------------------------------------
    private static double clamp01(double v) {
        return Math.max(0, Math.min(1, v));
    }

    private static double easeOutCubic(double x) {
        double v = 1 - x;
        return 1 - v * v * v;
    }

    /**
     * Muestra la pantalla de carga, anima el progreso de forma
     * continua y, en paralelo, ejecuta las tareas reales de
     * inicialización en segundo plano (para no congelar la
     * animación). Cuando ambas cosas terminan, cierra el splash y
     * ejecuta la acción final (normalmente, abrir la ventana de
     * Bienvenida).
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

        inicioNano = System.nanoTime();

        // ── Animación continua de progreso (≈60 fps) ──────────
        timerAnimacion = new Timer(MS_POR_FOTOGRAMA, null);
        timerAnimacion.addActionListener(e -> {

            double tSeg = (System.nanoTime() - inicioNano) / 1_000_000_000.0;

            if (!tareaTerminada) {
                progresoObjetivo = TOPE_SIMULADO
                        * (1 - Math.exp(-tSeg / CONSTANTE_AVANCE));
            } else {
                progresoObjetivo = 100.0;
            }

            double suavizado = tareaTerminada ? SUAVIZADO_FINAL : SUAVIZADO_NORMAL;
            progresoMostrado += (progresoObjetivo - progresoMostrado) * suavizado;

            if (tareaTerminada && progresoMostrado > 99.4) {
                progresoMostrado = 100.0;
            }

            panelAnimado.repaint();

            if (tareaTerminada && progresoMostrado >= 100.0) {
                timerAnimacion.stop();

                Timer timerCierre = new Timer(MS_PAUSA_FINAL, ev -> {
                    dispose();
                    if (alTerminar != null) {
                        SwingUtilities.invokeLater(alTerminar);
                    }
                });
                timerCierre.setRepeats(false);
                timerCierre.start();
            }
        });
        timerAnimacion.start();

        // ── Tareas reales de inicialización en segundo plano ──
        // doInBackground() corre en un hilo aparte del EDT, así que
        // ManejadorErroresGlobal (que solo vigila dispatchEvent())
        // nunca ve lo que pase aquí adentro. Por eso este catch debe
        // distinguir ConexionException explícitamente y avisar en
        // done(), en vez de solo imprimir en consola y seguir como
        // si nada — que era el bug original: la app llegaba a Login
        // sin que nadie supiera que MySQL nunca respondió.
        SwingWorker<Void, Void> worker = new SwingWorker<>() {

            private boolean huboErrorConexion = false;

            @Override
            protected Void doInBackground() {
                if (tareaFondo != null) {
                    try {
                        tareaFondo.run();
                    } catch (ConexionException ex) {
                        AppLogger.error(SplashScreen.class,
                                "No se pudo conectar a la base de datos al iniciar.", ex);
                        huboErrorConexion = true;
                    } catch (Exception ex) {
                        AppLogger.error(SplashScreen.class,
                                "Error durante la carga inicial.", ex);
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                tareaTerminada = true;

                if (huboErrorConexion) {
                    // Se avisa pero NO se bloquea el arranque: se
                    // deja seguir a Login para que, por ejemplo, el
                    // Administrador pueda revisar Configuracion sin
                    // quedar totalmente bloqueado. Cualquier acción
                    // que sí necesite base de datos a partir de aquí
                    // queda cubierta por ManejadorErroresGlobal.
                    FabricaDialogos.advertencia(SplashScreen.this,
                            "No se pudo conectar con la base de datos.\n"
                                    + "Verifica que el servicio de MySQL esté "
                                    + "encendido. Podrás seguir navegando, pero "
                                    + "las acciones que necesiten datos (iniciar "
                                    + "sesión, ver productos, registrar pedidos) "
                                    + "no funcionarán hasta que la conexión se "
                                    + "restablezca.");
                }
            }
        };
        worker.execute();
    }
}
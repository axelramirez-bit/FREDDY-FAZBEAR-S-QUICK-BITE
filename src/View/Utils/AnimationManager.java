package View.Utils;

import java.awt.Color;
import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.Timer;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Administrador central de animaciones.
 *
 * Responsabilidades:
 *
 * • Animar componentes.
 * • Centralizar efectos visuales.
 * • Evitar código repetido.
 * • Mejorar la experiencia del usuario.
 *
 * Todas las animaciones del sistema deben realizarse desde aquí.
 * ===============================================================
 */
public final class AnimationManager {

    /**
     * Tiempo entre cada actualización.
     */
    private static final int FPS = 15;

    private AnimationManager() {
    }

    // ==========================================================
    // HOVER
    // ==========================================================

    /**
     * Cambia el color de fondo de un componente.
     *
     * @param componente Componente.
     * @param colorNuevo Nuevo color.
     */
    public static void cambiarColor(
            JComponent componente,
            Color colorNuevo) {

        componente.setBackground(colorNuevo);
        componente.repaint();

    }

    // ==========================================================
    // MOVER COMPONENTE
    // ==========================================================

    /**
     * Mueve suavemente un componente hasta una posición.
     *
     * @param componente Componente.
     * @param destinoX Posición X.
     * @param destinoY Posición Y.
     */
    public static void mover(
            JComponent componente,
            int destinoX,
            int destinoY) {

        Timer timer = new Timer(FPS, null);

        timer.addActionListener(e -> {

            Point p = componente.getLocation();

            int dx = destinoX - p.x;
            int dy = destinoY - p.y;

            if (Math.abs(dx) <= 2 && Math.abs(dy) <= 2) {

                componente.setLocation(destinoX, destinoY);

                timer.stop();

                return;

            }

            componente.setLocation(
                    p.x + dx / 5,
                    p.y + dy / 5
            );

        });

        timer.start();

    }

    // ==========================================================
    // DESLIZAR DESDE LA IZQUIERDA
    // ==========================================================

    /**
     * Desliza un componente desde la izquierda.
     *
     * @param componente Componente.
     * @param destinoX Posición final X.
     * @param destinoY Posición final Y.
     */
    public static void deslizarIzquierda(
            JComponent componente,
            int destinoX,
            int destinoY) {

        componente.setLocation(
                -componente.getWidth(),
                destinoY
        );

        mover(
                componente,
                destinoX,
                destinoY
        );

    }

    // ==========================================================
    // DESLIZAR DESDE LA DERECHA
    // ==========================================================

    /**
     * Desliza un componente desde la derecha.
     *
     * @param componente Componente.
     * @param destinoX Posición final.
     * @param destinoY Posición Y.
     */
    public static void deslizarDerecha(
            JComponent componente,
            int destinoX,
            int destinoY) {

        componente.setLocation(
                DisenoAdaptable.getAnchoPantalla(),
                destinoY
        );

        mover(
                componente,
                destinoX,
                destinoY
        );

    }

    // ==========================================================
    // EFECTO REBOTE
    // ==========================================================

    /**
     * Produce un pequeño efecto de rebote.
     *
     * @param componente Componente.
     */
    public static void rebote(JComponent componente) {

        Point original = componente.getLocation();

        Timer timer = new Timer(25, null);

        final int[] paso = {0};

        timer.addActionListener(e -> {

            switch (paso[0]) {

                case 0 ->
                    componente.setLocation(original.x, original.y - 5);

                case 1 ->
                    componente.setLocation(original.x, original.y);

                default ->
                    timer.stop();

            }

            paso[0]++;

        });

        timer.start();

    }

}
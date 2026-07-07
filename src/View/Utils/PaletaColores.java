package View.Utils;

import java.awt.*;

/**
 * Paleta de colores oficial del proyecto.
 * Todos los componentes de la interfaz deben utilizar
 * únicamente los colores definidos en esta clase.
 */
public final class PaletaColores {

    private PaletaColores() {
        // Evita crear instancias
    }

    // ==========================================================
    // COLORES PRINCIPALES
    // ==========================================================

    /** Color principal del sistema (Header, botones activos). 
     Rojo*/
    public static final Color PRINCIPAL = new Color(0xD62828);

    /** Color secundario (Botones, acciones principales). 
     Amarrillo*/
    public static final Color SECUNDARIO = new Color(0xF4C542);

    /** Fondo principal de la aplicación
     * Crema. */
    public static final Color FONDO = new Color(0xFFF8E8);

    /** Fondo de tarjetas, paneles y formularios. 
     Blanco*/
    public static final Color TARJETA = Color.WHITE;

    /** Color principal del texto. 
     Café oscuro*/
    public static final Color TEXTO = new Color(0x3D2C29);

    /** Color para estados positivos o destacados. 
     Verde*/
    public static final Color ACENTO = new Color(0x2E7D32);

    // ==========================================================
    // VARIANTES
    // ==========================================================

    /** Hover del color principal. */
    public static final Color PRINCIPAL_HOVER = PRINCIPAL.darker();

    /** Hover del color secundario. */
    public static final Color SECUNDARIO_HOVER = SECUNDARIO.darker();

    /** Fondo ligeramente más oscuro. */
    public static final Color FONDO_SECUNDARIO = FONDO.darker();

    /** Bordes de tarjetas y controles. */
    public static final Color BORDE = new Color(220, 220, 220);

    /** Color de sombras. */
    public static final Color SOMBRA = new Color(0, 0, 0, 35);

    /** Texto sobre fondo rojo. */
    public static final Color TEXTO_BLANCO = Color.WHITE;
}
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

    // ==========================================================
    // COLORES DE ESTADO (badges: Pendiente, Listo, Cancelado...)
    //
    // Estos son ADEMÁS de ACENTO/PRINCIPAL, no un reemplazo — se
    // agregan porque una tabla con 4-5 estados distintos (Pedidos,
    // Pagos, Promociones, Inventario) necesita más de un color
    // "positivo" y uno "negativo" para que cada estado se
    // distinga del resto de un vistazo.
    // ==========================================================

    /** Estado positivo/activo (Listo, Activa, Pagado, Disponible). */
    public static final Color ESTADO_EXITO = new Color(0x2E7D32);

    /** Fondo suave para el badge de éxito. */
    public static final Color ESTADO_EXITO_FONDO = new Color(0xE3F1E4);

    /** Estado de advertencia/en proceso (Preparación, Stock Bajo, Pendiente de pago). */
    public static final Color ESTADO_ADVERTENCIA = new Color(0xB8860B);

    /** Fondo suave para el badge de advertencia. */
    public static final Color ESTADO_ADVERTENCIA_FONDO = new Color(0xFCF1D6);

    /** Estado negativo (Cancelado, Rechazado, Agotado, Vencida). */
    public static final Color ESTADO_PELIGRO = new Color(0xC62828);

    /** Fondo suave para el badge de peligro. */
    public static final Color ESTADO_PELIGRO_FONDO = new Color(0xFAE1E1);

    /** Estado informativo/neutro (Programada, Pendiente inicial, Nuevo). */
    public static final Color ESTADO_INFO = new Color(0x1565C0);

    /** Fondo suave para el badge informativo. */
    public static final Color ESTADO_INFO_FONDO = new Color(0xE1EBFA);

    /** Estado apagado/sin actividad (Inactivo, Entregado ya cerrado). */
    public static final Color ESTADO_NEUTRO = new Color(0x616161);

    /** Fondo suave para el badge neutro. */
    public static final Color ESTADO_NEUTRO_FONDO = new Color(0xEAEAEA);
}
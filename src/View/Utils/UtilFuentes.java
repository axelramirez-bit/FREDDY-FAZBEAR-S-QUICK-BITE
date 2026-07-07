package View.Utils;

import java.awt.Font;

/**
 * Utilidad para gestionar las fuentes del sistema.
 *
 * Todas las vistas deben obtener las fuentes desde esta clase
 * para mantener una apariencia uniforme.
 *l
 */
public final class UtilFuentes {

    private UtilFuentes() {
    }

    // ==========================================================
    // FUENTE BASE
    // ==========================================================

    private static final String FUENTE = UIConstants.FUENTE;

    // ==========================================================
    // FUENTES PLANAS
    // ==========================================================

    public static Font pequeña() {
        return new Font(
                FUENTE,
                Font.PLAIN,
                UIConstants.FUENTE_PEQUEÑA
        );
    }

    public static Font normal() {
        return new Font(
                FUENTE,
                Font.PLAIN,
                UIConstants.FUENTE_NORMAL
        );
    }

    public static Font mediana() {
        return new Font(
                FUENTE,
                Font.PLAIN,
                UIConstants.FUENTE_MEDIANA
        );
    }

    public static Font titulo() {
        return new Font(
                FUENTE,
                Font.PLAIN,
                UIConstants.FUENTE_TITULO
        );
    }

    // ==========================================================
    // FUENTES NEGRITA
    // ==========================================================

    public static Font pequeñaNegrita() {
        return new Font(
                FUENTE,
                Font.BOLD,
                UIConstants.FUENTE_PEQUEÑA
        );
    }

    public static Font normalNegrita() {
        return new Font(
                FUENTE,
                Font.BOLD,
                UIConstants.FUENTE_NORMAL
        );
    }

    public static Font medianaNegrita() {
        return new Font(
                FUENTE,
                Font.BOLD,
                UIConstants.FUENTE_MEDIANA
        );
    }

    public static Font tituloNegrita() {
        return new Font(
                FUENTE,
                Font.BOLD,
                UIConstants.FUENTE_TITULO
        );
    }

    // ==========================================================
    // FUENTES PERSONALIZADAS
    // ==========================================================

    /**
     * Devuelve una fuente personalizada.
     *
     * @param estilo Font.PLAIN, Font.BOLD o Font.ITALIC
     * @param tamaño Tamaño de la fuente
     * @return Fuente personalizada
     */
    public static Font obtenerFuente(int estilo, int tamaño) {

        return new Font(
                FUENTE,
                estilo,
                tamaño
        );
    }

}
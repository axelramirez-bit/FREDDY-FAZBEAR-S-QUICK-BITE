package View.Utils;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Carga los archivos .ttf de Quicksand y Poppins desde
 * Resources/Fuentes/ y los deja disponibles para UtilFuentes.
 *
 * Por qué existe esta clase (y no simplemente escribir
 * new Font("Quicksand", Font.BOLD, 40)):
 *
 * Quicksand y Poppins son fuentes de Google Fonts, NO vienen
 * instaladas en Windows por defecto. Si un compañero de equipo no
 * las tiene instaladas en su sistema, Java sustituye silenciosamente
 * por una fuente genérica (Dialog) sin avisar — la app se vería
 * distinta en cada máquina del equipo. Al empacar los .ttf dentro
 * del proyecto y cargarlos aquí con Font.createFont(), la
 * apariencia queda garantizada igual en las 4 computadoras del
 * equipo, sin que nadie tenga que instalar nada.
 *
 * Además, Quicksand y Poppins vienen en archivos .ttf separados
 * por cada peso (Bold, SemiBold, Medium, Regular). Java no permite
 * pedir "Quicksand SemiBold" con new Font(nombre, estilo, tamaño)
 * de forma confiable cuando hay varios pesos de la misma familia
 * registrados — por eso cada peso se guarda como su propio Font
 * base, y UtilFuentes.deriveFont(tamaño) para obtenerlo del tamaño
 * que necesite.
 *
 * Uso: llamar UNA sola vez a CargadorFuentes.registrar(), antes de
 * crear cualquier ventana — normalmente la primera línea de
 * Main.main().
 * ===============================================================
 */
public final class CargadorFuentes {

    private CargadorFuentes() {
    }

    /**
     * Fuentes ya cargadas, en tamaño base (1pt), listas para
     * escalarse con .deriveFont(tamaño).
     */
    private static final Map<String, Font> FUENTES_BASE = new HashMap<>();

    // ==========================================================
    // REGISTRAR
    // ==========================================================
    /**
     * Carga y registra todas las fuentes del proyecto. Si se
     * llama más de una vez, la segunda llamada no hace nada (evita
     * volver a leer los archivos del disco/jar innecesariamente).
     */
    public static void registrar() {

        if (!FUENTES_BASE.isEmpty()) {
            return;
        }

        cargar("QUICKSAND_SEMIBOLD", "/Fuentes/Quicksand-SemiBold.ttf");
        cargar("QUICKSAND_BOLD", "/Fuentes/Quicksand-Bold.ttf");

        cargar("POPPINS_REGULAR", "/Fuentes/Poppins-Regular.ttf");
        cargar("POPPINS_MEDIUM", "/Fuentes/Poppins-Medium.ttf");
        cargar("POPPINS_SEMIBOLD", "/Fuentes/Poppins-SemiBold.ttf");
        cargar("POPPINS_BOLD", "/Fuentes/Poppins-Bold.ttf");
    }

    // ==========================================================
    // CARGA INDIVIDUAL
    // ==========================================================
    private static void cargar(String clave, String ruta) {

        try (InputStream flujo = CargadorFuentes.class.getResourceAsStream(ruta)) {

            if (flujo == null) {
                System.err.println(
                        "No se encontró la fuente: " + ruta
                        + " (revisa que esté dentro de src/Resources/Fuentes"
                        + " y que la carpeta esté marcada como recurso)."
                );
                return;
            }

            Font fuente = Font.createFont(Font.TRUETYPE_FONT, flujo);

            GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .registerFont(fuente);

            FUENTES_BASE.put(clave, fuente);

        } catch (Exception e) {

            System.err.println(
                    "Error cargando la fuente " + ruta
                    + ": " + e.getMessage()
            );
        }
    }

    // ==========================================================
    // OBTENER
    // ==========================================================
    /**
     * Devuelve la fuente solicitada en el tamaño indicado.
     *
     * Si la fuente no se pudo cargar (archivo faltante, o
     * registrar() nunca se llamó), devuelve la fuente de respaldo
     * del sistema en vez de lanzar una excepción — así un recurso
     * faltante nunca tumba la aplicación completa, solo se ve
     * ligeramente distinto hasta que se corrija.
     *
     * @param clave   Una de las claves cargadas en registrar()
     *                (ej. "QUICKSAND_BOLD").
     * @param tamaño  Tamaño en puntos.
     * @return Font lista para usar.
     */
    public static Font obtener(String clave, float tamaño) {

        Font base = FUENTES_BASE.get(clave);

        if (base == null) {
            return new Font(
                    UIConstants.FUENTE_RESPALDO,
                    Font.PLAIN,
                    (int) tamaño
            );
        }

        return base.deriveFont(tamaño);
    }

}
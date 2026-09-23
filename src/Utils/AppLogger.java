// Paquete Utils: utilidades generales
package Utils;

// Importa Level (niveles de log)
import java.util.logging.Level;
// Importa Logger
import java.util.logging.Logger;

/**
 * ===============================================================
 * Utilidad centralizada de logging.
 * ---------------------------------------------------------------
 * Reemplaza el uso disperso de e.printStackTrace() en la capa DAO.
 * Ventajas frente a printStackTrace():
 *  - Se puede redirigir a archivo (ver logging.properties) sin
 *    tocar el código.
 *  - Se puede subir/bajar el nivel de detalle por clase.
 *  - No se "traga" el error silenciosamente en producción: queda
 *    registrado con fecha, clase y stacktrace completo.
 * ===============================================================
 */
// Clase final de logging centralizado
public final class AppLogger {

    // Constructor privado: no se instancia
    private AppLogger() {
    }

    // Devuelve el Logger de la clase indicada
    public static Logger get(Class<?> clase) {
        // Obtiene el logger por el nombre de la clase
        return Logger.getLogger(clase.getName());
    }

    // Registra un error grave con su excepción
    public static void error(Class<?> clase, String mensaje, Throwable error) {
        // Escribe en nivel SEVERE con mensaje y causa
        get(clase).log(Level.SEVERE, mensaje, error);
    }

    // Registra un aviso
    public static void aviso(Class<?> clase, String mensaje) {
        // Escribe en nivel WARNING
        get(clase).log(Level.WARNING, mensaje);
    }

    // Registra información general
    public static void info(Class<?> clase, String mensaje) {
        // Escribe en nivel INFO
        get(clase).log(Level.INFO, mensaje);
    }
}

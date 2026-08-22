package Utils;

import java.util.logging.Level;
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
public final class AppLogger {

    private AppLogger() {
    }

    public static Logger get(Class<?> clase) {
        return Logger.getLogger(clase.getName());
    }

    public static void error(Class<?> clase, String mensaje, Throwable error) {
        get(clase).log(Level.SEVERE, mensaje, error);
    }

    public static void aviso(Class<?> clase, String mensaje) {
        get(clase).log(Level.WARNING, mensaje);
    }

    public static void info(Class<?> clase, String mensaje) {
        get(clase).log(Level.INFO, mensaje);
    }
}

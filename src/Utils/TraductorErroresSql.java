package Utils;

import Excepciones.AccesoDatosException;
import Excepciones.AppException;
import Excepciones.ReglaNegocioException;

import java.sql.SQLException;

/**
 * Convierte una SQLException en una AppException con un mensaje claro.
 *
 * Uso típico en un DAO (en lugar de "log + return false"):
 *
 *   } catch (SQLException e) {
 *       throw TraductorErroresSql.traducir(getClass(), "No se pudo guardar el pedido.", e);
 *   }
 *
 * Así la vista sabe POR QUÉ falló (duplicado, stock, registro en uso,
 * sin conexión...) y no solo que "algo salió mal".
 */
public final class TraductorErroresSql {

    // Códigos de error de MySQL que nos interesan
    private static final int DUPLICADO = 1062;           // UNIQUE repetido
    private static final int FK_HIJOS = 1451;            // no se puede borrar: lo usan otros
    private static final int FK_PADRE = 1452;            // el registro relacionado no existe
    private static final int CHECK_VIOLADO = 3819;       // CHECK (ej. stock >= 0)
    private static final int BLOQUEO_TIMEOUT = 1205;     // lock wait timeout
    private static final int INTERBLOQUEO = 1213;        // deadlock

    private TraductorErroresSql() {
    }

    public static AppException traducir(Class<?> origen, String accion, SQLException e) {

        // Siempre deja el detalle técnico completo en el log
        AppLogger.error(origen, accion, e);

        int codigo = e.getErrorCode();
        String estado = e.getSQLState() == null ? "" : e.getSQLState();
        String texto = e.getMessage() == null ? "" : e.getMessage().toLowerCase();

        if (codigo == DUPLICADO) {
            return new ReglaNegocioException(
                    "Ya existe un registro con ese valor (dato duplicado).", e);
        }

        if (codigo == FK_HIJOS) {
            return new ReglaNegocioException(
                    "No se puede eliminar: otros registros dependen de este. "
                    + "Puedes desactivarlo en su lugar.", e);
        }

        if (codigo == FK_PADRE) {
            return new ReglaNegocioException(
                    "Un dato relacionado ya no existe. Actualiza la pantalla e inténtalo de nuevo.", e);
        }

        if (codigo == CHECK_VIOLADO) {
            if (texto.contains("stock")) {
                return new ReglaNegocioException(
                        "No hay stock suficiente para completar la operación.", e);
            }
            return new ReglaNegocioException(
                    "Un valor no cumple las reglas de la base de datos.", e);
        }

        if (codigo == BLOQUEO_TIMEOUT || codigo == INTERBLOQUEO) {
            return new AccesoDatosException(
                    "La base de datos está ocupada. Inténtalo de nuevo en unos segundos.", e);
        }

        // 08xxx = error de conexión (por ejemplo 08S01: enlace de comunicaciones caído)
        if (estado.startsWith("08")) {
            return new AccesoDatosException(
                    "No hay conexión con la base de datos. "
                    + "Verifica que MySQL esté encendido.", e);
        }

        return new AccesoDatosException(
                accion + " Ocurrió un error en la base de datos.", e);
    }
}


package Config;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ========================================================================
 * REDDY-FAZBEAR'S QUICK BITE
 * ------------------------------------------------------------------------
 * Arquitectura de Configuración Centralizada.
 * ========================================================================
 */
public final class Configuracion {

    // Cambiado a la ruta solicitada por el usuario
    private static final String ARCHIVO_CONFIGURACION =
        "config.properties";
    private static final Properties PROPIEDADES = new Properties();

    private Configuracion() { }

    static {
        cargarConfiguracion();
    }

    // ─── MÉTODOS DE CARGA Y PROCESAMIENTO INTERNO ──────────────────────────

    private static void cargarConfiguracion() {
        // Implementado el bloque try-with-resources exactamente como lo solicitaste
        try (InputStream input = Configuracion.class.getClassLoader().getResourceAsStream(ARCHIVO_CONFIGURACION)) {
            if (input == null) {
                throw new RuntimeException("No se encontró el archivo de configuración: " + ARCHIVO_CONFIGURACION);
            }
            PROPIEDADES.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error fatal al leer el archivo config.properties.", e);
        }
    }

    private static String obtener(String clave) {
        String valor = PROPIEDADES.getProperty(clave);
        if (valor == null || valor.isBlank()) {
            throw new RuntimeException("No existe la propiedad requerida: " + clave);
        }
        return valor.trim();
    }

    /**
     * Permite obtener dinámicamente cualquier propiedad fuera de las predefinidas.
     */
    public static String getPropiedad(String clave) {
        return obtener(clave);
    }

    // ─── BLOQUE: BASE DE DATOS ──────────────────────────────────────────────

    public static String getHost() { return obtener("db.host"); }
    public static String getPuerto() { return obtener("db.port"); }
    public static String getBaseDatos() { return obtener("db.name"); }
    public static String getUsuario() { return obtener("db.user"); }
    public static String getPassword() { return obtener("db.password"); }

    public static String getUrl() {
        return "jdbc:mysql://" + getHost() + ":" + getPuerto() + "/" + getBaseDatos() 
             + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8&connectTimeout=5000&socketTimeout=5000";
    }

    // ─── BLOQUE: APLICACIÓN ─────────────────────────────────────────────────

    public static String getNombreAplicacion() { return obtener("app.name"); }
    public static String getVersionAplicacion() { return obtener("app.version"); }
    public static String getEmpresa() { return obtener("app.empresa"); }

    // ─── BLOQUE: INTERFAZ ───────────────────────────────────────────────────

    public static String getTema() { return obtener("ui.theme"); }
    public static String getIdioma() { return obtener("ui.language"); }
    public static boolean pantallaCompleta() { return Boolean.parseBoolean(obtener("ui.fullscreen")); }

    // ─── BLOQUE: LOGGER ─────────────────────────────────────────────────────

    public static String getNivelLog() { return obtener("log.level"); }
}
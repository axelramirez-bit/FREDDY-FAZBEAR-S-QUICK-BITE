
// Paquete Config
package Config;
// Importa IOException (errores de lectura)
import java.io.IOException;
// Importa InputStream para leer el archivo
import java.io.InputStream;
// Importa Properties para leer pares clave=valor
import java.util.Properties;

/**
 * ========================================================================
 * REDDY-FAZBEAR'S QUICK BITE
 * ------------------------------------------------------------------------
 * Arquitectura de Configuración Centralizada.
 * ========================================================================
 */
// Clase final con acceso estático a la configuración
public final class Configuracion {

    // Cambiado a la ruta solicitada por el usuario
    // Nombre del archivo de configuración
    private static final String ARCHIVO_CONFIGURACION =
        "config.properties";
    // Almacén de propiedades cargadas
    private static final Properties PROPIEDADES = new Properties();

    // Constructor privado: la clase no se instancia
    private Configuracion() { }

    // Bloque estático: corre una sola vez al cargar la clase
    static {
        // Carga el archivo de configuración
        cargarConfiguracion();
    }

    // ─── MÉTODOS DE CARGA Y PROCESAMIENTO INTERNO ──────────────────────────

    // Lee config.properties desde el classpath
    private static void cargarConfiguracion() {
        // Implementado el bloque try-with-resources exactamente como lo solicitaste
        // Abre el archivo como flujo (se cierra solo)
        try (InputStream input = Configuracion.class.getClassLoader().getResourceAsStream(ARCHIVO_CONFIGURACION)) {
            // Si no se encontró el archivo
            if (input == null) {
                // Lanza error indicando cuál archivo falta
                throw new RuntimeException("No se encontró el archivo de configuración: " + ARCHIVO_CONFIGURACION);
            }
            // Carga las propiedades desde el flujo
            PROPIEDADES.load(input);
        // Si hay error de lectura
        } catch (IOException e) {
            // Lanza error fatal con la causa
            throw new RuntimeException("Error fatal al leer el archivo config.properties.", e);
        }
    }

    // Devuelve el valor de una clave obligatoria
    private static String obtener(String clave) {
        // Busca el valor de la clave
        String valor = PROPIEDADES.getProperty(clave);
        // Si es nulo o está vacío
        if (valor == null || valor.isBlank()) {
            // Lanza error con el nombre de la clave
            throw new RuntimeException("No existe la propiedad requerida: " + clave);
        }
        // Devuelve el valor sin espacios sobrantes
        return valor.trim();
    }

    /**
     * Permite obtener dinámicamente cualquier propiedad fuera de las predefinidas.
     */
    // Obtiene cualquier propiedad por su clave
    public static String getPropiedad(String clave) {
        // Delega en obtener()
        return obtener(clave);
    }

    // ─── BLOQUE: BASE DE DATOS ──────────────────────────────────────────────

    // Host de la base de datos
    public static String getHost() { return obtener("db.host"); }
    // Puerto de la base de datos
    public static String getPuerto() { return obtener("db.port"); }
    // Nombre de la base de datos
    public static String getBaseDatos() { return obtener("db.name"); }
    // Usuario de la base de datos
    public static String getUsuario() { return obtener("db.user"); }
    // Contraseña de la base de datos
    public static String getPassword() { return obtener("db.password"); }

    // Arma la URL JDBC de MySQL
    public static String getUrl() {
        // Une host, puerto y nombre de la base
        return "jdbc:mysql://" + getHost() + ":" + getPuerto() + "/" + getBaseDatos() 
             // Agrega parámetros: sin SSL, zona UTC, UTF-8 y timeouts de 5 s
             + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8&connectTimeout=5000&socketTimeout=5000";
    }

    // ─── BLOQUE: APLICACIÓN ─────────────────────────────────────────────────

    // Nombre de la aplicación
    public static String getNombreAplicacion() { return obtener("app.name"); }
    // Versión de la aplicación
    public static String getVersionAplicacion() { return obtener("app.version"); }
    // Empresa
    public static String getEmpresa() { return obtener("app.empresa"); }

    // ─── BLOQUE: INTERFAZ ───────────────────────────────────────────────────

    // Tema visual configurado
    public static String getTema() { return obtener("ui.theme"); }
    // Idioma configurado
    public static String getIdioma() { return obtener("ui.language"); }
    // Indica si va en pantalla completa (texto a boolean)
    public static boolean pantallaCompleta() { return Boolean.parseBoolean(obtener("ui.fullscreen")); }

    // ─── BLOQUE: LOGGER ─────────────────────────────────────────────────────

    // Nivel de log configurado
    public static String getNivelLog() { return obtener("log.level"); }
}
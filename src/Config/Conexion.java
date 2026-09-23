// Paquete Config: configuración y conexión a la base de datos
package Config;

// Importa el logger
import Utils.AppLogger;

// Importa InvocationHandler para crear el proxy dinámico
import java.lang.reflect.InvocationHandler;
// Importa la excepción que envuelve errores al invocar métodos
import java.lang.reflect.InvocationTargetException;
// Importa Method para representar el método invocado
import java.lang.reflect.Method;
// Importa Proxy para crear el proxy de Connection
import java.lang.reflect.Proxy;
// Importa Connection (conexión JDBC)
import java.sql.Connection;
// Importa DriverManager para abrir la conexión
import java.sql.DriverManager;
// Importa SQLException (errores SQL)
import java.sql.SQLException;

/**
 * ===============================================================
 * Clase encargada de administrar la conexión con MySQL.
 *
 * Patrón utilizado: Singleton
 *
 * Responsabilidades: - Crear una única conexión. - Verificar disponibilidad. -
 * Reconectar si es necesario. - Cerrar conexión correctamente.
 *
 * Esta clase pertenece a la capa Config. No contiene lógica de negocio.
 * ---------------------------------------------------------------
 * NOTA DE DISEÑO (corrección):
 *
 * Todos los DAO usan el patrón:
 *
 *   try (Connection con = Conexion.getInstancia().getConexion(); ...) { ... }
 *
 * Como Connection es un recurso de try-with-resources, al salir del
 * bloque se llama con.close() automáticamente. Si eso cerrara la
 * conexión real, el singleton perdería su propósito: cada consulta
 * dejaría la conexión cerrada y la siguiente tendría que reconectar
 * desde cero (abrir socket TCP + handshake con MySQL) antes de poder
 * ejecutar un simple SELECT.
 *
 * Para que los DAO puedan seguir escribiéndose igual (con
 * try-with-resources, que es buena práctica) SIN que eso mate la
 * conexión compartida, getConexion() no devuelve la conexión real:
 * devuelve un proxy que reenvía todos los métodos a la conexión real,
 * excepto close(), que no hace nada. La conexión real solo se cierra
 * de verdad con cerrarConexion() (pensado para llamarse al cerrar la
 * aplicación, por ejemplo desde main.java).
 * ===============================================================
 */
// Clase final (no heredable) que administra la conexión a MySQL
public final class Conexion {

    // Singleton
    // Única instancia de la clase (patrón Singleton)
    private static Conexion instancia;

    // Conexión real hacia MySQL (una sola, se reutiliza)
    // Conexión real hacia MySQL
    private Connection conexionReal;

    // Proxy que se entrega a los DAO: mismo comportamiento que
    // Connection, salvo que close() no cierra nada de verdad.
    // Proxy que se entrega a los DAO
    private Connection conexionProxy;

    // Constructor. Impide new Conexion().
    // Constructor privado: impide crear objetos desde fuera
    private Conexion() {
        // Abre la conexión al crear la instancia
        conectar();
    }

    // Crear conexión y abrirla
    // Abre la conexión real (synchronized evita hilos simultáneos)
    private synchronized void conectar() {
        // Intenta establecer una conexión con la base de datos.
        // Si ocurre algún error, lanza una ConexionException.
        // Inicia el bloque que captura errores de conexión
        try {

            // Carga el Driver de MySQL. Hace que Java sepa cómo comunicarse con MySQL.
            // Carga el driver JDBC de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Crea una conexión JDBC utilizando los parámetros
            // obtenidos desde Configuracion.java.
            // Abre la conexión con URL, usuario y contraseña
            conexionReal = DriverManager.getConnection(
                    // URL de conexión desde Configuracion
                    Configuracion.getUrl(),
                    // Usuario de la base de datos
                    Configuracion.getUsuario(),
                    // Contraseña de la base de datos
                    Configuracion.getPassword()
            );

            // Se reconstruye el proxy cada vez que hay conexión real nueva.
            // Crea el proxy que ignora close()
            conexionProxy = crearProxyNoCerrable(conexionReal);

            // Registra en el log que conectó
            AppLogger.info(Conexion.class, "Conectado a MySQL correctamente.");

            // Si no se usa/no encuentra el Driver.
        // Si no se encuentra el driver
        } catch (ClassNotFoundException e) {

            // Lanza ConexionException con la causa
            throw new ConexionException("No fue posible conectar", e);

            // Si ocurre el error
        // Si falla la conexión SQL
        } catch (SQLException e) {

            // Lanza ConexionException con la causa
            throw new ConexionException("No fue posible conectar", e);

        }

    }

    /**
     * Envuelve la conexión real en un Proxy dinámico que reenvía todo
     * a la conexión real, salvo close(), que se ignora. Así los DAO
     * pueden seguir haciendo:
     *
     *   try (Connection con = Conexion.getInstancia().getConexion()) { ... }
     *
     * sin cerrar la conexión compartida en cada consulta.
     */
    // Crea un proxy de Connection cuyo close() no hace nada
    private Connection crearProxyNoCerrable(Connection real) {

        // Define el manejador que intercepta cada llamada al proxy
        InvocationHandler manejador = (Object proxy, Method metodo, Object[] args) -> {

            // Si el método llamado es close()
            if ("close".equals(metodo.getName())) {
                // No cerramos la conexión real; el ciclo de vida lo
                // controla únicamente Conexion.cerrarConexion().
                // No cierra nada y devuelve null
                return null;
            }

            // Intenta reenviar la llamada a la conexión real
            try {
                // Ejecuta el método en la conexión real y devuelve su resultado
                return metodo.invoke(real, args);

            // Si el método lanzó una excepción
            } catch (InvocationTargetException e) {
                // Desenvuelve la excepción original (por ejemplo SQLException)
                // en vez de dejarla envuelta en InvocationTargetException.
                // Relanza la excepción original (la causa) sin envoltorio
                throw e.getCause() != null ? e.getCause() : e;
            }
        };

        // Crea y devuelve el proxy como Connection
        return (Connection) Proxy.newProxyInstance(
                // Usa el class loader de Connection
                Connection.class.getClassLoader(),
                // Indica que el proxy implementa Connection
                new Class<?>[]{Connection.class},
                // Usa el manejador definido arriba
                manejador
        );
    }

    // Singleton: evita que se creen más objetos de conexión al mismo tiempo.
    // Devuelve la única instancia (synchronized)
    public static synchronized Conexion getInstancia() {

        // Si aún no existe la instancia
        if (instancia == null) {
            // La crea
            instancia = new Conexion();
        }

        // Devuelve la instancia
        return instancia;

    }

    /**
     * Obtener conexión (proxy) lista para usar.
     * Verifica que la conexión real siga viva y reconecta si hiciera falta.
     */
    // Entrega la conexión (proxy) lista para usar
    public Connection getConexion() {

        // Inicia bloque para capturar errores SQL
        try {

            // Si no hay conexión real
            if (conexionReal == null
                    // o está cerrada
                    || conexionReal.isClosed()
                    // o no responde en 2 segundos
                    || !conexionReal.isValid(2)) {

                // Reconecta
                conectar();

            }

        // Si falla la verificación
        } catch (SQLException e) {

            // Lanza ConexionException con la causa
            throw new ConexionException("No fue posible conectar", e);

        }

        // Devuelve el proxy
        return conexionProxy;

    }

    // Verificar conexión
    // Indica si la conexión está viva
    public boolean estaConectado() {

        // Inicia bloque para capturar errores SQL
        try {
            // Verdadero si existe
            return conexionReal != null
                    // y no está cerrada
                    && !conexionReal.isClosed()
                    // y responde en 2 segundos
                    && conexionReal.isValid(2);

        // Si hay error SQL
        } catch (SQLException e) {

            // Considera la conexión no disponible (false)
            return false;

        }

    }

    /**
     * Cierra la conexión REAL. A diferencia de con.close() llamado desde
     * un DAO (que ahora no hace nada), este método sí cierra el socket
     * hacia MySQL de verdad. Pensado para invocarse al cerrar la
     * aplicación (por ejemplo, en un shutdown hook desde main.java).
     */
    // Cierra la conexión REAL
    public void cerrarConexion() {

        // Inicia bloque para capturar errores SQL
        try {

            // Si existe y sigue abierta
            if (conexionReal != null && !conexionReal.isClosed()) {

                // Cierra la conexión real con MySQL
                conexionReal.close();
                // Limpia la referencia a la conexión real
                conexionReal = null;
                // Limpia el proxy
                conexionProxy = null;
                // Reinicia el singleton para poder reconectar después
                instancia = null;

                // Registra el cierre en el log
                AppLogger.info(Conexion.class, "Conexión cerrada.");

            }

        // Si falla al cerrar
        } catch (SQLException e) {

            // Lanza ConexionException con la causa
            throw new ConexionException("No fue posible conectar", e);

        }

    }

    // Reiniciar conexión
    // Reinicia la conexión (synchronized)
    public synchronized void reiniciarConexion() {

        // Cierra la conexión actual
        cerrarConexion();
        // Abre una conexión nueva
        conectar();

    }

    // Devuelve el estado como texto
    public String getEstado() {

        // Si está conectado
        if (estaConectado()) {
            // Devuelve "Conectado"
            return "Conectado";
        }

        // Devuelve "Desconectado"
        return "Desconectado";

    }
}

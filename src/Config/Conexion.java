package Config;

import Utils.AppLogger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
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
public final class Conexion {

    // Singleton
    private static Conexion instancia;

    // Conexión real hacia MySQL (una sola, se reutiliza)
    private Connection conexionReal;

    // Proxy que se entrega a los DAO: mismo comportamiento que
    // Connection, salvo que close() no cierra nada de verdad.
    private Connection conexionProxy;

    // Constructor. Impide new Conexion().
    private Conexion() {
        conectar();
    }

    // Crear conexión y abrirla
    private synchronized void conectar() {
        // Intenta establecer una conexión con la base de datos.
        // Si ocurre algún error, lanza una ConexionException.
        try {

            // Carga el Driver de MySQL. Hace que Java sepa cómo comunicarse con MySQL.
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Crea una conexión JDBC utilizando los parámetros
            // obtenidos desde Configuracion.java.
            conexionReal = DriverManager.getConnection(
                    Configuracion.getUrl(),
                    Configuracion.getUsuario(),
                    Configuracion.getPassword()
            );

            // Se reconstruye el proxy cada vez que hay conexión real nueva.
            conexionProxy = crearProxyNoCerrable(conexionReal);

            AppLogger.info(Conexion.class, "Conectado a MySQL correctamente.");

            // Si no se usa/no encuentra el Driver.
        } catch (ClassNotFoundException e) {

            throw new ConexionException("No fue posible conectar", e);

            // Si ocurre el error
        } catch (SQLException e) {

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
    private Connection crearProxyNoCerrable(Connection real) {

        InvocationHandler manejador = (Object proxy, Method metodo, Object[] args) -> {

            if ("close".equals(metodo.getName())) {
                // No cerramos la conexión real; el ciclo de vida lo
                // controla únicamente Conexion.cerrarConexion().
                return null;
            }

            try {
                return metodo.invoke(real, args);

            } catch (InvocationTargetException e) {
                // Desenvuelve la excepción original (por ejemplo SQLException)
                // en vez de dejarla envuelta en InvocationTargetException.
                throw e.getCause() != null ? e.getCause() : e;
            }
        };

        return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class<?>[]{Connection.class},
                manejador
        );
    }

    // Singleton: evita que se creen más objetos de conexión al mismo tiempo.
    public static synchronized Conexion getInstancia() {

        if (instancia == null) {
            instancia = new Conexion();
        }

        return instancia;

    }

    /**
     * Obtener conexión (proxy) lista para usar.
     * Verifica que la conexión real siga viva y reconecta si hiciera falta.
     */
    public Connection getConexion() {

        try {

            if (conexionReal == null
                    || conexionReal.isClosed()
                    || !conexionReal.isValid(2)) {

                conectar();

            }

        } catch (SQLException e) {

            throw new ConexionException("No fue posible conectar", e);

        }

        return conexionProxy;

    }

    // Verificar conexión
    public boolean estaConectado() {

        try {
            return conexionReal != null
                    && !conexionReal.isClosed()
                    && conexionReal.isValid(2);

        } catch (SQLException e) {

            return false;

        }

    }

    /**
     * Cierra la conexión REAL. A diferencia de con.close() llamado desde
     * un DAO (que ahora no hace nada), este método sí cierra el socket
     * hacia MySQL de verdad. Pensado para invocarse al cerrar la
     * aplicación (por ejemplo, en un shutdown hook desde main.java).
     */
    public void cerrarConexion() {

        try {

            if (conexionReal != null && !conexionReal.isClosed()) {

                conexionReal.close();
                conexionReal = null;
                conexionProxy = null;
                instancia = null;

                AppLogger.info(Conexion.class, "Conexión cerrada.");

            }

        } catch (SQLException e) {

            throw new ConexionException("No fue posible conectar", e);

        }

    }

    // Reiniciar conexión
    public synchronized void reiniciarConexion() {

        cerrarConexion();
        conectar();

    }

    public String getEstado() {

        if (estaConectado()) {
            return "Conectado";
        }

        return "Desconectado";

    }
}

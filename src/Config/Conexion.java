package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * =============================================================== Clase
 * encargada de administrar la conexión con MySQL.
 *
 * Patrón utilizado: Singleton
 *
 * Responsabilidades: - Crear una única conexión. - Verificar disponibilidad. -
 * Reconectar si es necesario. - Cerrar conexión correctamente.
 *
 * Esta clase pertenece a la capa Config. No contiene lógica de negocio.
 * ===============================================================
 */
public final class Conexion {

    // Singleton
    private static Conexion instancia;
    // Conexión
    private Connection conexion;

    // Constructor  Impidenew Conexion().
    private Conexion() {

        conectar();

    }

    // Crear conexión y abrirla
    private synchronized void conectar() {
        // Intenta establecer una conexión con la base de datos.
// Si ocurre algún error, lanza una ConexionException.
        try {

            // Cartga el Driver de MySQL Hace que Java sepa cómo comunicarse con MySQL.
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Crea una conexión JDBC utilizando los parámetros
// obtenidos desde Configuracion.java.
            conexion = DriverManager.getConnection(
                    Configuracion.getUrl(),
                    Configuracion.getUsuario(),
                    Configuracion.getPassword()
            );
            // Muestra mensaje en la run
            System.out.println(
                    "✓ Conectado a MySQL correctamente."
            );
            // Si no se usa no encuentra el Driver.
        } catch (ClassNotFoundException e) {

            throw new ConexionException(
                    "No fue posible conectar",
                    e
            );
            // Si ocurre el error 
        } catch (SQLException e) {

            throw new ConexionException(
                    "No fue posible conectar",
                    e
            );

        }

    }

    // Singleton--Evita que se cren mas objetos de conexion al mismo tiempo.
    public static synchronized Conexion getInstancia() {
        // Si esta vacua
        if (instancia == null) {
            // Crea la conexion
            instancia = new Conexion();

        }
        // Devuelve la instancia 
        return instancia;

    }

    // Obtener conexión
    public Connection getConexion() {
        // Intenta verificar la conexion 
        try {

            if (conexion == null // No se ha creado
                    || conexion.isClosed() // Fue Cerrada
                    || !conexion.isValid(2)) {// Pregunta a MySQL si esta activo // el numero 2 hace que la espara se de 2S
                /// Si responde a una de estas tres condiciones crea la oonexion

            conectar();

            }
            // si ocurre un error en verificar la conexion
        } catch (SQLException e) {

            throw new ConexionException(
                    "No fue posible conectar",
                    e
            );

        }
        // Retorna la conexion
        return conexion;

    }

    // Verificar conexión
    public boolean estaConectado() {

        try {
            // Intenta Retornar si Exite la conexion y si No esta cerrada
            return conexion != null
                    && !conexion.isClosed()
                    && conexion.isValid(2);
            // Si no se puede retorna falso
        } catch (SQLException e) {

            return false;

        }

    }

    // Cerrar conexión
    public void cerrarConexion() {

        try {
            // Verifica si esta creada y abierta la conexion
            if (conexion != null && !conexion.isClosed()) {
                // Si es true la condicion cierra la conexion y la vacia 
                conexion.close();

                conexion = null;

                instancia = null;
                // Mensaje de veificacion que se cerro
                System.out.println(
                        "Conexión cerrada."
                );

            }
            // si no se puede cerrar
        } catch (SQLException e) {

            throw new ConexionException(
                    "No fue posible conectar",
                    e
            );

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

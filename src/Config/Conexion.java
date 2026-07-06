package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Conexion {

    // ==========================
    // Singleton
    // ==========================
    private static Conexion instancia;

    // ==========================
    // Conexión
    // ==========================
    private Connection conexion;

    // ==========================
    // Configuración MySQL
    // ==========================
    private static final String URL =
            "jdbc:mysql://localhost:3306/FreddyQuickBite"
            + "?useSSL=false"
            + "&allowPublicKeyRetrieval=true"
            + "&serverTimezone=UTC"
            + "&characterEncoding=UTF-8";

    private static final String USER = "root";

    private static final String PASSWORD = "admin";

    // ==========================
    // Constructor privado
    // ==========================
    private Conexion() {

        conectar();

    }

    // ==========================
    // Crear conexión
    // ==========================
    private void conectar() {

        try {

            conexion = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

            System.out.println(
                    "✓ Conectado a MySQL correctamente."
            );

        } catch (SQLException e) {

            throw new RuntimeException(
                    "No fue posible conectar con MySQL.",
                    e
            );

        }

    }

    // ==========================
    // Singleton
    // ==========================
    public static synchronized Conexion getInstancia() {

        if (instancia == null) {

            instancia = new Conexion();

        }

        return instancia;

    }

    // ==========================
    // Obtener conexión
    // ==========================
    public Connection getConexion() {

        try {

            if (conexion == null || conexion.isClosed()) {

                conectar();

            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error verificando la conexión.",
                    e
            );

        }

        return conexion;

    }

    // ==========================
    // Verificar conexión
    // ==========================
    public boolean estaConectado() {

        try {

            return conexion != null
                    && !conexion.isClosed();

        } catch (SQLException e) {

            return false;

        }

    }

    // ==========================
    // Cerrar conexión
    // ==========================
    public void cerrarConexion() {

        try {

            if (conexion != null && !conexion.isClosed()) {

                conexion.close();

                System.out.println(
                        "Conexión cerrada."
                );

            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "No fue posible cerrar la conexión.",
                    e
            );

        }

    }

}
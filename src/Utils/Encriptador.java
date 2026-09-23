// Paquete Utils
package Utils;

// Importa BCrypt para el hash de contraseñas
import org.mindrot.jbcrypt.BCrypt;

// Clase con utilidades de encriptación
public class Encriptador {

    // Encriptar contraseña
    // Genera el hash de una contraseña
    public static String hashPassword(
            String password
    ) {

        // Aplica BCrypt a la contraseña
        return BCrypt.hashpw(
                // Contraseña en texto plano
                password,
                // Genera un salt aleatorio
                BCrypt.gensalt()
        );
    }

    // Verificar contraseña
    // Verifica una contraseña contra su hash
    public static boolean verificarPassword(
            String password,
            String passwordHash
    ) {

        // Compara con BCrypt
        return BCrypt.checkpw(
                // Contraseña ingresada
                password,
                // Hash guardado en la base de datos
                passwordHash
        );
    }
}
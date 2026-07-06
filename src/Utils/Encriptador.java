package Utils;

import org.mindrot.jbcrypt.BCrypt;

public class Encriptador {

    // Encriptar contraseña
    public static String hashPassword(
            String password
    ) {

        return BCrypt.hashpw(
                password,
                BCrypt.gensalt()
        );
    }

    // Verificar contraseña
    public static boolean verificarPassword(
            String password,
            String passwordHash
    ) {

        return BCrypt.checkpw(
                password,
                passwordHash
        );
    }
}
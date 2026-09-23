package Excepciones;

/**
 * Falla técnica al acceder a la base de datos (conexión, timeout, etc.).
 * La vista la muestra como ERROR; el detalle técnico va al log.
 */
public class AccesoDatosException extends AppException {

    public AccesoDatosException(String mensajeUsuario, Throwable causa) {
        super(mensajeUsuario, causa);
    }
}

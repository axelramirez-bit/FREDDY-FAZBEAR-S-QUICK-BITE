// Paquete Config
package Config;

// Excepción no verificada (RuntimeException) para errores de conexión
public class ConexionException extends RuntimeException {

    // Constructor que recibe solo el mensaje
    public ConexionException(String mensaje) {
        // Pasa el mensaje a la clase padre
        super(mensaje);
    }

    // Constructor con mensaje y causa original
    public ConexionException(String mensaje, Throwable causa) {
        // Pasa mensaje y causa a la clase padre
        super(mensaje, causa);
    }

}
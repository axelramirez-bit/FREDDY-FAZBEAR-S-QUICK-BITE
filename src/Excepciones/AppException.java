package Excepciones;

/**
 * Excepción base de la aplicación.
 * Lleva un mensaje ya redactado para el usuario final, de modo que la
 * capa View pueda mostrarlo tal cual en un diálogo.
 */
public class AppException extends RuntimeException {

    // Mensaje pensado para mostrarse al usuario (sin detalles técnicos)
    private final String mensajeUsuario;

    public AppException(String mensajeUsuario) {
        super(mensajeUsuario);
        this.mensajeUsuario = mensajeUsuario;
    }

    public AppException(String mensajeUsuario, Throwable causa) {
        super(mensajeUsuario, causa);
        this.mensajeUsuario = mensajeUsuario;
    }

    public String getMensajeUsuario() {
        return mensajeUsuario;
    }
}

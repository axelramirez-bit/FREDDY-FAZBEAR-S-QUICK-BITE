package Excepciones;

/**
 * Se incumple una regla del negocio (stock insuficiente, dato duplicado,
 * registro en uso, pedido ya existente, etc.).
 * La vista la muestra como ADVERTENCIA con la razón concreta.
 */
public class ReglaNegocioException extends AppException {

    public ReglaNegocioException(String mensajeUsuario) {
        super(mensajeUsuario);
    }

    public ReglaNegocioException(String mensajeUsuario, Throwable causa) {
        super(mensajeUsuario, causa);
    }
}

package Excepciones;

/**
 * Un dato ingresado no es válido (campo vacío, formato incorrecto, etc.).
 * La vista la muestra como ADVERTENCIA: el usuario puede corregirla.
 */
public class ValidacionException extends AppException {

    public ValidacionException(String mensajeUsuario) {
        super(mensajeUsuario);
    }
}

// Paquete Utils
package Utils;

// Importa el modelo Usuario
import Model.Usuario;

// Clase final que guarda la sesión activa
public final class Sesion {

    // ==========================
    // Singleton
    // ==========================
    // Única instancia (Singleton)
    private static Sesion instancia;

    // ==========================
    // Usuario autenticado
    // ==========================
    // Usuario autenticado actual
    private Usuario usuario;

    // ==========================
    // Constructor privado
    // ==========================
    // Constructor privado
    private Sesion() {
    }

    // ==========================
    // Obtener instancia
    // ==========================
    // Devuelve la instancia única (synchronized)
    public static synchronized Sesion getInstancia() {

        // Si aún no existe
        if (instancia == null) {
            // La crea
            instancia = new Sesion();
        }

        // Devuelve la instancia
        return instancia;
    }

    // ==========================
    // Iniciar sesión
    // ==========================
    // Inicia sesión con un usuario
    public void iniciarSesion(Usuario usuario) {

        // Guarda el usuario en la sesión
        this.usuario = usuario;

    }

    // ==========================
    // Cerrar sesión
    // ==========================
    // Cierra la sesión
    public void cerrarSesion() {

        // Borra el usuario guardado
        usuario = null;

    }

    // ==========================
    // Obtener usuario
    // ==========================
    // Devuelve el usuario autenticado
    public Usuario getUsuario() {

        // Retorna el usuario
        return usuario;

    }

    // ==========================
    // Verificar sesión
    // ==========================
    // Indica si hay sesión activa
    public boolean haySesion() {

        // True si hay un usuario guardado
        return usuario != null;

    }

}
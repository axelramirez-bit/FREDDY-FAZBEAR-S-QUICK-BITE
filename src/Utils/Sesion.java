package Utils;

import Model.Usuario;

public final class Sesion {

    // ==========================
    // Singleton
    // ==========================
    private static Sesion instancia;

    // ==========================
    // Usuario autenticado
    // ==========================
    private Usuario usuario;

    // ==========================
    // Constructor privado
    // ==========================
    private Sesion() {
    }

    // ==========================
    // Obtener instancia
    // ==========================
    public static synchronized Sesion getInstancia() {

        if (instancia == null) {
            instancia = new Sesion();
        }

        return instancia;
    }

    // ==========================
    // Iniciar sesión
    // ==========================
    public void iniciarSesion(Usuario usuario) {

        this.usuario = usuario;

    }

    // ==========================
    // Cerrar sesión
    // ==========================
    public void cerrarSesion() {

        usuario = null;

    }

    // ==========================
    // Obtener usuario
    // ==========================
    public Usuario getUsuario() {

        return usuario;

    }

    // ==========================
    // Verificar sesión
    // ==========================
    public boolean haySesion() {

        return usuario != null;

    }

}
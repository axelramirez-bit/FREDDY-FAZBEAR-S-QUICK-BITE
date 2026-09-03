package Service.Interfaz;

import Model.Usuario;

import java.util.List;

public interface IUsuarioService {

    boolean registrarUsuario(Usuario usuario);

    boolean actualizarUsuario(Usuario usuario);

    /**
     * Valida un usuario y devuelve el motivo exacto por el que no se
     * podría guardar (null si está todo bien). Antes, un solo booleano
     * en registrarUsuario/actualizarUsuario escondía si el problema era
     * un campo vacío, un correo mal formado, un correo ya usado por
     * otro usuario, o la edad mínima — la pantalla solo podía decir
     * "No se pudo guardar el usuario." sin explicar por qué.
     * Los paneles deben llamar este método ANTES de registrar/actualizar
     * y mostrar el mensaje si no es null.
     */
    String validar(Usuario usuario);

    boolean eliminarUsuario(int idUsuario);

    // Caso de uso 2.4: desactivar (estado=false) siempre es posible.
    boolean desactivarUsuario(int idUsuario);

    boolean activarUsuario(int idUsuario);

    Usuario obtenerUsuarioPorId(int idUsuario);

    List<Usuario> listarUsuarios();

    Usuario iniciarSesion(String correo, String password);

}
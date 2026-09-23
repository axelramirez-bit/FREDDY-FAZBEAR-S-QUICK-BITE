// Paquete Service.Interfaz
package Service.Interfaz;

// Importa el modelo Usuario
import Model.Usuario;

// Importa List
import java.util.List;

// Contrato del servicio de usuarios
public interface IUsuarioService {

    // Registra un usuario
    boolean registrarUsuario(Usuario usuario);

    // Actualiza un usuario
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
    // Valida y devuelve el motivo del error (null si está bien)
    String validar(Usuario usuario);

    // Elimina un usuario por id
    boolean eliminarUsuario(int idUsuario);

    // Caso de uso 2.4: desactivar (estado=false) siempre es posible.
    // Desactiva un usuario
    boolean desactivarUsuario(int idUsuario);

    // Activa un usuario
    boolean activarUsuario(int idUsuario);

    // Obtiene un usuario por id
    Usuario obtenerUsuarioPorId(int idUsuario);

    // Lista todos los usuarios
    List<Usuario> listarUsuarios();

    // Valida correo y contraseña e inicia sesión
    Usuario iniciarSesion(String correo, String password);

}
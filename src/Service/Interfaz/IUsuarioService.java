package Service.Interfaz;

import Model.Usuario;

import java.util.List;

public interface IUsuarioService {

    boolean registrarUsuario(Usuario usuario);

    boolean actualizarUsuario(Usuario usuario);

    boolean eliminarUsuario(int idUsuario);

    Usuario obtenerUsuarioPorId(int idUsuario);

    List<Usuario> listarUsuarios();

    Usuario iniciarSesion(String correo, String password);

}
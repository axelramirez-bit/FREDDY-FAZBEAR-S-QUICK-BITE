package DAO.Interfaz;

import Model.Usuario;
import java.util.List;

public interface IUsuarioDAO {

    boolean insertar(Usuario usuario);

    boolean actualizar(Usuario usuario);

    boolean eliminar(int id);

    Usuario buscarPorId(int id);

    Usuario iniciarSesion(String correo, String contraseña);

    List<Usuario> listar();
}

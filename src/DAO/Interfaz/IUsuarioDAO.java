package DAO.Interfaz;

import Model.Usuario;
import java.util.List;

public interface IUsuarioDAO {

    boolean insertar(Usuario usuario);

    boolean actualizar(Usuario usuario);

    boolean eliminar(int id);

    // Caso de uso 2.4: "Desactivar usuario" siempre debe ser
    // posible (a diferencia de eliminar, que la BD bloquea si el
    // usuario ya procesó pedidos por la FK pedido.id_usuario).
    boolean cambiarEstado(int idUsuario, boolean estado);

    Usuario buscarPorId(int id);

    Usuario iniciarSesion(String correo, String contraseña);

    List<Usuario> listar();

    Usuario buscarPorCorreo(String correo);


}
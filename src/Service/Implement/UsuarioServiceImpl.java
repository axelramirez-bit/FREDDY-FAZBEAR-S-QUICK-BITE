package Service.Implement;

import DAO.Implement.UsuarioDAOImpl;
import DAO.Interfaz.IUsuarioDAO;
import Model.Usuario;
import Service.Interfaz.IUsuarioService;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;

public class UsuarioServiceImpl implements IUsuarioService {

    private static final Pattern CORREO_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    private static final int EDAD_MINIMA = 13;

    private final IUsuarioDAO usuarioDAO;

    public UsuarioServiceImpl() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    public UsuarioServiceImpl(IUsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }


    @Override
    public boolean actualizarUsuario(Usuario usuario) {

        if (usuario.getIdUsuario() <= 0) {
            return false;
        }

        if (!validarUsuario(usuario)) {
            return false;
        }

        Usuario existente = usuarioDAO.buscarPorId(usuario.getIdUsuario());

        if (existente == null) {
            return false;
        }

        return usuarioDAO.actualizar(usuario);

    }

    @Override
    public boolean eliminarUsuario(int idUsuario) {

        if (idUsuario <= 0) {
            return false;
        }

        return usuarioDAO.eliminar(idUsuario);

    }

    @Override
    public boolean desactivarUsuario(int idUsuario) {

        if (idUsuario <= 0) {
            return false;
        }

        return usuarioDAO.cambiarEstado(idUsuario, false);
    }

    @Override
    public boolean activarUsuario(int idUsuario) {

        if (idUsuario <= 0) {
            return false;
        }

        return usuarioDAO.cambiarEstado(idUsuario, true);
    }

    @Override
    public Usuario obtenerUsuarioPorId(int idUsuario) {

        if (idUsuario <= 0) {
            return null;
        }

        return usuarioDAO.buscarPorId(idUsuario);

    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioDAO.listar();
    }


    // ---------- Métodos auxiliares de negocio ----------

    private boolean validarUsuario(Usuario usuario) {

        if (usuario == null) {
            return false;
        }

        if (usuario.getRol() == null || usuario.getRol().getIdRol() <= 0) {
            return false;
        }

        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            return false;
        }

        if (usuario.getApellido() == null || usuario.getApellido().isBlank()) {
            return false;
        }

        if (usuario.getCorreo() == null || !CORREO_PATTERN.matcher(usuario.getCorreo()).matches()) {
            return false;
        }

        if (usuario.getPassword() == null || usuario.getPassword().length() < 6) {
            return false;
        }

        if (usuario.getFechaNacimiento() == null || !esMayorDeEdadMinima(usuario.getFechaNacimiento())) {
            return false;
        }

        return true;

    }

    private boolean esMayorDeEdadMinima(LocalDate fechaNacimiento) {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears() >= EDAD_MINIMA;
    }
    
    @Override
    public boolean registrarUsuario(Usuario usuario) {

        if (!validarUsuario(usuario)) {
            return false;
        }

        // FIX: antes se guardaba la contraseña tal cual la escribió el
        // usuario. Ahora se guarda el hash de BCrypt.
        usuario.setPassword(
                Utils.Encriptador.hashPassword(usuario.getPassword())
        );

        return usuarioDAO.insertar(usuario);
    }

    @Override
    public Usuario iniciarSesion(String correo, String password) {

        if (correo == null || correo.isBlank()
                || password == null || password.isBlank()) {
            return null;
        }

        // FIX: antes se comparaba correo+password en la misma consulta
        // SQL en texto plano. Ahora se busca solo por correo, y la
        // contraseña se verifica en Java contra el hash guardado.
        Usuario usuario = usuarioDAO.buscarPorCorreo(correo.trim());

        if (usuario == null) {
            return null; // no existe ese correo
        }

        boolean coincide = Utils.Encriptador.verificarPassword(
                password,
                usuario.getPassword()
        );

        return coincide ? usuario : null;
    }
}
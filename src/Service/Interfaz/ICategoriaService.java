package Service.Interfaz;

import Model.Categoria;
import java.util.List;

public interface ICategoriaService {
    
    boolean guardar(Categoria categoria);

    boolean actualizar(Categoria categoria);

    /** Motivo exacto por el que una categoría no se podría guardar (null si está bien). */
    String validar(Categoria categoria);

    boolean cambiarEstado(int idCategoria, boolean estado);

    Categoria buscarPorId(int idCategoria);

    List<Categoria> listar();

    List<Categoria> listarActivas();
    
}
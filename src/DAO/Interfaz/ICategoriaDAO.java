package DAO.Interfaz;

import Model.Categoria;
import java.util.List;

public interface ICategoriaDAO {

    boolean guardar(Categoria categoria);

    boolean actualizar(Categoria categoria);

    boolean cambiarEstado(int idCategoria, boolean estado);

    Categoria buscarPorId(int idCategoria);

    List<Categoria> listar();

    List<Categoria> listarActivas();

}

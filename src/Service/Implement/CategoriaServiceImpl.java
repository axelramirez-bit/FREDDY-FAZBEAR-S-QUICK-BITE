package Service.Implement;

import DAO.Implement.CategoriaDAOImpl;
import DAO.Interfaz.ICategoriaDAO;
import Model.Categoria;
import Service.Interfaz.ICategoriaService;

import java.util.List;

public class CategoriaServiceImpl implements ICategoriaService {

    private final ICategoriaDAO categoriaDAO;

    public CategoriaServiceImpl() {
        this.categoriaDAO = new CategoriaDAOImpl();
    }

    @Override
    public boolean guardar(Categoria categoria) {

        if (categoria == null) {
            return false;
        }

        if (categoria.getNombre() == null
                || categoria.getNombre().trim().isEmpty()) {
            return false;
        }

        return categoriaDAO.guardar(categoria);
    }

    @Override
    public boolean actualizar(Categoria categoria) {

        if (categoria == null) {
            return false;
        }

        if (categoria.getIdCategoria() <= 0) {
            return false;
        }

        return categoriaDAO.actualizar(categoria);
    }

    @Override
    public boolean cambiarEstado(int idCategoria, boolean estado) {

        if (idCategoria <= 0) {
            return false;
        }

        return categoriaDAO.cambiarEstado(idCategoria, estado);
    }

    @Override
    public Categoria buscarPorId(int idCategoria) {

        if (idCategoria <= 0) {
            return null;
        }

        return categoriaDAO.buscarPorId(idCategoria);
    }

    @Override
    public List<Categoria> listar() {

        return categoriaDAO.listar();
    }

    @Override
    public List<Categoria> listarActivas() {

        return categoriaDAO.listarActivas();
    }

}

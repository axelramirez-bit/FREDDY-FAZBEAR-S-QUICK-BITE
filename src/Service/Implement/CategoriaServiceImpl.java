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

        if (validar(categoria) != null) {
            return false;
        }

        return categoriaDAO.guardar(categoria);
    }

    @Override
    public boolean actualizar(Categoria categoria) {

        if (categoria == null || categoria.getIdCategoria() <= 0) {
            return false;
        }

        if (validar(categoria) != null) {
            return false;
        }

        return categoriaDAO.actualizar(categoria);
    }

    @Override
    public String validar(Categoria categoria) {

        if (categoria == null) {
            return "Categoría inválida.";
        }

        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            return "El nombre de la categoría es obligatorio.";
        }

        // La columna "nombre" es UNIQUE en la tabla categoria: sin este
        // chequeo, crear/renombrar a un nombre repetido fallaba en
        // silencio contra esa restricción y solo se veía "No se pudo
        // guardar la categoría.", sin explicar que ya existía.
        for (Categoria existente : categoriaDAO.listar()) {
            boolean mismoNombre = existente.getNombre() != null
                    && existente.getNombre().trim().equalsIgnoreCase(categoria.getNombre().trim());
            boolean esOtraCategoria = existente.getIdCategoria() != categoria.getIdCategoria();

            if (mismoNombre && esOtraCategoria) {
                return "Ya existe una categoría con el nombre \"" + categoria.getNombre().trim() + "\".";
            }
        }

        return null;
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
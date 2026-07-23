package Service.Implement;

import DAO.Implement.PromocionDAOImpl;
import DAO.Interfaz.IPromocionDAO;
import Model.Promocion;
import Service.Interfaz.IPromocionService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PromocionServiceImpl implements IPromocionService {

    private final IPromocionDAO promocionDAO;

    public PromocionServiceImpl() {
        this.promocionDAO = new PromocionDAOImpl();
    }

    public PromocionServiceImpl(IPromocionDAO promocionDAO) {
        this.promocionDAO = promocionDAO;
    }

    @Override
    public boolean registrarPromocion(Promocion promocion) {

        if (!validarPromocion(promocion)) {
            return false;
        }

        return promocionDAO.insertar(promocion);

    }

    @Override
    public boolean actualizarPromocion(Promocion promocion) {

        if (promocion.getIdPromocion() <= 0) {
            return false;
        }

        if (!validarPromocion(promocion)) {
            return false;
        }

        Promocion existente = promocionDAO.buscarPorId(promocion.getIdPromocion());

        if (existente == null) {
            return false;
        }

        return promocionDAO.actualizar(promocion);

    }

    @Override
    public boolean eliminarPromocion(int idPromocion) {

        if (idPromocion <= 0) {
            return false;
        }

        return promocionDAO.eliminar(idPromocion);

    }

    @Override
    public Promocion obtenerPromocionPorId(int idPromocion) {

        if (idPromocion <= 0) {
            return null;
        }

        return promocionDAO.buscarPorId(idPromocion);

    }

    @Override
    public List<Promocion> listarPromociones() {
        return promocionDAO.listar();
    }

    @Override
    public List<Promocion> listarPromocionesActivas() {

        LocalDate hoy = LocalDate.now();

        return promocionDAO.listar().stream()
                .filter(Promocion::isEstado)
                .filter(p -> !hoy.isBefore(p.getFechaInicio()))
                .filter(p -> !hoy.isAfter(p.getFechaFin()))
                .collect(Collectors.toList());

    }

    // ---------- Métodos auxiliares de negocio ----------

    private boolean validarPromocion(Promocion promocion) {

        if (promocion == null) {
            return false;
        }

        if (promocion.getNombre() == null || promocion.getNombre().isBlank()) {
            return false;
        }

        if (promocion.getDescuento() < 0 || promocion.getDescuento() > 100) {
            return false;
        }

        if (promocion.getFechaInicio() == null || promocion.getFechaFin() == null) {
            return false;
        }

        if (promocion.getFechaFin().isBefore(promocion.getFechaInicio())) {
            return false;
        }

        return true;

    }

}
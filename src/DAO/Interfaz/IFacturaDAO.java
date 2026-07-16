package DAO.Interfaz;

import Model.Factura;
import java.util.List;

public interface IFacturaDAO {

    boolean guardar(Factura factura);

    Factura buscarPorId(int idFactura);

    Factura buscarPorNumero(String numeroFactura);

    Factura buscarPorPedido(int idPedido);

    List<Factura> listar();

    List<Factura> listarPorCliente(int idCliente);

}

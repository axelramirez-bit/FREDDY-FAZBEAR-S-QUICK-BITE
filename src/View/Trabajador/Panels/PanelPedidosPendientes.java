package View.Trabajador.Panels;

import Base.PanelListaPedidos;
import Model.EstadoPedido;

/**
 * Cola de pedidos recién confirmados por el Cliente, esperando a
 * que el Trabajador inicie la preparación.
 */
public class PanelPedidosPendientes extends PanelListaPedidos {

    public PanelPedidosPendientes() {

        super(
                EstadoPedido.PENDIENTE,
                EstadoPedido.PREPARACION,
                "Iniciar preparación"
        );
    }

}
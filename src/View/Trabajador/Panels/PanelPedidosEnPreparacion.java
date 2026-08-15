package View.Trabajador.Panels;

import Base.PanelListaPedidos;
import Model.EstadoPedido;

/**
 * Pedidos que el Trabajador ya empezó a preparar. Mismo panel base
 * que PanelPedidosPendientes, solo cambia el estado que filtra y a
 * dónde avanza.
 */
public class PanelPedidosEnPreparacion extends PanelListaPedidos {

    public PanelPedidosEnPreparacion() {

        super(
                EstadoPedido.PREPARACION,
                EstadoPedido.LISTO,
                "Marcar como listo"
        );
    }

}
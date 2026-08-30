package View.PedidosProceso.Panels;

import Base.PanelListaPedidos;
import Model.EstadoPedido;

public class PanelPedidosListos  extends PanelListaPedidos {

    public PanelPedidosListos() {

        super(
                EstadoPedido.LISTO,
                EstadoPedido.LISTO,
                "Iniciar preparación"
        );
    }
}
package View.PedidosProceso;

import Base.DashboardBase;
import Base.OpcionesTrabajador;
import Base.Rol;
import View.Autoservicio.Autoservicio;
import View.PedidosProceso.Panels.PanelHistorial;
import View.PedidosProceso.Panels.PanelInicio;
import View.PedidosProceso.Panels.PanelPedidosEnPreparacion;
import View.PedidosProceso.Panels.PanelPedidosListos;
import View.PedidosProceso.Panels.PanelPedidosPendientes;

import javax.swing.SwingUtilities;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Dashboard del Trabajador.
 *
 * IMPORTANTE: cada idVista se toma de OpcionesTrabajador.X.getIdVista(),
 * NUNCA se escribe el String a mano aquí. Si algún día cambias el
 * idVista de una opción en OpcionesTrabajador, este archivo no
 * necesita tocarse — sigue apuntando al mismo enum y automáticamente
 * queda alineado. Escribir el mismo String en dos archivos distintos
 * es exactamente el bug que tenía la versión anterior.
 * ===============================================================
 */
public class PedidosProceso extends DashboardBase {

    public PedidosProceso() {
        super(Rol.TRABAJADOR, "Freddy Fazbear's Quick Bite - Trabajador");
    }

    @Override
    protected void registrarPaneles() {

        registrarVista(
                OpcionesTrabajador.INICIO.getIdVista(),
                new PanelInicio()
        );

        registrarVista(
                OpcionesTrabajador.PENDIENTES.getIdVista(),
                new PanelPedidosPendientes()
        );

        registrarVista(
                OpcionesTrabajador.EN_PREPARACION.getIdVista(),
                new PanelPedidosEnPreparacion()
        );

        registrarVista(
                OpcionesTrabajador.LISTOS.getIdVista(),
                new PanelPedidosListos()
        );

        registrarVista(
                OpcionesTrabajador.HISTORIAL.getIdVista(),
                new PanelHistorial()
        );
    }

    @Override
    protected String vistaInicial() {
        return OpcionesTrabajador.INICIO.getIdVista();
    }

    /**
     * ===============================================================
     * Espejo de DashboardCliente.onConfiguracion(): el botón que antes
     * abría "Configuración" ahora regresa a la ventana de catálogo +
     * carrito, para que el Cajero pueda seguir tomando pedidos sin
     * cerrar sesión.
     * ===============================================================
     */
    @Override
    public void onConfiguracion() {
        Autoservicio dashboard = new Autoservicio();
        dashboard.setVisible(true);
        dispose();
    }

    // ==========================================================
    // PRUEBA AISLADA
    // ==========================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PedidosProceso dashboard = new PedidosProceso();
            dashboard.setVisible(true);
        });
    }

}
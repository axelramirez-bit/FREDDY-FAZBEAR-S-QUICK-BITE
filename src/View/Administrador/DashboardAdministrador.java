package View.Administrador;

import Base.DashboardBase;
import Base.OpcionesAdministrador;
import Base.Rol;
import View.Administrador.Panels.PanelCategorias;
import View.Administrador.Panels.PanelDashboard;
import View.Administrador.Panels.PanelPagos;
import View.Administrador.Panels.PanelPedidos;
import View.Administrador.Panels.PanelProductos;
import View.Administrador.Panels.PanelPromociones;
import View.Administrador.Panels.PanelReportes;
import View.Administrador.Panels.PanelTrabajadores;
import View.Administrador.Panels.PanelUsuarios;
import View.Administrador.Panels.PanelVentas;
import View.Utils.FabricaScroll;

import javax.swing.SwingUtilities;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Dashboard del Administrador.
 *
 * Cada panel se registra envuelto en FabricaScroll.crearPanel(...)
 * para que, si el contenido (tarjetas KPI + tabla + notas) es más
 * alto que la ventana, aparezca una barra de scroll vertical en
 * vez de recortarse. El scroll horizontal queda deshabilitado
 * (VELOCIDAD_SCROLL/estilo ya definido en EstilosComponentes) para
 * no romper el diseño responsivo de cada panel.
 * ===============================================================
 */
public class DashboardAdministrador extends DashboardBase {

    public DashboardAdministrador() {
        super(Rol.ADMINISTRADOR, "Freddy Fazbear's Quick Bite - Administrador");
    }

    @Override
    protected void registrarPaneles() {

        registrarVista(
                OpcionesAdministrador.DASHBOARD.getIdVista(),
                FabricaScroll.crearPanel(new PanelDashboard())
        );

        registrarVista(
                OpcionesAdministrador.USUARIOS.getIdVista(),
                FabricaScroll.crearPanel(new PanelUsuarios())
        );

        registrarVista(
                OpcionesAdministrador.TRABAJADORES.getIdVista(),
                FabricaScroll.crearPanel(new PanelTrabajadores())
        );

        registrarVista(
                OpcionesAdministrador.PRODUCTOS.getIdVista(),
                FabricaScroll.crearPanel(new PanelProductos())
        );

        registrarVista(
                OpcionesAdministrador.CATEGORIAS.getIdVista(),
                FabricaScroll.crearPanel(new PanelCategorias())
        );

        registrarVista(
                OpcionesAdministrador.PROMOCIONES.getIdVista(),
                FabricaScroll.crearPanel(new PanelPromociones())
        );

        registrarVista(
                OpcionesAdministrador.PEDIDOS.getIdVista(),
                FabricaScroll.crearPanel(new PanelPedidos())
        );

        registrarVista(
                OpcionesAdministrador.PAGOS.getIdVista(),
                FabricaScroll.crearPanel(new PanelPagos())
        );

        registrarVista(
                OpcionesAdministrador.VENTAS.getIdVista(),
                FabricaScroll.crearPanel(new PanelVentas())
        );

        registrarVista(
                OpcionesAdministrador.REPORTES.getIdVista(),
                FabricaScroll.crearPanel(new PanelReportes())
        );
    }

    @Override
    protected String vistaInicial() {
        return OpcionesAdministrador.DASHBOARD.getIdVista();
    }

    // ==========================================================
    // PRUEBA AISLADA
    // ==========================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DashboardAdministrador dashboard = new DashboardAdministrador();
            dashboard.setVisible(true);
        });
    }

}
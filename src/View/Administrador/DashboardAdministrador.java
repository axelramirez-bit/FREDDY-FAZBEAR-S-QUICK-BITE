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

import javax.swing.SwingUtilities;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Dashboard del Administrador.
 *
 * AJUSTA LOS IMPORTS si tus paneles de Administrador viven en un
 * paquete distinto o tienen otro nombre de clase — los nombres de
 * abajo (PanelDashboard, PanelUsuarios, etc.) son mi mejor
 * suposición siguiendo la misma convención que ya usaste en
 * Trabajador (View.<Rol>.Panels.PanelX). Lo que NO debes cambiar
 * es el patrón de tomar el idVista siempre desde
 * OpcionesAdministrador.X.getIdVista() — eso es lo que evita el
 * bug de Strings desalineados que tenía DashboardTrabajador.
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
                new PanelDashboard()
        );

        registrarVista(
                OpcionesAdministrador.USUARIOS.getIdVista(),
                new PanelUsuarios()
        );

        registrarVista(
                OpcionesAdministrador.TRABAJADORES.getIdVista(),
                new PanelTrabajadores()
        );

        registrarVista(
                OpcionesAdministrador.PRODUCTOS.getIdVista(),
                new PanelProductos()
        );

        registrarVista(
                OpcionesAdministrador.CATEGORIAS.getIdVista(),
                new PanelCategorias()
        );

        registrarVista(
                OpcionesAdministrador.PROMOCIONES.getIdVista(),
                new PanelPromociones()
        );

        registrarVista(
                OpcionesAdministrador.PEDIDOS.getIdVista(),
                new PanelPedidos()
        );

        registrarVista(
                OpcionesAdministrador.PAGOS.getIdVista(),
                new PanelPagos()
        );

        registrarVista(
                OpcionesAdministrador.VENTAS.getIdVista(),
                new PanelVentas()
        );

        registrarVista(
                OpcionesAdministrador.REPORTES.getIdVista(),
                new PanelReportes()
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
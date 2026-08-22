package View.Cliente;

import Base.DashboardBase;
import Base.Rol;
import View.Cliente.Panels.PanelAlmuerzos;
import View.Cliente.Panels.PanelAntojos;
import View.Cliente.Panels.PanelBebidas;
import View.Cliente.Panels.PanelCajitaFeliz;
import View.Cliente.Panels.PanelCarrito;
import View.Cliente.Panels.PanelCombos;
import View.Cliente.Panels.PanelDesayunos;
import View.Cliente.Panels.PanelInicio;
import View.Cliente.Panels.PanelMcCafe;
import View.Cliente.Panels.PanelPostres;
import View.Cliente.Panels.PanelPromociones;
import View.Trabajador.DashboardTrabajador;

import javax.swing.SwingUtilities;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Dashboard del Cliente. Solo registra sus paneles — toda la
 * estructura visual (sidebar, header, CardLayout) vive en
 * DashboardBase.
 * ===============================================================
 */
public class DashboardCliente extends DashboardBase {

    public DashboardCliente() {
        super(Rol.CLIENTE, "Freddy Fazbear's Quick Bite - Cliente");
    }

    @Override
    protected void registrarPaneles() {

        registrarVista("INICIO", new PanelInicio());
        registrarVista("DESAYUNOS", new PanelDesayunos());
        registrarVista("ALMUERZOS", new PanelAlmuerzos());
        registrarVista("POSTRES", new PanelPostres());
        registrarVista("MCCAFE", new PanelMcCafe());
        registrarVista("BEBIDAS", new PanelBebidas());
        registrarVista("ANTOJOS", new PanelAntojos());
        registrarVista("CAJITA_FELIZ", new PanelCajitaFeliz());
        registrarVista("COMBOS", new PanelCombos());
        registrarVista("PROMOCIONES_CLIENTE", new PanelPromociones());
        registrarVista("CARRITO", new PanelCarrito());
       
    }

    @Override
    protected String vistaInicial() {
        return "INICIO";
    }

    /**
     * ===============================================================
     * Con el modelo de 2 actores, el mismo Cajero opera esta ventana
     * (catálogo + carrito, para armar el pedido) y la ventana de
     * Trabajador (gestionar el estado del pedido). El botón que antes
     * abría "Configuración" (que nunca tuvo panel propio) ahora es el
     * atajo para saltar de una ventana a la otra sin cerrar sesión.
     *
     * No se toca DashboardBase ni BarraLateral: onConfiguracion() ya
     * estaba pensado para que cada DashboardX lo redefina.
     * ===============================================================
     */
    @Override
    public void onConfiguracion() {
        DashboardTrabajador dashboard = new DashboardTrabajador();
        dashboard.setVisible(true);
        dispose();
    }


/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Ventana de prueba SOLO para verificar que DashboardCliente
 * navega bien entre paneles mientras no existe todavía el flujo
 * real de Login -> Sesion -> Dashboard.
 *
 * No es el Main definitivo del sistema: cuando el compañero de
 * Login/Registro/Bienvenida termine su parte, el dashboard se
 * abrirá desde ahí (tras autenticar), no desde esta clase.
 * ===============================================================
 */

  public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DashboardCliente dashboard = new DashboardCliente();
            dashboard.setVisible(true);
        });
    }
    
}
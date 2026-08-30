package View.Autoservicio;

import Base.DashboardBase;
import Base.Rol;
import View.Autoservicio.Panels.PanelAlmuerzos;
import View.Autoservicio.Panels.PanelAntojos;
import View.Autoservicio.Panels.PanelBebidas;
import View.Autoservicio.Panels.PanelCajitaFeliz;
import View.Autoservicio.Panels.PanelCarrito;
import View.Autoservicio.Panels.PanelCombos;
import View.Autoservicio.Panels.PanelDesayunos;
import View.Autoservicio.Panels.PanelInicio;
import View.Autoservicio.Panels.PanelMcCafe;
import View.Autoservicio.Panels.PanelPostres;
import View.Autoservicio.Panels.PanelPromociones;
import View.PedidosProceso.PedidosProceso;

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
public class Autoservicio extends DashboardBase {

    public Autoservicio() {
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
        PedidosProceso dashboard = new PedidosProceso();
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
            Autoservicio dashboard = new Autoservicio();
            dashboard.setVisible(true);
        });
    }
    
}
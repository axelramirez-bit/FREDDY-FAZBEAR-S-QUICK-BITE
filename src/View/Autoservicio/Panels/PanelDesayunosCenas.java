package View.Autoservicio.Panels;

import Base.PanelProductos;
import Utils.FranjaHoraria;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Catálogo de la opción dinámica "Desayunos / Cenas" (ver
 * Base.OpcionesCliente.DESAYUNOS_CENAS).
 *
 * A diferencia de PanelHamburguesas/PanelPizzas, el filtro de este
 * panel NO queda fijo en el constructor: es un lambda que llama a
 * FranjaHoraria.nombreCategoria() cada vez que PanelProductos lo
 * evalúa (dentro de cargarProductos()), así que siempre usa la hora
 * local ACTUAL, no la hora en la que se creó el panel.
 *
 * Como DashboardBase crea todos los paneles del Cliente una sola
 * vez al abrir sesión (ver Autoservicio.registrarPaneles()) y
 * después solo los oculta/muestra con CardLayout, agregamos un
 * ComponentListener: cada vez que este panel vuelve a hacerse
 * visible (el Cliente hace clic en la opción de nuevo), se vuelve a
 * consultar la base de datos con cargarProductos(), por si la
 * franja horaria cambió mientras la sesión seguía abierta (por
 * ejemplo, un cliente que entra a las 11:55 y vuelve a esta opción
 * a las 12:05).
 * ===============================================================
 */
public class PanelDesayunosCenas extends PanelProductos {

    public PanelDesayunosCenas() {

        super(producto -> producto.perteneceACategoria(FranjaHoraria.nombreCategoria()));

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                cargarProductos();
            }
        });
    }

}

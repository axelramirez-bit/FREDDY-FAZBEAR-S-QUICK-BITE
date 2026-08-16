package View.Cliente.Panels;

import Base.PanelProductos;
import View.Componentes.BarraBusqueda;
import View.Utils.UIConstants;

import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

/**
 * Pantalla de INICIO del Cliente: muestra TODOS los productos
 * disponibles del catálogo (sin filtrar por categoría ni por
 * promoción) y permite buscarlos por nombre con BarraBusqueda,
 * reutilizando ServicioBusqueda a través de PanelProductos.
 *
 * Antes este panel filtraba por "producto.getPromocion() != null"
 * (copiado de PanelPromociones), lo cual lo dejaba casi siempre
 * vacío y además no era el propósito de un panel de "Inicio".
 */
public class PanelInicio extends PanelProductos {

    private BarraBusqueda barraBusqueda;

    public PanelInicio() {

        // Sin filtro de categoría/promoción: muestra todo lo disponible.
        super(producto -> true);

        agregarBarraBusqueda();
    }

    private void agregarBarraBusqueda() {

        barraBusqueda = new BarraBusqueda("Buscar en el menú...");

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setOpaque(false);
        panelSuperior.setBorder(
                javax.swing.BorderFactory.createEmptyBorder(
                        UIConstants.ESPACIO_PRODUCTOS,
                        UIConstants.ESPACIO_PRODUCTOS,
                        0,
                        UIConstants.ESPACIO_PRODUCTOS));

        panelSuperior.add(barraBusqueda);

        add(panelSuperior, BorderLayout.NORTH);

        barraBusqueda.agregarListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrar();
            }
        });
    }

    private void filtrar() {
        aplicarBusqueda(barraBusqueda.getTexto());
    }

}

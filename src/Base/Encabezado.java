package Base;

import View.Utils.AdministradorTema;
import View.Utils.PaletaColores;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Encabezado (header) compartido por los tres dashboards.
 *
 * Responsabilidad única: mostrar el título de la sección actual
 * y la fecha. No sabe nada de navegación — DashboardBase le llama
 * setTitulo(...) cada vez que se abre una vista distinta.
 * ===============================================================
 */
public class Encabezado extends JPanel {

    private final JLabel lblTitulo;
    private final JLabel lblFecha;

    public Encabezado() {

        setLayout(new BorderLayout());

        setBackground(PaletaColores.PRINCIPAL);

        setPreferredSize(
                new Dimension(0, AdministradorTema.alturaEncabezado())
        );

        setBorder(
                BorderFactory.createEmptyBorder(
                        AdministradorTema.espacioMediano(),
                        AdministradorTema.espacioGrande(),
                        AdministradorTema.espacioMediano(),
                        AdministradorTema.espacioGrande()
                )
        );

        JPanel panelTextos = new JPanel();

        panelTextos.setOpaque(false);

        panelTextos.setLayout(
                new BoxLayout(panelTextos, BoxLayout.Y_AXIS)
        );

        lblTitulo = new JLabel();

        lblTitulo.setForeground(AdministradorTema.colorTextoBlanco());

        lblTitulo.setFont(AdministradorTema.fuenteTituloSeccion());

        lblFecha = new JLabel(obtenerFechaActual());

        lblFecha.setForeground(AdministradorTema.colorTextoBlanco());

        lblFecha.setFont(AdministradorTema.fuenteSubtituloHeader());

        panelTextos.add(lblTitulo);

        panelTextos.add(lblFecha);

        add(panelTextos, BorderLayout.WEST);
    }

    // ==========================================================
    // API PÚBLICA
    // ==========================================================
    /**
     * Cambia el título mostrado (ej. "Antojos", "Ventas").
     * DashboardBase lo llama cada vez que se navega a una vista
     * distinta.
     */
    public void setTitulo(String titulo) {
        lblTitulo.setText(titulo);
    }

    // ==========================================================
    // FECHA
    // ==========================================================
    private String obtenerFechaActual() {

        DateTimeFormatter formato = DateTimeFormatter.ofPattern(
                "EEEE, dd 'de' MMMM 'del' yyyy",
                new Locale("es", "ES")
        );

        String fecha = LocalDate.now().format(formato);

        // Capitaliza el día de la semana ("sábado" -> "Sábado")
        return fecha.substring(0, 1).toUpperCase() + fecha.substring(1);
    }

}
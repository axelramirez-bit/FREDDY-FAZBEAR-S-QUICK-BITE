// Paquete Base
package Base;

// Importa el administrador de tema
import View.Utils.AdministradorTema;
// Importa la paleta de colores
import View.Utils.PaletaColores;

// Importa BorderFactory
import javax.swing.BorderFactory;
// Importa BoxLayout
import javax.swing.BoxLayout;
// Importa JLabel
import javax.swing.JLabel;
// Importa JPanel
import javax.swing.JPanel;
// Importa BorderLayout
import java.awt.BorderLayout;
// Importa Dimension
import java.awt.Dimension;
// Importa LocalDate
import java.time.LocalDate;
// Importa DateTimeFormatter
import java.time.format.DateTimeFormatter;
// Importa Locale
import java.util.Locale;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * --------------------------------------------------------------- Encabezado
 * (header) compartido por los tres dashboards.
 *
 * Responsabilidad única: mostrar el título de la sección actual y la fecha. No
 * sabe nada de navegación — DashboardBase le llama setTitulo(...) cada vez que
 * se abre una vista distinta.
 * ===============================================================
 */
// Panel de encabezado compartido por los tres dashboards
public class Encabezado extends JPanel {

    // Etiqueta del título
    private final JLabel lblTitulo;
    // Etiqueta de la fecha
    private final JLabel lblFecha;

    // Constructor del encabezado
    public Encabezado() {

        // Usa BorderLayout
        setLayout(new BorderLayout());

        // Fondo con el color principal
        setBackground(PaletaColores.PRINCIPAL);

        // Define el tamaño preferido
        setPreferredSize(
                // Ancho libre y alto tomado del tema
                new Dimension(0, AdministradorTema.alturaEncabezado())
        );

        // Define los márgenes
        setBorder(
                // Crea un borde vacío
                BorderFactory.createEmptyBorder(
                        // Margen superior
                        AdministradorTema.espacioMediano(),
                        // Margen izquierdo
                        AdministradorTema.espacioGrande(),
                        // Margen inferior
                        AdministradorTema.espacioMediano(),
                        // Margen derecho
                        AdministradorTema.espacioGrande()
                )
        );

        // Panel que apila los textos
        JPanel panelTextos = new JPanel();

        // Fondo transparente
        panelTextos.setOpaque(false);

        // Asigna layout vertical
        panelTextos.setLayout(
                new BoxLayout(panelTextos, BoxLayout.Y_AXIS)
        );

        // Crea la etiqueta del título
        lblTitulo = new JLabel();

        // Título en color blanco
        lblTitulo.setForeground(AdministradorTema.colorTextoBlanco());

        // Fuente de título de sección
        lblTitulo.setFont(AdministradorTema.fuenteTituloSeccion());

        // Crea la etiqueta de fecha con la fecha de hoy
        lblFecha = new JLabel(obtenerFechaActual());

        // Fecha en color blanco
        lblFecha.setForeground(AdministradorTema.colorTextoBlanco());

        // Fuente de subtítulo
        lblFecha.setFont(AdministradorTema.fuenteSubtituloHeader());

        // Agrega el título
        panelTextos.add(lblTitulo);

        // Agrega la fecha
        panelTextos.add(lblFecha);

        // Ubica los textos a la izquierda
        add(panelTextos, BorderLayout.WEST);
    }

    // ==========================================================
    // API PÚBLICA
    // ==========================================================
    /**
     * Cambia el título mostrado (ej. "Antojos", "Ventas"). DashboardBase lo
     * llama cada vez que se navega a una vista distinta.
     */
    // Cambia el título mostrado
    public void setTitulo(String titulo) {
        // Actualiza el texto de la etiqueta
        lblTitulo.setText(titulo);
    }

    // ==========================================================
    // FECHA
    // ==========================================================
    // Devuelve la fecha actual formateada
    private String obtenerFechaActual() {

        // Crea el formato de fecha
        DateTimeFormatter formato = DateTimeFormatter.ofPattern(
                // Patrón: día de la semana, número, mes y año en texto
                "EEEE, dd 'de' MMMM 'del' yyyy",
                // Idioma español
                new Locale("es", "ES")
        );

        // Formatea la fecha de hoy
        String fecha = LocalDate.now().format(formato);

        // Capitaliza el día de la semana ("sábado" -> "Sábado")
        // Pone la primera letra en mayúscula y la devuelve
        return fecha.substring(0, 1).toUpperCase() + fecha.substring(1);
    }

}

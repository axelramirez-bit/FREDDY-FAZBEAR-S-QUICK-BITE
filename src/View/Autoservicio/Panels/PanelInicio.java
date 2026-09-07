package View.Autoservicio.Panels;

import Base.PanelProductos;
import View.Componentes.BarraBusqueda;
import View.Componentes.PanelRedondeado;
import View.Utils.AdministradorTema;
import View.Utils.FabricaBotones;
import View.Utils.FabricaEtiquetas;
import View.Utils.FabricaIconos;
import View.Utils.PaletaColores;
import View.Utils.UIConstants;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.function.Consumer;

/**
 * Pantalla de INICIO del Cliente: muestra TODOS los productos
 * disponibles del catálogo (sin filtrar por categoría ni por
 * promoción) y permite buscarlos por nombre con BarraBusqueda,
 * reutilizando ServicioBusqueda a través de PanelProductos.
 *
 * Antes este panel filtraba por "producto.getPromocion() != null"
 * (copiado de PanelPromociones), lo cual lo dejaba casi siempre
 * vacío y además no era el propósito de un panel de "Inicio".
 *
 * NUEVO: "Sugerir categoría por hora" (caso de uso 3.1 del diagrama
 * del Cajero) — nunca se había implementado en ningún panel del
 * proyecto. Vive aquí, en el Inicio del autoservicio, porque es la
 * primera pantalla que ve el cliente al sentarse a pedir: un banner
 * calcula la categoría del catálogo que mejor calza con la hora
 * actual (hamburguesas, pizza, café de la tarde, antojo de noche)
 * y ofrece saltar directo a esa categoría con un clic.
 *
 * La hora usada es SIEMPRE la hora local de Guatemala
 * (ZonedDateTime con ZoneId "America/Guatemala"), no la hora del
 * sistema operativo donde corra la app — así el banner no cambia
 * de sugerencia si alguien prueba el programa en una computadora
 * configurada en otro huso horario.
 */
public class PanelInicio extends PanelProductos {

    private BarraBusqueda barraBusqueda;

    // Permite navegar a la categoría sugerida sin que este panel
    // dependa de DashboardBase directamente — mismo patrón que ya
    // se usó para corregir los "Accesos rápidos" del Inicio del
    // Trabajador (ver PedidosProceso.registrarPaneles()).
    private final Consumer<String> navegador;

    /** Constructor de compatibilidad: sin navegación (el banner no tendrá botón activo). */
    public PanelInicio() {
        this(null);
    }

    public PanelInicio(Consumer<String> navegador) {

        // Sin filtro de categoría/promoción: muestra todo lo disponible.
        super(producto -> true);

        this.navegador = navegador;

        agregarEncabezado();
    }

    // ==========================================================
    // ENCABEZADO: banner de sugerencia + barra de búsqueda
    // ==========================================================
    private void agregarEncabezado() {

        JPanel encabezado = new JPanel(new BorderLayout(0, UIConstants.ESPACIO_PRODUCTOS));
        encabezado.setOpaque(false);
        encabezado.setBorder(
                javax.swing.BorderFactory.createEmptyBorder(
                        UIConstants.ESPACIO_PRODUCTOS,
                        UIConstants.ESPACIO_PRODUCTOS,
                        0,
                        UIConstants.ESPACIO_PRODUCTOS));

        encabezado.add(crearBannerSugerencia(), BorderLayout.NORTH);
        encabezado.add(crearBarraBusqueda(), BorderLayout.SOUTH);

        add(encabezado, BorderLayout.NORTH);
    }

    private JPanel crearBarraBusqueda() {

        barraBusqueda = new BarraBusqueda("Buscar en el menú...");

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);
        panel.add(barraBusqueda);

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

        return panel;
    }

    private void filtrar() {
        aplicarBusqueda(barraBusqueda.getTexto());
    }

    // ==========================================================
    // SUGERENCIA DE CATEGORÍA POR HORA (caso de uso 3.1)
    // ==========================================================
    private static final ZoneId ZONA_HORARIA_LOCAL = ZoneId.of("America/Guatemala");

    private JPanel crearBannerSugerencia() {

        LocalTime horaLocal = ZonedDateTime.now(ZONA_HORARIA_LOCAL).toLocalTime();
        Sugerencia sugerencia = sugerirSegunHora(horaLocal);

        PanelRedondeado banner = new PanelRedondeado(PaletaColores.SECUNDARIO);
        banner.setLayout(new BorderLayout(UIConstants.ESPACIO_PRODUCTOS, 0));
        banner.setBorder(
                javax.swing.BorderFactory.createEmptyBorder(
                        AdministradorTema.espacioMediano(), AdministradorTema.espacioGrande(),
                        AdministradorTema.espacioMediano(), AdministradorTema.espacioGrande()));

        JLabel icono = new JLabel(sugerencia.icono);
        banner.add(icono, BorderLayout.WEST);

        JPanel textos = new JPanel(new BorderLayout());
        textos.setOpaque(false);
        JLabel titulo = FabricaEtiquetas.crearSubtitulo(sugerencia.titulo);
        titulo.setForeground(PaletaColores.TEXTO);
        JLabel detalle = FabricaEtiquetas.crearTexto(sugerencia.detalle);
        detalle.setForeground(PaletaColores.TEXTO);
        textos.add(titulo, BorderLayout.NORTH);
        textos.add(detalle, BorderLayout.SOUTH);
        banner.add(textos, BorderLayout.CENTER);

        JButton btnVer = FabricaBotones.crearPrimario("Ver " + sugerencia.nombreCategoria);
        btnVer.setEnabled(navegador != null);
        btnVer.addActionListener(e -> {
            if (navegador != null) {
                navegador.accept(sugerencia.idVista);
            }
        });

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelBoton.setOpaque(false);
        panelBoton.add(btnVer);
        banner.add(panelBoton, BorderLayout.EAST);

        return banner;
    }

    /**
     * Franjas horarias típicas de un restaurante de comida rápida,
     * ya con el catálogo actualizado: "Hamburguesas" (antes
     * "Desayunos") y "Pizzas" (antes "Almuerzos y Cenas") — ver
     * migracion_categorias_hamburguesas_pizzas.sql. Las hamburguesas
     * se sugieren de mañana Y de mediodía (comida rápida a toda
     * hora); la pizza se deja para la noche.
     */
    private Sugerencia sugerirSegunHora(LocalTime hora) {

        if (esEntre(hora, 6, 11)) {
            return new Sugerencia(
                    "¡Buenos días! Arranca el día con energía",
                    "Nuestras hamburguesas también son una gran opción para desayunar.",
                    "Hamburguesas", "HAMBURGUESAS", FabricaIconos.hamburguesas()
            );
        }

        if (esEntre(hora, 11, 15)) {
            return new Sugerencia(
                    "Ya casi es mediodía",
                    "Una buena hamburguesa nunca falla a esta hora.",
                    "Hamburguesas", "HAMBURGUESAS", FabricaIconos.hamburguesas()
            );
        }

        if (esEntre(hora, 15, 18)) {
            return new Sugerencia(
                    "Un break de media tarde",
                    "Acompaña tu tarde con algo del McCafé.",
                    "McCafé", "MCCAFE", FabricaIconos.mcCafe()
            );
        }

        if (esEntre(hora, 18, 22)) {
            return new Sugerencia(
                    "Hora de cenar",
                    "Cierra el día compartiendo una pizza recién horneada.",
                    "Pizzas", "PIZZAS", FabricaIconos.pizzas()
            );
        }

        return new Sugerencia(
                "¿Antojo de última hora?",
                "Tenemos algo rápido y rico para ti.",
                "Antojos", "ANTOJOS", FabricaIconos.antojos()
        );
    }

    private boolean esEntre(LocalTime hora, int desdeHora, int hastaHoraExclusiva) {
        return !hora.isBefore(LocalTime.of(desdeHora, 0)) && hora.isBefore(LocalTime.of(hastaHoraExclusiva, 0));
    }

    private static final class Sugerencia {

        final String titulo;
        final String detalle;
        final String nombreCategoria;
        final String idVista;
        final ImageIcon icono;

        Sugerencia(String titulo, String detalle, String nombreCategoria, String idVista, ImageIcon icono) {
            this.titulo = titulo;
            this.detalle = detalle;
            this.nombreCategoria = nombreCategoria;
            this.idVista = idVista;
            this.icono = icono;
        }
    }

}
package View.ModelUI;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Modelo que representa una opción del menú lateral.
 *
 * Esta clase únicamente almacena la información necesaria para
 * construir un ItemMenu.
 *
 * Es utilizada por:
 *
 * • BarraLateral
 * • Menus
 *
 * ===============================================================
 */
public class OpcionMenu {

    //==========================================================
    // ATRIBUTOS
    //==========================================================

    /**
     * Texto mostrado.
     */
    private final String titulo;

    /**
     * Panel asociado.
     */
    private final String panel;

    /**
     * Nombre del icono.
     */
    private final String icono;

    /**
     * Indica si la opción inicia seleccionada.
     */
    private final boolean seleccionado;

    //==========================================================
    // CONSTRUCTOR
    //==========================================================

    public OpcionMenu(
            String titulo,
            String panel,
            String icono) {

        this(
                titulo,
                panel,
                icono,
                false);

    }

    public OpcionMenu(
            String titulo,
            String panel,
            String icono,
            boolean seleccionado) {

        this.titulo = titulo;
        this.panel = panel;
        this.icono = icono;
        this.seleccionado = seleccionado;

    }

    //==========================================================
    // GETTERS
    //==========================================================

    public String getTitulo() {
        return titulo;
    }

    public String getPanel() {
        return panel;
    }

    public String getIcono() {
        return icono;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    //==========================================================
    // TOSTRING
    //==========================================================

    @Override
    public String toString() {

        return titulo;

    }

}
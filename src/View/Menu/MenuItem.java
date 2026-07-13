package View.Menu;

import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * Representa una opción del menú lateral.
 *
 * Contiene:
 * - Texto mostrado.
 * - Icono.
 * - Evento al hacer clic.
 * - Estado habilitado.

 */
public class MenuItem {

    //==========================================================
    // ATRIBUTOS
    //==========================================================

    private String texto;

    private String icono;

    private ActionListener accion;

    private boolean habilitado;

    //==========================================================
    // CONSTRUCTORES
    //==========================================================

    /**
     * Constructor vacío.
     */
    public MenuItem() {

        this.habilitado = true;

    }

    /**
     * Constructor con texto e icono.
     *
     * @param texto Texto del menú.
     * @param icono Nombre del icono.
     */
    public MenuItem(String texto, String icono) {

        this();

        this.texto = texto;
        this.icono = icono;

    }

    /**
     * Constructor completo.
     *
     * @param texto Texto.
     * @param icono Icono.
     * @param accion Evento.
     */
    public MenuItem(
            String texto,
            String icono,
            ActionListener accion) {

        this(texto, icono);

        this.accion = accion;

    }

    //==========================================================
    // GETTERS
    //==========================================================

    public String getTexto() {
        return texto;
    }

    public String getIcono() {
        return icono;
    }

    public ActionListener getAccion() {
        return accion;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    //==========================================================
    // SETTERS
    //==========================================================

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public void setAccion(ActionListener accion) {
        this.accion = accion;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    //==========================================================
    // MÉTODOS
    //==========================================================

    /**
     * Ejecuta la acción asociada al menú.
     *
     * @param e Evento recibido.
     */
    public void ejecutar(java.awt.event.ActionEvent e) {

        if (accion != null && habilitado) {

            accion.actionPerformed(e);

        }

    }

    //==========================================================
    // EQUALS & HASHCODE
    //==========================================================

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof MenuItem)) {
            return false;
        }

        MenuItem otro = (MenuItem) obj;

        return Objects.equals(texto, otro.texto);

    }

    @Override
    public int hashCode() {

        return Objects.hash(texto);

    }

    //==========================================================
    // TO STRING
    //==========================================================

    @Override
    public String toString() {

        return texto;

    }

}
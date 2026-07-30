package Base;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Contrato de una opción de menú lateral.
 *
 * En lugar de escribir "Inicio", "Productos", "Pedidos"... como
 * Strings sueltos dentro de BarraLateral, cada rol define sus
 * opciones como un enum que implementa esta interfaz
 * (ver OpcionesCliente, OpcionesTrabajador, OpcionesAdministrador).
 *
 * Ventaja: si escribes mal el nombre de un icono o un idVista,
 * el error aparece una sola vez, en la definición del enum — no
 * en cada lugar donde se usa el texto.
 * ===============================================================
 */
public interface OpcionMenu {

    /**
     * Texto visible del botón de menú.
     */
    String getTexto();

    /**
     * Nombre del icono (sin ruta ni extensión), tal como lo espera
     * View.Utils.UtilImagenes.icono(nombre, tamaño).
     */
    String getNombreIcono();

    /**
     * Identificador de la vista que se registró en
     * View.Utils.ControlNavegacion. Es lo que se le pasa a
     * ControlNavegacion.abrir(idVista).
     */
    String getIdVista();

}
package View.Componentes;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Cualquier panel que implemente esto será refrescado automáticamente
 * por PanelContenido cada vez que el usuario navega hacia él —
 * sin esto, un panel construido una sola vez al abrir sesión se
 * queda con los datos de ese momento para siempre, y solo se ve
 * actualizado si el usuario presiona ↻ a mano o reinicia la app.
 *
 * Uso: que el panel implemente esta interfaz y exponga su método
 * de carga ya existente como cargarDatos() — la mayoría de los
 * paneles de Trabajador ya tienen ese método público, así que
 * suele bastar con agregar "implements Refrescable" a la clase.
 *
 * NO se usa en paneles donde perder el estado al navegar sería un
 * problema (ej. un formulario a medio llenar como "Tomar pedido"):
 * ese tipo de panel se deja fuera a propósito.
 * ===============================================================
 */
public interface Refrescable {
    void cargarDatos();
}
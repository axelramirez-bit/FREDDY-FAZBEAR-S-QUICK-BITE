// Paquete Base
package Base;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Devuelve las opciones de menú correspondientes a un rol.
 *
 * Gracias a esta clase, BarraLateral nunca necesita un
 * if/switch de roles: solo llama a
 * MenuPorRol.obtener(rol) y no le importa qué enum hay detrás.
 *
 * Agregar un rol nuevo en el futuro (ej. "Repartidor") significa
 * crear su enum OpcionesRepartidor y agregar un caso aquí — cero
 * cambios dentro de BarraLateral (principio Abierto/Cerrado).
 * ===============================================================
 */
// Clase que entrega las opciones de menú según el rol
public final class MenuPorRol {

    // Constructor privado: no se instancia
    private MenuPorRol() {
    }

    /**
     * Obtiene las opciones de menú de un rol.
     *
     * @param rol Rol para el cual se construye el menú.
     * @return Arreglo de opciones de menú.
     */
    // Devuelve las opciones de menú del rol recibido
    public static OpcionMenu[] obtener(Rol rol) {

        // Evalúa el rol
        switch (rol) {

            // Si es CLIENTE
            case CLIENTE:
                // devuelve las opciones de Cliente
                return OpcionesCliente.values();

            // Si es TRABAJADOR
            case TRABAJADOR:
                // devuelve las opciones de Trabajador
                return OpcionesTrabajador.values();

            // Si es ADMINISTRADOR
            case ADMINISTRADOR:
                // devuelve las opciones de Administrador
                return OpcionesAdministrador.values();

            // Cualquier otro rol
            default:
                // Lanza error de rol no soportado
                throw new IllegalArgumentException(
                        // Mensaje con el rol recibido
                        "Rol no soportado: " + rol
                );
        }
    }

}

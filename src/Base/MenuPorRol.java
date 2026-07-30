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
public final class MenuPorRol {

    private MenuPorRol() {
    }

    /**
     * Obtiene las opciones de menú de un rol.
     *
     * @param rol Rol para el cual se construye el menú.
     * @return Arreglo de opciones de menú.
     */
    public static OpcionMenu[] obtener(Rol rol) {

        switch (rol) {

            case CLIENTE:
                return OpcionesCliente.values();

            case TRABAJADOR:
                return OpcionesTrabajador.values();

            case ADMINISTRADOR:
                return OpcionesAdministrador.values();

            default:
                throw new IllegalArgumentException(
                        "Rol no soportado: " + rol
                );
        }
    }

}

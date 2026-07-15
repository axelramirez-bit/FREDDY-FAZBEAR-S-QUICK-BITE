package View.Utils;

import javax.swing.ImageIcon;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Fábrica oficial de iconos del sistema.
 *
 * Responsabilidades:
 *
 * • Centralizar todos los iconos.
 * • Mantener un tamaño uniforme.
 * • Evitar escribir nombres de archivos en las vistas.
 * • Trabajar junto con CacheImagenes.
 *
 * Ninguna vista debe cargar iconos directamente.
 * ===============================================================
 */
public final class FabricaIconos {



    private FabricaIconos(){}

    public static ImageIcon crear(String nombreIcono, int iconoMediano){

        return UtilImagenes.icono(nombreIcono, iconoMediano);

    }
    public static ImageIcon crear(
        String nombre,
        int ancho,
        int alto) {

    return UtilImagenes.imagen(
            nombre,
            ancho,
            alto);

}

    public static ImageIcon pequeño(String nombre){

        return crear(
                nombre,
                AdministradorTema.iconoPequeño());

    }

    public static ImageIcon mediano(String nombre){

        return crear(
                nombre,
                AdministradorTema.iconoMediano());

    }

    public static ImageIcon grande(String nombre){

        return crear(
                nombre,
                AdministradorTema.iconoGrande());

    }

    public static ImageIcon menu(String nombre){

        return crear(
                nombre,
                AdministradorTema.iconoMenu());

    }


    // ==========================================================
    // MÉTODO GENERAL
    // ==========================================================

    /**
     * Devuelve un icono con el tamaño indicado.
     *
     * @param nombre Nombre del archivo.
     * @param tamaño Tamaño del icono.
     * @return ImageIcon.
     */
    private static ImageIcon icono(
            String nombre,
            int tamaño) {

        return CacheImagenes.obtenerIcono(
                nombre,
                tamaño
        );

    }

    /**
     * Devuelve un icono con el tamaño mediano.
     *
     * @param nombre Nombre del archivo.
     * @return ImageIcon.
     */
    private static ImageIcon icono(String nombre) {

        return icono(
                nombre,
                UIConstants.ICONO_MEDIANO
        );

    }

    // ==========================================================
    // ADMINISTRADOR
    // ==========================================================

    public static ImageIcon dashboard() {
        return icono("icon_admin");
    }

    public static ImageIcon usuarios() {
        return icono("icon_usuarios");
    }

    public static ImageIcon trabajadores() {
        return icono("icon_trabajadores");
    }

    public static ImageIcon productos() {
        return icono("icon_comida");
    }

    public static ImageIcon promociones() {
        return icono("icon_promociones");
    }

    public static ImageIcon pedidos() {
        return icono("icon_pedidos");
    }

    public static ImageIcon pagos() {
        return icono("icon_pagos");
    }

    public static ImageIcon ventas() {
        return icono("icon_ventas");
    }

    public static ImageIcon reportes() {
        return icono("icon_reportes");
    }

    // ==========================================================
    // CLIENTE
    // ==========================================================

    public static ImageIcon inicio() {
        return icono("icon_inicio");
    }

    public static ImageIcon desayunos() {
        return icono("icon_desayunos");
    }

    public static ImageIcon almuerzos() {
        return icono("icon_almuerzoscenas");
    }

    public static ImageIcon bebidas() {
        return icono("icon_bebidas");
    }

    public static ImageIcon mcCafe() {
        return icono("icon_mccafe");
    }

    public static ImageIcon postres() {
        return icono("icon_postres");
    }

    public static ImageIcon antojos() {
        return icono("icon_antojos");
    }

    public static ImageIcon cajitaFeliz() {
        return icono("icon_cajitafeliz");
    }

    public static ImageIcon carrito() {
        return icono("icon_carrito");
    }

    // ==========================================================
    // TRABAJADOR
    // ==========================================================

    public static ImageIcon pedidosPendientes() {
        return icono("icon_pedidos");
    }

    public static ImageIcon enPreparacion() {
        return icono("icon_enpreparacion");
    }

    public static ImageIcon pedidosListos() {
        return icono("icon_pedidoslistos");
    }

    public static ImageIcon historial() {
        return icono("icon_historial");
    }

    // ==========================================================
    // ACCIONES
    // ==========================================================

    public static ImageIcon buscar() {
        return icono(
                "icon_buscar",
                UIConstants.ICONO_PEQUEÑO
        );
    }

    public static ImageIcon agregar() {
        return icono(
                "icon_agregar",
                UIConstants.ICONO_PEQUEÑO
        );
    }

    public static ImageIcon editar() {
        return icono(
                "icon_editar",
                UIConstants.ICONO_PEQUEÑO
        );
    }

    public static ImageIcon eliminar() {
        return icono(
                "icon_eliminar",
                UIConstants.ICONO_PEQUEÑO
        );
    }

    public static ImageIcon guardar() {
        return icono(
                "icon_guardar",
                UIConstants.ICONO_PEQUEÑO
        );
    }

    public static ImageIcon regresar() {
        return icono(
                "icon_regresar",
                UIConstants.ICONO_PEQUEÑO
        );
    }

    public static ImageIcon confirmar() {
        return icono(
                "icon_confirmar",
                UIConstants.ICONO_PEQUEÑO
        );
    }

    public static ImageIcon cancelar() {
        return icono(
                "icon_cancelar",
                UIConstants.ICONO_PEQUEÑO
        );
    }

    // ==========================================================
    // ESTADOS
    // ==========================================================

    public static ImageIcon pendiente() {
        return icono("icon_pedidos");
    }

    public static ImageIcon preparando() {
        return icono("icon_enpreparacion");
    }

    public static ImageIcon listo() {
        return icono("icon_pedidoslistos");
    }

    // ==========================================================
    // GENERAL
    // ==========================================================

    public static ImageIcon cerrarSesion() {
        return icono("icon_cerrarsesion");
    }

    public static ImageIcon logo() {
        return CacheImagenes.obtenerLogotipo();
    }

}
package View.Utils;

import View.ModelUI.OpcionMenu;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Define todas las opciones de los menús laterales de la aplicación.
 *
 * Ningún Dashboard debe escribir manualmente los Items.
 *
 * ===============================================================
 */
public final class Menus {

    private Menus() {
    }

    //==========================================================
    // CLIENTE
    //==========================================================

    public static final OpcionMenu[] MENU_CLIENTE = {

        new OpcionMenu(
                "Desayunos",
                "DESAYUNOS",
                "icon_desayunos",
                true),

        new OpcionMenu(
                "Almuerzos y cenas",
                "ALMUERZOS",
                "icon_almuerzos"),

        new OpcionMenu(
                "Postres",
                "POSTRES",
                "icon_postres"),

        new OpcionMenu(
                "FazCafe",
                "FAZCAFE",
                "icon_fazcafe"),

        new OpcionMenu(
                "Bebidas",
                "BEBIDAS",
                "icon_bebidas"),

        new OpcionMenu(
                "Antojos",
                "ANTOJOS",
                "icon_antojos"),

        new OpcionMenu(
                "Cajita Feliz",
                "CAJITA_FELIZ",
                "icon_cajita"),

        new OpcionMenu(
                "Promociones",
                "PROMOCIONES",
                "icon_promociones"),

        new OpcionMenu(
                "Carrito",
                "CARRITO",
                "icon_carrito")

    };

    //==========================================================
    // TRABAJADOR
    //==========================================================

    public static final OpcionMenu[] MENU_TRABAJADOR = {

        new OpcionMenu(
                "Inicio",
                "INICIO",
                "icon_inicio",
                true),

        new OpcionMenu(
                "Pedidos Pendientes",
                "PEDIDOS_PENDIENTES",
                "icon_pedidos"),

        new OpcionMenu(
                "En Preparación",
                "EN_PREPARACION",
                "icon_preparacion"),

        new OpcionMenu(
                "Pedidos Listos",
                "PEDIDOS_LISTOS",
                "icon_listos"),

        new OpcionMenu(
                "Historial",
                "HISTORIAL",
                "icon_historial")

    };

    //==========================================================
    // ADMINISTRADOR
    //==========================================================

    public static final OpcionMenu[] MENU_ADMINISTRADOR = {

        new OpcionMenu(
                "Dashboard",
                "DASHBOARD",
                "icon_dashboard",
                true),

        new OpcionMenu(
                "Usuarios",
                "USUARIOS",
                "icon_usuarios"),

        new OpcionMenu(
                "Trabajadores",
                "TRABAJADORES",
                "icon_trabajadores"),

        new OpcionMenu(
                "Productos",
                "PRODUCTOS",
                "icon_productos"),

        new OpcionMenu(
                "Pagos",
                "PAGOS",
                "icon_pagos"),

        new OpcionMenu(
                "Promociones",
                "PROMOCIONES",
                "icon_promociones"),

        new OpcionMenu(
                "Pedidos",
                "PEDIDOS",
                "icon_pedidos"),

        new OpcionMenu(
                "Ventas",
                "VENTAS",
                "icon_ventas"),

        new OpcionMenu(
                "Reportes",
                "REPORTES",
                "icon_reportes")

    };

}
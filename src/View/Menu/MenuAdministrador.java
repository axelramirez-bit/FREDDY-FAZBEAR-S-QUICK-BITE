/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.Menu;

import java.util.ArrayList;
import java.util.List;

public class MenuAdministrador {
    public static List<MenuItem> obtener() {

    List<MenuItem> menu = new ArrayList<>();

    menu.add(new MenuItem(
            "Inicio",
            "icon_Inicio"));

    menu.add(new MenuItem(
            "Usuarios",
            "icon_usuarios"));

    menu.add(new MenuItem(
            "Trabajadores",
            "icon_trabajadores"));

    menu.add(new MenuItem(
            "Ventas",
            "icon_ventas"));

    return menu;

}
}

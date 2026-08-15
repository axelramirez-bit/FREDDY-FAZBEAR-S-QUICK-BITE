package View.Utils;

import java.awt.*;
import javax.swing.*;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Fábrica de menús reutilizables.
 *
 * Contiene:
 *
 * • JMenuBar
 * • JMenu
 * • JMenuItem
 * • JCheckBoxMenuItem
 * • JRadioButtonMenuItem
 * • Separadores
 *
 * ===============================================================
 */
public final class FabricaMenus {

    private FabricaMenus() {
    }

    // ==========================================================
    // MENU BAR
    // ==========================================================

    public static JMenuBar crearBarra() {

        JMenuBar barra = new JMenuBar();

        barra.setBackground(
                AdministradorTema.colorTarjeta());

        barra.setBorder(
                BorderFactory.createEmptyBorder(
                        4,
                        8,
                        4,
                        8));

        return barra;

    }

    // ==========================================================
    // MENU
    // ==========================================================

    public static JMenu crearMenu(
            String texto) {

        JMenu menu = new JMenu(texto);

        aplicar(menu);

        return menu;

    }

    public static JMenu crearMenu(
            String texto,
            Icon icono) {

        JMenu menu =
                crearMenu(texto);

        menu.setIcon(icono);

        return menu;

    }

    // ==========================================================
    // ITEM
    // ==========================================================

    public static JMenuItem crearItem(
            String texto) {

        JMenuItem item =
                new JMenuItem(texto);

        aplicar(item);

        return item;

    }

    public static JMenuItem crearItem(
            String texto,
            Icon icono) {

        JMenuItem item =
                crearItem(texto);

        item.setIcon(icono);

        return item;

    }

    // ==========================================================
    // CHECK ITEM
    // ==========================================================

    public static JCheckBoxMenuItem crearCheckItem(
            String texto) {

        JCheckBoxMenuItem item =
                new JCheckBoxMenuItem(texto);

        aplicar(item);

        return item;

    }

    // ==========================================================
    // RADIO ITEM
    // ==========================================================

    public static JRadioButtonMenuItem crearRadioItem(
            String texto) {

        JRadioButtonMenuItem item =
                new JRadioButtonMenuItem(texto);

        aplicar(item);

        return item;

    }

    // ==========================================================
    // SEPARADOR
    // ==========================================================

    public static JPopupMenu.Separator crearSeparador() {

        return new JPopupMenu.Separator();

    }

    // ==========================================================
    // POPUP MENU
    // ==========================================================

    public static JPopupMenu crearPopup() {

        JPopupMenu popup =
                new JPopupMenu();

        popup.setBackground(
                AdministradorTema.colorTarjeta());

        return popup;

    }

    // ==========================================================
    // ESTILO
    // ==========================================================

    private static void aplicar(AbstractButton boton) {

        boton.setFont(
                AdministradorTema.fuenteNormal());

        boton.setBackground(
                AdministradorTema.colorTarjeta());

        boton.setForeground(
                AdministradorTema.colorTexto());

        boton.setCursor(
                new Cursor(Cursor.HAND_CURSOR));

        boton.setFocusPainted(false);

    }

}
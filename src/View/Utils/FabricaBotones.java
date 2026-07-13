package View.Utils;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Fábrica especializada en la creación de botones.
 *
 * Responsabilidades:
 *
 * • Crear botones primarios.
 * • Crear botones secundarios.
 * • Crear botones del menú.
 * • Crear botones con icono.
 * • Crear botones personalizados.
 *
 * La apariencia es aplicada por EstilosComponentes.
 * ===============================================================
 */
public final class FabricaBotones {

    private FabricaBotones() {
    }

    // ==========================================================
    // BOTÓN PRIMARIO
    // ==========================================================

    public static JButton crearPrimario(String texto) {

        JButton boton = new JButton(texto);

        EstilosComponentes.aplicarBotonPrimario(boton);

        EstilosComponentes.aplicarTamañoPreferido(
                boton,
                AdministradorTema.anchoBoton(),
                AdministradorTema.alturaBoton());

        return boton;

    }

    // ==========================================================
    // BOTÓN SECUNDARIO
    // ==========================================================

    public static JButton crearSecundario(String texto) {

        JButton boton = new JButton(texto);

        EstilosComponentes.aplicarBotonSecundario(boton);

        EstilosComponentes.aplicarTamañoPreferido(
                boton,
                AdministradorTema.anchoBoton(),
                AdministradorTema.alturaBoton());

        return boton;

    }

    // ==========================================================
    // BOTÓN DEL MENÚ
    // ==========================================================

    public static JButton crearMenu(
            String texto,
            ImageIcon icono) {

        JButton boton = new JButton(texto);

        boton.setIcon(icono);

        EstilosComponentes.aplicarBotonMenu(boton);

        EstilosComponentes.aplicarTamañoPreferido(
                boton,
                AdministradorTema.anchoMenu()
                        - AdministradorTema.margenMenu(),
                AdministradorTema.alturaBotonMenu());

        return boton;

    }

    // ==========================================================
    // BOTÓN CON ICONO
    // ==========================================================

    public static JButton crearIcono(
            String texto,
            ImageIcon icono) {

        JButton boton = new JButton(texto);

        boton.setIcon(icono);

        EstilosComponentes.aplicarBotonIcono(boton);

        EstilosComponentes.aplicarTamañoPreferido(
                boton,
                AdministradorTema.anchoBotonIcono(),
                AdministradorTema.alturaBoton());

        return boton;

    }

    // ==========================================================
    // BOTÓN SOLO ICONO
    // ==========================================================

    public static JButton crearSoloIcono(
            ImageIcon icono) {

        JButton boton = new JButton(icono);

        EstilosComponentes.aplicarBotonSecundario(boton);

        EstilosComponentes.aplicarTamañoPreferido(
                boton,
                AdministradorTema.alturaBoton(),
                AdministradorTema.alturaBoton());

        return boton;

    }

    // ==========================================================
    // BOTÓN PERSONALIZADO
    // ==========================================================

    public static JButton crearPersonalizado(
            String texto,
            int ancho,
            int alto) {

        JButton boton = new JButton(texto);

        EstilosComponentes.aplicarBotonPrimario(boton);

        EstilosComponentes.aplicarTamañoPreferido(
                boton,
                ancho,
                alto);

        return boton;

    }

    // ==========================================================
    // BOTÓN PERSONALIZADO CON ICONO
    // ==========================================================

    public static JButton crearPersonalizado(
            String texto,
            ImageIcon icono,
            int ancho,
            int alto) {

        JButton boton = new JButton(texto);

        boton.setIcon(icono);

        EstilosComponentes.aplicarBotonIcono(boton);

        EstilosComponentes.aplicarTamañoPreferido(
                boton,
                ancho,
                alto);

        return boton;

    }

    // ==========================================================
    // BOTÓN DE ANCHO COMPLETO
    // ==========================================================

    /**
     * Botón que ocupa todo el ancho disponible.
     */
    public static JButton crearExpandible(
            String texto) {

        JButton boton = new JButton(texto);

        EstilosComponentes.aplicarBotonPrimario(boton);

        EstilosComponentes.aplicarAlto(
                boton,
                AdministradorTema.alturaBoton());

        boton.setMaximumSize(
                new java.awt.Dimension(
                        Integer.MAX_VALUE,
                        AdministradorTema.alturaBoton()));

        return boton;

    }

    // ==========================================================
    // BOTÓN DE ACCIÓN PEQUEÑO
    // ==========================================================

    /**
     * Botón utilizado dentro de tablas o tarjetas.
     */
    public static JButton crearAccion(
            String texto) {

        JButton boton = new JButton(texto);

        EstilosComponentes.aplicarBotonSecundario(boton);

        EstilosComponentes.aplicarTamañoPreferido(
                boton,
                UIConstants.ANCHO_MINIMO_BOTON,
                AdministradorTema.alturaBoton());

        return boton;

    }

}
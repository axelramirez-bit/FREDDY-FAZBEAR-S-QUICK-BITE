package View.Componentes;

import View.Utils.AdministradorTema;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Locale;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Badge/píldora de estado con color, como los que se ven en TODOS
 * los bocetos: "Pendiente" naranja, "Listo" verde, "Cancelado"
 * rojo, "Activa" verde, "Programada" azul, etc.
 *
 * Es UN solo componente para las 10 pantallas de Administrador
 * (Pedidos, Pagos, Promociones, Productos, Inventario...) — no
 * una clase de badge distinta por pantalla.
 *
 * USO DIRECTO (fuera de una tabla, ej. dentro de una tarjeta):
 *
 *     panel.add(EtiquetaEstado.exito("Activa"));
 *     panel.add(EtiquetaEstado.peligro("Cancelado"));
 *
 * USO AUTOMÁTICO (deja que el componente adivine el color según
 * el texto — cubre los estados más comunes del proyecto):
 *
 *     panel.add(EtiquetaEstado.automatico("Preparacion"));
 *     // → detecta "preparacion" y la pinta de advertencia (ámbar)
 *
 * Para pintar una columna completa de una JTable como badges, no
 * uses este componente directamente en convertirFila() (un
 * Object[] de JTable no puede contener un JLabel con estilo
 * propio) — usa RenderizadorEstado, que sí sabe pintar celdas.
 * ===============================================================
 */
public class EtiquetaEstado extends JLabel {

    public EtiquetaEstado(String texto, Color colorTexto, Color colorFondo) {

        super(texto, SwingConstants.CENTER);

        setOpaque(false);

        setForeground(colorTexto);

        this.colorFondo = colorFondo;

        setFont(getFont().deriveFont(getFont().getSize2D() - 1f).deriveFont(java.awt.Font.BOLD));

        setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));

    }

    private final Color colorFondo;

    // ==========================================================
    // FÁBRICAS SEMÁNTICAS
    //
    // Uso recomendado en vez de pasar colores sueltos a mano —
    // así, si el proyecto cambia de paleta algún día, solo se
    // toca AdministradorTema y estos 5 métodos siguen sirviendo.
    // ==========================================================

    public static EtiquetaEstado exito(String texto) {
        return new EtiquetaEstado(
                texto,
                AdministradorTema.colorEstadoExito(),
                AdministradorTema.colorEstadoExitoFondo());
    }

    public static EtiquetaEstado advertencia(String texto) {
        return new EtiquetaEstado(
                texto,
                AdministradorTema.colorEstadoAdvertencia(),
                AdministradorTema.colorEstadoAdvertenciaFondo());
    }

    public static EtiquetaEstado peligro(String texto) {
        return new EtiquetaEstado(
                texto,
                AdministradorTema.colorEstadoPeligro(),
                AdministradorTema.colorEstadoPeligroFondo());
    }

    public static EtiquetaEstado info(String texto) {
        return new EtiquetaEstado(
                texto,
                AdministradorTema.colorEstadoInfo(),
                AdministradorTema.colorEstadoInfoFondo());
    }

    public static EtiquetaEstado neutro(String texto) {
        return new EtiquetaEstado(
                texto,
                AdministradorTema.colorEstadoNeutro(),
                AdministradorTema.colorEstadoNeutroFondo());
    }

    // ==========================================================
    // FÁBRICA AUTOMÁTICA
    //
    // Adivina el color según palabras clave en español que ya
    // usan las tablas reales del proyecto (pedido.estado,
    // pago.estado, promocion "calculada", producto.disponible,
    // usuario.estado). Si el texto no calza con ninguna palabra
    // conocida, regresa neutro() en vez de fallar.
    //
    // No es magia: es exactamente el mismo mapeo que ibas a
    // escribir a mano en cada pantalla, centralizado una sola
    // vez. Si una pantalla necesita un color distinto para un
    // mismo texto, usa las fábricas semánticas de arriba en vez
    // de esta.
    // ==========================================================

    public static EtiquetaEstado automatico(String textoOriginal) {

        String texto = textoOriginal == null ? "" : textoOriginal.trim();

        String normalizado = texto
                .toLowerCase(Locale.forLanguageTag("es"))
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u");

        // ---- positivos / éxito ----
        if (normalizado.contains("listo")
                || normalizado.contains("activ")
                || normalizado.contains("disponible")
                || normalizado.contains("entregado")
                || normalizado.contains("pagado")
                || normalizado.contains("completado")
                || normalizado.contains("normal")
                || normalizado.equals("si")) {
            return exito(texto);
        }

        // ---- advertencia / en proceso ----
        if (normalizado.contains("preparacion")
                || normalizado.contains("pendiente")
                || normalizado.contains("bajo")
                || normalizado.contains("por vencer")
                || normalizado.contains("proxima")) {
            return advertencia(texto);
        }

        // ---- negativos / peligro ----
        if (normalizado.contains("cancelado")
                || normalizado.contains("rechazado")
                || normalizado.contains("agotado")
                || normalizado.contains("sin stock")
                || normalizado.contains("vencid")
                || normalizado.contains("inactiv")
                || normalizado.equals("no")) {
            return peligro(texto);
        }

        // ---- informativos ----
        if (normalizado.contains("programada")
                || normalizado.contains("nuevo")) {
            return info(texto);
        }

        return neutro(texto);
    }

    // ==========================================================
    // DIBUJADO DE LA PÍLDORA
    // ==========================================================

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(colorFondo);

        int radio = getHeight();

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);

        g2.dispose();

        super.paintComponent(g);
    }

}
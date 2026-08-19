package View.Componentes;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * LayoutManager para el grid de TarjetaProducto que reemplaza al
 * GridLayout(0, 2, ...) que usaba Base.PanelProductos.
 *
 * BUG QUE ESTO CORRIGE: GridLayout reparte el ancho (Y el alto)
 * del contenedor en partes iguales entre TODAS las celdas,
 * IGNORANDO por completo el tamaño preferido de cada tarjeta
 * (AdministradorTema.anchoTarjetaProducto()/altoTarjetaProducto()).
 * Eso traía dos problemas encadenados:
 *
 *  1) SIEMPRE se mostraban 2 columnas, sin importar si la ventana
 *     era angosta (pantalla chica) o ancha (pantalla grande): el
 *     usuario pedía 1 columna en pantallas chicas y 2 en pantallas
 *     grandes, manteniendo el tamaño de tarjeta tal cual está
 *     definido en el tema (nada de estirar/encoger la tarjeta).
 *
 *  2) Como GridLayout también reparte el ALTO del contenedor entre
 *     todas las filas por igual, cada fila terminaba con una altura
 *     = altoDisponible / numeroDeFilas. Con varios productos (varias
 *     filas) esa altura por fila queda muy por debajo del alto real
 *     que necesita una TarjetaProducto (imagen + nombre + descripción
 *     + precio + fila de cantidad + botón "Agregar al carrito"). El
 *     BoxLayout interno de TarjetaProducto no tiene a dónde reducir
 *     ese contenido, así que lo que se ve "cortado"/invisible primero
 *     es justo lo último del panel: la fila de SelectorCantidad y el
 *     botón, porque quedan fuera del área real que GridLayout le dio
 *     a la tarjeta. Esto es lo que se reportaba como "el selector de
 *     cantidad no aparece": no es un bug del propio SelectorCantidad,
 *     es la tarjeta contenedora encogida por GridLayout.
 *
 * SOLUCIÓN: esta clase NUNCA cambia el tamaño de las tarjetas (usa
 * siempre su getPreferredSize() real, tal como venía definido). Lo
 * único que decide según el ancho disponible del viewport es CUÁNTAS
 * columnas entran (1 en pantallas chicas, 2 en pantallas grandes,
 * hasta un máximo configurable) y arma tantas filas como haga falta,
 * cada una con la altura real de la tarjeta. Así el ancho SÍ es
 * responsivo, pero el tamaño de cada tarjeta se respeta siempre.
 * ===============================================================
 */
public class RejillaResponsiva implements LayoutManager {

    private final int espacio;

    private final int columnasMaximas;

    public RejillaResponsiva(int espacio, int columnasMaximas) {
        this.espacio = espacio;
        this.columnasMaximas = Math.max(1, columnasMaximas);
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {

        synchronized (parent.getTreeLock()) {

            Insets insets = parent.getInsets();
            Dimension tarjeta = tamañoTarjeta(parent);

            if (tarjeta == null) {
                return new Dimension(
                        insets.left + insets.right,
                        insets.top + insets.bottom
                );
            }

            int anchoDisponible = anchoObjetivo(parent);

            int columnas = calcularColumnas(anchoDisponible, tarjeta.width);

            int visibles = contarVisibles(parent);

            int filas = (int) Math.ceil(visibles / (double) columnas);

            int ancho = insets.left + insets.right
                    + (columnas * tarjeta.width)
                    + (Math.max(0, columnas - 1) * espacio);

            int alto = insets.top + insets.bottom
                    + (filas * tarjeta.height)
                    + (Math.max(0, filas - 1) * espacio);

            return new Dimension(ancho, alto);
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    @Override
    public void layoutContainer(Container parent) {

        synchronized (parent.getTreeLock()) {

            Insets insets = parent.getInsets();
            Dimension tarjeta = tamañoTarjeta(parent);

            if (tarjeta == null) {
                return;
            }

            int anchoDisponible = parent.getWidth() - insets.left - insets.right;

            if (anchoDisponible <= 0) {
                anchoDisponible = anchoObjetivo(parent);
            }

            int columnas = calcularColumnas(anchoDisponible, tarjeta.width);

            // Espacio sobrante repartido entre columnas para que la
            // fila no quede pegada a la izquierda cuando el viewport
            // es más ancho que "columnas * tarjeta". La tarjeta NUNCA
            // se estira: solo se separa más de la siguiente.
            int anchoUsado = (columnas * tarjeta.width) + (Math.max(0, columnas - 1) * espacio);
            int sobrante = Math.max(0, anchoDisponible - anchoUsado);
            int espacioExtra = columnas > 1 ? sobrante / columnas : 0;

            int x = insets.left;
            int y = insets.top;
            int columnaActual = 0;

            int n = parent.getComponentCount();

            for (int i = 0; i < n; i++) {

                Component c = parent.getComponent(i);

                if (!c.isVisible()) {
                    continue;
                }

                Dimension d = c.getPreferredSize();

                c.setBounds(x, y, d.width, d.height);

                columnaActual++;

                if (columnaActual >= columnas) {
                    columnaActual = 0;
                    x = insets.left;
                    y += d.height + espacio;
                } else {
                    x += d.width + espacio + espacioExtra;
                }
            }
        }
    }

    // ==========================================================
    // UTILIDADES
    // ==========================================================

    private int calcularColumnas(int anchoDisponible, int anchoTarjeta) {

        if (anchoDisponible <= 0 || anchoTarjeta <= 0) {
            return columnasMaximas;
        }

        for (int columnas = columnasMaximas; columnas > 1; columnas--) {

            int necesario = (columnas * anchoTarjeta) + ((columnas - 1) * espacio);

            if (anchoDisponible >= necesario) {
                return columnas;
            }
        }

        return 1;
    }

    // Ancho objetivo cuando el contenedor todavía no tiene un ancho
    // real asignado (por ejemplo, la primera vez que un JScrollPane
    // pide preferredLayoutSize antes de hacer el primer layout). Se
    // sube por la jerarquía de contenedores buscando uno con ancho
    // real (típicamente el JViewport) para no calcular siempre con
    // el máximo de columnas.
    private int anchoObjetivo(Container parent) {

        if (parent.getWidth() > 0) {
            return parent.getWidth() - parent.getInsets().left - parent.getInsets().right;
        }

        Container padre = parent.getParent();

        while (padre != null) {

            if (padre.getWidth() > 0) {
                return padre.getWidth();
            }

            padre = padre.getParent();
        }

        Dimension tarjeta = tamañoTarjeta(parent);

        if (tarjeta == null) {
            return Integer.MAX_VALUE;
        }

        return (columnasMaximas * tarjeta.width) + ((columnasMaximas - 1) * espacio);
    }

    private Dimension tamañoTarjeta(Container parent) {

        int n = parent.getComponentCount();

        for (int i = 0; i < n; i++) {

            Component c = parent.getComponent(i);

            if (c.isVisible()) {
                return c.getPreferredSize();
            }
        }

        return null;
    }

    private int contarVisibles(Container parent) {

        int visibles = 0;

        int n = parent.getComponentCount();

        for (int i = 0; i < n; i++) {

            if (parent.getComponent(i).isVisible()) {
                visibles++;
            }
        }

        return Math.max(1, visibles);
    }

}
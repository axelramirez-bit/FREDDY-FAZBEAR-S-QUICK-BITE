package View.Componentes;

import Model.Producto;
import Service.Implement.ProductoServiceImpl;
import Service.Interfaz.IProductoService;
import View.Utils.AdministradorTema;
import View.Utils.PaletaColores;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Fila de alerta "N productos en stock bajo", pensada para vivir
 * dentro de la tarjeta "Alertas importantes" de CUALQUIER dashboard
 * (Trabajador y Administrador la usan igual). Es una sola clase
 * para no duplicar el umbral de stock bajo ni el texto en dos
 * lugares que se puedan desincronizar.
 *
 * El Trabajador la ve en modo solo-lectura (necesita saber que hay
 * poco stock para avisarle al cliente o priorizar), el
 * Administrador la ve igual, pero es quien de verdad puede entrar
 * a "Gestionar productos" a reponer/ajustar el stock — eso ya lo
 * resuelve el menú lateral de cada rol, esta clase no necesita
 * saber nada de permisos.
 * ===============================================================
 */
public class AlertaStockBajo extends JLabel {

    /**
     * Umbral de "stock bajo". Un solo lugar para cambiarlo si el
     * negocio decide otro número (ej. distinto por producto) —
     * hoy es fijo y global, igual que en el mockup revisado.
     */
    public static final int UMBRAL_STOCK_BAJO = 5;

    private final IProductoService productoService = new ProductoServiceImpl();

    public AlertaStockBajo() {

        setFont(AdministradorTema.fuentePequeña());

        actualizar();
    }

    /**
     * Vuelve a consultar productos y refresca el texto/color.
     * Llamarlo cada vez que se abra o refresque el dashboard.
     */
    public void actualizar() {

        List<Producto> productosStockBajo = obtenerProductosStockBajo();

        int cantidad = productosStockBajo.size();

        if (cantidad == 0) {

            setText("✔ Stock de productos en buen nivel.");
            setForeground(PaletaColores.ACENTO);

        } else {

            String nombres = productosStockBajo.stream()
                    .limit(3)
                    .map(Producto::getNombre)
                    .collect(Collectors.joining(", "));

            String detalle = cantidad > 3 ? nombres + "…" : nombres;

            setText("⚠ " + cantidad + " producto" + (cantidad == 1 ? "" : "s")
                    + " en stock bajo (" + detalle + ")");
            setForeground(new Color(0xC77700));
        }

        setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
    }

    /**
     * Cantidad de productos en stock bajo, para usar en tarjetas KPI
     * o títulos sin tener que crear una instancia del componente visual.
     */
    public static int contarProductosStockBajo() {

        return (int) new ProductoServiceImpl().listarProductosDisponibles().stream()
                .filter(p -> p.getStock() <= UMBRAL_STOCK_BAJO)
                .count();
    }

    private List<Producto> obtenerProductosStockBajo() {

        return productoService.listarProductosDisponibles().stream()
                .filter(p -> p.getStock() <= UMBRAL_STOCK_BAJO)
                .collect(Collectors.toList());
    }
}

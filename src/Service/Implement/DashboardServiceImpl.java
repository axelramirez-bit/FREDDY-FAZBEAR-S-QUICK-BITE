package Service.Implement;

import Model.DetallePedido;
import Model.EstadoPedido;
import Model.Pedido;
import Service.Interfaz.IDashboardService;
import Service.Interfaz.IPedidoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Implementación de IDashboardService. Solo lee de IPedidoService
 * y calcula en memoria — no hace INSERT/UPDATE/DELETE, no
 * pertenece aquí ninguna regla de negocio que modifique un
 * Pedido (eso es de PedidoService).
 * ===============================================================
 */
public class DashboardServiceImpl implements IDashboardService {

    private final IPedidoService pedidoService;

    public DashboardServiceImpl(IPedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Override
    public Map<String, BigDecimal> ventasPorDia(int diasAtras) {

        Map<String, BigDecimal> ventas = new LinkedHashMap<>();

        LocalDate hoy = LocalDate.now();
        Locale espanol = new Locale("es");

        for (int i = diasAtras - 1; i >= 0; i--) {
            LocalDate dia = hoy.minusDays(i);
            String etiqueta = dia.getDayOfWeek().getDisplayName(TextStyle.SHORT, espanol);
            ventas.put(etiqueta, BigDecimal.ZERO);
        }

        LocalDate limiteInferior = hoy.minusDays(diasAtras - 1L);

        for (Pedido pedido : pedidosValidos()) {

            LocalDate fechaPedido = pedido.getFecha().toLocalDate();

            if (fechaPedido.isBefore(limiteInferior)) {
                continue;
            }

            String etiqueta = fechaPedido.getDayOfWeek().getDisplayName(TextStyle.SHORT, espanol);

            BigDecimal acumulado = ventas.getOrDefault(etiqueta, BigDecimal.ZERO);

            ventas.put(etiqueta, acumulado.add(pedido.getTotal()));
        }

        return ventas;
    }

    @Override
    public Map<String, BigDecimal> ventasPorCategoria() {

        Map<String, BigDecimal> ventas = new LinkedHashMap<>();

        for (Pedido pedido : pedidosValidos()) {
            for (DetallePedido detalle : pedido.getDetalles()) {

                String categoria = detalle.getProducto().getCategoria().getNombre();

                BigDecimal acumulado = ventas.getOrDefault(categoria, BigDecimal.ZERO);

                ventas.put(categoria, acumulado.add(detalle.getSubtotal()));
            }
        }

        return ventas;
    }

    @Override
    public Map<String, Integer> productosMasVendidos(int cantidadTop) {

        Map<String, Integer> conteos = new LinkedHashMap<>();

        for (Pedido pedido : pedidosValidos()) {
            for (DetallePedido detalle : pedido.getDetalles()) {

                String nombre = detalle.getProducto().getNombre();

                int cantidadActual = conteos.getOrDefault(nombre, 0);

                conteos.put(nombre, cantidadActual + detalle.getCantidad());
            }
        }

        List<Map.Entry<String, Integer>> ordenados = new ArrayList<>(conteos.entrySet());

        ordenados.sort((a, b) -> b.getValue() - a.getValue());

        Map<String, Integer> topProductos = new LinkedHashMap<>();

        for (int i = 0; i < ordenados.size() && i < cantidadTop; i++) {
            Map.Entry<String, Integer> entrada = ordenados.get(i);
            topProductos.put(entrada.getKey(), entrada.getValue());
        }

        return topProductos;
    }

    @Override
    public Map<String, BigDecimal> ingresosPorProducto() {

        Map<String, BigDecimal> ingresos = new LinkedHashMap<>();

        for (Pedido pedido : pedidosValidos()) {
            for (DetallePedido detalle : pedido.getDetalles()) {

                String nombre = detalle.getProducto().getNombre();

                BigDecimal acumulado = ingresos.getOrDefault(nombre, BigDecimal.ZERO);

                ingresos.put(nombre, acumulado.add(detalle.getSubtotal()));
            }
        }

        return ingresos;
    }

    @Override
    public Map<EstadoPedido, Long> pedidosPorEstado() {

        Map<EstadoPedido, Long> conteos = new LinkedHashMap<>();

        for (EstadoPedido estado : EstadoPedido.values()) {
            conteos.put(estado, 0L);
        }

        for (Pedido pedido : pedidoService.listarPedidos()) {
            long actual = conteos.getOrDefault(pedido.getEstado(), 0L);
            conteos.put(pedido.getEstado(), actual + 1);
        }

        return conteos;
    }

    @Override
    public Map<Integer, Long> pedidosPorHora() {

        Map<Integer, Long> conteos = new LinkedHashMap<>();

        for (int hora = 0; hora < 24; hora++) {
            conteos.put(hora, 0L);
        }

        for (Pedido pedido : pedidoService.listarPedidos()) {
            int hora = pedido.getFecha().getHour();
            long actual = conteos.getOrDefault(hora, 0L);
            conteos.put(hora, actual + 1);
        }

        return conteos;
    }

    @Override
    public ResumenDashboard obtenerResumen() {

        LocalDate hoy = LocalDate.now();

        BigDecimal ventasHoy = BigDecimal.ZERO;
        int pendientes = 0;
        int completados = 0;

        List<Pedido> todos = pedidoService.listarPedidos();

        for (Pedido pedido : todos) {

            boolean esDeHoy = pedido.getFecha().toLocalDate().isEqual(hoy);
            boolean noCancelado = pedido.getEstado() != EstadoPedido.CANCELADO;

            if (esDeHoy && noCancelado) {
                ventasHoy = ventasHoy.add(pedido.getTotal());
            }

            if (pedido.getEstado() == EstadoPedido.PENDIENTE
                    || pedido.getEstado() == EstadoPedido.PREPARACION) {
                pendientes++;
            }

            if (pedido.getEstado() == EstadoPedido.ENTREGADO) {
                completados++;
            }
        }

        return new ResumenDashboard(ventasHoy, todos.size(), pendientes, completados);
    }

    // ==========================================================
    // UTILIDAD PRIVADA
    // ==========================================================

    private List<Pedido> pedidosValidos() {

        List<Pedido> validos = new ArrayList<>();

        for (Pedido pedido : pedidoService.listarPedidos()) {
            if (pedido.getEstado() != EstadoPedido.CANCELADO) {
                validos.add(pedido);
            }
        }

        return validos;
    }

}
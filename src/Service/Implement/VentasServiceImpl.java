package Service.Implement;

import Model.DetallePedido;
import Model.EstadoPedido;
import Model.MetodoPago;
import Model.Pago;
import Model.Pedido;
import Service.Interfaz.IPagoService;
import Service.Interfaz.IPedidoService;
import Service.Interfaz.IVentasService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Implementación de IVentasService. Igual que DashboardServiceImpl:
 * no hace SQL de agregación, calcula en memoria sobre lo que traen
 * IPedidoService (pedidos + detalles) e IPagoService (método de
 * pago por pedido). Si el volumen crece, se puede reemplazar el
 * cuerpo de cada método por una consulta SQL sin tocar la interfaz
 * ni quién la usa (Controller/View no cambian).
 * ===============================================================
 */
public class VentasServiceImpl implements IVentasService {

    private final IPedidoService pedidoService;
    private final IPagoService pagoService;

    public VentasServiceImpl(IPedidoService pedidoService, IPagoService pagoService) {
        this.pedidoService = pedidoService;
        this.pagoService = pagoService;
    }

    @Override
    public Map<String, BigDecimal> ventasPorHora(LocalDate dia) {

        Map<String, BigDecimal> ventas = new LinkedHashMap<>();

        for (int hora = 0; hora < 24; hora++) {
            ventas.put(String.format("%02d:00", hora), BigDecimal.ZERO);
        }

        for (Pedido pedido : pedidosValidosEnRango(dia, dia)) {

            String etiqueta = String.format("%02d:00", pedido.getFecha().getHour());

            BigDecimal acumulado = ventas.getOrDefault(etiqueta, BigDecimal.ZERO);

            ventas.put(etiqueta, acumulado.add(pedido.getTotal()));
        }

        return ventas;
    }

    @Override
    public Map<String, BigDecimal> ventasPorCategoria(LocalDate desde, LocalDate hasta) {

        Map<String, BigDecimal> ventas = new LinkedHashMap<>();

        for (Pedido pedido : pedidosValidosEnRango(desde, hasta)) {
            for (DetallePedido detalle : pedido.getDetalles()) {

                if (detalle.getProducto() == null || detalle.getProducto().getCategoria() == null) {
                    continue; // ver QueFalta.md: detalle sin producto/categoría cargados
                }

                String categoria = detalle.getProducto().getCategoria().getNombre();

                BigDecimal acumulado = ventas.getOrDefault(categoria, BigDecimal.ZERO);

                ventas.put(categoria, acumulado.add(detalle.getSubtotal()));
            }
        }

        return ventas;
    }

    @Override
    public Map<String, Integer> topProductos(int cantidadTop, LocalDate desde, LocalDate hasta) {

        Map<String, Integer> conteos = new LinkedHashMap<>();

        for (Pedido pedido : pedidosValidosEnRango(desde, hasta)) {
            for (DetallePedido detalle : pedido.getDetalles()) {

                if (detalle.getProducto() == null) {
                    continue;
                }

                String nombre = detalle.getProducto().getNombre();

                int actual = conteos.getOrDefault(nombre, 0);

                conteos.put(nombre, actual + detalle.getCantidad());
            }
        }

        List<Map.Entry<String, Integer>> ordenados = new ArrayList<>(conteos.entrySet());

        ordenados.sort((a, b) -> b.getValue() - a.getValue());

        Map<String, Integer> top = new LinkedHashMap<>();

        for (int i = 0; i < ordenados.size() && i < cantidadTop; i++) {
            top.put(ordenados.get(i).getKey(), ordenados.get(i).getValue());
        }

        return top;
    }

    @Override
    public ResumenVentas obtenerResumen(LocalDate desde, LocalDate hasta) {

        List<Pedido> pedidos = pedidosValidosEnRango(desde, hasta);

        BigDecimal totalVentas = BigDecimal.ZERO;
        int productosVendidos = 0;

        for (Pedido pedido : pedidos) {

            totalVentas = totalVentas.add(pedido.getTotal());

            for (DetallePedido detalle : pedido.getDetalles()) {
                productosVendidos += detalle.getCantidad();
            }
        }

        BigDecimal ticketPromedio = pedidos.isEmpty()
                ? BigDecimal.ZERO
                : totalVentas.divide(BigDecimal.valueOf(pedidos.size()), 2, RoundingMode.HALF_UP);

        return new ResumenVentas(totalVentas, pedidos.size(), ticketPromedio, productosVendidos);
    }

    @Override
    public List<VentaHistorial> historialVentas(FiltroVentas filtro) {

        List<VentaHistorial> resultado = new ArrayList<>();

        LocalDate desde = filtro.desde != null ? filtro.desde : LocalDate.MIN;
        LocalDate hasta = filtro.hasta != null ? filtro.hasta : LocalDate.MAX;

        for (Pedido pedido : pedidoService.listarPedidos()) {

            LocalDate fechaPedido = pedido.getFecha().toLocalDate();

            if (fechaPedido.isBefore(desde) || fechaPedido.isAfter(hasta)) {
                continue;
            }

            if (filtro.estado != null && pedido.getEstado() != filtro.estado) {
                continue;
            }

            Pago pago = pagoService.buscarPorPedido(pedido.getIdPedido());
            MetodoPago metodo = pago != null ? pago.getMetodoPago() : null;

            if (filtro.metodoPago != null && metodo != filtro.metodoPago) {
                continue;
            }

            String cliente = (pedido.getUsuario() != null)
                    ? pedido.getUsuario().getNombreCompleto().trim()
                    : "";

            if (filtro.busqueda != null && !filtro.busqueda.isBlank()) {

                String texto = filtro.busqueda.toLowerCase();

                boolean coincideOrden = pedido.getNumeroOrden() != null
                        && pedido.getNumeroOrden().toLowerCase().contains(texto);

                boolean coincideCliente = cliente.toLowerCase().contains(texto);

                if (!coincideOrden && !coincideCliente) {
                    continue;
                }
            }

            resultado.add(new VentaHistorial(
                    pedido.getIdPedido(),
                    pedido.getNumeroOrden(),
                    pedido.getFecha(),
                    cliente.isEmpty() ? ("Cliente #" + (pedido.getUsuario() != null ? pedido.getUsuario().getIdUsuario() : 0)) : cliente,
                    null, // cajero: no existe todavía en el modelo, ver QueFalta.md
                    pedido.getTotal(),
                    metodo,
                    pedido.getEstado(),
                    pedido.getCantidadDetalles()
            ));
        }

        // más recientes primero, igual que la maqueta
        resultado.sort((a, b) -> b.fecha.compareTo(a.fecha));

        return resultado;
    }

    // ==========================================================
    // UTILIDAD PRIVADA
    // ==========================================================

    private List<Pedido> pedidosValidosEnRango(LocalDate desde, LocalDate hasta) {

        List<Pedido> validos = new ArrayList<>();

        for (Pedido pedido : pedidoService.listarPedidos()) {

            if (pedido.getEstado() == EstadoPedido.CANCELADO) {
                continue;
            }

            LocalDate fechaPedido = pedido.getFecha().toLocalDate();

            if (fechaPedido.isBefore(desde) || fechaPedido.isAfter(hasta)) {
                continue;
            }

            validos.add(pedido);
        }

        return validos;
    }
}
package Service.Interfaz;

import Model.EstadoPedido;
import Model.MetodoPago;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Agregaciones de negocio para el panel VENTAS del Administrador.
 * Mismo espíritu que IDashboardService: no toca SQL directamente,
 * se apoya en IPedidoService (que trae cada Pedido con sus
 * DetallePedido cargados) y en IPagoService (para método de pago).
 *
 * IMPORTANTE (ver nota al final del archivo "QueFalta.md" que te
 * mandé aparte): hoy PedidoDAOImpl.listar()/buscarPorId() NO
 * rellenan detalles ni el nombre del usuario — mientras eso no se
 * arregle, ventasPorCategoria(), topProductos() y el nombre de
 * cliente en el historial van a venir vacíos o en blanco. La firma
 * de este Service ya queda lista para cuando eso se resuelva.
 * ===============================================================
 */
public interface IVentasService {

    /** Ventas (Q) agrupadas por hora del día indicado, de 00 a 23. */
    Map<String, BigDecimal> ventasPorHora(LocalDate dia);

    /** Ventas (Q) agrupadas por nombre de categoría, en el rango [desde, hasta]. */
    Map<String, BigDecimal> ventasPorCategoria(LocalDate desde, LocalDate hasta);

    /** Los N productos con más unidades vendidas en el rango, ya ordenados de mayor a menor. */
    Map<String, Integer> topProductos(int cantidadTop, LocalDate desde, LocalDate hasta);

    /** Resumen para las 4 TarjetaKPI de arriba del panel (ventas, pedidos, ticket promedio, productos vendidos). */
    ResumenVentas obtenerResumen(LocalDate desde, LocalDate hasta);

    /** Historial de ventas ya filtrado, para llenar la tabla. */
    List<VentaHistorial> historialVentas(FiltroVentas filtro);

    /**
     * DTO de solo lectura para las tarjetas KPI. No es una entidad
     * de BD, por eso vive aquí y no en Model/.
     */
    class ResumenVentas {

        public final BigDecimal totalVentas;
        public final int totalPedidos;
        public final BigDecimal ticketPromedio;
        public final int productosVendidos;

        public ResumenVentas(
                BigDecimal totalVentas,
                int totalPedidos,
                BigDecimal ticketPromedio,
                int productosVendidos) {

            this.totalVentas = totalVentas;
            this.totalPedidos = totalPedidos;
            this.ticketPromedio = ticketPromedio;
            this.productosVendidos = productosVendidos;
        }
    }

    /**
     * Una fila de la tabla "Historial de ventas". "cajero" queda
     * como String y puede venir null: la tabla `pedido` hoy no
     * tiene una columna que identifique quién atendió la venta
     * (ver QueFalta.md) — cuando se agregue, solo hay que rellenar
     * este campo en la implementación, la vista no cambia.
     */
    class VentaHistorial {

        public final int idPedido;
        public final String numeroOrden;
        public final java.time.LocalDateTime fecha;
        public final String cliente;
        public final String cajero;
        public final BigDecimal total;
        public final MetodoPago metodoPago;
        public final EstadoPedido estado;
        public final int cantidadProductos;

        public VentaHistorial(
                int idPedido,
                String numeroOrden,
                java.time.LocalDateTime fecha,
                String cliente,
                String cajero,
                BigDecimal total,
                MetodoPago metodoPago,
                EstadoPedido estado,
                int cantidadProductos) {

            this.idPedido = idPedido;
            this.numeroOrden = numeroOrden;
            this.fecha = fecha;
            this.cliente = cliente;
            this.cajero = cajero;
            this.total = total;
            this.metodoPago = metodoPago;
            this.estado = estado;
            this.cantidadProductos = cantidadProductos;
        }
    }

    /**
     * Filtros de la barra "FILTROS DE VENTAS" de la maqueta. Todos
     * son opcionales (null = sin filtrar por ese campo).
     */
    class FiltroVentas {

        public LocalDate desde;
        public LocalDate hasta;
        public MetodoPago metodoPago;
        public EstadoPedido estado;
        public String busqueda; // número de orden o nombre de cliente

        public FiltroVentas(LocalDate desde, LocalDate hasta) {
            this.desde = desde;
            this.hasta = hasta;
        }
    }
}
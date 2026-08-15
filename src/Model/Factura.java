package Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
public class Factura {

    private int idFactura;

    private Pedido pedido;

    private String numeroFactura;

    private LocalDateTime fecha;

    private Usuario cliente;

    private String direccion;

    private BigDecimal subtotal;

    private BigDecimal descuento;

    private MetodoPago metodoPago;

    private BigDecimal total;

    private BigDecimal iva;
    
    private static final BigDecimal TASA_IVA =
        new BigDecimal("0.12");
    
    private String  nit;
    
    private List<DetalleFactura> detalles;
    
    
public Factura() {

    this.fecha = LocalDateTime.now();

    this.detalles = new ArrayList<>();

    this.subtotal = BigDecimal.ZERO;

    this.descuento = BigDecimal.ZERO;

    this.iva = BigDecimal.ZERO;

    this.total = BigDecimal.ZERO;
}
public Factura(
        Pedido pedido,
        Usuario cliente) {

    this();

    this.pedido = pedido;
    this.cliente = cliente;
}

    public Factura(int idFactura, Pedido pedido, String numeroFactura, LocalDateTime fecha, Usuario cliente, String direccion, BigDecimal subtotal, BigDecimal descuento, MetodoPago metodoPago, BigDecimal total, BigDecimal iva, String  nit) {
        this.idFactura = idFactura;
        this.pedido = pedido;
        this.numeroFactura = numeroFactura;
        this.fecha = fecha;
        this.cliente = cliente;
        this.direccion = direccion;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.metodoPago = metodoPago;
        this.total = total;
        this.iva = iva;
        this.nit = nit;
        this.detalles = new ArrayList<>();
    }

    public List<DetalleFactura> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleFactura> detalles) {
        this.detalles = detalles != null
                ? detalles
                : new ArrayList<>();
    }

    public void agregarDetalle(DetalleFactura detalle) {

        if (detalle == null) {
            throw new IllegalArgumentException(
                    "El detalle no puede ser null."
            );
        }

        detalles.add(detalle);
    }
    public void eliminarDetalle(DetalleFactura detalle) {

    detalles.remove(detalle);
}

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getDireccion() {
        return direccion;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public String  getNit() {
        return nit;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public void setNit(String  nit) {
        this.nit = nit;
    }

    public String generarNumeroFactura() {

        return "FAC-" + String.format("%06d", idFactura);

    }

public String getNombreCliente() {

    if (cliente == null) {
        return "Cliente no registrado";
    }

    return cliente.getNombreCompleto();
}

    public String getFechaFormateada() {

    if (fecha == null) {
        return "";
    }

    DateTimeFormatter formato =
            DateTimeFormatter.ofPattern(
                    "dd/MM/yyyy HH:mm"
            );

    return fecha.format(formato);
}
    public BigDecimal calcularSubtotal() {

    BigDecimal resultado = BigDecimal.ZERO;

    for (DetalleFactura detalle : detalles) {

        if (detalle != null) {

            resultado = resultado.add(
                    detalle.obtenerSubtotal()
            );
        }
    }

    this.subtotal = resultado;

    return resultado;
}
public BigDecimal calcularIva() {

    BigDecimal base = subtotal.subtract(
            descuento != null
                    ? descuento
                    : BigDecimal.ZERO
    );

    iva = base.multiply(
            TASA_IVA
    );

    return iva;
}
public BigDecimal calcularTotal() {

    BigDecimal base = subtotal.subtract(
            descuento != null
                    ? descuento
                    : BigDecimal.ZERO
    );

    calcularIva();

    total = base.add(iva);

    return total;
}
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Factura)) {
            return false;
        }

        Factura other = (Factura) obj;

        return idFactura == other.idFactura;

    }

    @Override
    public int hashCode() {

        return Objects.hash(idFactura);

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Factura{");
        sb.append("idFactura=").append(idFactura);
        sb.append(", pedido=").append(pedido);
        sb.append(", numeroFactura=").append(numeroFactura);
        sb.append(", fecha=").append(fecha);
        sb.append(", cliente=").append(cliente);
        sb.append(", direccion=").append(direccion);
        sb.append(", subtotal=").append(subtotal);
        sb.append(", descuento=").append(descuento);
        sb.append(", total=").append(total);
        sb.append('}');
        return sb.toString();
    }

}

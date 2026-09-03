package Model;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;

public class Pedido {
    private int idPedido;

    private String numeroOrden;
    
    private Usuario usuario;

    private LocalDateTime fecha;

    private TipoEntrega tipoEntrega;

    private EstadoPedido estado;
    
    private LocalDateTime horaEstimada;

    private BigDecimal subtotal;
    
    private BigDecimal descuento;
    
    private BigDecimal total;

    // Costo de envío cuando tipoEntrega = DOMICILIO. Q0 para los
    // demás tipos de entrega.
    private BigDecimal costoEnvio;

    // Solo se usan cuando tipoEntrega = DOMICILIO. Para los otros
    // tipos de entrega quedan en null.
    private String direccionEntrega;

    private String referenciaEntrega;

 // Composición
    private final List<DetallePedido> detalles;

    public Pedido() {

    this.estado = EstadoPedido.PENDIENTE;
    this.fecha = LocalDateTime.now();
    this.detalles = new ArrayList<>();
    this.costoEnvio = BigDecimal.ZERO;

}


   

    public List<DetallePedido> getDetalles(){
        return Collections.unmodifiableList(detalles);
    }

    public Pedido(Usuario usuario, TipoEntrega tipoEntrega, List<DetallePedido> detalles) {
        this.usuario = usuario;
        this.tipoEntrega = tipoEntrega;
        this.detalles = detalles;
            this.estado = EstadoPedido.PENDIENTE;
    this.fecha = LocalDateTime.now();

    }

    public Pedido(int idPedido, String numeroOrden, Usuario usuario, LocalDateTime fecha, TipoEntrega tipoEntrega, EstadoPedido estado, LocalDateTime horaEstimada, BigDecimal subtotal, BigDecimal descuento, BigDecimal total, List<DetallePedido> detalles) {
        this.idPedido = idPedido;
        this.numeroOrden = numeroOrden;
        this.usuario = usuario;
        this.fecha = fecha;
        this.tipoEntrega = tipoEntrega;
        this.estado = estado;
        this.horaEstimada = horaEstimada;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = total;
        this.detalles = detalles;
        this.costoEnvio = BigDecimal.ZERO;
    }

    public BigDecimal getCostoEnvio() {
        return costoEnvio != null ? costoEnvio : BigDecimal.ZERO;
    }

    public void setCostoEnvio(BigDecimal costoEnvio) {
        this.costoEnvio = costoEnvio != null ? costoEnvio : BigDecimal.ZERO;
        recalcularTotales();
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getReferenciaEntrega() {
        return referenciaEntrega;
    }

    public void setReferenciaEntrega(String referenciaEntrega) {
        this.referenciaEntrega = referenciaEntrega;
    }







    public int getIdPedido() {
        return idPedido;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }



    public Usuario getIdUsuario() {
        return usuario;
    }



    public LocalDateTime getFecha() {
        return fecha;
    }

    public TipoEntrega getTipoEntrega() {
        return tipoEntrega;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public LocalDateTime getHoraEstimada() {
        return horaEstimada;
    }

    public Usuario getUsuario() {
        return usuario;
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




    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.usuario = idUsuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

 
    public void setTotal(BigDecimal total) {
        this.total = total;
    }



    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setTipoEntrega(TipoEntrega tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public void setHoraEstimada(LocalDateTime horaEstimada) {
        this.horaEstimada = horaEstimada;
    }

public void setDescuento(BigDecimal descuento){

    this.descuento = descuento;

    recalcularTotales();

}
public void vaciarPedido(){

    detalles.clear();

    recalcularTotales();

}
public boolean tieneProductos(){

    return !detalles.isEmpty();

}
public int getCantidadDetalles(){

    return detalles.size();

}
public void agregarDetalle(
        Producto producto,
        int cantidad
) {
    agregarDetalle(producto, cantidad, null);
}

public void agregarDetalle(
        Producto producto,
        int cantidad,
        String observaciones
) {

    DetallePedido detalle =

            new DetallePedido(
                    this,
                    producto,
                    cantidad,
                    observaciones
            );

    detalles.add(detalle);

    recalcularTotales();

}
public void eliminarDetalle(DetallePedido detalle){

    detalles.remove(detalle);

    recalcularTotales();

}

public void recalcularTotales() {
    subtotal = BigDecimal.ZERO;
    for (DetallePedido detalle : detalles) {
        if (detalle.getSubtotal() != null) {
            subtotal = subtotal.add(detalle.getSubtotal());
        }
    }
    
    BigDecimal desc = (descuento != null) ? descuento : BigDecimal.ZERO;
    BigDecimal envio = (costoEnvio != null) ? costoEnvio : BigDecimal.ZERO;
    total = subtotal.subtract(desc).add(envio);
}

public boolean estaEntregado() {

    return estado == EstadoPedido.ENTREGADO;

}
public boolean estaCancelado() {

    return estado == EstadoPedido.CANCELADO;

}

public boolean estaPendiente() {

    return estado == EstadoPedido.PENDIENTE;

}
public boolean estaListo() {

    return estado == EstadoPedido.LISTO;

}
public void cambiarEstado(EstadoPedido estado) {

    this.estado = estado;

}
public void prepararPedido() {

    this.estado = EstadoPedido.PREPARACION;

}

public void entregarPedido() {

    this.estado = EstadoPedido.ENTREGADO;

}
@Override
public boolean equals(Object obj) {

    if (this == obj) {
        return true;
    }

    if (!(obj instanceof Pedido)) {
        return false;
    }

    Pedido other = (Pedido) obj;

    return idPedido == other.idPedido;

}

@Override
public int hashCode() {

    return Objects.hash(idPedido);

}

    @Override
    public String toString() {
        return "Pedido{" + "idPedido=" + idPedido + ", numeroOrden=" + numeroOrden + ", usuario=" + usuario + ", fecha=" + fecha + ", tipoEntrega=" + tipoEntrega + ", estado=" + estado + ", horaEstimada=" + horaEstimada + ", subtotal=" + subtotal + ", descuento=" + descuento + ", total=" + total + ", detalles=" + detalles + '}';
    }

    
}
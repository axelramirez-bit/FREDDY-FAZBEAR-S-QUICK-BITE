
package Model;
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

    private double subtotal;
    
    private double descuento;
    
    private double total;
    

 // Composición
    private final List<DetallePedido> detalles;

    public Pedido() {

    this.estado = EstadoPedido.PENDIENTE;
    this.fecha = LocalDateTime.now();
    this.detalles = new ArrayList<>();

}

    //------------------------------------------------
    // Composición
    //------------------------------------------------

    public DetallePedido agregarDetalle(Producto idProducto,
                                        int cantidad,
                                        double precio,
                                        String observaciones){

        DetallePedido detalle = new DetallePedido(
                this,
                idProducto,
                cantidad);

        detalles.add(detalle);

        recalcularTotales();

        return detalle;
    }

   

    public List<DetallePedido> getDetalles(){
        return Collections.unmodifiableList(detalles);
    }

    //------------------------------------------------
public Pedido(Usuario usuario, TipoEntrega tipoEntrega) {

    this.usuario = usuario;
    this.tipoEntrega = tipoEntrega;
    this.estado = EstadoPedido.PENDIENTE;
    this.fecha = LocalDateTime.now();
    this.detalles = new ArrayList<>();
    this.subtotal = 0;
    this.descuento = 0;
    this.total = 0;

}

    public Pedido(int idPedido, String numeroOrden, Usuario usuario, LocalDateTime fecha, TipoEntrega tipoEntrega, EstadoPedido estado, LocalDateTime horaEstimada, double subtotal, double descuento, double total, List<DetallePedido> detalles) {
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

    public double getSubtotal() {
        return subtotal;
    }

    public double getDescuento() {
        return descuento;
    }

    public double getTotal() {
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

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public void setTotal(double total) {
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

public void setDescuento(double descuento){

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

    DetallePedido detalle =

            new DetallePedido(
                    this,
                    producto,
                    cantidad
            );

    detalles.add(detalle);

    recalcularTotales();

}
public void eliminarDetalle(DetallePedido detalle){

    detalles.remove(detalle);

    recalcularTotales();

}

public void recalcularTotales(){

    subtotal = 0;

    for(DetallePedido detalle : detalles){

        subtotal += detalle.getSubtotal();

    }

    total = subtotal - descuento;

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

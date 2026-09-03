package Model;

import java.math.BigDecimal;
import java.util.Objects;

public class DetallePedido {

    private int idDetalle;

    private  Pedido pedido;

    private Producto  producto;

    private int cantidad;

    private BigDecimal precio;

    private BigDecimal subtotal;

    // Instrucciones del cliente para esta línea del pedido (ej. "sin
    // cebolla", "extra queso"), capturadas desde el carrito
    // (CarritoDetalle.observaciones — ver FilaProductoCarrito.editarObservaciones())
    // y persistidas junto con el resto de la línea para que el
    // Trabajador las vea al revisar el pedido antes de entregarlo.
    private String observaciones;


    public DetallePedido(Pedido pedido, Producto producto, int cantidad) {
    this.pedido = pedido;
    this.producto = producto;
    this.cantidad = cantidad;
    this.precio = producto.getPrecio();
    calcularSubtotal();
}

    public DetallePedido(Pedido pedido, Producto producto, int cantidad, String observaciones) {
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = producto.getPrecio();
        this.observaciones = observaciones;
        calcularSubtotal();
    }



    public DetallePedido(int idDetalle, Pedido pedido, Producto producto, int cantidad, BigDecimal precio, BigDecimal subtotal) {
        this.idDetalle = idDetalle;
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        calcularSubtotal();
    }

    public DetallePedido(int idDetalle, Pedido pedido, Producto producto, int cantidad, BigDecimal precio, BigDecimal subtotal, String observaciones) {
        this.idDetalle = idDetalle;
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.observaciones = observaciones;
        calcularSubtotal();
    }
    private void calcularSubtotal(){

        subtotal = precio.multiply(BigDecimal.valueOf(cantidad));

    }

    public void setCantidad(int cantidad){

        this.cantidad = cantidad;

        calcularSubtotal();

        if (pedido != null) {
    pedido.recalcularTotales();
}

    }

    public void setPrecio(BigDecimal precio){

        this.precio = precio;

        calcularSubtotal();

        if (pedido != null) {
    pedido.recalcularTotales();
}

    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }



    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public int getCantidad() {
        return cantidad;
    }

    public Producto getProducto() {
        return producto;
    }
  
    public BigDecimal getPrecio() {
        return precio;
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean tieneObservaciones() {
        return observaciones != null && !observaciones.isBlank();
    }

 @Override
public boolean equals(Object obj) {

    if (this == obj) {
        return true;
    }

    if (!(obj instanceof DetallePedido)) {
        return false;
    }

    DetallePedido other = (DetallePedido) obj;

    return idDetalle == other.idDetalle;

}

@Override
public int hashCode() {

    return Objects.hash(idDetalle);

}

    @Override
    public String toString() {
        return "DetallePedido{" + "idDetalle=" + idDetalle + ", pedido=" + pedido + ", producto=" + producto + ", cantidad=" + cantidad + ", precio=" + precio + ", subtotal=" + subtotal + '}';
    }



    
}
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
    

    public DetallePedido(Pedido pedido, Producto producto, int cantidad) {
    this.pedido = pedido;
    this.producto = producto;
    this.cantidad = cantidad;
    this.precio = producto.getPrecio();
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
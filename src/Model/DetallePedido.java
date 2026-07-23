package Model;

import java.util.Objects;

public class DetallePedido {

    private int idDetalle;

    private  Pedido pedido;

    private Producto  producto;

    private int cantidad;

    private double precio;

    private double subtotal;


    public DetallePedido(Pedido pedido, Producto producto, int cantidad) {
    this.pedido = pedido;
    this.producto = producto;
    this.cantidad = cantidad;
    this.precio = producto.getPrecio();
    calcularSubtotal();
}

    public DetallePedido(int idDetalle, Pedido pedido, Producto producto, int cantidad, double precio, double subtotal) {
        this.idDetalle = idDetalle;
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.subtotal = subtotal;
        calcularSubtotal();
    }





    private void calcularSubtotal(){

        subtotal = cantidad * precio;

    }

    public void setCantidad(int cantidad){

        this.cantidad = cantidad;

        calcularSubtotal();

        pedido.recalcularTotales();

    }

    public void setPrecio(double precio){

        this.precio = precio;

        calcularSubtotal();

        pedido.recalcularTotales();

    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public void setIdProducto(Producto producto) {
        this.producto = producto;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }


    
    //------------------------------------------------

    public Pedido getPedido() {
        return pedido;
    }

    public int getCantidad() {
        return cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

  
    public double getPrecio() {
        return precio;
    }

 


    public int getIdDetalle() {
        return idDetalle;
    }
    public double getSubtotal(){

    return cantidad * precio;

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
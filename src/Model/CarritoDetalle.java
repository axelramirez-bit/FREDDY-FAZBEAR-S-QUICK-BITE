
package Model;

import java.util.Objects;


public class CarritoDetalle {

    private int idCarritoDetalle;

    private Carrito carrito;

    private Producto producto;

    private int cantidad;

    private String observaciones;

    public CarritoDetalle() {
    }

    public CarritoDetalle(
            Carrito carrito,
            Producto producto,
            int cantidad
    ) {

        this.carrito = carrito;
        this.producto = producto;
        this.cantidad = cantidad;

    }

    public CarritoDetalle(int idCarritoDetalle, Carrito carrito, Producto producto, int cantidad, String observaciones) {
        this.idCarritoDetalle = idCarritoDetalle;
        this.carrito = carrito;
        this.producto = producto;
        this.cantidad = cantidad;
        this.observaciones = observaciones;
    }

    public int getIdCarritoDetalle() {
        return idCarritoDetalle;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setIdCarritoDetalle(int idCarritoDetalle) {
        this.idCarritoDetalle = idCarritoDetalle;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

public double getSubtotal(){

    return producto.getPrecio()*cantidad;

}
public void aumentarCantidad(int cantidad){

    this.cantidad += cantidad;

}
public void disminuirCantidad(int cantidad){

    this.cantidad -= cantidad;

}
@Override
public boolean equals(Object obj) {

    if (this == obj) {
        return true;
    }

    if (!(obj instanceof CarritoDetalle)) {
        return false;
    }

    CarritoDetalle other = (CarritoDetalle) obj;

    return idCarritoDetalle == other.idCarritoDetalle;

}

@Override
public int hashCode() {

    return Objects.hash(idCarritoDetalle);

}

    @Override
    public String toString() {
        return "CarritoDetalle{" + "idCarritoDetalle=" + idCarritoDetalle + ", carrito=" + carrito + ", producto=" + producto + ", cantidad=" + cantidad + ", observaciones=" + observaciones + '}';
    }


}

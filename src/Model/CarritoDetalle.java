
// Paquete Model
package Model;

// Importa BigDecimal
import java.math.BigDecimal;
// Importa Objects
import java.util.Objects;


// Modelo de una línea (detalle) del carrito
public class CarritoDetalle {

    // Atributo idCarritoDetalle
    private int idCarritoDetalle;

    // Atributo carrito
    private Carrito carrito;

    // Atributo producto
    private Producto producto;

    // Atributo cantidad
    private int cantidad;

    // Atributo observaciones
    private String observaciones;

    // Constructor vacío
    public CarritoDetalle() {
    }

    // Constructor con carrito, producto y cantidad
    public CarritoDetalle(
            Carrito carrito,
            Producto producto,
            int cantidad
    ) {

        // Asigna carrito
        this.carrito = carrito;
        // Asigna producto
        this.producto = producto;
        // Asigna cantidad
        this.cantidad = cantidad;

    }

    // Constructor con todos los datos
    public CarritoDetalle(int idCarritoDetalle, Carrito carrito, Producto producto, int cantidad, String observaciones) {
        // Asigna idCarritoDetalle
        this.idCarritoDetalle = idCarritoDetalle;
        // Asigna carrito
        this.carrito = carrito;
        // Asigna producto
        this.producto = producto;
        // Asigna cantidad
        this.cantidad = cantidad;
        // Asigna observaciones
        this.observaciones = observaciones;
    }

    // Devuelve idCarritoDetalle
    public int getIdCarritoDetalle() {
        // Retorna el valor
        return idCarritoDetalle;
    }

    // Devuelve carrito
    public Carrito getCarrito() {
        // Retorna el valor
        return carrito;
    }

    // Devuelve producto
    public Producto getProducto() {
        // Retorna el valor
        return producto;
    }

    // Devuelve cantidad
    public int getCantidad() {
        // Retorna el valor
        return cantidad;
    }

    // Devuelve observaciones
    public String getObservaciones() {
        // Retorna el valor
        return observaciones;
    }

    // Asigna idCarritoDetalle
    public void setIdCarritoDetalle(int idCarritoDetalle) {
        // Asigna idCarritoDetalle
        this.idCarritoDetalle = idCarritoDetalle;
    }

    // Asigna carrito
    public void setCarrito(Carrito carrito) {
        // Asigna carrito
        this.carrito = carrito;
    }

    // Asigna producto
    public void setProducto(Producto producto) {
        // Asigna producto
        this.producto = producto;
    }

    // Asigna cantidad
    public void setCantidad(int cantidad) {
        // Asigna cantidad
        this.cantidad = cantidad;
    }

    // Asigna observaciones
    public void setObservaciones(String observaciones) {
        // Asigna observaciones
        this.observaciones = observaciones;
    }

// Calcula el subtotal (precio por cantidad)
public BigDecimal getSubtotal() {

    // Si no hay producto o precio
    if (producto == null || producto.getPrecio() == null) {
        // devuelve cero
        return BigDecimal.ZERO;
    }

    // Precio del producto
    return producto.getPrecio()
            // multiplicado por la cantidad
            .multiply(BigDecimal.valueOf(cantidad));
}
// Aumenta la cantidad
public void aumentarCantidad(int cantidad){

    // Si la cantidad recibida es positiva
    if(cantidad>0){
    // la suma a la actual
    this.cantidad += cantidad;
}
}
// Disminuye la cantidad
public void disminuirCantidad(int cantidad){

// Resta sin bajar nunca de 1
this.cantidad = Math.max(1, this.cantidad-cantidad);
}
// Sobrescribe equals
@Override
public boolean equals(Object obj) {

    // Si es el mismo objeto
    if (this == obj) {
        // son iguales
        return true;
    }

    // Si el otro no es un CarritoDetalle
    if (!(obj instanceof CarritoDetalle)) {
        // no son iguales
        return false;
    }

    // Convierte el objeto a CarritoDetalle
    CarritoDetalle other = (CarritoDetalle) obj;

    // Son iguales si tienen el mismo id
    return idCarritoDetalle == other.idCarritoDetalle;

}

// Sobrescribe hashCode
@Override
public int hashCode() {

    // Calcula el hash con el id
    return Objects.hash(idCarritoDetalle);

}

    // Sobrescribe toString
    @Override
    public String toString() {
        // Devuelve el texto con los datos del detalle
        return "CarritoDetalle{" + "idCarritoDetalle=" + idCarritoDetalle + ", carrito=" + carrito + ", producto=" + producto + ", cantidad=" + cantidad + ", observaciones=" + observaciones + '}';
    }


}

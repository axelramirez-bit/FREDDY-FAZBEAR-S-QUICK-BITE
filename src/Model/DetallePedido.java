// Paquete Model
package Model;

// Importa BigDecimal
import java.math.BigDecimal;
// Importa Objects
import java.util.Objects;

// Modelo de una línea (detalle) de un pedido
public class DetallePedido {

    // Atributo idDetalle
    private int idDetalle;

    // Atributo pedido
    private  Pedido pedido;

    // Atributo producto
    private Producto  producto;

    // Atributo cantidad
    private int cantidad;

    // Atributo precio
    private BigDecimal precio;

    // Atributo subtotal
    private BigDecimal subtotal;

    // Instrucciones del cliente para esta línea del pedido (ej. "sin
    // cebolla", "extra queso"), capturadas desde el carrito
    // (CarritoDetalle.observaciones — ver FilaProductoCarrito.editarObservaciones())
    // y persistidas junto con el resto de la línea para que el
    // Trabajador las vea al revisar el pedido antes de entregarlo.
    // Atributo observaciones
    private String observaciones;


    // Constructor con pedido, producto y cantidad
    public DetallePedido(Pedido pedido, Producto producto, int cantidad) {
    // Asigna pedido
    this.pedido = pedido;
    // Asigna producto
    this.producto = producto;
    // Asigna cantidad
    this.cantidad = cantidad;
    // Toma el precio actual del producto
    this.precio = producto.getPrecio();
    // Calcula el subtotal
    calcularSubtotal();
}

    // Constructor con pedido, producto, cantidad y observaciones
    public DetallePedido(Pedido pedido, Producto producto, int cantidad, String observaciones) {
        // Asigna pedido
        this.pedido = pedido;
        // Asigna producto
        this.producto = producto;
        // Asigna cantidad
        this.cantidad = cantidad;
        // Toma el precio actual del producto
        this.precio = producto.getPrecio();
        // Asigna observaciones
        this.observaciones = observaciones;
        // Calcula el subtotal
        calcularSubtotal();
    }



    // Constructor con todos los datos (sin observaciones)
    public DetallePedido(int idDetalle, Pedido pedido, Producto producto, int cantidad, BigDecimal precio, BigDecimal subtotal) {
        // Asigna idDetalle
        this.idDetalle = idDetalle;
        // Asigna pedido
        this.pedido = pedido;
        // Asigna producto
        this.producto = producto;
        // Asigna cantidad
        this.cantidad = cantidad;
        // Asigna precio
        this.precio = precio;
        // Calcula el subtotal (ignora el recibido)
        calcularSubtotal();
    }

    // Constructor con todos los datos y observaciones
    public DetallePedido(int idDetalle, Pedido pedido, Producto producto, int cantidad, BigDecimal precio, BigDecimal subtotal, String observaciones) {
        // Asigna idDetalle
        this.idDetalle = idDetalle;
        // Asigna pedido
        this.pedido = pedido;
        // Asigna producto
        this.producto = producto;
        // Asigna cantidad
        this.cantidad = cantidad;
        // Asigna precio
        this.precio = precio;
        // Asigna observaciones
        this.observaciones = observaciones;
        // Calcula el subtotal
        calcularSubtotal();
    }
    // Calcula el subtotal (precio por cantidad)
    private void calcularSubtotal(){

        // Multiplica el precio por la cantidad
        subtotal = precio.multiply(BigDecimal.valueOf(cantidad));

    }

    // Asigna la cantidad y recalcula
    public void setCantidad(int cantidad){

        // Asigna cantidad
        this.cantidad = cantidad;

        // Recalcula el subtotal
        calcularSubtotal();

        // Si hay pedido asociado
        if (pedido != null) {
    // recalcula los totales del pedido
    pedido.recalcularTotales();
}

    }

    // Asigna el precio y recalcula
    public void setPrecio(BigDecimal precio){

        // Asigna precio
        this.precio = precio;

        // Recalcula el subtotal
        calcularSubtotal();

        // Si hay pedido asociado
        if (pedido != null) {
    // recalcula los totales del pedido
    pedido.recalcularTotales();
}

    }

    // Asigna idDetalle
    public void setIdDetalle(int idDetalle) {
        // Asigna idDetalle
        this.idDetalle = idDetalle;
    }



    // Asigna subtotal
    public void setSubtotal(BigDecimal subtotal) {
        // Asigna subtotal
        this.subtotal = subtotal;
    }

    // Asigna pedido
    public void setPedido(Pedido pedido) {
        // Asigna pedido
        this.pedido = pedido;
    }

    // Asigna producto
    public void setProducto(Producto producto) {
        // Asigna producto
        this.producto = producto;
    }

    // Devuelve pedido
    public Pedido getPedido() {
        // Retorna el valor
        return pedido;
    }

    // Devuelve cantidad
    public int getCantidad() {
        // Retorna el valor
        return cantidad;
    }

    // Devuelve producto
    public Producto getProducto() {
        // Retorna el valor
        return producto;
    }
  
    // Devuelve precio
    public BigDecimal getPrecio() {
        // Retorna el valor
        return precio;
    }

    // Devuelve idDetalle
    public int getIdDetalle() {
        // Retorna el valor
        return idDetalle;
    }

    // Devuelve subtotal
    public BigDecimal getSubtotal() {
        // Retorna el valor
        return subtotal;
    }

    // Devuelve observaciones
    public String getObservaciones() {
        // Retorna el valor
        return observaciones;
    }

    // Asigna observaciones
    public void setObservaciones(String observaciones) {
        // Asigna observaciones
        this.observaciones = observaciones;
    }

    // Indica si la línea tiene observaciones
    public boolean tieneObservaciones() {
        // Verdadero si no es nulo ni está en blanco
        return observaciones != null && !observaciones.isBlank();
    }

 // Sobrescribe equals
 @Override
public boolean equals(Object obj) {

    // Si es el mismo objeto
    if (this == obj) {
        // son iguales
        return true;
    }

    // Si el otro no es un DetallePedido
    if (!(obj instanceof DetallePedido)) {
        // no son iguales
        return false;
    }

    // Convierte el objeto a DetallePedido
    DetallePedido other = (DetallePedido) obj;

    // Son iguales si tienen el mismo id
    return idDetalle == other.idDetalle;

}

// Sobrescribe hashCode
@Override
public int hashCode() {

    // Calcula el hash con el id
    return Objects.hash(idDetalle);

}

    // Sobrescribe toString
    @Override
    public String toString() {
        // Devuelve el texto con los datos del detalle
        return "DetallePedido{" + "idDetalle=" + idDetalle + ", pedido=" + pedido + ", producto=" + producto + ", cantidad=" + cantidad + ", precio=" + precio + ", subtotal=" + subtotal + '}';
    }



    
}
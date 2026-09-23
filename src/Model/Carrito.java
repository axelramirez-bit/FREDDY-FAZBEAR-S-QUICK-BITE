
package Model;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class Carrito {
    private int idCarrito;
    
    private Usuario usuario;

    private LocalDateTime fechaCreacion;
    
    private EstadoCarrito estado;
    
       // Composición
    private final List<CarritoDetalle> detalles;

public Carrito() {

    this.estado = EstadoCarrito.ACTIVO;
    this.fechaCreacion = LocalDateTime.now();
    this.detalles = new ArrayList<>();

}

    public Carrito(int idCarrito, Usuario usuario, LocalDateTime fechaCreacion, EstadoCarrito estado, List<CarritoDetalle> detalles) {
        this.idCarrito = idCarrito;
        this.usuario = usuario;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.detalles = detalles;
    }
public Carrito(Usuario usuario) {

    this.usuario = usuario;
    this.estado = EstadoCarrito.ACTIVO;
    this.fechaCreacion = LocalDateTime.now();
    this.detalles = new ArrayList<>();

}




    public int getIdCarrito() {
        return idCarrito;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public List<CarritoDetalle> getDetalles() {
        return detalles;
    }


    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public EstadoCarrito getEstado() {
        return estado;
    }

    public void setIdCarrito(int idCarrito) {
        this.idCarrito = idCarrito;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.usuario = idUsuario;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setEstado(EstadoCarrito estado) {
        this.estado = estado;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    

public void eliminarProducto(Producto producto) {

    if (producto == null) {
        return;
    }

    detalles.removeIf(detalle ->
            detalle.getProducto().equals(producto));

}
public void vaciarCarrito() {

    detalles.clear();

}
public BigDecimal calcularTotal() {

    BigDecimal total = BigDecimal.ZERO;

    for (CarritoDetalle detalle : detalles) {

        total = total.add(detalle.getSubtotal());

    }

    return total;

}
public int getCantidadProductos() {

    int cantidad = 0;

    for (CarritoDetalle detalle : detalles) {

        cantidad += detalle.getCantidad();

    }

    return cantidad;

}
public boolean estaVacio() {

    return detalles.isEmpty();

}
public void agregarProducto(Producto producto, int cantidad) {

    if (producto == null || cantidad <= 0) {
        throw new IllegalArgumentException("Producto o cantidad inválida.");
    }

    for (CarritoDetalle detalle : detalles) {

        if (detalle.getProducto().equals(producto)) {

            detalle.setCantidad(
                    detalle.getCantidad() + cantidad
            );

            return;
        }
    }

    CarritoDetalle detalle =
            new CarritoDetalle(this, producto, cantidad);

    detalles.add(detalle);

}
public boolean contieneProducto(Producto producto) {

    return buscarProducto(producto) != null;

}
public CarritoDetalle buscarProducto(Producto producto) {

    for (CarritoDetalle detalle : detalles) {

        if (detalle.getProducto().equals(producto)) {

            return detalle;

        }

    }

    return null;

}
public int getCantidadItems() {

    return detalles.size();

}
    // ANTES (bug): hashCode() devolvía siempre 7, sin importar el id.
    // equals() sí compara por id, así que dos carritos distintos eran
    // "iguales" en hash pero no en equals: en un HashMap/HashSet todos
    // caían en el mismo bucket (funciona, pero sin ninguna ventaja de
    // usar una tabla hash).
    // CORRECCIÓN: el hash se calcula a partir del id, igual que equals().
    @Override
    public int hashCode() {
        return Objects.hash(idCarrito);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Carrito other = (Carrito) obj;
        return this.idCarrito == other.idCarrito;
    }

    @Override
    public String toString() {
        return "Carrito{" + "idCarrito=" + idCarrito + ", idUsuario=" + usuario + ", fechaCreacion=" + fechaCreacion + ", estado=" + estado + '}';
    }
    
}

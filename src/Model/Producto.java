package Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Producto {

    private int idProducto;

    private Categoria categoria;

    private String nombre;

    private String descripcion;

    private BigDecimal precio;

    private BigDecimal precioAnterior;

    private int stock;

    private boolean disponible;

    private int calorias;

    private int tiempoPreparacion;

    private boolean destacado;

    private String imagenPrincipal;

    private String imagenBanner;

    private boolean estado;

    private LocalDateTime fechaActualizacion;

    private Promocion promocion;

    public Producto() {
    }

    public Producto(int idProducto, Categoria categoria, String nombre, String descripcion, BigDecimal precio, BigDecimal precioAnterior, int stock, boolean disponible, int calorias, int tiempoPreparacion, boolean destacado, String imagenPrincipal, String imagenBanner, boolean estado, LocalDateTime fechaActualizacion, Promocion promocion) {
        this.idProducto = idProducto;
        this.categoria = categoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.precioAnterior = precioAnterior;
        this.stock = stock;
        this.disponible = disponible;
        this.calorias = calorias;
        this.tiempoPreparacion = tiempoPreparacion;
        this.destacado = destacado;
        this.imagenPrincipal = imagenPrincipal;
        this.imagenBanner = imagenBanner;
        this.estado = estado;
        this.fechaActualizacion = fechaActualizacion;
        this.promocion = promocion;
    }

 
    public Categoria getCategoria() {
        return categoria;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public Promocion getPromocion() {
        return promocion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }



    public BigDecimal getPrecioAnterior() {
        return precioAnterior;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }



    public int getStock() {
        return stock;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public int getCalorias() {
        return calorias;
    }

    public int getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    public boolean isDestacado() {
        return destacado;
    }

    public String getImagenPrincipal() {
        return imagenPrincipal;
    }

    public String getImagenBanner() {
        return imagenBanner;
    }

    public boolean isEstado() {
        return estado;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setPromocion(Promocion promocion) {
        this.promocion = promocion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public void setPrecioAnterior(BigDecimal precioAnterior) {
        this.precioAnterior = precioAnterior;
    }



    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public void setCalorias(int calorias) {
        this.calorias = calorias;
    }

    public void setTiempoPreparacion(int tiempoPreparacion) {
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public void setDestacado(boolean destacado) {
        this.destacado = destacado;
    }

    public void setImagenPrincipal(String imagenPrincipal) {
        this.imagenPrincipal = imagenPrincipal;
    }

    public void setImagenBanner(String imagenBanner) {
        this.imagenBanner = imagenBanner;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public boolean tienePromocion() {

        return promocion != null
                && promocion.estaVigente();

    }

public BigDecimal getPrecioFinal() {
    if (promocion == null) return precio;
    BigDecimal descuento = precio.multiply(promocion.getDescuento())
                                  .divide(BigDecimal.valueOf(100));
    return precio.subtract(descuento);
}

    public boolean hayStock() {

        return stock > 0;

    }

    public boolean hayStock(int cantidad) {

        return stock >= cantidad;

    }

    public void disminuirStock(int cantidad) {

        stock -= cantidad;

    }

    public void aumentarStock(int cantidad) {

        stock += cantidad;

    }

    public boolean estaDisponible() {

        return disponible && estado;

    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Producto)) {
            return false;
        }

        Producto other = (Producto) obj;

        return idProducto == other.idProducto;

    }

    @Override
    public int hashCode() {

        return Objects.hash(idProducto);

    }

    @Override
    public String toString() {
        return "Producto{" + "idProducto=" + idProducto + ", categoria=" + categoria + ", nombre=" + nombre + ", descripcion=" + descripcion + ", precio=" + precio + ", precioAnterior=" + precioAnterior + ", stock=" + stock + ", disponible=" + disponible + ", calorias=" + calorias + ", tiempoPreparacion=" + tiempoPreparacion + ", destacado=" + destacado + ", imagenPrincipal=" + imagenPrincipal + ", imagenBanner=" + imagenBanner + ", estado=" + estado + ", fechaActualizacion=" + fechaActualizacion + ", promocion=" + promocion + '}';
    }

}

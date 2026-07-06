
package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;


public class Promocion {
    private int idPromocion;
    
    private String  nombre;
    
    private String descripcion;

    private double descuento;

    private LocalDate  fechaInicio;
    
    private LocalDate  fechaFin;
    
    private boolean estado;

    public Promocion() {
    }

    public Promocion(int idPromocion, String nombre, String descripcion, double descuento, LocalDate fechaInicio, LocalDate fechaFin, boolean estado) {
        this.idPromocion = idPromocion;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.descuento = descuento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }





    public int getIdPromocion() {
        return idPromocion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getDescuento() {
        return descuento;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }



    public boolean isEstado() {
        return estado;
    }

    public void setIdPromocion(int idPromocion) {
        this.idPromocion = idPromocion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }



    public void setEstado(boolean estado) {
        this.estado = estado;
    }


public boolean estaVigente() {

    LocalDate hoy = LocalDate.now();

    return estado
            && !hoy.isBefore(fechaInicio)
            && !hoy.isAfter(fechaFin);

}
    
@Override
public boolean equals(Object obj) {

    if (this == obj) {
        return true;
    }

    if (!(obj instanceof Promocion)) {
        return false;
    }

    Promocion other = (Promocion) obj;

    return idPromocion == other.idPromocion;

}

@Override
public int hashCode() {

    return Objects.hash(idPromocion);

}
    @Override
    public String toString() {
        return "Promocion{" + "idPromocion=" + idPromocion + ", nombre=" + nombre + ", descripcion=" + descripcion + ", descuento=" + descuento + ", fechalnicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", estado=" + estado + '}';
    }
    
}

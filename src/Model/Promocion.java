
// Paquete Model
package Model;

// Importa BigDecimal
import java.math.BigDecimal;
// Importa LocalDate
import java.time.LocalDate;
// Importa Objects
import java.util.Objects;


// Modelo de una promoción
public class Promocion {
    // Atributo idPromocion
    private int idPromocion;
    
    // Atributo nombre
    private String  nombre;
    
    // Atributo descripcion
    private String descripcion;

    // Atributo descuento
    private BigDecimal descuento;

    // Atributo fechaInicio
    private LocalDate  fechaInicio;
    
    // Atributo fechaFin
    private LocalDate  fechaFin;
    
    // Atributo estado
    private boolean estado;

    // Constructor vacío
    public Promocion() {
    }

    // Constructor con todos los datos
    public Promocion(int idPromocion, String nombre, String descripcion, BigDecimal descuento, LocalDate fechaInicio, LocalDate fechaFin, boolean estado) {
        // Asigna idPromocion
        this.idPromocion = idPromocion;
        // Asigna nombre
        this.nombre = nombre;
        // Asigna descripcion
        this.descripcion = descripcion;
        // Asigna descuento
        this.descuento = descuento;
        // Asigna fechaInicio
        this.fechaInicio = fechaInicio;
        // Asigna fechaFin
        this.fechaFin = fechaFin;
        // Asigna estado
        this.estado = estado;
    }







    // Devuelve idPromocion
    public int getIdPromocion() {
        // Retorna el valor
        return idPromocion;
    }

    // Devuelve nombre
    public String getNombre() {
        // Retorna el valor
        return nombre;
    }

    // Devuelve descripcion
    public String getDescripcion() {
        // Retorna el valor
        return descripcion;
    }

    // Devuelve descuento
    public BigDecimal getDescuento() {
        // Retorna el valor
        return descuento;
    }


    // Devuelve fechaInicio
    public LocalDate getFechaInicio() {
        // Retorna el valor
        return fechaInicio;
    }

    // Devuelve fechaFin
    public LocalDate getFechaFin() {
        // Retorna el valor
        return fechaFin;
    }



    // Devuelve estado
    public boolean isEstado() {
        // Retorna el valor
        return estado;
    }

    // Asigna idPromocion
    public void setIdPromocion(int idPromocion) {
        // Asigna idPromocion
        this.idPromocion = idPromocion;
    }

    // Asigna nombre
    public void setNombre(String nombre) {
        // Asigna nombre
        this.nombre = nombre;
    }

    // Asigna descripcion
    public void setDescripcion(String descripcion) {
        // Asigna descripcion
        this.descripcion = descripcion;
    }

    // Asigna descuento
    public void setDescuento(BigDecimal descuento) {
        // Asigna descuento
        this.descuento = descuento;
    }



    // Asigna fechaInicio
    public void setFechaInicio(LocalDate fechaInicio) {
        // Asigna fechaInicio
        this.fechaInicio = fechaInicio;
    }

    // Asigna fechaFin
    public void setFechaFin(LocalDate fechaFin) {
        // Asigna fechaFin
        this.fechaFin = fechaFin;
    }



    // Asigna estado
    public void setEstado(boolean estado) {
        // Asigna estado
        this.estado = estado;
    }


// Indica si la promoción está vigente hoy
public boolean estaVigente() {

    // BUG QUE ESTO CORRIGE: si fechaInicio/fechaFin llegan en null
    // (por ejemplo porque el mapeo que arma este objeto no las trae),
    // hoy.isBefore(null)/isAfter(null) lanzan NullPointerException y
    // cualquier pantalla que dependa de tienePromocion() se cae. Sin
    // fechas no hay forma de saber si está vigente, así que se trata
    // como "no vigente" en vez de reventar.
    // Si falta alguna fecha
    if (fechaInicio == null || fechaFin == null) {
        // no está vigente
        return false;
    }

    // Fecha de hoy
    LocalDate hoy = LocalDate.now();

    // Vigente si está activa
    return estado
            // y hoy no es anterior al inicio
            && !hoy.isBefore(fechaInicio)
            // y hoy no es posterior al fin
            && !hoy.isAfter(fechaFin);

}
    
// Sobrescribe equals
@Override
public boolean equals(Object obj) {

    // Si es el mismo objeto
    if (this == obj) {
        // son iguales
        return true;
    }

    // Si el otro no es una Promocion
    if (!(obj instanceof Promocion)) {
        // no son iguales
        return false;
    }

    // Convierte el objeto a Promocion
    Promocion other = (Promocion) obj;

    // Son iguales si tienen el mismo id
    return idPromocion == other.idPromocion;

}

// Sobrescribe hashCode
@Override
public int hashCode() {

    // Calcula el hash con el id
    return Objects.hash(idPromocion);

}
    // Sobrescribe toString
    @Override
    public String toString() {
        // Devuelve el texto con los datos de la promoción
        return "Promocion{" + "idPromocion=" + idPromocion + ", nombre=" + nombre + ", descripcion=" + descripcion + ", descuento=" + descuento + ", fechalnicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", estado=" + estado + '}';
    }
    
}

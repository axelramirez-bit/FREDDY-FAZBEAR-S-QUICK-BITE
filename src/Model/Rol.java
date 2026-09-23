
// Paquete Model
package Model;

// Importa Objects para calcular el hashCode
import java.util.Objects;


// Clase que representa un rol (tabla rol)
public class Rol {

    // Id del rol
    private int idRol;
    // Nombre del rol
    private String nombre;
    // Descripción del rol
    private String descripcion;

    // Constructor vacío
    public Rol() {
    }

    // Constructor con todos los datos
    public Rol(int idRol, String nombre, String descripcion) {
        // Asigna el id
        this.idRol = idRol;
        // Asigna el nombre
        this.nombre = nombre;
        // Asigna la descripción
        this.descripcion = descripcion;
    }

    // Devuelve el id del rol
    public int getIdRol() {
        return idRol;
    }

    // Devuelve el nombre
    public String getNombre() {
        return nombre;
    }

    // Devuelve la descripción
    public String getDescripcion() {
        return descripcion;
    }

    // Asigna el id
    public void setIdRol(int idRol) {
        // Guarda el id
        this.idRol = idRol;
    }

    // Asigna el nombre
    public void setNombre(String nombre) {
        // Guarda el nombre
        this.nombre = nombre;
    }

    // Asigna la descripción
    public void setDescripcion(String descripcion) {
        // Guarda la descripción
        this.descripcion = descripcion;
    }
    // Compara roles por su id
    @Override
public boolean equals(Object obj) {

    // Si es el mismo objeto
    if (this == obj) {
        // son iguales
        return true;
    }

    // Si el otro objeto no es un Rol
    if (!(obj instanceof Rol)) {
        // no son iguales
        return false;
    }

    // Convierte el objeto a Rol
    Rol other = (Rol) obj;

    // Son iguales si tienen el mismo id
    return idRol == other.idRol;

}

// Calcula el hash a partir del id
@Override
public int hashCode() {

    // Genera el hash con el id
    return Objects.hash(idRol);

}

    // Devuelve el texto con los datos del rol
    @Override
    public String toString() {
        // Arma el texto con id, nombre y descripción
        return "Rol{" + "idRol=" + idRol + ", nombre=" + nombre + ", descripcion=" + descripcion + '}';
    }

}
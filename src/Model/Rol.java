
package Model;

import java.util.Objects;


public class Rol {

    private int idRol;
    private String nombre;
    private String descripcion;

    public Rol() {
    }

    public Rol(int idRol, String nombre, String descripcion) {
        this.idRol = idRol;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getIdRol() {
        return idRol;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    @Override
public boolean equals(Object obj) {

    if (this == obj) {
        return true;
    }

    if (!(obj instanceof Rol)) {
        return false;
    }

    Rol other = (Rol) obj;

    return idRol == other.idRol;

}

@Override
public int hashCode() {

    return Objects.hash(idRol);

}

    @Override
    public String toString() {
        return "Rol{" + "idRol=" + idRol + ", nombre=" + nombre + ", descripcion=" + descripcion + '}';
    }

}
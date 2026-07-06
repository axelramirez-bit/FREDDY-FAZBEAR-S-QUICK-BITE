
package Model;

import java.util.Objects;



public class Categoria {
    private int idCategoria;
    
    private String  nombre;
    
    private String descripcion;

    private String icono;

    private String imagen;
    
    private String color;
    
    private int  orden;
    
    private boolean estado;

    public Categoria() {
    }

    public Categoria(int idCategoria, String nombre, String descripcion, String icono, String imagen, String color, int orden, boolean estado) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.icono = icono;
        this.imagen = imagen;
        this.color = color;
        this.orden = orden;
        this.estado = estado;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getIcono() {
        return icono;
    }

    public String getImagen() {
        return imagen;
    }

    public String getColor() {
        return color;
    }

    public int getOrden() {
        return orden;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    public boolean estaActiva(){

    return estado;

}
@Override
public boolean equals(Object obj) {

    if (this == obj) {
        return true;
    }

    if (!(obj instanceof Categoria)) {
        return false;
    }

    Categoria other = (Categoria) obj;

    return idCategoria == other.idCategoria;

}

@Override
public int hashCode() {

    return Objects.hash(idCategoria);

}

    @Override
    public String toString() {
        return "Categoria{" + "idCategoria=" + idCategoria + ", nombre=" + nombre + ", descripcion=" + descripcion + ", icono=" + icono + ", imagen=" + imagen + ", color=" + color + ", orden=" + orden + ", estado=" + estado + '}';
    }


    
}

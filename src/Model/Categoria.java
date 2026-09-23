
// Paquete Model
package Model;

// Importa Objects
import java.util.Objects;



// Modelo de una categoría de productos
public class Categoria {
    // Atributo idCategoria
    private int idCategoria;
    
    // Atributo nombre
    private String  nombre;
    
    // Atributo descripcion
    private String descripcion;

    // Atributo icono
    private String icono;

    // Atributo imagen
    private String imagen;
    
    // Atributo color
    private String color;
    
    // Atributo orden
    private int  orden;
    
    // Atributo estado
    private boolean estado;

    // Constructor vacío
    public Categoria() {
    }

    // Constructor con todos los datos
    public Categoria(int idCategoria, String nombre, String descripcion, String icono, String imagen, String color, int orden, boolean estado) {
        // Asigna idCategoria
        this.idCategoria = idCategoria;
        // Asigna nombre
        this.nombre = nombre;
        // Asigna descripcion
        this.descripcion = descripcion;
        // Asigna icono
        this.icono = icono;
        // Asigna imagen
        this.imagen = imagen;
        // Asigna color
        this.color = color;
        // Asigna orden
        this.orden = orden;
        // Asigna estado
        this.estado = estado;
    }

    // Devuelve idCategoria
    public int getIdCategoria() {
        // Retorna el valor
        return idCategoria;
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

    // Devuelve icono
    public String getIcono() {
        // Retorna el valor
        return icono;
    }

    // Devuelve imagen
    public String getImagen() {
        // Retorna el valor
        return imagen;
    }

    // Devuelve color
    public String getColor() {
        // Retorna el valor
        return color;
    }

    // Devuelve orden
    public int getOrden() {
        // Retorna el valor
        return orden;
    }

    // Devuelve estado
    public boolean isEstado() {
        // Retorna el valor
        return estado;
    }

    // Asigna idCategoria
    public void setIdCategoria(int idCategoria) {
        // Asigna idCategoria
        this.idCategoria = idCategoria;
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

    // Asigna icono
    public void setIcono(String icono) {
        // Asigna icono
        this.icono = icono;
    }

    // Asigna imagen
    public void setImagen(String imagen) {
        // Asigna imagen
        this.imagen = imagen;
    }

    // Asigna color
    public void setColor(String color) {
        // Asigna color
        this.color = color;
    }

    // Asigna orden
    public void setOrden(int orden) {
        // Asigna orden
        this.orden = orden;
    }

    // Asigna estado
    public void setEstado(boolean estado) {
        // Asigna estado
        this.estado = estado;
    }
    // Indica si la categoría está activa
    public boolean estaActiva(){

    // Devuelve el estado
    return estado;

}
// Sobrescribe equals
@Override
public boolean equals(Object obj) {

    // Si es el mismo objeto
    if (this == obj) {
        // son iguales
        return true;
    }

    // Si el otro no es una Categoria
    if (!(obj instanceof Categoria)) {
        // no son iguales
        return false;
    }

    // Convierte el objeto a Categoria
    Categoria other = (Categoria) obj;

    // Son iguales si tienen el mismo id
    return idCategoria == other.idCategoria;

}

// Sobrescribe hashCode
@Override
public int hashCode() {

    // Calcula el hash con el id
    return Objects.hash(idCategoria);

}

    // Sobrescribe toString
    @Override
    public String toString() {
        // Devuelve el texto con los datos de la categoría
        return "Categoria{" + "idCategoria=" + idCategoria + ", nombre=" + nombre + ", descripcion=" + descripcion + ", icono=" + icono + ", imagen=" + imagen + ", color=" + color + ", orden=" + orden + ", estado=" + estado + '}';
    }


    
}

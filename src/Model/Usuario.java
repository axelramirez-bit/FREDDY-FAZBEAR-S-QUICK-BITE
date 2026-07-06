
package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class Usuario {
    
    private int idUsuario;

    private Rol rol;

    private String nombre;

    private String apellido;

    private String correo;
    
    private String telefono;

    private String password;

    private LocalDate  fechaNacimiento;
    
    private String foto;
    
    private boolean estado;
    
    private LocalDateTime  fechaRegistro;
    
    private LocalDateTime  fechaActualizacion;

    public Usuario() {
    }

    public Usuario(int idUsuario, Rol rol, String nombre, String correo) {
        this.idUsuario = idUsuario;
        this.rol = rol;
        this.nombre = nombre;
        this.correo = correo;
    }

    public Usuario(int idUsuario, Rol rol, String nombre, String apellido, String correo, String telefono, String password, LocalDate fechaNacimiento, String foto, boolean estado, LocalDateTime fechaRegistro, LocalDateTime fechaActualizacion) {
        this.idUsuario = idUsuario;
        this.rol = rol;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.password = password;
        this.fechaNacimiento = fechaNacimiento;
        this.foto = foto;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
        this.fechaActualizacion = fechaActualizacion;
    }

    public Usuario(Rol rol, String nombre, String apellido, String telefono, String password, LocalDate fechaNacimiento) {
        this.rol = rol;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.password = password;
        this.fechaNacimiento = fechaNacimiento;
    }




    public int getIdUsuario() {
        return idUsuario;
    }

    public Rol getRol() {
        return rol;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate  getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getFoto() {
        return foto;
    }

    public boolean isEstado() {
        return estado;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }


    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFechaNacimiento(LocalDate  fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }


public String getNombreCompleto() {

    return (nombre == null ? "" : nombre)
            + " "
            + (apellido == null ? "" : apellido);

}
public boolean estaActivo(){

    return estado;

}
public boolean esAdministrador() {

    return rol != null
            && rol.getNombre().equalsIgnoreCase("Administrador");

}
public boolean esTrabajador() {

    return rol != null
            && rol.getNombre().equalsIgnoreCase("Trabajador");

}
public boolean esCliente() {

    return rol != null
            && rol.getNombre().equalsIgnoreCase("Cliente");

}
    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }
public boolean esRol(Rol tipo) {
    return rol != null && rol == tipo;
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
        final Usuario other = (Usuario) obj;
        return this.idUsuario == other.idUsuario;
    }

    @Override
    public String toString() {
        return "Usuario{" + "idUsuario=" + idUsuario + ", rol=" + rol + ", nombre=" + nombre + ", apellido=" + apellido + ", correo=" + correo + ", telefono=" + telefono +  ", fechaNacimiento=" + fechaNacimiento + ", foto=" + foto + ", estado=" + estado + ", fechaActualizacion=" + fechaActualizacion + '}';
    }

    
}

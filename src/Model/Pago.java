
// Paquete Model
package Model;

// Importa BigDecimal
import java.math.BigDecimal;
// Importa LocalDateTime
import java.time.LocalDateTime;
// Importa Objects
import java.util.Objects;

// Modelo de un pago de un pedido
public class Pago {
    // Atributo idPago
    private int idPago;
    
    // Atributo pedido
    private Pedido  pedido;
    
    // Atributo metodoPago
    private MetodoPago metodoPago;

    // Atributo monto
    private BigDecimal monto;

    // Atributo fechaPago
    private LocalDateTime fechaPago;
    
    // Atributo estado
    private EstadoPago estado;
    
    // Atributo referencia
    private String referencia;

    // Constructor vacío
    public Pago() {
    }

    // Constructor con todos los datos
    public Pago(int idPago, Pedido pedido, MetodoPago metodoPago, BigDecimal monto, LocalDateTime fechaPago, EstadoPago estado, String referencia) {
        // Asigna idPago
        this.idPago = idPago;
        // Asigna pedido
        this.pedido = pedido;
        // Asigna metodoPago
        this.metodoPago = metodoPago;
        // Asigna monto
        this.monto = monto;
        // Asigna fechaPago
        this.fechaPago = fechaPago;
        // Asigna estado
        this.estado = estado;
        // Asigna referencia
        this.referencia = referencia;
    }





    // Devuelve idPago
    public int getIdPago() {
        // Retorna el valor
        return idPago;
    }

    // Devuelve pedido
    public Pedido getPedido() {
        // Retorna el valor
        return pedido;
    }



    // Devuelve metodoPago
    public MetodoPago getMetodoPago() {
        // Retorna el valor
        return metodoPago;
    }

    // Devuelve monto
    public BigDecimal getMonto() {
        // Retorna el valor
        return monto;
    }

    // Devuelve fechaPago
    public LocalDateTime getFechaPago() {
        // Retorna el valor
        return fechaPago;
    }

    // Devuelve estado
    public EstadoPago getEstado() {
        // Retorna el valor
        return estado;
    }

    // Devuelve referencia
    public String getReferencia() {
        // Retorna el valor
        return referencia;
    }

    // Asigna idPago
    public void setIdPago(int idPago) {
        // Asigna idPago
        this.idPago = idPago;
    }

    // Asigna pedido
    public void setPedido(Pedido pedido) {
        // Asigna pedido
        this.pedido = pedido;
    }



    // Asigna metodoPago
    public void setMetodoPago(MetodoPago metodoPago) {
        // Asigna metodoPago
        this.metodoPago = metodoPago;
    }

    // Asigna monto
    public void setMonto(BigDecimal monto) {
        // Asigna monto
        this.monto = monto;
    }

    // Asigna fechaPago
    public void setFechaPago(LocalDateTime fechaPago) {
        // Asigna fechaPago
        this.fechaPago = fechaPago;
    }

    // Asigna estado
    public void setEstado(EstadoPago estado) {
        // Asigna estado
        this.estado = estado;
    }

    // Asigna referencia
    public void setReferencia(String referencia) {
        // Asigna referencia
        this.referencia = referencia;
    }

    // Indica si el pago está pagado
    public boolean estaPagado() {

    // Devuelve true si el estado es PAGADO
    return estado == EstadoPago.PAGADO;

}
    // Marca el pago como pagado
    public void confirmarPago() {

    // Cambia el estado a PAGADO
    estado = EstadoPago.PAGADO;

}
    // Marca el pago como rechazado
    public void rechazarPago() {

    // Cambia el estado a RECHAZADO
    estado = EstadoPago.RECHAZADO;

}
    // Marca el pago como pendiente
    public void marcarPendiente() {

    // Cambia el estado a PENDIENTE
    estado = EstadoPago.PENDIENTE;

}
// Sobrescribe equals
@Override
public boolean equals(Object obj) {

    // Si es el mismo objeto
    if (this == obj) {
        // son iguales
        return true;
    }

    // Si el otro no es un Pago
    if (!(obj instanceof Pago)) {
        // no son iguales
        return false;
    }

    // Convierte el objeto a Pago
    Pago other = (Pago) obj;

    // Son iguales si tienen el mismo id
    return idPago == other.idPago;

}

// Sobrescribe hashCode
@Override
public int hashCode() {

    // Calcula el hash con el id
    return Objects.hash(idPago);

}

    // Sobrescribe toString
    @Override
    public String toString() {
        // Devuelve el texto con los datos del pago
        return "Pago{" + "idPago=" + idPago + ", pedido=" + pedido + ", metodoPago=" + metodoPago + ", monto=" + monto + ", fechaPago=" + fechaPago + ", estado=" + estado + ", referencia=" + referencia + '}';
    }


    
}

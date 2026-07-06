
package Model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Pago {
    private int idPago;
    
    private Pedido  pedido;
    
    private MetodoPago metodoPago;

    private double monto;

    private LocalDateTime fechaPago;
    
    private EstadoPago estado;
    
    private String referencia;

    public Pago() {
    }

    public Pago(int idPago, Pedido pedido, MetodoPago metodoPago, double monto, LocalDateTime fechaPago, EstadoPago estado, String referencia) {
        this.idPago = idPago;
        this.pedido = pedido;
        this.metodoPago = metodoPago;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.estado = estado;
        this.referencia = referencia;
    }




    public int getIdPago() {
        return idPago;
    }

    public Pedido getPedido() {
        return pedido;
    }



    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public double getMonto() {
        return monto;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public EstadoPago getEstado() {
        return estado;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }



    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public void setEstado(EstadoPago estado) {
        this.estado = estado;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public boolean estaPagado() {

    return estado == EstadoPago.PAGADO;

}
    public void confirmarPago() {

    estado = EstadoPago.PAGADO;

}
    public void rechazarPago() {

    estado = EstadoPago.RECHAZADO;

}
    public void marcarPendiente() {

    estado = EstadoPago.PENDIENTE;

}
@Override
public boolean equals(Object obj) {

    if (this == obj) {
        return true;
    }

    if (!(obj instanceof Pago)) {
        return false;
    }

    Pago other = (Pago) obj;

    return idPago == other.idPago;

}

@Override
public int hashCode() {

    return Objects.hash(idPago);

}

    @Override
    public String toString() {
        return "Pago{" + "idPago=" + idPago + ", pedido=" + pedido + ", metodoPago=" + metodoPago + ", monto=" + monto + ", fechaPago=" + fechaPago + ", estado=" + estado + ", referencia=" + referencia + '}';
    }


    
}

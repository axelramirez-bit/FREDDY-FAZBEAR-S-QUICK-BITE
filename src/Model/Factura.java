
package Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.time.format.DateTimeFormatter;

public class Factura {
    private int idFactura;
    
    private Pedido pedido;
    
    private String   numeroFactura;

    private LocalDateTime fecha;
    
    private Usuario cliente;
    
    private String direccion;
    
    private BigDecimal subtotal;

    private BigDecimal descuento;
    
    private BigDecimal total;
    private BigDecimal iva;
    private int  nit;

    public Factura() {
    }

    public Factura(int idFactura, Pedido pedido, String numeroFactura, LocalDateTime fecha, Usuario cliente, String direccion, BigDecimal subtotal, BigDecimal descuento, BigDecimal total, BigDecimal iva, int nit) {
        this.idFactura = idFactura;
        this.pedido = pedido;
        this.numeroFactura = numeroFactura;
        this.fecha = fecha;
        this.cliente = cliente;
        this.direccion = direccion;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = total;
        this.iva = iva;
        this.nit = nit;
    }



    public Usuario getCliente() {
        return cliente;
    }



    public int getIdFactura() {
        return idFactura;
    }

    public Pedido getPedido() {
        return pedido;
    }



    public String getNumeroFactura() {
        return numeroFactura;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }



    public String getDireccion() {
        return direccion;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public int getNit() {
        return nit;
    }


    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }


    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }




    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public void setNit(int nit) {
        this.nit = nit;
    }


public String generarNumeroFactura() {

    return "FAC-" + String.format("%06d", idFactura);

}
public String getNombreCliente() {

    return cliente.getNombreCompleto();

}
public String getFechaFormateada() {

    DateTimeFormatter formato =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    return fecha.format(formato);

}
@Override
public boolean equals(Object obj) {

    if (this == obj) {
        return true;
    }

    if (!(obj instanceof Factura)) {
        return false;
    }

    Factura other = (Factura) obj;

    return idFactura == other.idFactura;

}

@Override
public int hashCode() {

    return Objects.hash(idFactura);

}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Factura{");
        sb.append("idFactura=").append(idFactura);
        sb.append(", pedido=").append(pedido);
        sb.append(", numeroFactura=").append(numeroFactura);
        sb.append(", fecha=").append(fecha);
        sb.append(", cliente=").append(cliente);
        sb.append(", direccion=").append(direccion);
        sb.append(", subtotal=").append(subtotal);
        sb.append(", descuento=").append(descuento);
        sb.append(", total=").append(total);
        sb.append('}');
        return sb.toString();
    }


    
}

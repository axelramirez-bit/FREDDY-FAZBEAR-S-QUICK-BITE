package Model;

import java.math.BigDecimal;

public class DetalleFactura {

    // ==========================================================
    // ATRIBUTOS
    // ==========================================================

    private int idDetalle;

    private Factura factura;

    private Producto producto;

    private int cantidad;

    private BigDecimal precioUnitario;

    private BigDecimal subtotal;


    // ==========================================================
    // CONSTRUCTORES
    // ==========================================================

    /**
     * Constructor vacío.
     * Necesario para crear el objeto y establecer sus datos
     * posteriormente mediante setters.
     */
    public DetalleFactura() {
    }


    /**
     * Constructor principal.
     *
     * @param producto Producto comprado
     * @param cantidad Cantidad comprada
     * @param precioUnitario Precio del producto
     */
    public DetalleFactura(
            Producto producto,
            int cantidad,
            BigDecimal precioUnitario) {

        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;

        calcularSubtotal();
    }


    /**
     * Constructor completo.
     */
    public DetalleFactura(
            int idDetalle,
            Factura factura,
            Producto producto,
            int cantidad,
            BigDecimal precioUnitario,
            BigDecimal subtotal) {

        this.idDetalle = idDetalle;
        this.factura = factura;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }


    // ==========================================================
    // CÁLCULOS
    // ==========================================================

    /**
     * Calcula el subtotal del detalle:
     *
     * cantidad × precioUnitario
     */
    public void calcularSubtotal() {

        if (precioUnitario == null) {
            subtotal = BigDecimal.ZERO;
            return;
        }

        subtotal = precioUnitario.multiply(
                BigDecimal.valueOf(cantidad)
        );
    }


    /**
     * Obtiene el subtotal calculándolo nuevamente.
     *
     * Esto permite mantener el valor actualizado si cambia
     * la cantidad o el precio.
     *
     * @return subtotal
     */
    public BigDecimal obtenerSubtotal() {

        calcularSubtotal();

        return subtotal;
    }


    // ==========================================================
    // GETTERS Y SETTERS
    // ==========================================================

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }


    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }


    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }


    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero."
            );
        }

        this.cantidad = cantidad;

        calcularSubtotal();
    }


    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(
            BigDecimal precioUnitario) {

        if (precioUnitario == null) {
            throw new IllegalArgumentException(
                    "El precio unitario no puede ser null."
            );
        }

        if (precioUnitario.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "El precio unitario no puede ser negativo."
            );
        }

        this.precioUnitario = precioUnitario;

        calcularSubtotal();
    }


    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }


    // ==========================================================
    // UTILIDADES
    // ==========================================================

    /**
     * Obtiene el nombre del producto.
     *
     * Evita tener que escribir:
     * detalle.getProducto().getNombre()
     *
     * en diferentes partes de la interfaz.
     */
    public String getNombreProducto() {

        if (producto == null) {
            return "";
        }

        return producto.getNombre();
    }


    @Override
    public String toString() {

        return "DetalleFactura{"
                + "idDetalle=" + idDetalle
                + ", producto=" + getNombreProducto()
                + ", cantidad=" + cantidad
                + ", precioUnitario=" + precioUnitario
                + ", subtotal=" + subtotal
                + '}';
    }
}
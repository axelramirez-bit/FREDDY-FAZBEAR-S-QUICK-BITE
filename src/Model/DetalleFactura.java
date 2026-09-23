// Paquete Model
package Model;

// Importa BigDecimal
import java.math.BigDecimal;

// Modelo de un detalle de factura (solo en memoria)
public class DetalleFactura {

    // ==========================================================
    // ATRIBUTOS
    // ==========================================================

    // Atributo idDetalle
    private int idDetalle;

    // Atributo factura
    private Factura factura;

    // Atributo producto
    private Producto producto;

    // Atributo cantidad
    private int cantidad;

    // Atributo precioUnitario
    private BigDecimal precioUnitario;

    // Atributo subtotal
    private BigDecimal subtotal;


    // ==========================================================
    // CONSTRUCTORES
    // ==========================================================

    /**
     * Constructor vacío.
     * Necesario para crear el objeto y establecer sus datos
     * posteriormente mediante setters.
     */
    // Constructor vacío
    public DetalleFactura() {
    }


    /**
     * Constructor principal.
     *
     * @param producto Producto comprado
     * @param cantidad Cantidad comprada
     * @param precioUnitario Precio del producto
     */
    // Constructor principal: producto, cantidad y precio
    public DetalleFactura(
            Producto producto,
            int cantidad,
            BigDecimal precioUnitario) {

        // Asigna producto
        this.producto = producto;
        // Asigna cantidad
        this.cantidad = cantidad;
        // Asigna precioUnitario
        this.precioUnitario = precioUnitario;

        // Calcula el subtotal
        calcularSubtotal();
    }


    /**
     * Constructor completo.
     */
    // Constructor completo con todos los datos
    public DetalleFactura(
            int idDetalle,
            Factura factura,
            Producto producto,
            int cantidad,
            BigDecimal precioUnitario,
            BigDecimal subtotal) {

        // Asigna idDetalle
        this.idDetalle = idDetalle;
        // Asigna factura
        this.factura = factura;
        // Asigna producto
        this.producto = producto;
        // Asigna cantidad
        this.cantidad = cantidad;
        // Asigna precioUnitario
        this.precioUnitario = precioUnitario;
        // Asigna subtotal
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
    // Calcula el subtotal del detalle
    public void calcularSubtotal() {

        // Si no hay precio unitario
        if (precioUnitario == null) {
            // el subtotal es cero
            subtotal = BigDecimal.ZERO;
            // termina el método
            return;
        }

        // Subtotal = precio unitario por cantidad
        subtotal = precioUnitario.multiply(
                // Cantidad convertida a BigDecimal
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
    // Devuelve el subtotal recalculado
    public BigDecimal obtenerSubtotal() {

        // Recalcula el subtotal
        calcularSubtotal();

        // Devuelve el subtotal
        return subtotal;
    }


    // ==========================================================
    // GETTERS Y SETTERS
    // ==========================================================

    // Devuelve idDetalle
    public int getIdDetalle() {
        // Retorna el valor
        return idDetalle;
    }

    // Asigna idDetalle
    public void setIdDetalle(int idDetalle) {
        // Asigna idDetalle
        this.idDetalle = idDetalle;
    }


    // Devuelve factura
    public Factura getFactura() {
        // Retorna el valor
        return factura;
    }

    // Asigna factura
    public void setFactura(Factura factura) {
        // Asigna factura
        this.factura = factura;
    }


    // Devuelve producto
    public Producto getProducto() {
        // Retorna el valor
        return producto;
    }

    // Asigna producto
    public void setProducto(Producto producto) {
        // Asigna producto
        this.producto = producto;
    }


    // Devuelve cantidad
    public int getCantidad() {
        // Retorna el valor
        return cantidad;
    }

    // Asigna la cantidad (debe ser mayor que cero)
    public void setCantidad(int cantidad) {

        // Si la cantidad no es positiva
        if (cantidad <= 0) {
            // lanza error
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero."
            );
        }

        // Asigna cantidad
        this.cantidad = cantidad;

        // Recalcula el subtotal
        calcularSubtotal();
    }


    // Devuelve precioUnitario
    public BigDecimal getPrecioUnitario() {
        // Retorna el valor
        return precioUnitario;
    }

    // Asigna el precio unitario (no nulo ni negativo)
    public void setPrecioUnitario(
            BigDecimal precioUnitario) {

        // Si es nulo
        if (precioUnitario == null) {
            // lanza error
            throw new IllegalArgumentException(
                    "El precio unitario no puede ser null."
            );
        }

        // Si es negativo
        if (precioUnitario.compareTo(BigDecimal.ZERO) < 0) {
            // lanza error
            throw new IllegalArgumentException(
                    "El precio unitario no puede ser negativo."
            );
        }

        // Asigna precioUnitario
        this.precioUnitario = precioUnitario;

        // Recalcula el subtotal
        calcularSubtotal();
    }


    // Devuelve subtotal
    public BigDecimal getSubtotal() {
        // Retorna el valor
        return subtotal;
    }

    // Asigna subtotal
    public void setSubtotal(BigDecimal subtotal) {
        // Asigna subtotal
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
    // Devuelve el nombre del producto
    public String getNombreProducto() {

        // Si no hay producto
        if (producto == null) {
            // devuelve texto vacío
            return "";
        }

        // Devuelve el nombre del producto
        return producto.getNombre();
    }


    // Sobrescribe toString
    @Override
    public String toString() {

        // Arma el texto con los datos del detalle
        return "DetalleFactura{"
                + "idDetalle=" + idDetalle
                // Usa el nombre del producto
                + ", producto=" + getNombreProducto()
                + ", cantidad=" + cantidad
                + ", precioUnitario=" + precioUnitario
                + ", subtotal=" + subtotal
                + '}';
    }
}
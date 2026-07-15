package View.Componentes;

import Model.Producto;
import View.Utils.*;
import java.awt.*;
import javax.swing.*;

/**
 * =============================================================== TARJETA
 * PRODUCTO ---------------------------------------------------------------
 * Componente reutilizable para mostrar un producto del catálogo.
 *
 * La tarjeta está compuesta por:
 *
 * • Imagen del producto • Información • Selector de cantidad • Botón Agregar al
 * carrito
 *
 * La construcción se divide en varias partes para mantener una arquitectura
 * limpia. ===============================================================
 */
public class TarjetaProducto extends PanelRedondeado {

    //==========================================================
    // PANELES
    //==========================================================
    private JPanel panelImagen;

    private JPanel panelInformacion;

    //==========================================================
    // COMPONENTES
    //==========================================================
    private JLabel lblImagen;

    private JLabel lblNombre;

    private JLabel lblDescripcion;

    private JLabel lblPrecio;

    private JButton btnAgregar;

    private Producto producto;
    // Se implementará en la Parte 4
    private SelectorCantidad selectorCantidad;

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public TarjetaProducto() {

        inicializar();

    }

    public TarjetaProducto(Producto producto) {

        this();

        setProducto(producto);

    }

    //==========================================================
    // INICIALIZAR
    //==========================================================
    private void inicializar() {

        configurarTarjeta();

        crearPanelPrincipal();

        crearPanelImagen();

        crearPanelInformacion();
        crearImagen();

        crearContenido();

    }

    //==========================================================
    // CONFIGURACIÓN
    //==========================================================
    private void configurarTarjeta() {

        EstilosComponentes.aplicarEstiloTarjeta(this);

        EstilosComponentes.aplicarTamaño(
                this,
                AdministradorTema.anchoTarjetaProducto(),
                AdministradorTema.altoTarjetaProducto());

    }

    private void crearPanelPrincipal() {

        setLayout(new GridBagLayout());

    }

    //==========================================================
    // PANEL IMAGEN
    //==========================================================
    private void crearPanelImagen() {

        panelImagen
                = FabricaPaneles.crearTransparente();

        panelImagen.setLayout(new BorderLayout());

        GridBagConstraints gbc
                = new GridBagConstraints();

        gbc.gridx = 0;

        gbc.gridy = 0;

        gbc.weightx = 0.38;

        gbc.weighty = 1;

        gbc.fill = GridBagConstraints.BOTH;

        gbc.insets = new Insets(
                AdministradorTema.espacioMediano(),
                AdministradorTema.espacioMediano(),
                AdministradorTema.espacioMediano(),
                AdministradorTema.espacioPequeño());

        add(panelImagen, gbc);

    }

    //==========================================================
    // PANEL INFORMACIÓN
    //==========================================================
    private void crearPanelInformacion() {

        panelInformacion
                = FabricaPaneles.crearTransparente();

        panelInformacion.setLayout(
                new BoxLayout(
                        panelInformacion,
                        BoxLayout.Y_AXIS)
        );

        GridBagConstraints gbc
                = new GridBagConstraints();

        gbc.gridx = 1;

        gbc.gridy = 0;

        gbc.weightx = 0.62;

        gbc.weighty = 1;

        gbc.fill = GridBagConstraints.BOTH;

        gbc.insets = new Insets(
                AdministradorTema.espacioMediano(),
                AdministradorTema.espacioPequeño(),
                AdministradorTema.espacioMediano(),
                AdministradorTema.espacioMediano());

        add(panelInformacion, gbc);

    }

    //==========================================================
// IMAGEN
//==========================================================
    private void crearImagen() {

        lblImagen = new JLabel();

        lblImagen.setHorizontalAlignment(
                SwingConstants.CENTER);

        lblImagen.setVerticalAlignment(
                SwingConstants.CENTER);

        lblImagen.setBorder(
                AdministradorTema.bordePequeño());

        panelImagen.add(
                lblImagen,
                BorderLayout.CENTER);

    }

    private void crearContenido() {

        crearInformacion();

        crearAcciones();

    }

    private void crearInformacion() {

        crearNombre();

        crearDescripcion();

        crearPrecio();

    }

    private void crearAcciones() {

        JPanel acciones
                = FabricaPaneles.crearTransparente();

        acciones.setLayout(new BorderLayout(
                AdministradorTema.espacioMediano(),
                0));

        selectorCantidad = new SelectorCantidad();

        btnAgregar
                = FabricaBotones.crearPrimario(
                        "Agregar al carrito");

        acciones.add(
                selectorCantidad,
                BorderLayout.WEST);

        acciones.add(
                btnAgregar,
                BorderLayout.CENTER);

        panelInformacion.add(acciones);

    }

    private void crearNombre() {

        lblNombre
                = FabricaEtiquetas.crearTitulo("");

        panelInformacion.add(lblNombre);

        panelInformacion.add(
                Box.createVerticalStrut(
                        AdministradorTema.espacioPequeño()));

    }

    private void crearDescripcion() {

        lblDescripcion
                = FabricaEtiquetas.crearTexto("");

        lblDescripcion.setAlignmentX(
                Component.LEFT_ALIGNMENT);

        panelInformacion.add(lblDescripcion);

        panelInformacion.add(
                Box.createVerticalGlue());

    }

    private void crearPrecio() {

        lblPrecio
                = FabricaEtiquetas.crearSubtitulo("");

        lblPrecio.setForeground(
                AdministradorTema.colorPrincipal());

        panelInformacion.add(lblPrecio);

        panelInformacion.add(
                Box.createVerticalStrut(
                        AdministradorTema.espacioMediano()));

    }

    public void setProducto(Producto producto) {

        this.producto = producto;

        if (producto == null) {
            limpiar();
            return;
        }

        lblNombre.setText(producto.getNombre());

        lblDescripcion.setText(
                "<html>" + producto.getDescripcion() + "</html>");

        lblPrecio.setText(
                String.format("Q %.2f", producto.getPrecio()));

        actualizarImagen();

    }

    private void actualizarImagen() {

        if (producto == null) {
            return;
        }

        lblImagen.setIcon(
                FabricaImagenes.producto(
                        producto.getImagenPrincipal()));

    }

    public void actualizar() {

        setProducto(producto);

    }

    public void setDisponible(boolean disponible) {

        btnAgregar.setEnabled(disponible);

        selectorCantidad.setEditable(disponible);

        repaint();

    }

    public void resaltarOferta() {

        setColorBorde(
                AdministradorTema.colorPrincipal());

        setMostrarBorde(true);

        repaint();

    }

    public void quitarResaltado() {

        setMostrarBorde(false);

    }

    public void setImagen(String nombreImagen) {

        lblImagen.setIcon(
                FabricaImagenes.producto(nombreImagen));

    }

    public JLabel getImagen() {

        return lblImagen;

    }

    public String getNombre() {

        return lblNombre.getText();

    }

    public String getDescripcion() {

        return lblDescripcion.getText();

    }

    public void setCantidad(int cantidad) {

        selectorCantidad.setCantidad(cantidad);

    }

    public void reiniciar() {

        selectorCantidad.reiniciar();

    }

    public double getPrecio() {

        if (producto == null) {
            return 0;
        }

        return producto.getPrecio();

    }

    public Producto getProducto() {

        return producto;

    }

    public JButton getBotonAgregar() {

        return btnAgregar;

    }

    public SelectorCantidad getSelectorCantidad() {

        return selectorCantidad;

    }

    public void addAgregarListener(
            java.awt.event.ActionListener listener) {

        btnAgregar.addActionListener(listener);

    }

    public void setImagen(ImageIcon icono) {

        lblImagen.setIcon(icono);

    }

    public void setTextoBoton(String texto) {

        btnAgregar.setText(texto);

    }

    public int getCantidad() {

        return selectorCantidad.getCantidad();

    }

    public void setDescripcion(String descripcion) {

        lblDescripcion.setText(
                "<html>" + descripcion + "</html>");

    }

    public void setNombre(String nombre) {

        lblNombre.setText(nombre);

    }

    public void setPrecio(double precio) {

        lblPrecio.setText(
                String.format("Q %.2f", precio));

    }

    public void mostrarBoton(boolean mostrar) {

        btnAgregar.setVisible(mostrar);

    }

    public void mostrarSelector(boolean mostrar) {

        selectorCantidad.setVisible(mostrar);

    }

    private void crearBotonAgregar() {

        btnAgregar
                = FabricaBotones.crearPrimario("Agregar al carrito");

        btnAgregar.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelInformacion.add(btnAgregar);

    }

    private void crearSelectorCantidad() {

        selectorCantidad = new SelectorCantidad();

        selectorCantidad.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelInformacion.add(selectorCantidad);

        panelInformacion.add(
                Box.createVerticalStrut(
                        AdministradorTema.espacioMediano()));

    }

    public void limpiar() {

        producto = null;

        lblNombre.setText("");

        lblDescripcion.setText("");

        lblPrecio.setText("");

        lblImagen.setIcon(null);

        selectorCantidad.reiniciar();

    }
}

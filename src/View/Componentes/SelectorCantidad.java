package View.Componentes;

import View.Utils.*;
import java.awt.*;
import javax.swing.*;

/**
 * =============================================================== SELECTOR DE
 * CANTIDAD ---------------------------------------------------------------
 * Componente reutilizable para seleccionar cantidades.
 *
 * Se utiliza en:
 *
 * • TarjetaProducto • Carrito • Pedidos • Promociones
 *
 * Características:
 *
 * • Botón disminuir • Cantidad centrada • Botón aumentar • Límite mínimo •
 * Límite máximo configurable
 * ===============================================================
 */
public class SelectorCantidad extends PanelRedondeado {

    //==========================================================
    // COMPONENTES
    //==========================================================
    private JButton btnMenos;

    private JButton btnMas;

    private JLabel lblCantidad;

    //==========================================================
    // ATRIBUTOS
    //==========================================================
    private int cantidad;

    private int minimo;

    private int maximo;

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public SelectorCantidad() {

        inicializar();

    }

    //==========================================================
    // INICIALIZAR
    //==========================================================
    private void inicializar() {

        cantidad = 1;

        minimo = 1;

        maximo = 99;

        configurarPanel();

        crearComponentes();

        actualizarCantidad();

    }

    //==========================================================
    // PANEL
    //==========================================================
    private void configurarPanel() {

        setLayout(new GridLayout(1, 3));

        EstilosComponentes.aplicarEstiloTarjeta(this);

        EstilosComponentes.aplicarTamaño(
                this,
                AdministradorTema.anchoSelectorCantidad(),
                AdministradorTema.altoSelectorCantidad());

    }

    //==========================================================
    // COMPONENTES
    //==========================================================
    private void crearComponentes() {

        crearBotonMenos();

        crearCantidad();

        crearBotonMas();

    }
//==========================================================
// BOTÓN MENOS
//==========================================================

    private void crearBotonMenos() {

        btnMenos = FabricaBotones.crearSecundario("-");

        EstilosComponentes.aplicarTamaño(
                btnMenos,
                AdministradorTema.anchoBotonCantidad(),
                AdministradorTema.altoBotonCantidad());

        btnMenos.addActionListener(e -> disminuir());

        add(btnMenos);

    }

//==========================================================
// CANTIDAD
//==========================================================
    private void crearCantidad() {

        lblCantidad = FabricaEtiquetas.crearCantidad("1");

        lblCantidad.setHorizontalAlignment(
                SwingConstants.CENTER);

        actualizarCantidad();

        add(lblCantidad);

    }

//==========================================================
// BOTÓN MÁS
//==========================================================
    private void crearBotonMas() {

        btnMas = FabricaBotones.crearSecundario("+");

        EstilosComponentes.aplicarTamaño(
        btnMas,
        AdministradorTema.anchoBotonCantidad(),
        AdministradorTema.altoBotonCantidad());
        btnMas.addActionListener(e -> aumentar());

        add(btnMas);

    }

//==========================================================
// AUMENTAR
//==========================================================
    private void aumentar() {

        if (cantidad < maximo) {

            cantidad++;

            actualizarCantidad();

        }

    }

//==========================================================
// DISMINUIR
//==========================================================
    private void disminuir() {

        if (cantidad > minimo) {

            cantidad--;

            actualizarCantidad();

        }

    }

//==========================================================
// GETTERS
//==========================================================
    public int getCantidad() {

        return cantidad;

    }

    public int getMinimo() {

        return minimo;

    }

    public int getMaximo() {

        return maximo;

    }

    public JButton getBotonMas() {

        return btnMas;

    }

    public JButton getBotonMenos() {

        return btnMenos;

    }

//==========================================================
// SETTERS
//==========================================================
    public void setCantidad(int cantidad) {

        if (cantidad < minimo) {

            cantidad = minimo;

        }

        if (cantidad > maximo) {

            cantidad = maximo;

        }

        this.cantidad = cantidad;

        actualizarCantidad();

    }

    public void setMinimo(int minimo) {

        this.minimo = minimo;

        if (cantidad < minimo) {

            cantidad = minimo;

            actualizarCantidad();

        }

    }

    public void setMaximo(int maximo) {

        this.maximo = maximo;

        if (cantidad > maximo) {

            cantidad = maximo;

            actualizarCantidad();

        }

    }

//==========================================================
// UTILIDADES
//==========================================================
    public void reiniciar() {

        cantidad = minimo;

        actualizarCantidad();

    }
//==========================================================
// LISTENER
//==========================================================

public interface CantidadListener {

    void cantidadCambiada(int cantidad);

}

    private CantidadListener listener;

    public void setCantidadListener(CantidadListener listener) {

        this.listener = listener;

    }

private void actualizarCantidad() {

    if (lblCantidad != null) {
        lblCantidad.setText(String.valueOf(cantidad));
    }

    actualizarBotones();

    if (listener != null) {
        listener.cantidadCambiada(cantidad);
    }

}

    public void setEditable(boolean editable) {

        btnMas.setEnabled(editable);

        btnMenos.setEnabled(editable);

    }

    public boolean llegoAlMaximo() {

        return cantidad >= maximo;

    }

    public boolean llegoAlMinimo() {

        return cantidad <= minimo;

    }

    private void actualizarBotones() {

        btnMenos.setEnabled(cantidad > minimo);

        btnMas.setEnabled(cantidad < maximo);

    }

    public void setTamaño(int ancho, int alto) {

        EstilosComponentes.aplicarTamaño(
                this,
                ancho,
                alto);

    }

public void setColorPrincipal(Color color) {

    if (btnMas instanceof BotonRedondeado botonMas) {
        botonMas.setColorFondo(color);
    }

    if (btnMenos instanceof BotonRedondeado botonMenos) {
        botonMenos.setColorFondo(color);
    }

}
public void configurarLimites(int minimo, int maximo) {

    this.minimo = minimo;
    this.maximo = maximo;

    setCantidad(cantidad);

}

    public void soloLectura() {

        btnMas.setVisible(false);

        btnMenos.setVisible(false);

    }
    
}

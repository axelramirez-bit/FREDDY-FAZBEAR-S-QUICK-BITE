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

        // BUG REAL QUE ESTO CORRIGE (confirmado renderizando el
        // componente): EstilosComponentes.aplicarEstiloTarjeta(this)
        // le ponía a este selector el borde/padding pensado para
        // tarjetas grandes (AdministradorTema.bordeTarjeta() =
        // UIConstants.ESPACIADO_GRANDE = 24px POR LADO, o sea 48px
        // de padding vertical). El selector completo mide solo
        // ALTO_SELECTOR_CANTIDAD = 38px de alto: el padding por sí
        // solo ya era más grande que todo el componente. El
        // GridLayout(1,3) terminaba repartiendo un alto NEGATIVO
        // entre los 3 hijos (botón "-", número, botón "+"), por eso
        // no se veía nada dentro del selector — no era un bug del
        // propio SelectorCantidad ni de TarjetaProducto, era este
        // borde de más. Un control compacto como este no necesita
        // padding interno: PanelRedondeado ya dibuja su forma con
        // esquinas redondeadas en paintComponent().
        setBorder(FabricaBordes.vacio());

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

        // BUG REAL QUE ESTO CORRIGE (confirmado renderizando el
        // componente): FabricaBotones.crearSecundario() aplica
        // FabricaBordes.boton() = 15px de padding POR LADO (30px en
        // total), pensado para botones normales como "Agregar al
        // carrito" (mucho más anchos). Este botón se encoge después
        // a 28x28 con aplicarTamaño(): con 30px de padding metidos en
        // un botón de 28px de ancho, no queda espacio para dibujar
        // el "-", así que Swing lo recorta y pinta "..." en su
        // lugar. Se le quita el padding para botones compactos como
        // este; con la letra centrada por el propio JButton alcanza
        // de sobra en 28x28.
        btnMenos.setBorder(BorderFactory.createEmptyBorder());

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

        // No llamar aquí a actualizarCantidad(): btnMas todavía no existe
        // (se crea después, en crearBotonMas()). inicializar() ya llama a
        // actualizarCantidad() una vez que los 3 componentes existen.

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

        // Mismo bug que btnMenos, ver comentario en crearBotonMenos().
        btnMas.setBorder(BorderFactory.createEmptyBorder());

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
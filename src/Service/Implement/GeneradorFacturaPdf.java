package Service.Implement;

import Model.DetalleFactura;
import Model.Factura;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.List;
import java.util.Properties;

/**
 * Servicio encargado de:
 *
 * 1. Validar una factura.
 * 2. Calcular sus valores.
 * 3. Generar la factura PDF.
 * 4. Enviar la factura por correo electrónico.
 *
 * Tecnologías utilizadas:
 * - Apache PDFBox
 * - JavaMail
 *
 * @author Axel
 */
public class GeneradorFacturaPdf {

    // ==========================================================
    // CONFIGURACIÓN GENERAL
    // ==========================================================

    private static final String NOMBRE_NEGOCIO =
            "FREDDY FAZBEAR'S";

    private static final String NOMBRE_COMERCIAL =
            "QUICK BITE";

    private static final String SLOGAN =
            "DIVERSIÓN Y SABOR EN CADA BOCADO";

    private static final String DIRECCION_NEGOCIO =
            "Ciudad de Guatemala, Guatemala";

    private static final String CORREO_NEGOCIO =
            "info@freddyquickbite.com";


    // ==========================================================
    // COLORES OFICIALES FREDDY QUICK BITE
    // ==========================================================

    private static final Color ROJO =
            Color.decode("#D62828");

    private static final Color AMARILLO =
            Color.decode("#F4C542");

    private static final Color FONDO =
            Color.decode("#FFF8E8");

    private static final Color TARJETA =
            Color.WHITE;

    private static final Color TEXTO =
            Color.decode("#3D2C29");

    private static final Color VERDE =
            Color.decode("#2E7D32");

    private static final Color GRIS =
            Color.decode("#6B5B57");


    // ==========================================================
    // CONFIGURACIÓN PDF
    // ==========================================================

    private static final float MARGEN = 40f;

    private static final PDRectangle TAMAÑO_PAGINA =
            PDRectangle.LETTER;

    private static final float ANCHO_PAGINA =
            TAMAÑO_PAGINA.getWidth();

    private static final float ALTO_PAGINA =
            TAMAÑO_PAGINA.getHeight();


    // ==========================================================
    // IVA
    // ==========================================================

    private static final BigDecimal TASA_IVA =
            new BigDecimal("0.12");


    // ==========================================================
    // RUTAS DE RECURSOS
    // ==========================================================

    private static final String RUTA_LOGO =
            "/Resources/imagenes/Logotipo.png";


    // ==========================================================
    // GENERAR PDF
    // ==========================================================

    /**
     * Genera una factura PDF profesional.
     *
     * @param factura factura que se desea generar
     * @return archivo PDF generado
     * @throws Exception si ocurre un error durante la generación
     */
    public File generarPdf(Factura factura) throws Exception {

        validarFactura(factura);

        // Recalcular los valores antes de generar el documento.
        factura.calcularSubtotal();
        factura.calcularIva();
        factura.calcularTotal();

        String numeroFactura =
                factura.getNumeroFactura();

        if (numeroFactura == null ||
                numeroFactura.trim().isEmpty()) {

            numeroFactura =
                    factura.generarNumeroFactura();
        }

        File carpeta =
                new File("facturas");

        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        File archivo =
                new File(
                        carpeta,
                        "Factura_"
                        + numeroFactura
                        + ".pdf"
                );


        try (PDDocument documento =
                     new PDDocument()) {

            PDPage pagina =
                    new PDPage(TAMAÑO_PAGINA);

            documento.addPage(pagina);

            try (PDPageContentStream contenido =
                         new PDPageContentStream(
                                 documento,
                                 pagina)) {

                dibujarFondo(contenido);

                dibujarEncabezado(
                        documento,
                        contenido
                );

                dibujarInformacionFactura(
                        contenido,
                        factura
                );

                float siguienteY =
                        dibujarTablaProductos(
                                contenido,
                                factura
                        );

                dibujarResumen(
                        contenido,
                        factura,
                        siguienteY
                );

                dibujarPie(
                        contenido
                );
            }

            documento.save(archivo);
        }

        return archivo;
    }


    // ==========================================================
    // VALIDAR FACTURA
    // ==========================================================

    private void validarFactura(
            Factura factura) {

        if (factura == null) {

            throw new IllegalArgumentException(
                    "La factura no puede ser null."
            );
        }

        if (factura.getDetalles() == null ||
                factura.getDetalles().isEmpty()) {

            throw new IllegalArgumentException(
                    "La factura debe contener al menos "
                    + "un producto."
            );
        }

        if (factura.getCliente() == null) {

            throw new IllegalArgumentException(
                    "La factura debe tener un cliente."
            );
        }
    }


    // ==========================================================
    // FONDO
    // ==========================================================

    private void dibujarFondo(
            PDPageContentStream contenido)
            throws IOException {

        contenido.setNonStrokingColor(FONDO);

        contenido.addRect(
                0,
                0,
                ANCHO_PAGINA,
                ALTO_PAGINA
        );

        contenido.fill();


        // Franja superior amarilla.

        contenido.setNonStrokingColor(
                AMARILLO
        );

        contenido.addRect(
                0,
                ALTO_PAGINA - 8,
                ANCHO_PAGINA,
                8
        );

        contenido.fill();


        // Franja inferior roja.

        contenido.setNonStrokingColor(
                ROJO
        );

        contenido.addRect(
                0,
                0,
                ANCHO_PAGINA,
                8
        );

        contenido.fill();
    }


    // ==========================================================
    // ENCABEZADO
    // ==========================================================

    private void dibujarEncabezado(
            PDDocument documento,
            PDPageContentStream contenido)
            throws IOException {

        float y =
                ALTO_PAGINA - 45;


        // ------------------------------------------------------
        // LOGO
        // ------------------------------------------------------

        try (InputStream entrada =
                     getClass().getResourceAsStream(
                             RUTA_LOGO)) {

            if (entrada != null) {

                byte[] datos =
                        entrada.readAllBytes();

                PDImageXObject logo =
                        PDImageXObject.createFromByteArray(
                                documento,
                                datos,
                                "logo-freddy"
                        );

                contenido.drawImage(
                        logo,
                        MARGEN,
                        y - 85,
                        80,
                        80
                );
            }
        }


        // ------------------------------------------------------
        // NOMBRE
        // ------------------------------------------------------

        escribirTexto(
                contenido,
                NOMBRE_NEGOCIO,
                135,
                y - 20,
                20,
                TEXTO,
                true
        );

        escribirTexto(
                contenido,
                NOMBRE_COMERCIAL,
                135,
                y - 48,
                24,
                ROJO,
                true
        );

        escribirTexto(
                contenido,
                SLOGAN,
                137,
                y - 68,
                9,
                VERDE,
                false
        );


        // ------------------------------------------------------
        // TARJETA FACTURA
        // ------------------------------------------------------

        float xFactura = 420;
        float anchoFactura = 145;
        float altoFactura = 90;

        float yFactura = y - 5;


        contenido.setNonStrokingColor(
                TARJETA
        );

        contenido.addRect(
                xFactura,
                yFactura - altoFactura,
                anchoFactura,
                altoFactura
        );

        contenido.fill();


        contenido.setStrokingColor(
                ROJO
        );

        contenido.setLineWidth(1.5f);

        contenido.addRect(
                xFactura,
                yFactura - altoFactura,
                anchoFactura,
                altoFactura
        );

        contenido.stroke();


        // Cabecera roja.

        contenido.setNonStrokingColor(
                ROJO
        );

        contenido.addRect(
                xFactura,
                yFactura - 27,
                anchoFactura,
                27
        );

        contenido.fill();


        escribirTexto(
                contenido,
                "FACTURA",
                xFactura + 35,
                yFactura - 18,
                12,
                Color.WHITE,
                true
        );
    }


    // ==========================================================
    // INFORMACIÓN DE FACTURA
    // ==========================================================

    private void dibujarInformacionFactura(
            PDPageContentStream contenido,
            Factura factura)
            throws IOException {

        float y =
                ALTO_PAGINA - 170;


        // ======================================================
        // TARJETA CLIENTE
        // ======================================================

        float alto = 90;

        contenido.setNonStrokingColor(
                TARJETA
        );

        contenido.addRect(
                MARGEN,
                y - alto,
                ANCHO_PAGINA - (MARGEN * 2),
                alto
        );

        contenido.fill();


        contenido.setStrokingColor(
                AMARILLO
        );

        contenido.setLineWidth(1.2f);

        contenido.addRect(
                MARGEN,
                y - alto,
                ANCHO_PAGINA - (MARGEN * 2),
                alto
        );

        contenido.stroke();


        // ======================================================
        // CLIENTE
        // ======================================================

        escribirTexto(
                contenido,
                "CLIENTE",
                MARGEN + 18,
                y - 25,
                10,
                ROJO,
                true
        );


        escribirTexto(
                contenido,
                factura.getNombreCliente(),
                MARGEN + 18,
                y - 46,
                14,
                TEXTO,
                true
        );


        if (factura.getNit() != null &&
                !factura.getNit().trim().isEmpty()) {

            escribirTexto(
                    contenido,
                    "NIT: " + factura.getNit(),
                    MARGEN + 18,
                    y - 65,
                    9,
                    GRIS,
                    false
            );
        }


        // ======================================================
        // FECHA / FACTURA
        // ======================================================

        float xDerecha = 350;


        escribirTexto(
                contenido,
                "N.o FACTURA",
                xDerecha,
                y - 25,
                9,
                ROJO,
                true
        );


        escribirTexto(
                contenido,
                factura.getNumeroFactura(),
                xDerecha,
                y - 43,
                11,
                TEXTO,
                true
        );


        escribirTexto(
                contenido,
                "FECHA",
                xDerecha,
                y - 62,
                9,
                ROJO,
                true
        );


        escribirTexto(
                contenido,
                factura.getFechaFormateada(),
                xDerecha,
                y - 79,
                9,
                TEXTO,
                false
        );


        // Dirección.

        if (factura.getDireccion() != null &&
                !factura.getDireccion().trim().isEmpty()) {

            escribirTexto(
                    contenido,
                    "Direccion: "
                    + factura.getDireccion(),
                    MARGEN + 250,
                    y - 65,
                    8,
                    GRIS,
                    false
            );
        }
    }


    // ==========================================================
    // TABLA DE PRODUCTOS
    // ==========================================================

    private float dibujarTablaProductos(
            PDPageContentStream contenido,
            Factura factura)
            throws IOException {

        float inicioY =
                ALTO_PAGINA - 285;

        float x =
                MARGEN;

        float ancho =
                ANCHO_PAGINA - (MARGEN * 2);

        float altoCabecera = 28;

        float altoFila = 34;


        // ======================================================
        // CABECERA
        // ======================================================

        contenido.setNonStrokingColor(
                TEXTO
        );

        contenido.addRect(
                x,
                inicioY,
                ancho,
                altoCabecera
        );

        contenido.fill();


        escribirTexto(
                contenido,
                "CANT.",
                x + 10,
                inicioY + 9,
                9,
                Color.WHITE,
                true
        );

        escribirTexto(
                contenido,
                "ARTICULO",
                x + 70,
                inicioY + 9,
                9,
                Color.WHITE,
                true
        );

        escribirTexto(
                contenido,
                "PRECIO UNITARIO",
                x + 300,
                inicioY + 9,
                8,
                Color.WHITE,
                true
        );

        escribirTexto(
                contenido,
                "TOTAL",
                x + 445,
                inicioY + 9,
                9,
                Color.WHITE,
                true
        );


        // ======================================================
        // DETALLES
        // ======================================================

        float y =
                inicioY - altoFila;

        List<DetalleFactura> detalles =
                factura.getDetalles();


        for (DetalleFactura detalle :
                detalles) {

            // Fondo de fila.

            contenido.setNonStrokingColor(
                    TARJETA
            );

            contenido.addRect(
                    x,
                    y,
                    ancho,
                    altoFila
            );

            contenido.fill();


            // Bordes.

            contenido.setStrokingColor(
                    AMARILLO
            );

            contenido.setLineWidth(
                    0.5f
            );

            contenido.addRect(
                    x,
                    y,
                    ancho,
                    altoFila
            );

            contenido.stroke();


            // --------------------------------------------------
            // CANTIDAD
            // --------------------------------------------------

            escribirTexto(
                    contenido,
                    detalle.getCantidad() + "x",
                    x + 12,
                    y + 12,
                    10,
                    TEXTO,
                    true
            );


            // --------------------------------------------------
            // PRODUCTO
            // --------------------------------------------------

            String nombre =
                    detalle.getNombreProducto();

            if (nombre == null) {
                nombre = "Producto";
            }

            escribirTexto(
                    contenido,
                    limitarTexto(nombre, 34),
                    x + 70,
                    y + 12,
                    9,
                    TEXTO,
                    false
            );


            // --------------------------------------------------
            // PRECIO UNITARIO
            // --------------------------------------------------

            escribirTexto(
                    contenido,
                    formatoMoneda(
                            detalle.getPrecioUnitario()
                    ),
                    x + 320,
                    y + 12,
                    9,
                    TEXTO,
                    false
            );


            // --------------------------------------------------
            // TOTAL
            // --------------------------------------------------

            escribirTexto(
                    contenido,
                    formatoMoneda(
                            detalle.getSubtotal()
                    ),
                    x + 450,
                    y + 12,
                    9,
                    TEXTO,
                    true
            );


            y -= altoFila;
        }


        return y;
    }


    // ==========================================================
    // RESUMEN DE PAGO
    // ==========================================================

    private void dibujarResumen(
            PDPageContentStream contenido,
            Factura factura,
            float y)
            throws IOException {

        y -= 15;


        // ======================================================
        // MÉTODO DE PAGO
        // ======================================================

        float xIzquierda =
                MARGEN;

        escribirTexto(
                contenido,
                "DETALLES DE PAGO",
                xIzquierda,
                y,
                12,
                ROJO,
                true
        );


        String metodo =
                factura.getMetodoPago() != null
                        ? factura.getMetodoPago().toString()
                        : "No especificado";


        escribirTexto(
                contenido,
                "Metodo: " + metodo,
                xIzquierda,
                y - 23,
                10,
                TEXTO,
                false
        );


        escribirTexto(
                contenido,
                "Estado: PAGADO",
                xIzquierda,
                y - 42,
                10,
                VERDE,
                true
        );


        // ======================================================
        // RESUMEN MONETARIO
        // ======================================================

        float xDerecha =
                340;

        float ancho =
                215;


        escribirTexto(
                contenido,
                "Subtotal:",
                xDerecha,
                y,
                10,
                TEXTO,
                false
        );

        escribirTextoDerecha(
                contenido,
                formatoMoneda(
                        factura.getSubtotal()
                ),
                xDerecha + ancho,
                y,
                10,
                TEXTO,
                false
        );


        // Descuento.

        BigDecimal descuento =
                factura.getDescuento();

        if (descuento != null &&
                descuento.compareTo(
                        BigDecimal.ZERO
                ) > 0) {

            escribirTexto(
                    contenido,
                    "Descuento:",
                    xDerecha,
                    y - 20,
                    10,
                    TEXTO,
                    false
            );

            escribirTextoDerecha(
                    contenido,
                    "- "
                    + formatoMoneda(descuento),
                    xDerecha + ancho,
                    y - 20,
                    10,
                    VERDE,
                    false
            );
        }


        // IVA.

        escribirTexto(
                contenido,
                "IVA (12%):",
                xDerecha,
                y - 40,
                10,
                TEXTO,
                false
        );

        escribirTextoDerecha(
                contenido,
                formatoMoneda(
                        factura.getIva()
                ),
                xDerecha + ancho,
                y - 40,
                10,
                TEXTO,
                false
        );


        // ======================================================
        // TOTAL
        // ======================================================

        float yTotal =
                y - 75;


        contenido.setNonStrokingColor(
                ROJO
        );

        contenido.addRect(
                xDerecha - 8,
                yTotal,
                ancho + 8,
                35
        );

        contenido.fill();


        escribirTexto(
                contenido,
                "TOTAL",
                xDerecha + 8,
                yTotal + 11,
                12,
                Color.WHITE,
                true
        );


        escribirTextoDerecha(
                contenido,
                formatoMoneda(
                        factura.getTotal()
                ),
                xDerecha + ancho - 5,
                yTotal + 10,
                13,
                Color.WHITE,
                true
        );
    }


    // ==========================================================
    // PIE DE FACTURA
    // ==========================================================

    private void dibujarPie(
            PDPageContentStream contenido)
            throws IOException {

        float y = 75;


        // Línea.

        contenido.setStrokingColor(
                AMARILLO
        );

        contenido.setLineWidth(2);

        contenido.moveTo(
                MARGEN,
                y + 25
        );

        contenido.lineTo(
                ANCHO_PAGINA - MARGEN,
                y + 25
        );

        contenido.stroke();


        // Gracias.

        escribirTextoCentrado(
                contenido,
                "GRACIAS POR TU VISITA!",
                ANCHO_PAGINA / 2,
                y,
                16,
                ROJO,
                true
        );


        escribirTextoCentrado(
                contenido,
                SLOGAN,
                ANCHO_PAGINA / 2,
                y - 18,
                10,
                VERDE,
                false
        );


        escribirTextoCentrado(
                contenido,
                CORREO_NEGOCIO
                + "  |  "
                + DIRECCION_NEGOCIO,
                ANCHO_PAGINA / 2,
                y - 36,
                8,
                GRIS,
                false
        );
    }


    // ==========================================================
    // MÉTODO PARA ESCRIBIR TEXTO
    // ==========================================================

    private void escribirTexto(
            PDPageContentStream contenido,
            String texto,
            float x,
            float y,
            float tamaño,
            Color color,
            boolean negrita)
            throws IOException {

        if (texto == null) {
            texto = "";
        }

        PDType1Font fuente =
                new PDType1Font(
                        negrita
                                ? Standard14Fonts.FontName.HELVETICA_BOLD
                                : Standard14Fonts.FontName.HELVETICA
                );


        contenido.beginText();

        contenido.setFont(
                fuente,
                tamaño
        );

        contenido.setNonStrokingColor(
                color
        );

        contenido.newLineAtOffset(
                x,
                y
        );

        contenido.showText(
                limpiarTexto(texto)
        );

        contenido.endText();
    }


    // ==========================================================
    // TEXTO ALINEADO A LA DERECHA
    // ==========================================================

    private void escribirTextoDerecha(
            PDPageContentStream contenido,
            String texto,
            float x,
            float y,
            float tamaño,
            Color color,
            boolean negrita)
            throws IOException {

        if (texto == null) {
            texto = "";
        }


        PDType1Font fuente =
                new PDType1Font(
                        negrita
                                ? Standard14Fonts.FontName.HELVETICA_BOLD
                                : Standard14Fonts.FontName.HELVETICA
                );


        float anchoTexto =
                fuente.getStringWidth(
                        limpiarTexto(texto)
                ) / 1000 * tamaño;


        escribirTexto(
                contenido,
                texto,
                x - anchoTexto,
                y,
                tamaño,
                color,
                negrita
        );
    }


    // ==========================================================
    // TEXTO CENTRADO
    // ==========================================================

    private void escribirTextoCentrado(
            PDPageContentStream contenido,
            String texto,
            float centroX,
            float y,
            float tamaño,
            Color color,
            boolean negrita)
            throws IOException {

        PDType1Font fuente =
                new PDType1Font(
                        negrita
                                ? Standard14Fonts.FontName.HELVETICA_BOLD
                                : Standard14Fonts.FontName.HELVETICA
                );


        String textoLimpio =
                limpiarTexto(texto);


        float anchoTexto =
                fuente.getStringWidth(
                        textoLimpio
                ) / 1000 * tamaño;


        escribirTexto(
                contenido,
                textoLimpio,
                centroX - (anchoTexto / 2),
                y,
                tamaño,
                color,
                negrita
        );
    }


    // ==========================================================
    // FORMATO DE MONEDA
    // ==========================================================

    private String formatoMoneda(
            BigDecimal valor) {

        if (valor == null) {
            valor = BigDecimal.ZERO;
        }

        return "Q"
                + valor.setScale(
                        2,
                        RoundingMode.HALF_UP
                ).toPlainString();
    }


    // ==========================================================
    // LIMITAR TEXTO
    // ==========================================================

    private String limitarTexto(
            String texto,
            int maximo) {

        if (texto == null) {
            return "";
        }

        if (texto.length() <= maximo) {
            return texto;
        }

        return texto.substring(
                0,
                maximo - 3
        ) + "...";
    }


    // ==========================================================
    // LIMPIAR TEXTO PARA PDFBOX
    // ==========================================================

    private String limpiarTexto(
            String texto) {

        if (texto == null) {
            return "";
        }

        /*
         * Las fuentes Standard 14 de PDFBox no soportan
         * todos los caracteres Unicode.
         *
         * Se sustituyen algunos caracteres problemáticos.
         */

        return texto
                .replace("’", "'")
                .replace("“", "\"")
                .replace("”", "\"")
                .replace("–", "-")
                .replace("—", "-")
                .replace("•", "-");
    }


    // ==========================================================
    // ENVIAR POR CORREO
    // ==========================================================

    /**
     * Envía una factura PDF mediante Gmail.
     *
     * IMPORTANTE:
     * No usar la contraseña normal de Gmail.
     * Utilizar una contraseña de aplicación.
     *
     * @param pdfFactura archivo PDF
     * @param correoDestino correo del cliente
     */
    public void enviarPorCorreo(
            File pdfFactura,
            String correoDestino)
            throws Exception {

        if (pdfFactura == null ||
                !pdfFactura.exists()) {

            throw new IllegalArgumentException(
                    "El archivo PDF no existe."
            );
        }

        if (correoDestino == null ||
                correoDestino.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El correo destino es obligatorio."
            );
        }


        // ======================================================
        // CONFIGURACIÓN
        // ======================================================

        String correoEmisor =
                System.getenv(
                        "FREDDY_EMAIL"
                );

        String contraseña =
                System.getenv(
                        "FREDDY_EMAIL_PASSWORD"
                );


        if (correoEmisor == null ||
                correoEmisor.trim().isEmpty()) {

            throw new IllegalStateException(
                    "No se configuró FREDDY_EMAIL."
            );
        }


        if (contraseña == null ||
                contraseña.trim().isEmpty()) {

            throw new IllegalStateException(
                    "No se configuró "
                    + "FREDDY_EMAIL_PASSWORD."
            );
        }


        Properties propiedades =
                new Properties();

        propiedades.put(
                "mail.smtp.auth",
                "true"
        );

        propiedades.put(
                "mail.smtp.starttls.enable",
                "true"
        );

        propiedades.put(
                "mail.smtp.host",
                "smtp.gmail.com"
        );

        propiedades.put(
                "mail.smtp.port",
                "587"
        );


        // ======================================================
        // SESIÓN
        // ======================================================

        Session sesion =
                Session.getInstance(
                        propiedades,
                        new Authenticator() {

                            @Override
                            protected PasswordAuthentication
                            getPasswordAuthentication() {

                                return new PasswordAuthentication(
                                        correoEmisor,
                                        contraseña
                                );
                            }
                        }
                );


        // ======================================================
        // MENSAJE
        // ======================================================

        Message mensaje =
                new MimeMessage(sesion);


        mensaje.setFrom(
                new InternetAddress(
                        correoEmisor,
                        "Freddy Fazbear's Quick Bite"
                )
        );


        mensaje.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(
                        correoDestino
                )
        );


        mensaje.setSubject(
                "Factura - Freddy Fazbear's Quick Bite"
        );


        // ======================================================
        // CUERPO
        // ======================================================

        MimeBodyPart cuerpo =
                new MimeBodyPart();


        cuerpo.setText(
                "Hola!\n\n"
                + "Gracias por comprar en "
                + "Freddy Fazbear's Quick Bite.\n\n"
                + "Adjuntamos tu factura en formato PDF.\n\n"
                + "Gracias por tu visita!\n"
                + "Donde la magia cobra vida!",
                "UTF-8"
        );


        // ======================================================
        // ADJUNTO
        // ======================================================

        MimeBodyPart adjunto =
                new MimeBodyPart();


        adjunto.attachFile(
                pdfFactura
        );


        // ======================================================
        // MULTIPART
        // ======================================================

        Multipart contenido =
                new MimeMultipart();


        contenido.addBodyPart(
                cuerpo
        );

        contenido.addBodyPart(
                adjunto
        );


        mensaje.setContent(
                contenido
        );


        // ======================================================
        // ENVIAR
        // ======================================================

        Transport.send(
                mensaje
        );
    }


    // ==========================================================
    // GENERAR Y ENVIAR
    // ==========================================================

    /**
     * Genera la factura PDF y posteriormente
     * la envía al correo del cliente.
     *
     * @param factura factura que se procesará
     * @param correoDestino correo del cliente
     * @return archivo PDF generado
     */
    public File generarYEnviar(
            Factura factura,
            String correoDestino)
            throws Exception {

        File pdf =
                generarPdf(factura);

        enviarPorCorreo(
                pdf,
                correoDestino
        );

        return pdf;
    }
}
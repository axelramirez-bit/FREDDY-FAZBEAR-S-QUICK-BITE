package Service.Implement;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Properties;

public class FacturaService {

    // ==========================================================
    // 1. GENERAR EL PDF CON PDFBox
    // ==========================================================
    public File generarPdf(Model.Factura factura) throws Exception {

        PDDocument documento = new PDDocument();
        PDPage pagina = new PDPage();
        documento.addPage(pagina);

        try (PDPageContentStream contenido =
                new PDPageContentStream(documento, pagina)) {

            contenido.beginText();
            contenido.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16);
            contenido.newLineAtOffset(50, 750);
            contenido.showText("Freddy Fazbear's Quick Bite");
            contenido.newLineAtOffset(0, -30);
            contenido.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            contenido.showText("Factura N.o " + factura.getNumeroFactura());
            contenido.newLineAtOffset(0, -20);
            contenido.showText("Total: Q" + factura.getTotal());
            contenido.endText();
        }

        File archivo = new File("Factura_" + factura.getNumeroFactura() + ".pdf");
        documento.save(archivo);
        documento.close();

        return archivo; // este File es lo que le pasamos a JavaMail
    }

    // ==========================================================
    // 2. ENVIAR ESE PDF POR CORREO CON JavaMail
    // ==========================================================
    public void enviarPorCorreo(File pdfFactura, String correoDestino) throws Exception {

        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true");
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.put("mail.smtp.port", "587");

        Session sesionCorreo = Session.getInstance(propiedades, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        "tu_correo@gmail.com",
                        "tu_contraseña_de_aplicacion" // nunca la contraseña normal de Gmail
                );
            }
        });

        Message mensaje = new MimeMessage(sesionCorreo);
        mensaje.setFrom(new InternetAddress("tu_correo@gmail.com"));
        mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoDestino));
        mensaje.setSubject("Tu factura - Freddy Fazbear's Quick Bite");

        MimeBodyPart cuerpo = new MimeBodyPart();
        cuerpo.setText("Adjunto encontrarás tu factura. ¡Gracias por tu compra!");

        MimeBodyPart adjunto = new MimeBodyPart();
        adjunto.attachFile(pdfFactura); // aquí se conecta con lo que generó PDFBox

        Multipart multiparte = new MimeMultipart();
        multiparte.addBodyPart(cuerpo);
        multiparte.addBodyPart(adjunto);

        mensaje.setContent(multiparte);

        Transport.send(mensaje);
    }
}
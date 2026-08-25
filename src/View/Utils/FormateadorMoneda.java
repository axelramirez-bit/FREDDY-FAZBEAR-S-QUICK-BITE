package View.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Formatea montos en quetzales de forma consistente en toda la
 * aplicación ("Q98.56"). Antes cada panel armaba el String a mano
 * ("Q" + valor), lo que redondeaba distinto según quién lo
 * escribiera. Esta clase es el único punto de formateo.
 * ===============================================================
 */
public final class FormateadorMoneda {

    private FormateadorMoneda() {
    }

    public static String formatear(BigDecimal valor) {

        BigDecimal seguro = valor != null ? valor : BigDecimal.ZERO;

        return "Q" + seguro.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}

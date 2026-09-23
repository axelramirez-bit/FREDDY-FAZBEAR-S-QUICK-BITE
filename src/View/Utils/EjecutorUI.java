package View.Utils;

import Config.ConexionException;
import Excepciones.AccesoDatosException;
import Excepciones.AppException;
import Excepciones.ReglaNegocioException;
import Excepciones.ValidacionException;
import Utils.AppLogger;

import javax.swing.SwingWorker;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Window;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.SwingUtilities;

/**
 * Ejecuta acciones de la interfaz mostrando el diálogo correcto según el
 * tipo de error, y permite correr consultas pesadas fuera del hilo de
 * Swing (EDT) para que la ventana no se congele.
 *
 * Ejemplos:
 *
 *   // 1) Acción rápida con manejo de errores (síncrona)
 *   EjecutorUI.ejecutar(this, () -> usuarioService.registrarUsuario(u));
 *
 *   // 2) Consulta pesada en segundo plano
 *   EjecutorUI.enSegundoPlano(this,
 *           () -> pedidoService.listarPedidos(),
 *           pedidos -> cargarTabla(pedidos));
 */
public final class EjecutorUI {

    private EjecutorUI() {
    }

    /**
     * Ejecuta la acción. Si lanza una excepción, muestra el diálogo
     * adecuado y devuelve null.
     */
    public static <T> T ejecutar(Component padre, Supplier<T> accion) {
        try {
            return accion.get();
        } catch (Throwable ex) {
            mostrarError(padre, ex);
            return null;
        }
    }

    /**
     * Igual que ejecutar(), para acciones sin resultado.
     * Devuelve true si terminó sin errores.
     */
    public static boolean ejecutar(Component padre, Runnable accion) {
        try {
            accion.run();
            return true;
        } catch (Throwable ex) {
            mostrarError(padre, ex);
            return false;
        }
    }

    /**
     * Corre la tarea en un hilo aparte y entrega el resultado en el EDT.
     * Mientras tanto muestra el cursor de espera. Si falla, muestra el
     * diálogo de error correspondiente.
     */
    public static <T> void enSegundoPlano(Component padre, Supplier<T> tarea, Consumer<T> alTerminar) {

        Window ventana = padre == null ? null : SwingUtilities.getWindowAncestor(padre);

        if (ventana != null) {
            ventana.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }

        new SwingWorker<T, Void>() {

            @Override
            protected T doInBackground() {
                // Aquí NO se toca ningún componente Swing
                return tarea.get();
            }

            @Override
            protected void done() {

                if (ventana != null) {
                    ventana.setCursor(Cursor.getDefaultCursor());
                }

                try {
                    alTerminar.accept(get());

                } catch (InterruptedException ex) {
                    // Se restaura la marca de interrupción y no se muestra nada
                    Thread.currentThread().interrupt();

                } catch (ExecutionException ex) {
                    mostrarError(padre, ex.getCause() == null ? ex : ex.getCause());

                } catch (Throwable ex) {
                    // Error dentro de alTerminar (código de la propia vista)
                    mostrarError(padre, ex);
                }
            }

        }.execute();
    }

    /**
     * Elige el diálogo según el tipo de excepción.
     */
    public static void mostrarError(Component padre, Throwable ex) {

        if (ex instanceof ValidacionException || ex instanceof ReglaNegocioException) {
            // El usuario puede corregirlo: advertencia con la razón concreta
            FabricaDialogos.advertencia(padre, ((AppException) ex).getMensajeUsuario());

        } else if (ex instanceof AccesoDatosException) {
            // Falla técnica ya registrada en el log por el traductor
            FabricaDialogos.error(padre, ((AppException) ex).getMensajeUsuario());

        } else if (ex instanceof AppException) {
            FabricaDialogos.error(padre, ((AppException) ex).getMensajeUsuario());

        } else if (ex instanceof ConexionException) {
            AppLogger.error(EjecutorUI.class, "Se perdió la conexión con la base de datos.", ex);
            FabricaDialogos.error(padre,
                    "No se pudo conectar con la base de datos.\n"
                    + "Verifica que el servicio de MySQL esté encendido e inténtalo de nuevo.");

        } else {
            // Error inesperado (bug): al log con stacktrace y mensaje genérico
            AppLogger.error(EjecutorUI.class, "Error inesperado en la interfaz.", ex);
            FabricaDialogos.error(padre,
                    "Ocurrió un error inesperado.\nSi el problema continúa, avisa al equipo.");
        }
    }
}

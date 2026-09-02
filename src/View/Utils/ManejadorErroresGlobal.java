package View.Utils;

import Config.ConexionException;
import Utils.AppLogger;

import java.awt.AWTEvent;
import java.awt.EventQueue;

/**
 * ===============================================================
 * FREDDY-FAZBEAR'S QUICK BITE
 * ---------------------------------------------------------------
 * Manejador CENTRAL de excepciones para toda la interfaz.
 *
 * Problema que resuelve:
 * Cuando una excepción (por ejemplo ConexionException, si MySQL se
 * cae mientras la app ya está abierta) se lanza dentro del
 * ActionListener de un botón, Swing la atrapa internamente en su
 * propio EventDispatchThread y solo la imprime en consola. El botón
 * "no hace nada" y el usuario nunca se entera de qué pasó.
 *
 * Solución: sustituir la cola de eventos por defecto de Swing por
 * esta, que envuelve dispatchEvent() en un try/catch. Así cualquier
 * excepción que se escape de CUALQUIER botón/acción de CUALQUIER
 * panel del proyecto pasa por aquí, sin tener que agregar un
 * try/catch nuevo en cada uno de los paneles existentes.
 *
 * Se instala UNA sola vez, en Main/main.java, antes de abrir
 * cualquier ventana:
 *
 *   Toolkit.getDefaultToolkit().getSystemEventQueue()
 *          .push(new ManejadorErroresGlobal());
 *
 * IMPORTANTE — esto NO reemplaza los try/catch de SQLException que
 * ya existen en los DAO. Esos deben seguir devolviendo false/null de
 * forma controlada hacia la capa Service (ver AppLogger). Este
 * manejador es la red de seguridad para lo que se escape SIN
 * capturar — sobre todo ConexionException, que al ser un
 * RuntimeException puede subir desde cualquier DAO que llame
 * Conexion.getInstancia().getConexion() fuera de un try/catch.
 *
 * Tampoco cubre el primer intento de conexión al arrancar la app
 * (eso ocurre en un SwingWorker en segundo plano, no en el EDT, así
 * que dispatchEvent() nunca lo ve) — ese caso se maneja aparte en
 * View.Splash.SplashScreen.
 * ===============================================================
 */
public class ManejadorErroresGlobal extends EventQueue {

    @Override
    protected void dispatchEvent(AWTEvent evento) {

        try {

            super.dispatchEvent(evento);

        } catch (ConexionException ex) {

            AppLogger.error(ManejadorErroresGlobal.class,
                    "Se perdió la conexión con la base de datos.", ex);

            FabricaDialogos.error(null,
                    "No se pudo conectar con la base de datos.\n"
                            + "Verifica que el servicio de MySQL esté "
                            + "encendido e inténtalo de nuevo.");

        } catch (Throwable ex) {

            // Red de seguridad final: cualquier otra excepción que
            // se haya escapado sin capturar en algún panel. Mejor
            // mostrar un mensaje genérico que dejar el botón
            // "muerto" sin ninguna explicación.
            AppLogger.error(ManejadorErroresGlobal.class,
                    "Error inesperado no capturado en la interfaz.", ex);

            FabricaDialogos.error(null,
                    "Ocurrió un error inesperado.\n"
                            + "Si el problema continúa, avisa al equipo.");
        }
    }
}

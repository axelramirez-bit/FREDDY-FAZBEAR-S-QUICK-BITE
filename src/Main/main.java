
// Paquete Main: punto de entrada de la aplicación
package Main;

// Importa la conexión a MySQL (singleton)
import Config.Conexion;
// Importa el lector de config.properties
import Config.Configuracion;
// Importa el logger de la aplicación
import Utils.AppLogger;
// Importa la ventana de Login
import View.Login.Login;
// Importa la pantalla de carga
import View.Splash.SplashScreen;
// Importa el gestor del tema visual
import View.Utils.AdministradorTema;
// Importa el manejador global de errores
import View.Utils.ManejadorErroresGlobal;
// Importa Toolkit para acceder a la cola de eventos de AWT
import java.awt.Toolkit;
// Importa SwingUtilities para ejecutar código en el hilo de Swing
import javax.swing.SwingUtilities;


// Clase principal que arranca la aplicación
public class main {


    // Método main: primer método que ejecuta Java
    public static void main(String[] args) {

        // Manejador central de excepciones: debe instalarse ANTES de
        // crear cualquier ventana, para que cubra también los
        // errores que ocurran en la pantalla de carga y en Login.
        // Ver View.Utils.ManejadorErroresGlobal para el detalle de
        // qué problema resuelve.
        // Obtiene el Toolkit del sistema
        Toolkit.getDefaultToolkit()
                // Accede a la cola de eventos de AWT
                .getSystemEventQueue()
                // Instala el manejador global de excepciones en esa cola
                .push(new ManejadorErroresGlobal());

        // Ejecuta el bloque en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {

            // El tema se aplica antes de crear cualquier ventana,
            // incluida la pantalla de carga.
            // Inicializa el tema visual antes de crear ventanas
            AdministradorTema.inicializar();

            // Crea la pantalla de carga
            SplashScreen splash = new SplashScreen();

            // Inicia la carga con dos acciones: tareas y acción final
            splash.iniciarCarga(
                    // ── Tareas reales de inicialización ───────
                    // Se ejecutan en segundo plano mientras se
                    // anima la pantalla de carga, para no
                    // congelar la animación.
                    // Acción 1: tareas de inicialización en segundo plano
                    () -> {
                        // Corrección: antes se imprimían usuario y
                        // contraseña de la base de datos en consola
                        // (System.out.println(Configuracion.getPassword())).
                        // Nunca se debe loguear una credencial, ni
                        // siquiera en desarrollo.
                        // Registra en el log a qué URL se conecta (sin credenciales)
                        AppLogger.info(main.class,
                                // URL de conexión armada por Configuracion
                                "Conectando a " + Configuracion.getUrl());

                        // Obtiene (o crea) la instancia única de Conexion
                        Conexion conexion = Conexion.getInstancia();

                        // Verifica si la conexión está activa
                        if (conexion.estaConectado()) {
                            // Si está activa, registra éxito en el log
                            AppLogger.info(main.class,
                                    // Mensaje de éxito
                                    "La conexión funciona correctamente.");
                        // Si no está activa, ejecuta el else
                        } else {
                            // Registra un aviso en el log
                            AppLogger.aviso(main.class,
                                    // Mensaje de aviso por fallo de conexión
                                    "No se pudo verificar la conexión a la base de datos.");
                        }

                        // Corrección: aquí antes se llamaba a
                        // conexion.cerrarConexion() justo después de
                        // conectar, lo cual cerraba la conexión real
                        // apenas arrancaba la app (el primer DAO que
                        // se usara tenía que reconectar desde cero).
                        // El cierre real de la conexión debe ocurrir
                        // solo al cerrar la aplicación: eso ya lo hace
                        // el shutdown hook de más abajo.
                    },
                    // ── Al terminar la carga ──────────────────
                    // Se ejecuta en el hilo de Swing: abre la
                    // ventana de Bienvenida.
                    // Acción 2: al terminar la carga, abre la ventana de Login
                    () -> new Login().setVisible(true)
            );
        });

        // Obtiene el runtime de la JVM
        Runtime.getRuntime()
                // Registra un hook que corre al cerrar la aplicación
                .addShutdownHook(
                        // Crea el hilo que ejecutará el cierre
                        new Thread(() -> {

                            // Obtiene la instancia de conexión
                            Conexion.getInstancia()
                                    // Cierra la conexión real a MySQL
                                    .cerrarConexion();

                        })
                );
    }
}

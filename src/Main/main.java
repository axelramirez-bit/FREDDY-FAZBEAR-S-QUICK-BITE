
package Main;

import Config.Conexion;
import Config.Configuracion;
import Utils.AppLogger;
import View.Bienvenida.Bienvenida;
import View.Splash.SplashScreen;
import View.Utils.AdministradorTema;
import javax.swing.SwingUtilities;


public class main {


    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            // El tema se aplica antes de crear cualquier ventana,
            // incluida la pantalla de carga.
            AdministradorTema.inicializar();

            SplashScreen splash = new SplashScreen();

            splash.iniciarCarga(
                    // ── Tareas reales de inicialización ───────
                    // Se ejecutan en segundo plano mientras se
                    // anima la pantalla de carga, para no
                    // congelar la animación.
                    () -> {
                        // Corrección: antes se imprimían usuario y
                        // contraseña de la base de datos en consola
                        // (System.out.println(Configuracion.getPassword())).
                        // Nunca se debe loguear una credencial, ni
                        // siquiera en desarrollo.
                        AppLogger.info(main.class,
                                "Conectando a " + Configuracion.getUrl());

                        Conexion conexion = Conexion.getInstancia();

                        if (conexion.estaConectado()) {
                            AppLogger.info(main.class,
                                    "La conexión funciona correctamente.");
                        } else {
                            AppLogger.aviso(main.class,
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
                    () -> new Bienvenida().setVisible(true)
            );
        });

        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread(() -> {

                            Conexion.getInstancia()
                                    .cerrarConexion();

                        })
                );
    }
}

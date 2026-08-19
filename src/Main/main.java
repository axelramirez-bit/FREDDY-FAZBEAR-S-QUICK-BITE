
package Main;

import Config.Conexion;
import Config.Configuracion;
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
                        System.out.println(
                                Configuracion.getUsuario()
                        );

                        System.out.println(
                                Configuracion.getPassword()
                        );

                        System.out.println(
                                Configuracion.getUrl()
                        );

                        Conexion conexion = Conexion.getInstancia();

                        if (conexion.estaConectado()) {

                            System.out.println("La conexión funciona correctamente.");

                        }

                        conexion.cerrarConexion();
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


package Main;

import Config.Conexion;
import Config.Configuracion;
import View.Bienvenida.Bienvenida;
import View.Utils.AdministradorTema;


public class main {


    public static void main(String[] args) {
        AdministradorTema.inicializar();
        new Bienvenida().setVisible(true);
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
        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread(() -> {

                            Conexion.getInstancia()
                                    .cerrarConexion();

                        })
                );
    }
}

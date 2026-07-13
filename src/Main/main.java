
package Main;

import Config.Conexion;
import Config.Configuracion;


public class main {


    public static void main(String[] args) {
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

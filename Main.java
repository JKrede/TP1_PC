import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        List<Pedido> listaPedidos = new ArrayList<>();
        int contadorLog = 0;
        Log log = new Log();

        Sistema sistema = new Sistema(log);

        for (int i = 0; i < 500; i++) {
            Pedido pedido = new Pedido(i);
            listaPedidos.add(pedido);
        }

        PreparadorDePedidos procesodePrepararDePedidos = new PreparadorDePedidos(sistema, listaPedidos);
        DespachadorDePedidos despachadorDePedidos = new DespachadorDePedidos(sistema);
        EntregadorDePedido entregadorDePedidos = new EntregadorDePedido(sistema);
        VerificadorDePedido verificadorDePedidos = new VerificadorDePedido(sistema);

        Thread preparadorDePedidos1 = new Thread(procesodePrepararDePedidos);
        Thread preparadorDePedidos2 = new Thread(procesodePrepararDePedidos);
        Thread preparadorDePedidos3 = new Thread(procesodePrepararDePedidos);

        Thread despachadorDePedidos1 = new Thread(despachadorDePedidos); //Hilo 4
        Thread despachadorDePedidos2 = new Thread(despachadorDePedidos); //Hilo 5

        Thread entregadorDePedidos1 = new Thread(entregadorDePedidos); //Hilo 6
        Thread entregadorDePedidos2 = new Thread(entregadorDePedidos); //Hilo 7
        Thread entregadorDePedidos3 = new Thread(entregadorDePedidos); //Hilo 8

        Thread verificadorDePedidos1 = new Thread(verificadorDePedidos); //Hilo 9
        Thread verificadorDePedidos2 = new Thread(verificadorDePedidos); //Hilo 10

        try{
            sistema.getLog().crearArchivo();
        }catch (Exception e) {
            System.out.println("No se pudo crear el archivo");
        }

        preparadorDePedidos1.start();
        preparadorDePedidos2.start();
        preparadorDePedidos3.start();

        despachadorDePedidos1.start();
        despachadorDePedidos2.start();

        entregadorDePedidos1.start();
        entregadorDePedidos2.start();
        entregadorDePedidos3.start();

        verificadorDePedidos1.start();
        verificadorDePedidos2.start();

        while (true) { // Bucle infinito (o hasta una condición)
            try {
                sistema.getLog().escribirHistorial(); // Llama a tu método
                Thread.sleep(200); // Espera 200 ms
                contadorLog++;
                if(contadorLog ==50){
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break; // Termina si hay interrupción
            }
        }
        //hace un recuento de la cantidad de casilleros fuera de servicio
        sistema.refreshCantCasillerosFueraDeServicio();
        //Imprime la estadistica final de los casilleros
        sistema.getLog().escribirFinalHistorial();

    }
}
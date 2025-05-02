import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args){
        List<Pedido> listaPedidos = new ArrayList<>();
        Log log = new Log();
        Sistema sistema = new Sistema(log);

        // Hilos preparadores (original)
        PreparadorDePedidos preparadorDePedidos1 = new PreparadorDePedidos(sistema); //Hilo 1
        PreparadorDePedidos preparadorDePedidos2 = new PreparadorDePedidos(sistema); //Hilo 2
        PreparadorDePedidos preparadorDePedidos3 = new PreparadorDePedidos(sistema); //Hilo 3

        // Hilos despachadores (original)
        Thread despachadorDePedidos1 = new Thread(new DespachadorDePedidos(sistema)); //Hilo 4
        Thread despachadorDePedidos2 = new Thread(new DespachadorDePedidos(sistema)); //Hilo 5

        // Hilos entregadores (original)
        Thread entregadorDePedidos1 = new Thread(new EntregadorDePedido(sistema)); //Hilo 6
        Thread entregadorDePedidos2 = new Thread(new EntregadorDePedido(sistema)); //Hilo 7
        Thread entregadorDePedidos3 = new Thread(new EntregadorDePedido(sistema)); //Hilo 8

        // Hilos verificadores (original)
        Thread verificadorDePedidos1 = new Thread(new VerificadorDePedido(sistema)); //Hilo 9
        Thread verificadorDePedidos2 = new Thread(new VerificadorDePedido(sistema)); //Hilo 10

        // Creación de pedidos (original)
        for (int i = 0; i < 100; i++) {
            listaPedidos.add(new Pedido());
        }

        // Distribución de pedidos (original con pequeña mejora en el sleep)
        for(int i = 0; i < 96; i++){
            preparadorDePedidos1.setPedido(listaPedidos.get(i));
            preparadorDePedidos2.setPedido(listaPedidos.get(i+1));
            preparadorDePedidos3.setPedido(listaPedidos.get(i+2));
            
            try {
                TimeUnit.MILLISECONDS.sleep(50); // Mejorado usando TimeUnit
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        // Inicio de hilos (original)
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

        // Manejo de finalización (original con mejora para mostrar resumen)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Interrumpir todos los hilos
            preparadorDePedidos1.interrupt();
            preparadorDePedidos2.interrupt();
            preparadorDePedidos3.interrupt();
            
            despachadorDePedidos1.interrupt();
            despachadorDePedidos2.interrupt();
            
            entregadorDePedidos1.interrupt();
            entregadorDePedidos2.interrupt();
            entregadorDePedidos3.interrupt();
            
            verificadorDePedidos1.interrupt();
            verificadorDePedidos2.interrupt();
            
            // Mostrar resumen al finalizar
            log.mostrarResumen(); // Cambio mínimo aquí para que funcione
        }));
    }
}
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        // Arranco a medir el tiempo de ejecucion del programa
        long tiempoInicio = System.currentTimeMillis();
        // Creo el sistema
        Sistema sistema = new Sistema();
        // Creo el Log
        Log log = new Log(sistema);
        // Creo la lista de hilos para simplificar la escritura
        List<Thread> hilos = new ArrayList<>();
        // Creo y agrego hilos para cada clase de Registros
        for (int i = 0; i < 3; i++){
            hilos.add(new Thread(new PreparacionPedidos(sistema)));
            hilos.add(new Thread(new EntregaPedido(sistema)));
        }
        for (int i = 0; i < 2; i++){
            hilos.add(new Thread(new TransitoPedidos(sistema)));
            hilos.add(new Thread(new VerificacionPedido(sistema)));
        }
        // Inicializo todos los hilos
        for (Thread hilo : hilos){
            hilo.start();
        }
        // Mediante Runtime accedo cuando el programa es por finalizar para despues tomar las estadisticas
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Cerrando Sistema");
            // Interrumpo los hilos
            for (Thread hilo : hilos){
                hilo.interrupt();
            }
            // Hago esperar al hilo Main
            for (Thread hilo : hilos){
                try {
                    hilo.join();
                } catch (InterruptedException e){
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
            // Mido el tiempo final
            long tiempoFinal = System.currentTimeMillis();
            long tiempoTotal = tiempoFinal - tiempoInicio;
            // Seteo el tiempo al log
            log.setTiempoTotalDemora(tiempoTotal);
            log.imprimirHistorial();
        }));
    }
}
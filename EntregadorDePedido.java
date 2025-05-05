import java.util.Random;

public class EntregadorDePedido implements Runnable {
    private final Sistema sistema;
    private final int duracion = 50; //en milisegundos
    private final int intentosMaximos = 25;
    private final double probDeConfrmacion = 0.90;

    public EntregadorDePedido(Sistema sistema) {
        this.sistema = sistema;
    }

    /**
     * Obtiene un pedido de la lista de pedidos en transito de sistema mediante el metodo getPedidoDeListaEnTransitoAleatorio().
     * Si el pedido es null significa que la lista no tiene pedidos por lo tanto espera 100 milisegundos y vuelve a intentarlo
     * si lo intenta la cantidad de veces intentosMaximos y no logra obtener un pedido entonces finaliza su ejecucion.
     * Si el pedido no es null a partir del resultado del experimento aleatorio:
     * -Si el resultado es exitoso el pedido es agregado a la lista de pedidos entregados y se cambia el estado del pedido a ENTREGADO
     * -Si el resultado no es exitoso el pedido es agregado a la lista de pedidos fallidos y se cambia el estado del pedido a FALLIDO
     *
     * @Param intento: contador de intentos realizados
     *
     */
    @Override
    public void run() {
        int intentos = 0;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Pedido pedido = sistema.getPedidoDeListaEnTransitoAleatorio();
                if (pedido != null) {
                    //Devuelve un double entre 0.00 y 1.00 que representa el resultado probabilistico de la verificacion
                    double resultado = new Random().nextDouble();

                    if (resultado <= probDeConfrmacion) {
                        sistema.removePedidoEnTransito(pedido);
                        sistema.addPedidoEnEntregados(pedido);
                        pedido.setEstado(EstadoPedido.ENTREGADO);
                        intentos = 0;
                    } else {
                        sistema.removePedidoEnTransito(pedido);
                        sistema.addPedidoEnFallidos(pedido);
                        pedido.setEstado(EstadoPedido.FALLIDO);
                        intentos = 0;
                    }
                    Thread.sleep(duracion);
                }else{
                    if(intentos<intentosMaximos){
                        Thread.sleep(100);
                        intentos++;
                    }else{
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
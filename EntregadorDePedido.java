import java.util.Random;

public class EntregadorDePedido implements Runnable {
    private final Sistema sistema;
    private final int duracionProceso = 100; //en milisegundos
    private final int duracionEspera = 100; //en milisegundos
    private final int intentosMaximos = 20;
    private final double probDeConfrmacion = 0.90;

    public EntregadorDePedido(Sistema sistema) {
        this.sistema = sistema;
    }

    /**
     * Hilo que simula el proceso de entrega de pedidos en transito,
     * aplicando una probabilidad para determinar si son entregados o fallidos.
     * <p>
     * Si no hay pedidos disponibles, realiza varios intentos antes de detenerse.
     * Si el hilo es interrumpido, finaliza su ejecución.
     * </p>
     * @see Sistema#getPedidoDeListaEnTransitoAleatorio()
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
                    Thread.sleep(duracionProceso);
                }else{
                    if(intentos<intentosMaximos){
                        Thread.sleep(duracionEspera);
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
import java.util.Random;

public class VerificadorDePedido implements Runnable {
    private Sistema sistema;
    private final int duracion = 30; //en milisegundos
    private final int intentosMaximos = 300;
    private final double probDeVerificacion = 0.95;

    public VerificadorDePedido(Sistema sistema) {
        this.sistema = sistema;
    }

    /**
     * Obtiene un pedido de la lista de pedidos entregados de sistema mediante el metodo getPedidoDeListaEntregadosAleatorio()
     * Si el pedido es null significa que la lista no tiene pedidos por lo tanto espera 100 milisegundos y vuelve a intentarlo
     * si lo intenta la cantidad de veces intentosMaximos y no logra obtener un pedido entonces finaliza su ejecucion.
     * Si el pedido no es null a partir del resultado del experimento aleatorio:
     * -Si el resultado es exitoso el pedido es agregado a la lista de pedidos verificados y se cambia el estado del pedido a VERIFICADO
     * -Si el resultado no es exitoso el pedido es agregado a la lista de pedidos fallidos y se cambia el estado del pedido a FALLIDO
     *
     * @Param intento: contador de intentos realizados
     *
     */
    @Override
    public void run() {
        int intentos = 0;
        while (!Thread.currentThread().isInterrupted()) {
                try{
                    //El pedido y el id del casillero en el que se encuentra
                    Pedido pedido = sistema.getPedidoDeListaEntregadosAleatorio();
                    if (pedido != null) {
                        //Devuelve un double entre 0.00 y 1.00 que representa el resultado probabilistico de la verificacion
                        double resultado = new Random().nextDouble();

                        if (resultado <= probDeVerificacion) {
                            sistema.removePedidoEnEntregados(pedido);
                            sistema.addPedidoEnVerificados(pedido);
                            pedido.setEstado(EstadoPedido.VERIFICADO);
                            intentos = 0;
                        } else {
                            sistema.removePedidoEnEntregados(pedido);
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

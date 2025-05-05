import java.util.Random;

public class DespachadorDePedidos implements Runnable {
    private final Sistema sistema;
    private final int duracion = 10; //en milisegundos
    private final int intentosMaximos = 5;
    private final double probInfoCorrecta = 0.85;

    public DespachadorDePedidos(Sistema sistema) {
        this.sistema = sistema;
    }

    /**
     * Obtiene un pedido de la lista de pedidos en preparacion de sistema mediante el metodo getPedidoDeListaEnPreparacionAleatorio().
     * Si el pedido es null significa que la lista no tiene pedidos por lo tanto espera 100 milisegundos y vuelve a intentarlo
     * si lo intenta la cantidad de veces intentosMaximos y no logra obtener un pedido entonces finaliza su ejecucion.
     * Si el pedido no es null a partir del resultado del experimento aleatorio:
     * -Si el resultado es exitoso se libera el casillero, el pedido es agregado a la lista de pedidos en transito
     * y se cambia el estado del pedido a EN_TRANSITO
     * -Si el resultado no es exitoso el casillero se pone en fuera de servicio, el pedido es agregado a la lista de pedidos fallidos
     * y se cambia el estado del pedido a FALLIDO
     *
     * @Param intento: contador de intentos realizados
     *
     */
    @Override
    public void run() {
        int intentos = 0;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                //Simula el experimento aleatorio de que la informacion sea correcta o no
                double resultado = new Random().nextDouble();

                //El pedido y el id del casillero en el que se encuentra
                Pedido pedido = sistema.getPedidoDeListaEnPreparacionAleatorio();

                if (pedido != null) {
                    Casillero casillero = pedido.getCasilleroAsignado();
                    if (resultado <= probInfoCorrecta) {
                        casillero.liberar();
                        sistema.removePedidoEnPreparacion(pedido);
                        sistema.addPedidoEnTransito(pedido);
                        pedido.setEstado(EstadoPedido.EN_TRANSITO);
                        intentos =0;
                    } else {
                        casillero.sacarDeServicio();
                        sistema.removePedidoEnPreparacion(pedido);
                        sistema.addPedidoEnFallidos( pedido);
                        pedido.setEstado(EstadoPedido.FALLIDO);
                        intentos =0;
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
                break;
            }
        }
    }
}
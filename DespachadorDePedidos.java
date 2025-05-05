import java.util.Random;

public class DespachadorDePedidos implements Runnable {
    private final Sistema sistema;
    private final int duracionProceso = 70; //en milisegundos
    private final int duracionEspera = 70; //en milisegundos
    private final int intentosMaximos = 20;
    private final double probInfoCorrecta = 0.85;

    public DespachadorDePedidos(Sistema sistema) {
        this.sistema = sistema;
    }

    /**
     * Hilo que simula el proceso de despacho de pedidos en preparacion,
     * aplicando una probabilidad para determinar si son despachados o fallidos.
     * <p>
     * Si no hay pedidos disponibles, realiza varios intentos antes de detenerse.
     * Si el hilo es interrumpido, finaliza su ejecución.
     * </p>
     * @see Sistema#getPedidoDeListaEnPreparacionAleatorio()
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
                break;
            }
        }
    }
}
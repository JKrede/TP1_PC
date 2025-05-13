import java.util.Random;

public class VerificadorDePedido implements Runnable {

    private SistemaDeLogistica sistema;
    private final int duracionProceso = 90; //en milisegundos
    private final int duracionEspera = 100; //en milisegundos
    private final int intentosMaximos = 20;
    private final double probDeVerificacion = 0.95;

    public VerificadorDePedido(SistemaDeLogistica sistema) {
        this.sistema = sistema;
    }

    /**
     * Hilo que simula el proceso de verificación de pedidos entregados,
     * aplicando una probabilidad para determinar si son verificados o fallidos.
     * <p>
     * Si no hay pedidos disponibles, realiza varios intentos antes de detenerse.
     * Si el hilo es interrumpido, finaliza su ejecución.
     * </p>
     * @see SistemaDeLogistica#getPedidoDeListaEntregadosAleatorio()
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
                            sistema.addPedidoEnVerificados(pedido);
                            pedido.setEstado(EstadoPedido.VERIFICADO);
                            intentos = 0;
                        } else {
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

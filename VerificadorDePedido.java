import java.util.Random;

public class VerificadorDePedido implements Runnable {
    private Sistema sistema;
    private final int duracion = 1; //en milisegundos
    private final double ProbDeVerificacion = 0.95;

    public VerificadorDePedido(Sistema sistema) {
        this.sistema = sistema;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
                try{
                    //El pedido y el id del casillero en el que se encuentra
                    Pedido pedido = sistema.getPedidoDeListaEntregadosAleatorio();
                    if (pedido == null) {break;}
                    //Devuelve un double entre 0.00 y 1.00 que representa el resultado probabilistico de la verificacion
                    double resultado = new Random().nextDouble(1.00);

                    if (ProbDeVerificacion >= resultado) {
                        sistema.removePedidoEnEntregados(pedido);
                        sistema.addPedidoEnVerificados(pedido);
                        pedido.setEstado(EstadoPedido.VERIFICADO);
                        sistema.getLog().incCantPedidosVerificados();
                    } else {
                        sistema.removePedidoEnEntregados(pedido);
                        sistema.addPedidoEnFallidos(pedido);
                        pedido.setEstado(EstadoPedido.FALLIDO);
                        sistema.getLog().incCantPedidosFallidos();
                    }
                    Thread.sleep(duracion);
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
        }
    }
}

import java.util.Random;

public class EntregadorDePedido implements Runnable {
    private final Sistema sistema;
    private final int duracion = 1; //en milisegundos
    private final double ProbDeConfrmacion = 0.90;

    public EntregadorDePedido(Sistema sistema) {
        this.sistema = sistema;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Pedido pedido = sistema.getPedidoDeListaEnTransitoAleatorio();
                if (pedido == null) {break;}
                //Devuelve un double entre 0.00 y 1.00 que representa el resultado probabilistico de la verificacion
                double resultado = new Random().nextDouble(1.00);

                if (ProbDeConfrmacion >= resultado) {
                    sistema.removePedidoEnTransito(pedido);
                    sistema.addPedidoEnEntregados(pedido);
                    pedido.setEstado(EstadoPedido.ENTREGADO);
                } else {
                    sistema.removePedidoEnTransito(pedido);
                    sistema.addPedidoEnFallidos(pedido);
                    sistema.getLog().incCantPedidosFallidos();
                    pedido.setEstado(EstadoPedido.FALLIDO);
                }
                Thread.sleep(duracion);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
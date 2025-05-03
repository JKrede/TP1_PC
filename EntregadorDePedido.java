import java.util.Random;

public class EntregadorDePedido implements Runnable {
    private final Sistema sistema;
    private final int duracion = 100; //en milisegundos
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
                    sistema.getListadoEnTransito().remove(pedido);
                    pedido.setEstado(EstadoPedido.ENTREGADO);
                    sistema.getListadoEntregados().add(pedido);
                } else {
                    sistema.getListadoEnTransito().remove(pedido);
                    sistema.getListadoFallidos().add(pedido);
                    pedido.setEstado(EstadoPedido.FALLIDO);
                    sistema.getLog().incCantPedidosFallidos();
                }
                Thread.sleep(duracion);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
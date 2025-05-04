import java.util.Random;

public class EntregadorDePedido implements Runnable {
    private final Sistema sistema;
    private final int duracion = 0; //en milisegundos
    private int intentos =0;
    private final int intentosMaximos = 300;
    private final double probDeConfrmacion = 0.90;

    public EntregadorDePedido(Sistema sistema) {
        this.sistema = sistema;
    }

    @Override
    public void run() {
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
                    } else {
                        sistema.removePedidoEnTransito(pedido);
                        sistema.addPedidoEnFallidos(pedido);
                        pedido.setEstado(EstadoPedido.FALLIDO);
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
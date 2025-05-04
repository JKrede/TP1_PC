import java.util.Random;

public class VerificadorDePedido implements Runnable {
    private Sistema sistema;
    private final int duracion = 0; //en milisegundos
    private int intentos =0;
    private final int intentosMaximos = 300;
    private final double probDeVerificacion = 0.95;

    public VerificadorDePedido(Sistema sistema) {
        this.sistema = sistema;
    }

    @Override
    public void run() {
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
                        } else {
                            sistema.removePedidoEnEntregados(pedido);
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

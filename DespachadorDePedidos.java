import java.util.Random;

public class DespachadorDePedidos implements Runnable {
    private final Sistema sistema;
    private final int duracion = 1; //en milisegundos
    private final double probInfoCorrecta = 0.85;

    public DespachadorDePedidos(Sistema sistema) {
        this.sistema = sistema;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                //Simula el experimento aleatorio de que la informacion sea correcta o no
                double resultado = new Random().nextDouble(1.00);
                //El pedido y el id del casillero en el que se encuentra
                Pedido pedido = sistema.getPedidoDeListaEnPreparacionAleatorio();
                if (pedido == null) {
                    break;
                }

                Casillero casillero = pedido.getCasilleroAsignado();
                if (resultado <= probInfoCorrecta) {
                    casillero.liberar();
                    sistema.removePedidoEnPreparacion(pedido);
                    sistema.addPedidoEnTransito(pedido);
                    pedido.setEstado(EstadoPedido.EN_TRANSITO);
                } else {
                    casillero.sacarDeServicio();
                    sistema.removePedidoEnPreparacion(pedido);
                    sistema.addPedidoEnFallidos( pedido);
                    sistema.getLog().incCantPedidosFallidos();
                    pedido.setEstado(EstadoPedido.FALLIDO);
                }
                Thread.sleep(duracion);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
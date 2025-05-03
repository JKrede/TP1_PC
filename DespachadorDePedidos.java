import java.util.Random;

public class DespachadorDePedidos implements Runnable {
    private final Sistema sistema;
    private final int duracion = 100; //en milisegundos
    private final double probInfoCorrecta = 0.85;

    public DespachadorDePedidos(Sistema sistema) {
        this.sistema = sistema;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                //El pedido y el id del casillero en el que se encuentra
                Pedido pedido = sistema.getPedidoDeListaEnPreparacionAleatorio();
                if (pedido == null) {
                    break;
                }

                int idCasillero = pedido.getCasilleroAsignado().getId();

                //Simula el experimento aleatorio de que la informacion sea correcta o no
                double resultado = new Random().nextDouble(1.00);

                if (resultado <= probInfoCorrecta) {
                    sistema.getListadoEnPreparacion().remove(pedido);
                    sistema.getListadoEnTransito().add(pedido);
                    pedido.setEstado(EstadoPedido.EN_TRANSITO);
                    sistema.getCasillero(idCasillero).liberar();
                } else {
                    sistema.getListadoEnPreparacion().remove(pedido);
                    sistema.getListadoFallidos().add(pedido);
                    sistema.getLog().incCantPedidosFallidos();
                    pedido.setEstado(EstadoPedido.FALLIDO);
                    sistema.getCasillero(idCasillero).sacarDeServicio();
                }
                Thread.sleep(duracion);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
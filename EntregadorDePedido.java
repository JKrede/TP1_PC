import java.util.Random;

public class EntregadorDePedido implements Runnable {
    private final Sistema sistema;
    private final int duracion = 100;
    private final double probDeConfirmacion = 0.90;
    private final Random generador = new Random();

    public EntregadorDePedido(Sistema sistema) {
        this.sistema = sistema;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Pedido pedido = sistema.getPedidoDeListaEnTransitoRemovidoAleatorio();
                
                if (pedido != null) {
                    double resultado = generador.nextDouble();

                    if (resultado <= probDeConfirmacion) {
                        synchronized (sistema.getListadoEntregados()) {
                            sistema.getListadoEntregados().add(pedido);
                        }
                        sistema.getCasillero(pedido.getCasilleroAsignado().getId()).liberar();
                    } else {
                        synchronized (sistema.getListadoFallidos()) {
                            sistema.getListadoFallidos().add(pedido);
                        }
                        sistema.getLog().incCantPedidosFallidos();
                        sistema.getCasillero(pedido.getCasilleroAsignado().getId()).sacarDeServicio();
                    }
                }
                
                Thread.sleep(duracion);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.println("Error en entregador: " + e.getMessage());
            }
        }
    }
}
import java.util.Random;

public class DespachadorDePedidos implements Runnable {
    private final Sistema sistema;
    private final int duracion = 100;
    private final double probInfoCorrecta = 0.85;
    private final Random generador = new Random();

    public DespachadorDePedidos(Sistema sistema) {
        this.sistema = sistema;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Pedido pedido = sistema.getPedidoListadoEnPreparacionRemovidoAleatorio();
                
                if (pedido != null) {
                    int idCasillero = pedido.getCasilleroAsignado().getId();
                    double resultado = generador.nextDouble();

                    if (resultado <= probInfoCorrecta) {
                        synchronized (sistema.getListadoEnTransito()) {
                            sistema.getListadoEnTransito().add(pedido);
                        }
                        sistema.getCasillero(idCasillero).liberar();
                    } else {
                        synchronized (sistema.getListadoFallidos()) {
                            sistema.getListadoFallidos().add(pedido);
                        }
                        sistema.getLog().incCantPedidosFallidos();
                        sistema.getCasillero(idCasillero).sacarDeServicio();
                    }
                }
                
                Thread.sleep(duracion);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.println("Error en despachador: " + e.getMessage());
            }
        }
    }
}
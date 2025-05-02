import java.util.Random;

public class VerificadorDePedido implements Runnable {
    private final Sistema sistema;
    private final int duracion = 100;
    private final double probDeVerificacion = 0.95;
    private final Random generador = new Random();

    public VerificadorDePedido(Sistema sistema) {
        this.sistema = sistema;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Pedido pedido = sistema.getPedidoDeListaEntregadosRemovidoAleatorio();
                
                if (pedido != null) {
                    double resultado = generador.nextDouble();

                    if (resultado <= probDeVerificacion) {
                        synchronized (sistema.getListadoVerificados()) {
                            sistema.getListadoVerificados().add(pedido);
                        }
                        sistema.getLog().incCantPedidosVerificados();
                    } else {
                        synchronized (sistema.getListadoFallidos()) {
                            sistema.getListadoFallidos().add(pedido);
                        }
                        sistema.getLog().incCantPedidosFallidos();
                    }
                }
                
                Thread.sleep(duracion);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.println("Error en verificador: " + e.getMessage());
            }
        }
    }
}
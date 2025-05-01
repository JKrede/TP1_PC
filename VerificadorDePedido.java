import java.util.Random;

public class VerificadorDePedido implements Runnable {
    private Sistema sistema;
    private final int duracion = 100; //en milisegundos
    private final double ProbDeVerificacion = 0.95;

    public VerificadorDePedido(Sistema sistema) {
        this.sistema = sistema;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {

                    if (!sistema.getListadoEntregados().isEmpty()) {
                        Random generador = new Random();
                        //Posicion aleatoria de la lista
                        int posAleatoria = generador.nextInt(sistema.getListadoEntregados().size());

                        //Devuelve un double entre 0.00 y 1.00 que representa el resultado probabilistico de la verificacion
                        double resultado = generador.nextDouble(1.00);

                        //El pedido y el id del casillero en el que se encuentra
                        Pedido pedido = sistema.getListadoEntregados().get(posAleatoria);

                        if (ProbDeVerificacion >= resultado) {
                            sistema.getListadoEntregados().remove(pedido);
                            sistema.getListadoVerificados().add(pedido);
                            sistema.getLog().incCantPedidosVerificados();
                        } else {
                            sistema.getListadoEntregados().remove(pedido);
                            sistema.getListadoFallidos().add(pedido);
                            sistema.getLog().incCantPedidosFallidos();
                        }
                        Thread.sleep(duracion);
                    }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

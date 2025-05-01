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

                    if (!sistema.getListadoEnTransito().isEmpty()) {
                        Random generador = new Random();
                        //Posicion aleatoria de la lista
                        int posAleatoria = generador.nextInt(sistema.getListadoEnTransito().size());

                        //Devuelve un double entre 0.00 y 1.00 que representa el resultado probabilistico de la verificacion
                        double resultado = generador.nextDouble(1.00);

                        //El pedido y el id del casillero en el que se encuentra
                        Pedido pedido = sistema.getListadoEnTransito().get(posAleatoria);

                        if (ProbDeConfrmacion >= resultado) {
                            sistema.ListarEnEntregados(pedido);
                        } else {
                            sistema.ListarEnFallidos(pedido);
                        }
                        Thread.sleep(duracion);
                    }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

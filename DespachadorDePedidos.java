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

                    if (!sistema.getListadoEnPreparacion().isEmpty()) {
                        Random generador = new Random();
                        //Posicion aleatoria de la lista
                        int posAleatoria = generador.nextInt(sistema.getListadoEnPreparacion().size());

                        //El pedido y el id del casillero en el que se encuentra
                        Pedido pedido = sistema.getListadoEnTransito().get(posAleatoria);
                        int idCasillero = sistema.getListadoEnTransito().get(posAleatoria).getCasilleroAsignado().getId();

                        //Simula el experimento aleatorio de que la informacion sea correcta o no
                        double resultado = generador.nextDouble(1.00);

                        if (resultado <= probInfoCorrecta) {
                            sistema.ListarEnTransito(pedido);
                            sistema.getCasillero(idCasillero).liberar();
                        } else {
                            sistema.ListarEnFallidos(pedido);
                            sistema.getCasillero(idCasillero).sacarDeServicio();
                        }
                        Thread.sleep(duracion);
                    }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
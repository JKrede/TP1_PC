import java.util.Random;

public class PreparadorDePedidos implements Runnable {
    private final Sistema sistema;
    private final int duracion = 100; //en milisegundos
    private Pedido pedido;

    public PreparadorDePedidos(Sistema sistema) {
        this.sistema = sistema;
        pedido = null;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    /**
     * Este metodo lo que hace es elegir casilleros aleatorios hasta encontrar uno en estado vacio, y lo ocupa
     * con el pedido establecido en su campo pedido
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {

                    Random generador = new Random();
                    boolean casilleroOcupado = true;

                    // Repite hasta encontrar un casillero vacío
                    while (casilleroOcupado && !Thread.currentThread().isInterrupted()) {

                        int posAleatoria = generador.nextInt(200);

                        // Verificar si el casillero está vacío
                        if (sistema.getCasillero(posAleatoria).getEstado() == EstadoCasillero.VACIO) {

                            pedido.setEstado(EstadoPedido.EN_PREPARACION);
                            sistema.getCasillero(posAleatoria).ocupar(pedido);
                            sistema.getListadoEnPreparacion().add(pedido);

                            casilleroOcupado = false;
                            Thread.sleep(duracion);
                        }
                    }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
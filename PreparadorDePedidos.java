import java.util.List;

public class PreparadorDePedidos implements Runnable {
    private final Sistema sistema;
    private final int duracion = 20; //en milisegundos
    private final List<Pedido> listaPedidosPendientes;
    private final Object lockSelectPedido = new Object();
    private final int intentosMaximos = 25;

    public PreparadorDePedidos(Sistema sistema, List<Pedido> listaPedidosPendientes) {
        this.sistema = sistema;
        this.listaPedidosPendientes = listaPedidosPendientes;
    }

    /**
     * Devuelve un pedido de la lista de pedidos pendientes y si la lista esta vacia devuelve null
     *
     * @return Pedido: El pedido tomado de la lista por el preparador
     */
    public Pedido obtenerPedido() {
        synchronized (lockSelectPedido) {
            if (!listaPedidosPendientes.isEmpty()) {
                return listaPedidosPendientes.removeLast();
            } else {
                return null;
            }
        }
    }


    /**
     * Obtiene un pedido mediante el metodo obtenerPedido().
     * Si el pedido es null significa que la lista ya no tiene mas pedidos y el hilo finaliza su ejecucion saliendo del while.
     * Si el pedido es un pedido, entonces intenta colocarlo en un casillero aleatorio de la matriz de casilleros, si no fue posible agregar
     * el pedido a la matriz de casilleros espera 100 milisegundos y vuelve a intentarlo las veces establecidas en intentosMaximos.
     * Si supera la cantidad de intentosMaximos el hilo finaliza su ejecucion saliendo del while.
     *
     * @Param pedido: El pedido a agregar en el casillero aleatorio
     * @Param intento: contador de intentos realizados
     *
     */
    @Override
    public void run() {
        Pedido pedido = null;
        int intentos =0;
        while (!Thread.currentThread().isInterrupted()) {
            // Si no tengo pedido, lo traigo de la lista
            if (pedido == null) {
                pedido = obtenerPedido();
                if (pedido == null) break;  // no quedan más pedidos
            }

            try {
                // Agrega el pedido en un casillero aleat
                boolean agregado = sistema.setPedidoEnCasilleroAleatorio(pedido);
                if (agregado) {
                    pedido.setEstado(EstadoPedido.EN_PREPARACION);
                    sistema.addPedidoEnPreparacion(pedido);
                    pedido = null;
                    intentos = 0;
                    Thread.sleep(duracion);
                } else if (intentos< intentosMaximos) {
                    Thread.sleep(100);
                    intentos++;
                } else {
                    Thread.currentThread().interrupt();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
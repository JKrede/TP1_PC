import java.util.List;

public class PreparadorDePedidos implements Runnable {
    private final Sistema sistema;
    private final int duracionProceso = 50; //en milisegundos
    private final int duracionEspera = 100; //en milisegundos
    private final int intentosMaximos = 20;

    private final List<Pedido> listaPedidosPendientes;
    private final Object lockSelectPedido = new Object();

    //La duracion maxima de espera es intentosMaximo*duracionEspera

    public PreparadorDePedidos(Sistema sistema, List<Pedido> listaPedidosPendientes) {
        this.sistema = sistema;
        this.listaPedidosPendientes = listaPedidosPendientes;
    }

    /**
     * Obtiene y elimina un pedido de la lista de pedidos pendientes.
     *
     * @return El pedido tomado de la lista, o {@code null} si no hay pedidos disponibles.
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
     * Hilo que gestiona la asignación de pedidos a casilleros disponibles.
     * <p>
     * Si no hay pedidos disponibles o no se puede asignar un pedido tras varios intentos,
     * el hilo finaliza su ejecución.
     * </p>
     * @see Sistema#setPedidoEnCasilleroAleatorio(Pedido)
     * @see Sistema#addPedidoEnPreparacion(Pedido)
     */
    @Override
    public void run() {
        Pedido pedido = null;
        int intentos =0;
        while (!Thread.currentThread().isInterrupted()) {
            // Si no tengo pedido, lo traigo de la lista
            if (pedido == null) {
                pedido = obtenerPedido();
                if (pedido == null) break;  // llega aqui si no quedan más pedidos pendientes
            }

            try {
                // Agrega el pedido en un casillero vacio aleatorio
                boolean agregado = sistema.setPedidoEnCasilleroAleatorio(pedido);
                if (agregado) {
                    pedido.setEstado(EstadoPedido.EN_PREPARACION);
                    sistema.addPedidoEnPreparacion(pedido);
                    pedido = null;
                    intentos = 0;
                    Thread.sleep(duracionProceso);
                } else if (intentos< intentosMaximos) {
                    Thread.sleep(duracionEspera);
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
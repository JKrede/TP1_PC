import java.util.List;

public class PreparadorDePedidos implements Runnable {
    private final Sistema sistema;
    private final int duracion = 20; //en milisegundos
    private final List<Pedido> listaPedidosPendientes;
    private final Object lockSelectPedido = new Object();
    private int intentos =0;
    private final int intentosMaximos = 300;

    public PreparadorDePedidos(Sistema sistema, List<Pedido> listaPedidosPendientes) {
        this.sistema = sistema;
        this.listaPedidosPendientes = listaPedidosPendientes;
    }

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
     * Este metodo lo que hace es elegir casilleros aleatorios hasta encontrar uno en estado vacio, y lo ocupa
     * con el pedido establecido en su campo pedido
     */
    @Override
    public void run() {
        Pedido pedido = null;
        while (!Thread.currentThread().isInterrupted()) {
            // Si no tengo pedido, lo traigo de la lista
            if (pedido == null) {
                pedido = obtenerPedido();
                if (pedido == null) break;  // no quedan más pedidos
            }

            try {
                boolean agregado = sistema.setPedidoEnCasilleroAleatorio(pedido);
                if (agregado) {
                    pedido.setEstado(EstadoPedido.EN_PREPARACION);
                    sistema.addPedidoEnPreparacion(pedido);
                    pedido = null;
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
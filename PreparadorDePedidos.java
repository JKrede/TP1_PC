import java.util.List;

public class PreparadorDePedidos implements Runnable {
    private final Sistema sistema;
    private final int duracion = 50; //en milisegundos
    private Pedido pedido;
    private final List<Pedido> listaPedidosPendientes;
    private boolean hayPedidos = true;
    private final Object lockSelectPedido = new Object();

    public PreparadorDePedidos(Sistema sistema, List<Pedido> listaPedidosPendientes) {
        this.sistema = sistema;
        this.listaPedidosPendientes = listaPedidosPendientes;
        pedido = null;
    }

    public void setPedido() {
        synchronized (lockSelectPedido) {
            if(!listaPedidosPendientes.isEmpty()){
                    this.pedido = listaPedidosPendientes.getLast();
                    listaPedidosPendientes.removeLast();
            }else{
                hayPedidos = false;
            }
        }
    }

    /**
     * Este metodo lo que hace es elegir casilleros aleatorios hasta encontrar uno en estado vacio, y lo ocupa
     * con el pedido establecido en su campo pedido
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && hayPedidos) {
            try {
                setPedido();
                Casillero casilleroAleatorio = sistema.getCasilleroAleatorio();
                //-1 entonces no hay casillero vacios
                if ( casilleroAleatorio == null || pedido == null){
                    break;
                }
                pedido.setEstado(EstadoPedido.EN_PREPARACION);
                casilleroAleatorio.ocupar(pedido);
                sistema.addPedidoEnPreparacion(pedido);
                pedido = null;
                Thread.sleep(duracion);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
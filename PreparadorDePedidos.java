import java.util.List;

public class PreparadorDePedidos implements Runnable {
    private final Sistema sistema;
    private final int duracion = 1; //en milisegundos
    private Pedido pedido;
    private final List<Pedido> listaPedidos;
    private final Object lockSelectPedido = new Object();

    public PreparadorDePedidos(Sistema sistema, List<Pedido> listaPedidos) {
        this.sistema = sistema;
        this.listaPedidos = listaPedidos;
        pedido = null;
    }

    public void setPedido() {
        synchronized (lockSelectPedido) {
            for(int i=0;i<listaPedidos.size();i++){
                if(listaPedidos.get(i).getCasilleroAsignado()==null){
                    this.pedido = listaPedidos.get(i);
                    break;
                }else{
                    this.pedido=null;
                }
            }
        }
    }

    /**
     * Este metodo lo que hace es elegir casilleros aleatorios hasta encontrar uno en estado vacio, y lo ocupa
     * con el pedido establecido en su campo pedido
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
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
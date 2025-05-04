import java.util.List;

public class PreparadorDePedidos extends Thread {
    private final Sistema sistema;
    private final int duracion = 100; //en milisegundos
    private Pedido pedido;
    private final Object lockSelectPedido = new Object();
    private final List<Pedido> listaPedidos;

    public PreparadorDePedidos(Sistema sistema, List<Pedido> listaPedidos) {
        this.sistema = sistema;
        pedido = null;
        this.listaPedidos = listaPedidos;
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
                int posAleatoria = sistema.getPosicionCasilleroAleatorio();
                //-1 entonces no hay casillero vacios
                if ( posAleatoria != -1 && pedido != null){
                pedido.setEstado(EstadoPedido.EN_PREPARACION);
                sistema.getCasillero(posAleatoria).ocupar(pedido);
                sistema.getListadoEnPreparacion().add(pedido);
                pedido = null;
                Thread.sleep(duracion);
                }
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
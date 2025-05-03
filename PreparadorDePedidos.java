public class PreparadorDePedidos extends Thread {
    private final Sistema sistema;
    private final int duracion = 100; //en milisegundos
    private Pedido pedido;
    private final Object lockSelectPedido = new Object();

    public PreparadorDePedidos(Sistema sistema) {
        this.sistema = sistema;
        pedido = null;
    }

    public void setPedido(Pedido pedido) {
        synchronized (lockSelectPedido) {
            if(pedido.getCasilleroAsignado()==null){
                this.pedido = pedido;
            }else{
                this.pedido=null;
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
                if ( posAleatoria == -1 || pedido == null){
                    break;
                }
                pedido.setEstado(EstadoPedido.EN_PREPARACION);
                sistema.getCasillero(posAleatoria).ocupar(pedido);
                sistema.getListadoEnPreparacion().add(pedido);
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
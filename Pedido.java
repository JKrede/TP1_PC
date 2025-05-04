
public class Pedido {

    private final int id;
    private EstadoPedido estado;
    private Casillero casilleroAsignado;
    private final Object lockPedido = new Object();

    public Pedido(int id) {
        this.id = id;
        estado = EstadoPedido.CREADO;
        casilleroAsignado = null;
    }

    public void setEstado(EstadoPedido estado){
        synchronized (lockPedido){
            this.estado = estado;
        }
    }

    public void setCasilleroAsignado(Casillero casilleroAsignado) {
        this.casilleroAsignado = casilleroAsignado;
    }

    public EstadoPedido getEstado(){
        return estado;
    }

    public Casillero getCasilleroAsignado(){
        return casilleroAsignado;
    }

    public int getId(){
        return id;
    }
}

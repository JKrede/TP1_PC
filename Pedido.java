
public class Pedido {
    private static int CONTADOR=0;
    private final int id;
    private EstadoPedido estado;
    private Casillero casilleroAsignado;

    public Pedido() {
        this.id = Pedido.CONTADOR++;
        estado = EstadoPedido.CREADO;
        casilleroAsignado = null;
    }

    public void setEstado(EstadoPedido estado){
        this.estado = estado;
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

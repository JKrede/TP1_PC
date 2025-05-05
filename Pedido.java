
public class Pedido {

    private final int id;
    private EstadoPedido estado;
    private Casillero casilleroAsignado;

    public Pedido(int id) {
        this.id = id;
        estado = EstadoPedido.CREADO;
        casilleroAsignado = null;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public void setCasilleroAsignado(Casillero casilleroAsignado) {
        this.casilleroAsignado = casilleroAsignado;
    }

    public Casillero getCasilleroAsignado() {
        return casilleroAsignado;
    }
    public EstadoPedido getEstado() {
        return estado;
    }
}

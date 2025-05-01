
public class Pedido {

    private EstadoPedido estado;
    private int id;


    public Pedido(EstadoPedido p, int id) {
        setEstadoPedido(p);
        setId(id);

    }
    public void setEstadoPedido(EstadoPedido estado){ this.estado = estado;}
    public void setId(int id){ this.id = id;}
    public EstadoPedido getEstado(){return estado;}
    public int getId(){return id;}
}

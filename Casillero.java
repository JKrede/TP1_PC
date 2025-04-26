public class Casillero {
    private EstadoCasillero estado;
    private Pedido pedido;
    private int contador;

    public Casillero() {
        this.pedido = new Pedido<>();
        this.estado = EstadoCasillero.VACIO;
        this.contador = 0;
    }

    public void incrementarContador(){
        this.contador++;
    }

    public int getContador(){
        return this.contador;
    }

    public void setEstado(EstadoCasillero estado){
        this.estado = estado;
    }

    public EstadoCasillero getEstado(){
        return this.estado;
    }

    public void setPedido(EstadoPedido estadoPedido) {
        this.pedido.setEstado(estadoPedido);
    }

    public EstadoPedido getPedido(){
        return pedido.getEstado();
    }

    public boolean esVacio(){
        return this.contador == 0;
    }

}
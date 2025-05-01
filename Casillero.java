public class Casillero {

    private final int id;
    private Pedido pedido;
    private EstadoCasillero estado;
    private int vecesOcupado;

    public Casillero(int id) {
        this.id = id;
        pedido = null;
        estado = EstadoCasillero.VACIO;
        vecesOcupado = 0;
    }
    /**
     * Verifica que el casillero este vacio y lo ocupa con el pedido pasado por argumento
     *
     * @Param pedido: El pedido con el cual se va a ocupar el casillero
     * @Throws IllegalStateException: Si el casillero no se encuentra ocupado
     */
    public synchronized void ocupar(Pedido pedido) {
        if (this.estado == EstadoCasillero.VACIO){
            this.pedido = pedido;
            this.pedido.setCasilleroAsignado(this);
            estado = EstadoCasillero.OCUPADO;
            vecesOcupado++;
        }
        else{
            throw new IllegalStateException("El casillero esta ocupado");
        }
    }

    /**
     * Verifica que el casillero este ocupado y lo libera de su pedido asociado y pasa a estado VACIO
     *
     * @Throws IllegalStateException: Si el casillero no se encuentra ocupado
     */
    public synchronized void liberar() {
        if(this.estado==EstadoCasillero.OCUPADO) {
            this.pedido = null;
            this.estado = EstadoCasillero.VACIO;
        }
        else{
            throw new IllegalStateException("El casillero esta libre");
        }
    }
    /**
     * Cambia el estado del casillero a fuera de servicio y borra el pedido asociado al mismo
     *
     */
    public synchronized void sacarDeServicio() {
        this.pedido = null;
        this.estado = EstadoCasillero.FUERA_DE_SERVICIO;
    }

    public EstadoCasillero getEstado() {
        return estado;
    }

    public int getId() {
        return id;
    }

    public int getVecesOcupado() {
        return vecesOcupado;
    }

}
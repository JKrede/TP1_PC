public class Casillero {

    private final int id;
    private Pedido pedido;
    private EstadoCasillero estado;
    private int vecesOcupado;
    private final Object lockCasillero = new Object();

    public Casillero(int id) {
        this.id = id;
        pedido = null;
        estado = EstadoCasillero.VACIO;
        vecesOcupado = 0;
    }

    /**
     * Ocupa el casillero con el pedido recibido si está vacío.
     *
     * @param pedido El pedido con el cual se ocupará el casillero.
     * @throws IllegalStateException Si el casillero ya está ocupado.
     */
    public void ocupar(Pedido pedido) {
       synchronized (lockCasillero) {
           if (this.estado == EstadoCasillero.VACIO) {
               this.pedido = pedido;
               this.pedido.setCasilleroAsignado(this);
               estado = EstadoCasillero.OCUPADO;
               vecesOcupado++;
           } else {
               throw new IllegalStateException("El casillero esta ocupado");
           }
       }
    }

    /**
     * Libera el casillero si actualmente está ocupado.
     *
     * @throws IllegalStateException Si el casillero ya está vacío.
     */
    public void liberar() {
        synchronized (lockCasillero) {
            if (this.estado == EstadoCasillero.OCUPADO) {
                this.pedido = null;
                this.estado = EstadoCasillero.VACIO;
            } else {
                throw new IllegalStateException("El casillero esta libre");
            }
        }
    }

    /**
     * Marca el casillero como fuera de servicio y elimina cualquier pedido asignado.
     */
    public void sacarDeServicio() {
        synchronized (lockCasillero) {
            this.pedido = null;
            this.estado = EstadoCasillero.FUERA_DE_SERVICIO;
        }
    }

    /**
     * Obtiene el estado actual del casillero.
     *
     * @return El estado del casillero.
     */
    public EstadoCasillero getEstado() {
        synchronized (lockCasillero) {return estado;}
    }
    public int getId () {
        return id;
    }
    /**
     * Obtiene la cantidad de veces que el casillero fue ocupado.
     *
     * @return Número de veces que fue ocupado.
     */
    public int getVecesOcupado() {
        return vecesOcupado;
    }

}
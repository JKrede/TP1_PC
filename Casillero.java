public class Casillero {
    private final int id;
    private Pedido pedido;
    private EstadoCasillero estado;
    private int vecesOcupado;
    private final Object lock = new Object();

    public Casillero(int id) {
        this.id = id;
        this.pedido = null;
        this.estado = EstadoCasillero.VACIO;
        this.vecesOcupado = 0;
    }

    public void ocupar(Pedido pedido) {
        synchronized (lock) {
            if (this.estado != EstadoCasillero.VACIO) {
                throw new IllegalStateException("El casillero está ocupado");
            }
            this.pedido = pedido;
            pedido.setCasilleroAsignado(this);
            this.estado = EstadoCasillero.OCUPADO;
            this.vecesOcupado++;
        }
    }

    public void liberar() {
        synchronized (lock) {
            if (this.estado != EstadoCasillero.OCUPADO) {
                throw new IllegalStateException("El casillero está libre");
            }
            this.pedido = null;
            this.estado = EstadoCasillero.VACIO;
        }
    }

    public void sacarDeServicio() {
        synchronized (lock) {
            this.pedido = null;
            this.estado = EstadoCasillero.FUERA_DE_SERVICIO;
        }
    }

    public EstadoCasillero getEstado() {
        synchronized (lock) {
            return estado;
        }
    }

    public int getId() {
        return id;
    }

    public int getVecesOcupado() {
        synchronized (lock) {
            return vecesOcupado;
        }
    }
}
import java.util.ArrayList;

public class Sistema {
    public static final int CANT_CASILLEROS = 200;
    private final Casillero[] matrizDeCasilleros = new Casillero[CANT_CASILLEROS];

    private final ArrayList<Pedido> pedidosEnPreparacion = new ArrayList<>();
    private final ArrayList<Pedido> pedidosEnTransito = new ArrayList<>();
    private final ArrayList<Pedido> pedidosEntregados = new ArrayList<>();
    private final ArrayList<Pedido> pedidosVerificados = new ArrayList<>();
    private final ArrayList<Pedido> pedidosFallidos = new ArrayList<>();
    private final Log log;
    //mas de un lock?
    private final Object lock = new Object();

    public Sistema(Log log) {
        for (int i = 0; i < CANT_CASILLEROS; i++) {
            matrizDeCasilleros[i] = new Casillero(i);
        }
        this.log = log;
    }

    public Casillero getCasillero(int index) {
        if (index < 0 || index >= CANT_CASILLEROS) {
            throw new IndexOutOfBoundsException("Índice de casillero inválido");
        }
        synchronized (lock) {
            return matrizDeCasilleros[index];
        }
    }
    /**
     * Recorre la matriz de casilleros e incrementa la cantidad de casilleros fuera de servicio en el log
     */
    public void refreshCantCasillerosFueraDeServicio() {
        for (int i = 0; i < CANT_CASILLEROS; i++) {
            if (matrizDeCasilleros[i].getEstado() == EstadoCasillero.FUERA_DE_SERVICIO) {
                log.incCantCasillerosFueraDeServicio();
            }
        }
    }
    public ArrayList<Pedido> getListadoEnPreparacion() {
        return pedidosEnPreparacion;
    }

    public ArrayList<Pedido> getListadoEnTransito() {
        return pedidosEnTransito;
    }

    public ArrayList<Pedido> getListadoEntregados() {
        return pedidosEntregados;
    }

    public ArrayList<Pedido> getListadoVerificados() {
        return pedidosVerificados;
    }

    public ArrayList<Pedido> getListadoFallidos() {
        return pedidosFallidos;
    }

    public void ListarEnPreparacion(Pedido pedido) {
        if(pedido!=null){
            synchronized (lock) {
                pedidosEnPreparacion.add(pedido);
            }
        }
        else{
            throw new IllegalArgumentException("Pedido y casillero no pueden ser null");
        }
    }

    public void ListarEnTransito(Pedido pedido) {
        synchronized (lock) {
            if (pedidosEnPreparacion.remove(pedido)) {
                pedidosEnTransito.add(pedido);
            }
        }
    }

    public void ListarEnEntregados(Pedido pedido) {
        synchronized (lock) {
            if (pedidosEnTransito.remove(pedido)) {
                pedidosEntregados.add(pedido);
            }
        }
    }

    public void ListarEnVerificados(Pedido pedido) {
        synchronized (lock) {
            if (pedidosEntregados.remove(pedido)) {
                pedidosVerificados.add(pedido);
                log.incCantPedidosVerificados();
            }
        }
    }

    public void ListarEnFallidos(Pedido pedido) {
        synchronized (lock) {
            pedidosFallidos.add(pedido);
            log.incCantPedidosFallidos();
        }
    }

}
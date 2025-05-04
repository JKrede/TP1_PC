import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Sistema {
    public static final int CANT_CASILLEROS = 200;
    private final Casillero[] matrizDeCasilleros = new Casillero[CANT_CASILLEROS];

    private final List<Pedido> pedidosEnPreparacion = new ArrayList<>();
    private final List<Pedido> pedidosEnTransito = new ArrayList<>();
    private final List<Pedido> pedidosEntregados = new ArrayList<>();
    private final List<Pedido> pedidosVerificados = new ArrayList<>();
    private final List<Pedido> pedidosFallidos = new ArrayList<>();
    private final Log log;

    private final Object lockMatrizCasillero = new Object();
    private final Object lockPreparacion = new Object();
    private final Object lockTransito = new Object();
    private final Object lockEntrega = new Object();
    private final Object lockVerificacion = new Object();
    private final Object lockFallido = new Object();

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
            return matrizDeCasilleros[index];
    }

    /**
     * Recorre la matriz de casilleros e incrementa la cantidad de casilleros fuera de servicio en el log
     * se usa una vez
     */
    public void refreshCantCasillerosFueraDeServicio() {
        for (int i = 0; i < CANT_CASILLEROS; i++) {
            if (matrizDeCasilleros[i].getEstado() == EstadoCasillero.FUERA_DE_SERVICIO) {
                log.incCantCasillerosFueraDeServicio();
            }
        }
    }
    //Usado por los preparadores de pedidos
    public Casillero getCasilleroAleatorio() {
        synchronized (lockMatrizCasillero) {
            boolean hayCasillerosVacios = false;
            //Verifica que haya algun casillero vacio
            for (int i = 0; i < CANT_CASILLEROS; i++){
                if(matrizDeCasilleros[i].getEstado()==EstadoCasillero.VACIO){
                    hayCasillerosVacios = true;
                    break;
                }
            }
            // Repite hasta encontrar un casillero vacío
            while (hayCasillerosVacios) {
                int posAleatoria = new Random().nextInt(CANT_CASILLEROS);
                // Verificar si el casillero está vacío
                if (getCasillero(posAleatoria).getEstado() == EstadoCasillero.VACIO) {
                    return getCasillero(posAleatoria);
                }
            }
            return null;
        }
    }
    //Usado por los despachadores de pedidos
    public Pedido getPedidoDeListaEnPreparacionAleatorio() {
        synchronized (lockPreparacion) {
            if (pedidosEnPreparacion.isEmpty()){
                return null;
            }
            int posAleatoria = new Random().nextInt(pedidosEnPreparacion.size());
            Pedido pedido = pedidosEnPreparacion.get(posAleatoria);
            pedidosEnPreparacion.remove(posAleatoria);
            return pedido;
        }
    }
    //Usado por los entregadores de pedidos
    public Pedido getPedidoDeListaEnTransitoAleatorio() {
        synchronized (lockTransito) {
            if (pedidosEnTransito.isEmpty()){
                return null;
            }
            int posAleatoria = new Random().nextInt(pedidosEnTransito.size());
            Pedido pedido = pedidosEnTransito.get(posAleatoria);
            pedidosEnTransito.remove(posAleatoria);
            return pedido;
        }
    }
    //Usado por los verificadores de pedidos
    public Pedido getPedidoDeListaEntregadosAleatorio() {
        synchronized (lockEntrega) {
            if (pedidosEntregados.isEmpty()){
                return null;
            }
            int posAleatoria = new Random().nextInt(pedidosEntregados.size());
            Pedido pedido = pedidosEntregados.get(posAleatoria);
            pedidosEntregados.remove(posAleatoria);
            return pedido;
        }
    }

    public Log getLog(){
            return log;
    }

    public void addPedidoEnPreparacion(Pedido pedido) {
        synchronized (lockPreparacion) {
            pedidosEnPreparacion.add(pedido);
        }
    }
    public void addPedidoEnTransito(Pedido pedido) {
        synchronized (lockTransito) {
            pedidosEnTransito.add(pedido);
        }
    }
    public void addPedidoEnEntregados(Pedido pedido) {
        synchronized (lockEntrega) {
            pedidosEntregados.add(pedido);
        }
    }
    public void addPedidoEnVerificados(Pedido pedido) {
        synchronized (lockVerificacion) {
            pedidosVerificados.add(pedido);
        }
    }
    public void addPedidoEnFallidos(Pedido pedido) {
        synchronized (lockFallido) {
            pedidosFallidos.add(pedido);
        }
    }
    public void removePedidoEnPreparacion(Pedido pedido) {
        synchronized (lockPreparacion) {
            pedidosEnPreparacion.remove(pedido);
        }
    }
    public void removePedidoEnTransito(Pedido pedido) {
        synchronized (lockTransito) {
            pedidosEnTransito.remove(pedido);
        }
    }
    public void removePedidoEnEntregados(Pedido pedido) {
        synchronized (lockEntrega) {
            pedidosEntregados.remove(pedido);
        }
    }


}
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

    private final Object lockMatrizCasillero = new Object();
    private final Object lockPreparacion = new Object();
    private final Object lockTransito = new Object();
    private final Object lockEntrega = new Object();
    private final Object lockVerificacion = new Object();
    private final Object lockFallido = new Object();

    public Sistema() {
        for (int i = 0; i < CANT_CASILLEROS; i++) {
            matrizDeCasilleros[i] = new Casillero(i);
        }
    }

    /**
     *
     * @param index: indice d
     * @return
     */
    public Casillero getCasillero(int index) {
        if (index < 0 || index >= CANT_CASILLEROS) {
            throw new IndexOutOfBoundsException("Índice de casillero inválido");
        }
            return matrizDeCasilleros[index];
    }

    //Usado por los preparadores de pedidos
    public boolean setPedidoEnCasilleroAleatorio(Pedido pedido) {
        synchronized (lockMatrizCasillero) {
            List<Integer> casillerosVacios = new ArrayList<>();

            // Buscar todos los casilleros vacíos
            for (int i = 0; i < CANT_CASILLEROS; i++) {
                if (matrizDeCasilleros[i].getEstado() == EstadoCasillero.VACIO) {
                    casillerosVacios.add(i);
                }
            }

            if (casillerosVacios.isEmpty()) {
                return false;
            }
            // Elije un casillero vacío al azar
            int posAleatoria = casillerosVacios.get(new Random().nextInt(casillerosVacios.size()));
            matrizDeCasilleros[posAleatoria].ocupar(pedido);
            return true;
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

    public boolean todosLosPedidosFinalizados() {
        if(pedidosEnPreparacion.isEmpty() && pedidosEnTransito.isEmpty() && pedidosEntregados.isEmpty()){
            return true;
        }
        return false;
    }

    public List<Pedido> getPedidosVerificados() {
        return pedidosVerificados;
    }
    public List<Pedido> getPedidosFallidos() {
        return pedidosFallidos;
    }

    public int getCantCasillerosFueraDeServicio() {
        int contadorCasilleros=0;
        for(int i=0;i<CANT_CASILLEROS;i++){
            if(matrizDeCasilleros[i].getEstado()==EstadoCasillero.FUERA_DE_SERVICIO){
                contadorCasilleros++;
            }
        }
        return contadorCasilleros;
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
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

    private final Object lockPreparacion = new Object();
    private final Object lockTransito = new Object();
    private final Object lockEntrega = new Object();
    private final Object lockVerificacion = new Object();
    private final Object lockFallido = new Object();
    private final Object lockLog = new Object();

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

    public List<Pedido> getListadoEnPreparacion() {
        synchronized (lockPreparacion) {
            return pedidosEnPreparacion;
        }
    }

    public Pedido getPedidoDeListaEnTransitoRemovidoAleatorio() {
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
    public int getPosicionCasilleroEnPreparacionAleatorio() {
        synchronized (lockPreparacion) {

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
                    return posAleatoria;
                }
            }
            // hayCasillerosVacios siempre va a ser false
            int aux = hayCasillerosVacios ? 0 : -1;
            return aux;
        }
    }


    public List<Pedido> getListadoEnTransito() {
        synchronized (lockTransito) {
            return pedidosEntregados;
        }
    }

    public List<Pedido> getListadoEntregados() {
        synchronized (lockEntrega) {
            return pedidosEntregados;
        }
    }

    public List<Pedido> getListadoVerificados() {
        synchronized (lockVerificacion) {
            return pedidosVerificados;
        }
    }

    public List<Pedido> getListadoFallidos() {
        synchronized (lockFallido) {
            return pedidosFallidos;
        }
    }

    public Log getLog(){
        synchronized (lockLog) {
            return log;
        }
    }
}
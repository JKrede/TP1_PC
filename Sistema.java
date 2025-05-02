import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Sistema {
    public static final int CANT_CASILLEROS = 200;
    private final Casillero[] matrizDeCasilleros = new Casillero[CANT_CASILLEROS];

    private final List<Pedido> pedidosEnPreparacion = Collections.synchronizedList(new ArrayList<>());
    private final List<Pedido> pedidosEnTransito = Collections.synchronizedList(new ArrayList<>());
    private final List<Pedido> pedidosEntregados = Collections.synchronizedList(new ArrayList<>());
    private final List<Pedido> pedidosVerificados = Collections.synchronizedList(new ArrayList<>());
    private final List<Pedido> pedidosFallidos = Collections.synchronizedList(new ArrayList<>());
    
    private final Log log;
    private final Random random = new Random();

    public Sistema(Log log) {
        this.log = log;
        for (int i = 0; i < CANT_CASILLEROS; i++) {
            matrizDeCasilleros[i] = new Casillero(i);
        }
    }

    public Casillero getCasillero(int index) {
        if (index < 0 || index >= CANT_CASILLEROS) {
            throw new IllegalArgumentException("Índice de casillero inválido");
        }
        return matrizDeCasilleros[index];
    }

    public int getPosicionCasilleroEnPreparacionAleatorio() {
        List<Integer> casillerosVacios = new ArrayList<>();
        for (int i = 0; i < CANT_CASILLEROS; i++) {
            if(matrizDeCasilleros[i].getEstado() == EstadoCasillero.VACIO){
                casillerosVacios.add(i);
            }
        }
        return casillerosVacios.isEmpty() ? -1 : 
               casillerosVacios.get(random.nextInt(casillerosVacios.size()));
    }

    public Pedido getPedidoListadoEnPreparacionRemovidoAleatorio() {
        synchronized (pedidosEnPreparacion) {
            if (pedidosEnPreparacion.isEmpty()) return null;
            return pedidosEnPreparacion.remove(random.nextInt(pedidosEnPreparacion.size()));
        }
    }

    public Pedido getPedidoDeListaEnTransitoRemovidoAleatorio() {
        synchronized (pedidosEnTransito) {
            if (pedidosEnTransito.isEmpty()) return null;
            return pedidosEnTransito.remove(random.nextInt(pedidosEnTransito.size()));
        }
    }

    public Pedido getPedidoDeListaEntregadosRemovidoAleatorio() {
        synchronized (pedidosEntregados) {
            if (pedidosEntregados.isEmpty()) return null;
            return pedidosEntregados.remove(random.nextInt(pedidosEntregados.size()));
        }
    }

    public List<Pedido> getListadoEnPreparacion() {
        return new ArrayList<>(pedidosEnPreparacion);
    }

    public List<Pedido> getListadoEnTransito() {
        return new ArrayList<>(pedidosEnTransito);
    }

    public List<Pedido> getListadoEntregados() {
        return new ArrayList<>(pedidosEntregados);
    }

    public List<Pedido> getListadoVerificados() {
        return new ArrayList<>(pedidosVerificados);
    }

    public List<Pedido> getListadoFallidos() {
        return new ArrayList<>(pedidosFallidos);
    }

    public Log getLog() {
        return log;
    }

    public void refreshCantCasillerosFueraDeServicio() {
        int count = 0;
        for (Casillero c : matrizDeCasilleros) {
            if (c.getEstado() == EstadoCasillero.FUERA_DE_SERVICIO) {
                count++;
            }
        }
        log.setCantCasillerosFueraDeServicio(count);
    }
}
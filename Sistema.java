import java.util.ArrayList;

public class Sistema {
    //Matriz de casilleros
    private final Casillero[] matrizDeCasilleros = new Casillero[200];

    //Listados de pedidos en distintos estado
    private final ArrayList<Pedido> pedidosEnPreparacion = new ArrayList<>();
    private final ArrayList<Pedido> pedidosEnTransito = new ArrayList<>();
    private final ArrayList<Pedido> pedidosEntregados = new ArrayList<>();
    private final ArrayList<Pedido> pedidosFallidos = new ArrayList<>();

    // Lock para secciones criticas dentro de Sistema
    private final Object lock = new Object();

    public Sistema() {
        // Creacion de casilleros con el id por posicion que ocupa en la matriz
        for (int i = 0; i < 200; i++) {
            matrizDeCasilleros[i] = new Casillero(i);
        }
    }
    // Accede al casillero mediante el indice
    public Casillero getCasillero(int index) {
        if (index < 0 || index >= 200) {
            throw new IllegalArgumentException("Índice de casillero inválido");
        }
        synchronized (lock) {
            return matrizDeCasilleros[index];
        }
    }

    // Agrega el pedido en preparacion a la lista de pedidos en preparacion
    public void moverPedidoAPreparacion(Pedido pedido) {
        synchronized (lock) {
            pedidosEnPreparacion.add(pedido);
        }
    }
    //Verifica que el pedido este en la lista de pedidos en preparacion y lo mueve a pedidos en transito
    public void moverPedidoATransito(Pedido pedido) {
        synchronized (lock) {
            if (pedidosEnPreparacion.remove(pedido)) {
                pedidosEnTransito.add(pedido);
            }
        }
    }
    //Verifica que el pedido este en la lista de pedidos en transito y lo mueve a pedidos entregados
    public void moverPedidoAEntregados(Pedido pedido) {
        synchronized (lock) {
            if (pedidosEnTransito.remove(pedido)) {
                pedidosEntregados.add(pedido);
            }
        }
    }
    // Agrega el pedido en preparacion a la lista de pedidos en preparacion
    public void moverPedidoAFallidos(Pedido pedido) {
        synchronized (lock) {
            //Un pedido puede resultar fallido desde cualquier lista
            pedidosFallidos.add(pedido);
        }
    }


}
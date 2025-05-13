import java.util.ArrayList;
import java.util.List;

public class Main {

    static final int CANT_PEDIDOS = 500;

    public static void main(String[] args){

        List<Pedido> listaPedidos = new ArrayList<>();
        SistemaDeLogistica sistema = new SistemaDeLogistica();
        Log log = new Log(sistema);

        for (int i = 0; i < CANT_PEDIDOS; i++) {
            Pedido pedido = new Pedido(i);
            listaPedidos.add(pedido);
        }

        PreparadorDePedidos preparadorDePedidos = new PreparadorDePedidos(sistema, listaPedidos);
        DespachadorDePedidos despachadorDePedidos = new DespachadorDePedidos(sistema);
        EntregadorDePedido entregadorDePedidos = new EntregadorDePedido(sistema);
        VerificadorDePedido verificadorDePedidos = new VerificadorDePedido(sistema);

        Thread preparadorDePedidos1 = new Thread(preparadorDePedidos); //Hilo 1
        Thread preparadorDePedidos2 = new Thread(preparadorDePedidos); //Hilo 2
        Thread preparadorDePedidos3 = new Thread(preparadorDePedidos); //Hilo 3

        Thread despachadorDePedidos1 = new Thread(despachadorDePedidos); //Hilo 4
        Thread despachadorDePedidos2 = new Thread(despachadorDePedidos); //Hilo 5

        Thread entregadorDePedidos1 = new Thread(entregadorDePedidos); //Hilo 6
        Thread entregadorDePedidos2 = new Thread(entregadorDePedidos); //Hilo 7
        Thread entregadorDePedidos3 = new Thread(entregadorDePedidos); //Hilo 8

        Thread verificadorDePedidos1 = new Thread(verificadorDePedidos); //Hilo 9
        Thread verificadorDePedidos2 = new Thread(verificadorDePedidos); //Hilo 10

        Thread historial = new Thread(log); //Hilo auxiliar

        historial.start();
        preparadorDePedidos1.start();
        preparadorDePedidos2.start();
        preparadorDePedidos3.start();

        despachadorDePedidos1.start();
        despachadorDePedidos2.start();

        entregadorDePedidos1.start();
        entregadorDePedidos2.start();
        entregadorDePedidos3.start();

        verificadorDePedidos1.start();
        verificadorDePedidos2.start();

    }
}
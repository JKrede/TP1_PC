import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        List<Pedido> listaPedidos = new ArrayList<>();

        Log log = new Log();

        Sistema sistema = new Sistema(log);

        Thread preparadorDePedidos1 = new Thread(new PreparadorDePedidos(sistema)); //Hilo 1
        Thread preparadorDePedidos2 = new Thread(new PreparadorDePedidos(sistema)); //Hilo 2
        Thread preparadorDePedidos3 = new Thread(new PreparadorDePedidos(sistema)); //Hilo 3

        Thread despachadorDePedidos1 = new Thread(new DespachadorDePedidos(sistema)); //Hilo 4
        Thread despachadorDePedidos2 = new Thread(new DespachadorDePedidos(sistema)); //Hilo 5

        Thread entregadorDePedidos1 = new Thread(new EntregadorDePedido(sistema)); //Hilo 6
        Thread entregadorDePedidos2 = new Thread(new EntregadorDePedido(sistema)); //Hilo 7
        Thread entregadorDePedidos3 = new Thread(new EntregadorDePedido(sistema)); //Hilo 8

        Thread verificadorDePedidos1 = new Thread(new VerificadorDePedido(sistema)); //Hilo 9
        Thread verificadorDePedidos2 = new Thread(new VerificadorDePedido(sistema)); //Hilo 10

        // Crea 500 pedidos
        for (int i = 0; i < 500; i++) {
            Pedido pedido = new Pedido();
            listaPedidos.add(pedido);
        }



    }
}

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        List<Pedido> listaPedidos = new ArrayList<>();

        Log log = new Log();

        Sistema sistema = new Sistema(log);

        PreparadorDePedidos preparadorDePedidos1 = new PreparadorDePedidos(sistema); //Hilo 1
        PreparadorDePedidos preparadorDePedidos2 = new PreparadorDePedidos(sistema); //Hilo 2
        PreparadorDePedidos preparadorDePedidos3 = new PreparadorDePedidos(sistema); //Hilo 3

        Thread despachadorDePedidos1 = new Thread(new DespachadorDePedidos(sistema)); //Hilo 4
        Thread despachadorDePedidos2 = new Thread(new DespachadorDePedidos(sistema)); //Hilo 5

        Thread entregadorDePedidos1 = new Thread(new EntregadorDePedido(sistema)); //Hilo 6
        Thread entregadorDePedidos2 = new Thread(new EntregadorDePedido(sistema)); //Hilo 7
        Thread entregadorDePedidos3 = new Thread(new EntregadorDePedido(sistema)); //Hilo 8

        Thread verificadorDePedidos1 = new Thread(new VerificadorDePedido(sistema)); //Hilo 9
        Thread verificadorDePedidos2 = new Thread(new VerificadorDePedido(sistema)); //Hilo 10


        // Crea 500 pedidos
        for (int i = 0; i < 100; i++) {
            Pedido pedido = new Pedido();
            listaPedidos.add(pedido);
        }
        for(int i = 0; i < 96; i++){
            preparadorDePedidos1.setPedido(listaPedidos.get(i));
            preparadorDePedidos2.setPedido(listaPedidos.get(i+1));
            preparadorDePedidos3.setPedido(listaPedidos.get(i+2));
        }
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
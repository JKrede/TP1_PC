
import java.util.Random;


class PreparacionPedidoProceso extends Proceso {
    private Random random = new Random();
    private GeneradorPedidos generadorPedidos;

    public PreparacionPedidoProceso(Sistema sistema, long demoraIteracion, GeneradorPedidos generadorPedidos) {
        super(sistema, demoraIteracion, "Preparacion");
        this.generadorPedidos = generadorPedidos;
    }

    protected void realizarTrabajo() throws InterruptedException {
        Pedido pedido = sistema.obtenerPedidoEnPreparacion();
        if (pedido != null) {
            int intentos = 0;
            while (intentos < 5) {
                int indiceCasillero = random.nextInt(200); // Obtiene un indice de casillero aleatorio
                Casillero casillero = sistema.getCasillero(indiceCasillero); // Obtiene el casillero

                if (casillero.getEstado() == EstadoCasillero.VACIO) {
                    casillero.setEstado(EstadoCasillero.OCUPADO);
                    sistema.moverPedidoATransito(pedido); 
                    return;
                } else {
                    intentos++;
                }
            }
            //Si despues de los 5 intentos no encuentra un vacio, falla
            sistema.moverPedidoAFallidos(pedido);
        }
    }
}

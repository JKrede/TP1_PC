
import java.util.Random;


class PreparacionPedidoProceso extends Proceso {
    private Random random = new Random();
    private GeneradorPedidos generadorPedidos;

    public PreparacionPedidoProceso(Sistema sistema, long demoraIteracion, GeneradorPedidos generadorPedidos) {
        super(sistema, demoraIteracion, "Preparacion");
        this.generadorPedidos = generadorPedidos;
    }

    protected void realizarTrabajo() throws InterruptedException {
        Pedido pedido = sistema.obtenerPedidoEnPreparacion(); // Obtiene un pedido para preparar
        if (pedido != null) {
            int intentos = 0;
            while (intentos < 5) { // Numero maximo de intentos para encontrar un casillero vacio
                int indiceCasillero = random.nextInt(200); // Obtiene un indice de casillero aleatorio
                Casillero casillero = sistema.getCasillero(indiceCasillero); // Obtiene el casillero

                if (casillero.getEstado() == EstadoCasillero.VACIO) {
                    // Marca el casillero como ocupado, registra el pedido y sale del bucle
                    casillero.setEstado(EstadoCasillero.OCUPADO);
                    sistema.moverPedidoATransito(pedido); // Mueve el pedido a la lista de transito
                  //FALTA REGISTRAR EL PEDIDO 
                    return;
                } else {
                    intentos++;
                  //ACA TAMBIEN MARCAR QUE ESTA OCUPADO EL CASILLERO EN LOG
                }
            }
            //Si despues de los 5 intentos no encuentra un vacio, falla
            sistema.moverPedidoAFallidos(pedido);
          //FALTARIA REGISTRAR EL PEDIDO COMO FALLIDO
        }
    }
}


import java.util.Random;


class DespachoPedidoProceso extends Proceso {
    private Random random = new Random();

    public DespachoPedidoProceso(Sistema sistema, long demoraIteracion) {
        super(sistema, demoraIteracion, "Despacho");
    }

    @Override
    protected void realizarTrabajo() throws InterruptedException {
        Pedido pedido = sistema.obtenerPedidoEnTransito();
        if (pedido != null) {
            int intentos = 0;
            while (intentos < 5) {
                int indiceCasillero = random.nextInt(200);
                Casillero casillero = sistema.getCasillero(indiceCasillero);
                if (casillero.getEstado() == EstadoCasillero.OCUPADO) { // Verifica que el casillero esté ocupado
                    casillero.setEstado(EstadoCasillero.VACIO); // Simula la liberación del casillero
                    sistema.moverPedidoAEntregados(pedido);
                  //FALTA REGISTRAR EL LOG (DESPACHADO)
                    return;
                } else {
                    intentos++;
                  //REGISTRAR EL CASILLERO COMO OCUPADO
                }
            }
            sistema.moverPedidoAFallidos(pedido);
          //REGISTRAR QUE NO SE PUDO DESPACHAR EL PEDIDO
        }
    }
}

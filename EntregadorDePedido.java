import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class EntregadorDePedido implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(EntregadorDePedido.class.getName());
    private static final double PROBABILIDAD_EXITO = 0.90;
    private static final Random RANDOM = new Random();

    private final Sistema sistema;
    private final int delayMin;
    private final int delayMax;

    public EntregadorDePedido(Sistema sistema, int delayMin, int delayMax) {
        this.sistema = sistema;
        this.delayMin = delayMin;
        this.delayMax = delayMax;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                entregarPedido();
                esperarTiempoAleatorio();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.info("Hilo entregador interrumpido");
            }
        }
    }

    private void entregarPedido() {
        List<Pedido> pedidosEnTransito = sistema.pedidosEnTransito;

        synchronized (pedidosEnTransito) {
            if (pedidosEnTransito.isEmpty()) {
                LOGGER.fine("No hay pedidos en tránsito para entregar");
                return;
            }

            Pedido pedido = pedidosEnTransito.get(RANDOM.nextInt(pedidosEnTransito.size()));
            procesarPedido(pedido);
        }
    }

    private void procesarPedido(Pedido pedido) {
        synchronized (pedido) {
            boolean exito = RANDOM.nextDouble() < PROBABILIDAD_EXITO;

            if (exito) {
                // Éxito en la entrega (90% de probabilidad)
                pedido.setEstado("ENTREGADO");
                sistema.pedidosEnTransito.remove(pedido);
                sistema.pedidosEntregados.add(pedido);
                LOGGER.info("Pedido " + pedido.getId() + " entregado exitosamente");

                // Liberar el casillero si está asignado
                if (pedido.getCasilleroAsignado() != null) {
                    pedido.getCasilleroAsignado().setEstado(EstadoCasillero.VACIO);
                }
            } else {
                // Falla en la entrega (10% de probabilidad)
                pedido.setEstado("FALLIDO");
                sistema.pedidosEnTransito.remove(pedido);
                sistema.pedidosFallidos.add(pedido);
                LOGGER.warning("Pedido " + pedido.getId() + " falló en la entrega");

                // Marcar casillero como fuera de servicio si está asignado
                if (pedido.getCasilleroAsignado() != null) {
                    pedido.getCasilleroAsignado().setEstado(EstadoCasillero.FUERA_DE_SERVICIO);
                }
            }
        }
    }

    private void esperarTiempoAleatorio() throws InterruptedException {
        int delay = delayMin + RANDOM.nextInt(delayMax - delayMin + 1);
        TimeUnit.MILLISECONDS.sleep(delay);
    }
}
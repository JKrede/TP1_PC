public class PreparadorDePedidos extends Thread {
    private final Sistema sistema;
    private final int duracion = 100;
    private Pedido pedido;
    private final Object lock = new Object();

    public PreparadorDePedidos(Sistema sistema) {
        this.sistema = sistema;
    }

    public void setPedido(Pedido pedido) {
        synchronized (lock) {
            this.pedido = pedido;
            lock.notify();
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                synchronized (lock) {
                    while (pedido == null && !Thread.currentThread().isInterrupted()) {
                        lock.wait(50);
                    }
                }

                if (Thread.currentThread().isInterrupted()) break;

                int posCasillero;
                while ((posCasillero = sistema.getPosicionCasilleroEnPreparacionAleatorio()) == -1) {
                    Thread.sleep(50);
                    if (Thread.currentThread().isInterrupted()) return;
                }

                Casillero casillero = sistema.getCasillero(posCasillero);
                synchronized (casillero) {
                    if (casillero.getEstado() == EstadoCasillero.VACIO) {
                        pedido.setEstado(EstadoPedido.EN_PREPARACION);
                        casillero.ocupar(pedido);
                        sistema.getListadoEnPreparacion().add(pedido);
                        synchronized (lock) {
                            pedido = null;
                        }
                    }
                }

                Thread.sleep(duracion);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    private int cantPedidosVerificados = 0;
    private int cantPedidosFallidos = 0;
    private int cantCasillerosFueraDeServicio = 0;

    public synchronized void incCantPedidosVerificados() {
        cantPedidosVerificados++;
        escribirLog("Pedido verificado. Total: " + cantPedidosVerificados);
    }

    public synchronized void incCantPedidosFallidos() {
        cantPedidosFallidos++;
        escribirLog("Pedido fallido. Total: " + cantPedidosFallidos);
    }

    public synchronized void setCantCasillerosFueraDeServicio(int cantidad) {
        cantCasillerosFueraDeServicio = cantidad;
        escribirLog("Casilleros fuera de servicio: " + cantidad);
    }

    public synchronized void mostrarResumen() {
        String resumen = "\n=== RESUMEN FINAL ===" +
                       "\nPedidos verificados: " + cantPedidosVerificados +
                       "\nPedidos fallidos: " + cantPedidosFallidos +
                       "\nCasilleros fuera de servicio: " + cantCasillerosFueraDeServicio;
        
        System.out.println(resumen);
        escribirLog(resumen);
    }

    private void escribirLog(String mensaje) {
        try (FileWriter writer = new FileWriter("log_sistema.txt", true)) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.write("[" + timestamp + "] " + mensaje + "\n");
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de log: " + e.getMessage());
        }
    }
}
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log {

    static final int INTERVALO_DE_CAPTURA = 200; // milisegundos
    private int cantPedidosVerificados;
    private int cantPedidosFallidos;
    private final int cantCasilleros = Sistema.CANT_CASILLEROS;
    private int cantCasillerosFueraDeServicio;
    private long tiempoInicio = System.currentTimeMillis();

    FileWriter archivo = null;
    PrintWriter escritor = null;

    public Log() {
        cantPedidosVerificados = 0;
        cantPedidosFallidos = 0;
        cantCasillerosFueraDeServicio = 0;
    }

    public void incCantPedidosVerificados(){
        cantPedidosVerificados++;
    }

    public void incCantPedidosFallidos(){
        cantPedidosFallidos++;
    }

    public void incCantCasillerosFueraDeServicio(){
        cantCasillerosFueraDeServicio++;
    }

    public String getFecha() {
        Calendar calendario = Calendar.getInstance();
        SimpleDateFormat fecha = new SimpleDateFormat("dd/MM HH:mm:ss");
        return fecha.format(calendario.getTime());
    }

    public String getHora() {
        Calendar calendario = Calendar.getInstance();
        SimpleDateFormat hora = new SimpleDateFormat("HH:mm:ss:SSS");
        return hora.format(calendario.getTime());
    }

    public void crearArchivo() throws IOException {
        String fecha = this.getFecha().replace("/", "-").replace(":", "_");
        archivo = new FileWriter("Log_" + fecha + ".txt");
        escritor = new PrintWriter(archivo);
    }

    public void escribirHistorial() {

            if (escritor == null) {
                throw new IllegalStateException("Archivo no inicializado");
            }
            escritor.println(getHora());
            escritor.println("Pedidos fallidos: " + cantPedidosFallidos);
            escritor.println("Pedidos verificados: " + cantPedidosVerificados);
    }

    public void escribirFinalHistorial() {
        if (escritor == null) {
            throw new IllegalStateException("Archivo no inicializado.");
        }
        long tiempoFinal = System.currentTimeMillis();
        double tiempoTotal = (tiempoFinal - tiempoInicio) / 1000.0;

        escritor.println("Casilleros disponibles: " + (cantCasilleros - cantCasillerosFueraDeServicio));
        escritor.println("Casilleros fuera de servicio: " + cantCasillerosFueraDeServicio);
        escritor.println("Tiempo total de ejecución: "+ tiempoTotal+" segundos");

        escritor.close();
    }

}
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log implements Runnable {

    static final int INTERVALO_DE_CAPTURA = 200; // milisegundos
    private Sistema sistema;
    private long tiempoInicio = System.currentTimeMillis();
    private int contadorLog = 0;
    private boolean archivoCreado = false;
    private boolean procesamientoFinalizados = false;
    private FileWriter archivo = null;
    private PrintWriter escritor = null;

    public Log(Sistema sistema) {
        this.sistema = sistema;
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

    public void crearArchivo()  {
        try{
                String fecha = this.getFecha().replace("/", "-").replace(":", "_");
                archivo = new FileWriter("Log_" + fecha + ".txt");
                escritor = new PrintWriter(archivo, true);
        } catch (IOException e) {
                System.out.println("algo salio mal: "+e.getMessage());
        }
    }

    public void escribirHistorial() {
        if (escritor == null) {
            throw new IllegalStateException("Archivo no inicializado");
        }
        escritor.println(getHora());
        escritor.println("Pedidos verificados: " + sistema.getPedidosVerificados().size());
        escritor.println("Pedidos fallidos: " + sistema.getPedidosFallidos().size());
        escritor.flush();
    }

    public void escribirFinalHistorial() {
        if (escritor == null) {
            throw new IllegalStateException("Archivo no inicializado.");
        }
        long tiempoFinal = System.currentTimeMillis();
        double tiempoTotal = (tiempoFinal - tiempoInicio) / 1000.0;

        escritor.println(" ");
        escritor.println("Estadisticas de ejecucion: ");
        escritor.println("Casilleros disponibles: " + (Sistema.CANT_CASILLEROS - sistema.getCantCasillerosFueraDeServicio()));
        escritor.println("Casilleros fuera de servicio: " + sistema.getCantCasillerosFueraDeServicio());
        escritor.println("Tiempo total de ejecución: "+ tiempoTotal+" segundos");
        escritor.println(" ");
        for(int i = 0; i < Sistema.CANT_CASILLEROS; i++){
            escritor.println("Casilleros "+i+" ocupado "+sistema.getCasillero(i).getVecesOcupado()+" veces");
        }
        try {
            escritor.close();
            if (archivo != null) {
                archivo.close();
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar el archivo de log: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if(!archivoCreado){
                    crearArchivo();
                    archivoCreado = true;
                }
                if(!procesamientoFinalizados){
                    escribirHistorial();
                    contadorLog++;
                    procesamientoFinalizados = sistema.todosLosPedidosFinalizados();
                    Thread.sleep(INTERVALO_DE_CAPTURA);
                }
                else{
                    escribirFinalHistorial();
                    Thread.currentThread().interrupt();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
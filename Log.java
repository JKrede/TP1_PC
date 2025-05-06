import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log implements Runnable {

    static final int INTERVALO_DE_CAPTURA = 200;
    private Sistema sistema;
    private long tiempoInicio = System.currentTimeMillis();
    private boolean archivoCreado = false;
    private boolean procesamientoFinalizados = false;
    private FileWriter archivo = null;
    private PrintWriter escritor = null;

    /**
     * Constructor de la clase Log.
     *
     * @param sistema Sistema principal del que se obtiene la información de los pedidos.
     * @throws NullPointerException si el sistema proporcionado es null.
     */
    public Log(Sistema sistema) {
        this.sistema = sistema;
    }

    /**
     * Obtiene la fecha actual formateada.
     *
     * @return La fecha actual como cadena en formato "dd/MM HH:mm:ss".
     */
    public String getFecha() {
        Calendar calendario = Calendar.getInstance();
        SimpleDateFormat fecha = new SimpleDateFormat("dd/MM HH:mm:ss");
        return fecha.format(calendario.getTime());
    }

    /**
     * Obtiene la hora actual con mayor detalle.
     *
     * @return La hora actual como cadena en formato "HH:mm:ss:SSS".
     */
    public String getHora() {
        Calendar calendario = Calendar.getInstance();
        SimpleDateFormat hora = new SimpleDateFormat("HH:mm:ss:SSS");
        return hora.format(calendario.getTime());
    }

    /**
     * Crea un archivo de log con nombre basado en la fecha actual.
     *
     * @throws IOException Si ocurre un error al crear el archivo.
     */
    public void crearArchivo() {
        try {
            String fecha = this.getFecha().replace("/", "-").replace(":", "_");
            archivo = new FileWriter("Log_" + fecha + ".txt");
            escritor = new PrintWriter(archivo, true);
        } catch (IOException e) {
            System.out.println("Algo salió mal al crear el archivo: " + e.getMessage());
        }
    }

    /**
     * Escribe en el log el estado actual del sistema, incluyendo la cantidad
     * de pedidos verificados y pedidos fallidos.
     *
     * @throws IllegalStateException Si el archivo no ha sido inicializado.
     */
    public void escribirHistorial() {
        if (escritor == null) {
            throw new IllegalStateException("Archivo no inicializado");
        }
        escritor.println(getHora());
        escritor.println("Pedidos verificados: " + sistema.getPedidosVerificados().size());
        escritor.println("Pedidos fallidos: " + sistema.getPedidosFallidos().size());
        escritor.println(" ");
        escritor.flush();
    }

    /**
     * Escribe en el log estadísticas finales del sistema, como el tiempo total 
     * de ejecución, número de casilleros disponibles y una lista del estado 
     * de los casilleros.
     *
     * @throws IllegalStateException Si el archivo no ha sido inicializado.
     */
    public void escribirFinalHistorial() {
        if (escritor == null) {
            throw new IllegalStateException("Archivo no inicializado.");
        }
        long tiempoFinal = System.currentTimeMillis();
        double tiempoTotal = (tiempoFinal - tiempoInicio) / 1000.0;
        double tiempoTotalRedondeado = Math.round(tiempoTotal * 100) / 100.0;
        escritor.println("Estadísticas de ejecución: ");
        escritor.println("Casilleros disponibles: " + (Sistema.CANT_CASILLEROS - sistema.getCantCasillerosFueraDeServicio()));
        escritor.println("Casilleros fuera de servicio: " + sistema.getCantCasillerosFueraDeServicio());
        escritor.println("Tiempo total de ejecución: " + tiempoTotalRedondeado + " segundos");
        escritor.println(" ");
        for (int i = 0; i < Sistema.CANT_CASILLEROS; i++) {
            escritor.println("Casillero " + i + " ocupado " + sistema.getCasillero(i).getVecesOcupado() + " veces, estado: " + sistema.getCasillero(i).getEstado());
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

    /**
     * Ejecuta el proceso de registro en un hilo independiente.
     * Mientras el hilo no sea interrumpido, toma capturas periódicas del estado del sistema
     * y finaliza escribiendo un resumen de estadísticas al finalizar el procesamiento.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (!archivoCreado) {
                    crearArchivo();
                    archivoCreado = true;
                }
                if (!procesamientoFinalizados) {
                    escribirHistorial();
                    procesamientoFinalizados = sistema.todosLosPedidosFinalizados();
                    Thread.sleep(INTERVALO_DE_CAPTURA);
                } else {
                    escribirFinalHistorial();
                    Thread.currentThread().interrupt();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;

/**
 * Clase que gestiona el registro de información del sistema en un archivo de log.
 * Se encarga de capturar el estado del sistema periódicamente y guardar estadísticas
 * de pedidos y casilleros en un archivo de texto.
 * 
 * Implementa la interfaz {@link Runnable} para ejecutarse en un hilo independiente.
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public class Log implements Runnable {

    /** Intervalo de captura en milisegundos. */
    static final int INTERVALO_DE_CAPTURA = 200;

    /** Sistema principal que contiene la información sobre pedidos y casilleros. */
    private Sistema sistema;

    /** Tiempo de inicio de la ejecución del log. */
    private long tiempoInicio = System.currentTimeMillis();

    /** Contador para las capturas registradas. */
    private int contadorLog = 0;

    /** Indica si el archivo de log ya fue creado. */
    private boolean archivoCreado = false;

    /** Indica si se han finalizado todos los procesos de pedidos. */
    private boolean procesamientoFinalizados = false;

    /** Referencia al archivo de log. */
    private FileWriter archivo = null;

    /** Objeto para escribir texto en el archivo de log. */
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
        escritor.flush();
    }

    //Este metodo busca los casilleros o el casillero con mas ocupaciones, ademas que tambien nos dice su estado

    public void ImprimirCasilleroMasOcupado() {
        ArrayList<Casillero> casillerosMasOcupados = new ArrayList<>();
        int maxOcupaciones = -1;
    
        // Buscar la mayor cantidad de ocupaciones y los casilleros que la tienen
        for (int i = 0; i < Sistema.CANT_CASILLEROS; i++) {
            Casillero actual = sistema.getCasillero(i);
            int ocupaciones = actual.getVecesOcupado();
    
            if (ocupaciones > maxOcupaciones) {
                maxOcupaciones = ocupaciones;
                casillerosMasOcupados.clear();
                casillerosMasOcupados.add(actual);
            } else if (ocupaciones == maxOcupaciones) {
                casillerosMasOcupados.add(actual);
            }
        }
    
        // Imprimir el resultado
        if (casillerosMasOcupados.size() == 1) {
            Casillero c = casillerosMasOcupados.get(0);
            if (c.getEstado() == EstadoCasillero.FUERA_DE_SERVICIO) {
                escritor.println("El casillero más ocupado es el " + c.getId() +
                    " con " + c.getVecesOcupado() + " ocupaciones, pero está fuera de servicio.");
            } else if (c.getEstado() == EstadoCasillero.VACIO) {
                escritor.println("El casillero más ocupado es el " + c.getId() +
                    " con " + c.getVecesOcupado() + " ocupaciones, pero está vacío.");
            } else
            escritor.println("El casillero más ocupado es el " + c.getId() +
                " con " + c.getVecesOcupado() + " ocupaciones.");
        } else {
            escritor.println("Los casilleros más ocupados son:");
            for (Casillero c : casillerosMasOcupados) {
                if (c.getEstado() == EstadoCasillero.FUERA_DE_SERVICIO) {
                    escritor.println("- Casillero " + c.getId() + " con " +
                        c.getVecesOcupado() + " ocupaciones, pero está fuera de servicio.");
                } else if (c.getEstado() == EstadoCasillero.VACIO) {
                    escritor.println("- Casillero " + c.getId() + " con " +
                        c.getVecesOcupado() + " ocupaciones, pero está vacío.");
                } else
                escritor.println("- Casillero " + c.getId() + " con " +
                    c.getVecesOcupado() + " ocupaciones.");
            }
        }
    }

    //Este metodo busca los casilleros o el casillero con menos ocupaciones, ademas que tambien nos dice su estado

    public void ImprimirCasilleroMenosOcupado() {
        ArrayList<Casillero> casillerosMenosOcupados = new ArrayList<>();
        int minOcupaciones = Integer.MAX_VALUE;
    
        // Buscar la menor cantidad de ocupaciones y los casilleros que la tienen
        for (int i = 0; i < Sistema.CANT_CASILLEROS; i++) {
            Casillero actual = sistema.getCasillero(i);
            int ocupaciones = actual.getVecesOcupado();
    
            if (ocupaciones < minOcupaciones) {
                minOcupaciones = ocupaciones;
                casillerosMenosOcupados.clear();
                casillerosMenosOcupados.add(actual);
            } else if (ocupaciones == minOcupaciones) {
                casillerosMenosOcupados.add(actual);
            }
        }
    
        // Imprimir el resultado
        if (casillerosMenosOcupados.size() == 1) {
            Casillero c = casillerosMenosOcupados.get(0);
            if (c.getEstado() == EstadoCasillero.FUERA_DE_SERVICIO) {
                escritor.println("El casillero menos ocupado es el " + c.getId() +
                    " con " + c.getVecesOcupado() + " ocupaciones, pero está fuera de servicio.");
            } else if (c.getEstado() == EstadoCasillero.VACIO) {
                escritor.println("El casillero menos ocupado es el " + c.getId() +
                    " con " + c.getVecesOcupado() + " ocupaciones, pero está vacío.");
            } else {
                escritor.println("El casillero menos ocupado es el " + c.getId() +
                    " con " + c.getVecesOcupado() + " ocupaciones.");
            }
        } else {
            escritor.println("Los casilleros menos ocupados son:");
            for (Casillero c : casillerosMenosOcupados) {
                if (c.getEstado() == EstadoCasillero.FUERA_DE_SERVICIO) {
                    escritor.println("- Casillero " + c.getId() + " con " +
                        c.getVecesOcupado() + " ocupaciones, pero está fuera de servicio.");
                } else if (c.getEstado() == EstadoCasillero.VACIO) {
                    escritor.println("- Casillero " + c.getId() + " con " +
                        c.getVecesOcupado() + " ocupaciones, pero está vacío.");
                } else {
                    escritor.println("- Casillero " + c.getId() + " con " +
                        c.getVecesOcupado() + " ocupaciones.");
                }
            }
        }
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

        escritor.println(" ");
        escritor.println("Estadísticas de ejecución: ");
        escritor.println("Casilleros disponibles: " + (Sistema.CANT_CASILLEROS - sistema.getCantCasillerosFueraDeServicio()));
        escritor.println("Casilleros fuera de servicio: " + sistema.getCantCasillerosFueraDeServicio());
        escritor.println("Tiempo total de ejecución: " + tiempoTotal + " segundos");
        escritor.println(" ");
        for (int i = 0; i < Sistema.CANT_CASILLEROS; i++) {
            escritor.println("Casillero " + sistema.getCasillero(i).getId() + " ocupado " + sistema.getCasillero(i).getVecesOcupado() + " veces");
        }
        ImprimirCasilleroMenosOcupado();
        ImprimirCasilleroMasOcupado();
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
                    contadorLog++;
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
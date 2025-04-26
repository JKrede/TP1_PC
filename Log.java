public class Log {
    private Sistema sistema;
    private long tiempoTotalDemora;

    public void setTiempoTotalDemora(long tiempoTotalDemora){this.tiempoTotalDemora = tiempoTotalDemora}
    public Log(Sistema sistema){this.sistema = sistema}


    public void imprimirHistorial(){
        int pedidosVerificados = sistema.getPedidosVerificados().size();
        int pedidosFallidos = sistema.getPedidosFallidos().size();
        int casillerosVacios = 0;
        int casillerosOcupados = 0;

        for(int i =0; i < 200; i++){
            Casillero casillero = sistema.getCasillero(i);
            if(casillero.getEstado() == EstadoCasillero.VACIO){
                casillerosVacios = casillerosOcupados + 1;
            }
            else if (casillero.getEstado() == EstadoCasillero.OCUPADO){
                casillerosOcupados = casillerosOcupados + 1;
            }
        }
        System.out.println("Cantidad de pedidos Verificados: " + pedidosVerificados);
        System.out.println("Cantidad de pedidos Fallidos: " + pedidosFallidos);
        System.out.println("Cantidad de casilleros Ocupados: " + casillerosOcupados);
        System.out.println("Cantidad de casilleros Vacios: " + casillerosVacios);
        System.out.println("Tiempo total de demora: " + tiempoTotalDemora);
    }
}

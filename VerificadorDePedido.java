public class VerificadorDePedido {
    private String pedidoId;

    // Constructor: Recibe el ID del pedido a verificar
    public VerificadorDePedido(String pedidoId) {
        this.pedidoId = pedidoId;
    }

    // Verifica si el pedido está listo para entrega
    public boolean verificarDisponibilidad() {
        // Lógica simulada: Consulta una base de datos o API
        System.out.println("Verificando disponibilidad del pedido " + pedidoId);
        return Math.random() > 0.2; // Simula un 80% de éxito
    }

    // Valida la dirección de entrega
    public boolean validarDireccion(String direccion) {
        return direccion != null && !direccion.trim().isEmpty();
    }
}
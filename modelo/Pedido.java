package Hackathon.modelo;

public class Pedido {
    private final int numero;
    private final Cliente cliente;
    private final String descripcion;
    private final boolean tamaño;
    private final double kilometros;
    private final Repartidor repartidor;
    private final double costoDomicilio;
    private final double tiempoMinutos;
    private String estado;

    public Pedido(int numero, Cliente cliente, String descripcion, boolean tamaño, double kilometros, Repartidor repartidor, double costoDomicilio, double tiempoMinutos) {
        if (numero < 1) {
            throw new IllegalArgumentException("El número del pedido debe ser positivo.");
        }
        if (cliente == null) {
            throw new IllegalArgumentException("El pedido necesita un cliente.");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía.");
        }
        if (kilometros <= 0) {
            throw new IllegalArgumentException("Los kilómetros deben ser positivos.");
        }
        if (repartidor == null) {
            throw new IllegalArgumentException("El pedido necesita un repartidor.");
        }
        if (costoDomicilio < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo.");
        }
        if (tiempoMinutos < 0) {
            throw new IllegalArgumentException("El tiempo no puede ser negativo.");
        }

        this.numero = numero;
        this.cliente = cliente;
        this.descripcion = descripcion.trim();
        this.tamaño = tamaño;
        this.kilometros = kilometros;
        this.repartidor = repartidor;
        this.costoDomicilio = costoDomicilio;
        this.tiempoMinutos = tiempoMinutos;
        this.estado = "EN_CAMINO";
    }

    public int getNumero() {
        return numero;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean getTamaño() {
        return tamaño;
    }

    public double getKilometros() {
        return kilometros;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    public double getCostoDomicilio() {
        return costoDomicilio;
    }

    public double getTiempoMinutos() {
        return tiempoMinutos;
    }

    public String getEstado() {
        return estado;
    }

    public void cambiarEstado(String nuevoEstado) {
        if (!"ENTREGADO".equals(nuevoEstado) && !"CANCELADO".equals(nuevoEstado)) {
            throw new IllegalArgumentException("El estado debe ser ENTREGADO o CANCELADO.");
        }
        if (!"EN_CAMINO".equals(estado)) {
            throw new IllegalArgumentException("Un pedido finalizado no puede cambiar de estado.");
        }
        this.estado = nuevoEstado;
    }
}

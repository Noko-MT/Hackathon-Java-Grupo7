package Hackathon.modelo;

public class Pedido {
    private final int numero;
    private final Cliente cliente;
    private final String descripcion;
    private final boolean tamaño;
    private final double kilometros;
    private final Repartidor repartidor;
    private final long costoDomicilio;
    private final double tiempoMinutos;
    private String estado;

    public Pedido(int numero, Cliente cliente, String descripcion, boolean tamaño, double kilometros, Repartidor repartidor, long costoDomicilio, double tiempoMinutos) {
        this.numero = numero;
        this.cliente = cliente;
        this.descripcion = descripcion;
        this.tamaño = tamaño;
        this.kilometros = kilometros;
        this.repartidor = repartidor;
        this.costoDomicilio = costoDomicilio;
        this.tiempoMinutos = tiempoMinutos;
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
}

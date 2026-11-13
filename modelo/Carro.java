package Hackathon.modelo;

public class Carro extends Repartidor {
    private static final double TARIFA_BASE = 5000;
    private static final double VALOR_KM = 1200;
    private static final double VELOCIDAD = 25;

    public Carro(String codigo, String nombre) {
        super(codigo, nombre);
    }

    @Override
    public double calcularCosto(double km, boolean lluvia) {
        double costo = TARIFA_BASE + (km * VALOR_KM);
        if (lluvia) costo *= 1.2;
        return costo;
    }

    @Override
    public double calcularTiempo(double km) {
        return (km / VELOCIDAD) * 60;
    }

    @Override
    public boolean puedeLlevar(double km, boolean esGrande, boolean lluvia) {
        return true;
    }

    @Override
    public String getTipo() { return "Carro"; }
}
package Hackathon.modelo;

public class Moto extends Repartidor {
    private static final double TARIFA_BASE = 3000;
    private static final double VALOR_KM = 1000;
    private static final double VELOCIDAD = 30;
    private static final double DISTANCIA_MAX = 15;

    public Moto(String codigo, String nombre) {
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
        if (esGrande) return false;
        return km <= DISTANCIA_MAX;
    }

    @Override
    public String getTipo() { return "Moto"; }
}
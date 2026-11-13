package Hackathon.modelo;

public class Bici extends Repartidor {
    private static final double TARIFA_BASE = 2000;
    private static final double VALOR_KM = 800;
    private static final double VELOCIDAD = 15;
    private static final double DISTANCIA_MAX = 5;

    public Bici(String codigo, String nombre) {
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
        if (lluvia || esGrande) return false;
        return km <= DISTANCIA_MAX;
    }

    @Override
    public String getTipo() { return "Bici"; }
}
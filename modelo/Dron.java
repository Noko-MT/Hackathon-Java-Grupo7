package Hackathon.modelo;

public class Dron extends Repartidor {
    private static final double TARIFA_BASE = 4000;
    private static final double VALOR_POR_KM = 600;
    private static final double VELOCIDAD_KMH = 50;
    private static final double DISTANCIA_MAXIMA_KM = 8;

    public Dron(String codigo, String nombre) {
        super(codigo, nombre);
    }

    @Override
    public double calcularCosto(double km, boolean lluvia) {
        double costo = TARIFA_BASE + (km * VALOR_POR_KM);
        return lluvia ? costo * 1.20 : costo;
    }

    @Override
    public double calcularTiempo(double km) {
        return (km / VELOCIDAD_KMH) * 60;
    }

    @Override
    public boolean puedeLlevar(double km, boolean esGrande, boolean lluvia) {
        return km <= DISTANCIA_MAXIMA_KM && !esGrande && !lluvia;
    }

    @Override
    public String getTipo() {
        return "Dron";
    }
}

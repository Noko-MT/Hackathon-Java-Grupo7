package Hackathon.modelo;

public abstract class Repartidor implements Calificable {
    private String codigo;
    private String nombre;
    private boolean disponible;
    private int sumaCalificaciones;
    private int cantidadCalificaciones;

    public Repartidor(String codigo, String nombre) {
        setCodigo(codigo);
        setNombre(nombre);
        this.disponible = true;
        this.sumaCalificaciones = 0;
        this.cantidadCalificaciones = 0;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código del repartidor no puede estar vacío.");
        }
        this.codigo = codigo.trim();
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del repartidor no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public abstract double calcularCosto(double km, boolean lluvia);
    public abstract double calcularTiempo(double km);
    public abstract boolean puedeLlevar(double km, boolean esGrande, boolean lluvia);
    public abstract String getTipo();

    @Override
    public void recibirCalificacion(int calificacion) {
        if (calificacion < 1 || calificacion > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5.");
        }
        sumaCalificaciones += calificacion;
        cantidadCalificaciones++;
    }

    @Override
    public double obtenerPromedio() {
        if (cantidadCalificaciones == 0) return 0.0;
        return (double) sumaCalificaciones / cantidadCalificaciones;
    }

    public int getCantidadCalificaciones() { return cantidadCalificaciones; }

    @Override
    public String toString() {
        String prom = cantidadCalificaciones == 0 ? "Sin calificaciones" : String.format("%.2f", obtenerPromedio());
        return codigo + " | " + nombre + " | " + getTipo() + " | " + (disponible ? "Disponible" : "No disponible") + " | Prom: " + prom;
    }
}

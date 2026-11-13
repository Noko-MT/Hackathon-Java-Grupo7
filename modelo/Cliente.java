
package modelo;

public class Cliente {

    private String cedula;
    private String nombre;
    private String telefono;
    private String barrio;
    private String direccion;

    public Cliente(String cedula, String nombre, String telefono, String barrio, String direccion) {
        setCedula(cedula);
        setNombre(nombre);
        setTelefono(telefono);
        setBarrio(barrio);
        setDireccion(direccion);
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        validarTexto(cedula, "cédula");
        this.cedula = cedula.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        validarTexto(nombre, "nombre");
        this.nombre = nombre.trim();
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        if (telefono == null || !telefono.matches("\\d{7,10}")) {
            throw new IllegalArgumentException("El teléfono debe contener entre 7 y 10 dígitos numéricos.");
        }
        this.telefono = telefono;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        validarTexto(barrio, "barrio");
        this.barrio = barrio.trim();
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        validarTexto(direccion, "dirección");
        this.direccion = direccion.trim();
    }

    private void validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + campo + " no puede estar vacío.");
        }
    }
}


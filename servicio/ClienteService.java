
package servicio;

import Hackathon.modelo.Cliente;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ClienteService {

    private List<Cliente> clientes = new ArrayList<>();

    private static final Map<String, Double> DISTANCIAS_BARRIOS = new HashMap<>();

    static {
        DISTANCIAS_BARRIOS.put("chapinero", 1.5);
        DISTANCIAS_BARRIOS.put("teusaquillo", 3.0);
        DISTANCIAS_BARRIOS.put("la candelaria", 4.5);
        DISTANCIAS_BARRIOS.put("usaquén", 6.0);
        DISTANCIAS_BARRIOS.put("cedritos", 8.0);
        DISTANCIAS_BARRIOS.put("fontibón", 11.0);
        DISTANCIAS_BARRIOS.put("suba", 12.0);
        DISTANCIAS_BARRIOS.put("kennedy", 14.0);
        DISTANCIAS_BARRIOS.put("bosa", 18.0);
    }

    public ClienteService() {
        registrarCliente(new Cliente(
                "1020304050",
                "Laura Gómez",
                "3001234567",
                "Teusaquillo",
                "Dirección por definir"
        ));

        registrarCliente(new Cliente(
                "1030405060",
                "Mateo Rincón",
                "3109876543",
                "Usaquén",
                "Dirección por definir"
        ));
    }

    public void validarBarrio(String barrio) {
        if (barrio == null ||
                !DISTANCIAS_BARRIOS.containsKey(
                        barrio.trim().toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException(
                    "El barrio no está dentro de la cobertura."
            );
        }
    }

    public double obtenerDistanciaBarrio(String barrio) {
        validarBarrio(barrio);

        return DISTANCIAS_BARRIOS.get(
                barrio.trim().toLowerCase(Locale.ROOT)
        );
    }

    public void registrarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException(
                    "El cliente no puede ser nulo."
            );
        }

        validarBarrio(cliente.getBarrio());

        if (buscarCliente(cliente.getCedula()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un cliente con esa cédula."
            );
        }

        clientes.add(cliente);
    }

    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientes);
    }

    public Cliente buscarCliente(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            return null;
        }

        for (Cliente cliente : clientes) {
            if (cliente.getCedula().equals(cedula.trim())) {
                return cliente;
            }
        }

        return null;
    }

    public void editarCliente(String cedula, String nombre, String telefono,
                              String barrio, String direccion) {

        Cliente cliente = buscarCliente(cedula);

        if (cliente == null) {
            throw new IllegalArgumentException(
                    "No existe un cliente con esa cédula."
            );
        }

        validarBarrio(barrio);

        Cliente clienteValidado = new Cliente(
                cedula,
                nombre,
                telefono,
                barrio,
                direccion
        );

        cliente.setNombre(clienteValidado.getNombre());
        cliente.setTelefono(clienteValidado.getTelefono());
        cliente.setBarrio(clienteValidado.getBarrio());
        cliente.setDireccion(clienteValidado.getDireccion());
    }

    public void eliminarCliente(String cedula, boolean tienePedidoEnCamino) {
        Cliente cliente = buscarCliente(cedula);

        if (cliente == null) {
            throw new IllegalArgumentException(
                    "No existe un cliente con esa cédula."
            );
        }

        if (tienePedidoEnCamino) {
            throw new IllegalArgumentException(
                    "No se puede eliminar un cliente que tiene pedidos en camino."
            );
        }

        clientes.remove(cliente);
    }
}

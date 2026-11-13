package Hackathon.servicio;

import Hackathon.modelo.*;

import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

import java.util.LinkedHashSet;
import java.util.Set;

public class DomiService {

    private final Map<String, Cliente> clientes = new HashMap<>();

    private final Set<String> barriosCobertura = new LinkedHashSet<>();

    private final Map<String, Double> distancias = new HashMap<>();

    private final List<Repartidor> repartidores = new ArrayList<>();

    private final List<Pedido> pedidos = new ArrayList<>();

    private int siguienteNumero = 1;


    //cargar datos
    public DomiService() {
        cargarBarrios();
        cargarRepartidores();
        cargarClientes();
    }

    private void cargarBarrios() {
        agregarBarrio("Chapinero", 1.5);
        agregarBarrio("Teusaquillo", 3.0);
        agregarBarrio("La Candelaria", 4.5);
        agregarBarrio("Usaquén", 6.0);
        agregarBarrio("Cedritos", 8.0);
        agregarBarrio("Fontibón", 11.0);
        agregarBarrio("Suba", 12.0);
        agregarBarrio("Kennedy", 14.0);
        agregarBarrio("Bosa", 18.0);
    }

    private void agregarBarrio(String barrio, double kilometros) {
        barriosCobertura.add(barrio);
        distancias.put(barrio, kilometros);
    }

    private void cargarRepartidores() {
        repartidores.add(new Bici("R-01", "Camila Torres"));
        repartidores.add(new Moto("R-02", "Andrés Mejía"));
        repartidores.add(new Carro("R-03", "Jorge Pardo"));
    }

    private void cargarClientes() {
        clientes.put("1020304050", new Cliente("1020304050", "Laura Gómez", "3001234567", "Teusaquillo", "Dirección no especificada"));

        clientes.put("1030405060", new Cliente("1030405060", "Mateo Rincón", "3109876543", "Usaquén", "Dirección no especificada"));
    }

    //Modo lluvia
    private boolean modoLluvia = false;

    public boolean alternarModoLluvia() {
        modoLluvia = !modoLluvia;
        return modoLluvia;
    }

    public boolean estaModoLluvia() {
        return modoLluvia;
    }

    public boolean puedeRepartir(Repartidor repartidor, double km, boolean esGrande) {
        return repartidor.isDisponible() && repartidor.puedeLlevar(km, esGrande, modoLluvia);
    }

    //cierre del dia
    public String cierreDelDia() {
        Map<String, Integer> pedidosPorEstado = new HashMap<>();
        pedidosPorEstado.put("EN_CAMINO", 0);
        pedidosPorEstado.put("ENTREGADO", 0);
        pedidosPorEstado.put("CANCELADO", 0);

        Map<String, Double> ingresosPorTipo = new HashMap<>();

        double ingresosTotales = 0;

        for (Pedido pedido : pedidos) {
            String estado = pedido.getEstado();

            if (pedidosPorEstado.containsKey(estado)) {
                pedidosPorEstado.put(estado, pedidosPorEstado.get(estado) + 1);
            }

            if ("ENTREGADO".equals(estado)) {
                double costo = pedido.getCostoDomicilio();
                ingresosTotales += costo;

                String tipo = pedido.getRepartidor().getTipo();
                double ingresoActual = ingresosPorTipo.containsKey(tipo) ? ingresosPorTipo.get(tipo) : 0;

                ingresosPorTipo.put(tipo, ingresoActual + costo);
            }
        }

        Repartidor repartidorEstrella = null;

        for (Repartidor repartidor : repartidores) {
            if (repartidor.getCantidadCalificaciones() > 0 && (repartidorEstrella == null || repartidor.obtenerPromedio() > repartidorEstrella.obtenerPromedio())) {
                repartidorEstrella = repartidor;
            }
        }

        String reporte = "=== CIERRE DEL DÍA ===\n";
        reporte += "Total de pedidos: " + pedidos.size() + "\n";
        reporte += "En camino: " + pedidosPorEstado.get("EN_CAMINO") + "\n";
        reporte += "Entregados: " + pedidosPorEstado.get("ENTREGADO") + "\n";
        reporte += "Cancelados: " + pedidosPorEstado.get("CANCELADO") + "\n";
        reporte += "Ingresos totales: $" + Math.round(ingresosTotales) + "\n";
        reporte += "Ingresos por tipo de repartidor:\n";

        for (Map.Entry<String, Double> ingreso : ingresosPorTipo.entrySet()) {
            reporte += ingreso.getKey() + ": $" + Math.round(ingreso.getValue()) + "\n";
        }

        if (repartidorEstrella == null) {
            reporte += "Repartidor estrella: todavía no hay calificaciones.";
        } else {
            double promedio = repartidorEstrella.obtenerPromedio();
            promedio = Math.round(promedio * 100.0) / 100.0;
            reporte += "Repartidor estrella: " + repartidorEstrella.getNombre();
        }

        return reporte;
    }

    //metodos del cliente
    public boolean validarBarrio(String barrio) {
        if (barrio == null || barrio.trim().isEmpty()) {
            return false;
        }

        for (String barrioCubierto : barriosCobertura) {
            if (barrioCubierto.equalsIgnoreCase(barrio.trim())) {
                return true;
            }
        }

        return false;
    }

    public void registrarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }

        String cedula = cliente.getCedula().trim();

        if (clientes.containsKey(cedula)) {
            throw new IllegalArgumentException(
                    "Ya existe un cliente con esa cédula."
            );
        }

        if (!validarBarrio(cliente.getBarrio())) {
            throw new IllegalArgumentException("El barrio no está dentro de la cobertura.");
        }
        clientes.put(cedula, cliente);
    }

    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientes.values());
    }

    public Cliente buscarCliente(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            return null;
        }

        return clientes.get(cedula.trim());
    }

    public void editarCliente(String cedula, String telefono, String barrio, String direccion) {
        Cliente cliente = buscarCliente(cedula);

        if (cliente == null) {
            throw new IllegalArgumentException("No existe un cliente con esa cédula.");
        }

        if (!validarBarrio(barrio)) {
            throw new IllegalArgumentException("El barrio no está dentro de la cobertura.");
        }

        // Construir primero valida todos los datos antes de modificar el cliente.
        Cliente clienteValidado = new Cliente(
                cliente.getCedula(),
                cliente.getNombre(),
                telefono,
                barrio,
                direccion
        );

        cliente.setTelefono(clienteValidado.getTelefono());
        cliente.setBarrio(clienteValidado.getBarrio());
        cliente.setDireccion(clienteValidado.getDireccion());
    }
    public void eliminarCliente(String cedula) {
        Cliente cliente = buscarCliente(cedula);

        if (cliente == null) {
            throw new IllegalArgumentException(
                    "No existe un cliente con esa cédula."
            );
        }

        for (Pedido pedido : pedidos) {
            boolean esPedidoEnCamino =
                    "EN_CAMINO".equals(pedido.getEstado());

            boolean perteneceAlCliente =
                    pedido.getCliente().getCedula().equals(cliente.getCedula());

            if (esPedidoEnCamino && perteneceAlCliente) {
                throw new IllegalArgumentException(
                        "No se puede eliminar un cliente que tiene pedidos en camino."
                );
            }
        }

        clientes.remove(cliente.getCedula());
    }

    //metodos pedido
    public double obtenerDistanciaBarrio(String barrio) {
        validarBarrio(barrio);

        for (Map.Entry<String, Double> entrada : distancias.entrySet()) {
            if (entrada.getKey().equalsIgnoreCase(barrio.trim())) {
                return entrada.getValue();
            }
        }

        throw new IllegalArgumentException(
                "No se encontró la distancia para ese barrio."
        );
    }

    // Métodos de pedidos
    public ArrayList<Repartidor> obtenerRepartidoresAptos(double kilometros, boolean tamano) {
        ArrayList<Repartidor> aptos = new ArrayList<>();

        for (Repartidor repartidor : repartidores) {
            if (puedeRepartir(repartidor, kilometros, tamano)) {
                aptos.add(repartidor);
            }
        }
        return aptos;
    }

    public Pedido crearPedido(String cedula, String descripcion, boolean tamano) {
        Cliente cliente = buscarCliente(cedula);

        if (cliente == null) {
            throw new IllegalArgumentException(
                    "No existe un cliente con esa cédula."
            );
        }

        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La descripción no puede estar vacía."
            );
        }

        double kilometros = obtenerDistanciaBarrio(cliente.getBarrio());
        Repartidor mejor = null;
        double menorCosto = Double.MAX_VALUE;
        double menorTiempo = Double.MAX_VALUE;

        for (Repartidor repartidor :
                obtenerRepartidoresAptos(kilometros, tamano)) {
            double costo = repartidor.calcularCosto(kilometros, modoLluvia);
            double tiempo = repartidor.calcularTiempo(kilometros);

            if (costo < menorCosto
                    || (costo == menorCosto && tiempo < menorTiempo)) {
                mejor = repartidor;
                menorCosto = costo;
                menorTiempo = tiempo;
            }
        }

        if (mejor == null) {
            throw new IllegalArgumentException(
                    "No hay repartidores disponibles para este pedido."
            );
        }

        Pedido pedido = new Pedido(
                siguienteNumero,
                cliente,
                descripcion.trim(),
                tamano,
                kilometros,
                mejor,
                menorCosto,
                menorTiempo
        );

        pedidos.add(pedido);
        mejor.setDisponible(false);
        siguienteNumero++;
        return pedido;
    }

    public Pedido buscarPedido(int numero) {
        for (Pedido pedido : pedidos) {
            if (pedido.getNumero() == numero) {
                return pedido;
            }
        }
        throw new IllegalArgumentException("No existe el pedido número " + numero + ".");
    }

    public void entregarPedido(int numero, int calificacion) {
        Pedido pedido = buscarPedido(numero);
        if (!"EN_CAMINO".equals(pedido.getEstado())) {
            throw new IllegalArgumentException("Solo se puede entregar un pedido EN_CAMINO.");
        }
        if (calificacion < 1 || calificacion > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5.");
        }

        pedido.cambiarEstado("ENTREGADO");
        pedido.getRepartidor().recibirCalificacion(calificacion);
        pedido.getRepartidor().setDisponible(true);
    }

    public void cancelarPedido(int numero) {
        Pedido pedido = buscarPedido(numero);
        pedido.cambiarEstado("CANCELADO");
        pedido.getRepartidor().setDisponible(true);
    }

    public ArrayList<Pedido> listarPedidos() {
        return new ArrayList<>(pedidos);
    }

    public boolean tienePedidoEnCamino(Cliente cliente) {
        for (Pedido pedido : pedidos) {
            if (pedido.getCliente() == cliente && "EN_CAMINO".equals(pedido.getEstado())) {
                return true;
            }
        }
        return false;
    }

}

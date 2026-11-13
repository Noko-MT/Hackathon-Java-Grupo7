package Hackathon.app;

import Hackathon.modelo.Bici;
import Hackathon.modelo.Carro;
import Hackathon.modelo.Cliente;
import Hackathon.modelo.Dron;
import Hackathon.modelo.Moto;
import Hackathon.modelo.Pedido;
import Hackathon.modelo.Repartidor;
import Hackathon.servicio.DomiService;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final DomiService servicio;
    private final Scanner scanner;

    public Menu(DomiService servicio) {
        this.servicio = servicio;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        boolean continuar = true;

        while (continuar) {
            mostrarOpciones();

            try {
                int opcion = Integer.parseInt(scanner.nextLine().trim());
                continuar = ejecutarOpcion(opcion);
            } catch (NumberFormatException e) {
                System.out.println("Escribe el número de una opción.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void mostrarOpciones() {
        System.out.println();
        System.out.println("============== DOMI · OPERADOR ==============");
        if (servicio.estaModoLluvia()) {
            System.out.println("MODO LLUVIA ACTIVO");
        }
        System.out.println("CLIENTES      1. Registrar     2. Listar");
        System.out.println("              3. Editar        4. Eliminar");
        System.out.println("REPARTIDORES  5. Registrar     6. Listar");
        System.out.println("              7. Cambiar disponibilidad");
        System.out.println("PEDIDOS       8. Crear pedido");
        System.out.println("              9. Entregar     10. Cancelar");
        System.out.println("             11. Listar pedidos");
        System.out.println("OPERACIÓN    12. Activar / desactivar lluvia");
        System.out.println("             13. Cierre del día");
        System.out.println(" 0. Salir");
        System.out.println("=============================================");
        System.out.print("Elige una opción: ");
    }

    private boolean ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 0:
                System.out.println("Hasta luego.");
                return false;
            case 1:
                registrarCliente();
                break;
            case 2:
                listarClientes();
                break;
            case 3:
                editarCliente();
                break;
            case 4:
                eliminarCliente();
                break;
            case 5:
                registrarRepartidor();
                break;
            case 6:
                listarRepartidores();
                break;
            case 7:
                cambiarDisponibilidad();
                break;
            case 8:
                crearPedido();
                break;
            case 9:
                entregarPedido();
                break;
            case 10:
                cancelarPedido();
                break;
            case 11:
                listarPedidos();
                break;
            case 12:
                alternarLluvia();
                break;
            case 13:
                mostrarCierreDelDia();
                break;
            default:
                System.out.println("Opción no válida.");
        }
        return true;
    }

    private void registrarCliente() {
        String cedula = leer("Cédula: ");
        String nombre = leer("Nombre: ");
        String telefono = leer("Teléfono: ");
        String barrio = leer("Barrio: ");
        String direccion = leer("Dirección: ");

        Cliente cliente = new Cliente(cedula, nombre, telefono, barrio, direccion);
        servicio.registrarCliente(cliente);
        System.out.println("Cliente registrado: " + cliente.getNombre());
    }

    private void listarClientes() {
        List<Cliente> clientes = servicio.listarClientes();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }

        for (Cliente cliente : clientes) {
            System.out.println(cliente.getCedula() + " | " + cliente.getNombre()
                    + " | " + cliente.getTelefono() + " | " + cliente.getBarrio()
                    + " | " + cliente.getDireccion());
        }
    }

    private void editarCliente() {
        String cedula = leer("Cédula del cliente: ");
        String telefono = leer("Nuevo teléfono: ");
        String barrio = leer("Nuevo barrio: ");
        String direccion = leer("Nueva dirección: ");

        servicio.editarCliente(cedula, telefono, barrio, direccion);
        System.out.println("Cliente actualizado.");
    }

    private void eliminarCliente() {
        String cedula = leer("Cédula del cliente: ");
        servicio.eliminarCliente(cedula);
        System.out.println("Cliente eliminado.");
    }

    private void registrarRepartidor() {
        String codigo = leer("Código: ");
        String nombre = leer("Nombre: ");
        String tipo = leer("Tipo (Bici, Moto, Carro o Dron): ");

        Repartidor repartidor;
        if (tipo.equalsIgnoreCase("Bici")) {
            repartidor = new Bici(codigo, nombre);
        } else if (tipo.equalsIgnoreCase("Moto")) {
            repartidor = new Moto(codigo, nombre);
        } else if (tipo.equalsIgnoreCase("Carro")) {
            repartidor = new Carro(codigo, nombre);
        } else if (tipo.equalsIgnoreCase("Dron")) {
            repartidor = new Dron(codigo, nombre);
        } else {
            throw new IllegalArgumentException("El tipo debe ser Bici, Moto, Carro o Dron.");
        }

        servicio.registrarRepartidor(repartidor);
        System.out.println("Repartidor registrado: " + repartidor.getNombre()
                + " (" + repartidor.getTipo() + ").");
    }

    private void listarRepartidores() {
        List<Repartidor> repartidores = servicio.listarRepartidores();
        if (repartidores.isEmpty()) {
            System.out.println("No hay repartidores registrados.");
            return;
        }

        for (Repartidor repartidor : repartidores) {
            System.out.println(repartidor);
        }
    }

    private void cambiarDisponibilidad() {
        String codigo = leer("Código del repartidor: ");
        servicio.cambiarDisponibilidad(codigo);
        Repartidor repartidor = servicio.buscarRepartidor(codigo);
        System.out.println(repartidor.getNombre() + ": "
                + (repartidor.isDisponible() ? "disponible" : "no disponible"));
    }

    private void crearPedido() {
        String cedula = leer("Cédula del cliente: ");
        Cliente cliente = servicio.buscarCliente(cedula);
        if (cliente == null) {
            throw new IllegalArgumentException("No existe un cliente con esa cédula.");
        }

        String descripcion = leer("Descripción del pedido: ");
        boolean tamaño = leerPedidoGrande();
        double kilometros = servicio.obtenerDistanciaBarrio(cliente.getBarrio());
        boolean lluvia = servicio.estaModoLluvia();

        System.out.println("Cotizando para " + cliente.getNombre() + " en " + cliente.getBarrio()
                + " (" + kilometros + " km):");
        for (Repartidor repartidor : servicio.obtenerRepartidoresAptos(kilometros, tamaño)) {
            System.out.println(repartidor.getCodigo() + " | " + repartidor.getNombre()
                    + " (" + repartidor.getTipo() + ") | $"
                    + repartidor.calcularCosto(kilometros, lluvia) + " | "
                    + repartidor.calcularTiempo(kilometros) + " min");
        }

        Pedido pedido = servicio.crearPedido(cedula, descripcion, tamaño);
        System.out.println("Pedido #" + pedido.getNumero() + " asignado a "
                + pedido.getRepartidor().getNombre() + " | " + pedido.getEstado());
    }

    private void entregarPedido() {
        int numero = leerEntero("Número del pedido: ");
        int calificacion = leerEntero("Calificación del repartidor (1 a 5): ");
        servicio.entregarPedido(numero, calificacion);
        System.out.println("Pedido entregado. Repartidor disponible nuevamente.");
    }

    private void cancelarPedido() {
        int numero = leerEntero("Número del pedido: ");
        servicio.cancelarPedido(numero);
        System.out.println("Pedido cancelado. Repartidor disponible nuevamente.");
    }

    private void listarPedidos() {
        List<Pedido> pedidos = servicio.listarPedidos();
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.");
            return;
        }

        for (Pedido pedido : pedidos) {
            System.out.println("#" + pedido.getNumero() + " | " + pedido.getCliente().getNombre()
                    + " | " + pedido.getRepartidor().getNombre() + " ("
                    + pedido.getRepartidor().getTipo() + ") | $"
                    + pedido.getCostoDomicilio() + " | " + pedido.getTiempoMinutos()
                    + " min | " + pedido.getEstado());
        }
    }

    private void alternarLluvia() {
        boolean lluviaActiva = servicio.alternarModoLluvia();
        System.out.println(lluviaActiva ? "Lluvia activada." : "Lluvia desactivada.");
    }

    private void mostrarCierreDelDia() {
        System.out.println(servicio.cierreDelDia());
    }

    private String leer(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private int leerEntero(String mensaje) {
        return Integer.parseInt(leer(mensaje));
    }

    private boolean leerPedidoGrande() {
        String respuesta = leer("¿El pedido es grande? (s/n): ");
        if (respuesta.equalsIgnoreCase("s")) {
            return true;
        }
        if (respuesta.equalsIgnoreCase("n")) {
            return false;
        }
        throw new IllegalArgumentException("Responde s o n para el tamaño del pedido.");
    }

}


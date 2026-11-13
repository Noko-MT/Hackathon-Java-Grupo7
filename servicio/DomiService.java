package Hackathon.servicio;

import Hackathon.modelo.Cliente;
import Hackathon.modelo.Pedido;
import Hackathon.modelo.Repartidor;
import java.util.ArrayList;
import java.util.HashMap;

public class DomiService {
    private final ArrayList<Pedido> pedidos = new ArrayList<>();
    private final ArrayList<Repartidor> repartidores = new ArrayList<>();
    private int siguienteNumero = 1;

    // Métodos de pedidos
    public ArrayList<Repartidor> obtenerRepartidoresAptos(double kilometros, boolean tamaño, boolean lluvia) {
        ArrayList<Repartidor> aptos = new ArrayList<>();

        for (Repartidor repartidor : repartidores) {
            if (repartidor.isDisponible() && repartidor.puedeLlevar(kilometros, tamaño, lluvia)) {
                aptos.add(repartidor);
            }
        }
        return aptos;
    }

    public Pedido crearPedido(Cliente cliente, String descripcion, boolean tamaño,
                              double kilometros, boolean lluvia) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no existe.");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía.");
        }
        if (kilometros <= 0) {
            throw new IllegalArgumentException("La distancia debe ser mayor que cero.");
        }

        Repartidor mejor = null;
        double menorCosto = Double.MAX_VALUE;
        double menorTiempo = Double.MAX_VALUE;

        for (Repartidor repartidor : obtenerRepartidoresAptos(kilometros, tamaño, lluvia)) {
            double costo = repartidor.calcularCosto(kilometros, lluvia);
            double tiempo = repartidor.calcularTiempo(kilometros);

            if (costo < menorCosto || (costo == menorCosto && tiempo < menorTiempo)) {
                mejor = repartidor;
                menorCosto = costo;
                menorTiempo = tiempo;
            }
        }

        if (mejor == null) {
            throw new IllegalArgumentException("No hay repartidores disponibles para este pedido.");
        }

        Pedido pedido = new Pedido(siguienteNumero, cliente, descripcion.trim(), tamaño,
                kilometros, mejor, menorCosto, menorTiempo);
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

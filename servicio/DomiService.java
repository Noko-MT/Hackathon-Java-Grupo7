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

    public boolean alternarModoLluvia(){
        modoLluvia = !modoLluvia;
        return modoLluvia;
    }

    public boolean estaModoLluvia(){
        return modoLluvia;
    }

    public boolean puedeRepartir(Repartidor repartidor, double km, boolean esGrande) {
        return repartidor.isDisponible() && repartidor.puedeLlevar(km, esGrande, modoLluvia);
    }
}
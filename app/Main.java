package Hackathon.app;

import Hackathon.servicio.DomiService;

public class Main {
    public static void main(String[] args) {
        DomiService servicio = new DomiService();
        Menu menu = new Menu(servicio);
        menu.iniciar();
    }
}

package modelo;

import java.awt.Color;

public class Puerta extends Entidad implements Colisionable {

    public Puerta(double x, double y) {
        super("Meta", x, y, 40, 60, new Color(150, 95, 45), 1, 0); // 40x60; hp y ataque no se usan
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        h.completarNivel();
    }
}

package modelo;
import java.awt.Color;

/**
 * La meta: un portal dimensional en lo mas alto de la torre.
 * Al tocarlo, el heroe completa el juego.
 */
public class Puerta extends Entidad implements Colisionable {

    public Puerta(double x, double y) {
        super("Meta", x, y, 40, 60, new Color(150, 95, 45)); // 40x60;
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        h.completarNivel();
    }
}

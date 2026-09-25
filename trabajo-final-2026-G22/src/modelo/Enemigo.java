package modelo;
import java.awt.Color;

public abstract class Enemigo extends Entidad implements Colisionable {
    public Enemigo(String nombre, double x, double y,int ancho, int alto, Color color, int hp, int ataque) {
        super(nombre, x, y, ancho, alto, color, hp, ataque);
    }
    
    public abstract void actualizar(Heroe heroe, Escenario escenario); // Cada tipo de enemigo tiene su propia logica de movimiento y ataque, por eso es abstracta

    public boolean debeDibujarse(){//solo acechador lo va a redefinir, los demas enemigos se dibujan siempre
        return true;
    }
}
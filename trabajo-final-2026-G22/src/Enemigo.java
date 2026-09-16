import java.awt.Color;

public abstract class Enemigo extends Entidad implements Colisionable {
    public Enemigo(String nombre, double x, double y,int ancho, int alto, Color color, int hp, int ataque) {
        super(nombre, x, y, ancho, alto, color, hp, ataque);
    }
    
}
package modelo;
import java.awt.Color;

public abstract class Plataforma extends Entidad implements Colisionable {
    
    public Plataforma(String nombre, double x, double y, int alto, int ancho, Color color,int hp, int ataque) {
        // Llama al constructor de Entidad (HP 1, Ataque 0)
        super(nombre, x, y, alto, ancho, color, hp,ataque); // 1 seria la vida de la plataforma, 0 el ataque porque es inofensivo
    }
    // Obligamos a todas las plataformas hijas a tener este metodo
    @Override
    public abstract void aplicarEfectoColision(Heroe h);

    
    public void actualizar() {
        // Por ahora no hace nada, pero si alguna plataforma tiene logica propia, puede redefinirlo
    }

    public boolean debeEliminarse() {
        // Por defecto, las plataformas no se eliminan nunca, pero si alguna tiene logica propia puede redefinirlo
        return false;
    }
    
}

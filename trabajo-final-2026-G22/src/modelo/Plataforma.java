package modelo;
import java.awt.Color;

public abstract class Plataforma extends Entidad implements Colisionable {
    
    public Plataforma(String nombre, double x, double y, int alto, int ancho, Color color) {
        super(nombre, x, y, alto, ancho, color); 
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

    public void golpeadaDesdeAbajo() {
        // Por defecto, no pasa nada, pero si alguna tiene logica propia puede redefinirlo
    }    
    
}

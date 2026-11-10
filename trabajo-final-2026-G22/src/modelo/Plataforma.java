package modelo;

public abstract class Plataforma extends Entidad implements Colisionable {
    
    public Plataforma(String nombre, double x, double y, int ancho, int alto) {
        super(nombre, x, y, ancho, alto); 
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
    
    public abstract TipoPlataforma getTipo();

}

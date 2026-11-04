package modelo;

public abstract class Plataforma extends Entidad implements Colisionable {
    
    public Plataforma(String nombre, double x, double y, int ancho, int alto) {
        super(nombre, x, y, ancho, alto);
    }
    // Por defecto, una plataforma hace aterrizar al heroe.
    // Las hijas que hacen algo mas lo refinan llamando a super
    @Override
    public void aplicarEfectoColision(Heroe h){
        h.setVelocidadY(0);
        h.setEnElSuelo(true);
    }
    // El heroe golpea con la cabeza desde abajo, por defecto deja subir
    public void alGolpearDesdeAbajo(Heroe h){
        h.setVelocidadY(0);
        h.setEnElSuelo(false);
    }

    
    public void actualizar() {
        // Por ahora no hace nada, pero si alguna plataforma tiene logica propia, puede redefinirlo
    }

    public boolean debeEliminarse() {
        // Por defecto, las plataformas no se eliminan nunca, pero si alguna tiene logica propia puede redefinirlo
        return false;
    }
    
}

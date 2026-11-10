package modelo;

public class PlataformaNormal extends Plataforma {

    public PlataformaNormal(double x, double y, int ancho, int alto) {
        super("Plataforma Normal", x, y, ancho, alto);
    }
    @Override 
    public void aplicarEfectoColision(Heroe h) {
        // No hace nada, es solo un piso normal
        h.setVelocidadY(0);
        h.setEnElSuelo(true);
    }

    @Override 
    public TipoPlataforma getTipo(){
        return TipoPlataforma.NORMAL;
    }
} 
    


package modelo;
import java.awt.Color;

public class PlataformaNormal extends Plataforma {
    public PlataformaNormal(double x, double y, int alto, int ancho) {
        super("Plso", x, y, alto, ancho, Color.GRAY, 1, 0);
    }
    @Override 
    public void aplicarEfectoColision(Heroe h) {
        // No hace nada, es solo un piso normal
        h.setVelocidadY(0);
        h.setEnElSuelo(true);
    }
} 
    


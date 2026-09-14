import java.awt.Color;

public class PlataformaTrampolin extends Plataforma {
    
    private double multiplicadorRebote;

    public PlataformaTrampolin(double x, double y, int ancho, int alto) {
        // Un verde llamativo, 100 de HP y 0 de ataque
        super("Trampolín", x, y, ancho, alto, Color.GREEN, 100, 0);
        
        // Multiplica la fuerza del salto por 1.5
        this.multiplicadorRebote = 1.5;
    }

    public double getMultiplicadorRebote() {
        return multiplicadorRebote;
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        // En vez de frenarlo, le mandamos una velocidad negativa (hacia arriba) para que rebote
        h.setVelocidadY(-15.0 * multiplicadorRebote); 
    }
}
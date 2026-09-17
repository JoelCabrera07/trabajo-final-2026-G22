package modelo;
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
        // le mandamos una velocidad negativa (hacia arriba) para que rebote, en java para que vaya hacia arriba la velocidad es negativa
        h.setVelocidadY(-15.0 * this.multiplicadorRebote); // Ajusta el valor según la física del juego
        h.setEnElSuelo(false); // Le avisamos que ya no está en el suelo para que no pueda saltar de nuevo hasta tocar otra plataforma
    }
}
import java.awt.Color;

public class PlataformaTrampolin extends Plataforma {
    
    private double multiplicadorRebote;

    public PlataformaTrampolin(double x, double y, int ancho, int alto) {
        // Un color rosa/magenta típico de trampolines en los juegos
        super("Plataforma Trampolín", x, y, ancho, alto, Color.MAGENTA);
        
        // Multiplica la fuerza de salto (por ejemplo, 1.8 = 80% más alto)
        this.multiplicadorRebote = 1.8; 
    }

    public double getMultiplicadorRebote() {
        return multiplicadorRebote;
    }
}
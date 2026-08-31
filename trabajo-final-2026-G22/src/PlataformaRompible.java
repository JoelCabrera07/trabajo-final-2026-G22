import java.awt.Color;

public class PlataformaRompible extends Plataforma {
    
    public PlataformaRompible(double x, double y, int ancho, int alto) {
        // Le ponemos color naranja para distinguirla
        super("Plataforma Rompible", x, y, ancho, alto, Color.ORANGE);
    }

    // Faltaria llamar al método recibirDanio que heredó de Entidad.
}
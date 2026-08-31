import java.awt.Color;

public class PlataformaHielo extends Plataforma {
    
    private double nivelFriccion; // Variable exclusiva del hielo

    public PlataformaHielo(double x, double y, int ancho, int alto) {
        // Llamamos a Plataforma (que a su vez llama a Entidad)
        // Le clavamos el nombre y el color CYAN para identificarla fácil en la pantalla
        super("Plataforma de Hielo", x, y, ancho, alto, Color.CYAN);
        
        // 0.2 de fricción (patina mucho). Una normal tendría 1.0 por ejemplo.
        this.nivelFriccion = 0.2; 
    }

    public double getNivelFriccion() {
        return nivelFriccion;
    }
}
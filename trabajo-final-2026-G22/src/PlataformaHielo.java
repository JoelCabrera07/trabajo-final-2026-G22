import java.awt.Color;

public class PlataformaHielo extends Plataforma {
    
    private double nivelFriccion;

    public PlataformaHielo(double x, double y, int ancho, int alto) {
        // Le mandamos un color celestito, 100 de HP y 0 de ataque
        super("Plataforma de Hielo", x, y, ancho, alto, new Color(173, 216, 230), 100, 0);
        
        // Friccion baja para que el personaje patine cuando intente frenar
        this.nivelFriccion = 0.2;
    }

    public double getNivelFriccion() {
        return nivelFriccion;
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        // 1. Frenamos la caída apenas toca el piso
        h.setVelocidadY(0); 
        
        // (Aca va la lógica matemática para que el héroe patine con la inercia )
    }
}
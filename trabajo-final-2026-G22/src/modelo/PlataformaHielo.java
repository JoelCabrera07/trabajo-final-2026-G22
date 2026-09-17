package modelo;
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
        h.setVelocidadY(0); 
        h.setEnElSuelo(true);
        
        // Efecto resbalón: Si el jugador soltó la tecla y su velocidad horizontal es 0
        if (h.getVelocidadX() == 0) { 
            if (h.isMirandoDerecha()) {
                h.setVelocidadX(2.0); // Patina hacia la derecha (más lento que caminar que es 4)
            } else {
                h.setVelocidadX(-2.0); // Patina hacia la izquierda
            }
        }
    }
}
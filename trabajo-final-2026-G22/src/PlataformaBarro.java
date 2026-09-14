import java.awt.Color;

public class PlataformaBarro extends Plataforma {
    
    private double reduccionVelocidad;

    public PlataformaBarro(double x, double y, int ancho, int alto) {
        // Le sacamos las letras al Color y le agregamos 100 de HP y 0 de ataque al final
        super("Plataforma de Barro", x, y, ancho, alto, new Color(101, 67, 33), 100, 0);
        
        // Limita la velocidad del jugador a un 40%
        this.reduccionVelocidad = 0.4;
    }

    public double getReduccionVelocidad() {
        return reduccionVelocidad;
    }

    // Cumplimos con el contrato de la clase padre
    @Override
    public void aplicarEfectoColision(Heroe h) {
        // Frenamos la caída del héroe
        h.setVelocidadY(0); 
        
        // Falta meter la lógica para ralentizar al jugador
    }
}
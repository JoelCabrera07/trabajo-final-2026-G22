import java.awt.Color;

public class PlataformaBarro extends Plataforma {
    
    private double reduccionVelocidad;

    public PlataformaBarro(double x, double y, int ancho, int alto) {
        // Usamos RGB para crear un color marrón (Barro)
        super("Plataforma de Barro", x, y, ancho, alto, new Color(101, 67, 33));
        
        // Limita la velocidad del jugador a un 40%
        this.reduccionVelocidad = 0.4; 
    }

    public double getReduccionVelocidad() {
        return reduccionVelocidad;
    }
}
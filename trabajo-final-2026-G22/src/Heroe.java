import java.awt.Color;

public class Heroe extends Entidad{
    private double velocidadY;
    private double velocidadX;
    private double cargaSaltoActual;

    public Heroe(String nombre, int x, int y,int ancho, int alto, Color color, int hp, int ataque) {
        super(nombre, x, y, ancho, alto, color, hp, ataque);
    }
    // Método para modificar la velocidad vertical (frenarlo o hacerlo caer)
    public void setVelocidadY(double velocidadY) {
        this.velocidadY = velocidadY;
    }
    
    
}

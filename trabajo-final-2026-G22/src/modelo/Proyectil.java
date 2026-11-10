package modelo;

public class Proyectil extends Entidad {

    private double velocidadX;
    private double velocidadY;
    private final int ataque;

    public Proyectil(double x, double y, double dirX, double dirY, int ataque) {
        super("Proyectil", x, y, 10, 10);
        this.ataque = ataque; 
        
        double magnitud = Math.sqrt(dirX * dirX + dirY * dirY);
        if (magnitud == 0) magnitud = 1;

        double velocidad = 6.0;
        this.velocidadX = (dirX / magnitud) * velocidad;
        this.velocidadY = (dirY / magnitud) * velocidad;
    }

    public int getAtaque(){
        return ataque;
    }

    public double getAngulo() {
        return Math.atan2(velocidadY, velocidadX);
    }

    public void actualizar() {
        setX(getX() + velocidadX);
        setY(getY() + velocidadY);
    }
}

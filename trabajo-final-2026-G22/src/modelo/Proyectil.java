package modelo;
import java.awt.Color;

public class Proyectil extends Entidad {

    private double velocidadX;
    private double velocidadY;

    public Proyectil(double x, double y, double dirX, double dirY, int ataque) {
        super("Proyectil", x, y, 10, 10, Color.YELLOW, 1, ataque);

        double magnitud = Math.sqrt(dirX * dirX + dirY * dirY);
        if (magnitud == 0) magnitud = 1;

        double velocidad = 6.0;
        this.velocidadX = (dirX / magnitud) * velocidad;
        this.velocidadY = (dirY / magnitud) * velocidad;
    }

    public void actualizar() {
        setX(getX() + velocidadX);
        setY(getY() + velocidadY);
    }
}
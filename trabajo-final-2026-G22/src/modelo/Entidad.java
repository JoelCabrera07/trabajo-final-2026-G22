package modelo;
import java.awt.Rectangle;

public abstract class Entidad {
    
    private String nombre;
    private double x;//posicion en el eje x
    private double y;//posicion en el eje y
    private int ancho;//tamaño de la hitbox
    private int alto;//tamaño de la hitbox

    public Entidad(String nombre, double x, double y,int ancho, int alto) {
        this.nombre = nombre;
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
    }

    public String getNombre() {
        return nombre;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    //lo mejor es poner el metodo aca, para que todos lo aprendan automáticamente.
    public Rectangle getHitbox() {
        // Rectangle trabaja con números enteros
        return new Rectangle((int) x, (int) y, ancho, alto);
    }
}
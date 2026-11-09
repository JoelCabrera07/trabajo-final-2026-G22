package modelo;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
public abstract class Entidad {
    
private String nombre;
    private double x;//posicion en el eje x
    private double y;//posicion en el eje y
    private int ancho;//tamaño de la hitbox
    private int alto;//tamaño de la hitbox
    private Color color;//necesario para poder ponerle colores al programa
    private int ataque;//cantidad de hp que pueden quitar 
    private BufferedImage sprite;//imagen a dibujar; si no tiene ningun sprite, se sigue dibujando el rectangulo de color


    public Entidad(String nombre, double x, double y,int ancho, int alto, Color color, int ataque) {
        this.nombre = nombre;
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.color = color;
        this.ataque = ataque;
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

    public Color getColor() {
        return color;
    }

    public int getAtaque() { 
        return ataque; 
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    //lo mejor es poner el metodo aca, para que todos lo aprendan automáticamente.
    public Rectangle getHitbox() {
        // Rectangle trabaja con números enteros
        return new Rectangle((int) x, (int) y, ancho, alto);
    }

    // Rectangulo donde la vista dibuja el sprite. Por defecto es lo de siempre: arriba a la
    // izquierda de la hitbox, con su ancho y 50 px de alto. Las entidades cuyo dibujo tiene
    // otro tamaño que la hitbox (Acechador, Torreta, Proyectil) lo redefinen, asi la vista
    // no tiene que preguntar de que tipo es cada una.
    public Rectangle getAreaSprite() {
        return new Rectangle((int) x, (int) y, ancho, 50);
    }
}
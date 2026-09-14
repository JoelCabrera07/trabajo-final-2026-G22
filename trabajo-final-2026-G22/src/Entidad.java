import java.awt.Color;
import java.awt.Rectangle;
public abstract class Entidad {
    
private String nombre;
    private double x;//posicion en el eje x
    private double y;//posicion en el eje y
    private int ancho;//tamaño de la hitbox
    private int alto;//tamaño de la hitbox
    private Color color;//necesario para poder ponerle colores al programa
    private int hp;//hp actual
    private int hpMax;//limite de hp
    private int ataque;//cantidad de hp que pueden quitar 


    public Entidad(String nombre, double x, double y,int ancho, int alto, Color color, int hp, int ataque) {
        this.nombre = nombre;
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.color = color;
        this.hpMax = hp;
        this.hp = hp;
        this.ataque = ataque;
    }

    public void recibirDanio(int cantidad) {
        this.hp -= cantidad;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    public boolean estaVivo() {
        return this.hp > 0;
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


    public int getHp() { 
        return hp; 
    }

    public int getHpMax() { 
        return hpMax;
    }

    public int getAtaque() { 
        return ataque; 
    }
    //lo mejor es poner el metodo aca, para que todos lo aprendan automáticamente.
    public Rectangle getHitbox() {
        // Rectangle trabaja con números enteros
        return new Rectangle((int) x, (int) y, ancho, alto);
    }
}
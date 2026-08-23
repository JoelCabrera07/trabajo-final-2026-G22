import java.awt.Color;

public class Entidad {
    
private String nombre;
    private int x;//tamaño de la hitbox
    private int y;//tamaño de la hitbox
    private Color color;//necesario para poder ponerle colores al programa
    private int hp;//hp actual
    private int hpMax;//limite de hp
    private int ataque;//cantidad de hp que pueden quitar 


    public Entidad (String nombre, int x, int y, Color color, int hp, int ataque) {
        this.nombre = nombre;
        this.x = x;
        this.y = y;
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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


}
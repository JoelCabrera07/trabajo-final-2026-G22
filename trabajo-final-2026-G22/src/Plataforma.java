import java.awt.Color;

public class Plataforma extends Entidad {
    
    public Plataforma(String nombre, double x, double y, int alto, int ancho, Color color) {
        // Llama al constructor de Entidad (HP 1, Ataque 0)
        super(nombre, x, y, alto, ancho, color, 1,0); // 1 seria la vida de la plataforma, 0 el ataque porque es inofensivo
    }
    
    // Aca van los metodos genericos de colision para el piso normal
}

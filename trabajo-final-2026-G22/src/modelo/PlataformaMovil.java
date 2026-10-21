package modelo;
import java.awt.Color;

public class PlataformaMovil extends Plataforma {
    
    private double velocidadX;
    private double limiteIzquierdo;
    private double limiteDerecho;
    private boolean moviendoDerecha;

    public PlataformaMovil(double x, double y, int ancho, int alto, double limiteIzq, double limiteDer) {
        // Pasamos nombre, X, Y, ancho, alto, color, HP y Ataque (0) a la superclase
        super("Plataforma Móvil", x, y, ancho, alto, Color.LIGHT_GRAY, 100, 0);
        setSprite(Sprites.getSpritesPlataforma(0)[0]); // sprite de plataforma movil fila 0, columna 0 usa la de plataforma normal 
        this.velocidadX = 3.0; // Velocidad estándar de desplazamiento
        this.limiteIzquierdo = limiteIzq;
        this.limiteDerecho = limiteDer;
        this.moviendoDerecha = true; // Empieza moviéndose hacia la derecha por defecto
    }

    public void mover() {
        // Lógica matemática simple para que vaya y vuelva entre los dos puntos
        if (moviendoDerecha) {
            this.setX(this.getX() + velocidadX);
            if (this.getX() >= limiteDerecho) {
                moviendoDerecha = false; // Toca el límite derecho y da la vuelta
            }
        } else {
            this.setX(this.getX() - velocidadX);
            if (this.getX() <= limiteIzquierdo) {
                moviendoDerecha = true;  // Toca el límite izquierdo y da la vuelta
            }
        }
        this.getHitbox().setLocation((int)this.getX(), (int)this.getY()); // Actualizamos la hitbox para que coincida con la nueva posición, osea movemos la hitbox con esto
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        // 1. Frenar la caida del heroe y marcarlo en el suelo para que pueda saltar
        h.setVelocidadY(0); 
        h.setEnElSuelo(true);
        
        // 2. Hacer que el héroe se mueva junto con la plataforma
        if (moviendoDerecha) {
            h.setX(h.getX() + velocidadX);
        } else {
            h.setX(h.getX() - velocidadX);
        }
    }

    @Override 
    public void actualizar() {
        mover(); // Cada frame, la plataforma se mueve
    }
}
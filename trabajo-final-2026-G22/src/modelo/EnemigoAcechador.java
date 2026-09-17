package modelo;
import java.awt.Color;

public class EnemigoAcechador extends Enemigo {
    
    private int tiempoDeteccion;

    public EnemigoAcechador(double x, double y) {
        // Bicho raro que aparece si te quedás quieto
        super("Acechador", x, y, 30, 30, Color.MAGENTA, 8, 2);
        this.tiempoDeteccion = 5; // Segundos antes de empezar a perseguirte
    }

    public void perseguir(Heroe h) {
        // Lógica para que vaya directamente hacia las coordenadas X e Y del héroe
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        h.recibirDanio(this.getAtaque());
    }
}

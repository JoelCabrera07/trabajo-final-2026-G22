import java.awt.Color;

public class EnemigoTorreta extends Enemigo {
    
    private int cadenciaDisparo;

    public EnemigoTorreta(double x, double y) {
        // No se mueve, así que lo hacemos un poco más alto y duro
        super("Torreta", x, y, 30, 50, Color.ORANGE, 15, 2);
        this.cadenciaDisparo = 3; // Ejemplo: dispara cada 3 segundos
    }

    public void disparar() {
        // Lógica para instanciar el proyectil que va en línea recta
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        h.recibirDanio(this.getAtaque());
    }
}
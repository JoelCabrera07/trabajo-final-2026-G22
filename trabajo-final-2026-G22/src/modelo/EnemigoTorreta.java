package modelo;
import java.awt.Color;

public class EnemigoTorreta extends Enemigo {
    
    private int cadenciaDisparo; //seg entre disparos
    private long ultimoDisparo;//tiempo del último disparo en milisegundos

    public EnemigoTorreta(double x, double y) {
        // No se mueve, así que lo hacemos un poco más alto y duro
        super("Torreta", x, y, 30, 50, Color.ORANGE, 15, 2);
        this.cadenciaDisparo = 3; // Ejemplo: dispara cada 3 segundos
        this.ultimoDisparo = System.currentTimeMillis();
    }

    // Devuelve un Proyectil nuevo si ya toca disparar, o null si todavía no pasó el tiempo
    public Proyectil disparar(Heroe h) {
        long ahora = System.currentTimeMillis();
        if (ahora - ultimoDisparo < cadenciaDisparo * 1000L) {
            return null;
        }
        ultimoDisparo = ahora;

        double dirX = h.getX() - getX();
        double dirY = h.getY() - getY();
        return new Proyectil(getX(), getY(), dirX, dirY, this.getAtaque());
    }

    public void disparar() {
        // Lógica para instanciar el proyectil que va en línea recta
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        h.recibirDanio(this.getAtaque());
    }
}
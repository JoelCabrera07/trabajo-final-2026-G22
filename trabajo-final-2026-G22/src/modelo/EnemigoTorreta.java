package modelo;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class EnemigoTorreta extends Enemigo {

    private int cadenciaDisparo; //seg entre disparos
    private long ultimoDisparo;//tiempo del último disparo en milisegundos
    private boolean yaDisparo = false; // hasta el primer disparo solo muestra el reposo

    // --- Animacion (Sprites.getTorretaMagica): 0-3 reposo, 4-5 carga, 6 disparo, 7-8 enfriando ---
    private BufferedImage[] framesDerecha;
    private BufferedImage[] framesIzquierda;
    private boolean mirandoDerecha = true;

    // Cada fotograma mide 60x72. El pedestal apoya en el borde de abajo de la hitbox y el
    // cristal queda centrado sobre ella.
    private static final int SPRITE_ANCHO = 60;
    private static final int SPRITE_ALTO = 72;
    // Donde se forma el orbe dentro del dibujo (mirando a la derecha): de ahi sale el disparo
    private static final int BOCA_X = 46;
    private static final int BOCA_Y = 22;

    public EnemigoTorreta(double x, double y) {
        // No se mueve, así que lo hacemos un poco más alto y duro
        super("Torreta", x, y, 20, 40, Color.ORANGE, 15, 2);
        this.cadenciaDisparo = 1; // Ejemplo: dispara cada 1 segundo
        this.ultimoDisparo = System.currentTimeMillis();

        this.framesDerecha = Sprites.getTorretaMagica();
        this.framesIzquierda = Sprites.voltearAnimacion(framesDerecha);
        if (framesDerecha != null) {
            setSprite(framesDerecha[0]);
        }
    }

    // Devuelve un Proyectil nuevo si ya toca disparar, o null si todavía no pasó el tiempo
    public Proyectil disparar(Heroe h) {
        long ahora = System.currentTimeMillis();
        if (ahora - ultimoDisparo < cadenciaDisparo * 1000L) {
            return null;
        }
        ultimoDisparo = ahora;
        yaDisparo = true;

        // El orbe sale de adelante del cristal y apunta al centro del heroe
        Rectangle area = getAreaSprite();
        double salidaX = area.x + (mirandoDerecha ? BOCA_X : SPRITE_ANCHO - BOCA_X);
        double salidaY = area.y + BOCA_Y;
        double dirX = (h.getX() + h.getAncho() / 2.0) - salidaX;
        double dirY = (h.getY() + h.getAlto() / 2.0) - salidaY;
        // El proyectil mide 10x10: se resta 5 para que su centro quede en el punto de salida
        return new Proyectil(salidaX - 5, salidaY - 5, dirX, dirY, this.getAtaque());
    }

    // Elige el fotograma segun cuanto falta para el proximo disparo, asi la carga
    // siempre coincide con el momento en que sale el orbe.
    private int frameSegunTiempo() {
        long t = System.currentTimeMillis() - ultimoDisparo;
        long ciclo = cadenciaDisparo * 1000L;

        if (yaDisparo && t < 120) return 6;   // disparo
        if (yaDisparo && t < 240) return 7;   // enfriando
        if (yaDisparo && t < 360) return 8;
        if (t >= ciclo - 150) return 5;       // carga fuerte
        if (t >= ciclo - 300) return 4;       // empieza a cargar
        return (int) ((t / 150) % 4);         // reposo (flota y gira)
    }

    private void actualizarAnimacion(Heroe h) {
        if (framesDerecha == null || framesIzquierda == null) return;
        // Mira hacia el heroe, asi el orbe se forma del lado correcto
        mirandoDerecha = (h.getX() + h.getAncho() / 2.0) >= (getX() + getAncho() / 2.0);
        int frame = frameSegunTiempo();
        setSprite(mirandoDerecha ? framesDerecha[frame] : framesIzquierda[frame]);
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        h.recibirDanio(this.getAtaque());
    }

    @Override
    public Rectangle getAreaSprite() {
        int x = (int) getX() + getAncho() / 2 - SPRITE_ANCHO / 2;
        int y = (int) getY() + getAlto() - SPRITE_ALTO;
        return new Rectangle(x, y, SPRITE_ANCHO, SPRITE_ALTO);
    }

    @Override
    public void actualizar(Heroe heroe, Escenario escenario) {
        actualizarAnimacion(heroe);
        Proyectil nuevo = disparar(heroe);
        if (nuevo != null) {
            escenario.getProyectiles().add(nuevo);
        }
    }
}

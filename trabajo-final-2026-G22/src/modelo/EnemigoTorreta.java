package modelo;
import java.awt.Color;

public class EnemigoTorreta extends Enemigo {

    private int cadenciaDisparo; //seg entre disparos
    private long ultimoDisparo;//tiempo del último disparo en milisegundos
    private boolean yaDisparo = false; // hasta el primer disparo solo esta en reposo
    private boolean mirandoDerecha = true;

    // De donde sale el disparo, medido desde el centro de su hitbox
    private static final double SALIDA_X = 16;   // hacia el lado al que mira
    private static final double SALIDA_Y = -10;  // respecto del borde de arriba de la hitbox

    // Duracion (ms) de cada fase del ciclo
    private static final long MS_DISPARO = 120;
    private static final long MS_ENFRIANDO = 240;
    private static final long MS_CARGA = 300;    // antes del proximo disparo

    public EnemigoTorreta(double x, double y) {
        // No se mueve, así que lo hacemos un poco más alto y duro
        super("Torreta", x, y, 20, 40, Color.ORANGE, 2);
        this.cadenciaDisparo = 2; // Ejemplo: dispara cada 2 segundos
        this.ultimoDisparo = System.currentTimeMillis();
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
        double salidaX = getX() + getAncho() / 2.0 + (mirandoDerecha ? SALIDA_X : -SALIDA_X);
        double salidaY = getY() + SALIDA_Y;
        double dirX = (h.getX() + h.getAncho() / 2.0) - salidaX;
        double dirY = (h.getY() + h.getAlto() / 2.0) - salidaY;
        // El proyectil mide 10x10: se resta 5 para que su centro quede en el punto de salida
        return new Proyectil(salidaX - 5, salidaY - 5, dirX, dirY, this.getAtaque());
    }

    private long msDesdeDisparo() {
        return System.currentTimeMillis() - ultimoDisparo;
    }

    // La fase sale de cuanto falta para el proximo disparo, asi la carga siempre
    // coincide con el momento en que sale el orbe.
    @Override
    public FaseEnemigo getFase() {
        long t = msDesdeDisparo();
        if (yaDisparo && t < MS_DISPARO) return FaseEnemigo.DISPARANDO;
        if (yaDisparo && t < MS_DISPARO + MS_ENFRIANDO) return FaseEnemigo.ENFRIANDO;
        if (t >= cadenciaDisparo * 1000L - MS_CARGA) return FaseEnemigo.CARGANDO;
        return FaseEnemigo.REPOSO;
    }

    @Override
    public long getMsEnFase() {
        long t = msDesdeDisparo();
        switch (getFase()) {
            case ENFRIANDO: return t - MS_DISPARO;
            case CARGANDO:  return t - (cadenciaDisparo * 1000L - MS_CARGA);
            default:        return t;
        }
    }

    @Override
    public boolean mirandoDerecha() {
        return mirandoDerecha;
    }

    @Override
    public void actualizar(Heroe heroe, Escenario escenario) {
        // Mira hacia el heroe, asi el orbe se forma del lado correcto
        mirandoDerecha = (heroe.getX() + heroe.getAncho() / 2.0) >= (getX() + getAncho() / 2.0);
        Proyectil nuevo = disparar(heroe);
        if (nuevo != null) {
            escenario.getProyectiles().add(nuevo);
        }
    }

    @Override
    public TipoEnemigo getTipo(){
        return TipoEnemigo.TORRETA;
    }
}
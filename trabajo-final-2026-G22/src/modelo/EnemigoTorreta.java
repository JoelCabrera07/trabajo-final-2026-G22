package modelo;

import java.awt.Color;

public class EnemigoTorreta extends Enemigo {

    private int cadenciaDisparo; // seg entre disparos
    private long ultimoDisparo;// tiempo del último disparo en milisegundos
    private boolean yaDisparo = false; // hasta el primer disparo solo muestra el reposo
    private EstadoEnemigo estado = EstadoEnemigo.REPOSO;
    private int ticksEnEstado = 0;

    private boolean mirandoDerecha = true;
    // Duración de cada fase del ciclo de disparo, en milisegundos
    private static final int MS_DISPARANDO = 120;
    private static final int MS_FIN_ENFRIANDO = 360;
    private static final int MS_CARGA = 300;
    // De dónde sale el orbe, medido desde la posición de la torreta (coordenadas
    // del juego)
    private static final int DISPARO_X_DERECHA = 26;
    private static final int DISPARO_X_IZQUIERDA = -6;
    private static final int DISPARO_Y = -10;

    public EnemigoTorreta(double x, double y) {
        // No se mueve, así que lo hacemos un poco más alto y duro
        super("Torreta", x, y, 20, 40, Color.ORANGE, 15, 2);
        this.cadenciaDisparo = 2; // Ejemplo: dispara cada 2 segundos
        this.ultimoDisparo = System.currentTimeMillis();
    }

    // Devuelve un Proyectil nuevo si ya toca disparar, o null si todavía no pasó el
    // tiempo
    public Proyectil disparar(Heroe h) {
        long ahora = System.currentTimeMillis();
        if (ahora - ultimoDisparo < cadenciaDisparo * 1000L) {
            return null;
        }
        ultimoDisparo = ahora;
        yaDisparo = true;

        // El orbe sale de adelante del cristal y apunta al centro del heroe
        double salidaX = getX() + (mirandoDerecha ? DISPARO_X_DERECHA : DISPARO_X_IZQUIERDA);
        double salidaY = getY() + DISPARO_Y;
        double dirX = (h.getX() + h.getAncho() / 2.0) - salidaX;
        double dirY = (h.getY() + h.getAlto() / 2.0) - salidaY;
        // El proyectil mide 10x10: se resta 5 para que su centro quede en el punto de
        // salida
        return new Proyectil(salidaX - 5, salidaY - 5, dirX, dirY, this.getAtaque());
    }
    @Override
    public void aplicarEfectoColision(Heroe h) {
        h.recibirDanio(this.getAtaque());
    }

    @Override
    public void actualizar(Heroe heroe, Escenario escenario) {
        // Mira hacia el héroe, así el orbe sale del lado correcto
        mirandoDerecha = (heroe.getX() + heroe.getAncho() / 2.0) >= (getX() + getAncho() / 2.0);
        actualizarEstado();
        Proyectil nuevo = disparar(heroe);
        if (nuevo != null) {
            escenario.getProyectiles().add(nuevo);
        }
    }

    @Override
    public TipoEntidad getTipo() {
        return TipoEntidad.ENEMIGO_TORRETA;
    }

    @Override
    public EstadoEnemigo getEstado() {
        return estado;
    }

    @Override
    public int getTicksEnEstado() {
        return ticksEnEstado;
    }

    @Override
    public boolean isMirandoDerecha() {
        return mirandoDerecha; // mira hacia el héroe
    }

    // Calcula en qué fase del ciclo de disparo está, según el tiempo desde el
    // último disparo
    private void actualizarEstado() {
        long t = System.currentTimeMillis() - ultimoDisparo;
        long ciclo = cadenciaDisparo * 1000L;

        EstadoEnemigo nuevo;
        if (yaDisparo && t < MS_DISPARANDO) {
            nuevo = EstadoEnemigo.DISPARANDO;
        } else if (yaDisparo && t < MS_FIN_ENFRIANDO) {
            nuevo = EstadoEnemigo.ENFRIANDO;
        } else if (t >= ciclo - MS_CARGA) {
            nuevo = EstadoEnemigo.CARGANDO;
        } else {
            nuevo = EstadoEnemigo.REPOSO;
        }

        if (nuevo != estado) {
            estado = nuevo;
            ticksEnEstado = 0;
        } else {
            ticksEnEstado++;
        }
    }
}

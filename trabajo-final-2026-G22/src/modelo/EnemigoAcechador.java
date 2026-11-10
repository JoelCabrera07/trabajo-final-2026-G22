package modelo;
import java.awt.Rectangle;

public class EnemigoAcechador extends Enemigo {

    // Lo que esta haciendo el acechador.
    private enum Estado { OCULTO, APARECIENDO, PERSIGUIENDO, ATACANDO, DESAPARECIENDO }

    private double tiempoDeteccion; //segundos que el heroe debe quedarse quieto para que el acechador empiece a perseguirlo

    private double VELOCIDAD_PERSECUCION; //pixeles por frame
    private static final double DISTANCIA_APARICION= 180; //px de distancia al aparecer
    private static final double DISTANCIA_ATAQUE = 60;   //a menos de esto del heroe estira las manos (es solo visual)

    // Cuanto tarda en materializarse y en desvanecerse (mientras tanto no es solido)
    private static final long DURACION_APARICION_MS = 600;
    private static final long DURACION_DESAPARICION_MS = 480;

    // true: despues de golpear al heroe se desvanece y vuelve a esconderse hasta que el heroe
    // se quede quieto otra vez. false: lo persigue para siempre (como estaba antes).
    private static final boolean DESAPARECE_AL_GOLPEAR = true;

    private Estado estado = Estado.OCULTO;
    private long momentoCambioEstado = System.currentTimeMillis();

    // Guardamos la ultima posición del héroe para saber si se quedó quieto
    private double ultimaXHeroe;
    private double ultimaYHeroe;

    // Momento del ultimo frame en que se actualizó la posición del héroe, para medir el tiempo que estuvo quieto
    private long momentoUltimoMovimiento;

    private boolean mirandoDerecha = true;

    public EnemigoAcechador(double x, double y, double velocidad) {
        // Bicho raro que aparece si te quedás quieto
        super("Acechador", x, y, 20, 20, 2);
        this.tiempoDeteccion = 1.5; // Segundos antes de empezar a perseguirte
        this.momentoUltimoMovimiento = System.currentTimeMillis();
        this.VELOCIDAD_PERSECUCION = velocidad;
    }

    public void perseguir(Heroe h) {
        switch (estado) {
            case OCULTO:
                vigilar(h);
                break;
            case PERSIGUIENDO:
            case ATACANDO:
                moverHacia(h);
                break;
            default:
                // APARECIENDO y DESAPARECIENDO: se queda quieto mientras dura la transicion
                break;
        }
    }

    // Mientras esta oculto, mide cuanto tiempo lleva quieto el heroe
    private void vigilar(Heroe h) {
        double difX = Math.abs(h.getX() - ultimaXHeroe);
        double difY = Math.abs(h.getY() - ultimaYHeroe);
        if (difX > 3.0 || difY > 3.0) {
            // Si el héroe se movió, reiniciamos el tiempo de detección
            momentoUltimoMovimiento = System.currentTimeMillis();
            ultimaXHeroe = h.getX();
            ultimaYHeroe = h.getY();
            return;
        }
        //Revisamos si el héroe se quedó quieto el tiempo suficiente para activar al acechador
        long tiempoQuieto = System.currentTimeMillis() - momentoUltimoMovimiento;
        // Ojo: (long) tiempoDeteccion * 1000 convertia 1.5 en 1 ANTES de multiplicar (daba 1 s).
        // Con los parentesis se multiplica primero y da 1500 ms.
        if (tiempoQuieto >= (long) (tiempoDeteccion * 1000)) {
            //se cumplen los 1.5 segundos, el acechador aparece a cierta distancia del héroe
            double nuevaX = h.getX() + DISTANCIA_APARICION + (Math.random() * 100 - 50);
            if (nuevaX > 760) {
                // Si se iba a salir de la pantalla por la derecha, aparece del lado izquierdo
                nuevaX = h.getX() - DISTANCIA_APARICION - (Math.random() * 100 - 50);
            }
            setX(nuevaX);
            setY(h.getY() - 20 - (Math.random() * 60)); // Aparece un poco arriba del héroe
            mirandoDerecha = h.getX() > getX();
            cambiarEstado(Estado.APARECIENDO);
        }
    }

    //al estar activo flota lentamente hacia el héroe (atraviesa las plataformas)
    private void moverHacia(Heroe h) {
        if (h.getX() > getX()) {
            setX(getX() + VELOCIDAD_PERSECUCION);
        } else if (h.getX() < getX()) {
            setX(getX() - VELOCIDAD_PERSECUCION);
        }

        if (h.getY() > getY()) {
            setY(getY() + VELOCIDAD_PERSECUCION);
        } else if (h.getY() < getY()) {
            setY(getY() - VELOCIDAD_PERSECUCION);
        }

        // Mira hacia el heroe (con 2 px de margen para que no tiemble cuando estan alineados)
        double difX = (h.getX() + h.getAncho() / 2.0) - (getX() + getAncho() / 2.0);
        if (difX > 2) mirandoDerecha = true;
        if (difX < -2) mirandoDerecha = false;

        // Cerca del heroe estira las manos; si se aleja vuelve a flotar.
        // (60 para empezar y 80 para dejar, asi no cambia de estado a cada rato en el limite)
        double distancia = Math.hypot(h.getX() - getX(), h.getY() - getY());
        if (estado == Estado.PERSIGUIENDO && distancia < DISTANCIA_ATAQUE) {
            cambiarEstado(Estado.ATACANDO);
        } else if (estado == Estado.ATACANDO && distancia > DISTANCIA_ATAQUE + 20) {
            cambiarEstado(Estado.PERSIGUIENDO);
        }
    }

    private void cambiarEstado(Estado nuevo) {
        estado = nuevo;
        momentoCambioEstado = System.currentTimeMillis();
    }

    // Termina las transiciones cuando se cumple su duracion
    private void actualizarEstado() {
        long enEstado = getMsEnFase();
        if (estado == Estado.APARECIENDO && enEstado >= DURACION_APARICION_MS) {
            cambiarEstado(Estado.PERSIGUIENDO);   // termino de materializarse
        } else if (estado == Estado.DESAPARECIENDO && enEstado >= DURACION_DESAPARICION_MS) {
            cambiarEstado(Estado.OCULTO);         // vuelve a esconderse
            momentoUltimoMovimiento = System.currentTimeMillis();
        }
    }

    @Override
    public FaseEnemigo getFase() {
        switch (estado) {
            case APARECIENDO:    return FaseEnemigo.APARECIENDO;
            case PERSIGUIENDO:   return FaseEnemigo.FLOTANDO;
            case ATACANDO:       return FaseEnemigo.ATACANDO;
            case DESAPARECIENDO: return FaseEnemigo.DESAPARECIENDO;
            default:             return FaseEnemigo.NORMAL;
        }
    }

    @Override
    public long getMsEnFase() {
        return System.currentTimeMillis() - momentoCambioEstado;
    }

    @Override
    public boolean mirandoDerecha() {
        return mirandoDerecha;
    }

    //para que el panelJuego sepa si debe dibujar al acechador o no, ya que no aparece hasta que el heroe se queda quieto
    public boolean isActivo() {
        return estado != Estado.OCULTO;
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        // Mientras aparece o se desvanece todavia no es "solido": no lastima
        if (estado != Estado.PERSIGUIENDO && estado != Estado.ATACANDO) return;
        if (h.isInvencible()) return;
        // Se hace en este momento para que el acechador gaste su ataque y se desvanezca, si realmente golpe al heroe
        super.aplicarEfectoColision(h);
        if (DESAPARECE_AL_GOLPEAR) {
            cambiarEstado(Estado.DESAPARECIENDO);
        }
    }

    // Mientras esta oculto no tiene que chocar con nada (antes lastimaba aunque fuera invisible)
    @Override
    public Rectangle getHitbox() {
        if (estado == Estado.OCULTO) {
            return new Rectangle(-1000, -1000, 0, 0);
        }
        return super.getHitbox();
    }

    @Override
    public void actualizar(Heroe heroe, Escenario escenario) {
        perseguir(heroe);
        actualizarEstado();
    }

    @Override
    public boolean debeDibujarse() {
        return isActivo(); // Solo se dibuja si está activo
    }

    @Override
    public TipoEnemigo getTipo(){
        return TipoEnemigo.ACECHADOR;
    }
}
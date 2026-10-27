package modelo;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class EnemigoAcechador extends Enemigo {

    // Lo que esta haciendo el acechador. Cada estado tiene su animacion.
    private enum Estado { OCULTO, APARECIENDO, PERSIGUIENDO, ATACANDO, DESAPARECIENDO }

    private double tiempoDeteccion; //segundos que el heroe debe quedarse quieto para que el acechador empiece a perseguirlo

    private double VELOCIDAD_PERSECUCION; //pixeles por frame
    private static final double DISTANCIA_APARICION= 180; //px de distancia al aparecer
    private static final double DISTANCIA_ATAQUE = 60;   //a menos de esto del heroe estira las manos (es solo visual)

    // true: despues de golpear al heroe se desvanece y vuelve a esconderse hasta que el heroe
    // se quede quieto otra vez. false: lo persigue para siempre (como estaba antes).
    private static final boolean DESAPARECE_AL_GOLPEAR = true;

    private Estado estado = Estado.OCULTO;

    // Guardamos la ultima posición del héroe para saber si se quedó quieto
    private double ultimaXHeroe;
    private double ultimaYHeroe;

    // Momento del ultimo frame en que se actualizó la posición del héroe, para medir el tiempo que estuvo quieto
    private long momentoUltimoMovimiento;

    // --- Animaciones (cada una en version derecha e izquierda, como el resto de los enemigos) ---
    private BufferedImage[] aparecerDerecha, aparecerIzquierda;
    private BufferedImage[] flotarDerecha, flotarIzquierda;
    private BufferedImage[] atacarDerecha, atacarIzquierda;
    private BufferedImage[] desaparecerDerecha, desaparecerIzquierda;
    private int frameActual = 0;
    private int tickAnimacion = 0;
    private static final int VELOCIDAD_ANIMACION = 6; // cada 6 ticks cambia el cuadro
    private boolean mirandoDerecha = true;

    // Cada fotograma mide 68x72. El punto (34, 34) del dibujo (el cuerpo del fantasma)
    // se hace coincidir con el centro de la hitbox.
    private static final int SPRITE_ANCHO = 68;
    private static final int SPRITE_ALTO = 72;
    private static final int CENTRO_SPRITE_X = 34;
    private static final int CENTRO_SPRITE_Y = 34;


    public EnemigoAcechador(double x, double y, double velocidad) {
        // Bicho raro que aparece si te quedás quieto
        super("Acechador", x, y, 20, 20, Color.MAGENTA, 8, 2);
        this.tiempoDeteccion = 1.5; // Segundos antes de empezar a perseguirte
        this.momentoUltimoMovimiento = System.currentTimeMillis();
        this.VELOCIDAD_PERSECUCION = velocidad;

        this.aparecerDerecha = Sprites.getAcechadorAparecer();
        this.aparecerIzquierda = Sprites.voltearAnimacion(aparecerDerecha);
        this.flotarDerecha = Sprites.getAcechadorFlotar();
        this.flotarIzquierda = Sprites.voltearAnimacion(flotarDerecha);
        this.atacarDerecha = Sprites.getAcechadorAtacar();
        this.atacarIzquierda = Sprites.voltearAnimacion(atacarDerecha);
        this.desaparecerDerecha = Sprites.getAcechadorDesaparecer();
        this.desaparecerIzquierda = Sprites.voltearAnimacion(desaparecerDerecha);
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
                // APARECIENDO y DESAPARECIENDO: se queda quieto mientras dura la animacion
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
        // (60 para empezar y 80 para dejar, asi no cambia de animacion a cada rato en el limite)
        double distancia = Math.hypot(h.getX() - getX(), h.getY() - getY());
        if (estado == Estado.PERSIGUIENDO && distancia < DISTANCIA_ATAQUE) {
            cambiarEstado(Estado.ATACANDO);
        } else if (estado == Estado.ATACANDO && distancia > DISTANCIA_ATAQUE + 20) {
            cambiarEstado(Estado.PERSIGUIENDO);
        }
    }

    private void cambiarEstado(Estado nuevo) {
        estado = nuevo;
        frameActual = 0;
        tickAnimacion = 0;
    }

    private BufferedImage[] animacionActual() {
        switch (estado) {
            case APARECIENDO:    return mirandoDerecha ? aparecerDerecha : aparecerIzquierda;
            case ATACANDO:       return mirandoDerecha ? atacarDerecha : atacarIzquierda;
            case DESAPARECIENDO: return mirandoDerecha ? desaparecerDerecha : desaparecerIzquierda;
            default:             return mirandoDerecha ? flotarDerecha : flotarIzquierda;
        }
    }

    private void actualizarAnimacion() {
        if (estado == Estado.OCULTO) return;

        tickAnimacion++;
        if (tickAnimacion >= VELOCIDAD_ANIMACION) {
            tickAnimacion = 0;
            frameActual++;

            BufferedImage[] anim = animacionActual();
            int largo = (anim != null) ? anim.length : 4; // sin sprites igual respeta los tiempos
            if (frameActual >= largo) {
                if (estado == Estado.APARECIENDO) {
                    cambiarEstado(Estado.PERSIGUIENDO);   // termino de materializarse
                } else if (estado == Estado.DESAPARECIENDO) {
                    cambiarEstado(Estado.OCULTO);         // vuelve a esconderse
                    momentoUltimoMovimiento = System.currentTimeMillis();
                    setSprite(null);
                    return;
                } else {
                    frameActual = 0;                      // flotar y atacar se repiten
                }
            }
        }

        BufferedImage[] anim = animacionActual();
        if (anim != null) {
            setSprite(anim[frameActual]);
        }
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

        h.recibirDanio(this.getAtaque());
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

    // El dibujo (68x72) es mas grande que la hitbox (20x20): se centra sobre ella
    @Override
    public Rectangle getAreaSprite() {
        int centroX = (int) getX() + getAncho() / 2;
        int centroY = (int) getY() + getAlto() / 2;
        return new Rectangle(centroX - CENTRO_SPRITE_X, centroY - CENTRO_SPRITE_Y, SPRITE_ANCHO, SPRITE_ALTO);
    }

    @Override
    public void actualizar(Heroe heroe, Escenario escenario) {
        perseguir(heroe);
        actualizarAnimacion();
    }

    @Override
    public boolean debeDibujarse() {
        return isActivo(); // Solo se dibuja si está activo
    }
}

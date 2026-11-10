package modelo;

import java.awt.Rectangle;

public class EnemigoAcechador extends Enemigo {

    private double tiempoDeteccion; // segundos que el heroe debe quedarse quieto para que el acechador empiece a
                                    // perseguirlo

    private double velocidadPersecucion; // pixeles por frame
    private static final double DISTANCIA_APARICION = 180; // px de distancia al aparecer
    private static final double DISTANCIA_ATAQUE = 60; // a menos de esta distancia del heroe pasa a ATACANDO

    private EstadoEnemigo estado = EstadoEnemigo.OCULTO;

    // Guardamos la ultima posición del héroe para saber si se quedó quieto
    private double ultimaXHeroe;
    private double ultimaYHeroe;

    // Momento del ultimo frame en que se actualizó la posición del héroe, para
    // medir el tiempo que estuvo quieto
    private long momentoUltimoMovimiento;

    private boolean mirandoDerecha = true;

    // Cuánto duran (en frames del juego) la aparición y la desaparición.
    // Mientras dura, el acechador no lastima: es una regla del juego, no de la imagen.
    private static final int TICKS_APARECER = 30;
    private static final int TICKS_DESAPARECER = 24;
    private int ticksEnEstado = 0; // cuántos frames lleva en el estado actual

    public EnemigoAcechador(double x, double y, double velocidad) {
        // Bicho raro que aparece si te quedás quieto
        super("Acechador", x, y, 20, 20,2);
        this.tiempoDeteccion = 1.5; // Segundos antes de empezar a perseguirte
        this.momentoUltimoMovimiento = System.currentTimeMillis();
        this.velocidadPersecucion = velocidad;
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
                // APARECIENDO y DESAPARECIENDO: se queda quieto mientras aparece o desaparece
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
        // Revisamos si el héroe se quedó quieto el tiempo suficiente para activar al
        // acechador
        long tiempoQuieto = System.currentTimeMillis() - momentoUltimoMovimiento;
        // Ojo: (long) tiempoDeteccion * 1000 convertia 1.5 en 1 ANTES de multiplicar
        // (daba 1 s).
        // Con los parentesis se multiplica primero y da 1500 ms.
        if (tiempoQuieto >= (long) (tiempoDeteccion * 1000)) {
            // se cumplen los 1.5 segundos, el acechador aparece a cierta distancia del
            // héroe
            double nuevaX = h.getX() + DISTANCIA_APARICION + (Math.random() * 100 - 50);
            if (nuevaX > 760) {
                // Si se iba a salir de la pantalla por la derecha, aparece del lado izquierdo
                nuevaX = h.getX() - DISTANCIA_APARICION - (Math.random() * 100 - 50);
            }
            setX(nuevaX);
            setY(h.getY() - 20 - (Math.random() * 60)); // Aparece un poco arriba del héroe
            mirandoDerecha = h.getX() > getX();
            cambiarEstado(EstadoEnemigo.APARECIENDO);
        }
    }

    // al estar activo flota lentamente hacia el héroe (atraviesa las plataformas)
    private void moverHacia(Heroe h) {
        if (h.getX() > getX()) {
            setX(getX() + velocidadPersecucion);
        } else if (h.getX() < getX()) {
            setX(getX() - velocidadPersecucion);
        }

        if (h.getY() > getY()) {
            setY(getY() + velocidadPersecucion);
        } else if (h.getY() < getY()) {
            setY(getY() - velocidadPersecucion);
        }

        // Mira hacia el heroe (con 2 px de margen para que no tiemble cuando estan
        // alineados)
        double difX = (h.getX() + h.getAncho() / 2.0) - (getX() + getAncho() / 2.0);
        if (difX > 2)
            mirandoDerecha = true;
        if (difX < -2)
            mirandoDerecha = false;

        // Cerca del heroe estira las manos; si se aleja vuelve a flotar.
        double distancia = Math.hypot(h.getX() - getX(), h.getY() - getY());
        if (estado == EstadoEnemigo.PERSIGUIENDO && distancia < DISTANCIA_ATAQUE) {
            cambiarEstado(EstadoEnemigo.ATACANDO);
        } else if (estado == EstadoEnemigo.ATACANDO && distancia > DISTANCIA_ATAQUE + 20) {
            cambiarEstado(EstadoEnemigo.PERSIGUIENDO);
        }
    }

    private void cambiarEstado(EstadoEnemigo nuevo) {
        estado = nuevo;
        ticksEnEstado = 0;
    }

        // La aparición y la desaparición duran un tiempo fijo; al cumplirse, cambia de estado
    private void avanzarEstado() {
        ticksEnEstado++;
        if (estado == EstadoEnemigo.APARECIENDO && ticksEnEstado >= TICKS_APARECER) {
            cambiarEstado(EstadoEnemigo.PERSIGUIENDO);
        } else if (estado == EstadoEnemigo.DESAPARECIENDO && ticksEnEstado >= TICKS_DESAPARECER) {
            cambiarEstado(EstadoEnemigo.OCULTO);
            momentoUltimoMovimiento = System.currentTimeMillis();
        }
    }


    // para que el panelJuego sepa si debe dibujar al acechador o no, ya que no
    // aparece hasta que el heroe se queda quieto
    public boolean isActivo() {
        return estado != EstadoEnemigo.OCULTO;
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        // Mientras aparece o se desvanece todavia no es "solido": no lastima
        if (estado != EstadoEnemigo.PERSIGUIENDO && estado != EstadoEnemigo.ATACANDO)
            return;
        if (h.isInvencible())
            return;

        super.aplicarEfectoColision(h);
        cambiarEstado(EstadoEnemigo.DESAPARECIENDO); // despues de golpear se desvanece
    }

    // Mientras esta oculto no tiene que chocar con nada (antes lastimaba aunque
    // fuera invisible)
    @Override
    public Rectangle getHitbox() {
        if (estado == EstadoEnemigo.OCULTO) {
            return new Rectangle(-1000, -1000, 0, 0);
        }
        return super.getHitbox();
    }

    @Override
    public void actualizar(Heroe heroe, Escenario escenario) {
        perseguir(heroe);
        avanzarEstado();
    }

    @Override
    public boolean isVisible() {
        return isActivo(); // Solo se dibuja si está activo
    }

    @Override
    public TipoEntidad getTipo() {
        return TipoEntidad.ENEMIGO_ACECHADOR;
    }

    @Override
    public EstadoEnemigo getEstado() {
        return estado;
    }

    @Override
    public boolean isMirandoDerecha() {
        return mirandoDerecha; // mira hacia el héroe
    }

    @Override
    public int getTicksEnEstado() {
        return ticksEnEstado;
    }
}

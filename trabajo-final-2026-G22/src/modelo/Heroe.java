package modelo;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Heroe extends Entidad {

    // --- Constantes de gravedad ---
    private static final double GRAVEDAD = 0.5; // Cuanto aumenta la velocidad de caida por frame
    private static final double VELOCIDAD_CAIDA_MAXIMA = 12; // Tope, para que no caiga cada vez mas rapido sin limite
    private static final double MAX_CARGA = 15.0; // Cuanto se puede cargar el salto (en px/frame) antes de soltar la tecla y saltar
    private double velocidadX; // Cuánto se mueve por frame en horizontal (negativo=izquierda, positivo=derecha, 0=quieto)
    private double velocidadY; // Cuánto se mueve por frame en vertical (negativo=subiendo, positivo=cayendo)
    private double cargaSalto = 0; // Cuánto se ha cargado el salto (0 a MAX_CARGA); se acumula mientras el jugador mantiene presionada la tecla de salto
    private boolean cargandoSalto = false; // true si el jugador está manteniendo presionada la tecla de salto, false si la soltó y se debe ejecutar el salto
    private boolean enElSuelo; // true si está parado sobre una plataforma/piso, decide si puede saltar y si le aplica gravedad
    private boolean mirandoDerecha = true; // Hacia qué lado mira el personaje; sirve para dirección del salto y del sprite
    private static final double ESCALA_SALTO = Math.sqrt(0.75);// Escala de la altura del salto: sqrt(0.75) = 0.866, para que el salto sea un poco más bajo que la distancia horizontal recorrida
    private static final long DURACION_INVENCIBILIDAD_MS = 1000;// --- Invencibilidad tras recibir un golpe --- 1 segundo
    private long finInvencibilidad = 0;
    private final List<Observador> observadores = new CopyOnWriteArrayList<>();//lista de quienes quieren enterarse de lo que le pasa al heroe

    // --- Animaciones del Héroe según la hoja de sprites ---
    // Fila 1: Quieto (0, 1) y Movimiento (2, 3, 4, 5)
    private BufferedImage[] animacionQuieto;
    private BufferedImage[] animacionQuietoIzq;
    private BufferedImage[] animacionCorrer;
    private BufferedImage[] animacionCorrerIzq;

    // Fila 2: Tomando Impulso (0, 1, 2, 3) y Despegue (4)
    private BufferedImage[] animacionImpulso;
    private BufferedImage[] animacionImpulsoIzq;

    // Fila 3: Salto y Caída (0 a 5)
    private BufferedImage[] animacionSaltoCaida;
    private BufferedImage[] animacionSaltoCaidaIzq;

    // Contadores internos para cadencia de animación
    private int tickAnimacion = 0;
    private int frameCorrer = 0;
    private int frameQuieto = 0;
    private int ticksAterrizaje = 0;
    private int ticksDespegue = 0;
    private boolean estabaEnElSuelo = true;

    // Constructor: arma el Heroe pasándole todo lo genérico a Entidad e inicializa sus animaciones
    public Heroe(String nombre, double x, double y, int ancho, int alto, Color color, int hp, int ataque) {
        super(nombre, x, y, ancho, alto, color, hp, ataque);
        cargarSprites();
    }

    private void cargarSprites() {
        this.animacionQuieto = Sprites.getHeroeQuieto();
        this.animacionQuietoIzq = Sprites.voltearAnimacion(this.animacionQuieto);

        this.animacionCorrer = Sprites.getHeroeMovimiento();
        this.animacionCorrerIzq = Sprites.voltearAnimacion(this.animacionCorrer);

        this.animacionImpulso = Sprites.getHeroeImpulso();
        this.animacionImpulsoIzq = Sprites.voltearAnimacion(this.animacionImpulso);

        this.animacionSaltoCaida = Sprites.getHeroeSaltoCaida();
        this.animacionSaltoCaidaIzq = Sprites.voltearAnimacion(this.animacionSaltoCaida);

        if (this.animacionQuieto != null && this.animacionQuieto.length > 0) {
            this.setSprite(this.animacionQuieto[0]);
        }
    }

    // Se llama en cada frame del juego: aplica gravedad, mueve y actualiza el sprite actual
    public void actualizar() {
        this.actualizarCarga();
        velocidadY += GRAVEDAD;
        if (velocidadY > VELOCIDAD_CAIDA_MAXIMA) {
            velocidadY = VELOCIDAD_CAIDA_MAXIMA;
        }
        setX(getX() + velocidadX);
        setY(getY() + velocidadY);

        // Pared invisible a la izquierda
        if (getX() < 0) {
            setX(0);
        }
        // Pared invisible a la derecha
        if (getX() + getAncho() > 800) {
            setX(800 - getAncho());
        }

        this.actualizarAnimacion();
    }

    // Gestiona los estados y fotogramas correspondientes a cada fila de movimientos
    public void actualizarAnimacion() {
        if (animacionQuieto == null) return;

        tickAnimacion++;

        // Detectar el momento en que toca el suelo viniendo del aire
        if (!estabaEnElSuelo && enElSuelo) {
            ticksAterrizaje = 5; // Mostrar impacto de aterrizaje brevemente
        }
        estabaEnElSuelo = enElSuelo;

        // 1. FILA 2: TOMANDO IMPULSO (Carga progresiva del salto según porcentaje)
        if (cargandoSalto) {
            ticksAterrizaje = 0;
            ticksDespegue = 0;
            double pct = getPorcentajeCargaSalto();
            int idxImpulso;
            if (pct < 0.25) {
                idxImpulso = 0;
            } else if (pct < 0.50) {
                idxImpulso = 1;
            } else if (pct < 0.75) {
                idxImpulso = 2;
            } else {
                idxImpulso = 3;
            }
            setSprite(mirandoDerecha ? animacionImpulso[idxImpulso] : animacionImpulsoIzq[idxImpulso]);
            return;
        }

        // 2. FILA 3: SALTO Y CAÍDA (Cuando está en el aire)
        if (!enElSuelo) {
            ticksAterrizaje = 0;
            // Impulso inicial de despegue (Fila 2 Frame 4)
            if (ticksDespegue > 0) {
                ticksDespegue--;
                setSprite(mirandoDerecha ? animacionImpulso[4] : animacionImpulsoIzq[4]);
                return;
            }

            int idxAire;
            if (velocidadY < -3) {
                idxAire = 1; // Subiendo con puño arriba (Salto ascendente)
            } else if (velocidadY < 1.5) {
                idxAire = 2; // Cúspide del salto con brazos abiertos (Ápice)
            } else if (velocidadY < 5) {
                idxAire = 3; // En el aire girando / transición
            } else {
                idxAire = 4; // Caída rápida con capa al viento
            }
            setSprite(mirandoDerecha ? animacionSaltoCaida[idxAire] : animacionSaltoCaidaIzq[idxAire]);
            return;
        }

        // 3. FILA 3 FRAME 5: IMPACTO DE ATERRIZAJE AL TOCAR EL PISO
        if (ticksAterrizaje > 0) {
            ticksAterrizaje--;
            setSprite(mirandoDerecha ? animacionSaltoCaida[5] : animacionSaltoCaidaIzq[5]);
            return;
        }

        // 4. FILA 1: MOVIMIENTO (Caminar / Correr horizontalmente)
        if (Math.abs(velocidadX) > 0.1) {
            if (tickAnimacion % 6 == 0) {
                frameCorrer = (frameCorrer + 1) % animacionCorrer.length;
            }
            setSprite(mirandoDerecha ? animacionCorrer[frameCorrer] : animacionCorrerIzq[frameCorrer]);
            return;
        }

        // 5. FILA 1: QUIETO (Parado en reposo)
        if (tickAnimacion % 25 == 0) {
            frameQuieto = (frameQuieto + 1) % animacionQuieto.length;
        }
        setSprite(mirandoDerecha ? animacionQuieto[frameQuieto] : animacionQuietoIzq[frameQuieto]);
    }

    public void moverIzquierda() {
        velocidadX = -4;
        mirandoDerecha = false;
    }

    public void moverDerecha() {
        velocidadX = 4;
        mirandoDerecha = true;
    }

    public void detenerHorizontal() {
        velocidadX = 0;
    }

    public boolean isEnElSuelo() {
        return enElSuelo;
    }

    public void setEnElSuelo(boolean enElSuelo) {
        this.enElSuelo = enElSuelo;
    }

    public double getVelocidadY() {
        return this.velocidadY;
    }

    public void setVelocidadY(double velocidadY) {
        this.velocidadY = velocidadY;
    }

    public double getVelocidadX() {
        return this.velocidadX;
    }

    public void setVelocidadX(double velocidadX) {
        this.velocidadX = velocidadX;
    }

    public boolean isMirandoDerecha() {
        return this.mirandoDerecha;
    }

    public void iniciarCargaSalto() {
        if (isEnElSuelo()) {
            this.cargandoSalto = true;
            this.detenerHorizontal();
        }
    }

    public void actualizarCarga() {
        if (cargandoSalto && cargaSalto < MAX_CARGA) {
            cargaSalto += 0.5;
        }
    }

    public void ejecutarSalto() {
        if (cargandoSalto) {
            this.setVelocidadY(-(cargaSalto + 5.0) * ESCALA_SALTO);
            this.setEnElSuelo(false);
            this.cargandoSalto = false;
            this.cargaSalto = 0;
            this.ticksDespegue = 3; // 3 ticks con la pose de despegue
        }
    }

    public double getCargaSalto() {
        return this.cargaSalto;
    }

    public double getPorcentajeCargaSalto() {
        return (cargaSalto / MAX_CARGA);
    }

    public boolean isCargandoSalto() {
        return this.cargandoSalto;
    }

    public void reiniciarEstado() {
        this.velocidadX = 0;
        this.velocidadY = 0;
        this.cargaSalto = 0;
        this.cargandoSalto = false;
        this.enElSuelo = false;
        this.mirandoDerecha = true;
        this.ticksAterrizaje = 0;
        this.ticksDespegue = 0;
        if (this.animacionQuieto != null && this.animacionQuieto.length > 0) {
            this.setSprite(this.animacionQuieto[0]);
        }
    }

    // ---------- Patron Observer (Heroe es el SUJETO) ----------

    public boolean agregarObservador(Observador observador) {
        if (observador == null || observadores.contains(observador)) {
            return false;
        }
        return observadores.add(observador);
    }

    public boolean quitarObservador(Observador observador) {
        return observadores.remove(observador);
    }

    private void notificarObservadores(EventoJuego evento) {
        for (Observador o : observadores) {
            o.actualizar(evento, this);
        }
    }

    public boolean isInvencible() {
        return System.currentTimeMillis() < finInvencibilidad;
    }

    @Override
    public void recibirDanio(int cantidad) {
        if (!estaVivo() || isInvencible()) {
            return;
        }
        super.recibirDanio(cantidad);
        System.out.println("Vida del heroe: " + getHp());
        finInvencibilidad = System.currentTimeMillis() + DURACION_INVENCIBILIDAD_MS;
        notificarObservadores(EventoJuego.HEROE_RECIBIO_DANIO);
        if (!estaVivo()) {
            notificarObservadores(EventoJuego.HEROE_MURIO);
        }
    }
}

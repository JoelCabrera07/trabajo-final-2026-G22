package modelo;
import java.awt.Color; 

public class Heroe extends Entidad {

    // --- Constantes de gravedad ---
    private static final double GRAVEDAD = 0.5; // Cuanto aumenta la velocidad de caida por frame
    private static final double VELOCIDAD_CAIDA_MAXIMA = 12; // Tope, para que no caiga cada vez mas rapido sin limite

    private double velocidadX; // Cuánto se mueve por frame en horizontal (negativo=izquierda, positivo=derecha, 0=quieto)
    private double velocidadY; // Cuánto se mueve por frame en vertical (negativo=subiendo, positivo=cayendo)
    private boolean enElSuelo; // true si está parado sobre una plataforma/piso, decide si puede saltar y si le aplica gravedad
    private boolean mirandoDerecha = true; // Hacia qué lado mira el personaje; sirve para dirección del salto y del sprite

    // Constructor: arma el Heroe pasándole todo lo genérico a Entidad
    public Heroe(String nombre, double x, double y, int ancho, int alto, Color color, int hp, int ataque) {
        super(nombre, x, y, ancho, alto, color, hp, ataque); // Le delega la inicialización a la clase padre
    }

    // Se llama en cada frame del juego: aplica gravedad y mueve al héroe según su velocidad actual
    public void actualizar() {
        // Gravedad: cada frame acumula un poco mas de velocidad hacia abajo,
        // hasta un maximo, para que la caida se sienta acelerada (no lineal).
        velocidadY += GRAVEDAD;
        if (velocidadY > VELOCIDAD_CAIDA_MAXIMA) {
            velocidadY = VELOCIDAD_CAIDA_MAXIMA;
        }

        setX(getX() + velocidadX); // Suma la velocidad horizontal a la posición X actual
        setY(getY() + velocidadY); // Suma la velocidad vertical a la posición Y actual
    }

    // Se llama cuando el jugador presiona la tecla de mover a la izquierda
    public void moverIzquierda() {
        velocidadX = -4; // Fija una velocidad horizontal negativa (4 px por frame hacia la izquierda)
        mirandoDerecha = false; // Actualiza la orientación del personaje
    }

    // Se llama cuando el jugador presiona la tecla de mover a la derecha
    public void moverDerecha() {
        velocidadX = 4; // Fija una velocidad horizontal positiva (4 px por frame hacia la derecha)
        mirandoDerecha = true; // Actualiza la orientación del personaje
    }

    // Se llama cuando el jugador suelta la tecla de movimiento, para que no siga deslizando solo
    public void detenerHorizontal() {
        velocidadX = 0; // Frena el movimiento horizontal
    }

    // Consulta si el héroe está tocando el suelo/una plataforma
    public boolean isEnElSuelo() {
        return enElSuelo;
    }

    // Permite que otra clase (ej: JuegoManager al detectar colisión) marque si está o no en el suelo
    public void setEnElSuelo(boolean enElSuelo) {
        this.enElSuelo = enElSuelo;
    }

    // Devuelve la velocidad vertical actual; JuegoManager lo usa para saber si el héroe está cayendo
    public double getVelocidadY() {
        return this.velocidadY;
    }

    // Permite cambiar la velocidad vertical desde afuera; las Plataformas lo usan para
    // frenar la caída (setVelocidadY(0)) o para dar impulso (ej: trampolín, valor negativo)
    public void setVelocidadY(double velocidadY) {
        this.velocidadY = velocidadY;
    }

    // Devuelve la velocidad horizontal actual
    public double getVelocidadX() {
        return this.velocidadX;
    }

    // Permite cambiar la velocidad horizontal desde afuera (ej: una plataforma con hielo
    // que hace que el héroe resbale, o una plataforma móvil que lo arrastra)
    public void setVelocidadX(double velocidadX) {
        this.velocidadX = velocidadX;
    }
    public boolean isMirandoDerecha() {
        return this.mirandoDerecha; // Devuelve true si el héroe está mirando a la derecha, false si está mirando a la izquierda
    }
}
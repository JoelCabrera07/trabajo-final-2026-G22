package modelo;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Proyectil extends Entidad {

    private double velocidadX;
    private double velocidadY;

    // --- Animacion del orbe ---
    // El dibujo original mira a la derecha; al crearse se gira UNA vez hacia donde viaja.
    private BufferedImage[] frames;
    private int frameActual = 0;
    private int tickAnimacion = 0;
    private static final int VELOCIDAD_ANIMACION = 4;
    private static final int LADO_SPRITE = 44;     // lienzo cuadrado donde entra el orbe girado
    private static final double DIST_CABEZA = 9;   // la "cabeza" del orbe esta 9 px adelante del centro del dibujo
    private int desfaseX;                           // donde queda la cabeza dentro del lienzo girado
    private int desfaseY;

    public Proyectil(double x, double y, double dirX, double dirY, int ataque) {
        super("Proyectil", x, y, 10, 10, Color.YELLOW, 1, ataque);

        double magnitud = Math.sqrt(dirX * dirX + dirY * dirY);
        if (magnitud == 0) magnitud = 1;

        double velocidad = 6.0;
        this.velocidadX = (dirX / magnitud) * velocidad;
        this.velocidadY = (dirY / magnitud) * velocidad;

        double angulo = Math.atan2(velocidadY, velocidadX);
        BufferedImage[] orbe = Sprites.getOrbe();
        if (orbe != null) {
            frames = new BufferedImage[orbe.length];
            for (int i = 0; i < orbe.length; i++) {
                frames[i] = Sprites.rotar(orbe[i], angulo, LADO_SPRITE);
            }
            setSprite(frames[0]);
        }
        desfaseX = (int) Math.round(LADO_SPRITE / 2.0 + DIST_CABEZA * Math.cos(angulo));
        desfaseY = (int) Math.round(LADO_SPRITE / 2.0 + DIST_CABEZA * Math.sin(angulo));
    }

    public void actualizar() {
        setX(getX() + velocidadX);
        setY(getY() + velocidadY);

        if (frames != null) {
            tickAnimacion++;
            if (tickAnimacion >= VELOCIDAD_ANIMACION) {
                tickAnimacion = 0;
                frameActual = (frameActual + 1) % frames.length;
                setSprite(frames[frameActual]);
            }
        }
    }

    // La cabeza del orbe se dibuja sobre la hitbox y la estela queda detras
    @Override
    public Rectangle getAreaSprite() {
        int centroX = (int) getX() + getAncho() / 2;
        int centroY = (int) getY() + getAlto() / 2;
        return new Rectangle(centroX - desfaseX, centroY - desfaseY, LADO_SPRITE, LADO_SPRITE);
    }
}

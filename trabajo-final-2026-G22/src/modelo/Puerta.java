package modelo;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * La meta: un portal dimensional en lo mas alto de la torre.
 * Al tocarlo, el heroe completa el juego.
 */
public class Puerta extends Entidad implements Colisionable {

    private final BufferedImage[] fotogramas; // el remolino del portal
    private static final int MS_POR_FOTOGRAMA = 90;

    public Puerta(double x, double y) {
        super("Meta", x, y, 40, 60, new Color(150, 95, 45), 1, 0); // 40x60; hp y ataque no se usan
        this.fotogramas = Sprites.getPortal();
    }

    // La puerta no tiene metodo actualizar: el fotograma se elige segun el reloj,
    // asi el portal gira solo sin que nadie tenga que avisarle.
    @Override
    public BufferedImage getSprite() {
        if (fotogramas == null) return null; // sin imagenes se dibuja el rectangulo de color
        int i = (int) ((System.currentTimeMillis() / MS_POR_FOTOGRAMA) % fotogramas.length);
        return fotogramas[i];
    }

    // El portal (68x92) es mas grande que la hitbox: se centra sobre ella y apoya en el piso
    @Override
    public Rectangle getAreaSprite() {
        BufferedImage img = getSprite();
        if (img == null) return super.getAreaSprite();
        int x = (int) getX() + getAncho() / 2 - img.getWidth() / 2;
        int y = (int) getY() + getAlto() - img.getHeight();
        return new Rectangle(x, y, img.getWidth(), img.getHeight());
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        h.completarNivel();
    }
}

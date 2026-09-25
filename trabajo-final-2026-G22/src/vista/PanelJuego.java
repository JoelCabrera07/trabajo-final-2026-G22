package vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import modelo.Enemigo;
import modelo.Entidad;
import modelo.Escenario;
import modelo.Heroe;
import modelo.Plataforma;
import modelo.Proyectil;

/**
 * La vista del juego. No calcula fisica ni colisiones: solo lee las
 * referencias al escenario y al heroe que le pasaron por constructor, y
 * las dibuja. No conoce a JuegoManager -- esa dependencia va al reves
 * (el controlador es quien conoce a la vista, no al contrario).
 */
public class PanelJuego extends JPanel {

    private final Escenario escenario;
    private final Heroe heroe;

    public PanelJuego(Escenario escenario, Heroe heroe) {
        this.escenario = escenario;
        this.heroe = heroe;
        setPreferredSize(new java.awt.Dimension(800, 600));
        setBackground(new Color(30, 30, 40));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (escenario == null) {
            return;
        }

        for (Plataforma plataforma : escenario.getPlataformas()) {
            dibujarEntidad(g2, plataforma);
        }

        for (Enemigo enemigo : escenario.getEnemigos()) {
            // Cada enemigo decide solo si debe mostrarse (el Acechador solo se
            // dibuja despues de activarse); no hace falta preguntar de que tipo es.
            if (!enemigo.debeDibujarse()) {
                continue;
            }
            dibujarEntidad(g2, enemigo);
        }

        for (Proyectil proyectil : escenario.getProyectiles()) {
            dibujarEntidad(g2, proyectil);
        }

        if (heroe != null) {
            dibujarEntidad(g2, heroe);
        }
    }

    private void dibujarEntidad(Graphics2D g, Entidad entidad) {
        int x = (int) entidad.getX();
        int y = (int) entidad.getY();
        int ancho = entidad.getAncho();
        int alto = entidad.getAlto();

        g.setColor(entidad.getColor());
        g.fillRect(x, y, ancho, alto);

        g.setColor(Color.BLACK);
        g.drawRect(x, y, ancho, alto);

        g.setColor(Color.WHITE);
        g.drawString(entidad.getNombre(), x, y - 4);
    }
}
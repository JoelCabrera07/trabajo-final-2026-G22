package controlador;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import modelo.Heroe;
import modelo.Escenario;

public class ControladorTeclado extends KeyAdapter {
    private Heroe heroe;
    private final Escenario escenario; // el mismo candado que usan el juego y la vista


    public ControladorTeclado(Heroe heroe, Escenario escenario) {
        this.heroe = heroe;
        this.escenario = escenario;
    }

   @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        synchronized (escenario) {
            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                heroe.moverIzquierda();
            } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                heroe.moverDerecha();
            } else if (key == KeyEvent.VK_SPACE) {
                heroe.iniciarCargaSalto();
            }
        }
    }

     @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        synchronized (escenario) {
            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                heroe.detenerHorizontal();
            } else if (key == KeyEvent.VK_SPACE) {
                heroe.ejecutarSalto();
            }
        }
    }
}
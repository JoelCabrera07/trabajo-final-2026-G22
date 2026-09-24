package controlador;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import modelo.Heroe;

public class ControladorTeclado extends KeyAdapter {
    private Heroe heroe;

    public ControladorTeclado(Heroe heroe) {
        this.heroe = heroe;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            heroe.moverIzquierda();
        } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            heroe.moverDerecha();
        } else if (key == KeyEvent.VK_SPACE) {
            heroe.iniciarCargaSalto();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            heroe.detenerHorizontal();
        } else if (key == KeyEvent.VK_SPACE) {
            heroe.ejecutarSalto();
        }
    }
}
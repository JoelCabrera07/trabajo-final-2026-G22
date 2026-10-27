package vista;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class VentanaJuego extends JFrame {

    private boolean pantallaCompleta = false;

    public VentanaJuego() {
        // Le ponemos el título a la ventana en la barra superior
        this.setTitle("Jumping Hero");

        // Evitamos que el jugador cambie el tamaño de la ventana a mano.
        // (En pantalla completa el juego sigue calculando todo en 800x600 y solo se dibuja mas grande)
        this.setResizable(false);

        // Si no ponemos esto, al cerrar la ventana con la X el programa
        // sigue corriendo en segundo plano (el hilo del juego no se entera).
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Esto hace que la ventana aparezca bien centrada en el medio del monitor
        this.setLocationRelativeTo(null);

        // F11 entra y sale de pantalla completa, este donde este el foco (menu, juego o game over)
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_F11) {
                alternarPantallaCompleta();
                return true;
            }
            return false;
        });
    }

    public void alternarPantallaCompleta() {
        pantallaCompleta = !pantallaCompleta;
        // Para sacar o poner la barra de titulo hay que "esconder" la ventana un instante
        this.dispose();
        this.setUndecorated(pantallaCompleta);
        if (pantallaCompleta) {
            // Ocupa exactamente el monitor donde esta la ventana
            java.awt.GraphicsConfiguration gc = this.getGraphicsConfiguration();
            java.awt.Rectangle monitor = (gc != null) ? gc.getBounds()
                    : java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment()
                            .getDefaultScreenDevice().getDefaultConfiguration().getBounds();
            this.setBounds(monitor);
            this.setExtendedState(Frame.MAXIMIZED_BOTH);
        } else {
            this.setExtendedState(Frame.NORMAL);
            this.pack();
            this.setLocationRelativeTo(null);
        }
        this.setVisible(true);
        this.requestFocusInWindow(); // para que el teclado le siga llegando al juego
    }

    public boolean isPantallaCompleta() {
        return pantallaCompleta;
    }

    public void cambiarPanel(JPanel nuevoPanel) {
        this.getContentPane().removeAll(); // Elimina todos los componentes actuales
        this.add(nuevoPanel); // Agrega el nuevo panel
        if (!pantallaCompleta) {
            this.pack(); // Ajusta el tamaño de la ventana al nuevo panel
            this.setLocationRelativeTo(null); // Centra la ventana en la pantalla
        }
        this.revalidate(); // Revalida el contenedor para que se actualice la interfaz
        this.repaint(); // Repinta la ventana para reflejar los cambios
        this.requestFocusInWindow(); // Asegura que la ventana tenga el foco para recibir eventos de teclado
    }
}

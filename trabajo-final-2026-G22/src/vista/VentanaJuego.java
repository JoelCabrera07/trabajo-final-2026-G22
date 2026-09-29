package vista;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class VentanaJuego extends JFrame {

    public VentanaJuego() {
        // Le ponemos el título a la ventana en la barra superior
        this.setTitle("Jumping Hero");

        // Evitamos que el jugador cambie el tamaño de la ventana y nos rompa las colisiones
        this.setResizable(false);

        // Si no ponemos esto, al cerrar la ventana con la X el programa
        // sigue corriendo en segundo plano (el hilo del juego no se entera).
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Agregamos el panel donde se dibuja todo, y ajustamos la ventana
        // a su tamaño preferido (800x600, definido en PanelJuego).

        // Esto hace que la ventana aparezca bien centrada en el medio del monitor
        this.setLocationRelativeTo(null);
    }
    public void cambiarPanel(JPanel nuevoPanel) {
        this.getContentPane().removeAll(); // Elimina todos los componentes actuales
        this.add(nuevoPanel); // Agrega el nuevo panel
        this.pack(); // Ajusta el tamaño de la ventana al nuevo panel
        this.setLocationRelativeTo(null); // Centra la ventana en la pantalla
        this.revalidate(); // Revalida el contenedor para que se actualice la interfaz
        this.repaint(); // Repinta la ventana para reflejar los cambios
        this.requestFocusInWindow(); // Asegura que la ventana tenga el foco para recibir eventos de teclado
    }
}
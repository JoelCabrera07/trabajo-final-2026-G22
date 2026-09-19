package vista;
import javax.swing.JFrame;

public class VentanaJuego extends JFrame {

    public VentanaJuego(PanelJuego panel) {
        // Le ponemos el título a la ventana en la barra superior
        this.setTitle("Jumping Hero");

        // Evitamos que el jugador cambie el tamaño de la ventana y nos rompa las colisiones
        this.setResizable(false);

        // Si no ponemos esto, al cerrar la ventana con la X el programa
        // sigue corriendo en segundo plano (el hilo del juego no se entera).
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Agregamos el panel donde se dibuja todo, y ajustamos la ventana
        // a su tamaño preferido (800x600, definido en PanelJuego).
        this.add(panel);
        this.pack();

        // Esto hace que la ventana aparezca bien centrada en el medio del monitor
        this.setLocationRelativeTo(null);
    }
}
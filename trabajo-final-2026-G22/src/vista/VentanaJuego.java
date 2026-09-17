package vista;
import javax.swing.JFrame;
import java.awt.Dimension;

public class VentanaJuego extends JFrame {
    
    public VentanaJuego() {
        // Le ponemos el título a la ventana en la barra superior
        this.setTitle("Jumping Hero");
        
        // Evitamos que el jugador cambie el tamaño de la ventana y nos rompa las colisiones
        this.setResizable(false);
        
        // Definimos la resolución del juego (Por ahora clasico: 800x600)
        this.setSize(new Dimension(800, 600));
        
        // Esto hace que la ventana aparezca bien centrada en el medio del monitor
        this.setLocationRelativeTo(null); 
        
    }
}
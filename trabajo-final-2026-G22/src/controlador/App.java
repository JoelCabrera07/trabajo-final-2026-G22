package controlador;

import java.awt.Color;
import javax.swing.SwingUtilities;
import modelo.EnemigoAcorazado;
import modelo.EnemigoTerrestre;
import modelo.EnemigoVolador;
import modelo.Escenario;
import modelo.Heroe;
import modelo.PlataformaBarro;
import vista.PanelJuego;
import vista.VentanaJuego;

public class App {

    public static void main(String[] args) throws Exception {

        // --- 1. Armar el modelo (por ahora a mano, para probar la vista) ---
        Escenario escenario = new Escenario();
        // Plataforma grande, casi todo el ancho de la ventana, cerca del piso
        escenario.getPlataformas().add(new PlataformaBarro(50, 520, 700, 30));
        // Enemigo en la esquina izquierda de la plataforma(separado de heroe para que no se toque al arrancar) 
        escenario.getEnemigos().add(new EnemigoTerrestre(140, 480));
        // Enemigo volador en el centro de la ventana, a mitad de altura
        escenario.getEnemigos().add(new EnemigoVolador(400, 250));
        // Enemigo acorazado en el centro de la ventana, a mitad de altura
        escenario.getEnemigos().add(new EnemigoAcorazado(500, 480));
        // Enemigo torreta en la esquina derecha de la plataforma
        escenario.getEnemigos().add(new modelo.EnemigoTorreta(600, 100));

        // Heroe arranca arriba a la derecha, cae por gravedad hasta el otro extremo
        Heroe heroe = new Heroe("Heroe", 700, 50, 30, 40, Color.BLUE, 100, 10);

        // --- 2. Conectar el modelo con el controlador ---
        JuegoManager manager = JuegoManager.getInstance();
        manager.setEscenario(escenario);
        manager.setHeroe(heroe);

        // --- 3. Armar la vista y mostrarla (esto va en el hilo de eventos de Swing) ---
        PanelJuego panel = new PanelJuego(escenario, heroe);
        manager.setVista(panel);

        SwingUtilities.invokeLater(() -> {
            VentanaJuego ventana = new VentanaJuego(panel);
            ventana.addKeyListener(new ControladorTeclado(heroe)); // Conectamos el teclado
            ventana.setFocusable(true); // Fundamental para que la ventana capte las teclas
            ventana.requestFocusInWindow(); // Fundamental para que la ventana capte las teclas
            ventana.setVisible(true); // Arranca la ventana y el juego
        });

        // --- 4. Arrancar el motor del juego en un hilo APARTE ---
        // Si lo llamaramos en este mismo hilo (o adentro del invokeLater de
        // arriba), el while(true) de buclePrincipal() nunca terminaria y se
        // congelaria la ventana entera, porque bloquearia el hilo que Swing
        // necesita para dibujar y atender el teclado/mouse.
        Thread hiloDelJuego = new Thread(() -> manager.iniciarJuego()); // Arranca el bucle principal del juego en un hilo aparte
        hiloDelJuego.start(); // Arranca el hilo del juego, que ejecuta buclePrincipal() en paralelo con el hilo de eventos de Swing
    }
}
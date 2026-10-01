package controlador;

import java.awt.Color;
import javax.swing.SwingUtilities;
import modelo.EnemigoAcorazado;
import modelo.EnemigoTerrestre;
import modelo.EnemigoVolador;
import modelo.Escenario;
import modelo.Heroe;
import modelo.Plataforma;
import modelo.PlataformaBarro;
import modelo.PlataformaNormal;
import vista.MenuPrincipal;
import vista.PanelJuego;
import vista.VentanaJuego;

public class App {

    public static void main(String[] args) throws Exception {

        // --- 1. Armar el modelo (por ahora a mano, para probar la vista) ---
        Escenario escenario = new Escenario();
        escenario.cargarPantalla(1); // Carga la primera pantalla del juego 
        // Plataforma grande, casi todo el ancho de la ventana, cerca del piso
        escenario.getPlataformas().add(new PlataformaNormal(0, 550, 800, 50));

        escenario.getPlataformas().add(new PlataformaNormal(100,450,120,20));
        escenario.getPlataformas().add(new PlataformaNormal(300,340,120,20));
        escenario.getPlataformas().add(new PlataformaNormal(550,240,120,20));

        escenario.getPlataformas().add(new PlataformaBarro(300, 120, 120, 20));
        // Enemigo en la esquina izquierda de la plataforma(separado de heroe para que no se toque al arrancar) 
        escenario.getEnemigos().add(new EnemigoTerrestre(120, 420));
        // Enemigo volador en el centro de la ventana, a mitad de altura
        escenario.getEnemigos().add(new EnemigoVolador(400, 250));
        // Enemigo acorazado en el centro de la ventana, a mitad de altura
        escenario.getEnemigos().add(new EnemigoAcorazado(500, 480));
        // Enemigo torreta en la esquina derecha de la plataforma
        escenario.getEnemigos().add(new modelo.EnemigoTorreta(600, 100));

        // Heroe arranca arriba a la derecha, cae por gravedad hasta el otro extremo
        Heroe heroe = new Heroe("Heroe", 50, 500, 30, 40, Color.BLUE, 60, 10);

        // --- 2. Conectar el modelo con el controlador ---
        JuegoManager manager = JuegoManager.getInstance();
        manager.setEscenario(escenario);
        manager.setHeroe(heroe);

        // --- 3. Armar la vista y mostrarla (esto va en el hilo de eventos de Swing) ---
        PanelJuego panel = new PanelJuego(escenario, heroe);
        MenuPrincipal menu = new MenuPrincipal(); // Creamos el menu principal
        manager.setVista(panel);

       SwingUtilities.invokeLater(() -> {
            VentanaJuego ventana = new VentanaJuego(); // Sin el (panel)
            ventana.cambiarPanel(menu); // Asegurate de que la variable de arriba se llame MenuPrincipal
            ventana.setVisible(true);

            // Los botones van ADENTRO de este bloque
            menu.getBotonSalir().addActionListener(e -> System.exit(0));

            menu.getBotonJugar().addActionListener(e -> {
                ventana.cambiarPanel(panel); 
                ventana.addKeyListener(new ControladorTeclado(heroe));
                
                // El hilo del juego arranca solo cuando apretás "Jugar"
                Thread hiloDelJuego = new Thread(() -> manager.iniciarJuego());
                hiloDelJuego.start();
            });
        });
}
}
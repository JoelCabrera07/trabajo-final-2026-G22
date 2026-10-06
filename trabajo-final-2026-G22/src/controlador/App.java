package controlador;

import java.awt.Color;
import javax.swing.SwingUtilities;
import modelo.Escenario;
import modelo.Heroe;
import modelo.Sprites;
import vista.MenuPrincipal;
import vista.PanelJuego;
import vista.PantallaGameOver;
import vista.VentanaJuego;

public class App {

    public static void main(String[] args) throws Exception {

        // --- 1. Armar el modelo ---
        Escenario escenario = new Escenario();
        // cargarPantalla(1) ya crea las plataformas y enemigos de la pantalla 1
        // (con sus sprites puestos adentro de Escenario), no hace falta
        // agregarlos de nuevo aca a mano.
        escenario.cargarPantalla(1);

        // Heroe arranca arriba a la derecha, cae por gravedad hasta el otro extremo
        Heroe heroe = new Heroe("Heroe", 50, 500, 30, 40, Color.BLUE, 60, 10);
        heroe.setSprite(Sprites.cargar("/assets/heroe.png"));

        // --- 2. Conectar el modelo con el controlador ---
        JuegoManager manager = JuegoManager.getInstance();
        manager.setEscenario(escenario);
        manager.setHeroe(heroe);

        // --- 3. Armar la vista y mostrarla (esto va en el hilo de eventos de Swing) ---
        PanelJuego panel = new PanelJuego(escenario, heroe);
        MenuPrincipal menu = new MenuPrincipal(); // Creamos el menu principal
        PantallaGameOver pantallaGameOver = new PantallaGameOver();
        manager.setVista(panel);

       SwingUtilities.invokeLater(new Runnable() {
        public void run(){
            VentanaJuego ventana = new VentanaJuego(); // Sin el (panel)
            ventana.cambiarPanel(menu); // Asegurate de que la variable de arriba se llame MenuPrincipal
            ventana.setVisible(true);

            // Los botones van ADENTRO de este bloque
            menu.getBotonSalir().addActionListener(e -> System.exit(0));

            menu.getBotonJugar().addActionListener(e -> {
                menu.detenerAudio(); //Para detener el sonido cuando le damos al boton Jugar
                ventana.cambiarPanel(panel); 
                ventana.addKeyListener(new ControladorTeclado(heroe));
                
                // El hilo del juego arranca solo cuando apretás "Jugar"
                Thread hiloDelJuego = new Thread(() -> manager.iniciarJuego());
                hiloDelJuego.start();
            });
            // --- CONEXION DEL GAME OVER ---
            //1-Cuando el heroe se muere JuegoManager avisa y cambiamos de panel
            manager.setEventoGameOver(() -> {
                ventana.cambiarPanel(pantallaGameOver);
            });
            //2 BOTON "Salir al menu"
            pantallaGameOver.getBotonMenu().addActionListener(e -> { // Alvolver almenu dejamos posiciado para un nuevojuego
                heroe.setHp(heroe.getHpMax());
                heroe.setX(50);
                heroe.setY(500);
                ventana.cambiarPanel(menu);
            });

            //3 BOTON "Reintentar"
            pantallaGameOver.getBotonReintentar().addActionListener(e -> {
                //Revivimos al heroe(cambia el 100 de vida por la vida incial del juego)
                heroe.setHp(heroe.getHpMax());

                //Lo devolvemos a la posicioninicial
                heroe.setX(50);
                heroe.setY(500);

                //Recargamos el escenario y mostramos el panel del juego
                escenario.cargarPantalla(1);
                ventana.cambiarPanel(panel);

                //Arrancamos un hilo nuevopara el motor del juego
                Thread nuevoHilo = new Thread(() -> manager.iniciarJuego());
                nuevoHilo.start();
            });
        };
});
}
}
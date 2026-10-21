package controlador;

import java.awt.Color;
import javax.swing.SwingUtilities;
import modelo.Escenario;
import modelo.Heroe;
import modelo.Sprites;
import modelo.FabricaNivel1;
import vista.MenuPrincipal;
import vista.PanelJuego;
import vista.PantallaGameOver;
import vista.VentanaJuego;

public class App {

    public static void main(String[] args) throws Exception {

        // --- 1. Armar el modelo ---
        Escenario escenario = new Escenario();
        escenario.cargarPantalla(1, new FabricaNivel1());

        // Heroe con dimensiones proporcionales a su hoja de sprites (32x48)
        Heroe heroe = new Heroe("Heroe", 50, 500, 32, 48, Color.BLUE, 30, 10);

        // --- 2. Conectar el modelo con el controlador ---
        JuegoManager manager = JuegoManager.getInstance();
        manager.setEscenario(escenario);
        manager.setHeroe(heroe);
        heroe.agregarObservador(manager); // JuegoManager se entera cuando el heroe muere

        // --- 3. Armar la vista y mostrarla (esto va en el hilo de eventos de Swing) ---
        PanelJuego panel = new PanelJuego(escenario, heroe);
        MenuPrincipal menu = new MenuPrincipal();
        PantallaGameOver pantallaGameOver = new PantallaGameOver();
        manager.setVista(panel);

        // --- 4. Conectar Base de Datos SQLite ---
        GuardadorPuntaje guardadorPuntaje = null;
        try {
            java.sql.Connection conexionDB = persistencia.Conexion.getInstancia().get();
            modelo.PuntajeDAO puntajesDAO = new persistencia.PuntajeDAOSQLite(conexionDB);
            guardadorPuntaje = new GuardadorPuntaje(puntajesDAO);
            heroe.agregarObservador(guardadorPuntaje); // escucha cuando el heroe muere
            System.out.println("Base de datos conectada correctamente.");
        } catch (Exception e) {
            System.err.println("No se pudo iniciar la base de datos: " + e.getMessage());
        }
        final GuardadorPuntaje finalGuardador = guardadorPuntaje;

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                VentanaJuego ventana = new VentanaJuego();
                ventana.cambiarPanel(menu);
                ventana.setVisible(true);

                // Escuchador de teclado para el héroe
                ventana.addKeyListener(new ControladorTeclado(heroe));

                // Botón "Salir" del menú
                menu.getBotonSalir().addActionListener(e -> System.exit(0));

                // Botón "Jugar" del menú
                menu.getBotonJugar().addActionListener(e -> {
                    menu.detenerAudio(); // Detener música del menú
                    manager.reiniciarMetros();
                    heroe.reiniciarEstado();
                    ventana.cambiarPanel(panel); 
                    
                    Thread hiloDelJuego = new Thread(() -> manager.iniciarJuego());
                    hiloDelJuego.start();
                });

                // --- CONEXION DEL GAME OVER ---
                // 1- Cuando el heroe muere, JuegoManager avisa y cambiamos de panel mostrando metros
                manager.setEventoGameOver(() -> {
                    int metros = manager.getMetrosMaximos();
                    pantallaGameOver.mostrarPuntaje(metros);
                    ventana.cambiarPanel(pantallaGameOver);
                });

                // Botón "Guardar" puntaje en base de datos SQLite (en hilo secundario para concurrencia)
                pantallaGameOver.getBotonGuardar().addActionListener(e -> {
                    String nombre = pantallaGameOver.getNombreIngresado();
                    if (nombre.isEmpty()) {
                        pantallaGameOver.mostrarError("Por favor ingrese un nombre.");
                        return;
                    }
                    if (finalGuardador == null) {
                        pantallaGameOver.mostrarError("Error: Base de datos no conectada.");
                        return;
                    }
                    int metros = manager.getMetrosMaximos();
                    new Thread(() -> {
                        boolean ok = finalGuardador.guardarPuntaje(nombre, metros);
                        SwingUtilities.invokeLater(() -> {
                            if (ok) {
                                pantallaGameOver.mostrarConfirmacion("¡Puntaje guardado exitosamente!");
                            } else {
                                pantallaGameOver.mostrarError("Error al guardar en la base de datos.");
                            }
                        });
                    }).start();
                });

                // Permitir presionar Enter en el campo de texto para guardar directo
                pantallaGameOver.getCampoNombre().addActionListener(e -> {
                    if (pantallaGameOver.getBotonGuardar().isEnabled()) {
                        pantallaGameOver.getBotonGuardar().doClick();
                    }
                });

                // 2 BOTON "Salir al menu"
                pantallaGameOver.getBotonMenu().addActionListener(e -> {
                    manager.reiniciarMetros();
                    heroe.reiniciarEstado();
                    heroe.setHp(heroe.getHpMax());
                    heroe.setX(50);
                    heroe.setY(500);
                    ventana.cambiarPanel(menu);
                });

                // 3 BOTON "Reintentar"
                pantallaGameOver.getBotonReintentar().addActionListener(e -> {
                    manager.reiniciarMetros();
                    heroe.reiniciarEstado();
                    heroe.setHp(heroe.getHpMax());
                    heroe.setX(50);
                    heroe.setY(500);

                    // Recargamos el escenario y mostramos el panel del juego
                    escenario.cargarPantalla(1, new FabricaNivel1());
                    ventana.cambiarPanel(panel);

                    // Arrancamos un hilo nuevo para el motor del juego
                    Thread nuevoHilo = new Thread(() -> manager.iniciarJuego());
                    nuevoHilo.start();
                });
            }
        });
    }
}

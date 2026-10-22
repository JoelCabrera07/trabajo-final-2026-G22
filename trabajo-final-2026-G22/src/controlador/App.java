package controlador;

import java.awt.Color;
import javax.swing.SwingUtilities;
import modelo.Escenario;
import modelo.Heroe;
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

                menu.getBotonRanking().addActionListener(e -> {
                    try {
                        // 1. Reutilizamos tu conexión a la base de datos
                        java.sql.Connection conexionDB = persistencia.Conexion.getInstancia().get();
                        modelo.PuntajeDAO dao = new persistencia.PuntajeDAOSQLite(conexionDB);
                        
                        // 2. Pedimos los 5 mejores
                        java.util.List<modelo.Puntaje> listaMejores = dao.mejores(5);
                        
                        // 3. Validamos si hay puntajes o está vacía
                        if (listaMejores.isEmpty()) {
                            javax.swing.JOptionPane.showMessageDialog(ventana, "Todavía no hay puntajes guardados.", "Ranking", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            StringBuilder texto = new StringBuilder();
                            texto.append("🏆 TOP 5 MEJORES JUGADORES 🏆\n\n");
                            
                            int posicion = 1;
                            for (modelo.Puntaje p : listaMejores) {
                                texto.append(posicion).append("° - ")
                                     .append(p.getJugador()).append(": ")
                                     .append(p.getPuntos()).append(" puntos\n");
                                posicion++;
                            }
                            // Muestra el mensaje sobre la "ventana" principal
                            javax.swing.JOptionPane.showMessageDialog(ventana, texto.toString(), "Salón de la Fama", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (Exception ex) {
                        javax.swing.JOptionPane.showMessageDialog(ventana, "Error al conectar con la base de datos.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                        System.err.println("Error en ranking: " + ex.getMessage());
                    }
                });

                // Botón "Jugar" del menú
                menu.getBotonJugar().addActionListener(e -> {
                    menu.detenerAudio(); // Detener música del menú
                    panel.reproducirMusica();
                    manager.reiniciarMetros();
                    heroe.reiniciarEstado();
                    heroe.setHp(heroe.getHpMax());
                    heroe.setX(50);
                    heroe.setY(500);
                    escenario.cargarPantalla(1, new FabricaNivel1()); // siempre se arranca desde la pantalla 1
                    panel.cargarFondoNivel(1);
                    ventana.cambiarPanel(panel); 
                    
                    Thread hiloDelJuego = new Thread(() -> manager.iniciarJuego());
                    hiloDelJuego.start();
                });

                // --- CONEXION DEL GAME OVER ---
                // 1a- Cuando el heroe muere, JuegoManager avisa y cambiamos de panel mostrando metros
                manager.setEventoGameOver(() -> {
                    panel.detenerMusica();
                    panel.detenerAmbienteNivel4();
                    panel.reproducirEfectoSFX("/recursos/Escenario/Metros_Below.wav");
                    int metros = manager.getMetrosMaximos();
                    pantallaGameOver.mostrarPuntaje(metros);
                    ventana.cambiarPanel(pantallaGameOver);
                });
                // 1b- Cuando el heroe llega a la puerta de la meta, JuegoManager avisa y mostramos la pantalla de victoria
                manager.setEventoNivelCompletado(() -> {
                    panel.detenerMusica();
                    panel.detenerAmbienteNivel4();
                    panel.reproducirEfectoSFX("/recursos/Escenario/Victory_Lap.wav");
                    int metros = manager.getMetrosMaximos();
                    pantallaGameOver.mostrarVictoria(metros);
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
                    panel.detenerMusica();
                    panel.detenerEfecto();
                    menu.reproducirAudioAmbiente("/recursos/Menu/Ambiente_Menu.wav");
                    manager.reiniciarMetros();
                    heroe.reiniciarEstado();
                    heroe.setHp(heroe.getHpMax());
                    heroe.setX(50);
                    heroe.setY(500);
                    ventana.cambiarPanel(menu);
                });

                // 3 BOTON "Reintentar"
                pantallaGameOver.getBotonReintentar().addActionListener(e -> {
                    panel.detenerEfecto();
                    manager.reiniciarMetros();
                    panel.reproducirMusica();
                    heroe.reiniciarEstado();
                    heroe.setHp(heroe.getHpMax());
                    heroe.setX(50);
                    heroe.setY(500);

                    // Recargamos el escenario y mostramos el panel del juego
                    escenario.cargarPantalla(1, new FabricaNivel1());
                    panel.cargarFondoNivel(1);
                    ventana.cambiarPanel(panel);

                    // Arrancamos un hilo nuevo para el motor del juego
                    Thread nuevoHilo = new Thread(() -> manager.iniciarJuego());
                    nuevoHilo.start();
                });
            }
        });
    }
}

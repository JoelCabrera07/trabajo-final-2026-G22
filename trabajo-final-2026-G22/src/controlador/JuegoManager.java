package controlador;
import java.awt.Rectangle; //El asterisco importa todas las clases del paquete modelo
import modelo.*;
import vista.PanelJuego;

public class JuegoManager implements Observador {

    private static JuegoManager instance;

    private Escenario escenarioActual;
    private Heroe heroe;
    private boolean juegoCorriendo;

    // Guarda donde estaba el heroe en el frame anterior, para poder detectar
    // si "recien" aterrizo sobre una plataforma (y no aplicar el efecto en
    // cada frame que se solapen).
    private double yAnteriorHeroe;

    private PanelJuego vista;
    // Variable para avisarle a App.java que el juego termino
    private Runnable eventoGameOver;

    public void setEventoGameOver(Runnable evento){
        this.eventoGameOver = evento;
    }
    private JuegoManager() {
        this.juegoCorriendo = false;
    }

    public static JuegoManager getInstance() {
        if (instance == null) {
            instance = new JuegoManager();
        }
        return instance;
    }

    // --- Conexion con el modelo y la vista, desde App.java ---

    public void setHeroe(Heroe heroe) {
        this.heroe = heroe;
        this.yAnteriorHeroe = heroe.getY();
    }

    public void setEscenario(Escenario escenario) {
        this.escenarioActual = escenario;
    }

    public void setVista(PanelJuego vista) {
        this.vista = vista;
    }

    public void iniciarJuego() {
        this.juegoCorriendo = true;
        System.out.println("¡Iniciando el motor del juego!");
        buclePrincipal();
    }

    private void buclePrincipal() {
        while (juegoCorriendo) {

            // PASO A: Actualizar logicas (fisicas, movimiento, trayectoria)
            actualizarFisicas();
    
            // PASO B: Chequear colisiones (Heroe tocando cofres, enemigos o plataformas)
            verificarColisiones();

            // PASO C: Mandar a redibujar la pantalla (La Vista)
            if (vista != null) {
                vista.repaint();
            }

            // Un pequeño freno para que la compu no explote calculando a la velocidad de la luz
            try {
                Thread.sleep(16); // 16 milisegundos = aprox 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void actualizarFisicas() {
        if (heroe == null || escenarioActual == null) return;
        // 1. Actualiza la posición del héroe
        heroe.actualizar();

        // 2. Busca plataformas móviles y las hace patrullar
        for (Plataforma p : escenarioActual.getPlataformas()) {
            p.actualizar();
        }

        // 3. Hace patrullar a los enemigos terrestres
        for (Enemigo e : escenarioActual.getEnemigos()){
            e.actualizar(heroe, escenarioActual);
        }

        // 4. Mover todos los proyectiles que ya están en vuelo
        for (Proyectil proyectil : escenarioActual.getProyectiles()) {
            proyectil.actualizar();
        }
        // Transicion de pantalla hacia arriba: si el heroe supera el techo
        if (heroe.getY() < 0) {
            int pantalla = escenarioActual.getPantallaActual();
            if (pantalla == 1) {
                escenarioActual.cargarPantalla(2, new FabricaNivel2());
                heroe.setY(520);
            } else if (pantalla == 2) {
                escenarioActual.cargarPantalla(3, new FabricaNivel3());
                heroe.setY(520);
            } else if (pantalla == 3) {
                escenarioActual.cargarPantalla(4, new FabricaNivel4());
                heroe.setY(520);
            } else if (pantalla == 4) {
                // Llegó a la cima más alta (Nivel 4)
                heroe.setY(0);
                heroe.setVelocidadY(0);
            }
        }
        // Transicion hacia abajo si cae al vacio
        if (heroe.getY() > 650) {
            int pantalla = escenarioActual.getPantallaActual();
            if (pantalla == 4) {
                escenarioActual.cargarPantalla(3, new FabricaNivel3());
                heroe.setY(0);
                heroe.setVelocidadY(0);
            } else if (pantalla == 3) {
                escenarioActual.cargarPantalla(2, new FabricaNivel2());
                heroe.setY(0);
                heroe.setVelocidadY(0);
            } else if (pantalla == 2) {
                escenarioActual.cargarPantalla(1, new FabricaNivel1());
                heroe.setY(0); // Cae desde el techo de la pantalla 1
                heroe.setVelocidadY(0);
            } else {
                // Si cae al vacío en la pantalla 1, vuelve al piso base
                heroe.setX(50);
                heroe.setY(500);
                heroe.setVelocidadY(0);
            }
        }
    }

    
    private void verificarColisiones() {
        // Verificamos si el heroe no es nulo y si el escenario ya esta cargado para evitar errores
        if (heroe == null || escenarioActual == null) return;

        // --- Colisiones con enemigos ---
        escenarioActual.getPlataformas().removeIf(plataforma -> plataforma.debeEliminarse()); // Eliminamos las plataformas rompibles (como plataformaHielo) destruidas antes de verificar colisiones
        Rectangle hitboxHeroe = heroe.getHitbox(); // Obtenemos la hitbox del héroe para usarla en las colisiones
        for (Enemigo enemigo : escenarioActual.getEnemigos()) {
            if (hitboxHeroe.intersects(enemigo.getHitbox())) {
                System.out.println("¡Colisión detectada con: " + enemigo.getNombre() + "!");
                enemigo.aplicarEfectoColision(heroe);
            }
        }

        for (Proyectil p: escenarioActual.getProyectiles()) {
            if (hitboxHeroe.intersects(p.getHitbox())) {
                System.out.println("¡Colisión detectada con un proyectil!");
                heroe.recibirDanio(p.getAtaque());
                p.setX(-9999);// Mueve el proyectil fuera de la pantalla para "destruirlo"
            
            }
        }
            
        //Sacamos los proyectiles que ya se salieron de la pantalla para no seguir calculando colisiones con ellos
        escenarioActual.getProyectiles().removeIf(p -> p.getX() < -100 || p.getX() > 900 || p.getY() < -100 || p.getY() > 700
        );
      
        
        // --- Colisiones con plataformas ---
        hitboxHeroe = heroe.getHitbox();
        double bordeInferiorAnterior = yAnteriorHeroe + hitboxHeroe.height;
        double bordeSuperiorAnterior = yAnteriorHeroe;

        boolean apoyadoEnAlgunaPlataforma = false;

        for (Plataforma plataforma : escenarioActual.getPlataformas()) {

            // Una plataforma rompible ya destruida no debe seguir colisionando
            if (plataforma.debeEliminarse()) {
                continue;
            }

            Rectangle hitboxPlataforma = plataforma.getHitbox();
            double bordeSuperiorPlataforma = hitboxPlataforma.y;
            double bordeInferiorPlataforma = hitboxPlataforma.y + hitboxPlataforma.height;

            boolean solapaHorizontal = hitboxHeroe.x + hitboxHeroe.width > hitboxPlataforma.x
                    && hitboxHeroe.x < hitboxPlataforma.x + hitboxPlataforma.width;

            if (!solapaHorizontal) {
                continue;
            }

            boolean solapaVertical = hitboxHeroe.y < bordeInferiorPlataforma
                    && (hitboxHeroe.y + hitboxHeroe.height) > bordeSuperiorPlataforma;

            // 1. ATERRIZAJE DESDE ARRIBA:
            // El héroe cae o está en reposo, venía de arriba y su borde inferior toca o cruza la plataforma
            if (heroe.getVelocidadY() >= 0 && bordeInferiorAnterior <= bordeSuperiorPlataforma + 6 && solapaVertical) {
                heroe.setY(bordeSuperiorPlataforma - hitboxHeroe.height);
                plataforma.aplicarEfectoColision(heroe);
                apoyadoEnAlgunaPlataforma = true;
                hitboxHeroe = heroe.getHitbox();
                continue;
            }

            // 2. CHOQUE DESDE ABAJO (GOLPE DE CABEZA / NO ATRAVESAR):
            // El héroe va subiendo, venía de abajo y su cabeza impacta contra la base de la plataforma
            if (heroe.getVelocidadY() < 0 && bordeSuperiorAnterior >= bordeInferiorPlataforma - 6 && solapaVertical) {
                heroe.setY(bordeInferiorPlataforma);
                heroe.setVelocidadY(0);
                heroe.setEnElSuelo(false);

                // Si es una plataforma rompible, se rompe al impactarla desde abajo
                if (plataforma instanceof PlataformaRompible) {
                    ((PlataformaRompible) plataforma).romper();
                }
                hitboxHeroe = heroe.getHitbox();
            }
        }

        // Si el héroe no está apoyado en ninguna plataforma y está cayendo, no está en el suelo
        if (!apoyadoEnAlgunaPlataforma && heroe.getVelocidadY() > 1.0) {
            heroe.setEnElSuelo(false);
        }

        yAnteriorHeroe = heroe.getY();
    }

    // Patron Observer: JuegoManager es OBSERVADOR del heroe. Ya no revisa en cada
    // frame si el heroe murio; el heroe le avisa cuando pasa.
    @Override
    public void actualizar(EventoJuego evento, Heroe heroe) {
        if (evento == EventoJuego.HEROE_MURIO) {
            terminarJuego();
        }
    }

    public void terminarJuego(){
        //Frenamos el bucle del hilo del juego
        this.juegoCorriendo = false;

        // Le avisamos a App.java para que cambie la interfaz grafica
        if (this.eventoGameOver != null){
            javax.swing.SwingUtilities.invokeLater(this.eventoGameOver);
        }
    }
}


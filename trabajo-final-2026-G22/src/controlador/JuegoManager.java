package controlador;
import java.awt.Rectangle; //El asterisco importa todas las clases del paquete modelo
import modelo.*;
import vista.PanelJuego;

public class JuegoManager {

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
    //DAO
    private modelo.PuntajeDAO puntajesDAO;

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
    public void setPuntajesDAO(modelo.PuntajeDAO dao){
        this.puntajesDAO = dao;
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
        // Transicion de pantalla: si el heroe llega al borde derecho, cargamos la siguiente pantalla
        if (heroe.getY() < 0) {
            if (escenarioActual.getPantallaActual() == 1) {
                escenarioActual.cargarPantalla(2, new FabricaNivel2());
                heroe.setY(500);
            }

        }
        // Trancisicon hacia abajo si cae al vacio
        if (heroe.getY() > 800) {
            if (escenarioActual.getPantallaActual() == 2) {
                escenarioActual.cargarPantalla(1, new FabricaNivel1());
                heroe.setY(0); // Cae desde el techo de la pantalla 1, para que no se quede atrapado en el piso
                heroe.setVelocidadY(0); //Reiniciamos la velocidad para que caiga por gravedad
            } else {
                // Si cae al vacío en la pantalla 1, vuelve al piso base
                heroe.setX(50);
                heroe.setY(500);
                heroe.setVelocidadY(0);
            }
        }
        // Si los corazones llegan a 0,detonamos el Game Over
        if(!heroe.estaVivo()){
            terminarJuego();
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
        double bordeInferiorActual = hitboxHeroe.y + hitboxHeroe.height;
        double bordeInferiorAnterior = yAnteriorHeroe + hitboxHeroe.height;

        for (Plataforma plataforma : escenarioActual.getPlataformas()) {

            // Una plataforma rompible ya destruida no debe seguir colisionando
            if (plataforma.debeEliminarse()) {
                continue;
            }

            Rectangle hitboxPlataforma = plataforma.getHitbox();

            boolean solapaHorizontal = hitboxHeroe.x + hitboxHeroe.width > hitboxPlataforma.x
                    && hitboxHeroe.x < hitboxPlataforma.x + hitboxPlataforma.width;

            // "Recien aterrizo": venia cayendo, y su borde inferior acaba de
            // cruzar el borde superior de la plataforma en este frame.
            boolean cayendoSobreArriba = heroe.getVelocidadY() >= 0
                    && bordeInferiorAnterior <= hitboxPlataforma.y
                    && bordeInferiorActual >= hitboxPlataforma.y;

            if (solapaHorizontal && cayendoSobreArriba) {
                // Lo apoyamos justo arriba de la plataforma, para que no se
                // hunda un par de pixeles dentro de ella.
                heroe.setY(hitboxPlataforma.y - hitboxHeroe.height);
                plataforma.aplicarEfectoColision(heroe);
            }
        }

        yAnteriorHeroe = heroe.getY();
    }
    public void terminarJuego(){
        //Frenamos el bucle del hilo del juego
        this.juegoCorriendo = false;

        // --- GUARDAR PUNTAJE EN SQLITE ---
        if (this.puntajesDAO != null){
            //Aca podes reemplazar 1500 por la variable donde llevas los puntos reales
            modelo.Puntaje nuevoScore = new modelo.Puntaje("Jugador1",1500);
            this.puntajesDAO.guardar(nuevoScore);
            System.out.println("Puntaje guardado en SQLite");
        }
        
        // Le avisamos a App.java para que cambie la interfaz grafica
        if (this.eventoGameOver != null){
            javax.swing.SwingUtilities.invokeLater(this.eventoGameOver);
        }
        }
    }


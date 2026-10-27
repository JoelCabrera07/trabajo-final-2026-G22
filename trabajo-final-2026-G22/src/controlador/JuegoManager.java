package controlador;
import java.awt.Rectangle; //El asterisco importa todas las clases del paquete modelo
import modelo.*;
import vista.PanelJuego;

public class JuegoManager implements Observador {

    private static JuegoManager instance;

    // La torre tiene 6 pantallas; la puerta de la meta esta en la ultima
    public static final int TOTAL_PANTALLAS = 7;
    private static final int PRIMERA_PANTALLA_CON_TORMENTA = 4;

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

    private Runnable eventoNivelCompletado;// Variable para avisarle a App.java que el nivel se completo

    // Sistema de puntaje por metros recorridos hacia arriba
    private int metrosMaximosAlcanzados = 0;

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

    public void setEventoNivelCompletado(Runnable evento){
        this.eventoNivelCompletado = evento;
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

        // Actualizamos el récord de metros recorridos hacia arriba
        int metrosAhora = getMetrosActuales();
        if (metrosAhora > metrosMaximosAlcanzados) {
            metrosMaximosAlcanzados = metrosAhora;
        }

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
            if (pantalla < TOTAL_PANTALLAS) {
                cambiarPantalla(pantalla + 1);
                heroe.setY(520); // aparece abajo de la pantalla nueva, todavia subiendo
            } else {
                // Llegó a la cima más alta: arriba de la ultima pantalla no hay nada
                heroe.setY(0);
                heroe.setVelocidadY(0);
            }
        }
        // Transicion hacia abajo si cae al vacio
        if (heroe.getY() > 650) {
            int pantalla = escenarioActual.getPantallaActual();
            if (pantalla > 1) {
                cambiarPantalla(pantalla - 1);
                heroe.setY(0); // cae desde el techo de la pantalla de abajo
                heroe.setVelocidadY(0);
            } else {
                // Si cae al vacío en la pantalla 1, vuelve al piso base
                heroe.setX(50);
                heroe.setY(500);
                heroe.setVelocidadY(0);
            }
        }
    }

    // Patron Factory: cada pantalla de la torre tiene su propia fabrica.
    // Para agregar un nivel nuevo alcanza con sumar un case y subir TOTAL_PANTALLAS.
    public static FabricaNivel crearFabrica(int pantalla) {
        switch (pantalla) {
            case 1: return new FabricaNivel1();
            case 2: return new FabricaNivel2();
            case 3: return new FabricaNivel3();
            case 4: return new FabricaNivel4();
            case 5: return new FabricaNivel5();
            case 6: return new FabricaNivel6();
            case 7: return new FabricaNivel7();
            default: throw new IllegalArgumentException("No existe la pantalla " + pantalla);
        }
    }

    private void cambiarPantalla(int nuevaPantalla) {
        escenarioActual.cargarPantalla(nuevaPantalla, crearFabrica(nuevaPantalla));
        if (vista != null) {
            vista.cargarFondoNivel(nuevaPantalla);
            // De la pantalla 4 para arriba se esta afuera, en la tormenta
            if (nuevaPantalla >= PRIMERA_PANTALLA_CON_TORMENTA) {
                vista.reproducirAmbienteNivel4(); // si ya estaba sonando, sigue sin cortarse
            } else {
                vista.detenerAmbienteNivel4();
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

        // --- Colisiones con la puerta de la meta---
        Puerta puerta = escenarioActual.getPuerta();
        if (puerta != null && hitboxHeroe.intersects(puerta.getHitbox())) {
            puerta.aplicarEfectoColision(heroe);
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
        } else if (evento == EventoJuego.NIVEL_COMPLETADO) {
            completarNivel();
        }
    }

    public void terminarJuego(){
        frenarJuego();
        if (this.eventoGameOver != null){
            javax.swing.SwingUtilities.invokeLater(this.eventoGameOver);
        }
    }

    public void completarNivel(){
        frenarJuego();
        if (this.eventoNivelCompletado != null){
            javax.swing.SwingUtilities.invokeLater(this.eventoNivelCompletado);
        }
    }

    private void frenarJuego(){
        int metrosFinales = getMetrosActuales();
        if (metrosFinales > metrosMaximosAlcanzados) {
            metrosMaximosAlcanzados = metrosFinales;
        }
        this.juegoCorriendo = false;
    }

    public int getMetrosActuales() {
        if (heroe == null || escenarioActual == null) return 0;
        int pantalla = escenarioActual.getPantallaActual();
        // Altura vertical en píxeles desde el suelo base de la pantalla 1 (Y=550)
        double alturaPx = ((pantalla - 1) * 600.0) + (550.0 - heroe.getY());
        return Math.max(0, (int) (alturaPx / 10.0)); // 10 píxeles = 1 metro
    }

    public int getMetrosMaximos() {
        return metrosMaximosAlcanzados;
    }

    public void reiniciarMetros() {
        this.metrosMaximosAlcanzados = 0;
    }
}


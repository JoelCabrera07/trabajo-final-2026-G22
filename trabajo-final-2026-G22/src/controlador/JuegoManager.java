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
            if (p instanceof PlataformaMovil) {
                ((PlataformaMovil) p).mover();
            }
        }
        //hace patrullar a los enemigos terrestres
        for (Enemigo e : escenarioActual.getEnemigos()) {
            if (e instanceof EnemigoTerrestre) {
                ((EnemigoTerrestre) e).patrullar();
            }
            if(e instanceof EnemigoVolador){
                ((EnemigoVolador) e).flotarVerticalmente();
            }
            if (e instanceof EnemigoAcorazado) {
                ((EnemigoAcorazado) e).patrullar();
            }
            // las torretas intentan disparar en cada frame
            if(e instanceof EnemigoTorreta){
                Proyectil nuevo = ((EnemigoTorreta) e).disparar(heroe);
                if(nuevo != null){
                    escenarioActual.getProyectiles().add(nuevo);
                }
        }
    }

    //mover todos los proyectiles que ya están en vuelo
    for (Proyectil proyectil : escenarioActual.getProyectiles()) {
        proyectil.actualizar();
    }
}
    
    private void verificarColisiones() {
        // Verificamos si el heroe no es nulo y si el escenario ya esta cargado para evitar errores
        if (heroe == null || escenarioActual == null) return;

        // --- Colisiones con enemigos ---
        escenarioActual.getPlataformas().removeIf(p -> p instanceof PlataformaRompible && ((PlataformaRompible) p).isDestruida()); // Eliminamos las plataformas rompibles (como plataformaHielo) destruidas antes de verificar colisiones
        Rectangle hitboxHeroe = heroe.getHitbox(); // Obtenemos la hitbox del héroe para usarla en las colisiones
        for (Enemigo enemigo : escenarioActual.getEnemigos()) {
            if (heroe.getHitbox().intersects(enemigo.getHitbox())) {
                System.out.println("¡Colisión detectada con: " + enemigo.getNombre() + "!");
                enemigo.aplicarEfectoColision(heroe);
            }
        }

        for (Proyectil p: escenarioActual.getProyectiles()) {
            if (heroe.getHitbox().intersects(p.getHitbox())) {
                //System.out.println("¡Colisión detectada con un proyectil!");
                heroe.recibirDanio(p.getAtaque());
                p.setX(-9999);// Mueve el proyectil fuera de la pantalla para "destruirlo"
            
            }
        }
            
        //Sacamos los proyectiles que ya se salieron de la pantalla para no seguir calculando colisiones con ellos
        escenarioActual.getProyectiles().removeIf(p 
            -> p.getX() < -100 || p.getX() > 900 || p.getY() < -100 || p.getY() > 700
        );
      
        
        // --- Colisiones con plataformas ---
        hitboxHeroe = heroe.getHitbox();
        double bordeInferiorActual = hitboxHeroe.y + hitboxHeroe.height;
        double bordeInferiorAnterior = yAnteriorHeroe + hitboxHeroe.height;

        for (Plataforma plataforma : escenarioActual.getPlataformas()) {

            // Una plataforma rompible ya destruida no debe seguir colisionando
            if (plataforma instanceof PlataformaRompible
                    && ((PlataformaRompible) plataforma).isDestruida()) {
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
}
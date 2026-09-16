import java.awt.Rectangle;

public class JuegoManager {

    private static JuegoManager instance;

    private Escenario escenarioActual;
    private Heroe heroe;
    private boolean juegoCorriendo;

    // Guarda donde estaba el heroe en el frame anterior, para poder detectar
    // si "recien" aterrizo sobre una plataforma (y no aplicar el efecto en
    // cada frame que se solapen).
    private double yAnteriorHeroe;

    private JuegoManager() {
        this.juegoCorriendo = false;
    }

    public static JuegoManager getInstance() {
        if (instance == null) {
            instance = new JuegoManager();
        }
        return instance;
    }

    public void iniciarJuego() {
        this.juegoCorriendo = true;
        System.out.println("¡Iniciando el motor del juego!");
        buclePrincipal();
    }

    private void buclePrincipal() {
        while (juegoCorriendo) {

            // PASO A: Actualizar logicas (fisicas, movimiento, trayectoria)
            // actualizarFisicas();

            // PASO B: Chequear colisiones (Heroe tocando cofres, enemigos o plataformas)
            verificarColisiones();

            // PASO C: Mandar a redibujar la pantalla (La Vista)
            // repintarPantalla();

            // Un pequeño freno para que la compu no explote calculando a la velocidad de la luz
            try {
                Thread.sleep(16); // 16 milisegundos = aprox 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void verificarColisiones() {
        // Verificamos si el heroe no es nulo y si el escenario ya esta cargado para evitar errores
        if (heroe == null || escenarioActual == null) return;

        // --- Colisiones con enemigos ---
        for (Enemigo enemigo : escenarioActual.getEnemigos()) {
            if (heroe.getHitbox().intersects(enemigo.getHitbox())) {
                System.out.println("¡Colisión detectada con: " + enemigo.getNombre() + "!");
                enemigo.aplicarEfectoColision(heroe);
            }
        }

        // --- Colisiones con plataformas ---
        Rectangle hitboxHeroe = heroe.getHitbox();
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
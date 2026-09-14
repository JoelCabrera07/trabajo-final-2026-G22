public class JuegoManager {
    
    private static JuegoManager instance;
    
    // Los atributos que maneja (Modelo)
    private Escenario escenarioActual;
    private Heroe heroe;
    private boolean juegoCorriendo;

    // Constructor PRIVADO: Nadie desde afuera puede hacer un "new JuegoManager()"
    private JuegoManager() {
        // Acá prepararemos el mapa inicial y al personaje más adelante
        this.juegoCorriendo = false;
    }

    //  Metodo estatico para obtener la unica instancia permitida
    public static JuegoManager getInstance() {
        if (instance == null) {
            instance = new JuegoManager();
        }
        return instance;
    }

    // Arranca el motor
    public void iniciarJuego() {
        this.juegoCorriendo = true;
        System.out.println("¡Iniciando el motor del juego!");
        buclePrincipal();
    }

    // El corazón del juego
    private void buclePrincipal() {
        // Este while(true) es el que va a mantener el juego vivo
        while (juegoCorriendo) {
            
            // PASO A: Actualizar lógicas (físicas, movimiento, trayectoria)
            // actualizarFisicas();
            
            // PASO B: Chequear colisiones (Héroe tocando cofres, enemigos o plataformas)
            // verificarColisiones();

        }
        
        // Aca.... agregar el "for" 
        // para chequear los choques con las plataformas de la misma manera).
    
    
            // PASO C: Mandar a redibujar la pantalla (La Vista)
            // repintarPantalla();
            
            // Un pequeño freno para que la compu no explote calculando a la velocidad de la luz
            try {
                Thread.sleep(16); // 16 milisegundos = aprox 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        private void verificarColisiones() {
        // Verificamos si el héroe no es nulo y si el escenario ya está cargado para evitar errores
        if (heroe == null || escenarioActual == null) return;

        // Recorremos la lista de enemigos que está en el escenario
        for (Enemigo enemigo : escenarioActual.getEnemigos()) {
            
            // intersects() se fija si los dos rectángulos se tocan
            if (heroe.getHitbox().intersects(enemigo.getHitbox())) {
                
                System.out.println("¡Colisión detectada con: " + enemigo.getNombre() + "!");
                
                // Aplicamos el polimorfismo: cada enemigo le hará un efecto distinto al héroe
                enemigo.aplicarEfectoColision(heroe);
                
                // Opcional: Podrías poner un pequeño tiempo de invulnerabilidad acá 
                // para que el enemigo no le baje toda la vida en un milisegundo.
            }
        }
        
        // Aca mismo se agregar el "for" 
        // para chequear los choques con las plataformas de la misma manera).
    }
    }

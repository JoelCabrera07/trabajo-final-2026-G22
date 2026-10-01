package modelo;
import java.util.ArrayList;
import java.util.List;

public class Escenario {
    
    // Las listas que van a contener todos los objetos del mapa
    private List<Plataforma> plataformas;
    private List<Enemigo> enemigos;
    private List<Entidad> entidadesInteractuables; // Cofres, boosters, etc.
    private List<Proyectil> proyectiles; // Proyectiles disparados por torretas
    private int pantallaActual = 1; // Para saber en qué pantalla estamos, y cargar la siguiente cuando el héroe llegue al borde derecho

    public Escenario() {
        // Es re importante inicializar las listas vacías en el constructor
        // para que después Java no nos tire el famoso error "NullPointerException"
        this.plataformas = new ArrayList<>();
        this.enemigos = new ArrayList<>();
        this.entidadesInteractuables = new ArrayList<>();
        this.proyectiles = new ArrayList<>();
    }

    public void cargarMapa() {
        // Acá es donde más adelante vamos a hacer el diseño de nivel
        System.out.println("Cargando plataformas, enemigos y cofres en el mapa...");
        
        // Ejemplo:
        // plataformas.add(new PlataformaMovil(100, 500, 80, 20, 50, 250));
        // enemigos.add(new EnemigoTorreta(300, 450));
    }
    public void cargarPantalla(int numeroPantalla) {
        this.pantallaActual = numeroPantalla; // Actualiza la pantalla actual
        this.plataformas.clear(); // Limpia las listas para cargar la nueva pantalla
        this.enemigos.clear(); // Limpia las listas para cargar la nueva pantalla
        this.proyectiles.clear(); // Limpia las listas para cargar la nueva pantalla
        System.out.println("Cargando pantalla " + numeroPantalla + "...");

        if (numeroPantalla == 1) {
            // Pantalla 1: plataformas y enemigos de la primera pantalla
            plataformas.add(new PlataformaNormal(0, 550, 800, 50));
            plataformas.add(new PlataformaNormal(100,450,120,20));
            plataformas.add(new PlataformaNormal(300,340,120,20));
            plataformas.add(new PlataformaNormal(550,240,120,20));
            plataformas.add(new PlataformaBarro(300, 120, 120, 20));
            enemigos.add(new EnemigoTerrestre(120, 420));
            enemigos.add(new EnemigoVolador(400, 250));
            enemigos.add(new EnemigoAcorazado(500, 480));
            enemigos.add(new EnemigoTorreta(600, 100));
        } else if (numeroPantalla == 2) {
            // Pantalla 2: plataformas y enemigos de la segunda pantalla
            this.plataformas.add(new PlataformaNormal(250, 480, 140, 20));
            this.plataformas.add(new PlataformaNormal(450, 350, 120, 20));
            this.plataformas.add(new PlataformaNormal(200, 220, 140, 20));
            this.plataformas.add(new PlataformaNormal(350, 100, 150, 20));
            // Agregar aquí las plataformas y enemigos de la segunda pantalla
        }
    }
    public int getPantallaActual() {
        return pantallaActual;
    }
    // --- GETTERS ---
    // El JuegoManager va a necesitar estos métodos para leer qué hay en el mapa
    public List<Plataforma> getPlataformas() {
        return plataformas;
    }

    public List<Enemigo> getEnemigos() {
        return enemigos;
    }

    public List<Entidad> getEntidadesInteractuables() {
        return entidadesInteractuables;
    }

    public List<Proyectil> getProyectiles() {
        return proyectiles;
    }
}

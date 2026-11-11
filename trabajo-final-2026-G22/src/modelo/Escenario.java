package modelo;
import java.util.ArrayList;
import java.util.List;

public class Escenario {
    
    // Las listas que van a contener todos los objetos del mapa
    private List<Plataforma> plataformas;
    private List<Enemigo> enemigos;
    private List<Proyectil> proyectiles; // Proyectiles disparados por torretas
    private int pantallaActual = 1; // Para saber en qué pantalla estamos, y cargar la siguiente cuando el héroe llegue al borde derecho
    private Puerta puerta; // null si la pantalla no tiene

    public Escenario() {
        // Es re importante inicializar las listas vacías en el constructor
        // para que después Java no nos tire el famoso error "NullPointerException"
        this.plataformas = new ArrayList<>();
        this.enemigos = new ArrayList<>();
        this.proyectiles = new ArrayList<>();
    }

   public void cargarPantalla(int numeroPantalla, Nivel nivel) { 
        this.pantallaActual = numeroPantalla;
        this.plataformas.clear();
        this.enemigos.clear();  
        this.proyectiles.clear();
        // El nivel pide las entidades a FabricaEntidades, el Escenario solo las guardad
        this.plataformas.addAll(nivel.crearPlataformas());
        this.enemigos.addAll(nivel.crearEnemigos());
        this.puerta = nivel.crearPuerta(); // Puede ser null si la pantalla no tiene puerta
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

    public List<Proyectil> getProyectiles() {
        return proyectiles;
    }

    public Puerta getPuerta() {
        return puerta;
    }
}
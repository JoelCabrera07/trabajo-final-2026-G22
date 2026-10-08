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
   public void cargarPantalla(int numeroPantalla, FabricaNivel fabrica) { //Se aplica la fábrica para crear enemigos y plataformas según el nivel dejandolo mas limpio al codigo y no sobrecargado de código
        this.pantallaActual = numeroPantalla; 
        this.plataformas.clear(); 
        this.enemigos.clear(); 
        this.proyectiles.clear(); 
        System.out.println("Cargando pantalla " + numeroPantalla + " con el Patrón Factory...");

        // La fábrica se encarga de instanciar todo, el Escenario solo lo guarda
        this.plataformas.addAll(fabrica.crearPlataformas());
        this.enemigos.addAll(fabrica.crearEnemigos());
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
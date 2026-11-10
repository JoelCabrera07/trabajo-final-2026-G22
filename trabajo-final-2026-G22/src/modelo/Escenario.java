package modelo;

import java.util.ArrayList;
import java.util.List;

public class Escenario {

    // Las listas que van a contener todos los objetos del mapa
    private List<Plataforma> plataformas;
    private List<Enemigo> enemigos;
    private List<Proyectil> proyectiles; // Proyectiles disparados por torretas
    private int pantallaActual = 1; // Para saber en qué pantalla estamos, y cargar la siguiente cuando el héroe
                                    // llegue al borde derecho
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
        System.out.println("Cargando pantalla " + numeroPantalla);
        nivel.armar(this); // el nivel decide qué poner en la pantalla
    }

    // --- Métodos para modificar el escenario sin tocar sus listas desde afuera ---
    public void vaciar() {
        plataformas.clear();
        enemigos.clear();
        proyectiles.clear();
        puerta = null;
    }

    public void agregarPlataforma(Plataforma plataforma) {
        plataformas.add(plataforma);
    }

    public void agregarEnemigo(Enemigo enemigo) {
        enemigos.add(enemigo);
    }

    public void setPuerta(Puerta puerta) {
        this.puerta = puerta;
    }

    public void agregarProyectil(Proyectil proyectil) {
        proyectiles.add(proyectil);
    }

    // Saca las plataformas rotas y los proyectiles que ya pegaron o salieron de la pantalla
    public void eliminarPlataformasRotas() {
        plataformas.removeIf(plataforma -> plataforma.debeEliminarse());
    }

    public void eliminarProyectilesTerminados() {
        proyectiles.removeIf(proyectil -> proyectil.debeEliminarse());
    }

    public int getPantallaActual() {
        return pantallaActual;
    }

    // --- GETTERS ---
    // El JuegoManager va a necesitar estos metodos para leer qué hay en el mapa
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
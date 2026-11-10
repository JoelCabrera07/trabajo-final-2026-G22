package modelo;

import java.util.List;

// Creador del patron Factory Method: arma una pantalla del juego.
// Cada nivel concreto decide que plataformas, enemigos y puerta crear.
public abstract class Nivel {

    // Método de negocio: usa los métodos fábrica para llenar el escenario.
    public void armar(Escenario escenario) {
        escenario.vaciar();
        for (Plataforma plataforma : crearPlataformas()) {
            escenario.agregarPlataforma(plataforma);
        }
        for (Enemigo enemigo : crearEnemigos()) {
            escenario.agregarEnemigo(enemigo);
        }
        escenario.setPuerta(crearPuerta());
    }

    // Metodos fabrica: cada nivel concreto los implementa
    protected abstract List<Plataforma> crearPlataformas();

    protected abstract List<Enemigo> crearEnemigos();

    // Por defecto la pantalla no tiene puerta; solo Nivel6 lo redefine
    protected Puerta crearPuerta() {
        return null;
    }
}

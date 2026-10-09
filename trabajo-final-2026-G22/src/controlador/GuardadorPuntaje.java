package controlador;

import modelo.EventoJuego;
import modelo.Heroe;
import modelo.Observador;
import modelo.Puntaje;
import modelo.PuntajeDAO;

/**
 * Observador que guarda el puntaje en la base de datos cuando el heroe muere.
 * Antes esto estaba metido adentro de JuegoManager.terminarJuego(); ahora
 * JuegoManager no necesita conocer al DAO: solo este observador lo conoce.
 */
public class GuardadorPuntaje implements Observador {

    private final PuntajeDAO dao;

    // Todavia no hay un sistema de puntos real, se mantienen los valores de prueba
    // que ya usaba JuegoManager. Cuando exista, se reemplazan aca.
    private static final String JUGADOR_PRUEBA = "Jugador1";
    private static final int PUNTOS_PRUEBA = 1500;

    public GuardadorPuntaje(PuntajeDAO dao) {
        this.dao = dao;
    }

    @Override
    public void actualizar(EventoJuego evento, Heroe heroe) {
        if (evento == EventoJuego.HEROE_MURIO) {
            dao.guardar(new Puntaje(JUGADOR_PRUEBA, PUNTOS_PRUEBA));
            System.out.println("Puntaje guardado en SQLite");
        }
    }
}

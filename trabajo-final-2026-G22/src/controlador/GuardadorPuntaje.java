package controlador;

import java.util.List;
import modelo.EventoJuego;
import modelo.Heroe;
import modelo.Observador;
import modelo.Puntaje;
import modelo.PuntajeDAO;

/**
 * Observador que escucha los eventos del Heroe y gestiona la persistencia
 * de puntajes con el DAO.
 */
public class GuardadorPuntaje implements Observador {

    private final PuntajeDAO dao;
    private int ultimosMetros = 0;

    public GuardadorPuntaje(PuntajeDAO dao) {
        this.dao = dao;
    }

    @Override
    public void actualizar(EventoJuego evento, Heroe heroe) {
        if (evento == EventoJuego.HEROE_MURIO || evento == EventoJuego.NIVEL_COMPLETADO) {
            this.ultimosMetros = JuegoManager.getInstance().getMetrosMaximos();
            System.out.println("GuardadorPuntaje: El heroe murio alcanzando " + ultimosMetros + " metros.");
        }
    }

    /**
     * Guarda el puntaje final en la base de datos aplicando la regla de negocio
     * del nombre (máximo 10 caracteres).
     *
     * @param jugador Nombre ingresado por el jugador (máx 10 letras).
     * @param metros Metros recorridos hacia arriba.
     * @return true si se guardó con éxito en SQLite.
     */
    public boolean guardarPuntaje(String jugador, int metros) {
        if (jugador == null || jugador.trim().isEmpty()) {
            jugador = "Anonimo";
        }
        jugador = jugador.trim();
        if (jugador.length() > 10) {
            jugador = jugador.substring(0, 10);
        }
        boolean guardado = dao.guardar(new Puntaje(jugador, metros));
        if (guardado) {
            System.out.println("Puntaje guardado en SQLite: " + jugador + " - " + metros + "m");
        }
        return guardado;
    }

    public List<Puntaje> obtenerMejoresPuntajes(int cantidad) {
        return dao.mejores(cantidad);
    }

    public int getUltimosMetros() {
        return ultimosMetros;
    }
}

package modelo;

/**
 * Patron Observer - el OBSERVADOR.
 * Cualquier clase que quiera enterarse de lo que le pasa al heroe implementa
 * esta interfaz. El heroe la llama cada vez que pasa algo, sin saber quien
 * es el observador ni que hace con el aviso.
 */
public interface Observador {
    void actualizar(EventoJuego evento, Heroe heroe);
}

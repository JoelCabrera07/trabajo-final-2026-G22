package modelo;

/**
 * Los distintos "avisos" que el sujeto (Heroe) le puede mandar a sus observadores.
 */
public enum EventoJuego {
    HEROE_RECIBIO_DANIO, // el heroe perdio vida (incluye el golpe final)
    HEROE_MURIO          // la vida del heroe llego a 0 (se avisa UNA sola vez por muerte)
}

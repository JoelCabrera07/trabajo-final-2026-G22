package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla 2: Salon de los estandartes
 * El hielo de la izquierda te empuja hacia el vacio y la plataforma movil hay que agarrarla a tiempo,
 * sin quedarse quieto mucho porque aparece el acechador.
 */
public class FabricaNivel2 implements FabricaNivel {

    @Override
    public List<Plataforma> crearPlataformas() {
        List<Plataforma> plataformas = new ArrayList<>();

        // Llegada desde la pantalla 1
        plataformas.add(new PlataformaNormal(520, 470, 110, 20));
        plataformas.add(new PlataformaNormal(280, 420, 70, 20));
        // Hielo contra la pared: si no saltas rapido te caes
        plataformas.add(new PlataformaHielo(60, 300, 110, 20));
        // Plataforma movil
        plataformas.add(new PlataformaMovil(200, 170, 90, 20, 160, 400));
        // Salida a la pantalla 3
        plataformas.add(new PlataformaNormal(680, 110, 60, 20));
        // Saliente de la torreta
        plataformas.add(new PlataformaNormal(700, 330, 60, 20));

        return plataformas;
    }

    @Override
    public List<Enemigo> crearEnemigos() {
        List<Enemigo> enemigos = new ArrayList<>();

        enemigos.add(new EnemigoTorreta(720, 290));
        enemigos.add(new EnemigoVolador(520, 250));
        // Castiga al que espera mucho a la plataforma movil
        enemigos.add(new EnemigoAcechador(0, 0, 1.2));

        return enemigos;
    }
}

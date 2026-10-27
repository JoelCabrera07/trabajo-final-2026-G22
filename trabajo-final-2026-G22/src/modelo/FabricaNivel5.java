package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla 5: La aguja de hierro
 * Dos plataformas moviles seguidas, fuego cruzado de dos torretas y voladores en el camino.
 */
public class FabricaNivel5 implements FabricaNivel {

    @Override
    public List<Plataforma> crearPlataformas() {
        List<Plataforma> plataformas = new ArrayList<>();

        // Llegada desde la pantalla 4
        plataformas.add(new PlataformaNormal(180, 470, 100, 20));
        plataformas.add(new PlataformaMovil(250, 390, 80, 20, 120, 380));
        plataformas.add(new PlataformaMovil(450, 280, 70, 20, 400, 640));
        // Hielo contra la pared
        plataformas.add(new PlataformaHielo(700, 200, 90, 20));
        // Salida a la cima
        plataformas.add(new PlataformaNormal(430, 165, 60, 20));
        // Saliente de torreta
        plataformas.add(new PlataformaNormal(20, 250, 60, 20));

        return plataformas;
    }

    @Override
    public List<Enemigo> crearEnemigos() {
        List<Enemigo> enemigos = new ArrayList<>();

        enemigos.add(new EnemigoTorreta(40, 210));
        enemigos.add(new EnemigoTorreta(730, 160));
        enemigos.add(new EnemigoVolador(360, 300));
        enemigos.add(new EnemigoVolador(600, 180));
        enemigos.add(new EnemigoAcechador(0, 0, 1.4));

        return enemigos;
    }
}

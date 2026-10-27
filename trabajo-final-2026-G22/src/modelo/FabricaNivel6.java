package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla 6: La cima
 * La puerta esta en la punta derecha de la cima, custodiada por un acorazado que empuja al vacio.
 * Hay que saltarlo justo cuando se acerca (y sin esperar mucho, que aparecen los acechadores).
 */
public class FabricaNivel6 implements FabricaNivel {

    @Override
    public List<Plataforma> crearPlataformas() {
        List<Plataforma> plataformas = new ArrayList<>();

        // Llegada desde la pantalla 5
        plataformas.add(new PlataformaNormal(300, 470, 120, 20));
        plataformas.add(new PlataformaNormal(90, 400, 80, 20));
        // Hielo: hay que saltar enseguida
        plataformas.add(new PlataformaHielo(220, 300, 80, 20));
        // Plataforma movil
        plataformas.add(new PlataformaMovil(90, 190, 80, 20, 40, 170));
        // Cima: la puerta de la meta esta en la punta derecha
        plataformas.add(new PlataformaNormal(540, 170, 260, 20));
        // Saliente de la torreta
        plataformas.add(new PlataformaNormal(20, 280, 50, 20));

        return plataformas;
    }

    @Override
    public List<Enemigo> crearEnemigos() {
        List<Enemigo> enemigos = new ArrayList<>();

        // Custodia la puerta: empuja al heroe hacia el vacio (hay que saltarlo)
        EnemigoAcorazado acorazado1 = new EnemigoAcorazado(615, 120);
        acorazado1.setSprite(Sprites.cargar("/assets/enemigo_acorazado.png"));
        enemigos.add(acorazado1);
        enemigos.add(new EnemigoTorreta(35, 240));
        enemigos.add(new EnemigoAcechador(0, 0, 1.3));
        enemigos.add(new EnemigoAcechador(0, 0, 1.8));

        return enemigos;
    }

    @Override
    public Puerta crearPuerta() {
        // La meta: la puerta en lo mas alto de la torre
        return new Puerta(745, 110);
    }
}

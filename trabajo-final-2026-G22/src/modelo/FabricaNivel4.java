package modelo;

import java.util.ArrayList;
import java.util.List;

public class FabricaNivel4 implements FabricaNivel {

    @Override
    public List<Plataforma> crearPlataformas() {
        List<Plataforma> plataformas = new ArrayList<>();

        // Plataforma base al entrar al Nivel 4 desde el Nivel 3
        plataformas.add(new PlataformaNormal(260, 540, 220, 20));

        // Trampolín a la derecha que impulsa al héroe hacia arriba
        plataformas.add(new PlataformaTrampolin(600, 470, 130, 20));

        // Plataformas rompibles en la zona media (exigen reacción rápida)
        plataformas.add(new PlataformaRompible(350, 340, 120, 20));
        plataformas.add(new PlataformaRompible(150, 250, 120, 20));

        // Plataforma móvil en la altura
        plataformas.add(new PlataformaMovil(250, 170, 110, 20, 200, 520));

        // La cima: plataforma final del ascenso
        plataformas.add(new PlataformaNormal(50, 90, 160, 25));

        return plataformas;
    }

    @Override
    public List<Enemigo> crearEnemigos() {
        List<Enemigo> enemigos = new ArrayList<>();

        // Enemigo torreta en una cornisa disparando hacia la izquierda
        EnemigoTorreta torreta = new EnemigoTorreta(700, 140);
        torreta.setSprite(Sprites.cargar("/assets/enemigo_torreta.png"));
        enemigos.add(torreta);

        // Enemigo acorazado patrullando la plataforma base
        EnemigoAcorazado acorazado = new EnemigoAcorazado(300, 500);
        acorazado.setSprite(Sprites.cargar("/assets/enemigo_acorazado.png"));
        enemigos.add(acorazado);

        return enemigos;
    }
    @Override
    public Puerta crearPuerta() {
        return new Puerta(140, 30);
    }
}

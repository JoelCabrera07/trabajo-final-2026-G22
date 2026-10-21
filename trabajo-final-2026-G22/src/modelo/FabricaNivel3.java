package modelo;

import java.util.ArrayList;
import java.util.List;

public class FabricaNivel3 implements FabricaNivel {

    @Override
    public List<Plataforma> crearPlataformas() {
        List<Plataforma> plataformas = new ArrayList<>();

        // Plataforma base para recibir al héroe que sube desde el Nivel 2
        plataformas.add(new PlataformaNormal(250, 500, 200, 20));

        // Plataforma de hielo resbaladiza hacia la izquierda
        plataformas.add(new PlataformaHielo(60, 440, 140, 20));

        // Plataforma móvil que patrulla en el centro
        plataformas.add(new PlataformaMovil(250, 340, 120, 20, 200, 560));

        // Plataforma de barro que frena la velocidad
        PlataformaBarro barro = new PlataformaBarro(600, 230, 130, 20);
        plataformas.add(barro);

        // Plataforma de hielo superior para el salto final hacia el Nivel 4
        plataformas.add(new PlataformaHielo(300, 120, 140, 20));

        return plataformas;
    }

    @Override
    public List<Enemigo> crearEnemigos() {
        List<Enemigo> enemigos = new ArrayList<>();

        // Enemigo volador patrullando la zona central
        EnemigoVolador volador = new EnemigoVolador(400, 260);
        volador.setSprite(Sprites.getEnemigoVolador()[0]);
        enemigos.add(volador);

        // Enemigo acechador: castiga al jugador si se queda quieto pensando el salto
        enemigos.add(new EnemigoAcechador(0, 0));

        return enemigos;
    }
}

package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla 3: Muro derrumbado
 * Barro, hielo y un techo rompible sobre la salida: el primer salto lo rompe y el segundo pasa.
 */
public class FabricaNivel3 implements FabricaNivel {

    @Override
    public List<Plataforma> crearPlataformas() {
        List<Plataforma> plataformas = new ArrayList<>();

        // Llegada desde la pantalla 2
        plataformas.add(new PlataformaNormal(520, 440, 110, 20));
        // Barro: caminar hasta el borde cuesta
        plataformas.add(new PlataformaBarro(700, 330, 90, 20));
        plataformas.add(new PlataformaNormal(600, 190, 60, 20));
        // Hielo de la salida
        plataformas.add(new PlataformaHielo(270, 135, 100, 20));
        // Techo rompible: hay que romperlo de un cabezazo para poder salir
        plataformas.add(new PlataformaRompible(210, 20, 200, 20));

        return plataformas;
    }

    @Override
    public List<Enemigo> crearEnemigos() {
        List<Enemigo> enemigos = new ArrayList<>();

        // Cuida la llegada
        enemigos.add(new EnemigoTerrestre(540, 410));
        // Molesta en el salto del barro a la plataforma alta
        enemigos.add(new EnemigoVolador(520, 250));
        enemigos.add(new EnemigoAcechador(0, 0, 1.0));
        enemigos.add(new EnemigoAcechador(0, 0, 1.6));

        return enemigos;
    }
}

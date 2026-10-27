package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla 4: Balcon de la tormenta
 * Hay que caer en el trampolin y dirigir el rebote hasta la cornisa de la derecha,
 * despues de romper el techo que lo tapa.
 */
public class FabricaNivel4 implements FabricaNivel {

    @Override
    public List<Plataforma> crearPlataformas() {
        List<Plataforma> plataformas = new ArrayList<>();

        // Llegada desde la pantalla 3
        plataformas.add(new PlataformaNormal(480, 470, 110, 20));
        // Trampolin: rebota solo
        plataformas.add(new PlataformaTrampolin(300, 540, 70, 20));
        // Techo rompible sobre el trampolin
        plataformas.add(new PlataformaRompible(260, 250, 140, 20));
        // Salida a la pantalla 5
        plataformas.add(new PlataformaNormal(80, 150, 60, 20));
        // Saliente de la torreta
        plataformas.add(new PlataformaNormal(660, 300, 60, 20));

        return plataformas;
    }

    @Override
    public List<Enemigo> crearEnemigos() {
        List<Enemigo> enemigos = new ArrayList<>();

        enemigos.add(new EnemigoTorreta(680, 260));
        // Vuelan en la columna del rebote
        enemigos.add(new EnemigoVolador(250, 380));
        enemigos.add(new EnemigoVolador(400, 200));

        return enemigos;
    }
}

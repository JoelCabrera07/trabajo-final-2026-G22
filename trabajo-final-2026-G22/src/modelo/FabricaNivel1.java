package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla 1: Planta baja
 * Calentamiento: saltos medianos, el primer hielo y una salida que pide carga fuerte.
 */
public class FabricaNivel1 implements FabricaNivel {

    @Override
    public List<Plataforma> crearPlataformas() {
        List<Plataforma> plataformas = new ArrayList<>();

        // Piso de la planta baja (aca arranca el heroe)
        plataformas.add(new PlataformaNormal(0, 550, 800, 50));
        plataformas.add(new PlataformaNormal(150, 440, 90, 20));
        // Barro: caminar cuesta
        plataformas.add(new PlataformaBarro(330, 290, 80, 20));
        // Hielo: te resbalas mientras cargas el salto
        plataformas.add(new PlataformaHielo(520, 260, 100, 20));
        // Cornisa de salida: desde aca se salta a la pantalla 2
        plataformas.add(new PlataformaNormal(700, 130, 70, 20));
        // Saliente de la torreta
        plataformas.add(new PlataformaNormal(20, 300, 70, 20));

        return plataformas;
    }

    @Override
    public List<Enemigo> crearEnemigos() {
        List<Enemigo> enemigos = new ArrayList<>();

        // Patrulla la primera plataforma
        enemigos.add(new EnemigoTerrestre(160, 390));
        // Empuja al heroe por el piso
        EnemigoAcorazado acorazado1 = new EnemigoAcorazado(470, 500);
        acorazado1.setSprite(Sprites.cargar("/assets/enemigo_acorazado.png"));
        enemigos.add(acorazado1);
        // Cruza el hueco entre el barro y el hielo
        enemigos.add(new EnemigoVolador(455, 250));
        // Dispara a todo el salon
        enemigos.add(new EnemigoTorreta(45, 260));

        return enemigos;
    }
}

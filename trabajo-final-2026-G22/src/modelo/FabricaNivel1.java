package modelo;
import java.util.ArrayList;
import java.util.List;

public class FabricaNivel1 implements FabricaNivel {

    @Override
    public List<Plataforma> crearPlataformas() {
        List<Plataforma> plataformas = new ArrayList<>();
        plataformas.add(new PlataformaNormal(0, 550, 800, 50));
        plataformas.add(new PlataformaNormal(100, 450, 120, 20));
        plataformas.add(new PlataformaNormal(300, 340, 120, 20));
        plataformas.add(new PlataformaNormal(550, 240, 120, 20));

        PlataformaBarro barro = new PlataformaBarro(300, 120, 120, 20);
        barro.setSprite(Sprites.cargar("/assets/plataforma_barro.png"));
        plataformas.add(barro);
        
        return plataformas;
    }

    @Override
    public List<Enemigo> crearEnemigos() {
        List<Enemigo> enemigos = new ArrayList<>();
        
        EnemigoTerrestre terrestre = new EnemigoTerrestre(120, 420);
        terrestre.setSprite(Sprites.getEnemigoTerrestre()[0]);
        enemigos.add(terrestre);

        EnemigoVolador volador = new EnemigoVolador(400, 250);
        volador.setSprite(Sprites.getEnemigoVolador());
        enemigos.add(volador);

        EnemigoAcorazado acorazado = new EnemigoAcorazado(500, 480);
        acorazado.setSprite(Sprites.cargar("/assets/enemigo_acorazado.png"));
        enemigos.add(acorazado);

        EnemigoTorreta torreta = new EnemigoTorreta(600, 100);
        torreta.setSprite(Sprites.cargar("/assets/enemigo_torreta.png"));
        enemigos.add(torreta);

        return enemigos;
    }
}
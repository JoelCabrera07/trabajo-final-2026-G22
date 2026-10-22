package modelo;

import java.util.ArrayList;
import java.util.List;

public class FabricaNivel2 implements FabricaNivel {

    @Override
    public List<Plataforma> crearPlataformas() {
        List<Plataforma> plataformas = new ArrayList<>();
        
        // Las plataformas que rescatamos de tu antiguo Escenario
        plataformas.add(new PlataformaNormal(150, 480, 140, 20)); // Plataforma más baja
        plataformas.add(new PlataformaNormal(500, 350, 120, 20)); // Plataforma más alta
        plataformas.add(new PlataformaNormal(150, 220, 140, 20)); // Plataforma intermedia
        plataformas.add(new PlataformaNormal(450, 100, 150, 20)); // 
        
        // Acá podés sumar Plataformas de Hielo, Barro o Trampolines si querés
        
        return plataformas;
    }

    @Override
    public List<Enemigo> crearEnemigos() {
        List<Enemigo> enemigos = new ArrayList<>();
        
        // Como todavía no tenemos enemigos en el Nivel 2, devolvemos la lista vacía 
        // para que no haya errores. ¡Podés agregar los que quieras acá abajo!
        enemigos.add(new EnemigoAcechador(100, 100, 1.0));
        enemigos.add(new EnemigoAcechador(200, 200,1.5));
        enemigos.add(new EnemigoAcechador(300, 300,1.8));

        EnemigoTorreta torreta = new EnemigoTorreta(100, 100);
        torreta.setSprite(Sprites.cargar("/assets/enemigo_torreta.png"));
        enemigos.add(torreta);

        EnemigoAcorazado acorazado = new EnemigoAcorazado(600,120);
        acorazado.setSprite(Sprites.cargar("/assets/enemigo_acorazado.png"));
        enemigos.add(acorazado);
        return enemigos;
    }
}
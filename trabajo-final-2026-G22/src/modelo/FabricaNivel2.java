package modelo;

import java.util.ArrayList;
import java.util.List;

public class FabricaNivel2 implements FabricaNivel {

    @Override
    public List<Plataforma> crearPlataformas() {
        List<Plataforma> plataformas = new ArrayList<>();
        
        // Las plataformas que rescatamos de tu antiguo Escenario
        plataformas.add(new PlataformaNormal(250, 480, 140, 20));
        plataformas.add(new PlataformaNormal(450, 350, 120, 20));
        plataformas.add(new PlataformaNormal(200, 220, 140, 20));
        plataformas.add(new PlataformaNormal(350, 100, 150, 20));
        
        // Acá podés sumar Plataformas de Hielo, Barro o Trampolines si querés
        
        return plataformas;
    }

    @Override
    public List<Enemigo> crearEnemigos() {
        List<Enemigo> enemigos = new ArrayList<>();
        
        // Como todavía no tenemos enemigos en el Nivel 2, devolvemos la lista vacía 
        // para que no haya errores. ¡Podés agregar los que quieras acá abajo!
        
        return enemigos;
    }
}
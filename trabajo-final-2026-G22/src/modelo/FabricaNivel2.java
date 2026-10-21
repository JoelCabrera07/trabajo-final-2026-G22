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
        
        return enemigos;
    }
}
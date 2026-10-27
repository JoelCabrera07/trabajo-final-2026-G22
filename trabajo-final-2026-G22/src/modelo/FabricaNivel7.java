package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla 7: el cielo.
 * Esta arriba de la cima (pantalla 6) y no tiene plataformas ni enemigos: es solo el
 * espacio extra para completar el arco del salto y volver a caer sobre la plataforma
 * de la puerta. No redefine crearPuerta(), asi que usa el valor por defecto (null).
 */
public class FabricaNivel7 implements FabricaNivel {

    @Override
    public List<Plataforma> crearPlataformas() {
        return new ArrayList<>(); // sin plataformas
    }

    @Override
    public List<Enemigo> crearEnemigos() {
        return new ArrayList<>(); // sin enemigos
    }
}
    


package modelo;

import java.util.ArrayList;
import java.util.List;

public class Nivel7 extends Nivel {

    @Override
    protected List<Plataforma> crearPlataformas() {
        return new ArrayList<>(); // sin plataformas
    }

    @Override
    protected List<Enemigo> crearEnemigos() {
        return new ArrayList<>(); // sin enemigos
    }
}
    


package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * La descripcion de un nivel: que plataformas, enemigos y puerta tiene y donde van.
 * Es solo informacion. Los objetos del juego se crean recien cuando se carga la
 * pantalla, y los crea FabricaEntidades.
 */
public class Nivel {

    // Un pedido de creacion: el tipo y los parametros que necesita (x, y, ...)
    private static class Pedido<T> {
        private final T tipo;
        private final double[] parametros;

        private Pedido(T tipo, double[] parametros) {
            this.tipo = tipo;
            this.parametros = parametros;
        }
    }

    private final List<Pedido<TipoPlataforma>> plataformas = new ArrayList<>();
    private final List<Pedido<TipoEnemigo>> enemigos = new ArrayList<>();
    private double[] puerta; // null si el nivel no tiene puerta

    // Los metodos devuelven this para poder escribir un nivel como una lista encadenada
    public Nivel plataforma(TipoPlataforma tipo, double... parametros) {
        plataformas.add(new Pedido<>(tipo, parametros));
        return this;
    }

    public Nivel enemigo(TipoEnemigo tipo, double... parametros) {
        enemigos.add(new Pedido<>(tipo, parametros));
        return this;
    }

    public Nivel puerta(double x, double y) {
        this.puerta = new double[] { x, y };
        return this;
    }

    // Cada llamada crea objetos NUEVOS: las plataformas se rompen y los enemigos se
    // mueven, asi que al volver a cargar el nivel tienen que empezar de cero.
    public List<Plataforma> crearPlataformas() {
        List<Plataforma> creadas = new ArrayList<>();
        for (Pedido<TipoPlataforma> pedido : plataformas) {
            creadas.add(FabricaEntidades.crearPlataforma(pedido.tipo, pedido.parametros));
        }
        return creadas;
    }

    public List<Enemigo> crearEnemigos() {
        List<Enemigo> creados = new ArrayList<>();
        for (Pedido<TipoEnemigo> pedido : enemigos) {
            creados.add(FabricaEntidades.crearEnemigo(pedido.tipo, pedido.parametros));
        }
        return creados;
    }

    public Puerta crearPuerta() {
        if (puerta == null) {
            return null;
        }
        return FabricaEntidades.crearPuerta(puerta[0], puerta[1]);
    }
}
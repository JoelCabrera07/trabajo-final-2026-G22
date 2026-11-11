package modelo;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Patron Factory: a partir de un dato (el tipo) crea el objeto concreto.
 * Los niveles solo dicen "una plataforma de hielo en tal lugar" y esta clase es la
 * unica que conoce las clases concretas. En vez de un switch, cada tipo se registra
 * con una linea en un Map de tipo a lambda.
 *
 * Los parametros de cada lambda son siempre x, y, y despues lo que necesite cada clase.
 */
public class FabricaEntidades {

    private static final Map<TipoPlataforma, Function<double[], Plataforma>> PLATAFORMAS = new EnumMap<>(TipoPlataforma.class);
    private static final Map<TipoEnemigo, Function<double[], Enemigo>> ENEMIGOS = new EnumMap<>(TipoEnemigo.class);

    static {
        // Plataformas: x, y, ancho, alto (la movil agrega limiteIzquierdo y limiteDerecho)
        PLATAFORMAS.put(TipoPlataforma.NORMAL,   p -> new PlataformaNormal(p[0], p[1], (int) p[2], (int) p[3]));
        PLATAFORMAS.put(TipoPlataforma.BARRO,    p -> new PlataformaBarro(p[0], p[1], (int) p[2], (int) p[3]));
        PLATAFORMAS.put(TipoPlataforma.HIELO,    p -> new PlataformaHielo(p[0], p[1], (int) p[2], (int) p[3]));
        PLATAFORMAS.put(TipoPlataforma.ROMPIBLE, p -> new PlataformaRompible(p[0], p[1], (int) p[2], (int) p[3]));
        PLATAFORMAS.put(TipoPlataforma.TRAMPOLIN, p -> new PlataformaTrampolin(p[0], p[1], (int) p[2], (int) p[3]));
        PLATAFORMAS.put(TipoPlataforma.MOVIL,    p -> new PlataformaMovil(p[0], p[1], (int) p[2], (int) p[3], p[4], p[5]));

        // Enemigos: x, y (el acechador agrega su velocidad)
        ENEMIGOS.put(TipoEnemigo.TERRESTRE, p -> new EnemigoTerrestre(p[0], p[1]));
        ENEMIGOS.put(TipoEnemigo.VOLADOR,   p -> new EnemigoVolador(p[0], p[1]));
        ENEMIGOS.put(TipoEnemigo.TORRETA,   p -> new EnemigoTorreta(p[0], p[1]));
        ENEMIGOS.put(TipoEnemigo.ACORAZADO, p -> new EnemigoAcorazado(p[0], p[1]));
        ENEMIGOS.put(TipoEnemigo.ACECHADOR, p -> new EnemigoAcechador(p[0], p[1], p[2]));
    }

    private FabricaEntidades() {
    }

    public static Plataforma crearPlataforma(TipoPlataforma tipo, double... parametros) {
        return PLATAFORMAS.get(tipo).apply(parametros);
    }

    public static Enemigo crearEnemigo(TipoEnemigo tipo, double... parametros) {
        return ENEMIGOS.get(tipo).apply(parametros);
    }

    public static Puerta crearPuerta(double x, double y) {
        return new Puerta(x, y);
    }
}
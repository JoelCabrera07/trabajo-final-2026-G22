package vista;

import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;
import modelo.Sprites;
import modelo.TipoEnemigo;
import modelo.TipoPlataforma;

public class CatalogoSprites {

    private final Map<TipoPlataforma, BufferedImage> plataformas = new EnumMap<>(TipoPlataforma.class);
    private final Map<TipoEnemigo, BufferedImage[]> enemigosDerecha = new EnumMap<>(TipoEnemigo.class);
    private final Map<TipoEnemigo, BufferedImage[]> enemigosIzquierda = new EnumMap<>(TipoEnemigo.class);

    private final BufferedImage[] portal = Sprites.getPortal();
    private final BufferedImage[] orbe = Sprites.getOrbe();

    public CatalogoSprites() {
        plataformas.put(TipoPlataforma.NORMAL,     Sprites.getSpritesPlataforma(0)[0]);
        plataformas.put(TipoPlataforma.BARRO,      Sprites.getSpritesPlataforma(1)[0]);
        plataformas.put(TipoPlataforma.HIELO,      Sprites.getSpritesPlataforma(2)[0]);
        plataformas.put(TipoPlataforma.MOVIL,      Sprites.getSpritesPlataforma(0)[0]);
        plataformas.put(TipoPlataforma.ROMPIBLE,   Sprites.getSpritesPlataforma(3)[0]);
        plataformas.put(TipoPlataforma.TRAMPOLIN,  Sprites.getSpritesPlataforma(4)[0]);

        BufferedImage[] terrestre = Sprites.getEnemigoTerrestre();
        enemigosDerecha.put(TipoEnemigo.TERRESTRE, terrestre);
        enemigosIzquierda.put(TipoEnemigo.TERRESTRE, Sprites.voltearAnimacion(terrestre));

        BufferedImage[] volador = Sprites.getEnemigoVolador();
        enemigosDerecha.put(TipoEnemigo.VOLADOR, volador);
        enemigosIzquierda.put(TipoEnemigo.VOLADOR, Sprites.voltearAnimacion(volador));

        BufferedImage imgAcorazado = Sprites.cargar("/assets/enemigo_acorazado.png");
        if (imgAcorazado != null) {
            BufferedImage[] acorazado = { imgAcorazado };
            enemigosDerecha.put(TipoEnemigo.ACORAZADO, acorazado);
            enemigosIzquierda.put(TipoEnemigo.ACORAZADO, acorazado);
        }
    }

    public BufferedImage imagenDe(TipoPlataforma tipo) {
        return plataformas.get(tipo);
    }
    public BufferedImage[] fotogramasDe(TipoEnemigo tipo, boolean haciaDerecha) {
    return haciaDerecha ? enemigosDerecha.get(tipo) : enemigosIzquierda.get(tipo);
    }
    public BufferedImage[] fotogramasPortal(){
        return portal;
    }
    public BufferedImage[] fotogramasOrbe(){
        return orbe;
    }
}

package vista;

import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;
import modelo.Sprites;
import modelo.TipoPlataforma;

public class CatalogoSprites {

    private final Map<TipoPlataforma, BufferedImage> plataformas = new EnumMap<>(TipoPlataforma.class);
    private final BufferedImage[] portal = Sprites.getPortal();
    private final BufferedImage[] orbe = Sprites.getOrbe();

    public CatalogoSprites() {
        plataformas.put(TipoPlataforma.NORMAL,     Sprites.getSpritesPlataforma(0)[0]);
        plataformas.put(TipoPlataforma.BARRO,      Sprites.getSpritesPlataforma(1)[0]);
        plataformas.put(TipoPlataforma.HIELO,      Sprites.getSpritesPlataforma(2)[0]);
        plataformas.put(TipoPlataforma.MOVIL,      Sprites.getSpritesPlataforma(0)[0]);
        plataformas.put(TipoPlataforma.ROMPIBLE,   Sprites.getSpritesPlataforma(3)[0]);
        plataformas.put(TipoPlataforma.TRAMPOLIN,  Sprites.getSpritesPlataforma(4)[0]);
    }

    public BufferedImage imagenDe(TipoPlataforma tipo) {
        return plataformas.get(tipo);
    }
    public BufferedImage[] fotogramasPortal(){
        return portal;
    }
    public BufferedImage[] fotogramasOrbe(){
        return orbe;
    }
}

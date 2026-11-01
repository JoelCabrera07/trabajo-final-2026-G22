package vista;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import modelo.Sprites;
import modelo.TipoEntidad;

public class CatalogoSprites {
    private final Map<TipoEntidad, BufferedImage> imagenes = new HashMap<>();
    private final Map<TipoEntidad, BufferedImage[]> animaciones = new HashMap<>();
    private final Map<TipoEntidad, BufferedImage[]> animacionesIzquierda = new HashMap<>();

    public CatalogoSprites() {
        imagenes.put(TipoEntidad.PLATAFORMA_NORMAL, Sprites.getSpritesPlataforma(0)[0]);
        imagenes.put(TipoEntidad.PLATAFORMA_MOVIL, Sprites.getSpritesPlataforma(0)[0]); // Usa el mismo que plataforma
        imagenes.put(TipoEntidad.PLATAFORMA_BARRO, Sprites.getSpritesPlataforma(1)[0]);
        imagenes.put(TipoEntidad.PLATAFORMA_HIELO, Sprites.getSpritesPlataforma(2)[0]);
        imagenes.put(TipoEntidad.PLATAFORMA_ROMPIBLE, Sprites.getSpritesPlataforma(3)[0]);
        imagenes.put(TipoEntidad.PLATAFORMA_TRAMPOLIN, Sprites.getSpritesPlataforma(4)[0]);
        animaciones.put(TipoEntidad.PUERTA, Sprites.getPortal());
        animaciones.put(TipoEntidad.ENEMIGO_VOLADOR, Sprites.getEnemigoVolador());
        animacionesIzquierda.put(TipoEntidad.ENEMIGO_VOLADOR, Sprites.voltearAnimacion(Sprites.getEnemigoVolador()));
        animaciones.put(TipoEntidad.ENEMIGO_TERRESTRE, Sprites.getEnemigoTerrestre());
        animacionesIzquierda.put(TipoEntidad.ENEMIGO_TERRESTRE,
                Sprites.voltearAnimacion(Sprites.getEnemigoTerrestre()));
    }

    // Devuelve la imagen que corresponde a ese tipo, o null si no tiene
    public BufferedImage getImagen(TipoEntidad tipo) {
        return imagenes.get(tipo);
    }

    // Devuelve los fotogramas de la animación de ese tipo, o null si no tiene
    public BufferedImage[] getAnimacion(TipoEntidad tipo) {
        return animaciones.get(tipo);
    }

    // Devuelve la animación mirando hacia el lado pedido. Si no tiene versión
    // izquierda, devuelve la normal
    public BufferedImage[] getAnimacion(TipoEntidad tipo, boolean mirandoDerecha) {
        if (!mirandoDerecha && animacionesIzquierda.containsKey(tipo)) {
            return animacionesIzquierda.get(tipo);
        }
        return animaciones.get(tipo);
    }
}

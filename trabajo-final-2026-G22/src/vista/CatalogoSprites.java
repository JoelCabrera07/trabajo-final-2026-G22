package vista;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.awt.Rectangle;
import modelo.EstadoEnemigo;
import modelo.EstadoHeroe;
import modelo.Sprites;
import modelo.TipoEntidad;

public class CatalogoSprites {
    private final Map<TipoEntidad, BufferedImage> imagenes = new HashMap<>();
    private final Map<TipoEntidad, BufferedImage[]> animaciones = new HashMap<>();
    private final Map<TipoEntidad, BufferedImage[]> animacionesIzquierda = new HashMap<>();
    private final Map<EstadoHeroe, BufferedImage[]> animacionesHeroe = new HashMap<>();
    private final Map<EstadoHeroe, BufferedImage[]> animacionesHeroeIzquierda = new HashMap<>();
    private final Map<EstadoEnemigo, BufferedImage[]> animacionesPorEstado = new HashMap<>();
    private final Map<EstadoEnemigo, BufferedImage[]> animacionesPorEstadoIzquierda = new HashMap<>();
    // Dónde se dibuja cada tipo respecto de su posición: desplazamiento x, y, y
    // tamaño del dibujo
    private final Map<TipoEntidad, Rectangle> areasDibujo = new HashMap<>();

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
        // Héroe: una animación por estado (las versiones izquierda se generan
        // espejadas)
        animacionesHeroe.put(EstadoHeroe.QUIETO, Sprites.getHeroeQuieto());
        animacionesHeroe.put(EstadoHeroe.CORRIENDO, Sprites.getHeroeMovimiento());
        animacionesHeroe.put(EstadoHeroe.CARGANDO_SALTO, Sprites.getHeroeImpulso());
        animacionesHeroe.put(EstadoHeroe.DESPEGANDO, Sprites.getHeroeImpulso());
        animacionesHeroe.put(EstadoHeroe.EN_EL_AIRE, Sprites.getHeroeSaltoCaida());
        animacionesHeroe.put(EstadoHeroe.ATERRIZANDO, Sprites.getHeroeSaltoCaida());
        // El acechador mide 20x20 pero su dibujo es de 68x72: se centra sobre la hitbox
        areasDibujo.put(TipoEntidad.ENEMIGO_ACECHADOR, new Rectangle(-24, -24, 68, 72));
        for (EstadoHeroe estado : EstadoHeroe.values()) {
            animacionesHeroeIzquierda.put(estado, Sprites.voltearAnimacion(animacionesHeroe.get(estado)));
        }
        // Acechador: una animación por estado (OCULTO no se dibuja)
        animacionesPorEstado.put(EstadoEnemigo.APARECIENDO, Sprites.getAcechadorAparecer());
        animacionesPorEstado.put(EstadoEnemigo.PERSIGUIENDO, Sprites.getAcechadorFlotar());
        animacionesPorEstado.put(EstadoEnemigo.ATACANDO, Sprites.getAcechadorAtacar());
        animacionesPorEstado.put(EstadoEnemigo.DESAPARECIENDO, Sprites.getAcechadorDesaparecer());
        for (EstadoEnemigo estado : animacionesPorEstado.keySet()) {
            animacionesPorEstadoIzquierda.put(estado, Sprites.voltearAnimacion(animacionesPorEstado.get(estado)));
        }
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

    // Devuelve la animación del héroe para su estado, mirando hacia el lado pedido
    public BufferedImage[] getAnimacionHeroe(EstadoHeroe estado, boolean mirandoDerecha) {
        if (mirandoDerecha) {
            return animacionesHeroe.get(estado);
        }
        return animacionesHeroeIzquierda.get(estado);
    }

    // Animación de un enemigo: si tiene un estado especial usa esa; si no, la de su
    // tipo
    public BufferedImage[] getAnimacionEnemigo(TipoEntidad tipo, EstadoEnemigo estado, boolean mirandoDerecha) {
        if (animacionesPorEstado.containsKey(estado)) {
            return mirandoDerecha ? animacionesPorEstado.get(estado) : animacionesPorEstadoIzquierda.get(estado);
        }
        return getAnimacion(tipo, mirandoDerecha);
    }

    // Dónde se dibuja ese tipo, o null si se dibuja en su posición con el tamaño
    // normal
    public Rectangle getAreaDibujo(TipoEntidad tipo) {
        return areasDibujo.get(tipo);
    }
}

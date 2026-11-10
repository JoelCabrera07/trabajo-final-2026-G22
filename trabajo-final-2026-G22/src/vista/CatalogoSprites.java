package vista;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import modelo.Enemigo;
import modelo.FaseEnemigo;
import modelo.Sprites;
import modelo.TipoEnemigo;
import modelo.TipoPlataforma;

public class CatalogoSprites {

    private static final int ALTO_SPRITE_PREDETERMINADO = 50;

    private final Map<TipoPlataforma, BufferedImage> plataformas = new EnumMap<>(TipoPlataforma.class);
    private final Map<TipoEnemigo, Map<FaseEnemigo, BufferedImage[]>> enemigosDerecha = new EnumMap<>(TipoEnemigo.class);
    private final Map<TipoEnemigo, Map<FaseEnemigo, BufferedImage[]>> enemigosIzquierda = new EnumMap<>(TipoEnemigo.class);
    // Donde va el dibujo respecto de la hitbox; los que no estan usan la medida predeterminada
    private final Map<TipoEnemigo, Function<Enemigo, Rectangle>> areasSprite = new EnumMap<>(TipoEnemigo.class);

    private final BufferedImage[] portal = Sprites.getPortal();
    private final BufferedImage[] orbe = Sprites.getOrbe();

    private final BufferedImage[] heroeQuieto = Sprites.getHeroeQuieto();
    private final BufferedImage[] heroeCorrer = Sprites.getHeroeMovimiento();
    private final BufferedImage[] heroeImpulso = Sprites.getHeroeImpulso();
    private final BufferedImage[] heroeSaltoCaida = Sprites.getHeroeSaltoCaida();

    public CatalogoSprites() {
        plataformas.put(TipoPlataforma.NORMAL,     Sprites.getSpritesPlataforma(0)[0]);
        plataformas.put(TipoPlataforma.BARRO,      Sprites.getSpritesPlataforma(1)[0]);
        plataformas.put(TipoPlataforma.HIELO,      Sprites.getSpritesPlataforma(2)[0]);
        plataformas.put(TipoPlataforma.MOVIL,      Sprites.getSpritesPlataforma(0)[0]);
        plataformas.put(TipoPlataforma.ROMPIBLE,   Sprites.getSpritesPlataforma(3)[0]);
        plataformas.put(TipoPlataforma.TRAMPOLIN,  Sprites.getSpritesPlataforma(4)[0]);

        registrar(TipoEnemigo.TERRESTRE, FaseEnemigo.NORMAL, Sprites.getEnemigoTerrestre());
        registrar(TipoEnemigo.VOLADOR, FaseEnemigo.NORMAL, Sprites.getEnemigoVolador());

        BufferedImage imgAcorazado = Sprites.cargar("/assets/enemigo_acorazado.png");
        if (imgAcorazado != null) {
            registrar(TipoEnemigo.ACORAZADO, FaseEnemigo.NORMAL, new BufferedImage[] { imgAcorazado });
        }

        // Torreta: 0-3 reposo, 4-5 carga, 6 disparo, 7-8 enfriando
        BufferedImage[] torreta = Sprites.getTorretaMagica();
        if (torreta != null) {
            registrar(TipoEnemigo.TORRETA, FaseEnemigo.REPOSO,     Arrays.copyOfRange(torreta, 0, 4));
            registrar(TipoEnemigo.TORRETA, FaseEnemigo.CARGANDO,   Arrays.copyOfRange(torreta, 4, 6));
            registrar(TipoEnemigo.TORRETA, FaseEnemigo.DISPARANDO, Arrays.copyOfRange(torreta, 6, 7));
            registrar(TipoEnemigo.TORRETA, FaseEnemigo.ENFRIANDO,  Arrays.copyOfRange(torreta, 7, 9));
        }

        registrar(TipoEnemigo.ACECHADOR, FaseEnemigo.APARECIENDO,    Sprites.getAcechadorAparecer());
        registrar(TipoEnemigo.ACECHADOR, FaseEnemigo.FLOTANDO,       Sprites.getAcechadorFlotar());
        registrar(TipoEnemigo.ACECHADOR, FaseEnemigo.ATACANDO,       Sprites.getAcechadorAtacar());
        registrar(TipoEnemigo.ACECHADOR, FaseEnemigo.DESAPARECIENDO, Sprites.getAcechadorDesaparecer());

        // Torreta: dibujo de 60x72, el pedestal apoya en el borde de abajo de la hitbox, centrado
        areasSprite.put(TipoEnemigo.TORRETA, e -> new Rectangle(
                (int) e.getX() + e.getAncho() / 2 - 30, (int) e.getY() + e.getAlto() - 72, 60, 72));
        // Acechador: dibujo de 68x72, el cuerpo (34, 34) coincide con el centro de la hitbox
        areasSprite.put(TipoEnemigo.ACECHADOR, e -> new Rectangle(
                (int) e.getX() + e.getAncho() / 2 - 34, (int) e.getY() + e.getAlto() / 2 - 34, 68, 72));
    }

    // Guarda la animacion mirando a la derecha y su version espejada
    private void registrar(TipoEnemigo tipo, FaseEnemigo fase, BufferedImage[] derecha) {
        enemigosDerecha.computeIfAbsent(tipo, t -> new EnumMap<>(FaseEnemigo.class)).put(fase, derecha);
        enemigosIzquierda.computeIfAbsent(tipo, t -> new EnumMap<>(FaseEnemigo.class)).put(fase, Sprites.voltearAnimacion(derecha));
    }

    public BufferedImage imagenDe(TipoPlataforma tipo) {
        return plataformas.get(tipo);
    }

    public BufferedImage[] fotogramasDe(TipoEnemigo tipo, FaseEnemigo fase, boolean haciaDerecha) {
        Map<FaseEnemigo, BufferedImage[]> porFase = (haciaDerecha ? enemigosDerecha : enemigosIzquierda).get(tipo);
        return porFase == null ? null : porFase.get(fase);
    }

    public Rectangle areaSprite(Enemigo enemigo) {
        Function<Enemigo, Rectangle> area = areasSprite.get(enemigo.getTipo());
        if (area != null) {
            return area.apply(enemigo);
        }
        return new Rectangle((int) enemigo.getX(), (int) enemigo.getY(), enemigo.getAncho(), ALTO_SPRITE_PREDETERMINADO);
    }

    public BufferedImage[] fotogramasPortal() {
        return portal;
    }

    public BufferedImage[] fotogramasOrbe() {
        return orbe;
    }

    // "voltearAnimacion" guarda el resultado, asi que espejar a cada consulta no cuesta nada
    public BufferedImage[] heroeQuieto(boolean derecha)     { return derecha ? heroeQuieto : Sprites.voltearAnimacion(heroeQuieto); }
    public BufferedImage[] heroeCorrer(boolean derecha)     { return derecha ? heroeCorrer : Sprites.voltearAnimacion(heroeCorrer); }
    public BufferedImage[] heroeImpulso(boolean derecha)    { return derecha ? heroeImpulso : Sprites.voltearAnimacion(heroeImpulso); }
    public BufferedImage[] heroeSaltoCaida(boolean derecha) { return derecha ? heroeSaltoCaida : Sprites.voltearAnimacion(heroeSaltoCaida); }
}
package modelo;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 * Utilidad simple para cargar sprites. Usa el mismo mecanismo que ya usa
 * MenuPrincipal para su imagen de fondo (getResource, via el classpath) en
 * vez de leer directo del disco -- asi funciona igual sin importar si lo
 * corres con el boton Run de VSCode o desde la terminal con java -cp bin,
 * porque no depende de cual sea la carpeta "actual" en cada caso.
 *
 * Para que esto funcione, los sprites tienen que estar DENTRO de src/assets
 * (no en un assets/ suelto en la raiz del proyecto), igual que recursos/
 * esta dentro de src/.
 */
public class Sprites {

    // Constructor privado: esta clase no se instancia, solo se usa su metodo estatico
    private Sprites() {
    }

    public static BufferedImage cargar(String ruta) {
        URL url = Sprites.class.getResource(ruta);
        if (url == null) {
            System.out.println("Aviso: no se encontro el sprite '" + ruta
                    + "' (tiene que estar en src" + ruta + "). Se usa el rectangulo de color.");
            return null;
        }
        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            System.out.println("Aviso: no se pudo cargar el sprite '" + ruta + "': " + e.getMessage());
            return null;
        }
    }

    // -- LOGICA PARA LA HOJA DE SPRITES MAESTRA DE ENEMIGOS ---
    private static BufferedImage hojaEnemigos = null;

    private static void cargarHojaMaestra() {
        if (hojaEnemigos == null) {
            hojaEnemigos = cargar("/assets/enemigos.png");
        }
    }

    // Limpia líneas de guía del boceto y bordes residuales del recuadro
    private static BufferedImage limpiarSprite(BufferedImage img) {
        if (img == null) return null;
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage limpio = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgba = img.getRGB(x, y);
                int a = (rgba >> 24) & 0xff;
                int r = (rgba >> 16) & 0xff;
                int g = (rgba >> 8) & 0xff;
                int b = rgba & 0xff;

                if (a == 0) continue;

                // 1. Filtra las líneas de guía celestes/grises del boceto original
                boolean esGuia = (r >= 90 && r <= 150 && g >= 105 && g <= 160 && b >= 115 && b <= 170);

                // 2. Filtra líneas negras residuales en los bordes extremos del recorte
                boolean esBordeExtremo = (r < 30 && g < 30 && b < 30) && (x < 2 || x >= w - 2 || y < 2 || y >= h - 2);

                if (!esGuia && !esBordeExtremo) {
                    limpio.setRGB(x, y, rgba);
                }
            }
        }
        return limpio;
    }

    // Metodo para pedirle el corte del EnemigoTerreste (4 fotogramas limpios sin líneas)
    public static BufferedImage[] getEnemigoTerrestre() {
        cargarHojaMaestra();
        if (hojaEnemigos == null) return null;

        int[][] coords = {
            { 43, 153, 165, 180 },
            { 214, 153, 180, 180 },
            { 399, 153, 182, 180 },
            { 586, 153, 182, 180 }
        };

        BufferedImage[] frames = new BufferedImage[4];
        for (int i = 0; i < 4; i++) {
            BufferedImage sub = hojaEnemigos.getSubimage(coords[i][0], coords[i][1], coords[i][2], coords[i][3]);
            frames[i] = limpiarSprite(sub);
        }
        return frames;
    }

    // Metodo para pedirle los fotogramas de vuelo del Enemigo Volador
    public static BufferedImage[] getEnemigoVolador() {
        cargarHojaMaestra();
        if (hojaEnemigos == null) return null;

        BufferedImage f0 = limpiarSprite(hojaEnemigos.getSubimage(41, 563, 238, 240));  // Alas abajo
        BufferedImage f1 = limpiarSprite(hojaEnemigos.getSubimage(283, 563, 246, 240)); // Alas medio
        BufferedImage f2 = limpiarSprite(hojaEnemigos.getSubimage(533, 563, 233, 240)); // Alas arriba

        return new BufferedImage[] { f0, f1, f2, f1 };
    }

    // Metodo espejo para que la animacion tenga mas sentido al cambiar de orientacion
    public static BufferedImage[] voltearAnimacion(BufferedImage[] original) {
        if (original == null) return null;
        BufferedImage[] volteados = new BufferedImage[original.length];

        for (int i = 0; i < original.length; i++) {
            BufferedImage img = original[i];
            if (img == null) continue;
            BufferedImage flipped = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics2D g = flipped.createGraphics();

            g.drawImage(img, img.getWidth(), 0, -img.getWidth(), img.getHeight(), null);
            g.dispose();

            volteados[i] = flipped;
        }
        return volteados;
    }

    // =========================================================================
    // -- LOGICA PARA LA HOJA DE SPRITES DEL HEROE (Sprite_Heroe.png) --
    // =========================================================================
    private static BufferedImage hojaHeroe = null;

    private static void cargarHojaHeroe() {
        if (hojaHeroe == null) {
            hojaHeroe = cargar("/assets/Sprite_Heroe.png");
        }
    }

    // Dimensiones estandarizadas del lienzo para que todos los fotogramas
    // compartan la misma escala y alineacion respecto al suelo.
    private static final int HEROE_CANVAS_W = 86;
    private static final int HEROE_CANVAS_H = 130;
    private static final int HEROE_ANCHOR_X = 43;
    private static final int HEROE_ANCHOR_Y = 126;

    private static BufferedImage crearFrameHeroe(int srcX, int srcY, int srcW, int srcH, int torsoX, int groundY) {
        cargarHojaHeroe();
        if (hojaHeroe == null) return null;

        BufferedImage frame = new BufferedImage(HEROE_CANVAS_W, HEROE_CANVAS_H, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = frame.createGraphics();

        int destX = HEROE_ANCHOR_X - (torsoX - srcX);
        int destY = HEROE_ANCHOR_Y - (groundY - srcY);

        g.drawImage(hojaHeroe, destX, destY, destX + srcW, destY + srcH,
                srcX, srcY, srcX + srcW, srcY + srcH, null);
        g.dispose();

        return frame;
    }

    // Fila 1: Quieto (Frames 0 y 1)
    public static BufferedImage[] getHeroeQuieto() {
        return new BufferedImage[] {
            crearFrameHeroe(28, 34, 51, 96, 53, 130),
            crearFrameHeroe(116, 34, 53, 96, 142, 130)
        };
    }

    // Fila 1: Movimiento / Correr (Frames 2 a 5)
    public static BufferedImage[] getHeroeMovimiento() {
        return new BufferedImage[] {
            crearFrameHeroe(191, 34, 71, 96, 226, 130),
            crearFrameHeroe(283, 36, 76, 94, 320, 130),
            crearFrameHeroe(372, 37, 84, 93, 413, 130),
            crearFrameHeroe(467, 35, 81, 94, 506, 130)
        };
    }

    // Fila 2: Tomando Impulso (Frames 0 a 4)
    public static BufferedImage[] getHeroeImpulso() {
        return new BufferedImage[] {
            crearFrameHeroe(25, 170, 61, 95, 55, 265),
            crearFrameHeroe(113, 172, 62, 93, 143, 265),
            crearFrameHeroe(210, 180, 61, 85, 240, 265),
            crearFrameHeroe(303, 183, 62, 82, 333, 265),
            crearFrameHeroe(393, 151, 71, 106, 428, 265)
        };
    }

    // Fila 3: Salto y Caída (Frames 0 a 5)
    public static BufferedImage[] getHeroeSaltoCaida() {
        return new BufferedImage[] {
            crearFrameHeroe(24, 319, 57, 101, 52, 420),
            crearFrameHeroe(113, 309, 75, 106, 150, 420),
            crearFrameHeroe(202, 295, 84, 100, 243, 420),
            crearFrameHeroe(308, 295, 61, 104, 338, 420),
            crearFrameHeroe(387, 313, 73, 104, 423, 420),
            crearFrameHeroe(477, 331, 76, 89, 514, 420)
        };
    }
    // -- LÓGICA PARA LA HOJA DE PLATAFORMAS --
    private static BufferedImage hojaPlataformas = null;

    private static void cargarHojaPlataformas() {
        if (hojaPlataformas == null) {
            // Cargamos la imagen con el fondo rojo
            hojaPlataformas = cargar("/assets/plataformas.png");
        }
    }

    // Algoritmo Croma: borramos el fondo rojo píxel por píxel
    private static BufferedImage removerFondoRojo(BufferedImage img) {
        if (img == null) return null;
        int w = img.getWidth();
        int h = img.getHeight();
        // Creamos un lienzo transparente (ARGB) del mismo tamaño
        BufferedImage limpio = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgba = img.getRGB(x, y);
                int r = (rgba >> 16) & 0xff;
                int g = (rgba >> 8) & 0xff;
                int b = rgba & 0xff;

                // Detectamos el rojo puro (con tolerancia por la compresión JPG)
                // Mucho rojo (>200), casi nada de verde (<50) y casi nada de azul (<50)
                // Ampliamos la tolerancia para borrar los bordes rojizos difuminados
                boolean esFondoRojo = (r > 120 && g < 90 && b < 90);

                if (!esFondoRojo) {
                    limpio.setRGB(x, y, rgba); // Si NO es rojo, pintamos el píxel de la plataforma
                }
                // Si es rojo, lo ignoramos y queda transparente automáticamente
            }
        }
        return limpio;
    }

    /**
     * Devuelve los 4 fotogramas de la plataforma solicitada.
     * Fila 0 = Castillo (Normal)
     * Fila 1 = Barro
     * Fila 2 = Hielo
     * Fila 3 = Rompible
     * Fila 4 = Trampolín
     */
        // La hoja ya sin el fondo rojo. Se calcula UNA sola vez y se reutiliza
    // (antes se recorrian los 4 millones de pixeles cada vez que se creaba una plataforma).
    private static BufferedImage hojaPlataformasLimpia = null;

    public static BufferedImage[] getSpritesPlataforma(int filaTipo) {
        cargarHojaPlataformas();
        if (hojaPlataformas == null) return null;

        // 1. Limpiamos el croma rojo de la hoja entera (una sola vez)
        if (hojaPlataformasLimpia == null) {
            hojaPlataformasLimpia = removerFondoRojo(hojaPlataformas);
        }
        BufferedImage hojaLimpia = hojaPlataformasLimpia;

        // 2. Calculamos medidas dinamicamente (4 columnas, 5 filas)
        int anchoFrame = hojaLimpia.getWidth() / 4;
        int altoFrame = hojaLimpia.getHeight() / 5;

        // 3. Recortamos la fila correspondiente. Cada cuadro se recorta ademas hasta el
        //    dibujo visible, para que el sprite no traiga margen transparente: asi
        //    se puede escalar sin deformarlo y apoya justo en el borde de la plataforma.
        BufferedImage[] frames = new BufferedImage[4];
        for (int i = 0; i < 4; i++) {
            BufferedImage cuadro = hojaLimpia.getSubimage(i * anchoFrame, filaTipo * altoFrame, anchoFrame, altoFrame);
            frames[i] = recortarTransparente(cuadro);
        }
        return frames;
    }

    // Devuelve la imagen recortada al rectangulo mas chico que contiene todos los pixeles visibles
    private static BufferedImage recortarTransparente(BufferedImage img) {
        int minX = img.getWidth(), minY = img.getHeight(), maxX = -1, maxY = -1;
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                if (((img.getRGB(x, y) >> 24) & 0xff) != 0) { // alfa distinto de 0 = pixel visible
                    if (x < minX) minX = x;
                    if (x > maxX) maxX = x;
                    if (y < minY) minY = y;
                    if (y > maxY) maxY = y;
                }
            }
        }
        if (maxX < 0) return img; // imagen totalmente transparente: se deja como esta
        return img.getSubimage(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }
}

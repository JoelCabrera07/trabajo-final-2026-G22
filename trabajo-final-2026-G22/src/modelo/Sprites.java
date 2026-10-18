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
    // --LOGICA PARA LA HOJA DE SPRITES MAESTRA ---
    private static BufferedImage hojaEnemigos = null;
    // --CARGAMOS LA HOJA ENTERA UNA SOLA VEZ EN LA MEMORIA
    private static void cargarHojaMaestra(){
        if(hojaEnemigos == null){
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

        // Coordenadas exactas del interior de cada recuadro (evita las líneas negras de división)
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

        // Recortes limpios de los 3 fotogramas de aleteo (fila FLYING ENEMIES)
        BufferedImage f0 = limpiarSprite(hojaEnemigos.getSubimage(41, 563, 238, 240));  // Alas abajo
        BufferedImage f1 = limpiarSprite(hojaEnemigos.getSubimage(283, 563, 246, 240)); // Alas medio
        BufferedImage f2 = limpiarSprite(hojaEnemigos.getSubimage(533, 563, 233, 240)); // Alas arriba

        // Ciclo natural de aleteo continuo: abajo -> medio -> arriba -> medio
        return new BufferedImage[] { f0, f1, f2, f1 };
    }
//Metodo espejo para que la animacion tenga mas sentido
public static BufferedImage[] voltearAnimacion(BufferedImage[] original){
    if (original == null) return null;
    BufferedImage[] volteados = new BufferedImage[original.length];

    for (int i=0; i < original.length; i++){
        BufferedImage img = original[i];
        //Creamos un lienzo transparente delmismotamaño
        BufferedImage flipped =new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g= flipped.createGraphics();

        // Dibujamos la imagen con el ancho negativo para hacer el espejo
        g.drawImage(img,img.getWidth(),0,-img.getWidth(),img.getHeight(),null);
        g.dispose();

        volteados[i] = flipped;
    }
    return volteados;
}
}
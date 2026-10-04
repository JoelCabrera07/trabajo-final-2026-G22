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
}
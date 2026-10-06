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
// Metodo para pedirle el corte del EnemigoTerreste
public static BufferedImage[] getEnemigoTerrestre() {
    cargarHojaMaestra();
    if (hojaEnemigos == null) return null;

    //ATENCION: Cambia el 64 por los pixeles reales que mida el cuadradito
    int  xInicio = 43;
    int yInicio = 150;
    int ancho = 168;
    int alto = 188;

    //Creamos la lista para guardar los 4 fotogramas
    BufferedImage[] frames = new BufferedImage[4];

    //Distancia entre el primer cuadro y el segundo
    int separacionX = 167;

    for (int i = 0; i < 4; i++){
        //Calculamos la posicion X para este fotograma en particular
        int xActual = xInicio + (i * separacionX);
        //Ahora recortamos
        frames[i] = hojaEnemigos.getSubimage(xActual,yInicio,ancho,alto);
    }
    return frames;
}
//Metodo para pedirle el recorte del Enemigo Volador
public static BufferedImage getEnemigoVolador(){
    cargarHojaMaestra();
    if (hojaEnemigos == null) return null;
    int xInicio = 41;
    int yInicio = 560;
    int ancho = 246;
    int alto = 241; //MODIFICABLES

    //Recorta desde x=0, pero bajando en el eje Y (Fila de abajo)
    // Vasa tener que ajustar este "alto *2" dependiendo en que fila empiece el murcielago
    return hojaEnemigos.getSubimage(xInicio,yInicio, ancho, alto);
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
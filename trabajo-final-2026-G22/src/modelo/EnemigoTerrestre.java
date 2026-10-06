package modelo;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class EnemigoTerrestre extends Enemigo {
    private BufferedImage[] animacionDerecha;
    private BufferedImage[] animacionIzquierda;
    private int frameActual = 0;
    private int tickAnimacion = 0;
    private int velocidadAnimacion = 10; //Cada cuantos "ticks" cambia el dibujo
    
    private double velocidadPatrullaje;

    private double limiteIzquierdo;
    private double limiteDerecho;

    private boolean moviendoseDerecha;
    

    public EnemigoTerrestre(double x, double y) {
        super("Enemigo Terrestre", x, y, 30, 30, Color.RED, 10, 1);
        this.animacionDerecha = Sprites.getEnemigoTerrestre();
        this.animacionIzquierda = Sprites.voltearAnimacion(this.animacionDerecha);

        //Le ponemos el primer frame de la derecha para que arranque dibujado
        if(this.animacionDerecha != null){
            this.setSprite(this.animacionDerecha[0]);
        }
        
        this.velocidadPatrullaje = 2.0;

        // Por ahora patrulla 100px para cada lado de donde nace.
        // Despues podemos hacer que para cada instancia patrulle un tramo distinto.
        this.limiteIzquierdo = x - 100;
        this.limiteDerecho = x + 100;
        this.moviendoseDerecha = true; // arranca yendo hacia la derecha

        }
    

    public void patrullar() {
        if (moviendoseDerecha) {
            // Se mueve hacia la derecha sumando la velocidad a su posición x actual
            this.setX(this.getX() + velocidadPatrullaje);
            //Si llega al límite derecho, cambia de dirección
            if (this.getX() >= limiteDerecho) {
                moviendoseDerecha = false; 
            }
        } else {
            //Lo mismo pero hacia la izquierda
            this.setX(this.getX() - velocidadPatrullaje);
            if (this.getX() <= limiteIzquierdo) {
                moviendoseDerecha = true;
            }
        }
        this.actualizarAnimacion(); // Para que funcione la animacion de caminar
    }
    public void actualizarAnimacion(){
        // Verificamos las dos nuevas variables en lugar de animacionCaminar
        if(animacionDerecha == null || animacionIzquierda == null) return;
        tickAnimacion++;

        //Cuando el contador alcanza la velocidad, pasamos al siguiente frame
        if (tickAnimacion >= velocidadAnimacion) {
            tickAnimacion = 0; //Reiniciamos el reloj
            frameActual ++;

            if(frameActual >= animacionDerecha.length){ //Con esto arreglamos el crasheo que queria leer mas del fotograma 4
                frameActual = 0;
            }
            //Asignamos el sprite correspondientesegun la direccion de la patrulla
            if(moviendoseDerecha){
                this.setSprite(animacionDerecha[frameActual]);
            } else {
                this.setSprite(animacionIzquierda[frameActual]);
            }
        }
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        // Aplicamos polimorfismo: le saca vida al héroe según el ataque de este bicho
        h.recibirDanio(this.getAtaque());
    }

    @Override
    public void actualizar(Heroe heroe, Escenario escenario) {
        patrullar(); // Por ahora solo patrulla, no hace nada con el héroe ni con el escenario
    }

}
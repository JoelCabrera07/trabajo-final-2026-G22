package modelo;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class EnemigoVolador extends Enemigo {
    
    private double amplitudVuelo;

    //el centro vertical del cual se mueve,X se mantiene fijo, no hace falta guardarlo
    private double centroY;

    //Angulo actual del movimiento (en radianes)
    private double angulo;

    //Que tan rapido se mueve
    private static final double VELOCIDAD_ANGULAR = 0.03;

    // Animación de aleteo en vuelo (igual que EnemigoTerrestre con su caminata)
    private BufferedImage[] animacionVueloDerecha;
    private BufferedImage[] animacionVueloIzquierda;
    private int frameActual = 0;
    private int tickAnimacion = 0;
    private int velocidadAnimacion = 8; // Cada 8 ticks cambia el cuadro del aleteo

    public EnemigoVolador(double x, double y) {
        // Un poco más frágil pero molesto (ajustamos a 35x30 para mejor proporción visual)
        super("Enemigo Volador", x, y, 35, 30, Color.CYAN, 5, 1);
        this.amplitudVuelo = 50.0; // Píxeles que se mueve de su eje
        this.centroY = y;
        this.angulo = 0;

        // Cargamos la animación de aleteo
        this.animacionVueloDerecha = Sprites.getEnemigoVolador();
        this.animacionVueloIzquierda = Sprites.voltearAnimacion(this.animacionVueloDerecha);

        if (this.animacionVueloDerecha != null && this.animacionVueloDerecha.length > 0) {
            this.setSprite(this.animacionVueloDerecha[0]);
        }
    }

    public void flotarVerticalmente() {
        //Avanza el ángulo en cada frame
        this.angulo += VELOCIDAD_ANGULAR;
        if(angulo >= 2 * Math.PI){
            angulo -= 2 * Math.PI; //para que no crezca indefinidamente
        }
        //Calcula la nueva posición vertical usando una función seno
        double nuevoY = centroY + amplitudVuelo * Math.sin(angulo);
        this.setY(nuevoY);
    }

    public void actualizarAnimacion(Heroe heroe) {
        if (animacionVueloDerecha == null || animacionVueloIzquierda == null) return;
        tickAnimacion++;

        if (tickAnimacion >= velocidadAnimacion) {
            tickAnimacion = 0;
            frameActual = (frameActual + 1) % animacionVueloDerecha.length;

            // Orienta el murciélago mirando hacia donde está el héroe
            if (heroe != null && heroe.getX() < this.getX()) {
                this.setSprite(animacionVueloIzquierda[frameActual]);
            } else {
                this.setSprite(animacionVueloDerecha[frameActual]);
            }
        }
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        h.recibirDanio(this.getAtaque());
    }

    @Override 
    public void actualizar(Heroe heroe, Escenario escenario) {
        flotarVerticalmente();
        actualizarAnimacion(heroe);
    }
}

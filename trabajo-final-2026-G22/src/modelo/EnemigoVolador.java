package modelo;
import java.awt.Color;

public class EnemigoVolador extends Enemigo {
    
    private double amplitudVuelo;

    //el centro vertical del cual se mueve,X se mantiene fijo, no hace falta guardarlo
    private double centroY;

    //Angulo actual del movimiento (en radianes)
    private double angulo;

    //Que tan rapido se mueve
    private static final double VELOCIDAD_ANGULAR = 0.03;

    public EnemigoVolador(double x, double y) {
        // Un poco más frágil pero molesto
        super("Enemigo Volador", x, y, 35, 35, Color.CYAN, 5, 1);
        this.amplitudVuelo = 50.0; // Píxeles que se mueve de su eje
        //donde nace es el centro de desplazamiento
        this.centroY = y;
        this.angulo = 0;
    }

    public void flotarVerticalmente() {
        //Avanza el ángulo en cada frame
        this.angulo += VELOCIDAD_ANGULAR;
        if(angulo >=2*Math.PI){
            angulo -= 2*Math.PI; //para que no crezca indefinidamente
        }
        //Calcula la nueva posición vertical usando una función seno
        double nuevoY = centroY + amplitudVuelo * Math.sin(angulo);
        this.setY(nuevoY);
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        h.recibirDanio(this.getAtaque());
    }
}

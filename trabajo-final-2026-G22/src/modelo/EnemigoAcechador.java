package modelo;
import java.awt.Color;

public class EnemigoAcechador extends Enemigo {
    
    private int tiempoDeteccion; //segundos que el heroe debe quedarse quieto para que el acechador empiece a perseguirlo

    private static final double VELOCIDAD_PERSECUCION = 1.0; //pixeles por frame
    private static final double DISTANCIA_APARICION= 180; //px de distancia al aparecer

    private boolean activo; //false hasta que el heroe se quede quieto el tiempo suficiente, true cuando empieza a perseguirlo

    // Guardamos la ultima posición del héroe para saber si se quedó quieto
    private double ultimaXHeroe;
    private double ultimaYHeroe;

    // Momento del ultimo frame en que se actualizó la posición del héroe, para medir el tiempo que estuvo quieto
    private long momentoUltimoMovimiento;


    public EnemigoAcechador(double x, double y) {
        // Bicho raro que aparece si te quedás quieto
        super("Acechador", x, y, 30, 30, Color.MAGENTA, 8, 2);
        this.tiempoDeteccion = 5; // Segundos antes de empezar a perseguirte
        this.activo = false;
        this.momentoUltimoMovimiento = System.currentTimeMillis();
    }

    public void perseguir(Heroe h) {
        if(!activo) {
            // Si no está activo, no hace nada
            if (h.getX()!= ultimaXHeroe || h.getY()!= ultimaYHeroe) {
                // Si el héroe se movió, reiniciamos el tiempo de detección
                momentoUltimoMovimiento = System.currentTimeMillis();
                ultimaXHeroe = h.getX();
                ultimaYHeroe = h.getY();
                return;
            }
        //Revisamos si el héroe se quedó quieto el tiempo suficiente para activar al acechador
        long tiempoQuieto = System.currentTimeMillis() - momentoUltimoMovimiento;
        if (tiempoQuieto >= tiempoDeteccion * 1000L){
            //se cumplen los 5 segundos, el acechador se activa y aparece a cierta distancia del héroe
            setX(h.getX() + DISTANCIA_APARICION);
            setY(h.getY());
            activo = true;
        } 
        return; // No persigue en el mismo frame que aparece
        }

    //al estar activo camina lentamente hacia el héroe
   if (h.getX() > getX()) {
            setX(getX() + VELOCIDAD_PERSECUCION);
    } else if (h.getX() < getX()) {
            setX(getX() - VELOCIDAD_PERSECUCION);
        }

    if (h.getY() > getY()) {
            setY(getY() + VELOCIDAD_PERSECUCION);
    } else if (h.getY() < getY()) {
            setY(getY() - VELOCIDAD_PERSECUCION);
        }
    }

    //para que el panelJuego sepa si debe dibujar al acechador o no, ya que no aparece hasta que el heroe se queda quieto
    public boolean isActivo() {
        return activo;
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        h.recibirDanio(this.getAtaque());
    }
}

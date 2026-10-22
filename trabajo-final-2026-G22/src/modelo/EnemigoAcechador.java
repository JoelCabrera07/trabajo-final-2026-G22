package modelo;
import java.awt.Color;

public class EnemigoAcechador extends Enemigo {
    
    private double tiempoDeteccion; //segundos que el heroe debe quedarse quieto para que el acechador empiece a perseguirlo

    private double VELOCIDAD_PERSECUCION; //pixeles por frame
    private static final double DISTANCIA_APARICION= 180; //px de distancia al aparecer

    private boolean activo; //false hasta que el heroe se quede quieto el tiempo suficiente, true cuando empieza a perseguirlo

    // Guardamos la ultima posición del héroe para saber si se quedó quieto
    private double ultimaXHeroe;
    private double ultimaYHeroe;

    // Momento del ultimo frame en que se actualizó la posición del héroe, para medir el tiempo que estuvo quieto
    private long momentoUltimoMovimiento;


    public EnemigoAcechador(double x, double y, double velocidad) {
        // Bicho raro que aparece si te quedás quieto
        super("Acechador", x, y, 20, 20, Color.MAGENTA, 8, 2);
        this.tiempoDeteccion = 1.5; // Segundos antes de empezar a perseguirte
        this.activo = false;
        this.momentoUltimoMovimiento = System.currentTimeMillis();
        this.VELOCIDAD_PERSECUCION = velocidad;
    }

    public void perseguir(Heroe h) {
        if(!activo) {
            double difX = Math.abs(h.getX() - ultimaXHeroe);
            double difY = Math.abs(h.getY() - ultimaYHeroe);
            if (difX > 3.0 || difY > 3.0) {
                // Si el héroe se movió, reiniciamos el tiempo de detección
                momentoUltimoMovimiento = System.currentTimeMillis();
                ultimaXHeroe = h.getX();
                ultimaYHeroe = h.getY();
                return;
            }
        //Revisamos si el héroe se quedó quieto el tiempo suficiente para activar al acechador
        long tiempoQuieto = System.currentTimeMillis() - momentoUltimoMovimiento;
        if (tiempoQuieto >= (long) tiempoDeteccion * 1000){
            System.out.println("¡EL ACECHADOR SE ACTIVÓ EN LA CONSOLA!");
            //se cumplen los 1.5 segundos, el acechador se activa y aparece a cierta distancia del héroe
            setX(h.getX() + DISTANCIA_APARICION + (Math.random() * 100 - 50));
            setY(h.getY() - 20 - (Math.random() * 60)); // Aparece un poco arriba del héroe
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

    @Override
    public void actualizar(Heroe heroe, Escenario escenario) {
        perseguir(heroe);
    }

    @Override 
    public boolean debeDibujarse() {
        return isActivo(); // Solo se dibuja si está activo
    }
}

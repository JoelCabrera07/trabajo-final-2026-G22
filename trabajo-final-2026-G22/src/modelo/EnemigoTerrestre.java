package modelo;
import java.awt.Color;

public class EnemigoTerrestre extends Enemigo {
    
    private double velocidadPatrullaje;

    private double limiteIzquierdo;
    private double limiteDerecho;

    private boolean moviendoseDerecha;

    public EnemigoTerrestre(double x, double y) {
        super("Enemigo Terrestre", x, y, 40, 40, Color.RED, 10, 1);
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
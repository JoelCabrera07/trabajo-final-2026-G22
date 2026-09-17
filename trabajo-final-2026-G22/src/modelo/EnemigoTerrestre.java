package modelo;
import java.awt.Color;

public class EnemigoTerrestre extends Enemigo {
    
    private double velocidadPatrullaje;

    public EnemigoTerrestre(double x, double y) {
        super("Enemigo Terrestre", x, y, 40, 40, Color.RED, 10, 1);
        this.velocidadPatrullaje = 2.0;
    }

    public void patrullar() {
        // Acá irá la lógica para que camine de izquierda a derecha
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        // Aplicamos polimorfismo: le saca vida al héroe según el ataque de este bicho
        h.recibirDanio(this.getAtaque());
    }
}
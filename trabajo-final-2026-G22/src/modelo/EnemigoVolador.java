package modelo;
import java.awt.Color;

public class EnemigoVolador extends Enemigo {
    
    private double amplitudVuelo;

    public EnemigoVolador(double x, double y) {
        // Un poco más frágil pero molesto
        super("Enemigo Volador", x, y, 35, 35, Color.CYAN, 5, 1);
        this.amplitudVuelo = 50.0; // Píxeles que se mueve de su eje
    }

    public void volarEnCirculos() {
        //Falta para que gire
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        h.recibirDanio(this.getAtaque());
    }
}

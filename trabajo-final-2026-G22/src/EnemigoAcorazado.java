import java.awt.Color;

public class EnemigoAcorazado extends Enemigo {
    
    private boolean superArmadura;

    public EnemigoAcorazado(double x, double y) {
        // Grande, pesado y pega fuerte
        super("Acorazado", x, y, 50, 50, Color.DARK_GRAY, 30, 3);
        this.superArmadura = true;
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        h.recibirDanio(this.getAtaque());
        // Al tener super armadura, acá podriamos poner que el héroe rebote hacia atrás
    }
}
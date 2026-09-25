package modelo;
import java.awt.Color;

public class EnemigoAcorazado extends Enemigo {
    
    private final boolean superArmadura;

    // Patrulla, igual que EnemigoTerrestre, pero un poco más lento por ser pesado
    private final double velocidadPatrullaje;
    private final double limiteIzquierdo;
    private final double limiteDerecho;
    private boolean moviendoseDerecha; // esta SÍ cambia (va y vuelve), no puede ser final

    // Qué tan lejos empuja al héroe cada vez que lo golpea
    private static final double FUERZA_EMPUJE = 8.0;

    public EnemigoAcorazado(double x, double y) {
        // Grande, pesado y pega fuerte
        super("Acorazado", x, y, 50, 50, Color.DARK_GRAY, 30, 3);
        this.superArmadura = true;
        this.velocidadPatrullaje = 1.5;
        this.limiteIzquierdo = x - 80;
        this.limiteDerecho = x + 80;
        this.moviendoseDerecha = true;
    }

    public void patrullar() {
        if (moviendoseDerecha) {
            setX(getX() + velocidadPatrullaje);
            if (getX() >= limiteDerecho) {
                moviendoseDerecha = false;
            }
        } else {
            setX(getX() - velocidadPatrullaje);
            if (getX() <= limiteIzquierdo) {
                moviendoseDerecha = true;
            }
        }
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        h.recibirDanio(this.getAtaque());
      // Al tener super armadura, el héroe rebota hacia atrás al golpearlo.
        // Si el héroe está a la derecha del acorazado, lo empuja más hacia la derecha;
        // si está a la izquierda, lo empuja más hacia la izquierda.
        if (h.getX() >= getX()) {
            h.setX(h.getX() + FUERZA_EMPUJE);
        } else {
            h.setX(h.getX() - FUERZA_EMPUJE);
        }
    }

    @Override
    public void actualizar(Heroe heroe, Escenario escenario) {
        patrullar(); // Por ahora solo patrulla, no hace nada con el héroe ni con el escenario
    }
}
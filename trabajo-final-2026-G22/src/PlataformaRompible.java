import java.awt.Color;

public class PlataformaRompible extends Plataforma {
    
    private boolean destruida;

    public PlataformaRompible(double x, double y, int ancho, int alto) {
        // Color gris oscuro, y a esta le podemos poner solo 1 de HP para que se rompa fácil
        super("Plataforma Rompible", x, y, ancho, alto, Color.DARK_GRAY, 1, 0);
        
        this.destruida = false;
    }

    public void romper() {
        this.destruida = true;
        // Más adelante, el Escenario va a tener que revisar este booleano para borrarla de la lista
    }

    public boolean isDestruida() {
        return destruida;
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        // Frena la caída del héroe
        h.setVelocidadY(0); 
        
        // Al pisarla, llamamos al método para que se marque como destruida
        this.romper();
    }
}
package modelo;
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
        if (!this.isDestruida()){
            h.setVelocidadY(0); //Frena la caída del héroe para que no siga de largo
            h.setEnElSuelo(true); //Le avisa que esta pisando el suelo para que pueda saltar de nuevo
        }
        this.romper(); // La plataforma se rompe al primer contacto, sin importar si el héroe está en el suelo o no
    }

    @Override 
    public boolean debeEliminarse() {
        return this.isDestruida(); // Si está destruida, el escenario la va a eliminar de la lista
    }
}
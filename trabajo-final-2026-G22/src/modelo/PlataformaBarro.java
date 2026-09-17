package modelo;
import java.awt.Color;

public class PlataformaBarro extends Plataforma {
    
    private double reduccionVelocidad;

    public PlataformaBarro(double x, double y, int ancho, int alto) {
        // Le sacamos las letras al Color y le agregamos 100 de HP y 0 de ataque al final
        super("Plataforma de Barro", x, y, ancho, alto, new Color(101, 67, 33), 100, 0);
        
        // Limita la velocidad del jugador a un 40%
        this.reduccionVelocidad = 0.4;
    }

    public double getReduccionVelocidad() {
        return reduccionVelocidad;
    }

    // Cumplimos con el contrato de la clase padre
    @Override
    public void aplicarEfectoColision(Heroe h) {
        h.setVelocidadY(0); //Frena la caída del héroe para que no siga de largo
        h.setEnElSuelo(true); //Le avisa que esta pisando el suelo para que pueda saltar de nuevo 
        h.setVelocidadX(h.getVelocidadX()*0.5); //Reduce la velocidad del héroe a la mitad para simular que esta en barro
    }
}
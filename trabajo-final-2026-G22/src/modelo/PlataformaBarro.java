package modelo;

public class PlataformaBarro extends Plataforma {

    private double reduccionVelocidad;

    public PlataformaBarro(double x, double y, int ancho, int alto) {
        super("Plataforma de Barro", x, y, ancho, alto);
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

    @Override
    public TipoPlataforma getTipo(){
        return TipoPlataforma.BARRO;
    }
}
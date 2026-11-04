package modelo;

public class PlataformaBarro extends Plataforma {
    
    private double reduccionVelocidad;

    public PlataformaBarro(double x, double y, int ancho, int alto) {
        super("Plataforma de Barro", x, y, ancho, alto);
        // Limita la velocidad del jugador a la mitad
        this.reduccionVelocidad = 0.5;
    }

    // Cumplimos con el contrato de la clase padre
    @Override
     public void aplicarEfectoColision(Heroe h){
        super.aplicarEfectoColision(h); // aterriza como cualquier plataforma
        h.setVelocidadX(h.getVelocidadX()* reduccionVelocidad); // y ademas frena
     }
    
    @Override
    public TipoEntidad getTipo() {
        return TipoEntidad.PLATAFORMA_BARRO;
    }
}
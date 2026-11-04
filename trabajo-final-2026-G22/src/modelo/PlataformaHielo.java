package modelo;

public class PlataformaHielo extends Plataforma {
    
    private static final double VELOCIDAD_RESBALON = 2.0; //patina mas lento que caminar

    public PlataformaHielo(double x, double y, int ancho, int alto) {
        super("Plataforma de Hielo", x, y, ancho, alto);
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        super.aplicarEfectoColision(h);
        
        // Efecto resbalón: Si el jugador soltó la tecla y su velocidad horizontal es 0
        if (h.getVelocidadX() == 0) { 
            if (h.isMirandoDerecha()) {
                h.setVelocidadX(VELOCIDAD_RESBALON); // Patina hacia la derecha (más lento que caminar que es 4)
            } else {
                h.setVelocidadX(-VELOCIDAD_RESBALON); // Patina hacia la izquierda
            }
        }
    }
    @Override
    public TipoEntidad getTipo() {
        return TipoEntidad.PLATAFORMA_HIELO;
    }
}
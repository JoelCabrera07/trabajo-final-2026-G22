package modelo;

public class PlataformaRompible extends Plataforma {
    
    private boolean destruida;

    public PlataformaRompible(double x, double y, int ancho, int alto) {
        super("Plataforma Rompible", x, y, ancho, alto);
        
        this.destruida = false;
    }

    public void romper() {
        this.destruida = true;
    }

    public boolean isDestruida() {
        return destruida;
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        if (!this.isDestruida()){
            super.aplicarEfectoColision(h); //solose puede aterrizar si todavia esta entera
        }
        romper(); // La plataforma se rompe al primer contacto, sin importar si el héroe está en el suelo o no
    }

    @Override 
    public boolean debeEliminarse() {
        return this.isDestruida(); // Si está destruida, el escenario la va a eliminar de la lista
    }
    @Override
    public TipoEntidad getTipo() {
        return TipoEntidad.PLATAFORMA_ROMPIBLE;
    }

    @Override
    public void alGolpearDesdeAbajo(Heroe h){
        super.alGolpearDesdeAbajo(h); //frena al heroe como cualquier plataforma
        romper();                     //y ademas se rompe
    }
}
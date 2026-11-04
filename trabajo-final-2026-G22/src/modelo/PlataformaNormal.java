package modelo;

public class PlataformaNormal extends Plataforma {

    public PlataformaNormal(double x, double y, int alto, int ancho) {
        super("Plataforma Normal", x, y, alto, ancho);
    }
   
    
    @Override
    public TipoEntidad getTipo() {
        return TipoEntidad.PLATAFORMA_NORMAL;
    }
} 
    


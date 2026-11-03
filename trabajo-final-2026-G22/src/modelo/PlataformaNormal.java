package modelo;
import java.awt.Color;

public class PlataformaNormal extends Plataforma {

    public PlataformaNormal(double x, double y, int alto, int ancho) {
        super("Plataforma Normal", x, y, alto, ancho, Color.GRAY, 1, 0);
    }
   
    
    @Override
    public TipoEntidad getTipo() {
        return TipoEntidad.PLATAFORMA_NORMAL;
    }
} 
    


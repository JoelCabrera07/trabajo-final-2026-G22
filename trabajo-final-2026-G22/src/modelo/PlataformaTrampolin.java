package modelo;

public class PlataformaTrampolin extends Plataforma {
    
    private double multiplicadorRebote;

    public PlataformaTrampolin(double x, double y, int ancho, int alto) {
        super("Trampolín", x, y, ancho, alto);
        // Multiplica la fuerza del salto por 1.5
        this.multiplicadorRebote = 1.5;
    }

    //No llama a super: el trampolin hace rebotar
    @Override
    public void aplicarEfectoColision(Heroe h) {
        // le mandamos una velocidad negativa (hacia arriba) para que rebote, en java para que vaya hacia arriba la velocidad es negativa
        h.setVelocidadY(-15.0 * this.multiplicadorRebote); // Ajusta el valor según la física del juego
        h.setEnElSuelo(false); // Le avisamos que ya no está en el suelo para que no pueda saltar de nuevo hasta tocar otra plataforma
    }
    @Override
    public TipoEntidad getTipo() {
        return TipoEntidad.PLATAFORMA_TRAMPOLIN;
    }
}
package modelo;

public class PlataformaRompible extends Plataforma {

    private boolean destruida;

    public PlataformaRompible(double x, double y, int ancho, int alto) {
        super("Plataforma Rompible", x, y, ancho, alto);
        this.destruida = false;
    }

    public void romper() {
        this.destruida = true;
        // Solo marca el estado, JuegoManager se encargará de eliminarla del escenario en la siguiente actualización
    }



    @Override
    public void aplicarEfectoColision(Heroe h) {
        if (!this.destruida){
            h.setVelocidadY(0); //Frena la caída del héroe para que no siga de largo
            h.setEnElSuelo(true); //Le avisa que esta pisando el suelo para que pueda saltar de nuevo
        }
        this.romper(); // La plataforma se rompe al primer contacto, sin importar si el héroe está en el suelo o no
    }

    @Override
    public boolean debeEliminarse() {
        return this.destruida; // Juegomanager usa esto para sacarla de la lista de plataformas del escenario.
    }

    @Override
    public void golpeadaDesdeAbajo() {
        // Si el héroe golpea la plataforma desde abajo, se rompe
        this.romper();
    }

    @Override
    public TipoPlataforma getTipo(){
        return TipoPlataforma.ROMPIBLE;
    }
}
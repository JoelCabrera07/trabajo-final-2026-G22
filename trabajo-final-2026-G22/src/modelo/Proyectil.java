package modelo;

public class Proyectil extends EntidadHostil {

    private double velocidadX;
    private double velocidadY;
    private boolean impacto = false;

    public Proyectil(double x, double y, double dirX, double dirY, int ataque) {
        super("Proyectil", x, y, 10, 10, ataque);

        double magnitud = Math.sqrt(dirX * dirX + dirY * dirY);
        if (magnitud == 0) magnitud = 1;

        double velocidad = 6.0;
        this.velocidadX = (dirX / magnitud) * velocidad;
        this.velocidadY = (dirY / magnitud) * velocidad;
    }

    public void actualizar() {
        setX(getX() + velocidadX);
        setY(getY() + velocidadY);
    }
    
    @Override
    public TipoEntidad getTipo() {
        return TipoEntidad.PROYECTIL;
    }

    //Lastima como cualquier hostil y ademas se consume
    @Override
    public void aplicarEfectoColision(Heroe h){
        super.aplicarEfectoColision(h);
        impacto = true;
    }

    //Se elimina si ya pego o si salio de la pantalla
    public boolean debeEliminarse(){
        return impacto || getX()< -100 || getX() > 900 || getY()< -100 || getY()> 700;
    }
    public double getVelocidadX() { return velocidadX; }
    public double getVelocidadY() { return velocidadY; }
}

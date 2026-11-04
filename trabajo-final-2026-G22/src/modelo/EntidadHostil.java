package modelo;

//Todo lo que lastima al heroe cuando lo toca: enemigos y proyectiles
public abstract class EntidadHostil extends Entidad implements Colisionable {
    private final int ataque;

    public EntidadHostil(String nombre, double x, double y,int ancho, int alto,int ataque){
        super(nombre,x,y,ancho,alto);
        this.ataque = ataque;
    }
    
    public int getAtaque(){
        return ataque;
    }
    //Por defecto, tocarla le saca vida al heroe segun su ataque
    @Override
    public void aplicarEfectoColision (Heroe h){
        h.recibirDanio(getAtaque());
    }
}

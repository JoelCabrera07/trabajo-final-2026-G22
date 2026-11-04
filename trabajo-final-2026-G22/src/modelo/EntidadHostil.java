package modelo;

import java.awt.Color;

//Todo lo que lastima al heroe cuando lo toca: enemigos y proyectiles
public abstract class EntidadHostil extends Entidad implements Colisionable {
    public EntidadHostil(String nombre, double x, double y,int ancho, int alto, Color color, int hp,int ataque){
        super(nombre,x,y,ancho,alto,color,hp,ataque);
    }
    
    //Por defecto, tocarla le saca vida al heroe segun su ataque
    @Override
    public void aplicarEfectoColision (Heroe h){
        h.recibirDanio(getAtaque());
    }
}

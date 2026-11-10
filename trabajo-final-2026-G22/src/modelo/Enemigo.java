package modelo;

public abstract class Enemigo extends Entidad implements Colisionable {
    private final int ataque;

    public Enemigo(String nombre, double x, double y,int ancho, int alto, int ataque) {
        super(nombre, x, y, ancho, alto);
        this.ataque = ataque;
    }
    
    public abstract void actualizar(Heroe heroe, Escenario escenario); // Cada tipo de enemigo tiene su propia logica de movimiento y ataque, por eso es abstracta

    public boolean debeDibujarse(){//solo acechador lo va a redefinir, los demas enemigos se dibujan siempre
        return true;
    }

    public int getAtaque(){
        return ataque;
    }

    public abstract TipoEnemigo getTipo();

    public boolean mirandoDerecha() { 
        return true; 
    }

    public FaseEnemigo getFase() { 
        return FaseEnemigo.NORMAL;//por defecto todo estan en su fase normal 
    }

    public long getMsEnFase() { 
        return 0; 
    }

    @Override 
    public void aplicarEfectoColision(Heroe h){
        // Aplicamos polimorfismo: le saca vida al héroe según el ataque de x enemigo
        h.recibirDanio(this.getAtaque());     
    }
}
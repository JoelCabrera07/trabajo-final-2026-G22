package modelo;

public abstract class Enemigo extends EntidadHostil{
    public Enemigo(String nombre, double x, double y,int ancho, int alto, int ataque) {
        super(nombre, x, y, ancho, alto,ataque);
    }
    
    public abstract void actualizar(Heroe heroe, Escenario escenario); // Cada tipo de enemigo tiene su propia logica de movimiento y ataque, por eso es abstracta

    public boolean isVisible(){
        return true;
    }
    
    // Hacia dónde mira el enemigo. Por defecto a la derecha; cada uno lo redefine según su lógica
    public boolean isMirandoDerecha() {
        return true;
    }

    // En qué situación está el enemigo. Por defecto NORMAL; los que tienen estados lo redefinen
    public EstadoEnemigo getEstado() {
        return EstadoEnemigo.NORMAL;
    }
    // Cuántos frames lleva en su estado actual. Por defecto 0; los que tienen estados lo redefinen
    public int getTicksEnEstado() {
        return 0;
    }
}
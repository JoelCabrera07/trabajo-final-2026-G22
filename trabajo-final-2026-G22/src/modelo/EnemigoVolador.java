package modelo;

import java.awt.Color;

public class EnemigoVolador extends Enemigo {

    private double amplitudVuelo;

    // el centro vertical del cual se mueve,X se mantiene fijo, no hace falta
    // guardarlo
    private double centroY;

    // Angulo actual del movimiento (en radianes)
    private double angulo;

    // Que tan rapido se mueve
    private static final double VELOCIDAD_ANGULAR = 0.03;
    private boolean mirandoDerecha = true; // mira hacia el héroe

    public EnemigoVolador(double x, double y) {
        // Un poco más frágil pero molesto (ajustamos a 35x30 para mejor proporción
        // visual)
        super("Enemigo Volador", x, y, 35, 30, Color.CYAN, 5, 1);
        this.amplitudVuelo = 50.0; // Píxeles que se mueve de su eje
        this.centroY = y;
        this.angulo = 0;
    }

    public void flotarVerticalmente() {
        // Avanza el ángulo en cada frame
        this.angulo += VELOCIDAD_ANGULAR;
        if (angulo >= 2 * Math.PI) {
            angulo -= 2 * Math.PI; // para que no crezca indefinidamente
        }
        // Calcula la nueva posición vertical usando una función seno
        double nuevoY = centroY + amplitudVuelo * Math.sin(angulo);
        this.setY(nuevoY);
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        h.recibirDanio(this.getAtaque());
    }

    @Override
    public void actualizar(Heroe heroe, Escenario escenario) {
        mirandoDerecha = heroe.getX() >= getX(); // si el héroe está a la derecha, mira a la derecha
        flotarVerticalmente();
    }

    @Override
    public TipoEntidad getTipo() {
        return TipoEntidad.ENEMIGO_VOLADOR;
    }

    @Override
    public boolean isMirandoDerecha() {
        return mirandoDerecha;
    }
}

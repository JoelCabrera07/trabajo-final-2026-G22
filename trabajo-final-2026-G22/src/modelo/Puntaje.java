package modelo;

public class Puntaje {
    private String jugador;
    private int puntos;

    public Puntaje (String jugador,int puntos){
        this.jugador = jugador;
        this.puntos = puntos;
    }
    public String getJugador() {return jugador;}
    public int getPuntos() {return puntos;}
}

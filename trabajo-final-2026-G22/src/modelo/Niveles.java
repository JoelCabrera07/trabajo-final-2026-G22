package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Los niveles de la torre, en orden. Cada uno es solo la descripcion de lo que
 * contiene; para agregar una pantalla alcanza con sumar un Nivel a la lista.
 */
public class Niveles {

    private static final List<Nivel> NIVELES = new ArrayList<>();

    static {
        // Pantalla 1: Planta baja
        // Calentamiento: saltos medianos, el primer hielo y una salida que pide carga fuerte.
        NIVELES.add(new Nivel()
                // Piso de la planta baja (aca arranca el heroe)
                .plataforma(TipoPlataforma.NORMAL, 0, 550, 800, 50)
                .plataforma(TipoPlataforma.NORMAL, 150, 440, 90, 20)
                // Barro: caminar cuesta
                .plataforma(TipoPlataforma.BARRO, 330, 290, 80, 20)
                // Hielo: te resbalas mientras cargas el salto
                .plataforma(TipoPlataforma.HIELO, 520, 260, 100, 20)
                // Cornisa de salida: desde aca se salta a la pantalla 2
                .plataforma(TipoPlataforma.NORMAL, 700, 130, 70, 20)
                // Saliente de la torreta
                .plataforma(TipoPlataforma.NORMAL, 20, 300, 70, 20)
                // Patrulla la primera plataforma
                .enemigo(TipoEnemigo.TERRESTRE, 160, 390)
                // Empuja al heroe por el piso
                .enemigo(TipoEnemigo.ACORAZADO, 470, 500)
                // Cruza el hueco entre el barro y el hielo
                .enemigo(TipoEnemigo.VOLADOR, 455, 250)
                // Dispara a todo el salon
                .enemigo(TipoEnemigo.TORRETA, 45, 260));

        // Pantalla 2: Salon de los estandartes
        // El hielo de la izquierda te empuja hacia el vacio y la plataforma movil hay que agarrarla a tiempo,
        // sin quedarse quieto mucho porque aparece el acechador.
        NIVELES.add(new Nivel()
                // Llegada desde la pantalla 1
                .plataforma(TipoPlataforma.NORMAL, 520, 470, 110, 20)
                .plataforma(TipoPlataforma.NORMAL, 280, 420, 70, 20)
                // Hielo contra la pared: si no saltas rapido te caes
                .plataforma(TipoPlataforma.HIELO, 60, 300, 110, 20)
                // Plataforma movil
                .plataforma(TipoPlataforma.MOVIL, 200, 170, 90, 20, 160, 400)
                // Salida a la pantalla 3
                .plataforma(TipoPlataforma.NORMAL, 680, 110, 60, 20)
                // Saliente de la torreta
                .plataforma(TipoPlataforma.NORMAL, 700, 330, 60, 20)
                .enemigo(TipoEnemigo.TORRETA, 720, 290)
                .enemigo(TipoEnemigo.VOLADOR, 520, 250)
                // Castiga al que espera mucho a la plataforma movil
                .enemigo(TipoEnemigo.ACECHADOR, 0, 0, 1.2));

        // Pantalla 3: Muro derrumbado
        // Barro, hielo y un techo rompible sobre la salida: el primer salto lo rompe y el segundo pasa.
        NIVELES.add(new Nivel()
                // Llegada desde la pantalla 2
                .plataforma(TipoPlataforma.NORMAL, 520, 440, 110, 20)
                // Barro: caminar hasta el borde cuesta
                .plataforma(TipoPlataforma.BARRO, 700, 330, 90, 20)
                .plataforma(TipoPlataforma.NORMAL, 600, 190, 60, 20)
                // Hielo de la salida
                .plataforma(TipoPlataforma.HIELO, 270, 135, 100, 20)
                // Techo rompible: hay que romperlo de un cabezazo para poder salir
                .plataforma(TipoPlataforma.ROMPIBLE, 210, 20, 200, 20)
                // Cuida la llegada
                .enemigo(TipoEnemigo.TERRESTRE, 540, 390)
                // Molesta en el salto del barro a la plataforma alta
                .enemigo(TipoEnemigo.VOLADOR, 520, 250)
                .enemigo(TipoEnemigo.ACECHADOR, 0, 0, 1.0)
                .enemigo(TipoEnemigo.ACECHADOR, 0, 0, 1.6));

        // Pantalla 4: Balcon de la tormenta
        // Hay que caer en el trampolin y dirigir el rebote hasta la cornisa de la derecha,
        // despues de romper el techo que lo tapa.
        NIVELES.add(new Nivel()
                // Llegada desde la pantalla 3
                .plataforma(TipoPlataforma.NORMAL, 480, 470, 110, 20)
                // Trampolin: rebota solo
                .plataforma(TipoPlataforma.TRAMPOLIN, 300, 540, 70, 20)
                // Techo rompible sobre el trampolin
                .plataforma(TipoPlataforma.ROMPIBLE, 260, 250, 140, 20)
                // Salida a la pantalla 5
                .plataforma(TipoPlataforma.NORMAL, 80, 150, 60, 20)
                // Saliente de la torreta
                .plataforma(TipoPlataforma.NORMAL, 660, 300, 60, 20)
                .enemigo(TipoEnemigo.TORRETA, 680, 260)
                // Vuelan en la columna del rebote
                .enemigo(TipoEnemigo.VOLADOR, 250, 380)
                .enemigo(TipoEnemigo.VOLADOR, 400, 200));

        // Pantalla 5: La aguja de hierro
        // Dos plataformas moviles seguidas, fuego cruzado de dos torretas y voladores en el camino.
        NIVELES.add(new Nivel()
                // Llegada desde la pantalla 4
                .plataforma(TipoPlataforma.NORMAL, 180, 470, 100, 20)
                .plataforma(TipoPlataforma.MOVIL, 250, 390, 80, 20, 120, 380)
                .plataforma(TipoPlataforma.MOVIL, 450, 280, 70, 20, 400, 640)
                // Hielo contra la pared
                .plataforma(TipoPlataforma.HIELO, 700, 200, 90, 20)
                // Salida a la cima
                .plataforma(TipoPlataforma.NORMAL, 430, 165, 60, 20)
                // Saliente de torreta
                .plataforma(TipoPlataforma.NORMAL, 20, 250, 60, 20)
                .enemigo(TipoEnemigo.TORRETA, 40, 210)
                .enemigo(TipoEnemigo.TORRETA, 730, 160)
                .enemigo(TipoEnemigo.VOLADOR, 360, 300)
                .enemigo(TipoEnemigo.VOLADOR, 600, 180)
                .enemigo(TipoEnemigo.ACECHADOR, 0, 0, 1.4));

        // Pantalla 6: La cima
        // La puerta esta en la punta derecha de la cima, custodiada por un acorazado que empuja al vacio.
        // Hay que saltarlo justo cuando se acerca (y sin esperar mucho, que aparecen los acechadores).
        NIVELES.add(new Nivel()
                // Llegada desde la pantalla 5
                .plataforma(TipoPlataforma.NORMAL, 300, 470, 120, 20)
                .plataforma(TipoPlataforma.NORMAL, 90, 400, 80, 20)
                // Hielo: hay que saltar enseguida
                .plataforma(TipoPlataforma.HIELO, 220, 300, 80, 20)
                // Plataforma movil
                .plataforma(TipoPlataforma.MOVIL, 90, 190, 80, 20, 40, 170)
                // Cima: la puerta de la meta esta en la punta derecha
                .plataforma(TipoPlataforma.NORMAL, 540, 170, 260, 20)
                // Saliente de la torreta
                .plataforma(TipoPlataforma.NORMAL, 20, 280, 50, 20)
                // Custodia la puerta: empuja al heroe hacia el vacio (hay que saltarlo)
                .enemigo(TipoEnemigo.ACORAZADO, 615, 120)
                .enemigo(TipoEnemigo.TORRETA, 35, 240)
                .enemigo(TipoEnemigo.ACECHADOR, 0, 0, 1.3)
                .enemigo(TipoEnemigo.ACECHADOR, 0, 0, 1.8)
                // La meta: la puerta en lo mas alto de la torre
                .puerta(745, 110));

        // Pantalla 7: el cielo
        // Esta arriba de la cima (pantalla 6) y no tiene plataformas ni enemigos: es solo el
        // espacio extra para completar el arco del salto y volver a caer sobre la plataforma
        // de la puerta. No tiene puerta.
        NIVELES.add(new Nivel());
    }

    private Niveles() {
    }

    // Las pantallas van de 1 a total()
    public static Nivel get(int pantalla) {
        if (pantalla < 1 || pantalla > NIVELES.size()) {
            throw new IllegalArgumentException("No existe la pantalla " + pantalla);
        }
        return NIVELES.get(pantalla - 1);
    }

    public static int total() {
        return NIVELES.size();
    }
}
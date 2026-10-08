package modelo;

import java.util.List; // Importa la clase List para poder usar listas en el código

public interface FabricaNivel {
    List<Enemigo> crearEnemigos(); // Método para crear enemigos en el nivel
    List<Plataforma> crearPlataformas(); // Método para crear plataformas en el nivel
    
}

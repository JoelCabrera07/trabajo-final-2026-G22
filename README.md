# Proyecto: Tower Defense - El Último Bastión

## 1. Integrantes del Equipo 

- Apellido, Nombre 
- Apellido, Nombre 
- Apellido, Nombre 
- Apellido, Nombre

## 2. Dominio y Alcance del Sistema 

### Descripción del Problema
Se busca desarrollar una aplicación de escritorio del clásico videojuego **Tower Defense**. El jugador debe utilizar recursos limitados para construir torres defensivas en un mapa con un camino predefinido. Oleadas de enemigos avanzarán por este camino, y el objetivo es destruir a todos los enemigos antes de que lleguen al final del recorrido.

### Objetivo del Sistema
El sistema será un juego funcional y extensible que permitirá al jugador experimentar las mecánicas básicas del género. El diseño debe ser modular para facilitar la adición de nuevos tipos de torres, enemigos o mapas en el futuro, aplicando rigurosamente los conceptos del paradigma orientado a objetos.

### Funcionalidades Principales (Features)
- **Gestión de Torres:**
    - El jugador puede seleccionar distintos tipos de torres desde un panel.
    - El jugador puede posicionar las torres en ubicaciones válidas del mapa.
    - Cada torre tiene un costo, un rango de ataque y un daño específico.
- **Sistema de Oleadas de Enemigos:**
    - Los enemigos aparecen en oleadas de dificultad creciente.
    - Existen diferentes tipos de enemigos (ej: rápidos pero débiles, lentos pero resistentes).
    - Los enemigos siguen un camino preestablecido en el mapa.
- **Mecánicas de Juego:**
    - El jugador cuenta con una cantidad inicial de "recursos" (oro) para construir torres.
    - Destruir enemigos otorga recursos adicionales.
    - El jugador tiene un número limitado de "vidas", que disminuyen si un enemigo llega al final del camino. El juego termina si las vidas llegan a cero.
- **Interfaz Gráfica (IGU):**
    - Visualización del mapa, el camino, las torres y los enemigos.
    - Panel de control para seleccionar torres, ver recursos, vidas y número de oleada.
    - Botón para iniciar la siguiente oleada.
- **Persistencia:**
    - Sistema de guardado y carga de los puntajes más altos (High Scores) en una base de datos.

## 3. Arquitectura y Diseño 

### Patrón de Diseño Adicional: Factory Method
- **Nombre del Patrón:** **Factory Method (Método de Fábrica)**.
- **Justificación:** Se utilizará este patrón para la creación de los objetos `Enemigo`. Tendremos una clase abstracta `EnemigoFactory` con un método `crearEnemigo()`. Se crearán subclases concretas como `OrcoFactory` y `GoblinFactory` que implementarán este método para instanciar los enemigos correspondientes. Esto **desacopla** la lógica del juego (que solo necesita pedir un enemigo de un tipo) de la lógica de instanciación de cada enemigo concreto. Así, añadir un nuevo tipo de enemigo (ej. `DragonFactory`) no requerirá modificar el código que gestiona las oleadas.

### Diagramas de Diseño

#### **Diagrama de Clases UML (Conceptual)**


#### **Prototipo de la IGU (Wireframe)**

## 4. Stack Tecnológico 

- **Lenguaje:** Java 17
- **IDE:** Visual Studio Code
- **Base de Datos:** MySQL 8.0 (para persistencia de High Scores)
- **Framework de IGU:** Java Swing
- **Control de Versiones:** Git y GitHub Classroom

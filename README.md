# Proyecto: Jumping Hero

## 1. Integrantes del Equipo 

- Cabrera, Joel. 
- Hidalgo,Lautaro.
- Millar,Juan Cruz.
- Valentino,Ernesto Paez.

## 2.Dominio y Alcance del Sistema.

###Descripcion del problema

Buscamos desarrollar una aplicacion de escritorio del juego Jump king. El jugador debera controlar al personaje usando el salto como su principal movimiento,subiendo las plataformas,esquivando enemigos y calculando los saltos ya que entre mas tiempo tenga pulsado el boton de salto la distancia sera mayor para llegar a la cima.

### Objetivo del Sistema.

El sistema sera un videojuego funcional desarrollado bajo el paradigma de Programa Orientado a Objetos(POO).Su diseño debe ser modular, asi se facilitara futuras incorporaciones de nuevos tipo de plataformas (ej: resbaladizas, rompibles,etc),comportamiento de enemigos y escenarios.Tambien se dara mucha importancia a la correcta implementacion de fisicas basicas(gravedad,calculo de trayectorias) y en la deteccion de colisones en tiempo real entre las distintas entidades del juego(Jugador - Enemigo-Escenario).

### Funcionalidades principales (Features).

- **Mecanicas del Jugador y Fisicas: **
-El jugador puede moverse horizontalmente.
-El sistema de salto sera dinamico, ya que la altura y distancia de la parabola se calcula en base al tiempo que se mantenga pulsado el boton de salto.
-Se aplica constante de gravedad y mecanicas de caida libre.

- **Entorno y Colisiones**
-Posicionamiento estatico de plataformas para crear  un mapa de progresion vertical.
-Sistema de deteccion de colisiones(hitboxes) preciso entre el jugador,los limites de la pantalla y las superficies de las plataformas.

- **Sistema de Obstaculos y Enemigos: **
-Generacion de entidades hostiles en el mapa con patrones de movimiento simple.
-Deteccion de colisiones con enemigos que penalicen al jugador (ej: interrumpiendo el salto, empujandolo hacia abajo o quitando vida).

-**Interfaz Grafica(IGU):**
-Renderizado visualdel personaje , los enemigos, las plataformas y el entorno.
-Indicador visual en pantalla (barra de potencia) que muestre la fuerza de carga del salto en tiempo real.

- **Condiciones de Juego: **
-Ausencia de Game over tradicional: el castigo por fallar es la perdida de progreso al caer o poner Game Over si aplicamos vidas.
-Condicion de victoria al alcanzar la plataforma mas alta del nivel.

- **Persistencia**
-Sistema de guardado y carga del estado de la pantalla.
-Almacenamiento local de los datos del jugador(como las coordenadas exactas del personaje) para permitir retomar el ascenso en sesiones futuras


## 3. Arquitectura y Diseño

## Entrega: Aplicación de Patrones de Diseño
### 1. El problema: un enfoque con errores de diseño
Inicialmente, la clase "Escenario" era la responsable directa de instanciar cada plataforma y cada enemigo de todas las pantallas del juego mediante una estructura condicional basada en el número de pantalla:
#### Escenario.java (Versión anterior con errores de diseño)
<img width="1001" height="663" alt="Captura" src="https://github.com/user-attachments/assets/b097698b-c3e4-4c94-ae44-54728d30ba94" />
<img width="1008" height="266" alt="image" src="https://github.com/user-attachments/assets/e6d14b89-6676-4ee6-bca2-8866f4b4b7c4" />
Si sumamos más enemigos y plataformas, este bloque crece sin control...

**Problemas de este enfoque:**


-**Violación del Principio Abierto/Cerrado(OCP)**:Cada vez que se desea incorporar una nueva pantalla (Nivel 3, Nivel 4, etc.), es obligatorio abrir y modificar la clase Escenario, alterando código existente y arriesgando la estabilidad de los niveles previos.

-**Violación del Principio de Responsabilidad Única(SRP)**: La clase Escenario asumía dos responsabilidades no relacionadas: gestionar las entidades activas durante la ejecución del juego y, al mismo tiempo, actuar como ensamblador artesanal de cada nivel.

-**Alto acoplamiento**: Escenario dependía directamente de todas y cada una de las clases concretas de plataformas (PlataformaNormal, PlataformaBarro, etc.) y de enemigos (EnemigoTerrestre, EnemigoVolador, EnemigoAcorazado, EnemigoTorreta).

-**Con 10 o 20 niveles y decenas de elementos por nivel, la clase Escenario se convertía en un archivo inmanejable de cientos de líneas dedicadas puramente a la instanciación de objetos.**

2. **Patrón aplicado: Abstract Factory (Fábrica Abstracta)**

Para resolver este problema se implementó el patrón Abstract Factory. Se delegó la creación de las familias de elementos de cada pantalla (plataformas y enemigos) a fábricas especializadas e intercambiables, desacoplando completamente a Escenario de las clases concretas.


Diagrama de clases
<img width="669" height="885" alt="mermaid-diagram" src="https://github.com/user-attachments/assets/a2dc8943-fea0-46ad-a191-9bac4064eea8" />

Implementación
FabricaNivel.java (Interfaz de la Fábrica)
<img width="999" height="197" alt="image" src="https://github.com/user-attachments/assets/8e343037-d82f-4067-9720-721e39ce9d91" />

FabricaNivel1.java (Fábrica de la Pantalla 1)
<img width="1009" height="659" alt="image" src="https://github.com/user-attachments/assets/3aa7d81b-24e8-4941-acf0-1bf8af4a8f90" />
<img width="1005" height="276" alt="image" src="https://github.com/user-attachments/assets/28117a22-4d6a-4bd2-9d8c-ce45cd8c8b5b" />

FabricaNivel2.java (Fábrica de la Pantalla 2)
<img width="1005" height="658" alt="image" src="https://github.com/user-attachments/assets/d03da90e-f697-4f5c-919d-f76ee1c40fbb" />

Escenario.java (Cliente actualizado)
<img width="1004" height="285" alt="image" src="https://github.com/user-attachments/assets/483b9520-d5ee-4492-b9df-a7c64da9f754" />

Con este diseño tenemos beneficios:

-**Extensibilidad**: Si queremos agregar un Nivel 3, solo creamos la clase FabricaNivel3 implements FabricaNivel. No se toca ni una sola línea de código existente en Escenario.

-**Cohesión y legibilidad**: Cada nivel administra sus propias coordenadas y composición de manera aislada y ordenada.

-**Desacoplamiento*: Escenario solo conoce las abstracciones FabricaNivel, Plataforma y Enemigo. No conoce constructores ni implementaciones concretas.

### Diagramas de Diseño

#### **Diagrama de Clases UML (Conceptual)**
```mermaid
classDiagram
    class JuegoManager {
        <<Singleton>>
        -JuegoManager instance
        -Escenario escenarioActual
        -Heroe heroe
        +getInstance() JuegoManager
        +iniciarJuego() void
        +buclePrincipal() void
    }

    class Escenario {
        -List~Plataforma~ plataformas
        -List~Enemigo~ enemigos
        -List~Entidad~ entidadesInteractuables
        +cargarMapa() void
    }

    class Entidad {
        <<abstract>>
        -String nombre
        -double x
        -double y
        -int alto
        -int ancho
        -int hp
        +getHitbox() Rectangle
        +recibirDanio(int cantidad) void
    }

    class Heroe {
        -double velocidadY
        -double velocidadX
        -double cargaSaltoActual
        +cargarSalto(long tiempoPulsado) void
        +soltarSalto() void
        +predecirTrayectoria() List~Point~
    }

    class Cofre {
        -boolean abierto
        +abrir(Heroe h) void
    }

    class Booster {
        <<abstract>>
        +aplicarEfecto(Heroe h) void
    }

    class BoosterImpulso {
        +aplicarEfecto(Heroe h) void
    }

    class Plataforma {
        <<abstract>>
        +aplicarEfectoColision(Heroe h) void
    }

    class PlataformaBarro {
        -double reduccionVelocidad
        +aplicarEfectoColision(Heroe h) void
    }

    class PlataformaHielo {
        -double nivelFriccion
        +aplicarEfectoColision(Heroe h) void
    }

    class PlataformaTrampolin {
        -double multiplicadorRebote
        +aplicarEfectoColision(Heroe h) void
    }

    class PlataformaRompible {
        -boolean destruida
        +romper() void
    }

    class PlataformaMovil {
        -double velocidadX
        -double limiteIzquierdo
        -double limiteDerecho
        -boolean moviendoDerecha
        +mover() void
        +aplicarEfectoColision(Heroe h) void
    }

    class Enemigo {
        <<abstract>>
        +aplicarEfectoColision(Heroe h) void
    }

    class EnemigoTerrestre {
        -double velocidadPatrullaje
        +patrullar() void
    }

    class EnemigoVolador {
        -double amplitudVuelo
        +volarEnCirculos() void
    }

    class EnemigoTorreta {
        -int cadenciaDisparo
        +disparar() void
    }

    class EnemigoAcorazado {
        -boolean superArmadura
    }

    class EnemigoAcechador {
        -int tiempoDeteccion
        +perseguir(Heroe h) void
    }

    class PartidaDAO {
        <<interface>>
        +guardarPartida(Heroe h, Escenario e) boolean
    }
    
    class PartidaDAOMySQL {
        +guardarPartida(Heroe h, Escenario e) boolean
    }

    %% Relaciones de uso y composición
    JuegoManager --> "1" Escenario : gestiona
    JuegoManager --> "1" Heroe : controla
    JuegoManager ..> PartidaDAO : usa para base de datos
    PartidaDAO <|.. PartidaDAOMySQL
    Escenario --> "*" Plataforma : contiene
    Escenario --> "*" Enemigo : contiene
    Escenario --> "*" Entidad : contiene cofres
    Cofre ..> Booster : suelta

    %% Relaciones de Herencia Base
    Entidad <|-- Heroe
    Entidad <|-- Cofre
    Entidad <|-- Plataforma
    Entidad <|-- Enemigo
    Booster <|-- BoosterImpulso
    
    %% Herencia de Plataformas
    Plataforma <|-- PlataformaBarro
    Plataforma <|-- PlataformaHielo
    Plataforma <|-- PlataformaTrampolin
    Plataforma <|-- PlataformaRompible
    Plataforma <|-- PlataformaMovil

    %% Herencia de Enemigos
    Enemigo <|-- EnemigoTerrestre
    Enemigo <|-- EnemigoVolador
    Enemigo <|-- EnemigoTorreta
    Enemigo <|-- EnemigoAcorazado
    Enemigo <|-- EnemigoAcechador
```

#### **Prototipo de la IGU (Wireframe)**

<img width="590" height="336" alt="91e7c1b2-f1e7-46ec-9d11-460316710a72" src="https://github.com/user-attachments/assets/df5ce34c-46ac-40f3-a9f9-c1df09deb125" />
<img width="1920" height="1440" alt="thumb-1920-1348538" src="https://github.com/user-attachments/assets/fd7d6031-8971-43e2-adfe-e0671501f65d" />


## 4. Stack Tecnológico

- **Lenguaje:** Java (Versión 17 o superior)
- **IDE:** Eclipse IDE
- **Persistencia de Datos:** Sistema de Archivos / Persistencia basada en Archivos Locales (para el guardado de la sesión y coordenadas).
- **Framework de IGU:** Java Swing 
- **Control de Versiones:** Git y GitHub

## 5. Herencia y Polimorfismo(Segunda Entrega)

1. Herencia (Para no repetir código)
Básicamente aplicamos herencia simple para no escribir mil veces los mismos atributos y mantener la arquitectura ordenada.

Sistema de Entidades: En vez de programarle la vida, el nombre y las coordenadas al Héroe y después hacer lo mismo de cero para el Enemigo, creamos una clase abstracta padre llamada Entidad. De ahí heredan Heroe y Enemigo, llevándose toda esa base y sumando solo sus mecánicas específicas.

Plataformas: Hicimos la misma jugada. Tenemos una clase base Plataforma que se encarga de lo genérico (dibujarse en pantalla, detectar colisiones). De ahí sacamos las clases hijas como PlataformaHielo o PlataformaBarro, que heredan toda la física base pero modifican cosas puntuales como la fricción o la velocidad del jugador.

2. Polimorfismo
Lo implementamos más que nada en el sistema de interacciones y colisiones para mantener el motor del juego limpio.
En lugar de armar una cadena enorme de if/else preguntando con qué tipo de bloque o entidad chocó el jugador, usamos un método general. Gracias a la sobrescritura en las clases hijas, cuando ocurre una colisión, es el propio objeto el que define qué efecto aplicar (por ejemplo, alterar la velocidad si es hielo ). De esta forma, el bucle principal del juego solo llama a la acción genérica, y si el día de mañana agregamos un obstáculo nuevo, no necesitamos modificar la lógica del motor, solo creamos la clase nueva.

package modelo;

// En que parte de su ciclo esta un enemigo (la vista lo usa para elegir el dibujo)
public enum FaseEnemigo {
    NORMAL,                                               // enemigos sin ciclo (terrestre, volador, acorazado)
    REPOSO, CARGANDO, DISPARANDO, ENFRIANDO,              // torreta
    APARECIENDO, FLOTANDO, ATACANDO, DESAPARECIENDO       // acechador
}
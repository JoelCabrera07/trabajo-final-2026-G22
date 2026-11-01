package modelo;

import java.awt.Color;

public class EnemigoTerrestre extends Enemigo {
    private double velocidadPatrullaje;
    private double limiteIzquierdo;
    private double limiteDerecho;
    private boolean moviendoseDerecha;
    private boolean limitesAjustados = false;

    public EnemigoTerrestre(double x, double y) {
        super("Enemigo Terrestre", x, y, 30, 30, Color.RED, 10, 1);
        this.velocidadPatrullaje = 2.0;
        // Por ahora patrulla 100px para cada lado de donde nace.
        // Despues podemos hacer que para cada instancia patrulle un tramo distinto.
        this.limiteIzquierdo = x - 100;
        this.limiteDerecho = x + 100;
        this.moviendoseDerecha = true; // arranca yendo hacia la derecha
    }

    public void patrullar() {
        if (moviendoseDerecha) {
            // Se mueve hacia la derecha sumando la velocidad a su posición x actual
            this.setX(this.getX() + velocidadPatrullaje);
            // Si llega al límite derecho, cambia de dirección
            if (this.getX() >= limiteDerecho) {
                moviendoseDerecha = false;
            }
        } else {
            // Lo mismo pero hacia la izquierda
            this.setX(this.getX() - velocidadPatrullaje);
            if (this.getX() <= limiteIzquierdo) {
                moviendoseDerecha = true;
            }
        }
    }

    @Override
    public void aplicarEfectoColision(Heroe h) {
        // Aplicamos polimorfismo: le saca vida al héroe según el ataque de este bicho
        h.recibirDanio(this.getAtaque());
    }

    @Override
    public void actualizar(Heroe heroe, Escenario escenario) {
        // 1. Calculamos los bordes de la plataforma una sola vez
        if (!limitesAjustados && escenario != null) {
            for (Plataforma p : escenario.getPlataformas()) {
                java.awt.Rectangle hitboxP = p.getHitbox();

                boolean solapaHorizontal = (this.getX() + this.getAncho()) > hitboxP.x
                        && this.getX() < (hitboxP.x + hitboxP.width);
                boolean sobreLaPlataforma = Math.abs((this.getY() + this.getAlto()) - hitboxP.y) <= 30;

                if (solapaHorizontal && sobreLaPlataforma) {
                    this.limiteIzquierdo = hitboxP.x;
                    this.limiteDerecho = hitboxP.x + hitboxP.width - this.getAncho();
                    this.limitesAjustados = true;
                    break;
                }
            }
        }

        // 2. Inteligencia Artificial: ¿Patrullar o Perseguir?
        if (limitesAjustados) {
            // Calculamos dónde están los pies del héroe y del enemigo
            double piesEnemigo = this.getY() + this.getAlto();
            double piesHeroe = heroe.getY() + heroe.getAlto();

            // Verificamos si están a la misma altura (tolerancia de 40px)
            boolean mismaAltura = Math.abs(piesEnemigo - piesHeroe) <= 40;

            // Verificamos si el héroe está posicionado sobre esta misma plataforma
            boolean mismaPlataforma = heroe.getX() >= (limiteIzquierdo - 50) && heroe.getX() <= (limiteDerecho + 50);

            if (mismaAltura && mismaPlataforma) {
                perseguir(heroe); // MODO ATAQUE
            } else {
                patrullar(); // MODO PACÍFICO
            }
        } else {
            patrullar();
        }
    }

    // --- NUEVO MÉTODO DE PERSECUCIÓN ---
    private void perseguir(Heroe heroe) {
        // Multiplicamos la velocidad x1.5 para que corra más rápido al ver al héroe
        double velocidadAtaque = velocidadPatrullaje * 1.5;

        if (heroe.getX() > this.getX()) {
            this.setX(this.getX() + velocidadAtaque);
            moviendoseDerecha = true; // Para que el sprite mire a la derecha
            // Freno de emergencia para que no se caiga al vacío persiguiéndote
            if (this.getX() > limiteDerecho)
                this.setX(limiteDerecho);
        } else if (heroe.getX() < this.getX()) {
            this.setX(this.getX() - velocidadAtaque);
            moviendoseDerecha = false; // Para que el sprite mire a la izquierda
            // Freno de emergencia para que no se caiga al vacío
            if (this.getX() < limiteIzquierdo)
                this.setX(limiteIzquierdo);
        }
    }

    @Override
    public TipoEntidad getTipo() {
        return TipoEntidad.ENEMIGO_TERRESTRE;
    }

    @Override
    public boolean isMirandoDerecha() {
        return moviendoseDerecha; // mira hacia donde camina
    }

}
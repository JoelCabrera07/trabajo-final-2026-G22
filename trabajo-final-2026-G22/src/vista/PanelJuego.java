package vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import modelo.Enemigo;
import modelo.Entidad;
import modelo.Escenario;
import modelo.Heroe;
import modelo.Plataforma;
import modelo.Proyectil;
import modelo.Puerta;

/**
 * La vista del juego. No calcula fisica ni colisiones: solo lee las
 * referencias al escenario y al heroe que le pasaron por constructor, y
 * las dibuja. No conoce a JuegoManager -- esa dependencia va al reves
 * (el controlador es quien conoce a la vista, no al contrario).
 */
public class PanelJuego extends JPanel {

    private final Escenario escenario;
    private final Heroe heroe;
    private Image fondo; // GIF animado de fondo; si queda null se usa el color liso de siempre
    private javax.sound.sampled.Clip clipFondo;
    private javax.sound.sampled.Clip clipAmbienteNivel4;
    private javax.sound.sampled.Clip clipEfecto;
    private static final int HP_POR_CORAZON = 10; // Cada corazón representa 10 puntos de vida
    // Tamaño "logico" del juego: la fisica y las colisiones siempre usan estas medidas
    private static final int ANCHO_JUEGO = 800;
    private static final int ALTO_JUEGO = 600;
    private static final int MS_POR_FOTOGRAMA = 90;
    private static final int MS_POR_FOTOGRAMA_ORBE = 65; // antes: 4 ticks
    private static final int DIST_CABEZA = 9;            // la cabeza del orbe está 9 px adelante del centro del dibujo
    private static final int MS_POR_FOTOGRAMA_ENEMIGO = 130;
    private static final int ALTO_SPRITE_ENEMIGO = 50; 

    private final CatalogoSprites catalogo = new CatalogoSprites();

    public PanelJuego(Escenario escenario, Heroe heroe) {
        this.escenario = escenario;
        this.heroe = heroe;
        URL urlFondo = getClass().getResource("/assets/fondo_antorchas.gif");
        if (urlFondo != null) {
            this.fondo = new ImageIcon(urlFondo).getImage();
        } else {
            System.out.println("Aviso: no se encontro el fondo '/assets/fondo_antorchas.gif'. Se usa el color liso.");
        }
        setPreferredSize(new java.awt.Dimension(800, 600));
        setBackground(new Color(30, 30, 40));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Pixel art: al agrandar, que los pixeles queden nitidos y no borrosos
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        // El juego se calcula siempre en 800x600. Si la ventana es mas grande (pantalla completa)
        // se agranda todo con la misma escala y se centra, dejando bandas a los costados.
        double escala = Math.min(getWidth() / (double) ANCHO_JUEGO, getHeight() / (double) ALTO_JUEGO);
        int margenX = (int) ((getWidth() - ANCHO_JUEGO * escala) / 2);
        int margenY = (int) ((getHeight() - ALTO_JUEGO * escala) / 2);
        if (escala != 1.0) {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
        g2.translate(margenX, margenY);
        g2.scale(escala, escala);
        g2.clipRect(0, 0, ANCHO_JUEGO, ALTO_JUEGO);

        if (fondo != null) {
            g2.drawImage(fondo, 0, 0, ANCHO_JUEGO, ALTO_JUEGO, this);
        }

        if (escenario == null) {
            return;
        }

        for (Plataforma plataforma : escenario.getPlataformas()) {
            dibujarPlataforma(g2, plataforma);
        }

        if (escenario.getPuerta() != null) {
            dibujarPuerta(g2, escenario.getPuerta());
        }

        for (Enemigo enemigo : escenario.getEnemigos()) {
            // Cada enemigo decide solo si debe mostrarse (el Acechador solo se
            // dibuja despues de activarse); no hace falta preguntar de que tipo es.
            if (!enemigo.debeDibujarse()) {
                continue;
            }
            dibujarEnemigo(g2, enemigo);
        }

        for (Proyectil proyectil : escenario.getProyectiles()) {
            dibujarOrbe(g2, proyectil);
        }

        if (heroe != null) {
            // Si esta invencible, parpadea: se oculta en intervalos alternados de 100 ms
            boolean oculto = heroe.isInvencible() && (System.currentTimeMillis() / 100) % 2 == 0;
            if (!oculto) {
                dibujarEntidad(g2, heroe);
            }
            dibujarBarraVida(g2, heroe);

            //solo aparece si el heroe esta cargando el salto
            if (heroe.isCargandoSalto()) { 
                dibujarBarraCarga(g2, heroe);
            }
        }

        // Medidor de progreso: Nivel actual y metros de altura
        dibujarMetrosHUD(g2);
    }

    private void dibujarEntidad(Graphics2D g, Entidad entidad) {
        int y = (int) entidad.getY();
        int yTexto = y; // el nombre va arriba de lo que se dibuje

        if (entidad.getSprite() != null) {
            java.awt.image.BufferedImage img = entidad.getSprite();
            // Cada entidad dice donde y de que tamaño va su sprite (ver Entidad.getAreaSprite)
            java.awt.Rectangle area = entidad.getAreaSprite();
            g.drawImage(img, area.x, area.y, area.width, area.height, null);
            yTexto = Math.min(y, area.y);
        } else { //se dibuja un rectangulo de color si no tiene sprite
            dibujarSinSprite(g, entidad);
        }
        dibujarNombre(g, entidad, yTexto);
    }

    private void dibujarPlataforma (Graphics2D g, Plataforma plataforma) {
        int x = (int) plataforma.getX();
        int y = (int) plataforma.getY();
        int ancho = plataforma.getAncho();
        int alto = plataforma.getAlto();

        BufferedImage img = catalogo.imagenDe(plataforma.getTipo());
        if (img != null) {
            dibujarSpritePlataforma(g, img, x, y, ancho, alto);
        } else {
            dibujarSinSprite(g, plataforma);
        }
        dibujarNombre(g, plataforma, y);
    }

    private void dibujarEnemigo(Graphics2D g, Enemigo enemigo) {
        BufferedImage[] fotogramas = catalogo.fotogramasDe(enemigo.getTipo(), enemigo.mirandoDerecha());
        if (fotogramas == null) {
            dibujarEntidad(g, enemigo);  // Acechador y Torreta, hasta la etapa 5
            return;
        }
        int i = (int) (System.currentTimeMillis() / MS_POR_FOTOGRAMA_ENEMIGO % fotogramas.length);
        int y = (int) enemigo.getY();
        g.drawImage(fotogramas[i], (int) enemigo.getX(), y, enemigo.getAncho(), ALTO_SPRITE_ENEMIGO, null);
        dibujarNombre(g, enemigo, y);
    }

    private void dibujarPuerta(Graphics2D g, Puerta puerta) {
        BufferedImage[] fotogramas = catalogo.fotogramasPortal();

         if (fotogramas == null) {
            dibujarSinSprite(g, puerta);
            dibujarNombre(g, puerta, (int) puerta.getY());
            return;
         }
            //1.Elegir el fotograma segun el reloj
            int i = (int) ((System.currentTimeMillis() / MS_POR_FOTOGRAMA) % fotogramas.length);
            BufferedImage img = fotogramas[i];
            //2.Calcular x e y para centrarlo sobre la hitbox y apoyarlo en el piso
            int x = (int) puerta.getX() + puerta.getAncho() / 2 - img.getWidth() / 2;
            int y = (int) puerta.getY() + puerta.getAlto() - img.getHeight();

            // 3. Dibujar
            g.drawImage(img, x, y, null);

            // 4. El nombre va arriba de lo más alto entre la hitbox y el dibujo
            dibujarNombre(g, puerta, Math.min((int) puerta.getY(), y));
    }

    private void dibujarOrbe(Graphics2D g, Proyectil p) {
        BufferedImage[] fotogramas = catalogo.fotogramasOrbe();
        if (fotogramas == null) {
            dibujarSinSprite(g, p); 
            dibujarNombre(g, p, (int) p.getY() - 17);
            return;
        }
        int i = (int) (System.currentTimeMillis() / MS_POR_FOTOGRAMA_ORBE % fotogramas.length);
        BufferedImage img = fotogramas[i];

        double centroX = p.getX() + p.getAncho() / 2.0;
        double centroY = p.getY() + p.getAlto() / 2.0;

        Graphics2D g2 = (Graphics2D) g.create();   // copia: la rotación no afecta al resto
        g2.translate(centroX, centroY);
        g2.rotate(p.getAngulo());
        g2.drawImage(img, -img.getWidth() / 2 - DIST_CABEZA, -img.getHeight() / 2, null);
        g2.dispose();

        dibujarNombre(g, p, (int) p.getY() - 17);  // con el g original, no el rotado
    }

    private void dibujarSinSprite(Graphics2D g, Entidad entidad) {
        int x = (int) entidad.getX();
        int y = (int) entidad.getY();
        int ancho = entidad.getAncho();
        int alto = entidad.getAlto();

        g.setColor(entidad.getColor());
        g.fillRect(x, y, ancho, alto);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, ancho, alto);
    }

    private void dibujarNombre(Graphics2D g, Entidad entidad, int yBase) {
        int x = (int) entidad.getX();
        g.setColor(Color.WHITE);
        g.drawString(entidad.getNombre(), x, yBase-4);
    }

    // Dibuja el sprite de una plataforma SIN deformarlo: siempre se escala con la misma
    // proporcion en ancho y alto. El borde de arriba del dibujo queda justo en la
    // hitbox, que es donde el heroe apoya los pies.
    private void dibujarSpritePlataforma(Graphics2D g, java.awt.image.BufferedImage img,int x, int y, int ancho, int alto) {
        int w = img.getWidth();
        int h = img.getHeight();

        if (ancho > 250) {
            // Plataforma larga (el piso): alto = alto de la hitbox y se repite la parte central
            // del sprite, sin los bordes redondeados, hasta cubrir todo el ancho.
            double escala = (double) alto / h;
            int anchoBaldosa = Math.max(1, (int) Math.round((w / 2) * escala)); // ancho de cada repeticion en pantalla
            int origenX = w / 4;
            int origenAncho = w / 2;

            for (int xActual = x; xActual < x + ancho; xActual += anchoBaldosa) {
                int anchoDibujo = Math.min(anchoBaldosa, x + ancho - xActual);
                int origenFin = origenX + (int) Math.round(origenAncho * (double) anchoDibujo / anchoBaldosa);
                g.drawImage(img, xActual, y, xActual + anchoDibujo, y + alto,
                            origenX, 0, origenFin, h, null);
            }
        } else {
            // Plataforma normal: el ancho lo da la hitbox y el alto sale de mantener la proporcion
            int altoDibujo = (int) Math.round((double) ancho * h / w);
            g.drawImage(img, x, y, ancho, altoDibujo, null);
        }
    }

    private void dibujarCorazon(Graphics2D g, int x, int y, int tamano, Color color) {
        g.setColor(color);
        g.fillArc(x, y, tamano / 2, tamano / 2, 0, 360);
        g.fillArc(x + tamano / 2, y, tamano / 2, tamano / 2, 0, 360);
        int[] xs = { x, x + tamano, x + tamano / 2 };
        int[] ys = { y + tamano / 4, y + tamano / 4, y + tamano };
        g.fillPolygon(xs, ys, 3);
    }

    private void dibujarBarraVida(Graphics2D g, Heroe heroe) {
        int tamanoCorazon = 20;
        int espacio = 4;
        int x = 20;
        int y = 20;

        int numeroCorazones = (int) Math.ceil((double) heroe.getHpMax() / HP_POR_CORAZON);

        for (int i = 0; i < numeroCorazones; i++) {
            double hpDeEsteCorazon = heroe.getHp() - (i * HP_POR_CORAZON);
            // Que parte del corazon esta llena: 0.0 (vacio) a 1.0 (lleno)
            double llenado = Math.max(0, Math.min(1, hpDeEsteCorazon / HP_POR_CORAZON));
            int xCorazon = x + i * (tamanoCorazon + espacio);

            // Primero el corazon vacio (gris) y encima el rojo, recortado segun cuanta vida queda
            dibujarCorazon(g, xCorazon, y, tamanoCorazon, Color.GRAY);
            if (llenado > 0) {
                java.awt.Shape clipOriginal = g.getClip();
                g.clipRect(xCorazon, y, (int) Math.ceil(tamanoCorazon * llenado), tamanoCorazon + 1);
                dibujarCorazon(g, xCorazon, y, tamanoCorazon, Color.RED);
                g.setClip(clipOriginal);
            }
        }
    }

    private void dibujarBarraCarga(Graphics2D g, Heroe heroe) {
    int anchoBarra = 40;
    int altoBarra = 6;
    int x = (int) heroe.getX();
    int y = (int) heroe.getY() - 15; // un poco arriba de la cabeza del héroe

    double porcentaje = heroe.getPorcentajeCargaSalto();

    g.setColor(Color.DARK_GRAY);
    g.fillRect(x, y, anchoBarra, altoBarra);

    g.setColor(Color.YELLOW);
    g.fillRect(x, y, (int) (anchoBarra * porcentaje), altoBarra);

    g.setColor(Color.WHITE);
    g.drawRect(x, y, anchoBarra, altoBarra);
    }

    private void dibujarMetrosHUD(Graphics2D g2) {
        if (escenario == null || heroe == null) return;
        int pantalla = escenario.getPantallaActual();
        // 10 píxeles = 1 metro desde la base Y=550 de la pantalla 1
        double alturaPx = ((pantalla - 1) * 600.0) + (550.0 - heroe.getY());
        int metros = Math.max(0, (int) (alturaPx / 10.0));

        g2.setFont(new Font("Arial", Font.BOLD, 15));
        String texto = "Nivel " + pantalla + "  |  " + metros + " m";
        FontMetrics fm = g2.getFontMetrics();
        int anchoTexto = fm.stringWidth(texto);
        int x = ANCHO_JUEGO - anchoTexto - 25;
        int y = 25;

        // Fondo oscuro semitransparente
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(x - 10, y - 16, anchoTexto + 20, 26, 8, 8);

        // Borde dorado
        g2.setColor(new Color(255, 220, 60, 180));
        g2.drawRoundRect(x - 10, y - 16, anchoTexto + 20, 26, 8, 8);

        // Texto dorado
        g2.setColor(new Color(255, 220, 60));
        g2.drawString(texto, x, y + 3);
    }

//  Agregamos estos dos métodos al final de la clase PanelJuego:
public void reproducirMusica() {
    detenerMusica();
    try {
        java.net.URL urlAudio = getClass().getResource("/recursos/Escenario/Ascent_to_the_Iron_Spire.wav"); 
        if (urlAudio != null) {
            javax.sound.sampled.AudioInputStream audioStream = javax.sound.sampled.AudioSystem.getAudioInputStream(urlAudio);
            clipFondo = javax.sound.sampled.AudioSystem.getClip();
            clipFondo.open(audioStream);
            clipFondo.loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY); // Que se repita infinito
            clipFondo.start();
        } else {
            System.err.println("No se encontró la música del juego.");
        }
    } catch (Exception e) {
        System.err.println("Error al reproducir música: " + e.getMessage());
    }
}

public void detenerMusica() {
    if (clipFondo != null && clipFondo.isRunning()) {
        clipFondo.stop();
        clipFondo.close();}
    }
public void reproducirEfectoSFX(String rutaSFX) {
    new Thread(() -> {
        detenerEfecto();
        try {
            URL urlAudio = getClass().getResource(rutaSFX);
            if (urlAudio != null) {
                javax.sound.sampled.AudioInputStream audioStream = javax.sound.sampled.AudioSystem.getAudioInputStream(urlAudio);
                clipEfecto = javax.sound.sampled.AudioSystem.getClip();
                clipEfecto.open(audioStream);
                clipEfecto.start();
            } else {
                System.err.println("No se encontró el efecto de sonido: " + rutaSFX);
            }
        } catch (Exception e) {
            System.err.println("Error al reproducir efecto SFX: " + e.getMessage());
        }
    }).start();
}
public void detenerEfecto() { // Detiene el efecto de sonido si está sonando
    if (clipEfecto != null && clipEfecto.isRunning()) {
        clipEfecto.stop();
        clipEfecto.close();
    }
}
public void cargarFondoNivel(int nivel) {
    try {
        // Carga dinámicamente nivel_1.jpg, nivel_2.jpg, etc.
        URL rutaFondo = getClass().getResource("/recursos/Escenario/nivel_" + nivel + ".gif"); 
        if (rutaFondo != null) {
            this.fondo = new ImageIcon(rutaFondo).getImage();
            repaint(); // Fuerza a que se redibuje la pantalla al instante
        } else {
            System.err.println("No se pudo cargar el fondo del nivel: " + nivel);
        }
    } catch (Exception e) {
        System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
    }
}
public void reproducirAmbienteNivel4() {
    // Si ya está sonando el ambiente del nivel 4, no lo volvemos a arrancar desde cero
    if (clipAmbienteNivel4 != null && clipAmbienteNivel4.isRunning()) return;
    
    try {
        URL urlAudio = getClass().getResource("/recursos/Escenario/nivel4_ambiente.wav"); 
        if (urlAudio != null) {
            javax.sound.sampled.AudioInputStream audioStream = javax.sound.sampled.AudioSystem.getAudioInputStream(urlAudio);
            clipAmbienteNivel4 = javax.sound.sampled.AudioSystem.getClip();
            clipAmbienteNivel4.open(audioStream);
            clipAmbienteNivel4.loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY); // Bucle infinito
            clipAmbienteNivel4.start();
        } else {
            System.err.println("No se encontró el audio de ambiente del nivel 4.");
        }
    } catch (Exception e) {
        System.err.println("Error al reproducir ambiente del nivel 4: " + e.getMessage());
    }
}

public void detenerAmbienteNivel4() {
    if (clipAmbienteNivel4 != null && clipAmbienteNivel4.isRunning()) {
        clipAmbienteNivel4.stop();
        clipAmbienteNivel4.close();
    }
}
}
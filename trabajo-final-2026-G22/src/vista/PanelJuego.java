package vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
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
import modelo.EstadoEnemigo;

/**
 * La vista del juego. No calcula fisica ni colisiones: solo lee las
 * referencias al escenario y al heroe que le pasaron por constructor, y
 * las dibuja. No conoce a JuegoManager -- esa dependencia va al reves
 * (el controlador es quien conoce a la vista, no al contrario).
 */
public class PanelJuego extends JPanel {

    private final Escenario escenario;
    private final Heroe heroe;
    private static final Color COLOR_RESPALDO = Color.MAGENTA; //si falta una imagen, se nota enseguida
    private Image fondo; // GIF animado de fondo; si queda null se usa el color liso de siempre
    private javax.sound.sampled.Clip clipFondo;
    private javax.sound.sampled.Clip clipAmbienteNivel4;
    private javax.sound.sampled.Clip clipEfecto;
    private static final int HP_POR_CORAZON = 10; // Cada corazón representa 10 puntos de vida
    // Tamaño "logico" del juego: la fisica y las colisiones siempre usan estas
    // medidas
    private static final int ANCHO_JUEGO = 800;
    private static final int ALTO_JUEGO = 600;
    private final CatalogoSprites catalogo = new CatalogoSprites();
    private static final int MS_POR_FOTOGRAMA_PORTAL = 90;
    private static final int MS_POR_FOTOGRAMA_ENEMIGO = 130;
    private static final int ALTO_DIBUJO_ENEMIGO = 50; // mismo alto con que se dibujaban antes
    private static final int MS_POR_FOTOGRAMA_CORRER = 100; // antes: cada 6 frames
    private static final int MS_POR_FOTOGRAMA_QUIETO = 400; // antes: cada 25 frames
    private static final int ALTO_DIBUJO_HEROE = 50; // mismo alto con que se dibujaba antes
    private static final int TICKS_POR_FOTOGRAMA_ENEMIGO = 6; // aparecer/desaparecer: un fotograma cada 6 frames
    private static final int MS_POR_FOTOGRAMA_PROYECTIL = 64;
    private static final int DIST_CABEZA_PROYECTIL = 9;

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

        // El juego se calcula siempre en 800x600. Si la ventana es mas grande (pantalla
        // completa)
        // se agranda todo con la misma escala y se centra, dejando bandas a los
        // costados.
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
            if (!enemigo.isVisible()) {
                continue;
            }
            dibujarEnemigo(g2, enemigo);
        }

        for (Proyectil proyectil : escenario.getProyectiles()) {
            dibujarProyectil(g2, proyectil);
        }

        if (heroe != null) {
            // Si esta invencible, parpadea: se oculta en intervalos alternados de 100 ms
            boolean oculto = heroe.isInvencible() && (System.currentTimeMillis() / 100) % 2 == 0;
            if (!oculto) {
                dibujarHeroe(g2, heroe);
            }
            dibujarBarraVida(g2, heroe);

            // solo aparece si el heroe esta cargando el salto
            if (heroe.isCargandoSalto()) {
                dibujarBarraCarga(g2, heroe);
            }
        }

        // Medidor de progreso: Nivel actual y metros de altura
        dibujarMetrosHUD(g2);
    }

    // Las plataformas se dibujan con la imagen del catálogo según su tipo
    private void dibujarPlataforma(Graphics2D g, Plataforma plataforma) {
        int x = (int) plataforma.getX();
        int y = (int) plataforma.getY();
        BufferedImage imagen = catalogo.getImagen(plataforma.getTipo());

        if (imagen != null) {
            dibujarSpritePlataforma(g, imagen, x, y, plataforma.getAncho(), plataforma.getAlto());
        } else {
            g.setColor(COLOR_RESPALDO);
            g.fillRect(x, y, plataforma.getAncho(), plataforma.getAlto());
        }
    }

    // La puerta se anima con el reloj: cada 90 ms pasa al siguiente fotograma
    private void dibujarPuerta(Graphics2D g, Puerta puerta) {
        BufferedImage[] fotogramas = catalogo.getAnimacion(puerta.getTipo());

        if (fotogramas == null) {
            g.setColor(COLOR_RESPALDO);
            g.fillRect((int) puerta.getX(), (int) puerta.getY(), puerta.getAncho(), puerta.getAlto());
            return;
        }

        int i = (int) ((System.currentTimeMillis() / MS_POR_FOTOGRAMA_PORTAL) % fotogramas.length);
        BufferedImage img = fotogramas[i];

        // El portal es más grande que la hitbox: se centra sobre ella y apoya en el
        // piso
        int x = (int) puerta.getX() + puerta.getAncho() / 2 - img.getWidth() / 2;
        int y = (int) puerta.getY() + puerta.getAlto() - img.getHeight();
        g.drawImage(img, x, y, null);
    }

    // Los enemigos se dibujan con la animación del catálogo, según su tipo, su
    // estado y hacia dónde miran
    private void dibujarEnemigo(Graphics2D g, Enemigo enemigo) {
        BufferedImage[] fotogramas = catalogo.getAnimacionEnemigo(
                enemigo.getTipo(), enemigo.getEstado(), enemigo.isMirandoDerecha());

        if (fotogramas == null) {
            dibujarEntidad(g, enemigo); // si la imagen no cargo
            return;
        }

        BufferedImage img = fotogramas[elegirFotogramaEnemigo(enemigo, fotogramas.length)];
        int x = (int) enemigo.getX();
        int y = (int) enemigo.getY();

        // Si el catálogo no indica otra cosa, se dibuja en su posición con el tamaño
        // normal
        Rectangle area = catalogo.getAreaDibujo(enemigo.getTipo());
        if (area == null) {
            area = new Rectangle(0, 0, enemigo.getAncho(), ALTO_DIBUJO_ENEMIGO);
        }
        g.drawImage(img, x + area.x, y + area.y, area.width, area.height, null);

        g.setColor(Color.WHITE);
        g.drawString(enemigo.getNombre(), x, Math.min(y, y + area.y) - 4);
    }

    private void dibujarProyectil(Graphics2D g2, Proyectil proyectil) {
        BufferedImage[] frames = catalogo.getAnimacion(proyectil.getTipo());
        if (frames == null) {
            dibujarEntidad(g2, proyectil);
            return;
        }
        int indice = (int) ((System.currentTimeMillis() / MS_POR_FOTOGRAMA_PROYECTIL) % frames.length);
        BufferedImage frame = frames[indice];

        double angulo = Math.atan2(proyectil.getVelocidadY(), proyectil.getVelocidadX());
        double centroX = proyectil.getX() + proyectil.getAncho() / 2.0;
        double centroY = proyectil.getY() + proyectil.getAlto() / 2.0;

        Graphics2D g = (Graphics2D) g2.create();
        g.translate(centroX, centroY);
        g.rotate(angulo);
        g.drawImage(frame, -DIST_CABEZA_PROYECTIL - frame.getWidth() / 2, -frame.getHeight() / 2, null);
        g.dispose();
    }

    // Aparecer y desaparecer se ven una sola vez, según cuánto llevan; el resto se
    // repite con el reloj
    private int elegirFotogramaEnemigo(Enemigo enemigo, int cantidad) {
        EstadoEnemigo estado = enemigo.getEstado();
        if (estado == EstadoEnemigo.APARECIENDO || estado == EstadoEnemigo.DESAPARECIENDO
                || estado == EstadoEnemigo.CARGANDO || estado == EstadoEnemigo.ENFRIANDO) {
            return Math.min(cantidad - 1, enemigo.getTicksEnEstado() / TICKS_POR_FOTOGRAMA_ENEMIGO);
        }
        return (int) ((System.currentTimeMillis() / MS_POR_FOTOGRAMA_ENEMIGO) % cantidad);
    }

    // El héroe se dibuja según su estado: la vista elige la animación y el
    // fotograma
    private void dibujarHeroe(Graphics2D g, Heroe heroe) {
        BufferedImage[] fotogramas = catalogo.getAnimacionHeroe(heroe.getEstado(), heroe.isMirandoDerecha());

        if (fotogramas == null) {
            dibujarEntidad(g, heroe);
            return;
        }

        BufferedImage img = fotogramas[elegirFotogramaHeroe(heroe, fotogramas.length)];
        int x = (int) heroe.getX();
        int y = (int) heroe.getY();
        g.drawImage(img, x, y, heroe.getAncho(), ALTO_DIBUJO_HEROE, null);

        g.setColor(Color.WHITE);
        g.drawString(heroe.getNombre(), x, y - 4);
    }

    // Qué fotograma corresponde según lo que está haciendo el héroe
    private int elegirFotogramaHeroe(Heroe heroe, int cantidad) {
        long ahora = System.currentTimeMillis();
        switch (heroe.getEstado()) {
            case CARGANDO_SALTO:
                return Math.min(3, (int) (heroe.getPorcentajeCargaSalto() * 4)); // 0 a 3 según cuánto cargó
            case DESPEGANDO:
                return 4;
            case EN_EL_AIRE:
                double vy = heroe.getVelocidadY();
                if (vy < -3)
                    return 1; // subiendo
                if (vy < 1.5)
                    return 2; // punto más alto
                if (vy < 5)
                    return 3; // empezando a caer
                return 4; // cayendo rápido
            case ATERRIZANDO:
                return 5;
            case CORRIENDO:
                return (int) ((ahora / MS_POR_FOTOGRAMA_CORRER) % cantidad);
            default: // QUIETO
                return (int) ((ahora / MS_POR_FOTOGRAMA_QUIETO) % cantidad);
        }
    }

    private void dibujarEntidad(Graphics2D g, Entidad entidad) {
        int x = (int) entidad.getX();
        int y = (int) entidad.getY();
        int ancho = entidad.getAncho();
        int alto = entidad.getAlto();
        g.setColor(COLOR_RESPALDO);
        g.fillRect(x, y, ancho, alto);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, ancho, alto);
        g.setColor(Color.WHITE);
        g.drawString(entidad.getNombre(), x, y - 4);
    }

    // Dibuja el sprite de una plataforma SIN deformarlo: siempre se escala con la
    // misma
    // proporcion en ancho y alto. El borde de arriba del dibujo queda justo en la
    // hitbox, que es donde el heroe apoya los pies.
    private void dibujarSpritePlataforma(Graphics2D g, java.awt.image.BufferedImage img, int x, int y, int ancho,
            int alto) {
        int w = img.getWidth();
        int h = img.getHeight();

        if (ancho > 250) {
            // Plataforma larga (el piso): alto = alto de la hitbox y se repite la parte
            // central
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
            // Plataforma normal: el ancho lo da la hitbox y el alto sale de mantener la
            // proporcion
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

            // Primero el corazon vacio (gris) y encima el rojo, recortado segun cuanta vida
            // queda
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
        if (escenario == null || heroe == null)
            return;
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

    // Agregamos estos dos métodos al final de la clase PanelJuego:
    public void reproducirMusica() {
        detenerMusica();
        try {
            java.net.URL urlAudio = getClass().getResource("/recursos/Escenario/Ascent_to_the_Iron_Spire.wav");
            if (urlAudio != null) {
                javax.sound.sampled.AudioInputStream audioStream = javax.sound.sampled.AudioSystem
                        .getAudioInputStream(urlAudio);
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
            clipFondo.close();
        }
    }

    public void reproducirEfectoSFX(String rutaSFX) {
        new Thread(() -> {
            detenerEfecto();
            try {
                URL urlAudio = getClass().getResource(rutaSFX);
                if (urlAudio != null) {
                    javax.sound.sampled.AudioInputStream audioStream = javax.sound.sampled.AudioSystem
                            .getAudioInputStream(urlAudio);
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
        // Si ya está sonando el ambiente del nivel 4, no lo volvemos a arrancar desde
        // cero
        if (clipAmbienteNivel4 != null && clipAmbienteNivel4.isRunning())
            return;

        try {
            URL urlAudio = getClass().getResource("/recursos/Escenario/nivel4_ambiente.wav");
            if (urlAudio != null) {
                javax.sound.sampled.AudioInputStream audioStream = javax.sound.sampled.AudioSystem
                        .getAudioInputStream(urlAudio);
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
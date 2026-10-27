package vista;

import java.awt.Color; 
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics; 
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image; 
import java.awt.Insets;
import java.net.URL; 
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.swing.ImageIcon; 
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MenuPrincipal extends JPanel {
    
    private JButton botonJugar;
    private JButton botonRanking; 
    private JButton botonSalir;
    private Image imagenFondo; 
    private Clip clipAmbiente; 

    public MenuPrincipal() {
        this.setPreferredSize(new java.awt.Dimension(800, 600)); 
        this.setBackground(new Color(24,26,36)); 
        this.setLayout(new GridBagLayout()); 

        GridBagConstraints gbc = new GridBagConstraints(); 

        URL rutaGif = getClass().getResource("/recursos/Menu/Fondo_Menu.gif"); 
        if (rutaGif != null) {
            this.imagenFondo = new ImageIcon(rutaGif).getImage(); 
        } else {
            System.err.println("No se pudo cargar la imagen de fondo: " + rutaGif);
        }

        reproducirAudioAmbiente("/recursos/Menu/Ambiente_Menu.wav"); 

        // ------ TÍTULO ------
        // Logo animado en pixel art; si no se encuentra, queda el texto de siempre
        JLabel titulo;
        URL rutaTitulo = getClass().getResource("/recursos/Menu/titulo.gif");
        if (rutaTitulo != null) {
            titulo = new JLabel(new ImageIcon(rutaTitulo));
        } else {
            titulo = new JLabel("Jumping Hero");
            titulo.setFont(new Font("Arial", Font.BOLD, 46));
            titulo.setForeground(new Color(255, 220, 60));
        }
        gbc.insets = new Insets(0, 10, 150, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titulo, gbc);

        // ------ PANEL DE BOTONES ------ (uno debajo del otro, como en el menu de un juego)
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 0, 12));
        panelBotones.setOpaque(false); // Transparente para que se vea el fondo

        botonJugar = crearBoton("Jugar", "jugar");
        botonRanking = crearBoton("Ranking", "ranking");
        botonSalir = crearBoton("Salir", "salir");
        panelBotones.add(botonJugar);
        panelBotones.add(botonRanking);
        panelBotones.add(botonSalir);

        // Agregamos el panel entero abajo del título
        gbc.insets = new Insets(0, 10, 0, 10);
        gbc.gridx = 0; 
        gbc.gridy = 1; 
        add(panelBotones, gbc);
    }

    /**
     * Arma un boton con las imagenes de /recursos/Menu/boton_NOMBRE.png:
     * normal, _hover (mouse encima) y _presionado. Si faltan, queda un boton comun con texto.
     */
    private JButton crearBoton(String texto, String nombre) {
        URL normal = getClass().getResource("/recursos/Menu/boton_" + nombre + ".png");
        URL encima = getClass().getResource("/recursos/Menu/boton_" + nombre + "_hover.png");
        URL apretado = getClass().getResource("/recursos/Menu/boton_" + nombre + "_presionado.png");
        JButton boton;
        if (normal != null && encima != null && apretado != null) {
            boton = new JButton(new ImageIcon(normal));
            boton.setRolloverIcon(new ImageIcon(encima));
            boton.setPressedIcon(new ImageIcon(apretado));
            boton.setRolloverEnabled(true);
            boton.setContentAreaFilled(false); // sin el fondo gris de Swing
            boton.setBorderPainted(false);
            boton.setBorder(null);
            boton.setOpaque(false);
            boton.setToolTipText(texto);
        } else {
            boton = new JButton(texto);
            boton.setFont(new Font("Arial", Font.BOLD, 18));
        }
        boton.setFocusPainted(false);
        boton.setFocusable(false); // asi el teclado le sigue llegando a la ventana del juego
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return boton;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            // Cubre toda la ventana sin deformar el dibujo (en pantalla completa recorta los bordes)
            int w = imagenFondo.getWidth(this), h = imagenFondo.getHeight(this);
            if (w <= 0 || h <= 0) { w = 800; h = 600; }
            double escala = Math.max(getWidth() / (double) w, getHeight() / (double) h);
            int anchoDibujo = (int) Math.ceil(w * escala), altoDibujo = (int) Math.ceil(h * escala);
            g.drawImage(imagenFondo, (getWidth() - anchoDibujo) / 2, (getHeight() - altoDibujo) / 2,
                    anchoDibujo, altoDibujo, this);
        }
    }

    public JButton getBotonJugar() { return botonJugar; }
    public JButton getBotonRanking() { return botonRanking; }
    public JButton getBotonSalir() { return botonSalir; }

    public void reproducirAudioAmbiente(String ruta) {
        detenerAudio();
        try {
            URL urlAudio = getClass().getResource(ruta);
            if (urlAudio != null) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(urlAudio);
                clipAmbiente = AudioSystem.getClip();
                clipAmbiente.open(audioStream);
                clipAmbiente.loop(Clip.LOOP_CONTINUOUSLY); 
                clipAmbiente.start();
            }
        } catch (Exception e) {
            System.err.println("Error al reproducir sonido: " + e.getMessage());
        }
    }

    public void detenerAudio() {
        if (clipAmbiente != null && clipAmbiente.isRunning()) {
            clipAmbiente.stop();
            clipAmbiente.close();
        }
    }
}
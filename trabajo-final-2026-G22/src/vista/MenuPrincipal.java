package vista;

import java.awt.Color; // Importamos la clase Color para poder usar colores en el panel
import java.awt.Font;
import java.awt.Graphics; // Importamos la clase Graphics para poder dibujar en el panel
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image; // Importamos la clase Image para poder usar imágenes en el panel
import java.awt.Insets;
import java.net.URL; // Importamos la clase URL para poder cargar imágenes desde archivos
import javax.sound.sampled.AudioInputStream; // Importamos la clase AudioInputStream para poder reproducir sonidos desde archivos
import javax.sound.sampled.AudioSystem; // Importamos la clase AudioSystem para poder reproducir
import javax.sound.sampled.Clip; // Importamos la clase Clip para poder reproducir sonidos desde archivos
import javax.swing.ImageIcon; // Importamos la clase ImageIcon para poder cargar imágenes desde archivos
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MenuPrincipal extends JPanel {
    
    private JButton botonJugar;
    private JButton botonSalir;
    private Image imagenFondo; // Imagen de fondo del panel
    private Clip clipAmbiente; // Clip de audio para reproducir el sonido de fondo
    public MenuPrincipal() {
        this.setPreferredSize(new java.awt.Dimension(800, 600)); // Establecemos el tamaño preferido del panel
        this.setBackground(new Color(24,26,36)); // Establecemos el color de fondo del panel
        this.setLayout(new GridBagLayout()); // Establecemos el layout del panel como GridBagLayout para poder organizar los componentes en una cuadrícula

        GridBagConstraints gbc = new GridBagConstraints(); // Creamos un objeto GridBagConstraints para poder usar el layout GridBagLayout
        gbc.insets = new Insets(10, 10, 10, 10);

        URL rutaGif = getClass().getResource("/recursos/Menu/Fondo_Menu.gif"); // Cargamos la imagen de fondo desde el archivo
        if (rutaGif != null) {
            this.imagenFondo = new ImageIcon(rutaGif).getImage(); // Cargamos la imagen de fondo desde el archivo y la convertimos a un objeto Image
        } else {
            System.err.println("No se pudo cargar la imagen de fondo: " + rutaGif);
        }

        // CARGA DE SONIDO DE FONDO
        reproducirAudioAmbiente("/recursos/Menu/Ambiente_Menu.wav"); // Reproducimos el sonido de fondo del menú
        // ------TITULO---------
        JLabel titulo = new JLabel("Jumping Hero");
        titulo.setFont(new Font("Arial", Font.BOLD, 46)); // Establecemos la fuente y el tamaño del título
        titulo.setForeground(new Color(255,220,60)); // Color amarillo para el título
        
        gbc.insets = new Insets(30, 10, 220, 10); // Espaciado alrededor del título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titulo, gbc);

        // ------BOTONES---------
        gbc.insets = new Insets(10, 15, 10, 15); // Espaciado alrededor de los botones
        gbc.gridwidth = 1; // Cada botón ocupa una columna
        botonJugar = new JButton("Jugar");
        botonJugar.setFont(new Font("Arial", Font.BOLD, 18)); // Establecemos la fuente y el tamaño del botón
        botonJugar.setFocusPainted(false); // Quitamos el borde del botón al hacer clic
        gbc.gridx = 0; // Primer botón en la primera columna
        gbc.gridy = 1; // Primer botón en la segunda fila
        gbc.gridwidth = 1;
        add(botonJugar, gbc);

        botonSalir = new JButton("Salir");
        botonSalir.setFont(new Font ("Arial", Font.BOLD, 18)); // Establecemos la fuente y el tamaño del botón
        botonSalir.setFocusPainted(false); // Quitamos el borde del botón al hacer clic
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(botonSalir, gbc);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this); 
        }// Dibuja la imagen de fondo en el panel, ajustando su tamaño al del panel
    }
    public JButton getBotonJugar() {
        return botonJugar;
    }

    public JButton getBotonSalir() {
        return botonSalir;
    }
    private void reproducirAudioAmbiente(String ruta) {
        try {
            URL urlAudio = getClass().getResource(ruta);
            if (urlAudio != null) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(urlAudio);
                clipAmbiente = AudioSystem.getClip();
                clipAmbiente.open(audioStream);
                clipAmbiente.loop(Clip.LOOP_CONTINUOUSLY); // Bucle infinito
                clipAmbiente.start();
            } else {
                System.err.println("No se encontró el archivo de audio: " + ruta);
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

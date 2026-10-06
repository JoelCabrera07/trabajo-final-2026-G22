package vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PantallaGameOver extends JPanel {
    private JButton botonReintentar;
    private JButton botonMenu;

    public PantallaGameOver() {
        this.setPreferredSize(new java.awt.Dimension(800,600));
        this.setBackground(new Color (20,5,5)); //Fondo Rojo muy oscuro tipo sangre
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        // --- TITULO ---
        JLabel titulo = new JLabel("GAME OVER");
        titulo.setFont(new Font("Impact" , Font.BOLD, 70));
        titulo.setForeground(new Color(255,50,50)); //Rojo brillante

        gbc.insets = new Insets (0,10,100,10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titulo,gbc);

        // --- BOTONES ---
        gbc.insets = new Insets (10,15,10,15);
        gbc.gridwidth = 1;

        // --- BOTON REINTENTAR (IZQUIERDA) ---
        botonReintentar = new JButton("Reintentar");
        estilarBoton(botonReintentar);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(botonReintentar,gbc);

        // --- BOTON MENU (DERECHA) ---
        botonMenu = new JButton("Salir al Menu");
        estilarBoton(botonMenu);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(botonMenu, gbc);
    }
    //Metodo auxiliar para no repetir codigo visual
    private void estilarBoton(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 18));
        boton.setBackground(new Color(30,31,44));
        boton.setForeground(new Color(255,220,60));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(255,220,60),2));
    }
    public JButton getBotonReintentar(){
        return botonReintentar;
    }
    public JButton getBotonMenu(){
        return botonMenu;
    }
}

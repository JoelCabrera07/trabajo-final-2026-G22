package vista;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MenuPrincipal extends JPanel {
    
    private JButton botonJugar;
    private JButton botonSalir;

    public MenuPrincipal() {
        this.setPreferredSize(new java.awt.Dimension(800, 600)); // Establecemos el tamaño preferido del panel
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titulo = new JLabel("Jumping Hero");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titulo, gbc);

        botonJugar = new JButton("Jugar");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(botonJugar, gbc);

        botonSalir = new JButton("Salir");
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(botonSalir, gbc);
    }

    public JButton getBotonJugar() {
        return botonJugar;
    }

    public JButton getBotonSalir() {
        return botonSalir;
    }

    
}

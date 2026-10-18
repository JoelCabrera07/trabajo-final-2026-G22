package vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class PantallaGameOver extends JPanel {

    private JLabel lblPuntaje;
    private JTextField campoNombre;
    private JButton botonGuardar;
    private JLabel lblEstado;
    private JButton botonReintentar;
    private JButton botonMenu;

    public PantallaGameOver() {
        this.setPreferredSize(new Dimension(800, 600));
        this.setBackground(new Color(20, 5, 5)); // Fondo rojo muy oscuro
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        // 1. TITULO
        JLabel titulo = new JLabel("GAME OVER");
        titulo.setFont(new Font("Impact", Font.BOLD, 68));
        titulo.setForeground(new Color(255, 50, 50)); // Rojo brillante
        gbc.insets = new Insets(0, 10, 15, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titulo, gbc);

        // 2. METROS ALCANZADOS
        lblPuntaje = new JLabel("Altura alcanzada: 0 metros");
        lblPuntaje.setFont(new Font("Arial", Font.BOLD, 22));
        lblPuntaje.setForeground(new Color(255, 220, 60)); // Dorado
        gbc.insets = new Insets(0, 10, 20, 10);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(lblPuntaje, gbc);

        // 3. PANEL DE INGRESO DE NOMBRE
        JPanel panelRegistro = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelRegistro.setOpaque(false);

        JLabel lblNombre = new JLabel("Tu Nombre:");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 16));
        lblNombre.setForeground(Color.WHITE);
        panelRegistro.add(lblNombre);

        campoNombre = new JTextField(10);
        campoNombre.setFont(new Font("Arial", Font.BOLD, 16));
        campoNombre.setHorizontalAlignment(JTextField.CENTER);
        campoNombre.setBackground(new Color(35, 30, 45));
        campoNombre.setForeground(new Color(255, 220, 60));
        campoNombre.setCaretColor(Color.WHITE);
        campoNombre.setBorder(BorderFactory.createLineBorder(new Color(255, 220, 60), 2));

        // Regla: Máximo 10 caracteres (impide tipear o pegar más de 10)
        campoNombre.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if (str == null) return;
                if ((getLength() + str.length()) <= 10) {
                    super.insertString(offset, str, attr);
                }
            }
        });
        panelRegistro.add(campoNombre);

        botonGuardar = new JButton("Guardar");
        estilarBoton(botonGuardar);
        botonGuardar.setFont(new Font("Arial", Font.BOLD, 14));
        panelRegistro.add(botonGuardar);

        gbc.insets = new Insets(5, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(panelRegistro, gbc);

        // 4. ETIQUETA DE ESTADO / CONFIRMACION
        lblEstado = new JLabel(" ");
        lblEstado.setFont(new Font("Arial", Font.BOLD, 14));
        lblEstado.setForeground(new Color(100, 255, 100));
        gbc.insets = new Insets(0, 10, 25, 10);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(lblEstado, gbc);

        // 5. BOTONES DE NAVEGACION
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.gridwidth = 1;

        botonReintentar = new JButton("Reintentar");
        estilarBoton(botonReintentar);
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(botonReintentar, gbc);

        botonMenu = new JButton("Salir al Menu");
        estilarBoton(botonMenu);
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(botonMenu, gbc);
    }

    private void estilarBoton(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 18));
        boton.setBackground(new Color(30, 31, 44));
        boton.setForeground(new Color(255, 220, 60));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(255, 220, 60), 2));
    }

    public void mostrarPuntaje(int metros) {
        lblPuntaje.setText("Altura alcanzada: " + metros + " metros");
        campoNombre.setText("");
        campoNombre.setEnabled(true);
        botonGuardar.setEnabled(true);
        lblEstado.setText(" ");
    }

    public String getNombreIngresado() {
        return campoNombre.getText().trim();
    }

    public void mostrarConfirmacion(String msj) {
        lblEstado.setForeground(new Color(100, 255, 100)); // Verde
        lblEstado.setText(msj);
        campoNombre.setEnabled(false);
        botonGuardar.setEnabled(false);
    }

    public void mostrarError(String msj) {
        lblEstado.setForeground(new Color(255, 100, 100)); // Rojo
        lblEstado.setText(msj);
    }

    public JButton getBotonGuardar() {
        return botonGuardar;
    }

    public JTextField getCampoNombre() {
        return campoNombre;
    }

    public JButton getBotonReintentar() {
        return botonReintentar;
    }

    public JButton getBotonMenu() {
        return botonMenu;
    }
}

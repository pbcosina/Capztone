package Trackify;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class IntroScreen extends JFrame {
    public IntroScreen() {
        setTitle("Welcome to Trackify");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(220, 220, 220));

        JLabel titleLabel = new JLabel("WELCOME TO TRACKIFY!", JLabel.CENTER);
        titleLabel.setFont(new Font("Century Gothic", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        panel.add(titleLabel, BorderLayout.CENTER);

        JButton startButton = new JButton("START");
        startButton.setFont(new Font("Century Gothic", Font.PLAIN, 16));
        startButton.addActionListener(this::startProgram);
        panel.add(startButton, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    private void startProgram(ActionEvent e) {
        dispose();

        SwingUtilities.invokeLater(BudgetTrackerGUI::new);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(IntroScreen::new);
    }
}